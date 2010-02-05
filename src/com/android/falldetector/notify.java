package com.android.falldetector;

import java.util.Timer;
import java.util.TimerTask;

import com.android.falldetector.areyouok.nextScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.media.AudioManager;

public class notify extends Activity
{
	private static final String smsNumber = "4172786278";
	private static final String caregiverNumber = "tel:4172786278";
	
//	private static final String smsNumber = "8125750715";
//	private static final String caregiverNumber = "tel:8122721964";

    Button notifyCaregiver;
    Button notifyEmergency;
    Button notifyNobody;
    MediaPlayer mp;
    Timer t = new Timer();
    
    /** Called when the activity is first created. */
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);             
        setContentView(R.layout.notify);
        
        notifyCaregiver = (Button)findViewById(R.id.notifyCaregiver);
        notifyEmergency = (Button)findViewById(R.id.notifyEmergency);
        notifyNobody 	= (Button)findViewById(R.id.notifyNobody);
        
        notifyCaregiver.setOnClickListener( notifyCaregiverListener );
        notifyEmergency.setOnClickListener( notifyEmergencyListener );
        notifyNobody.setOnClickListener( notifyNobodyListener );
        
        mp = MediaPlayer.create(this, R.raw.ohno);
        mp.start();
        t.schedule(new autoNotifyCaregiver(), 10000);
    }
    class autoNotifyCaregiver extends TimerTask {
    	public void run() { 
        	if(mp.isPlaying() == true) 
        		mp.stop();
        	mp = MediaPlayer.create(notify.this, R.raw.calling);
        	mp.start();
        	sendSMS();
        	callCaregiver(); // limitation: not possible to play a sound or tts over a call http://developer.android.com/guide/topics/media/index.html
			notify.this.finish();
    	}
    }
    
    private OnClickListener notifyCaregiverListener = new OnClickListener() {
        public void onClick(View v) {
        	if(mp.isPlaying() == true) 
        		mp.stop();
        	t.cancel();
        	mp = MediaPlayer.create(notify.this, R.raw.calling);
        	mp.start();
        	sendSMS();
        	callCaregiver(); // limitation: not possible to play a sound or tts over a call http://developer.android.com/guide/topics/media/index.html
			notify.this.finish();
        }
    };
    private OnClickListener notifyEmergencyListener = new OnClickListener() {
        public void onClick(View v) {
        	if(mp.isPlaying() == true) 
        		mp.stop();
        	t.cancel();
        	mp = MediaPlayer.create(notify.this, R.raw.callingemergency);
        	mp.start();
        	sendSMS();
        	callEmergency();
        	
			notify.this.finish();
        }
    };
    private OnClickListener notifyNobodyListener = new OnClickListener() {
        public void onClick(View v) {
        if(mp.isPlaying() == true) 
        		mp.stop();
        t.cancel();
    	 notify.this.finish();
        }
    };
	
	public static void sendSMS()
	{
	  SmsManager.getDefault().sendTextMessage(smsNumber, null, "I've fallen and I can't get up!", null, null);
	}
	public void callEmergency()
	{
		startActivityForResult(new Intent(Intent.ACTION_CALL, Uri.parse("tel:8122721964")), 1);
	}
	public void callCaregiver()
	{
    	AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    	//am.STREAM_VOICE_CALL
    	am.setSpeakerphoneOn(true);
    	am.isSpeakerphoneOn();
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_SHOW_UI); 
        am.setRouting(AudioManager.MODE_CURRENT, AudioManager.ROUTE_ALL, 1); 
		startActivityForResult(new Intent(Intent.ACTION_CALL, Uri.parse(caregiverNumber)), 1);
    	am.setSpeakerphoneOn(false);
	}
}
