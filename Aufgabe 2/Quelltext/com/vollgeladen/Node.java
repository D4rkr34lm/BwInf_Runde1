package com.vollgeladen;

public class Node 
{
	private int position;
	private double rating;
	private Node previousNode;

	public Node(int position)
	{
		this.position = position;
	}
	
	public Node(int position, double rating)
	{
		this.position = position;
		this.rating = rating;
	}

	public int getPosition()
	{
		return position;
	}

	public double getRating()
	{
		return rating;
	}

	public Node getPreviousNode() 
	{
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) 
	{
		this.previousNode = previousNode;
	}

}
