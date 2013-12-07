package com.tacohen.killbots.Logic;

import java.util.List;
import java.util.Random;

import android.util.Log;
import android.util.Pair;

public class PlayerMovement implements IPlayerMovement{

	private static String TAG = "PlayerMovement";

	@Override
	public boolean canPlayerMoveLeft(Pair<Integer, Integer> p) {
		if (p.first == 1){
			Log.i(TAG, "Cannot move left, at edge of board");
			return false;
		}
		if (isRobotHere(Pair.create(p.first-1, p.second))){
			Log.i(TAG, "Cannot move left, robot is here");
			return false;
		}
		if (isDeadRobotHere(Pair.create(p.first-1, p.second))){
			Log.i(TAG, "Cannot move left, dead robot is here");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPlayerMoveRight(Pair<Integer, Integer> p) {
		if (p.first == 10){
			Log.i(TAG, "Cannot move right, at edge of board");
			return false;
		}
		if (isRobotHere(Pair.create(p.first+1, p.second))){
			Log.i(TAG, "Cannot move right, robot is here");
			return false;
		}
		if (isDeadRobotHere(Pair.create(p.first+1, p.second))){
			Log.i(TAG, "Cannot move right, dead robot is here");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPlayerMoveUp(Pair<Integer, Integer> p) {
		if (p.second == 0){
			Log.i(TAG, "Cannot move up, at edge of board");
			return false;
		}
		if (isRobotHere(Pair.create(p.first, p.second+1))){
			Log.i(TAG, "Cannot move up, robot is here");
			return false;
		}
		if (isDeadRobotHere(Pair.create(p.first, p.second+1))){
			Log.i(TAG, "Cannot move up, dead robot is here");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPlayerMoveDown(Pair<Integer, Integer> p) {
		if (p.second == 9){
			Log.i(TAG, "Cannot move down, at edge of board");
			return false;
		}
		if (isRobotHere(Pair.create(p.first, p.second-1))){
			Log.i(TAG, "Cannot move down, robot is here");
			return false;
		}
		if (isDeadRobotHere(Pair.create(p.first, p.second-1))){
			Log.i(TAG, "Cannot move down, dead robot is here");
			return false;
		}
		return true;
	}

	@Override
	public boolean isPlayerTrapped() {
		Integer currentXValue = CurrentPlayerLocation.currentPlayerLocation.first;
		Integer currentYValue = CurrentPlayerLocation.currentPlayerLocation.second;
		Pair<Integer,Integer> curLoc = Pair.create(currentXValue,currentYValue);
		if ((canPlayerMoveUp(curLoc))|(canPlayerMoveDown(curLoc))|
				(canPlayerMoveLeft(curLoc))|(canPlayerMoveRight(curLoc))){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public void setPlayerPosition(Pair<Integer, Integer> p) {
		CurrentPlayerLocation.setPlayerLocation(p.first,p.second);
		
	}

	@Override
	public Pair<Integer, Integer> getPlayerPosition(Pair<Integer, Integer> p) {
		return CurrentPlayerLocation.currentPlayerLocation;
	}

	@Override
	public boolean isRobotHere(Pair<Integer, Integer> p) {
		List<Pair<Integer,Integer>> robotsList = RobotLocations.liveRobotLocations();
		if (robotsList.contains(p)){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public boolean isDeadRobotHere(Pair<Integer, Integer> p) {
		List<Pair<Integer,Integer>> robotsList = RobotLocations.deadRobotLocations();
		if (robotsList.contains(p)){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Pair<Integer, Integer> teleportPlayer() {
		Random r = new Random();
		Integer newXlocation = r.nextInt(GridDimensions.height - 0);
		Integer newYlocation = r.nextInt(GridDimensions.width - 0);
		return Pair.create(newXlocation, newYlocation);
	}

}
