package com.vollgeladen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

public class VollgeladenApplication extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
private final int EXAMPLE_COUNT = 5;
	
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
			int index = n + 1;
			Button exampleLoadButton = new Button("Beispiel " + (index));
			exampleLoadButton.setOnAction(new EventHandler<ActionEvent>()
			{	
				@Override
				public void handle(ActionEvent event)
				{	
					File mapData = new File("Data\\hotels" + (index) + ".txt");
					try
					{
						solutionDisplay.setText(solveMap(mapData));
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
		
		mainScene = new Scene(root, 800, 500);
	}
	
	
	/*
	 * 
	 * 
	 * 
	 */
	
	public String solveMap(File mapData) throws IOException
	{
		ArrayList<Node> nodes = new ArrayList<>();
		Node startNode;
		Node endNode;
		
		
		/*
		 * Load Data
		 */
		FileReader fileReader = new FileReader(mapData);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		int hotelCount = Integer.parseInt(bufferedReader.readLine());
		int wayLenght = Integer.parseInt(bufferedReader.readLine());
		
		startNode = new Node(0);
		
		for(int i = 0; i < hotelCount; i++)
		{
			String hotelLine = bufferedReader.readLine();
			
			String[] hotelLineParts = hotelLine.split(" ");
			
			int position = Integer.parseInt(hotelLineParts[0]);
			double rating = Double.parseDouble(hotelLineParts[1]);
			
			nodes.add(new Node(position, rating));
		}
		
		bufferedReader.close();
		
		endNode = new Node(wayLenght);
		nodes.add(endNode);

		/*
		 * Find best path
		 */
		ArrayList<Node> openNodes = new ArrayList<Node>();
		ArrayList<Node> closedNodes = new ArrayList<Node>();
		
		openNodes.add(startNode);		
		
		while(endNode.getPreviousNode() == null)
		{		
			if(openNodes.size() == 0) return "Karte nicht lößbar";
			
			Node bestNode = null;
			
			/*
			 * Find best Node
			 */
			
			for(Node node : openNodes)
			{
				if(bestNode == null)
				{
					bestNode = node;
				}
				else if(rateNode(bestNode, startNode, 1) < rateNode(node, startNode, 1))
				{
					bestNode = node;
				}
				else if(rateNode(bestNode, startNode, 1) == rateNode(node, startNode, 1))
				{
					if(bestNode.getPosition() < node.getPosition())
					{
						bestNode = node;
					}
				}
			}
			
			closedNodes.add(bestNode);
			openNodes.remove(bestNode);
			
			ArrayList<Node> nodesInReach = getReachebleNodes(bestNode, nodes);
			
			for(Node node : nodesInReach)
			{				
				if(node.getPreviousNode() == null)
				{
					node.setPreviousNode(bestNode);
				}
				else
				{
					if(getPathLength(node, startNode) >= (getPathLength(bestNode, startNode) + 1))
					{
						node.setPreviousNode(bestNode);
						if(closedNodes.contains(node))
						{
							closedNodes.remove(node);
							openNodes.add(node);
						}
					}
				}
					
				if(node.getPreviousNode() != null && node.getPreviousNode().getPreviousNode() != null)
				{
					if(node.getPosition() - node.getPreviousNode().getPreviousNode().getPosition() <= 360)
					{
						node.setPreviousNode(node.getPreviousNode().getPreviousNode());
					}
				}
				
				if(!closedNodes.contains(node) && !openNodes.contains(node))
				{
					openNodes.add(node);
				}	
			}
		}
		
		return constructSolution(endNode, startNode, endNode);
	}
	
	public int getPathLength (Node node, Node startNode)
	{
		int n = 0;
		
		while (node != startNode)
		{
			n++;
			node = node.getPreviousNode();
		}
		
		return n;
	}
	
	public String constructSolution(Node node, Node startNode, Node endNode)
	{
		if(node == endNode)
		{
			return constructSolution(node.getPreviousNode(), startNode, endNode) + "Gehe zum Endpunkt bei " + endNode.getPosition() + "\n" + "Das geringst gewertetste Hotel auf dem Pfad hat eine Wertung von " + rateNode(endNode.getPreviousNode(), startNode, 1);
		}
		else if(node == startNode)
		{
			return "Starte bei 0 \n";
		}
		else
		{
			return constructSolution(node.getPreviousNode(), startNode, endNode) + ("Gehe zum Hotel, bei " + node.getPosition() + " mit der Wertung " + node.getRating() + "\n");
		}
	}
	
	public double rateNode(Node node, Node startNode, int pathLenght)
	{
		if(pathLenght > 4)
		{
			return 0;
		}
		else if(node.getPreviousNode() == startNode)
		{
			return node.getRating();
		} 
		else
		{
			double currentRating = rateNode(node.getPreviousNode(), startNode, pathLenght + 1);
			
			if(node.getRating() < currentRating)
			{
				return node.getRating();
			}
			else
			{
				return currentRating;
			}
		}
	}
	
	public ArrayList<Node> getReachebleNodes(Node subjectNode, ArrayList<Node> nodes)
	{
		ArrayList<Node> nodesInReach = new ArrayList<Node>();
		
		for(Node node : nodes)
		{
			if(node.getPosition() > subjectNode.getPosition() && node.getPosition() <= (subjectNode.getPosition() + 360))
			{
				nodesInReach.add(node);
			}
		}
		
		return nodesInReach;
	}
}
