package com.moshu.mazebank;

import com.moshu.mazebank.Models.Model;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application 
{
    

	@SuppressWarnings("exports")
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		
		Model.getInstance().getViewFactory().showLoginWindow();
		
		
	}
	
	public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
		launch(args);
    }
}
