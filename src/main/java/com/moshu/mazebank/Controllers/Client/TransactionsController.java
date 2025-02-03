package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Models.Transaction;
import com.moshu.mazebank.Views.TransactionCellFactory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class TransactionsController implements Initializable {
	
	@FXML
	private ListView<Transaction> transactions_listview;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		try {
			ObservableList<Transaction> transactions = Model.getInstance().getTransactions();
			transactions_listview.setItems(transactions);
			transactions_listview.setCellFactory(param -> new TransactionCellFactory());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("An error occured loading transactions");
			e.printStackTrace();
		}
		
		
	}

}
