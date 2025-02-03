package com.moshu.mazebank.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.AccountType;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	@FXML
	private AnchorPane login_pane;
	@FXML
	private ChoiceBox<AccountType> account_selector;
	@FXML
	private Label payee_address_lbl, password_lbl, error_lbl;
	@FXML
	private TextField payee_address_fld, password_fld;
	@FXML
	private Button login_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
			payee_address_lbl.setText("Payee Address:");
		}else {
			payee_address_lbl.setText("Username:");
		}
		account_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
		account_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
		account_selector.valueProperty().addListener(observable -> {
			Model.getInstance().getViewFactory().setLoginAccountType(account_selector.getValue());
			if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
				payee_address_lbl.setText("Payee Address:");
			} else {
				payee_address_lbl.setText("Username:");
			}
			});
		
		
		
		login_btn.setOnAction(event ->  onLogin());
		error_lbl.setText(null);
		
	}
	
	private void onLogin() {
		Stage stage = (Stage)login_pane.getScene().getWindow();
		
		if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
			// Evaluate Client Login Credentials
			String pAddress = payee_address_fld.getText();
			String password = password_fld.getText();
			if (pAddress == "" || password == "") {
				error_lbl.setText("You must provide Payee Address and Password");
				return;
			}
			Model.getInstance().evaluateClientCred(pAddress, password);
			if (Model.getInstance().getClientLoginSuccessFlag()) {
				Model.getInstance().evaluateTransactions(pAddress);
				Model.getInstance().evaluateLatestTransactions(pAddress);
				Model.getInstance().getViewFactory().showClientWindow();
				// Close the login stage
				Model.getInstance().getViewFactory().closeStage(stage);
			} else {
				payee_address_fld.setText("");
				password_fld.setText("");
				error_lbl.setText("No Such Login Credentials");
			}
			
		} else {
			String username = payee_address_fld.getText();
			String password = password_fld.getText();
			
			if (username.isEmpty() || password.isEmpty()) {
				error_lbl.setText("You must provide Username and Password");
				return;
			}
			Model.getInstance().evaluateAdminCred(username, password);
			// Admin login success
			if (Model.getInstance().getAdminLoginSuccessFlag()) {
				Model.getInstance().evaluateClients();
				Model.getInstance().getViewFactory().showAdminWindow();
				Model.getInstance().getViewFactory().closeStage(stage);
			} else {
				payee_address_fld.setText("");
				password_fld.setText("");
				error_lbl.setText("No Such Login Credentials");
			}
			
		}
		
		
	}
	
	

}
