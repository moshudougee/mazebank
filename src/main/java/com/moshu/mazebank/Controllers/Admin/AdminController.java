package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.AdminMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminController implements Initializable {
	
	@FXML
	private BorderPane admin_parent;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		// Explicitly set a default value for the selected menu item on initialization
	    Model.getInstance().getViewFactory().getAdminSelectedMenuItem().setValue(AdminMenuOptions.CREATE_CLIENT);
	    
	    // Add listener for menu item changes
		Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
			switch (newValue){
			case CREATE_CLIENT: {
				admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
				break;
			}
			case CLIENTS: {
				admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
				break;
			}
			case DEPOSIT: {
				admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositView());
				break;
			}
			case LOGOUT: {
				logout();
				break;
			}
			default:
				admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
				break;
			}
		});
	}
	
	private void logout() {
		Stage stage = (Stage)admin_parent.getScene().getWindow();
		
		//Model.getInstance().setAdminLoginSuccessFlag(false);
		Model.getInstance().resetSession();
		Model.getInstance().getViewFactory().resetViews();
		Model.getInstance().getViewFactory().showLoginWindow(stage);
		//Model.getInstance().getViewFactory().closeStage(stage);
	}

}
