package com.moshu.mazebank.Controllers.Client;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.moshu.mazebank.Models.Model;
import com.moshu.mazebank.Models.Transaction;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class TransactionCellController implements Initializable {
	
	@FXML
	private FontAwesomeIconView in_icon, out_icon;
	@FXML
	private Label trans_date_lbl, sender_lbl, receiver_lbl, amount_lbl;
	
	private final Transaction transaction;
	
	public TransactionCellController(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		String transDate = String.valueOf(transaction.dateProperty().getValue());
		String sender = transaction.senderProperty().getValue();
		String receiver = transaction.receiverProperty().getValue();
		String pAddress = Model.getInstance().getClient().pAddressProperty().getValue();
		double amount = transaction.amountProperty().getValue();
		DecimalFormat choosenFormat = new DecimalFormat("#,###.##");
		String amountString = choosenFormat.format(amount);
		trans_date_lbl.setText(transDate);
		sender_lbl.setText(sender);
		receiver_lbl.setText(receiver);
		amount_lbl.setText(amountString);
		if (receiver.equals(pAddress)) {
			in_icon.getStyleClass().setAll("in_icon");
		} else {
			out_icon.getStyleClass().setAll("out_icon");
		}
		
	}
	
	

}
