package com.schiebeparkplatz.problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parkinglot
{
	private ArrayList<NormalCar> normalCars = new ArrayList<NormalCar>();
	private ArrayList<WeirdCar> weirdCars = new ArrayList<WeirdCar>();
	
	public Parkinglot(File dataFile) throws IOException
	{
		/*
		 * Load parkinglot data
		 */ 
		System.out.println("Starting to load ----------------------");
		
		FileReader reader = new FileReader(dataFile);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String normalCarsLine = bufferedReader.readLine();
		char[] normalCarsLineChars = normalCarsLine.toCharArray();
		
		char startLetter = normalCarsLineChars[0];
		char endLetter = normalCarsLineChars[2];
		
		int normalCarsCount = (int)endLetter - (int) startLetter + 1;
			
		for(int n = 0; n < normalCarsCount; n++)
		{
			NormalCar normalCar = new NormalCar(n, (char) (65 + n));
			System.out.println(normalCar.toString());
			normalCars.add(normalCar);
		}
		
		bufferedReader.readLine();
		
		String weirdCarLine = bufferedReader.readLine();
		
		while (weirdCarLine != null) 
		{
			String[] weirdCarLineParts = weirdCarLine.split(" ");
			char[] conversionHelper = weirdCarLineParts[0].toCharArray();
			
			char name = conversionHelper[0];
			int position = Integer.parseInt(weirdCarLineParts[1]);
			
			WeirdCar weirdCar = new WeirdCar(position, name);
			System.out.println(weirdCar.toString());
			
			weirdCars.add(weirdCar);
			
			weirdCarLine = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		System.out.println("Finished loading ----------------------");
	}
	
	public String getSolution()
	{
		String solution = new String();
		
		for(int n = 0; n < normalCars.size(); n++)
		{
			solution += getSolution(normalCars.get(n));
			solution += "\n";
		}
		
		return solution;
	}
	
	public String getSolution(NormalCar normalCar)
	{
		String carSolution = new String();
		carSolution += "Auto: " + normalCar.getName() + "\n";
		
		if(isColliding(normalCar))
		{
			MovementCommand initialMovementCommandLeft = null;
			MovementCommand initialMovementCommandRight = null;
			WeirdCar blockingCar = getBlockingCar(normalCar);
			
			switch(getColisionType(normalCar))
			{
			case front:
				initialMovementCommandLeft = new MovementCommand(blockingCar.getName(), -2);
				initialMovementCommandRight = new MovementCommand(blockingCar.getName(), 1);
				break;
				
			case back:
				initialMovementCommandLeft = new MovementCommand(blockingCar.getName(), -1);
				initialMovementCommandRight = new MovementCommand(blockingCar.getName(), 2);
				break;
			}
			
			ArrayList<MovementCommand> leftPath = getSidePath(initialMovementCommandLeft, blockingCar, new ArrayList<MovementCommand>());
			ArrayList<MovementCommand> rightPath = getSidePath(initialMovementCommandRight, blockingCar, new ArrayList<MovementCommand>());
			
			if(rightPath == null && leftPath == null)
			{	
				carSolution += "Nicht ausparkbar\n";
			}
			else if(leftPath != null && (rightPath == null || leftPath.size() <= rightPath.size()))
			{
				for(int n = (leftPath.size() - 1); n > -1; n--)
				{
					carSolution += leftPath.get(n).toString();
					carSolution += "\n";
				}
				carSolution += "Parke Aus\n";
			}
			else if ( rightPath != null && (leftPath == null || rightPath.size() <= leftPath.size()))
			{
				for(int n = (rightPath.size() - 1); n > -1; n--)
				{
					carSolution += rightPath.get(n).toString();
					carSolution += "\n";
				}
				carSolution += "Parke Aus\n";
			}
		}
		else
		{
			carSolution += "Parke Aus\n";
		}
		
		return carSolution;
	}
	
	private ArrayList<MovementCommand> getSidePath(MovementCommand command, WeirdCar weirdCar, ArrayList<MovementCommand> path)
	{
		path.add(command);
		
		int movementDirection = 0;
		
		if(command.getMoveData() > 0)
		{
			movementDirection = 1;
		}
		else if(command.getMoveData() < 0)
		{
			movementDirection = -1;
		}
		
		if(isCommandGoingOutOfBounds(command, weirdCar))
		{
			return null;
		}
		else if (!isCommandColliding(command, weirdCar))
		{
			return path;
		}
		else
		{
			int movementStrenght = getColisionType(command, weirdCar);
			WeirdCar blockingCar = getBlockingCar(command, weirdCar);
			MovementCommand nextCommand = new MovementCommand(blockingCar.getName(), movementStrenght * movementDirection);
			
			return getSidePath(nextCommand, blockingCar, path);
		}
	}
	
	/*
	 * Helper methods
	 */
	private boolean isCommandGoingOutOfBounds(MovementCommand command, WeirdCar weirdCar)
	{
		int leftBorder = -1;
		int rightBorder = normalCars.size();
		
		int newPosition = weirdCar.getPosition() + command.getMoveData();
		
		if(newPosition > leftBorder && (newPosition + 1) < rightBorder)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private boolean isCommandColliding(MovementCommand command, WeirdCar weirdCar)
	{
		int newPosition = weirdCar.getPosition() + command.getMoveData();
		
		for(WeirdCar possibleBlockingCar : weirdCars)
		{
			if(possibleBlockingCar != weirdCar)
			{                                            
				if((newPosition + 1) == possibleBlockingCar.getPosition()|| newPosition == possibleBlockingCar.getPosition() || newPosition == (possibleBlockingCar.getPosition() + 1))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isColliding(NormalCar normalCar)
	{
		for(WeirdCar weirdCar : weirdCars)
		{
			if(normalCar.getPosition() == weirdCar.getPosition() || normalCar.getPosition() == (weirdCar.getPosition() + 1))
			{
				return true;
			}
		}
		return false;
	}
	
	private ColisionType getColisionType(NormalCar normalCar)
	{
		for(WeirdCar weirdCar : weirdCars)
		{
			if(normalCar.getPosition() == weirdCar.getPosition())
			{
				return ColisionType.front;
			}
			else if(normalCar.getPosition() == (weirdCar.getPosition() + 1))
			{
				return ColisionType.back;
			}
		}
		
		return null;
	}
	
	private int getColisionType(MovementCommand command, WeirdCar weirdCar)
	{
		int newPosition = weirdCar.getPosition() + command.getMoveData();
		
		for(WeirdCar possibleCollidingCar : weirdCars)
		{
			if (possibleCollidingCar != weirdCar)
			{
				if(newPosition == possibleCollidingCar.getPosition())
				{
					return 2;
				}
				else if(newPosition == (possibleCollidingCar.getPosition() + 1) || (newPosition + 1) == possibleCollidingCar.getPosition())
				{
					return 1;
				}
			}
		}
		
		return 0;
	}
	
	private WeirdCar getBlockingCar(MovementCommand command, WeirdCar weirdCar)
	{
		int newPosition = weirdCar.getPosition() + command.getMoveData();
		
		for(WeirdCar possibleCollidingCar : weirdCars)
		{
			if (possibleCollidingCar != weirdCar)
			{
				if(newPosition == possibleCollidingCar.getPosition() || newPosition == (possibleCollidingCar.getPosition() + 1) || (newPosition + 1) == possibleCollidingCar.getPosition())
				{
					return possibleCollidingCar;
				}
			}
		}
		
		return null;
	}
	
	private WeirdCar getBlockingCar(NormalCar normalCar)
	{
		for(WeirdCar possibleBlockingCar : weirdCars)
		{
			if(normalCar.getPosition() == possibleBlockingCar.getPosition() || normalCar.getPosition() == (possibleBlockingCar.getPosition() + 1))
			{
				return possibleBlockingCar;
			}
		}
		
		return null;
	}
}
