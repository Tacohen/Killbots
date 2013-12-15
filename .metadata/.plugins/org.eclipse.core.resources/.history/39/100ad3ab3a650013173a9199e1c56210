package com.tacohen.cloud;

import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

public class OtherPlayerLocation {
	private static String TAG = "CurrentPlayerLocation";
	private static Cloud c;
	public static Pair<Integer, Integer> otherPlayerLocation;
	private static boolean usingMultiplayer = false;

	
	public OtherPlayerLocation(Context context){
		super();
		 c = new Cloud();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean using_multiplayer = sharedPreferences.getBoolean("multiplayer", false);
		if (using_multiplayer){
			usingMultiplayer = true;
		}

	}
	
	public void setPlayerLocation(Integer xValue, Integer yValue,final int playerNumber){
		//currentPlayerLocation = Pair.create(xValue, yValue);
		if (usingMultiplayer){
			Log.i(TAG, "Using the cloud!");
		{   
		    final CountDownLatch latch = new CountDownLatch(1);
		    final int[] loc = new int[2];
		    loc[0] = xValue;
		    loc[1] = yValue;
		    Thread uiThread = new HandlerThread("UIHandler"){
		        @Override
		        public synchronized void run(){
		            c.sendPlayerLocation(playerNumber, loc[0], loc[1]);
		            latch.countDown(); // Release await() in the test thread.
		        }
		    };
		    uiThread.start();
		    try {
				latch.await();
			} catch (InterruptedException e) {

			} // Wait for countDown() in the UI thread.
		    
		}
		}
		else{
			//if local
			otherPlayerLocation = Pair.create(xValue, yValue);
		}
	}
	
	public int getPlayerXLocation(final int playerNumber){
		//return currentPlayerLocation.first;
		if(usingMultiplayer){
			Log.i(TAG, "Using the cloud!");
		{   
		    final CountDownLatch latch = new CountDownLatch(1);
		    final int[] xLoc = new int[1];
		    Thread uiThread = new HandlerThread("UIHandler"){
		        @Override
		        public synchronized void run(){
		            xLoc[0] = c.getPlayerLocation(playerNumber).first;
		            latch.countDown(); // Release await() in the test thread.
		        }
		    };
		    uiThread.start();
		    try {
				latch.await();
				return xLoc[0];
			} catch (InterruptedException e) {
				Log.e(TAG, "Could not get player location, assuming 5!");
				return 5;
			} // Wait for countDown() in the UI thread.
		}
		}else{
			return otherPlayerLocation.first;
		}
	}
	public int getPlayerYLocation(final int playerNumber){
		//return currentPlayerLocation.second;
		if (usingMultiplayer){
			Log.i(TAG, "Using the cloud!");
		{   
		    final CountDownLatch latch = new CountDownLatch(1);
		    final int[] xLoc = new int[1];
		    Thread uiThread = new HandlerThread("UIHandler"){
		        @Override
		        public synchronized void run(){
		            xLoc[0] = c.getPlayerLocation(playerNumber).second;
		            latch.countDown(); // Release await() in the test thread.
		        }
		    };
		    uiThread.start();
		    try {
				latch.await();
				return xLoc[0];
			} catch (InterruptedException e) {
				Log.e(TAG, "Could not get player location, assuming 5!");
				return 5;
			} // Wait for countDown() in the UI thread.
		}
		}
		else{
			//if local
			return otherPlayerLocation.second;
		}
	}
	
	public Pair<Integer,Integer> getPlayerLocation(final int playerNumber){
		if (usingMultiplayer){
			Log.i(TAG, "Using the cloud!");
		{   
		    final CountDownLatch latch = new CountDownLatch(1);
		    final int[] loc = new int[2];
		    Thread uiThread = new HandlerThread("UIHandler"){
		        @Override
		        public synchronized void run(){
		            loc[0] = c.getPlayerLocation(playerNumber).first;
		            loc[1] = c.getPlayerLocation(playerNumber).second;
		            latch.countDown(); // Release await() in the test thread.
		        }
		    };
		    uiThread.start();
		    try {
				latch.await();
				return Pair.create(loc[0],loc[1]);
			} catch (InterruptedException e) {
				Log.e(TAG, "Could not get player location, assuming (5,5)!");
				return Pair.create(5, 5);
			} // Wait for countDown() in the UI thread.
		}
		}
		else{
			//if local
			return otherPlayerLocation;
		}
	}
	
}

