package com.example.androidcanvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball
{
	protected float x;
	protected float y;
	protected float vx;
	protected float vy;
	protected float gx;
	protected float gy;
	protected float friction;
	protected int radius;
	protected Paint paint;
	
	private final int maxVX = 20;
	private final int maxVY = 20;
	
	public Ball(Canvas c, int rad, int frc)
	{
		x = c.getWidth()/2;
		y = c.getHeight()/2;
		vx = randBetween(-10,10);
		vy = randBetween(-10,10);
		gx = 0;
		gy = 1;
		radius = rad;
		friction = frc;
		paint = new Paint();
		paint.setColor(Color.RED);
	}
	
	public Ball(float f, float g, int rad, float friction2)
	{
		x = f;
		y = g;
		vx = randBetween(-20,20);
		vy = randBetween(-20,20);
		gx = 0;
		gy = 1;
		radius = rad;
		friction = friction2;
		paint = new Paint();
		paint.setColor(Color.RED);
		
	}
	
	public void setGravity(int gravx, int gravy)
	{
		gx = gravx;
		gy = gravy;
	}
	
	public void draw(Canvas c)
	{
		c.drawCircle(x,y,radius,paint);
	}
	
	public float randBetween(int a, int b)
	{
		return (float) (Math.random()*(b-a)-b);
		
	}
	
	public boolean checkCollision(Ball other)
	{
		if(Math.abs(x-other.getX())<radius && Math.abs(y-other.getY())<radius)
			return true;
		return false;
	}
	
	public void collide(Ball other)
	{

		//Collision is go
		float myVX, myVY;
		float otVX, otVY;
		if(vx < other.getVX())
		{
			myVX = (other.getVX() - vx)*friction;
			myVY = (other.getVY() - vy)*friction;
			otVX = (vx - other.getVX())*friction;
			otVY = (vy - other.getVY())*friction;
		}
		else
		{
			otVX = (other.getVX() - vx)*friction;
			otVY = (other.getVY() - vy)*friction;
			myVX = (vx - other.getVX())*friction;
			myVY = (vy - other.getVY())*friction;
		}
		
		//Make sure the balls are not intersecting
		while((other.getX() > x - radius && other.getX() < x + radius) 
		   || (other.getY() > y - radius && other.getY() < y + radius))
		{
			if(x > other.getX())
			{
				x++;
				other.setX(other.getX()-1);
			}
			else
			{
				x--;
				other.setX(other.getX()+1);
			}
			
			if(y > other.getY())
			{
				y++;
				other.setY(other.getY()-1);
			}
			else
			{
				y--;
				other.setY(other.getY()+1);
			}
		}
			
		//Apply new values
		vx = myVX;
		vy = myVY;
		other.setVelocity(otVX,otVY);
		
	}

	public void act(float gx, float gy, int width, int height, ArrayList<Ball> others)
	{
		vx = vx+gx;
		vy = vy+gy;		
		int nextX = (int) (x + vx);
		int nextY = (int) (y + vy);
		if(nextX < 0 || nextX > width)
			vx = -vx*friction;
		if(nextY < 0 || nextY > height)
			vy = -vy*friction;
		
		if(vy < 1 && vy > -1)
			vy = 0;
		
		if(y > height)
			y = height-radius;
		
		x = (int) (x + vx);
		y = (int) (y + vy);
	}
	
	public void jumpTo(float mx, float my) {
		float restraint = 0.1f;
		float diffX = mx-x;
		float diffY = my-y;
		vx = diffX*restraint;
		vy = diffY*restraint;
		
	}
	
	public void runFrom(float mx, float my) {
		float restraint = 0.1f;
		float diffX = mx-x;
		float diffY = my-y;
		vx = -diffX*restraint;
		vy = -diffY*restraint;
		
	}
	
	public Ball findClosest(ArrayList<Ball> others)
	{
		Ball closest = null;
		for(int x = 0; x < others.size(); x++)
		{
			Ball other = others.get(x);
			if(closest == null)
				closest = other;
			else if(Math.abs(other.getX()-x)+Math.abs(other.getY()-y) <
					Math.abs(closest.getX()-x)+Math.abs(closest.getY()-y))
				closest = other;
				
		}
		return closest;
	}
	
	public void setVelocity(float otherVX, float otherVY)
	{
		vx = otherVX;
		vy = otherVY;
	}
	
	public void setX(float newX)
	{
		x = newX;
	}
	
	public void setY(float newY)
	{
		y = newY;
	}
	
	public void setRadius(int newRadius)
	{
		radius = newRadius;
	}
	
	public void setColor(Paint color)
	{
		paint = color;
	}
	
	public float getX()	{ return x; }
	public float getY()	{ return y;	}
	public float getVX() { return vx; }
	public float getVY() { return vy; }
	public float getNextX() { return x * vx; }
	public float getNextY() { return y * vy; }
	public int getRadius() { return radius; }
	
	
}
