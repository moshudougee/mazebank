package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.ClientMenuOptions;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


public class ClientMenuController implements Initializable {
	
	@FXML
	private FontAwesomeIconView dashboard_icon, transaction_icon, accounts_icon, profile_icon;
	@FXML
	private Button dashboard_btn, transaction_btn, accounts_btn, profile_btn, logout_btn, report_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		addListeners();
		dashboard_icon.getStyleClass().add("icon_blue");
		dashboard_btn.getStyleClass().add("text_blue");
		
		
	}
	
	private void addListeners() {
		dashboard_btn.setOnAction(event -> {
			resetStyles(); // Remove all active styles first
			dashboard_icon.getStyleClass().setAll("icon_blue");
			dashboard_btn.getStyleClass().setAll("button_dyn", "text_blue");
			//dashboard_btn.setStyle("-fx-text-fill: blue;");
			onDashboard();
			
			});
		transaction_btn.setOnAction(event -> {
			resetStyles(); // Remove all active styles first
			transaction_icon.getStyleClass().setAll("icon_blue");
			transaction_btn.getStyleClass().setAll("button_dyn", "text_blue");
			//transaction_btn.setStyle("-fx-text-fill: blue;");
			onTransactions();
			
			});
		accounts_btn.setOnAction(event -> {
			resetStyles(); // Remove all active styles first
			accounts_icon.getStyleClass().setAll("icon_blue");
			accounts_btn.getStyleClass().setAll("button_dyn", "text_blue");
			//accounts_btn.setStyle("-fx-text-fill: blue;");
			onAccounts();
			
			});
		logout_btn.setOnAction(event -> onLogout());
	}
	
	private void onDashboard() {
		Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
		
	}
	
	private void onTransactions() {
		Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
		
	}
	
	private void onAccounts() {
		Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
		
	}
	
	private void onLogout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You are about to logout!!");
		alert.setContentText("Do you want to proceed? :");
		if (alert.showAndWait().get() == ButtonType.OK)
			Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.LOGOUT);
	}
	
	// Method to reset all styles back to the inactive state
	private void resetStyles() {
	    // Remove active styles
		/**
	    dashboard_icon.getStyleClass().removeAll("icon_blue", "text_blue");
	    transaction_icon.getStyleClass().removeAll("icon_blue", "text_blue");
	    accounts_icon.getStyleClass().removeAll("icon_blue", "text_blue");

	    dashboard_btn.getStyleClass().removeAll("text_blue");
	    transaction_btn.getStyleClass().removeAll("text_blue");
	    accounts_btn.getStyleClass().removeAll("text_blue");
        **/
	    // Set all icons and buttons to inactive
	    dashboard_icon.getStyleClass().setAll("icon_inactive");
	    transaction_icon.getStyleClass().setAll("icon_inactive");
	    accounts_icon.getStyleClass().setAll("icon_inactive");

	    dashboard_btn.getStyleClass().setAll("button_dyn", "text_inactive");
	    transaction_btn.getStyleClass().setAll("button_dyn", "text_inactive");
	    accounts_btn.getStyleClass().setAll("button_dyn", "text_inactive");
	}

}
