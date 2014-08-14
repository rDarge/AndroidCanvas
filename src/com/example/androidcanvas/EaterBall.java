package com.example.androidcanvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class EaterBall extends Ball {
	
	private int hunger;
	private boolean hungry;
	private final int APPETITE = 10;
	private Ball target;

	public EaterBall(Canvas c, int rad, int frc) {
		super(c, rad, frc);
		initializeEater(rad);
	}

	public EaterBall(float f, float g, int rad, float friction2) {
		super(f, g, rad, friction2);
		initializeEater(rad);
	}
	
	public void initializeEater(int rad)
	{
		paint.setColor(Color.GREEN);
		target = null;
		hunger = 1;
		hungry = false;
	}
	
	
	public void collide(Ball other)
	{
		super.collide(other);
		if(hunger > APPETITE)
			hungry = true;
		else if(hunger < 0)
			hungry = false;
		if(hungry && other.getRadius() <= radius)
		{
			other.setRadius(other.getRadius()-(radius/2));
			setRadius(radius+other.getRadius()/2);
			hunger-=radius;
		}
	}
	
	public void act(float gx, float gy, int width, int height, ArrayList<Ball> others)
	{
		super.act(gx, gy, width, height, others);
		hunger++;
		
		if(hungry && target == null)
			target = findClosest(others);
		if(hungry && target != null)
			jumpTo(target.getX(), target.getY());
		if(hunger > APPETITE*APPETITE)
		{
			radius = (int) (radius * 0.95);
			hunger = radius;
		}	
			
	}

}
