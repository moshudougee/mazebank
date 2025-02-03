package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.AdminMenuOptions;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class AdminMenuController implements Initializable {
	
	@FXML
	private FontAwesomeIconView create_client_icon, clients_icon, deposit_icon, profile_icon;
	@FXML
	private Button create_client_btn, clients_btn, deposit_btn, profile_btn, logout_btn, report_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		addListeners();
		create_client_icon.getStyleClass().add("icon_blue");
		create_client_btn.getStyleClass().add("text_blue");
	}
	
	private void addListeners() {
		create_client_btn.setOnAction(event -> {
			resetStyles();
			create_client_icon.getStyleClass().setAll("icon_blue");
			create_client_btn.getStyleClass().setAll("button_dyn", "text_blue");
			
			onCreateClient();
			});
		clients_btn.setOnAction(event -> {
			resetStyles();
			clients_icon.getStyleClass().setAll("icon_blue");
			clients_btn.getStyleClass().setAll("button_dyn", "text_blue");
			
			onClients();
			});
		deposit_btn.setOnAction(event -> {
			resetStyles();
			deposit_icon.getStyleClass().setAll("icon_blue");
			deposit_btn.getStyleClass().setAll("button_dyn", "text_blue");
			
			onDeposit();
			});
		logout_btn.setOnAction(event -> onLogout());
	}
	
	private void onCreateClient() {
		Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
	}
	
	private void onClients() {
		Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
	}
	
	private void onDeposit() {
		Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
	}
	
	private void onLogout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You are about to logout!!");
		alert.setContentText("Do you want to proceed? :");
		if (alert.showAndWait().get() == ButtonType.OK)
			Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.LOGOUT);
		
	}
	
	private void resetStyles() {
		// Set all icons and buttons to inactive
		create_client_icon.getStyleClass().setAll("icon_inactive");
		clients_icon.getStyleClass().setAll("icon_inactive");
		deposit_icon.getStyleClass().setAll("icon_inactive");
		
		create_client_btn.getStyleClass().setAll("button_dyn", "text_inactive");
		clients_btn.getStyleClass().setAll("button_dyn", "text_inactive");
		deposit_btn.getStyleClass().setAll("button_dyn", "text_inactive");
	}

}
