package com.schiebeparkplatz.problem;

public class MovementCommand
{
	private char carName;
	private int moveData;
	
	public MovementCommand(char carName, int moveData) 
	{
		this.carName = carName;
		this.moveData = moveData;
	}

	public char getCarName() 
	{
		return carName;
	}

	public int getMoveData() 
	{
		return moveData;
	}
	
	@Override
	public String toString()
	{
		String string = "Bewege Auto " + carName + " um ";
		
		if(moveData < 0)
		{
			string += (moveData *(-1));
			string += " nach links";
		}
		else if(moveData > 0)
		{
			string += moveData;
			string += " nach rechts";
		}
		
		return string;
	}
}
