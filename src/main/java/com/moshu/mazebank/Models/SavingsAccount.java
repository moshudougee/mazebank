package com.moshu.mazebank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account {
	private final DoubleProperty withdrawalLimit;

	public SavingsAccount(String owner, String accountNumber, double balance, double withdrawalLimit) {
		super(owner, accountNumber, balance);
		// TODO Auto-generated constructor stub
		this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
	}
	
	@SuppressWarnings("exports")
	public DoubleProperty withdrawalLimitProperty() {return withdrawalLimit;}

}
