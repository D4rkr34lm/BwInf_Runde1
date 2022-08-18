package com.wuerfelglueck;

public class Figure
{
	private int relativePosition = -1;
	private int absolutePosition = -1;
	private int startPosition;

	public Figure(int startPosition, int index)
	{
		this.startPosition = startPosition;
	}
	
	public int getRelativePosition() 
	{
		return relativePosition;
	}

	public void setRelativePosition(int position) 
	{
		this.relativePosition = position;
	}
	
	public void updateAbsolutePosition()
	{
		if(relativePosition == -1 || relativePosition > 39)
		{
			absolutePosition = -1;
		}
		else
		{
			absolutePosition = relativePosition + startPosition;
			if(absolutePosition > 39)
			{
				absolutePosition -= 39;
			}
		}
	}
	
	public int getAbsolutePosition()
	{
		return absolutePosition;
	}
}
