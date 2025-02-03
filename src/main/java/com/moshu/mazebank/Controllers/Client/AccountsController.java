package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AccountsController implements Initializable {
	
	@FXML
	private Label ch_acc_num, transaction_limit, ch_acc_date, ch_acc_bal, sv_acc_num, withdrawal_limit, sv_acc_date, sv_acc_bal, error_to_ch_lbl, error_to_sv_lbl;
	@FXML
	private TextField amount_to_sv, amount_to_ch;
	@FXML
	private Button trans_to_sv_btn, trans_to_ch_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		String checkingAccNumber = Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty().getValue();
		double transLimit = Double.valueOf(Model.getInstance().getClient().getTransactionLimit());
		String transactionLimit = Model.getInstance().getCurrencyFormat(transLimit);
		String date = String.valueOf(Model.getInstance().getClient().dateProperty().getValue());
		double chBal = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
		String chBalString = Model.getInstance().getCurrencyFormat(chBal);
		
		String savingsAccNumber = Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty().getValue();
		double withDLimit = Model.getInstance().getClient().getWithdrawalLimit();
		String withdrawalLimit = Model.getInstance().getCurrencyFormat(withDLimit);
		double svBal = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
		String svBalString = Model.getInstance().getCurrencyFormat(svBal);
		
		ch_acc_num.setText(checkingAccNumber);
		transaction_limit.setText(transactionLimit);
		ch_acc_date.setText(date);
		ch_acc_bal.setText(chBalString);
		
		sv_acc_num.setText(savingsAccNumber);
		withdrawal_limit.setText(withdrawalLimit);
		sv_acc_date.setText(date);
		sv_acc_bal.setText(svBalString);
		
		amount_to_sv.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					amount_to_sv.setText(oldValue);
					error_to_sv_lbl.getStyleClass().setAll("error_msg");
					error_to_sv_lbl.setText("Amount should only be numbers!");
				}
			}
		});
		
		trans_to_sv_btn.setOnAction(event -> transferToSavings());
		
		amount_to_ch.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					amount_to_ch.setText(oldValue);
					error_to_ch_lbl.getStyleClass().setAll("error_msg");
					error_to_ch_lbl.setText("Amount should only be numbers!");
				}
			}
		});
		
		trans_to_ch_btn.setOnAction(event -> transferToChecking());
		
	}
	
	public void transferToSavings() {
		String amountString = amount_to_sv.getText();
		
		if (amountString.isEmpty()) {
			error_to_sv_lbl.getStyleClass().setAll("error_msg");
			error_to_sv_lbl.setText("You must provide amount!");
			return;
		} else {
			double amount = Double.valueOf(amountString);
			double checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
			String convAmt = Model.getInstance().getCurrencyFormat(amount);
			String pAddress = Model.getInstance().getClient().pAddressProperty().getValue();
			if (amount > checkingBalance) {
				error_to_sv_lbl.getStyleClass().setAll("error_msg");
				error_to_sv_lbl.setText("Insufficient funds. Unable to send " + convAmt);
			} else if (Model.getInstance().evaluateMoveToSavings(pAddress, amount)) {
				Model.getInstance().updateChecking(pAddress);
				Model.getInstance().updateSavings(pAddress);
				
				checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
				double savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
				String chBalString = Model.getInstance().getCurrencyFormat(checkingBalance);
				String svBalString = Model.getInstance().getCurrencyFormat(savingsBalance);
				ch_acc_bal.setText(chBalString);
				sv_acc_bal.setText(svBalString);
				
				amount_to_sv.setText("");
				error_to_sv_lbl.getStyleClass().setAll("success_msg");
				error_to_sv_lbl.setText("Transfer of " + convAmt + " to Savings Account successful");
				return;
			} else {
				error_to_sv_lbl.getStyleClass().setAll("error_msg");
				error_to_sv_lbl.setText("An error occured. Contact support or try again later!");
				return;
			}
		}
	}
	
	public void transferToChecking() {
		String amountString = amount_to_ch.getText();
		
		if (amountString.isEmpty()) {
			error_to_ch_lbl.getStyleClass().setAll("error_msg");
			error_to_ch_lbl.setText("You must provide amount!");
			return;
		} else {
			double amount = Double.valueOf(amountString);
			double savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
			String convAmt = Model.getInstance().getCurrencyFormat(amount);
			String pAddress = Model.getInstance().getClient().pAddressProperty().getValue();
			if (amount > savingsBalance) {
				error_to_ch_lbl.getStyleClass().setAll("error_msg");
				error_to_ch_lbl.setText("Insufficient funds. Unable to send " + convAmt);
				return;
			} else if (Model.getInstance().evaluateMoveToChecking(pAddress, amount)) {
				Model.getInstance().updateChecking(pAddress);
				Model.getInstance().updateSavings(pAddress);
				
				savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().getValue();
				double checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().getValue();
				String chBalString = Model.getInstance().getCurrencyFormat(checkingBalance);
				String svBalString = Model.getInstance().getCurrencyFormat(savingsBalance);
				ch_acc_bal.setText(chBalString);
				sv_acc_bal.setText(svBalString);
				
				amount_to_ch.setText("");
				error_to_ch_lbl.getStyleClass().setAll("success_msg");
				error_to_ch_lbl.setText("Transfer of " + convAmt + " to Checking Account successful");
				return;
			} else {
				error_to_ch_lbl.getStyleClass().setAll("error_msg");
				error_to_ch_lbl.setText("An error occured. Contact support or try again later!");
				return;
			}
		}
	}

}
