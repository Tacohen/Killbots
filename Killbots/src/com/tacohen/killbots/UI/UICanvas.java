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

import com.tacohen.killbots.Logic.Ring;

import java.util.Stack;
import org.andengine.input.touch.TouchEvent;


public class UICanvas extends SimpleBaseGameActivity {
	
	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;
	
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion,  player, robot, deadRobot;
	private Sprite mTower1, mTower2, mTower3, playerSprite;
	private Stack mStack1, mStack2, mStack3;
	
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
		            return getAssets().open("gfx/background-resized-10x10.png");
		        }
		    });
		    ITexture towerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/tower.png");
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
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    towerTexture.load();
		    player.load();
		    robot.load();
		    deadRobot.load();
		    
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.mTowerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
		    this.player = TextureRegionFactory.extractFromTexture(player);
		    this.robot = TextureRegionFactory.extractFromTexture(robot);
		    this.deadRobot = TextureRegionFactory.extractFromTexture(deadRobot);
		    
		 // 4 - Create the stacks
		    this.mStack1 = new Stack();
		    this.mStack2 = new Stack();
		    this.mStack3 = new Stack();
		    
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
		
		//1.5, tacohen, attach the player directly to the background
		playerSprite = new Sprite(150,150, this.player, getVertexBufferObjectManager());
		
		// 2 - Add the towers
		mTower1 = new Sprite(192, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower2 = new Sprite(400, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower3 = new Sprite(604, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(mTower1);
		scene.attachChild(mTower2);
		scene.attachChild(mTower3);
		
		// 3 - Create the rings
		//tacohen note: (280,130) places the player at 6x7 on the grid
		//tacohen note: every square in the x direction is 47 pixels (e.g. 6x7 is (280,130),
		//7x7 is (320,130)
		Ring player = new Ring(1, 280, 130, this.player, getVertexBufferObjectManager()) {
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 
		            pSceneTouchEvent.getY() - this.getHeight() / 2);
		        return true;
		    }
		};
		Ring robot = new Ring(2, 118, 212, this.robot, getVertexBufferObjectManager()) {
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if (((Ring) this.getmStack().peek()).getmWeight() != this.getmWeight())
		            return false;
		        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 
		            pSceneTouchEvent.getY() - this.getHeight() / 2);
		        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
		            checkForCollisionsWithTowers(this);
		        }
		        return true;
		    }
		};
		Ring deadRobot = new Ring(3, 97, 255, this.deadRobot, getVertexBufferObjectManager()) {
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if (((Ring) this.getmStack().peek()).getmWeight() != this.getmWeight())
		            return false;
		        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 
		            pSceneTouchEvent.getY() - this.getHeight() / 2);
		        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
		            checkForCollisionsWithTowers(this);
		        }
		        return true;
		    }
		};
		scene.attachChild(player);
		scene.attachChild(robot);
		scene.attachChild(deadRobot);
		
		// 4 - Add all rings to stack one
		this.mStack1.add(deadRobot);
		this.mStack1.add(robot);
		//this.mStack1.add(player);
		// 5 - Initialize starting position for each ring
		player.setmStack(mStack1);
		robot.setmStack(mStack1);
		deadRobot.setmStack(mStack1);
		//player.setmTower(mTower1);
		robot.setmTower(mTower1);
		deadRobot.setmTower(mTower1);
		// 6 - Add touch handlers
		scene.registerTouchArea(player);
		scene.registerTouchArea(robot);
		scene.registerTouchArea(deadRobot);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		return scene;
	}
	
	private void checkForCollisionsWithTowers(Ring ring) {
		Stack stack = null;
	    Sprite tower = null;
	    if (ring.collidesWith(mTower1) && (mStack1.size() == 0 ||             
	            ring.getmWeight() < ((Ring) mStack1.peek()).getmWeight())) {
	        stack = mStack1;
	        tower = mTower1;
	    } else if (ring.collidesWith(mTower2) && (mStack2.size() == 0 || 
	            ring.getmWeight() < ((Ring) mStack2.peek()).getmWeight())) {
	        stack = mStack2;
	        tower = mTower2;
	    } else if (ring.collidesWith(mTower3) && (mStack3.size() == 0 || 
	            ring.getmWeight() < ((Ring) mStack3.peek()).getmWeight())) {
	        stack = mStack3;
	        tower = mTower3;
	    } else {
	        stack = ring.getmStack();
	        tower = ring.getmTower();
	    }
	    ring.getmStack().remove(ring);
	    if (stack != null && tower !=null && stack.size() == 0) {
	        ring.setPosition(tower.getX() + tower.getWidth()/2 - 
	            ring.getWidth()/2, tower.getY() + tower.getHeight() - 
	            ring.getHeight());
	    } else if (stack != null && tower !=null && stack.size() > 0) {
	        ring.setPosition(tower.getX() + tower.getWidth()/2 - 
	            ring.getWidth()/2, ((Ring) stack.peek()).getY() - 
	            ring.getHeight());
	    }
	    stack.add(ring);
	    ring.setmStack(stack);
	    ring.setmTower(tower);
	}

}