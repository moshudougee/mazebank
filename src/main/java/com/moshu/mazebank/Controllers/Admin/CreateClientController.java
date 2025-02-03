package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateClientController implements Initializable {
	
	@FXML
	private TextField fname_fld, lname_fld, password_fld, ch_amount_fld, sv_amount_fld, ch_number_fld, sv_number_fld;
	@FXML
	private CheckBox paddress_box, ch_acc_box, sv_acc_box;
	@FXML
	private Label paddress_lbl, error_lbl;
	@FXML
	private Button create_client_btn;
	
	private String firstName, lastName, password, chAccNumber, svAccNumber, pAddress;
	private double chAccBalance, svAccBalance;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		// Listener for paddress_box checkbox
		pAddress = "";
		chAccBalance = 0;
		svAccBalance = 0;
		paddress_box.setOnAction(event -> handlePAddressBox());
		// Listener for ch_acc_box checkbox
		ch_acc_box.setOnAction(event -> handleCheckingAccountBox());
		// Listener for sv_acc_box checkbox
		sv_acc_box.setOnAction(event -> handleSavingsAccountBox());
		
		ch_amount_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					ch_amount_fld.setText(oldValue);
					error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Amount should only be valid decimal numbers!");
				}
				
			}
		});
		
		sv_amount_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					sv_amount_fld.setText(oldValue);
					error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Amount should only be valid decimal numbers!");
				}
				
			}
		});
		
		ch_number_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*")) {
		            ch_number_fld.setText(newValue.replaceAll("[^\\d]", ""));
		            error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Checking Account Number should be numbers only!");
		        }
				
			}
		});
		
		sv_number_fld.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (!newValue.matches("\\d*")) {
		            ch_number_fld.setText(newValue.replaceAll("[^\\d]", ""));
		            error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Savings Account Number should be numbers only!");
		        }
				
			}
		});
		
		create_client_btn.setOnAction(event -> createClient());
		
	}
	
	// Method to handle paddress_box checkbox
		private void handlePAddressBox() {
			if (paddress_box.isSelected()) {
				firstName = fname_fld.getText();
				lastName = lname_fld.getText();
				
				if (!firstName.isEmpty() && !lastName.isEmpty()) {
					// Generate pAddress in the format @bBaker1
					Random random = new Random();
					int randomDigit = random.nextInt(9) + 1; // Generates a random number between 1-9
					pAddress = "@" + firstName.toLowerCase().charAt(0) + lastName + randomDigit;
					paddress_lbl.setText(pAddress);
				} else {
					error_lbl.getStyleClass().setAll("error_msg");
					error_lbl.setText("Please enter both first and last names.");
				}
			} else {
				paddress_lbl.setText(""); // Clear the pAddress label if checkbox is unchecked
			}
		}

		// Method to handle ch_acc_box checkbox
		private void handleCheckingAccountBox() {
			boolean isChecked = ch_acc_box.isSelected();
			ch_amount_fld.setDisable(!isChecked); // Enable or disable checking amount field
			ch_number_fld.setDisable(!isChecked); // Enable or disable checking number field
		}

		// Method to handle sv_acc_box checkbox
		private void handleSavingsAccountBox() {
			boolean isChecked = sv_acc_box.isSelected();
			sv_amount_fld.setDisable(!isChecked); // Enable or disable savings amount field
			sv_number_fld.setDisable(!isChecked); // Enable or disable savings number field
		}
		
		private void createClient() {
		    firstName = fname_fld.getText();
		    lastName = lname_fld.getText();
		    password = password_fld.getText();
		    String chAccBalString = ch_amount_fld.getText();
		    chAccNumber = ch_number_fld.getText();
		    String svAccBalString = sv_amount_fld.getText();
		    svAccNumber = sv_number_fld.getText();

		    // Validate that all required fields are filled
		    if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || chAccBalString.isEmpty() ||
		        chAccNumber.isEmpty() || svAccBalString.isEmpty() || svAccNumber.isEmpty() || pAddress.isEmpty()) {
		        
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Please enter all required information, including checking account, savings account, and generate pAddress.");
		        return;  // Stop execution here if validation fails
		    }

		    try {
		        chAccBalance = Double.valueOf(chAccBalString);  // This might throw the NumberFormatException if input is invalid
		        svAccBalance = Double.valueOf(svAccBalString);
		    } catch (NumberFormatException e) {
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Invalid balance format! Please enter valid decimal numbers for account balances.");
		        return;  // Stop execution here if NumberFormatException occurs
		    }

		    // Add prefixes to account numbers
		    chAccNumber = "1120 " + chAccNumber;
		    svAccNumber = "1109 " + svAccNumber;

		    // Validate Payee Address, Checking Account, and Savings Account
		    if (Model.getInstance().checkValidClient(pAddress)) {
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Payee Address exists. Generate a new one.");
		        return;
		    }

		    if (Model.getInstance().checkValidChecking(chAccNumber)) {
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Checking Account exists. Create a new one.");
		        return;
		    }

		    if (Model.getInstance().checkValidSavings(svAccNumber)) {
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Savings Account exists. Create a new one.");
		        return;
		    }

		    // Evaluate and create the client
		    if (Model.getInstance().evaluateCreateClient(firstName, lastName, password, chAccNumber, svAccNumber, pAddress, chAccBalance, svAccBalance)) {
		        error_lbl.getStyleClass().setAll("success_msg");
		        error_lbl.setText("Client " + pAddress + " created successfully!");

		        // Clear the fields after successful creation
		        fname_fld.setText("");
		        lname_fld.setText("");
		        password_fld.setText("");
		        ch_amount_fld.setText("");
		        ch_number_fld.setText("");
		        sv_amount_fld.setText("");
		        sv_number_fld.setText("");
		    } else {
		        error_lbl.getStyleClass().setAll("error_msg");
		        error_lbl.setText("Internal server error. Try again later or contact support!");
		    }
		}
}
