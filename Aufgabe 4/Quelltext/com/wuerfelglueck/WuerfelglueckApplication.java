package com.wuerfelglueck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;
import com.sun.org.apache.bcel.internal.generic.NEW;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WuerfelglueckApplication extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	private final int EXAMPLE_COUNT = 4;
	
	private Scene mainScene;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		init();
		
		stage.setScene(mainScene);
		stage.show();
	}
	
	@Override
	public void init() throws Exception 
	{
		VBox root = new VBox(5);
		
		HBox buttonContainer = new HBox(20);
		
		TextArea solutionDisplay = new TextArea();
		solutionDisplay.setPrefHeight(450);
		solutionDisplay.setFont(new Font(25));
		
		for (int n = 0; n < EXAMPLE_COUNT; n++) 
		{
			int index = n;
			Button exampleLoadButton = new Button("Beispiel " + (index));
			exampleLoadButton.setOnAction(new EventHandler<ActionEvent>()
			{	
				@Override
				public void handle(ActionEvent event)
				{	
					File mapData = new File("Data\\wuerfel" + (index) + ".txt");
					try
					{
						solutionDisplay.setText(getSolutionAsText(testDices(mapData)));
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			});
			buttonContainer.getChildren().add(exampleLoadButton);
		}
		
		root.getChildren().add(buttonContainer);
		root.getChildren().add(solutionDisplay);
		
		mainScene = new Scene(root, 900, 400);
	}
	
	
	
	private final int SIMULATIONS_PER_PAIR = 1000;
	
	public String getSolutionAsText(ArrayList<Dice> dicesInOrder)
	{
		String ret = new String();
		
		int placing = 1;
		
		for(Dice dice : dicesInOrder)
		{
			
			if(dicesInOrder.indexOf(dice) != 0 && dice.getWins() < dicesInOrder.get(dicesInOrder.indexOf(dice) - 1).getWins())
			{
				placing++;
			}
			
			ret += placing + ". mit " + dice.getWins() + " Siegen: " + dice.getSides() + " Seiten  [";
			
			for(Integer eyeCount : dice.getSideEyeCount())
			{
				ret += eyeCount + ";";
				
			}
			
			ret += "]\n";
		}
		
		return ret;
	}
	
	public ArrayList<Dice> testDices(File dataFile) throws IOException
	{
		FileReader fileReader = new FileReader(dataFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String inputLine = bufferedReader.readLine();
		
		int diceCount = Integer.parseInt(inputLine);
		
		Dice[] dices = new Dice[diceCount];
		
		for(int i = 0; i < diceCount; i++)
		{
			inputLine = bufferedReader.readLine();
			dices[i] = new Dice(inputLine);
		}
		
		bufferedReader.close();
		
		for(int diceA = 0; diceA < diceCount; diceA++)
		{
			for(int diceB = diceA + 1; diceB < diceCount; diceB++)
			{
				int diceAWins = 0;
				int diceBWins = 0;
				
				for(int i = 0; i < SIMULATIONS_PER_PAIR; i++)
				{
					int starter;
					
					if(i < (SIMULATIONS_PER_PAIR/2))
					{
						starter = 0;
					}
					else
					{
						starter = 1;
					}
					
					Dice[] gameDices = new Dice[2];
					gameDices[0] = dices[diceA];
					gameDices[1] = dices[diceB];
					Game game = new Game(dices);
					
					int winner = game.play(starter);
					
					if(winner == 0)
					{
						diceAWins++;
					}
					else
					{
						diceBWins++;
					}
				}
				
				if(diceAWins >= SIMULATIONS_PER_PAIR * 0.66 || diceBWins >= SIMULATIONS_PER_PAIR * 0.66 )
				{
					
					if(diceAWins > diceBWins)
					{
						dices[diceA].doWinsPlusOne();
						System.out.println("Dice " + diceA + " won against " + diceB + " with " + diceAWins + " wins.");
					}
					else
					{
						dices[diceB].doWinsPlusOne();
						System.out.println("Dice " + diceB + " won against " + diceA + " with " + diceBWins + " wins.");
					}
				}
				else
				{
					System.out.println("Draw between " + diceA + " and " + diceB);
					dices[diceA].doWinsPlusOneHalf();
					dices[diceB].doWinsPlusOneHalf();
				}
			}
		}
		
		ArrayList<Dice> dicesInOrder = new ArrayList<>();
		
		for(int i = 0; i < dices.length; i++)
		{
			Dice highestDice = null;
			for(Dice dice : dices)
			{
				if(!dicesInOrder.contains(dice) && highestDice == null)
				{
					highestDice = dice;
				}	
				else if(!dicesInOrder.contains(dice) && highestDice.getWins() <= dice.getWins())
				{
					highestDice = dice;
				}
			}
			dicesInOrder.add(highestDice);
		}
		
		return dicesInOrder;
	}
}
