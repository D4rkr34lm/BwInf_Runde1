package com.schiebeparkplatz.application;

import java.io.File;
import java.io.IOException;

import com.schiebeparkplatz.problem.Parkinglot;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SchiebeparkplatzApplication extends Application
{
	private Parkinglot[] parkinglots = new Parkinglot[6];
	private Scene scene;
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{
		init();
		
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void init() throws IOException
	{
		/*
		 * Data reading
		 */
		for(int n = 0; n < 6; n++)
		{
			parkinglots[n] = new Parkinglot(new File("Data\\parkplatz" + n + ".txt"));
		}
		
		/*
		 * GUI
		 */
		VBox root = new VBox(20 );
		
		HBox buttonBox = new HBox(10);
		
		TextArea solutionDisplay = new TextArea();
		solutionDisplay.setPrefHeight(450);
		
		for(int i = 0; i < 6; i++)
		{
			int index = i;
			
			Button exampleButton = new Button("Parkplatz " + index);
			exampleButton.setOnAction(new EventHandler<ActionEvent>() 
			{				
				@Override
				public void handle(ActionEvent arg0)
				{
						solutionDisplay.setText(parkinglots[index].getSolution());
				}
			});
			buttonBox.getChildren().add(exampleButton);
		}
		
		root.getChildren().add(buttonBox);
		root.getChildren().add(solutionDisplay);
		
		scene = new Scene(root, 500, 500);
	}
}
