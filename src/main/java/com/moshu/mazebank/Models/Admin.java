package com.moshu.mazebank.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
	private final StringProperty username;
	
	public Admin(String username) {
		this.username = new SimpleStringProperty(this, "Username", username);
	}
	
	@SuppressWarnings("exports")
	public StringProperty userNameProperty() {return this.username; }
	
}
