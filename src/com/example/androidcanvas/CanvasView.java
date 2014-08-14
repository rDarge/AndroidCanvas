package com.example.androidcanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/*
	In order to use the canvas, we need to implement a custom SurfaceView, that will hold
	and send commands to the canvas and handle interactions between the user and the program.
	
	This CanvasView makes a red square bounce around on a blue background; when the user
	taps on the square, the square jumps to a random spot on the canvas and speeds up.
	It's not too fancy .-.
*/

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	//We need these things for running the canvas itself
	Thread canvasThread;
	SurfaceHolder holder;
	volatile boolean running = true;
	
	//These things are used to make the canvas do stuff
	private BreakOut game;
	private GravityBall ball;
	private int gameMode = 1;
	private int touchMode = 0;
	private String spawnType = "";
	

	/**
	public CanvasView(Context context, AttributeSet attrs)
		This is called once per program execution, and should set up
		the SurfaceHolder. We also use it to set up some initial values 
		for the canvas (rectangle position(r) and background color (p)
	**/
	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//Initialize Game Objects
		game = null;
		ball = null;
		spawnType = "Normal";
		
		holder = this.getHolder();
		holder.addCallback(this);
	}

	/**
	public void run()
		This is called once every canvas "tick", and this is where we will put all of our 
		methods that we want to be constantly running while the canvas is active (like onDraw and move)
		We only want to run this while the program is "running", or else it will try and lock the canvas
		(which doesn't exist while the app is minimized or sleeping). We also don't want to run it
		if we are setting it up for the first time and we haven't initialized the holder yet.
	**/
	public void run() {
		Canvas threadCanvas;
		
		while(running)
		{
			threadCanvas = null;
			//Attempt to lock the Canvas so we can update it
			try{
				threadCanvas = holder.lockCanvas();
				
				if(threadCanvas != null)  //threadCanvas will return null if it's currently being used or is otherwise preoccupied
				{
					onDraw(threadCanvas);
					if(gameMode == 0)
					{
						if(game == null)
							game = new BreakOut(threadCanvas);
						if(game != null)
							game.draw(threadCanvas);
					}
					if(gameMode == 1)
					{	
						if(ball == null)
							ball = new GravityBall(threadCanvas);
						if(ball != null)
						{
							ball.act(threadCanvas);
							ball.draw(threadCanvas);
						}
					}
				}
			}
			//Unlock the Canvas so other stuff can use it
			finally
			{
				if(threadCanvas != null)
					holder.unlockCanvasAndPost(threadCanvas);
			}
		}

	}
	
	///This method will be called whenever we want to update the screen, and we will have to redraw everything each time it is called.
	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.GRAY);
		//canvas.drawRect(r,p);
	}
	
	public void tilt(float x, float y, float z)
	{
		int maxGravity = 5;
		ball.setGravity(-(int)x/maxGravity,(int)y/maxGravity);	
	}
	
	public void setSpawnType(String type)
	{
		spawnType = type;
	}
	
	/**
	public boolean onTouchEvent(MotionEvent m)
		This method is automatically called whenever a touch event occurs on the canvas
		In this example, we get the mouse position from the MotionEvent, and compare it to the rectangle's position.
		If the MotionEvent is within the bounds of the square on the screen, we set clicked to true, so the next time
		we update the square, we'll know it's been hit
	**/
	public boolean onTouchEvent(MotionEvent m)
	{
		if(gameMode == 0)
		{
			
		}
		if(gameMode == 1)
		{
			if(touchMode == 0)
			{
				if(spawnType.equals("Regular"))
					ball.newBall(m.getX(), m.getY());
				else if(spawnType.equals("Hungry"))
					ball.newEater(m.getX(), m.getY());
				else if(spawnType.equals("Hunter"))
					ball.newHunter(m.getX(), m.getY());
				else
					ball.newBall(m.getX(), m.getY());
			}
			else if(touchMode == 1)
				ball.pull(m.getX(),m.getY());
			else if(touchMode == 2)
				ball.push(m.getX(),m.getY());
		}
		return true;
	}

	/**
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
		This method is called when the surface is changed. 
		We don't really expect the canvas to change in this example, so we'll leave it blank.
		If you needed to rearrange the layout of buttons or elements on the canvas, you might want 
		to do it here.
	**/
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	/**
	public void surfaceCreated(SurfaceHolder holder)
		This method is called when the surface is created (Usually once when the app is started,
		and each time the app is resumed). We want to link our copy of the SurfaceHolder here, and
		we want to set running to true so that when we start the canvasThread (and subsequently run())
		the program will start to update the canvas again.
	**/
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = getHolder();
		this.holder.addCallback(this);
		running = true;
		
		canvasThread = new Thread(this);
		canvasThread.start();
	}

	/**
	public void surfaceDestroyed(SurfaceHolder holder)
		This method is called whenever the surface is destroyed (usually when the app is
		minimized, or the app is closed. We will want to set running to false here, so
		that the canvasThread doesn't try to update the canvas while the app is shutting down.
	**/
	public void surfaceDestroyed(SurfaceHolder holder) {
		running = false;
		boolean complete = false;
		
		while(!complete)
		{
			try
			{
				canvasThread.join(); //Blocks the thread until the reciever finishes, then it dies. q_q
				complete = true;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
		}

	}
	
	
	/**
		Toast!
	*/
	private void toast(String text)
    {
    	Toast toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT);
    	toast.show();
    }

	public void setMode(int i) {
		touchMode = i;		
	}

}
