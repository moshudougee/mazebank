package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.ClientMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientController implements Initializable {
	
	@FXML
	private BorderPane client_parent; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		// Explicitly set a default value for the selected menu item on initialization
		Model.getInstance().getViewFactory().getClientSelectedMenuItem().setValue(ClientMenuOptions.DASHBOARD);
		
		// Add listener for menu item changes
		Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
			switch (newValue){
			case DASHBOARD: {
				client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
				break;
			}
			case TRANSACTIONS: {
				client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
				break;
			}
			case ACCOUNTS: {
				client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView());
				break;
			}
			case LOGOUT: {
				logout();
				break;
			}
			default:
				client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
				break;
			}
		});
	}
	
	private void logout() {
		Stage stage = (Stage)client_parent.getScene().getWindow();
		
		//Model.getInstance().setClientLoginSuccessFlag(false);
		Model.getInstance().resetSession();
		Model.getInstance().getViewFactory().resetViews();
		Model.getInstance().getViewFactory().showLoginWindow(stage);
		//Model.getInstance().getViewFactory().closeStage(stage);
	}

}
