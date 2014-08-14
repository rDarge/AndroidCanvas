package com.example.androidcanvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class Hunter extends Ball {
	
	private Ball prey;

	public Hunter(Canvas c, int rad, int frc) {
		super(c, rad, frc);
		initializeHunter();
	}

	public Hunter(float f, float g, int rad, float friction2) {
		super(f, g, rad, friction2);
		initializeHunter();
	}
	
	public void initializeHunter()
	{
		paint.setColor(Color.BLACK);
		prey = null;
	}
	
	public void collide(Ball other)
	{
		super.collide(other);
		if(prey == null)
		{
			prey = other;
			paint.setColor(Color.DKGRAY);
		}
		else if(other == prey)
		{
			other.setRadius(other.getRadius()-1);
		}
	}
	
	public void act(float gx, float gy, int width, int height, ArrayList<Ball> others)
	{
		super.act(gx, gy, width, height, others);
		
		//Follow the prey until it dies
		if(prey != null && prey.getRadius() > 1)
		{
			jumpTo(prey.getX(), prey.getY());
		}
		else
		{
			paint.setColor(Color.BLACK);
			prey = null;
		}
	}

}
