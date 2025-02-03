package com.moshu.mazebank.Models;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty pAdddress;
	private final ObjectProperty<Account> checkingAccount;
	private final ObjectProperty<Account> savingsAccount;
	private final ObjectProperty<LocalDate> dateCreated;
	
	public Client(String fName, String lName, String pAddress, Account cAccount, Account sAccount, LocalDate date) {
		this.firstName = new SimpleStringProperty(this, "FirstName", fName);
		this.lastName = new SimpleStringProperty(this, "LastName", lName);
		this.pAdddress = new SimpleStringProperty(this, "Payee Address", pAddress);
		this.checkingAccount = new SimpleObjectProperty<>(this, "Checking Account", cAccount);
		this.savingsAccount = new SimpleObjectProperty<>(this, "Savings Account", sAccount);
		this.dateCreated = new SimpleObjectProperty<>(this, "Date", date);
	}
	
	@SuppressWarnings("exports")
	public StringProperty firstNameProperty() {return this.firstName;}
	@SuppressWarnings("exports")
	public StringProperty lastNameProperty() {return this.lastName;}
	@SuppressWarnings("exports")
	public StringProperty pAddressProperty() {return this.pAdddress;}
	public ObjectProperty<Account> checkingAccountProperty() {return this.checkingAccount;}
	public ObjectProperty<Account> savingsAccountProperty() {return this.savingsAccount;}
	public ObjectProperty<LocalDate> dateProperty() {return this.dateCreated;}
	
	public int getTransactionLimit() {
		if (checkingAccount.get() instanceof CheckingAccount) {
			return ((CheckingAccount) checkingAccount.get()).transactionLimitProperty().get();
		}
		return -1;
	}
	
	public double getWithdrawalLimit() {
		if (savingsAccount.get() instanceof SavingsAccount) {
			return ((SavingsAccount) savingsAccount.get()).withdrawalLimitProperty().get();
		}
		return -1;
	}
}
