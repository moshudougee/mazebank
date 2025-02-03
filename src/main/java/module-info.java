module mazebank {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.graphics;
	requires javafx.base;
	requires de.jensd.fx.glyphs.fontawesome;
	requires java.sql;
	requires org.xerial.sqlitejdbc;
	
	opens com.moshu.mazebank to javafx.fxml, de.jensd.fx.glyphs.fontawesome;
	opens com.moshu.mazebank.Controllers to javafx.fxml, de.jensd.fx.glyphs.fontawesome;
	opens com.moshu.mazebank.Controllers.Admin to javafx.fxml, de.jensd.fx.glyphs.fontawesome;
	opens com.moshu.mazebank.Controllers.Client to javafx.fxml, de.jensd.fx.glyphs.fontawesome;
	
	
	exports com.moshu.mazebank.Models;
	exports com.moshu.mazebank.Views;
	exports com.moshu.mazebank.Controllers;
	exports com.moshu.mazebank.Controllers.Admin;
	exports com.moshu.mazebank.Controllers.Client;
	exports com.moshu.mazebank;
	
	
}