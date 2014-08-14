package com.example.androidcanvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GravityBall {
	
	private ArrayList<Ball> balls;
	
	private int height;
	private int width;
	
	
	private float gx;
	private float gy;
	private float friction;
	private int radius;
	
	private int newBallWait;
	private final int WAITTIME = 2;
	
	

	public GravityBall(Canvas c)
	{
		height = c.getHeight();
		width = c.getWidth();

		gx = 0;
		gy = 1;
		friction = 0.5f;
		radius = 15;
		newBallWait = 0;
		
		balls = new ArrayList<Ball>();

	}
	
	public void newBall(float f, float g)
	{
		if(newBallWait == 0)
		{
			balls.add(new Ball(f, g, radius, friction));
			newBallWait = WAITTIME;
		}
	}
	
	public void newEater(float f, float g)
	{
		if(newBallWait == 0)
		{
			balls.add(new EaterBall(f, g, radius, friction));
			newBallWait = WAITTIME;
		}
	}
	
	public void newHunter(float f, float g)
	{
		if(newBallWait == 0)
		{
			balls.add(new Hunter(f, g, radius, friction));
			newBallWait = WAITTIME;
		}
	}
	
	public void setGravity(int gravx, int gravy)
	{
		gx = gravx;
		gy = gravy;
	}
	
	public void draw(Canvas c)
	{
		for(int x = 0; x < balls.size(); x++)
			balls.get(x).draw(c);		
	}
	
	public void act(Canvas c)
	{
		for(int x = 0; x < balls.size(); x++)
		{
			
			//Balls die if they get too small
			if(balls.get(x).getRadius() < 2)
			{
				balls.remove(x);
				if(x > 0)
					x--;
			}			
			for(int y = 0; y < balls.size(); y++)
				if(y != x && balls.get(x).checkCollision(balls.get(y)))
					balls.get(x).collide(balls.get(y));
			if(balls.size() > x)
				balls.get(x).act(gx, gy, width, height,balls);
		}
		if(newBallWait > 0)
			newBallWait = newBallWait - 1;
	}

	public void pull(float mx, float my) {
		for(int b = 0; b < balls.size(); b++)
			balls.get(b).jumpTo(mx,my);
		
	}

	public void push(float mx, float my) {
		for(int b = 0; b < balls.size(); b++)
			balls.get(b).runFrom(mx,my);
	}

	
}