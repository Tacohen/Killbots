package com.tacohen.killbots.UI;

import java.util.concurrent.CountDownLatch;

import com.tacohen.cloud.Cloud;
import com.tacohen.killbots.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameFinish extends Activity{
	
	private static String TAG = "GameFinish";
	private Integer place;
	private Integer score;
	private Boolean isScoreHigh;
	private Boolean didWin;
	//private static Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloud_communication);
		
		//ctx = getBaseContext();
		
		Bundle extras = getIntent().getExtras();
		score = extras.getInt("score");
		didWin = extras.getBoolean("didWin");
		Log.i(TAG, "score is: "+score);
		Log.i(TAG, "Did player win? "+didWin);
		TextView scoreText = (TextView) findViewById(R.id.scoreTextWaiting);
		scoreText.setText("Your score was: "+score);
		
		if(didWin){
			TextView winText = (TextView) findViewById(R.id.loseTextWaiting);
	        winText.setVisibility(View.GONE);
			win();
		}else{
			TextView loseText = (TextView) findViewById(R.id.winTextWaiting);
	        loseText.setVisibility(View.GONE);
			lose();
		}
	}
	
	public void lose(){
		final Context ctx = this;
		Log.i(TAG, "Player has lost!");
		Log.i(TAG, "Is this a high score? "+IsScoreHigh());
		if (IsScoreHigh()){
			final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("You got a high score! Do you want to have it displayed on our server?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					Intent i = new Intent(GameFinish.this,AddHighScore.class);
					i.putExtra("score", score);
					i.putExtra("place", GetPlace());
					startActivityForResult(i,0);
					finish();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), Opening.class));
					finish();
				}
			});

			final AlertDialog alert = builder.create();
			alert.show();     
		}
		else{
			final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("Unfortunately, you did not get a high score. Do you want to play again, or view the high scores list?")
			.setCancelable(false)
			.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), UICanvas.class));
					finish();
				}
			})
			.setNegativeButton("View High Scores", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), HighScores.class));
					finish();
				}
			});

			final AlertDialog alert = builder.create();
			alert.show();
		}
	}
	    		

	
	public void win(){
		Log.i(TAG, "Player has won!");
		Log.i(TAG, "Is this a high score? "+IsScoreHigh());
		final Context ctx = this;
		if (IsScoreHigh()){
			final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("You got a high score! Do you want to have it displayed on our server?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					Intent i = new Intent(GameFinish.this,AddHighScore.class);
					i.putExtra("score", score);
					i.putExtra("place", GetPlace());
					startActivityForResult(i,0);
					finish();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), Opening.class));
					finish();
				}
			});

			final AlertDialog alert = builder.create();
			alert.show();     
		}else{
			final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("Unfortunately, you did not get a high score. Do you want to play again, or view the high scores list?")
			.setCancelable(false)
			.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), UICanvas.class));
					finish();
				}
			})
			.setNegativeButton("View High Scores", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					dialog.cancel();
					startActivity(new Intent(getBaseContext(), HighScores.class));
					finish();
				}
			});

			final AlertDialog alert = builder.create();
			alert.show();
			
		}
	}
	
	public Boolean IsScoreHigh(){
		final CountDownLatch latch = new CountDownLatch(1);
	    Thread scoreThread = new HandlerThread("ScoreHandler"){
	        @Override
	        public synchronized void run(){
	            Cloud c = new Cloud();
	            isScoreHigh = c.isHighScore(score);
	            latch.countDown(); // Release await() in the UI thread.
	        }
	    };
	    scoreThread.start();
	    try {
			latch.await();
		} catch (InterruptedException e) {
			Log.e(TAG, "Interrupted contecting cloud!");
			isScoreHigh = false;//Assume false if we can't get actual value
		} // Wait for countDown() in the thread
	    return isScoreHigh;
	}
	
	public Integer GetPlace(){
		final CountDownLatch latch = new CountDownLatch(1);
	    Thread scoreThread = new HandlerThread("ScoreHandler"){
	        @Override
	        public synchronized void run(){
	            Cloud c = new Cloud();
	            place = c.getPlace(score);
	            latch.countDown(); // Release await() in the UI thread.
	        }
	    };
	    scoreThread.start();
	    try {
			latch.await();
		} catch (InterruptedException e) {
			Log.e(TAG, "Interrupted contecting cloud!");
			place = 6;//Assume 6 if we can't get actual value
		} // Wait for countDown() in the thread
	    return place;
	}

}
