package com.moshu.mazebank.Views;



import com.moshu.mazebank.Controllers.Admin.AdminController;
import com.moshu.mazebank.Controllers.Client.ClientController;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
	
	private AccountType loginAccountType;
	private DepositType depositAccounType;
	private Stage currentStage;
	
	// Client Views
	private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
	private AnchorPane dashboardView;
	private AnchorPane transactionsView;
	private AnchorPane accountsView;
	// Admin Views
	private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
	private AnchorPane createClientView;
	private AnchorPane clientsView;
	private AnchorPane depositView;
	
	public ViewFactory () {
		this.loginAccountType = AccountType.CLIENT;
		this.depositAccounType = DepositType.CHECKING;
		this.clientSelectedMenuItem = new SimpleObjectProperty<>();
		this.adminSelectedMenuItem = new SimpleObjectProperty<>();
	}
	
	public AccountType getLoginAccountType() {
		return loginAccountType;
	}
	
	public DepositType getDepositAccounType() {
		return depositAccounType;
	}

	public void setLoginAccountType(AccountType loginAccountType) {
		this.loginAccountType = loginAccountType;
	}
	
	public void setDepositAccountType(DepositType depositAccounType) {
		this.depositAccounType = depositAccounType;
	}
	
	public void resetViews() {
	    dashboardView = null;
	    transactionsView = null;
	    accountsView = null;
	    createClientView = null;
	    clientsView = null;
	    depositView = null;
	}

	
	// Client View Section
	
	public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
		
		return clientSelectedMenuItem;
		
	}
	@SuppressWarnings("exports")
	public AnchorPane getDashboardView() {
		
		if (dashboardView == null) {
			try {
				dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return dashboardView;
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getTransactionsView() {
		
		if (transactionsView == null) {
			try {
				transactionsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transactions.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return transactionsView;
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getAccountsView() {
		
		if (accountsView == null) {
			try {
				accountsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Accounts.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return accountsView;
	}
	
	public void showClientWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
		ClientController clientController = new ClientController();
		loader.setController(clientController);
		createStage(loader);
	}
	
	// Admin View Section
	
	public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
		return adminSelectedMenuItem;
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getCreateClientView() {
		if (createClientView == null) {
			try {
				createClientView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CreateClient.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return createClientView;
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getClientsView() {
		if (clientsView == null) {
			try {
				clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return clientsView;
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getDepositView() {
		if (depositView == null) {
			try {
				depositView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposit.fxml")).load();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return depositView;
	}
	
	public void showAdminWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
		AdminController adminController = new AdminController();
		loader.setController(adminController);
		createStage(loader);
	}
	
	public void showLoginWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
		createStage(loader);
	}
	
	@SuppressWarnings("exports")
	public void showLoginWindow(Stage currentStage) {
		// Close current stage before showing the login window
	    if (currentStage != null) {
	        closeStage(currentStage);
	    }
	    
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
		createStage(loader);
	}
	
	@SuppressWarnings("exports")
	public void createStage(FXMLLoader loader) {
		try {
	        Scene scene = new Scene(loader.load());
	        Stage stage = new Stage();
	        Image icon = new Image(String.valueOf(getClass().getResource("/Images/maze-bank.png")));
	        stage.getIcons().add(icon);
	        stage.setResizable(false);
	        stage.setScene(scene);
	        stage.setTitle("Maze Bank");
	        
	        // Close the old stage if it's still open
	        if (currentStage != null) {
	            closeStage(currentStage);
	        }
	        
	        stage.show();
	        currentStage = stage; // Track the new stage
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("exports")
	public void closeStage(Stage stage) {
		
		stage.close();
	}


}
