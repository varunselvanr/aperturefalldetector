package com.android.falldetector;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.media.MediaPlayer;

public class areyouok extends Activity {
	Button yesButton;
	Button noButton;
	MediaPlayer mp;
	MediaPlayer great;
	int countdown = 10;
    Timer t = new Timer();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.areyouok);
        
        // Capture our button from layout
        yesButton 	= (Button)findViewById( R.id.yesButton );
        noButton 	= (Button)findViewById( R.id.noButton );
        // Register the onClick listener with the implementation above
        yesButton.setOnClickListener( yesButtonListener );
        noButton.setOnClickListener( noButtonListener );
        mp = MediaPlayer.create(this, R.raw.areyouokay);
        mp.start();
        t.schedule(new nextScreen(), 10000);
    }
    class nextScreen extends TimerTask {
    	public void run() { 
    			Intent notify = new Intent();
    			notify.setClass(areyouok.this, notify.class);
    			startActivity(notify);
    			t.cancel();
    			areyouok.this.finish();
    	}
    }
    
    private OnClickListener yesButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	if(mp.isPlaying() == true) 
        		mp.stop();
        	t.cancel();
        	mp = MediaPlayer.create(areyouok.this, R.raw.great);
        	mp.start();
        	
			areyouok.this.finish();
        }
    };
    private OnClickListener noButtonListener = new OnClickListener() {
        public void onClick(View v) {
	    	if(mp.isPlaying() == true) 
	    		mp.stop();
	    	t.cancel();
	        Intent notify = new Intent();
	    	notify.setClass(areyouok.this, notify.class);
	    	startActivity(notify);
	    	areyouok.this.finish();
        }
    };
}
