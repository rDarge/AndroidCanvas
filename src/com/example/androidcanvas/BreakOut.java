package com.example.androidcanvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BreakOut {
	
	ArrayList<Block> blocks;
	int height;
	int width;
	
	int breakerX;
	int breakerY;
	int breakerVX;
	int breakerVY;
	
	int playerX;
	int playerY;
	int playerVX;
	int playerVY;
	int playerWidth;
	int playerHeight = 20;	
	

	public BreakOut(Canvas c) {
		height = c.getHeight();
		width = c.getWidth();
		blocks = new ArrayList<Block>();
		setup(1);
		
		playerX = width/2;
		playerWidth = 50;
		playerY = height-50;
		playerVX = 0;
		playerVY = 0;
		
		breakerX = playerX;
		breakerY = playerY - 10;
		breakerVX = 0;
		breakerVY = 0;
		
	}
	
	public void move()
	{
		int predictedX = playerX + playerVX;
		int predictedY = playerY + playerVY;
		
		if(predictedX < 0 || predictedX > width)
			playerVX = -playerVX;
		else
			playerX = predictedX;
		
		//Left off here. Do collisions.
	}
	
	public void setup(int level)
	{
		switch(level){
			case 1:
			{
				int numBlocksWide = 7;
				int numBlocksHigh = 3;
				int blockWidth = width/numBlocksWide;
				int blockHeight = 20;
				
				for(int x = 0; x < numBlocksWide; x++)
				{
					for(int y = 0; y < numBlocksHigh; y++)
					{
						blocks.add(new Block(x*blockWidth,y*blockHeight, blockWidth,blockHeight));
					}
				}
			}
		}
		
	}
	
	public void draw(Canvas c)
	{
		c.drawColor(Color.GRAY);
		
		//Draw the bricks!
		for(int x = 0; x < blocks.size(); x++)
		{
			blocks.get(x).draw(c);
		}
		
	}

}

class Block
{
	RectF block;
	float x;
	float y;
	float height;
	float width;
	
	int r, g, b;
	Paint p;
	
	public Block(int newx, int newy, int newWidth, int newHeight)
	{
		
		x = newx;
		y = newy;
		
		width = newWidth;
		height = newHeight;
		
		p = new Paint();
		p.setColor(Color.CYAN);
		
		block = new RectF(x+1, y+1, x+width-2, y+height-2);
	}
	
	public void draw(Canvas c)
	{
		c.drawRect(x, y, x+width, y+height, new Paint(Color.BLACK));
		c.drawRect(block, p);		
	}
	
}
