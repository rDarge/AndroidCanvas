/*
	This class is pretty much the same. Nothing to see here, folks!
*/

package com.example.androidcanvas;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Canvas_Draw extends Activity implements SensorEventListener, OnItemSelectedListener  {
	
	CanvasView canvas;
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas__draw);        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        
        spinner = (Spinner) findViewById(R.id.ballChooser);
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    adapter = ArrayAdapter.createFromResource(this,
	            R.array.ballTypes, android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(this);
        
        canvas = (CanvasView) findViewById(R.id.canvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_canvas__draw, menu);
        return true;
    }
    
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    
    public void button_push(View v)
    {
    	toast("Repulser Mode");
    	canvas.setMode(2);
    }
    
    public void button_pull(View v)
    {
    	toast("Attractor Mode");
    	canvas.setMode(1);
    }
    
    public void button_create(View v)
    {
    	toast("Creation Mode");
    	canvas.setMode(0);
    }
    
    private void toast(String text)
    {
    	Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    	toast.show();
    }
    
    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    	String type = (String) parent.getItemAtPosition(pos);
    	if(type.equals("Zombie"))
    	{
    		toast("The ["+type+"] class is not included in the trial version of this game!");
    		type = "Normal";
    	}    	
    	else
    		toast(""+parent.getItemAtPosition(pos)+" selected");
    	canvas.setSpawnType(type);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		TextView out = (TextView)findViewById(R.id.DebugOutput);
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			out.setText("0.0");
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			canvas.tilt(x,y,z);
			out.setText(Float.toString(x)+" "+Float.toString(y)+" "+Float.toString(z));
		}
	}
}
