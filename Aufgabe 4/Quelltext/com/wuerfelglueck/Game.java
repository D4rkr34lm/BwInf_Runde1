package com.wuerfelglueck;

import java.util.ArrayList;

public class Game
{

	private Figure[] [] figures = new Figure[2] [4];
	private Dice[] dices;
		
	public Game (Dice[] dices)
	{
		this.dices = dices;
		
		for(int p = 0; p < 2; p++)
		{
			for(int f = 0; f < 4; f++)
			{
					figures[p] [f] = new Figure(p * 20, f);
			}
		}
	}
	
	public int play(int startPlayer)
	{		
		int currentPlayer = startPlayer;
		
		while (!hasGameEnded(currentPlayer)) 
		{
			int dieResult = dices[currentPlayer].roll();
			
			Figure figure = getFigureToMove(currentPlayer, dieResult);
			
			if(figure != null)
			{
				moveFigure(figure,currentPlayer ,dieResult);
			}
			
			if(dieResult != 6)
			{
				currentPlayer = Math.abs(currentPlayer - 1);
			}
		}
		
		return currentPlayer;
	}
	
	public Figure getFigureToMove(int currentPlayer, int dieResult)
	{
		
		if(getFigureOnBField(currentPlayer) != null && getFigureOnAField(currentPlayer) != null)
		{
			if(canFigureBeMoved(getFigureOnAField(currentPlayer), currentPlayer, dieResult))
			{
				return getFigureOnAField(currentPlayer);
			}
		}
		
		if(getFigureOnBField(currentPlayer) != null)
		{
			if(canFigureBeMoved(getFigureOnBField(currentPlayer), currentPlayer, dieResult))
			{
				return getFigureOnBField(currentPlayer);
			}
		}
		
		if(getRemainingFiguresInOrder(currentPlayer).size() > 0)
		{
			for(Figure figure : getRemainingFiguresInOrder(currentPlayer))
			{
				if(canFigureBeMoved(figure, currentPlayer, dieResult))
				{
					return figure;
				}
			}
		}
		
		return null;
		
	}
	
	public void moveFigure(Figure figure, int currentPlayer, int dieResult)
	{
		if(figure.getRelativePosition() == -1 && dieResult == 6)
		{
			figure.setRelativePosition(0);
			figure.updateAbsolutePosition();
		}
		else
		{
			figure.setRelativePosition(figure.getRelativePosition() + dieResult);
			figure.updateAbsolutePosition();
		}
		
		int otherPlayer = Math.abs(currentPlayer - 1);
		
		for(Figure otherPlayerFigure : figures[otherPlayer])
		{
			if(figure.getAbsolutePosition() == otherPlayerFigure.getAbsolutePosition())
			{
				otherPlayerFigure.setRelativePosition(-1);
				otherPlayerFigure.updateAbsolutePosition();
			}
		}
	}
	
	public boolean hasGameEnded(int currentPlayer)
	{
		for(Figure figure : figures[currentPlayer])
		{
			if(figure.getRelativePosition() < 40)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public ArrayList<Figure> getRemainingFiguresInOrder(int currentPlayer)
	{
		ArrayList<Figure> remainingFigures = new ArrayList<Figure>();
		
		for(int i = 0; i < 4; i++)
		{
			Figure figure = figures[currentPlayer] [i];
			if(figure.getRelativePosition() != -1)
			{
				if(remainingFigures.size() == 0)
				{
					remainingFigures.add(figure);
				}
				else
				{
					for(int n = 0; n < remainingFigures.size(); n++)
					{
						if (remainingFigures.get(n).getRelativePosition() < figure.getRelativePosition())
						{
							remainingFigures.add(n,figure);
							break;
						}
					}
					
					if(!remainingFigures.contains(figure))
					{
						remainingFigures.add(figure);
					}
				}
			}
		}
		
		return remainingFigures;
	}
	
	public Figure getFigureOnBField(int currentPlayer)
	{
		Figure bFigure = null;
		
		for(Figure figure : figures[currentPlayer])
		{
			if(figure.getRelativePosition() == -1)
			{
				bFigure = figure;
			}
		}
		
		return bFigure;
	}
	
	public Figure getFigureOnAField(int currentPlayer)
	{
		Figure aFigure = null;
		
		for(Figure figure : figures[currentPlayer])
		{
			if(figure.getRelativePosition() == 0)
			{
				aFigure = figure;
			}
		}
		
		return aFigure;
	}
	
	public boolean canFigureBeMoved(Figure figure,int currentPlayer ,int dieResult)
	{
		if(figure.getRelativePosition() == -1)
		{
			if(dieResult == 6 && getFigureOnAField(currentPlayer) == null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			int nextPosition = figure.getRelativePosition() + dieResult;
			boolean isBlocked = false;
			
			for(Figure otherFigure : figures[currentPlayer])
			{
				if(otherFigure != figure && nextPosition == otherFigure.getRelativePosition())
				{
					isBlocked = true;
				}
			}
			
			
			if(nextPosition < 44 && !isBlocked)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public Figure [] [] getFigures()
	{
		return figures;
	}
}
