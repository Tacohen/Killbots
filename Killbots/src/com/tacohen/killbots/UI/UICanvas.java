package com.tacohen.killbots.UI;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
 
import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import org.andengine.entity.sprite.Sprite;

import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.Toast;


import org.andengine.input.touch.TouchEvent;

import com.google.common.collect.HashMultimap;
import com.tacohen.cloud.Cloud;
import com.tacohen.killbots.Logic.CurrentPlayerLocation;
import com.tacohen.killbots.Logic.GridDimensions;
import com.tacohen.killbots.Logic.MoveRobots;
import com.tacohen.killbots.Logic.PlayerMovement;
import com.tacohen.killbots.Logic.RobotLocations;

/**
 * License notes: uses graphics (the arrows) from picol.org, and they want a linkback
 * License notes: uses graphics (characters,background) from KDE, I should be okay as long as this app is GPL-licensed
 * @author Timothy Cohen
 *
 */

public class UICanvas extends SimpleBaseGameActivity {
	
	String TAG = "UICanvas";
	
	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;
	
	private ITextureRegion mBackgroundTextureRegion, playerTexture, robotTexture, deadRobotTexture, leftArrow,rightArrow,upArrow,downArrow;
	private Sprite leftArrowSprite,rightArrowSprite,downArrowSprite,upArrowSprite,player, robot1, robot2, deadRobot;
	
	private PlayerMovement playerMovement = new PlayerMovement();
	private MoveRobots moveRobots = new MoveRobots();
	
	private Integer robotCount;
	
