package com.moshu.mazebank.Models;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.LocalDate;

import com.moshu.mazebank.Views.AccountType;
import com.moshu.mazebank.Views.ViewFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private final ViewFactory viewFactory;
	private final DatabaseDriver databaseDriver;
	private static Model model;
	private AccountType logiAccountType =  AccountType.CLIENT;
	// Client Data Section
	private final Client client;
	private final Admin admin;
	private boolean clientLoginSuccessFlag;
	private boolean adminLoginSuccessFlag;
	private ObservableList<Transaction> transactions;
	private ObservableList<Transaction> latestTransactions;
	private ObservableList<Client> clients;
	private ObservableList<String> depositList;
	private String incomeString;
	private String expenseString;
	
	// Admin Data Section
	
	private Model() {
		this.viewFactory = new ViewFactory();
		this.databaseDriver = new DatabaseDriver();
		// Client Data Section
		this.clientLoginSuccessFlag = false;
		this.adminLoginSuccessFlag = false;
		this.client = new Client(null, null, null, null, null, null);
		this.admin = new Admin(null);
		this.transactions = FXCollections.observableArrayList();
		this.latestTransactions = FXCollections.observableArrayList();
		this.clients = FXCollections.observableArrayList();
		this.depositList = FXCollections.observableArrayList();
		this.incomeString = "";
		this.expenseString = "";
		// Admin Data Section
	}
	
	public static synchronized Model getInstance() {
		if(model == null) {
			model = new Model();
		}
		
		return model;
	}
	
	public ViewFactory getViewFactory() {
		
		return viewFactory;
	}
	
	public AccountType getLoginAccountType() {
		return logiAccountType;
	}
	
	public void setLoginAccountType(AccountType logiAccountType) {
		this.logiAccountType = logiAccountType;
	}
	
	public ObservableList<Transaction> getTransactions() {
		return transactions;
	}
	
	public ObservableList<Transaction> getLatestTransactions() {
		return latestTransactions;
	}
	
	public ObservableList<Client> getClients() {
		return clients;
	}
	
	public ObservableList<String> getDepositList() {
		return depositList;
	}
	
	public String getIncomeString() {
		return incomeString;
	}
	
	public String getExpenseString() {
		return expenseString;
	}
	
	public String getCurrencyFormat(double amount) {
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		String result = currencyFormatter.format(amount);
		return result;
	}
	
	public boolean checkValidClient(String pAddress) {
		return databaseDriver.checkClient(pAddress);
	}
	
	public boolean checkValidChecking(String accountNumber) {
		return databaseDriver.checkChecking(accountNumber);
	}
	
	public boolean checkValidSavings(String accountNumber) {
		return databaseDriver.checkSavings(accountNumber);
	}
	
	public void resetSession() {
	    clientLoginSuccessFlag = false;
	    adminLoginSuccessFlag = false;
	    transactions.clear();
	    latestTransactions.clear();
	    incomeString = "";
	    expenseString = "";
	    
	}
	
	/***
	 * Client Method Section
	 * */
	public boolean getClientLoginSuccessFlag() {return this.clientLoginSuccessFlag;}
	
	public void setClientLoginSuccessFlag(boolean flag) {this.clientLoginSuccessFlag = flag;}
	
	public Client getClient() {return client;}
	
	public void evaluateClientCred(String pAddress, String password) {
		CheckingAccount checkingAccount;
		SavingsAccount savingsAccount;
		ResultSet resultSet = databaseDriver.getClientData(pAddress, password);
		try {
			if (resultSet.isBeforeFirst()) {
				this.client.firstNameProperty().set(resultSet.getString("FirstName"));
				this.client.lastNameProperty().set(resultSet.getString("LastName"));
				this.client.pAddressProperty().set(resultSet.getString("PayeeAddress"));
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				this.client.dateProperty().set(date);
				this.clientLoginSuccessFlag = true;
				ResultSet checkingSet = databaseDriver.getCheckingAccount(resultSet.getString("PayeeAddress"));
				ResultSet savingsSet = databaseDriver.getSavingsAccount(resultSet.getString("PayeeAddress"));
				if (checkingSet.isBeforeFirst()) {
					String owner = checkingSet.getString("Owner");
					String accountNumber = checkingSet.getString("AccountNumber");
					int tLimit = (int)checkingSet.getDouble("TransactionLimit");
					double balance = checkingSet.getDouble("Balance");
					checkingAccount = new CheckingAccount(owner, accountNumber, balance, tLimit);
					this.client.checkingAccountProperty().set(checkingAccount);
				}
				if (savingsSet.isBeforeFirst()) {
					String owner = savingsSet.getString("Owner");
					String accountNumber = savingsSet.getString("AccountNumber");
					double balance = savingsSet.getDouble("Balance");
					double withdrawalLimit = savingsSet.getDouble("WithdrawalLimit");
					savingsAccount = new SavingsAccount(owner, accountNumber, balance, withdrawalLimit);
					this.client.savingsAccountProperty().set(savingsAccount);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void evaluateTransactions(String pAddress) {
		
		try {
			this.transactions.clear();
			ResultSet resultSet = databaseDriver.getTransactions(pAddress);
			if (resultSet.isBeforeFirst()) {
				double income = 0;
				double expense = 0;
				while (resultSet.next()) {
					String sender = resultSet.getString("Sender");
					String receiver = resultSet.getString("Receiver");
					double amount = resultSet.getDouble("Amount");
					LocalDate date = LocalDate.parse(resultSet.getString("Date"));
					String message = resultSet.getString("Message");
					Transaction transaction = new Transaction(sender, receiver, amount, date, message);
					this.transactions.add(transaction);
					if (receiver.equalsIgnoreCase(pAddress)) {
						income += amount;
					}else if (sender.equalsIgnoreCase(pAddress)) {
						expense += amount;
					}
				}
				this.incomeString = getCurrencyFormat(income);
				this.expenseString = getCurrencyFormat(expense);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void evaluateLatestTransactions(String pAddress) {
		
		try {
			this.latestTransactions.clear();
			ResultSet resultSet = databaseDriver.getLatestTransactions(pAddress);
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					String sender = resultSet.getString("Sender");
					String receiver = resultSet.getString("Receiver");
					double amount = resultSet.getDouble("Amount");
					LocalDate date = LocalDate.parse(resultSet.getString("Date"));
					String message = resultSet.getString("Message");
					Transaction transaction = new Transaction(sender, receiver, amount, date, message);
					this.latestTransactions.add(transaction);
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void updateChecking(String pAddress) {
		try {
			CheckingAccount checkingAccount;
			ResultSet checkingSet = databaseDriver.getCheckingAccount(pAddress);
			if (checkingSet.isBeforeFirst()) {
				String owner = checkingSet.getString("Owner");
				String accountNumber = checkingSet.getString("AccountNumber");
				int tLimit = (int)checkingSet.getDouble("TransactionLimit");
				double balance = checkingSet.getDouble("Balance");
				checkingAccount = new CheckingAccount(owner, accountNumber, balance, tLimit);
				this.client.checkingAccountProperty().set(checkingAccount);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void updateSavings(String pAddress) {
		try {
			SavingsAccount savingsAccount;
			ResultSet savingsSet = databaseDriver.getSavingsAccount(pAddress);
			if (savingsSet.isBeforeFirst()) {
				String owner = savingsSet.getString("Owner");
				String accountNumber = savingsSet.getString("AccountNumber");
				double balance = savingsSet.getDouble("Balance");
				double withdrawalLimit = savingsSet.getDouble("WithdrawalLimit");
				savingsAccount = new SavingsAccount(owner, accountNumber, balance, withdrawalLimit);
				this.client.savingsAccountProperty().set(savingsAccount);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean evaluateSendMoney(String receiver, String message, double amount, double senderBal) {
		boolean sent = false;
		try {
			String sender = this.client.pAddressProperty().getValue();
			
			int count = databaseDriver.sendMoney(sender, receiver, message, amount, senderBal);
			if (count > 0)
				sent = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return sent;
	}
	
	public boolean evaluateMoveToChecking(String pAddress, double amount) {
		boolean moved = false;
		try {
			int count = databaseDriver.transferToChecking(pAddress, amount);
			if (count > 0)
				moved = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return moved;
	}
	
	public boolean evaluateMoveToSavings(String pAddress, double amount) {
		boolean moved = false;
		try {
			int count = databaseDriver.transferToSavings(pAddress, amount);
			if (count > 0)
				moved = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return moved;
	}
	
	/***
	 * Admin Method Section
	 * */
	public boolean getAdminLoginSuccessFlag() { return this.adminLoginSuccessFlag; }
	
	public void setAdminLoginSuccessFlag(boolean flag) { this.adminLoginSuccessFlag = flag; }
	
	public void evaluateAdminCred(String username, String password) {
		try {
			ResultSet resultSet = databaseDriver.getAdminData(username, password);
			if (resultSet.isBeforeFirst()) {
				this.admin.userNameProperty().set(resultSet.getString("Username"));
				this.adminLoginSuccessFlag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean evaluateCreateClient(String firstName, String lastName, String password, String chAccNumber, String svAccNumber, 
										 String pAddress, double chAccBalance, double svAccBalance) {
		boolean create = false;
		try {
			int count = databaseDriver.createClient(firstName, lastName, password, chAccNumber, svAccNumber, pAddress, chAccBalance, svAccBalance);
			if (count > 0) 
				create = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return create;
	}
	
	public void evaluateClients() {
		try {
			this.clients.clear();
			ResultSet resultSet = databaseDriver.getClients();
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					CheckingAccount checkingAccount = null;
					SavingsAccount savingsAccount = null;
					String firstName = resultSet.getString("FirstName");
					String lastName = resultSet.getString("LastName");
					String pAddress = resultSet.getString("PayeeAddress");
					String[] dateParts = resultSet.getString("Date").split("-");
					LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
					ResultSet checkingSet = databaseDriver.getCheckingAccount(pAddress);
					ResultSet savingsSet = databaseDriver.getSavingsAccount(pAddress);
					if (checkingSet.isBeforeFirst()) {
						String owner = checkingSet.getString("Owner");
						String accountNumber = checkingSet.getString("AccountNumber");
						int tLimit = (int)checkingSet.getDouble("TransactionLimit");
						double balance = checkingSet.getDouble("Balance");
						checkingAccount = new CheckingAccount(owner, accountNumber, balance, tLimit);
					}
					if (savingsSet.isBeforeFirst()) {
						String owner = savingsSet.getString("Owner");
						String accountNumber = savingsSet.getString("AccountNumber");
						double balance = savingsSet.getDouble("Balance");
						double withdrawalLimit = savingsSet.getDouble("WithdrawalLimit");
						savingsAccount = new SavingsAccount(owner, accountNumber, balance, withdrawalLimit);
					}
					
					Client adminClient = new Client(firstName, lastName, pAddress, checkingAccount, savingsAccount, date);
					this.clients.add(adminClient);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void evaluateDepositList(String searchTerm) {
		try {
			this.depositList.clear();
			ResultSet resultSet = databaseDriver.getDepositList(searchTerm);
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					String pAddress = resultSet.getString("PayeeAddress");
					this.depositList.add(pAddress);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean evaluateDepositToChecking(String pAddress, double amount) {
		boolean deposited = false;
		try {
			int count = databaseDriver.depositToChecking(pAddress, amount);
			if(count > 0)
				deposited = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return deposited;
	}
	
	public boolean evaluateDepositToSavings(String pAddress, double amount) {
		boolean deposited = false;
		try {
			int count = databaseDriver.depositToSavings(pAddress, amount);
			if(count > 0)
				deposited =true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return deposited;
	}

}
