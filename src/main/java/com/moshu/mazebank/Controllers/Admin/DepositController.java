package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.DepositType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class DepositController implements Initializable {
	
	@FXML
	private TextField paddress_fld, amount_fld;
	@FXML
	private Button search_btn, deposit_btn;
	@FXML
	private ChoiceBox<DepositType> deposit_type;
	@FXML
	private ListView<String> result_listview;
	@FXML
	private Label search_err_lbl, paddress_lbl, deposit_err_lbl;
	
	private ObservableList<String> depositList = FXCollections.observableArrayList();
	private String pAddress = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		paddress_lbl.setText(pAddress);
		
		deposit_type.setItems(FXCollections.observableArrayList(DepositType.CHECKING, DepositType.SAVINGS));
		deposit_type.setValue(Model.getInstance().getViewFactory().getDepositAccounType());
		deposit_type.valueProperty().addListener(observable -> {
			Model.getInstance().getViewFactory().setDepositAccountType(deposit_type.getValue());
		});
		
		amount_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					amount_fld.setText(oldValue);
					deposit_err_lbl.getStyleClass().setAll("deposit_error_lbl");
					deposit_err_lbl.setText("Amount should only be valid decimal numbers!");
				}
				
			}
		});
		
		result_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String selectedString = result_listview.getSelectionModel().getSelectedItem();
				if(selectedString != "No results found!") 
					pAddress = selectedString;
				
				paddress_lbl.setText(pAddress);
				
			}
		});
		
		search_btn.setOnAction(event -> onSearch());
		deposit_btn.setOnAction(event -> onDeposit());
		
	}
	
	public void onSearch() {
		try {
			String searchTerm = paddress_fld.getText();
			if (searchTerm.isEmpty()) {
				depositList.clear();
				depositList.add("No results found!");
				result_listview.setItems(depositList);
				search_err_lbl.setText("Please provide a search term!");
				return;
			} else {
				Model.getInstance().evaluateDepositList(searchTerm);
				depositList = Model.getInstance().getDepositList();
				
				// Check if the list is empty
		        if (depositList.isEmpty()) {
		            depositList.add("No results found!");
		        }
		        
				result_listview.setItems(depositList);
				search_err_lbl.setText("");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("An error occured searching Payee Address");
			e.printStackTrace();
		}
		
	}
	
	public void onDeposit() {
		try {
			String amountString = amount_fld.getText();
			if (amountString.isEmpty() || pAddress.isEmpty()) {
				deposit_err_lbl.getStyleClass().setAll("deposit_error_lbl");
				deposit_err_lbl.setText("You must select a valid pAddress and provide amount!");
				return;
			} else {
				double amount = Double.valueOf(amountString);
				String convAmt = Model.getInstance().getCurrencyFormat(amount);
				if (Model.getInstance().getViewFactory().getDepositAccounType() == DepositType.CHECKING) {
					if (Model.getInstance().evaluateDepositToChecking(pAddress, amount)) {
						deposit_err_lbl.getStyleClass().setAll("deposit_success_lbl");
						deposit_err_lbl.setText("Deposit of " + convAmt + " to " + pAddress + "'s checking account successfull");
						paddress_lbl.setText("");
						paddress_fld.setText("");
						amount_fld.setText("");
						depositList.clear();
					} else {
						deposit_err_lbl.getStyleClass().setAll("deposit_error_lbl");
						deposit_err_lbl.setText("An error occured try again later or contact support!");
					}
				} else {
					if (Model.getInstance().evaluateDepositToSavings(pAddress, amount)) {
						deposit_err_lbl.getStyleClass().setAll("deposit_success_lbl");
						deposit_err_lbl.setText("Deposit of " + convAmt + " to " + pAddress + "'s savings account successfull");
						paddress_lbl.setText("");
						paddress_fld.setText("");
						amount_fld.setText("");
						depositList.clear();
					} else {
						deposit_err_lbl.getStyleClass().setAll("deposit_error_lbl");
						deposit_err_lbl.setText("An error occured try again later or contact support!");
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
