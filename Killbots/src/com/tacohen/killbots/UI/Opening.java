package com.tacohen.killbots.UI;

import com.tacohen.killbots.R;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Opening extends Activity {

	public GLSurfaceView mGLView;
	private String TAG = "Opening";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.opening);
		
		//Force landscape mode
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//attach event handler to dash buttons
	    DashboardClickListener dBClickListener = new DashboardClickListener();
	    findViewById(R.id.dashboard_button_settings).setOnClickListener(dBClickListener);
	    findViewById(R.id.dashboard_button_playGame).setOnClickListener(dBClickListener);
	    findViewById(R.id.dashboard_button_highScores).setOnClickListener(dBClickListener);
	    findViewById(R.id.dashboard_button_howToPlay).setOnClickListener(dBClickListener);
	}
	
	/**
	 * Handle clicks on dashboard buttons
	 */
	private class DashboardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.dashboard_button_settings:
                	Toast.makeText(getBaseContext(), "Settings", Toast.LENGTH_SHORT).show();
                	i = new Intent(v.getContext(), Settings.class);
                	Log.i(TAG,"Entering Settings");
                    break;
                case R.id.dashboard_button_playGame:
                	Toast.makeText(getBaseContext(), "Play Game", Toast.LENGTH_SHORT).show();
                	//Stub
                    break;
                case R.id.dashboard_button_highScores:
                	Toast.makeText(getBaseContext(), "High Scores", Toast.LENGTH_SHORT).show();
                	//Stub
                    break;
                case R.id.dashboard_button_howToPlay:
                	Toast.makeText(getBaseContext(), "How To Play", Toast.LENGTH_SHORT).show();
                	i = new Intent(v.getContext(), HowToPlay.class);
                	Log.i(TAG,"Entering HowToPlay");
                	break;
                default:
                    break;
            }
            if(i != null) {
                startActivity(i);
            }
        }
    }

}