	private Integer score;

	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		try {
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		        	GridDimensions.setHeight(10);
		        	GridDimensions.setWidth(10);
		        	GridDimensions.setGridDimensions(new Pair<Integer,Integer>(10,10));
		            return getAssets().open("gfx/background-resized-10x10.png");   
		        }
		    });
		    ITexture player = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/player.png");
		        }
		    });
		    ITexture robot = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/robot.png");
		        }
		    });
		    ITexture deadRobot = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/deadRobot.png");
		        }
		    });
		    ITexture leftArrow = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/left_arrow.png");
		        }
		    });
		    ITexture rightArrow = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/right_arrow.png");
		        }
		    });
		    ITexture upArrow = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/up_arrow.png");
		        }
		    });
		    ITexture downArrow = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/down_arrow.png");
		        }
		    });
		    
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    player.load();
		    robot.load();
		    deadRobot.load();
		    leftArrow.load();
		    rightArrow.load();
		    upArrow.load();
		    downArrow.load();
		    
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.playerTexture = TextureRegionFactory.extractFromTexture(player);
		    this.robotTexture = TextureRegionFactory.extractFromTexture(robot);
		    this.deadRobotTexture = TextureRegionFactory.extractFromTexture(deadRobot);
		    this.playerTexture = TextureRegionFactory.extractFromTexture(player);
		    this.leftArrow = TextureRegionFactory.extractFromTexture(leftArrow);
		    this.rightArrow = TextureRegionFactory.extractFromTexture(rightArrow);
		    this.upArrow = TextureRegionFactory.extractFromTexture(upArrow);
		    this.downArrow = TextureRegionFactory.extractFromTexture(downArrow);
		    
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		
		
	}

	@Override
	protected Scene onCreateScene() {
		// 1 - Create new scene
		final Scene scene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(backgroundSprite);
		
		robotCount = 0;
		//scoring rules: player starts with 100, with -1 for each turn, -5 for each teleport, and +10 for each
		//robot killed
		score = 100;
		
		
		//1.5, tacohen, attach the arrows directly to the background
		leftArrowSprite = new Sprite(530,100, this.leftArrow, getVertexBufferObjectManager()){
			@Override
	        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				//Only move the player sprite once the user lifts his/her finger to prevent accidental multi-touching
				case MotionEvent.ACTION_UP:{
					Log.i(TAG, "Touched Left Arrow");
					score -= 1;
					Log.i(TAG, "Player moved, score is now: "+score);
					if (playerMovement.canPlayerMoveLeft(new Pair<Integer, Integer>(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()))){
						player.setPosition(player.getX()-47,player.getY());
						CurrentPlayerLocation.setPlayerLocation(CurrentPlayerLocation.getPlayerXLocation()-1, CurrentPlayerLocation.getPlayerYLocation());
						Pair<Integer, Integer> robotLoc = RobotLocations.liveRobotLocations.get(0);
						Log.i(TAG,"old robot1 location is: "+robotLoc.first+","+robotLoc.second);
						Log.i(TAG, "new player location is: "+CurrentPlayerLocation.getPlayerXLocation()+","+CurrentPlayerLocation.getPlayerYLocation());
						Pair<Integer,Integer> newRobotLocation = moveRobots.desiredRobotLocation(robotLoc.first, robotLoc.second,CurrentPlayerLocation.getPlayerXLocation()+1,CurrentPlayerLocation.getPlayerYLocation());
						Log.i(TAG,"new robot1 location is:"+newRobotLocation.first+" , "+newRobotLocation.second);
						robot1.setPosition(newRobotLocation.first*47, newRobotLocation.second*47);
						RobotLocations.moveRobotLocation(0,newRobotLocation.first, newRobotLocation.second);
						
						Pair<Integer, Integer> robotLoc2 = RobotLocations.liveRobotLocations.get(1);
						Log.i(TAG,"old robot2 location is: "+robotLoc2.first+","+robotLoc2.second);
						Pair<Integer,Integer> newRobotLocation2 = moveRobots.desiredRobotLocation(robotLoc2.first, robotLoc2.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()+1);
						Log.i(TAG,"new robot2 location is:"+newRobotLocation2.first+" , "+newRobotLocation2.second);
						robot2.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
						RobotLocations.moveRobotLocation(1,newRobotLocation2.first, newRobotLocation2.second);
					
					
						if (RobotLocations.liveRobotLocations().get(0).equals(CurrentPlayerLocation.currentPlayerLocation)){
							Log.i(TAG, "Player has died!");
							lose();
							//Toast.makeText(getApplicationContext(), "Player has died!", Toast.LENGTH_LONG).show();
						}
						
						if (RobotLocations.liveRobotLocations.get(1).equals(RobotLocations.liveRobotLocations.get(0))){
							Log.i(TAG, "Robots have collided!");
							score += 10;
							Log.i(TAG, "Player killed two robots, score is now: "+score);
							robotCount -= 2;
							scene.detachChild(robot1);
							scene.detachChild(robot2);
							deadRobot.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
							RobotLocations.setDeadRobotLocation(newRobotLocation2.first,newRobotLocation2.second);
							if (robotCount == 0){
								win();
							}
						}
					}
				}
				}

				return true;
			}
		};
		scene.attachChild(leftArrowSprite);
		
		rightArrowSprite = new Sprite(680,100, this.rightArrow, getVertexBufferObjectManager()){
			@Override
	        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				//Only move the player sprite once the user lifts his/her finger to prevent accidental multi-touching
				case MotionEvent.ACTION_UP:{
					Log.i(TAG, "Touched Right Arrow");
					score -= 1;
					Log.i(TAG, "Player moved, score is now: "+score);
					if (playerMovement.canPlayerMoveRight(new Pair<Integer, Integer>(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()))){
						player.setPosition(player.getX()+47,player.getY());
						CurrentPlayerLocation.setPlayerLocation(CurrentPlayerLocation.getPlayerXLocation()+1, CurrentPlayerLocation.getPlayerYLocation());
						
						Pair<Integer, Integer> robotLoc = RobotLocations.liveRobotLocations.get(0);
						Log.i(TAG,"old robot1 location is: "+robotLoc.first+","+robotLoc.second);
						Log.i(TAG, "new player location is: "+CurrentPlayerLocation.getPlayerXLocation()+","+CurrentPlayerLocation.getPlayerYLocation());
						Pair<Integer,Integer> newRobotLocation = moveRobots.desiredRobotLocation(robotLoc.first, robotLoc.second,CurrentPlayerLocation.getPlayerXLocation()-1,CurrentPlayerLocation.getPlayerYLocation());
						Log.i(TAG,"new robot1 location is:"+newRobotLocation.first+" , "+newRobotLocation.second);
						robot1.setPosition(newRobotLocation.first*47, newRobotLocation.second*47);
						RobotLocations.moveRobotLocation(0,newRobotLocation.first, newRobotLocation.second);
						
						Pair<Integer, Integer> robotLoc2 = RobotLocations.liveRobotLocations.get(1);
						Log.i(TAG,"old robot2 location is: "+robotLoc2.first+","+robotLoc2.second);
						Pair<Integer,Integer> newRobotLocation2 = moveRobots.desiredRobotLocation(robotLoc2.first, robotLoc2.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()+1);
						Log.i(TAG,"new robot2 location is:"+newRobotLocation2.first+" , "+newRobotLocation2.second);
						robot2.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
						RobotLocations.moveRobotLocation(1,newRobotLocation2.first, newRobotLocation2.second);
					
						
						if (RobotLocations.liveRobotLocations().get(0).equals(CurrentPlayerLocation.currentPlayerLocation)){
							Log.i(TAG, "Player has died!");
							lose();
							//Toast.makeText(getApplicationContext(), "Player has died!", Toast.LENGTH_LONG).show();
						}
						
						if (RobotLocations.liveRobotLocations.get(1).equals(RobotLocations.liveRobotLocations.get(0))){
							Log.i(TAG, "Robots have collided!");
							score += 10;
							Log.i(TAG, "Player killed two robots, score is now: "+score);
							robotCount -= 2;
							scene.detachChild(robot1);
							scene.detachChild(robot2);
							deadRobot.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
							RobotLocations.setDeadRobotLocation(newRobotLocation2.first,newRobotLocation2.second);
							if (robotCount == 0){
								win();
							}
						}
					}
				}
				}

				return true;
			}
		};
		scene.attachChild(rightArrowSprite);
		
		upArrowSprite = new Sprite(605,0, this.upArrow, getVertexBufferObjectManager()){
			@Override
	        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				//Only move the player sprite once the user lifts his/her finger to prevent accidental multi-touching
				case MotionEvent.ACTION_UP:{
					Log.i(TAG, "Touched Up Arrow");
					score -= 1;
					Log.i(TAG, "Player moved, score is now: "+score);
					if (playerMovement.canPlayerMoveUp(new Pair<Integer, Integer>(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()))){
						player.setPosition(player.getX(),player.getY()-47);
						CurrentPlayerLocation.setPlayerLocation(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()-1);
						
						Pair<Integer, Integer> robotLoc = RobotLocations.liveRobotLocations.get(0);
						Log.i(TAG,"old robot1 location is: "+robotLoc.first+","+robotLoc.second);
						Log.i(TAG, "new player location is: "+CurrentPlayerLocation.getPlayerXLocation()+","+CurrentPlayerLocation.getPlayerYLocation());
						Pair<Integer,Integer> newRobotLocation = moveRobots.desiredRobotLocation(robotLoc.first, robotLoc.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()-1);
						Log.i(TAG,"new robot1 location is:"+newRobotLocation.first+" , "+newRobotLocation.second);
						robot1.setPosition(newRobotLocation.first*47, newRobotLocation.second*47);
						RobotLocations.moveRobotLocation(0,newRobotLocation.first, newRobotLocation.second);
						
						Pair<Integer, Integer> robotLoc2 = RobotLocations.liveRobotLocations.get(1);
						Log.i(TAG,"old robot2 location is: "+robotLoc2.first+","+robotLoc2.second);
						Pair<Integer,Integer> newRobotLocation2 = moveRobots.desiredRobotLocation(robotLoc2.first, robotLoc2.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()+1);
						Log.i(TAG,"new robot2 location is:"+newRobotLocation2.first+" , "+newRobotLocation2.second);
						robot2.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
						RobotLocations.moveRobotLocation(1,newRobotLocation2.first, newRobotLocation2.second);
					
					
						if (RobotLocations.liveRobotLocations().get(0).equals(CurrentPlayerLocation.currentPlayerLocation)){
							Log.i(TAG, "Player has died!");
							lose();
							//Toast.makeText(getApplicationContext(), "Player has died!", Toast.LENGTH_LONG).show();
						}
						
						if (RobotLocations.liveRobotLocations.get(1).equals(RobotLocations.liveRobotLocations.get(0))){
							Log.i(TAG, "Robots have collided!");
							score += 10;
							Log.i(TAG, "Player killed two robots, score is now: "+score);
							robotCount -= 2;
							scene.detachChild(robot1);
							scene.detachChild(robot2);
							deadRobot.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
							RobotLocations.setDeadRobotLocation(newRobotLocation2.first,newRobotLocation2.second);
							if (robotCount == 0){
								win();
							}
						}
					}
				}
				}

				return true;
			}
		};
		scene.attachChild(upArrowSprite);
		
		downArrowSprite = new Sprite(605,200, this.downArrow, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				//Only move the player sprite once the user lifts his/her finger to prevent accidental multi-touching
				case MotionEvent.ACTION_UP:{
					Log.i(TAG, "Touched Down Arrow");
					score -= 1;
					Log.i(TAG, "Player moved, score is now: "+score);
					if (playerMovement.canPlayerMoveDown(new Pair<Integer, Integer>(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()))){
						player.setPosition(player.getX(),player.getY()+47);
						CurrentPlayerLocation.setPlayerLocation(CurrentPlayerLocation.getPlayerXLocation(), CurrentPlayerLocation.getPlayerYLocation()+1);
						
						Pair<Integer, Integer> robotLoc = RobotLocations.liveRobotLocations.get(0);
						Log.i(TAG,"old robot1 location is: "+robotLoc.first+","+robotLoc.second);
						Log.i(TAG, "new player location is: "+CurrentPlayerLocation.getPlayerXLocation()+","+CurrentPlayerLocation.getPlayerYLocation());
						Pair<Integer,Integer> newRobotLocation = moveRobots.desiredRobotLocation(robotLoc.first, robotLoc.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()+1);
						Log.i(TAG,"new robot1 location is:"+newRobotLocation.first+" , "+newRobotLocation.second);
						robot1.setPosition(newRobotLocation.first*47, newRobotLocation.second*47);
						RobotLocations.moveRobotLocation(0,newRobotLocation.first, newRobotLocation.second);
						
						Pair<Integer, Integer> robotLoc2 = RobotLocations.liveRobotLocations.get(1);
						Log.i(TAG,"old robot2 location is: "+robotLoc2.first+","+robotLoc2.second);
						Pair<Integer,Integer> newRobotLocation2 = moveRobots.desiredRobotLocation(robotLoc2.first, robotLoc2.second,CurrentPlayerLocation.getPlayerXLocation(),CurrentPlayerLocation.getPlayerYLocation()+1);
						Log.i(TAG,"new robot2 location is:"+newRobotLocation2.first+" , "+newRobotLocation2.second);
						robot2.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
						RobotLocations.moveRobotLocation(1,newRobotLocation2.first, newRobotLocation2.second);
						
						if (RobotLocations.liveRobotLocations.get(1).equals(RobotLocations.liveRobotLocations.get(0))){
							Log.i(TAG, "Robots have collided!");
							score += 10;
							Log.i(TAG, "Player killed two robots, score is now: "+score);
							robotCount -= 2;
							scene.detachChild(robot1);
							scene.detachChild(robot2);
							deadRobot.setPosition(newRobotLocation2.first*47, newRobotLocation2.second*47);
							RobotLocations.setDeadRobotLocation(newRobotLocation2.first,newRobotLocation2.second);
							if (robotCount == 0){
								win();
							}
						}
					
						if (RobotLocations.liveRobotLocations().get(0).equals(CurrentPlayerLocation.currentPlayerLocation)){
							Log.i(TAG, "Player has died!");
							lose();
							//Toast.makeText(getApplicationContext(), "Player has died!", Toast.LENGTH_LONG).show();
						}
						
						if (RobotLocations.liveRobotLocations().get(1).equals(CurrentPlayerLocation.currentPlayerLocation)){
							Log.i(TAG, "Player has died!");
							lose();
							//Toast.makeText(getApplicationContext(), "Player has died!", Toast.LENGTH_LONG).show();
						}
					}
				}
				}

				return true;
			}
		};
		scene.attachChild(downArrowSprite);
		//tacohen note: top left grid square is (1,0), NOT (0,0)!!!!!
		//tacohen note: (280,130) places the player at 6x7 on the grid
		//tacohen note: every square in the any direction is 47 pixels (e.g. 6x7 is (280,130),
		//7x6 is (327,177)
		//UIObject player = new UIObject(1, 327, 177, this.playerTexture, getVertexBufferObjectManager()) {
		//player = new Sprite(327,177, this.playerTexture, getVertexBufferObjectManager());// {
		//CurrentPlayerLocation.setPlayerLocation(6, 5);
		player = new Sprite(376,0, this.playerTexture, getVertexBufferObjectManager());
		CurrentPlayerLocation.setPlayerLocation(8, 0);
		
		//Placeholder location values, replace later!
		robot1 = new Sprite(329,224, this.robotTexture, getVertexBufferObjectManager());
		RobotLocations.setRobotLocation(7,5);
		robotCount += 1;
		
		robot2 = new Sprite(423,224, this.robotTexture, getVertexBufferObjectManager());
		RobotLocations.setRobotLocation(9,5);
		robotCount += 1;
		
		//Place dead robot way off the screen edge do it can't be seen until needed!
		deadRobot = new Sprite(900,900, this.deadRobotTexture, getVertexBufferObjectManager());// {
		RobotLocations.setDeadRobotLocation(11,11);

		scene.attachChild(player);
		scene.attachChild(robot1);
		scene.attachChild(robot2);
		scene.attachChild(deadRobot);
		
		//Add touch handlers
		scene.registerTouchArea(downArrowSprite);
		scene.registerTouchArea(leftArrowSprite);
		scene.registerTouchArea(rightArrowSprite);
		scene.registerTouchArea(upArrowSprite);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		
		return scene;
	}
	
	public void lose(){
		Log.i(TAG, "Player has lost!");
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	    		Toast.makeText(getApplicationContext(), "You Died! Your score is: "+score, Toast.LENGTH_LONG).show();
	    		getHighScores();
	        }
	    });
	}
	
	public void win(){
		Log.i(TAG, "Player has won!");
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	    		Toast.makeText(getApplicationContext(), "You Won! Your score is: "+score, Toast.LENGTH_LONG).show();
	    		getHighScores();
	        }
	    });
	}
	
	public void getHighScores(){
		  new Thread(new Runnable() {
   			@Override
   			public void run() {
   				Cloud c = new Cloud();
   				HashMultimap scores = c.getHighScores();
   			}
   		}).start();
	}

}
