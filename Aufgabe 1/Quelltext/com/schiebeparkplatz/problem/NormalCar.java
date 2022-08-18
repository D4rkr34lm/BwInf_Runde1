package com.schiebeparkplatz.problem;

public class NormalCar
{
	private int position;
	private char name;
	
	public NormalCar(int position, char name)
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
		return ("NormalCar at " + position + " named " + name);
	}
}
