package com.wuerfelglueck;

public class Dice
{
	private double wins = 0;
	private int sides;
	private int[] sideEyeCount;
	
	public Dice (String constructor)
	{
		String[] constructorParts = constructor.split(" ");
		
		sides = Integer.parseInt(constructorParts[0]);
		sideEyeCount = new int[sides];
		
		for(int n = 1; n < constructorParts.length; n++)
		{
			sideEyeCount[n - 1] = Integer.parseInt(constructorParts[n]);
		}
	}
	
	public int roll()
	{
		int side =(int) (Math.random() * sides);
		return sideEyeCount[side];
	}
	
	public double getWins()
	{
		return wins;
	}
	
	public void doWinsPlusOne()
	{
		wins++;
	}
	
	public void doWinsPlusOneHalf()
	{
		wins += 0.5;
	}
	
	public int getSides()
	{
		return sides;
	}
	
	public int[] getSideEyeCount()
	{
		return sideEyeCount;
	}
}