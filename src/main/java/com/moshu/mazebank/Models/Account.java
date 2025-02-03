package com.moshu.mazebank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {
	private final StringProperty owner;
	private final StringProperty accountNumber;
	private final DoubleProperty balance;
	
	public Account(String owner, String accountNumber, double balance) {
		this.owner = new SimpleStringProperty(this, "Owner", owner);
		this.accountNumber = new SimpleStringProperty(this, "Account Number", accountNumber);
		this.balance = new SimpleDoubleProperty(this, "Balance", balance);
	}
	
	@SuppressWarnings("exports")
	public StringProperty ownerProperty() {return owner;}
	@SuppressWarnings("exports")
	public StringProperty accountNumberProperty() {return accountNumber;}
	@SuppressWarnings("exports")
	public DoubleProperty balanceProperty() {return balance;}
	
}
