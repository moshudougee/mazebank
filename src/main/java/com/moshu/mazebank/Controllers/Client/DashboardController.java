package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Models.Transaction;
import com.moshu.mazebank.Views.TransactionCellFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class DashboardController implements Initializable {
	
	@FXML
	private Text user_name;
	@FXML
	private Label login_date, checking_bal, checking_icc_num, savings_bal, savings_icc_num, income_lbl, expense_lbl, error_lbl;
	@FXML
	private ListView<Transaction> transaction_listview;
	@FXML
	private TextField payee_fld, amount_fld;
	@FXML
	private TextArea message_fld;
	@FXML
	private Button send_money_btn, refresh_btn;
	
	private ObservableList<Transaction> transactions;
	private double checkingBalance;
	private double savingsBalance;
	private String checkingBal;
	private String savingsBal;
	private String incomeString;
	private String expenseString;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//user_name.setText(Model.getInstance().getClient().firstNameProperty());
		Date curreDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(curreDate);
		String firstName = Model.getInstance().getClient().firstNameProperty().getValue();
		checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
		savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
		checkingBal = Model.getInstance().getCurrencyFormat(checkingBalance);
		savingsBal = Model.getInstance().getCurrencyFormat(savingsBalance);
		//System.out.println("Balance is " + checkingBalance);
		user_name.setText("Hi " + firstName);
		login_date.setText("Today, " + today);
		checking_bal.setText(checkingBal);
		savings_bal.setText(savingsBal);
		transactions = Model.getInstance().getLatestTransactions();
		//System.out.println("Transactions: ->" + transactions);
		transaction_listview.setItems(transactions);
		transaction_listview.setCellFactory(param -> new TransactionCellFactory());
		incomeString = Model.getInstance().getIncomeString();
		expenseString = Model.getInstance().getExpenseString();
		income_lbl.setText(incomeString);
		expense_lbl.setText(expenseString);
		amount_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					amount_fld.setText(oldValue);
					error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Amount should only be valid decimal numbers!");
				}
			}
			
		});
		send_money_btn.setOnAction(event -> sendMoney());
		refresh_btn.setOnAction(event -> refresh());
	}
	
	public void sendMoney() {
		String pAddress = payee_fld.getText();
		String amtString = amount_fld.getText();
		String message = message_fld.getText();
		
		if (pAddress.isEmpty() || amtString.isEmpty() || message.isEmpty()) {
			error_lbl.getStyleClass().setAll("error_msg");
			error_lbl.setText("You must provide Payee Address, Amount and Message!");
		} else if (Model.getInstance().checkValidClient(pAddress)) {
			double senderBal = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
			double amount = Double.valueOf(amtString);
			String convAmt = Model.getInstance().getCurrencyFormat(amount);
			if (amount > senderBal) {
				error_lbl.getStyleClass().setAll("error_msg");
				error_lbl.setText("Insufficient funds. Unable to send " + convAmt);
				return;
			} else if (Model.getInstance().evaluateSendMoney(pAddress, message, amount, senderBal)) {
				String myAddress = Model.getInstance().getClient().pAddressProperty().getValue();
				Model.getInstance().updateChecking(myAddress);
				Model.getInstance().evaluateLatestTransactions(myAddress);
				Model.getInstance().evaluateTransactions(myAddress);
				
				double newBal = senderBal - amount;
				String newBalString = Model.getInstance().getCurrencyFormat(newBal);
				incomeString = Model.getInstance().getIncomeString();
				expenseString = Model.getInstance().getExpenseString();
				
				income_lbl.setText(incomeString);
				expense_lbl.setText(expenseString);
				error_lbl.getStyleClass().setAll("success_msg");
				error_lbl.setText(convAmt + " sent to " + pAddress + " successfully");
				checking_bal.setText(newBalString);
				
				// Clear the input fields after successful transaction
				payee_fld.setText("");
				amount_fld.setText("");
				message_fld.setText("");
				
				
			} else {
				error_lbl.getStyleClass().setAll("error_msg");
				error_lbl.setText("An error occured. Contact support or try again later!");
				return;
			}
		} else {
			error_lbl.getStyleClass().setAll("error_msg");
			error_lbl.setText("Please provide a valid Payee Address");
			return;
		}
	}
	
	public void refresh() {
		
		String pAddress = Model.getInstance().getClient().pAddressProperty().getValue();
		Model.getInstance().evaluateLatestTransactions(pAddress);
		Model.getInstance().evaluateTransactions(pAddress);
		checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
		savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
		checkingBal = Model.getInstance().getCurrencyFormat(checkingBalance);
		savingsBal = Model.getInstance().getCurrencyFormat(savingsBalance);
		incomeString = Model.getInstance().getIncomeString();
		expenseString = Model.getInstance().getExpenseString();
		
		income_lbl.setText(incomeString);
		expense_lbl.setText(expenseString);
		checking_bal.setText(checkingBal);
		savings_bal.setText(savingsBal);
		
		// Update the transactions list
		transactions = Model.getInstance().getLatestTransactions();
		transaction_listview.setItems(transactions);
		transaction_listview.setCellFactory(param -> new TransactionCellFactory());
	}
	
	

}
