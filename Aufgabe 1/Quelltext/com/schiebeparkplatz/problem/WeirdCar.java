package com.schiebeparkplatz.problem;

public class WeirdCar 
{
	private int position;
	private char name;
	
	public WeirdCar(int position, char name)
	{
		this.position = position;
		this.name = name;
	}

	public int getPosition() 
	{
		return position;
	}

	public char getName()
	{
		return name;
	}
	
	@Override
	public String toString() 
	{
		return ("WeirdCar at " + position + " named " + name);
	}
}
