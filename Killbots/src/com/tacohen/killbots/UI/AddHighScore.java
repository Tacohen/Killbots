package com.tacohen.killbots.UI;

import java.util.concurrent.CountDownLatch;

import com.tacohen.cloud.Cloud;
import com.tacohen.killbots.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddHighScore extends Activity{
	
	private Context context;
	private static String TAG = "AddHighScore";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_score);
		
		this.context = getApplicationContext();
		
		Bundle extras = getIntent().getExtras();
		final int score = extras.getInt("score");
		final int place = extras.getInt("place");
		String placeName;
		
		//Polish the UI a little
		switch (place) {
		case 1:
			placeName = "first";
			break;
		case 2:
			placeName = "second";
			break;
		case 33:
			placeName = "third";
			break;
		case 4:
			placeName = "fourth";
			break;
		case 5:
			placeName = "fifth";
			break;
		default:
			placeName = "first";
		}
		
		
		TextView scoreText = (TextView) findViewById(R.id.scoreTextCongrats);
        scoreText.setText("Your score was: "+score);
        
		TextView placeText = (TextView) findViewById(R.id.placeTextCongrats);
        placeText.setText("Your score is good for "+placeName+" place!");
        
        
        Button button= (Button) findViewById(R.id.enterNameButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditText text = (EditText)findViewById(R.id.editName);
            	final String name = text.getText().toString();
                if (name == null){
    	    		Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_LONG).show();
                }else{
                	final CountDownLatch latch = new CountDownLatch(1);
            	    Thread scoreThread = new HandlerThread("ScoreHandler"){
            	        @Override
            	        public synchronized void run(){
            	            Cloud c = new Cloud();
            	            c.sendHighScore(name, score, place);
            	            latch.countDown(); // Release await() in the UI thread.
            	        }
            	    };
            	    scoreThread.start();
            	    try {
            			latch.await();
            			
            		} catch (InterruptedException e) {
            			Log.e(TAG, "Interrupted contecting cloud!");
            		} // Wait for countDown() in the thread
            	    Intent i = new Intent(AddHighScore.this,HighScores.class);
		    		startActivityForResult(i,0);

                	
                }
            }
        });
		
	}

}
