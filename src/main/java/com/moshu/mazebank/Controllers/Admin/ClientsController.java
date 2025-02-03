package com.moshu.mazebank.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Client;
import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Views.ClientCellFactory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ClientsController implements Initializable {
	
	@FXML
	private ListView<Client> clients_listview;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		try {
			Model.getInstance().evaluateClients();
			ObservableList<Client> clients = Model.getInstance().getClients();
			clients_listview.setItems(clients);
			clients_listview.setCellFactory(param -> new ClientCellFactory());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("An error occured loading clients");
			e.printStackTrace();
		}
		
	}

}
