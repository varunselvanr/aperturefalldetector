package com.android.falldetector;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.android.falldetector.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class FallDetector extends Activity {
	 private TextView accText;
	 private TextView total;
	 private TextView maxText;
	 private TextView orientText;
     private SensorManager myManager;
     private List<Sensor> sensors;
     private List<Sensor> sensors2;
     private Sensor accSensor;
     private Sensor orientSensor;
     private float oldX, oldY, oldZ = 0f;
     private float maxX = 0f;
     private float maxY = 0f;
     private float maxZ = 0f;
 	 private long lastTimestamp = 0l;
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
             
        setContentView(R.layout.main);
        
        accText = (TextView)findViewById(R.id.accText);
        total = (TextView)findViewById(R.id.total);
        maxText = (TextView)findViewById(R.id.max);
        orientText = (TextView)findViewById(R.id.orientText);
        
       
        // Set Sensor + Manager
        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensors = myManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        sensors2 = myManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(sensors.size() > 0)
        {
          accSensor = sensors.get(0);
        }
        if(sensors2.size() > 0)
        {
          orientSensor = sensors2.get(0);
        }
    }

    private void updateTV(float x, float y, float z, long timestamp)
    {
    //	fallData.addData(x, y, z);
     //float thisX = x - oldX * 10;
     //float thisY = y - oldY * 10;
     //float thisZ = z - oldZ * 10;
    	float thisX = x;
    	float thisY = y;
    	float thisZ = z;
     
   //  accText.setText("x: " + Math.round(thisX) + ";\n y:" + Math.round(thisY) + ";\n z: " + Math.round(thisZ));
     accText.setText("x: " + thisX + "\n y:" + thisY + "\n z:" + thisZ);
     float nowTotal = thisX + thisY + thisZ;
     float lastTotal = oldX + oldY + oldZ;
     total.setText("Total Acceleration: " + nowTotal + "Timestamp:" + timestamp);

     if( timestamp-TimeUnit.NANOSECONDS.convert(10, TimeUnit.SECONDS) > lastTimestamp && Math.abs( nowTotal-SensorManager.GRAVITY_EARTH ) > SensorManager.GRAVITY_EARTH*1.2 && lastTotal != 0 ) // double last acceleration
     {
         lastTimestamp = timestamp;
    	 Intent checkIfUserIsOkay = new Intent();
    	 checkIfUserIsOkay.setClass(this, areyouok.class);
    	 startActivity(checkIfUserIsOkay);
     }
     
     if(Math.abs(thisX) > maxX)
    	 maxX = Math.abs(thisX);
     if(Math.abs(thisY) > maxY)
    	 maxY = Math.abs(thisY);
     if(Math.abs(thisZ) > maxZ)
    	 maxZ = Math.abs(thisZ);
     maxText.setText("Max X:" + maxX + "\n Max Y: " + maxY + "\n MaxZ: " + maxZ);
     oldX = x;
     oldY = y;
     oldZ = z;
    }
    
    private void updateOrientation(int a, int p, int r, long timestamp)
    {
    	fallData.addData(a, p, r, timestamp);
    	orientText.setText("Azimuth: " + a + "\n Pitch: " + p + "\n R: " + r);
    }
   
    private final SensorEventListener mySensorListener = new SensorEventListener()
    {
     public void onSensorChanged(SensorEvent event)
     {
    	 if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
          updateTV( event.values[0], event.values[1], event.values[2], event.timestamp);
    		 
    	 //else if( event.sensor.getType() == Sensor.TYPE_ORIENTATION )
    	//	 updateOrientation((int) event.values[0], (int) event.values[1], (int) event.values[2], event.timestamp);
     }
     
     public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    @Override
    protected void onResume()
    {
     super.onResume();
     myManager.registerListener(mySensorListener, orientSensor, SensorManager.SENSOR_DELAY_NORMAL);
     myManager.registerListener(mySensorListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    // myManager.registerListener(mySensorListener, SensorManager.SENSOR_ACCELEROMETER | SensorManager.SENSOR_ORIENTATION, SensorManager.SENSOR_DELAY_GAME);
     }
   
    @Override
    protected void onStop()
    {     
     myManager.unregisterListener(mySensorListener);
     super.onStop();
    }
}
