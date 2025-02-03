package com.moshu.mazebank.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseDriver {
	private Connection conn;
	
	public DatabaseDriver() {
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Client Section
	 * */
	@SuppressWarnings("exports")
	public ResultSet getClientData(String pAddress, String password) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"' AND Password='"+password+"';");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getCheckingAccount(String pAddress) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getSavingsAccount(String pAddress) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getTransactions(String pAddress) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			String sql = "SELECT * FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' ORDER BY Date DESC;";
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getLatestTransactions(String pAddress) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			String sql = "SELECT * FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' ORDER BY Date DESC LIMIT 5;";
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int sendMoney(String sender, String receiver, String message, double amount, double senderBal) {
		int count = 0;
		PreparedStatement pStatement;
		ResultSet resultSet = null;
		String Query = "";
		double senderBalance;
		double receiverBalance;
		try {
			resultSet = getCheckingAccount(receiver);
			resultSet.next();
			receiverBalance = resultSet.getDouble("Balance");
			receiverBalance += amount;
			senderBalance = senderBal - amount;
			Query = "UPDATE CheckingAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(Query);
			pStatement.setDouble(1, senderBalance);
			pStatement.setString(2, sender);
			count += pStatement.executeUpdate();
			pStatement.setDouble(1, receiverBalance);
			pStatement.setString(2, receiver);
			count += pStatement.executeUpdate();
			Query = "INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) VALUES(?,?,?,?,?);";
			pStatement = this.conn.prepareStatement(Query);
			Date curreDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String today = dateFormat.format(curreDate);
			pStatement.setString(1, sender);
			pStatement.setString(2, receiver);
			pStatement.setDouble(3, amount);
			pStatement.setString(4, today);
			pStatement.setString(5, message);
			count += pStatement.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}
	
	public int transferToSavings(String pAddress, double amount) {
		int count = 0;
		PreparedStatement pStatement;
		ResultSet resultSet = null;
		double checkingBalance;
		double savingsBalance;
		String query ="";
		try {
			resultSet = getCheckingAccount(pAddress);
			checkingBalance = resultSet.getDouble("Balance");
			checkingBalance -= amount;
			resultSet = getSavingsAccount(pAddress);
			savingsBalance = resultSet.getDouble("Balance");
			savingsBalance += amount;
			query = "UPDATE CheckingAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, checkingBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
			query = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, savingsBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}
	
	public int transferToChecking(String pAddress, double amount) {
		int count = 0;
		PreparedStatement pStatement;
		ResultSet resultSet = null;
		double checkingBalance;
		double savingsBalance;
		String query ="";
		try {
			resultSet = getCheckingAccount(pAddress);
			checkingBalance = resultSet.getDouble("Balance");
			checkingBalance += amount;
			resultSet = getSavingsAccount(pAddress);
			savingsBalance = resultSet.getDouble("Balance");
			savingsBalance -= amount;
			query = "UPDATE CheckingAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, checkingBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
			query = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, savingsBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}
	/***
	 * Admin Section
	 * */
	@SuppressWarnings("exports")
	public ResultSet getAdminData(String username, String password) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username='"+username+"' AND Password='"+password+"';");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getClients() {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Clients;");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	@SuppressWarnings("exports")
	public ResultSet getDepositList(String searchTerm) {
		Statement statement;
		ResultSet resultSet = null;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress LIKE '%" + searchTerm + "%';");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int createClient(String firstName, String lastName, String password, String chAccNumber, String svAccNumber, 
							String pAddress, double chAccBalance, double svAccBalance) {
		int count = 0;
		PreparedStatement pStatement;
		String query = "";
		try {
			query = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES(?,?,?,?,?);";
			pStatement = this.conn.prepareStatement(query);
			Date curreDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String today = dateFormat.format(curreDate);
			pStatement.setString(1, firstName);
			pStatement.setString(2, lastName);
			pStatement.setString(3, pAddress);
			pStatement.setString(4, password);
			pStatement.setString(5, today);
			count += pStatement.executeUpdate();
			query = "INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES(?,?,?,?)";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setString(1, pAddress);
			pStatement.setString(2, chAccNumber);
			pStatement.setDouble(3, 10.0);
			pStatement.setDouble(4, chAccBalance);
			count += pStatement.executeUpdate();
			query = "INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES(?,?,?,?)";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setString(1, pAddress);
			pStatement.setString(2, svAccNumber);
			pStatement.setDouble(3, 2000.0);
			pStatement.setDouble(4, svAccBalance);
			count += pStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return count;
	}
	
	public int depositToChecking(String pAddress, double amount) {
		int count = 0;
		PreparedStatement pStatement;
		ResultSet resultSet = null;
		double checkingBalance;
		String query ="";
		try {
			resultSet = getCheckingAccount(pAddress);
			checkingBalance = resultSet.getDouble("Balance");
			checkingBalance += amount;
			query = "UPDATE CheckingAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, checkingBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return count;
	}
	
	public int depositToSavings(String pAddress, double amount) {
		int count = 0;
		PreparedStatement pStatement;
		ResultSet resultSet = null;
		double savingsBalance;
		String query ="";
		try {
			resultSet = getSavingsAccount(pAddress);
			savingsBalance = resultSet.getDouble("Balance");
			savingsBalance += amount;
			query = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?;";
			pStatement = this.conn.prepareStatement(query);
			pStatement.setDouble(1, savingsBalance);
			pStatement.setString(2, pAddress);
			count += pStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	/***
	 * Utility Methods
	 * */
	public boolean checkClient(String pAddress) {
		Statement statement;
		ResultSet resultSet = null;
		boolean isPresent = false;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='" + pAddress + "';");
			if (resultSet.isBeforeFirst())
				isPresent = true;
			else
				isPresent = false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return isPresent;
	}
	
	public boolean checkChecking(String accountNumber) {
		Statement statement;
		ResultSet resultSet = null;
		boolean isPresent = false;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE AccountNumber='" + accountNumber + "';");
			if (resultSet.isBeforeFirst())
				isPresent = true;
			else
				isPresent = false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return isPresent;
	}
	
	public boolean checkSavings(String accountNumber) {
		Statement statement;
		ResultSet resultSet = null;
		boolean isPresent = false;
		try {
			statement = this.conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE AccountNumber='" + accountNumber + "';");
			if (resultSet.isBeforeFirst())
				isPresent = true;
			else
				isPresent = false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return isPresent;
	}
	

}
