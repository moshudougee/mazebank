package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClientCellController implements Initializable {
	
	@FXML
	private Label fname_lbl, lname_lbl, paddress_lbl, ch_acc_lbl, sv_acc_lbl, date_lbl;
	@FXML
	private Button delete_btn;
	
	private final Client client;
	
	public ClientCellController(Client client) {
		this.client = client;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		String firstName = client.firstNameProperty().getValue();
		String lastName = client.lastNameProperty().getValue();
		String pAddress = client.pAddressProperty().getValue();
		String chAccNumber = client.checkingAccountProperty().get().accountNumberProperty().getValue();
		String svAccNumber = client.savingsAccountProperty().get().accountNumberProperty().getValue();
		String date = String.valueOf(client.dateProperty().getValue());
		
		fname_lbl.setText(firstName);
		lname_lbl.setText(lastName);
		paddress_lbl.setText(pAddress);
		ch_acc_lbl.setText(chAccNumber);
		sv_acc_lbl.setText(svAccNumber);
		date_lbl.setText(date);
		
	}

}
