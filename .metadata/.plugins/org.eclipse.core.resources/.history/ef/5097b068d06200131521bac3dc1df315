package com.tacohen.killbots.Logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.util.Pair;

public class RobotLocations{
	
	public static List<Pair<Integer, Integer>> liveRobotLocations = new ArrayList<Pair<Integer, Integer>>();
	public static List<Pair<Integer, Integer>> deadRobotLocations = new ArrayList<Pair<Integer, Integer>>();

	public static List<Pair<Integer, Integer>> liveRobotLocations() {
		return liveRobotLocations;
	}

	public static List<Pair<Integer, Integer>> deadRobotLocations() {
		return deadRobotLocations;
	}
	
	public static void setRobotLocation(Integer xValue, Integer yValue){
		liveRobotLocations.add(Pair.create(xValue, yValue));
	}
	
	public static void removeRobotLocation(Integer xValue, Integer yValue){
		//Since I can't use a remove all with pair<>, calling remove multiple times is the next best thing(in case several bots die on the same turn)
		liveRobotLocations.remove(Pair.create(xValue, yValue));
		liveRobotLocations.remove(Pair.create(xValue, yValue));
		liveRobotLocations.remove(Pair.create(xValue, yValue));
		liveRobotLocations.remove(Pair.create(xValue, yValue));
	}
	
	public static void moveRobotLocation(Integer index, Integer newxValue, Integer newyValue){
		//liveRobotLocations.remove(Pair.create(oldxValue, oldyValue));
		//liveRobotLocations.add(Pair.create(newxValue, newyValue));
		liveRobotLocations.set(index, (Pair.create(newxValue, newyValue)));
	}
	
	public static void setRobotDead(Integer xValue, Integer yValue){
		liveRobotLocations.remove(Pair.create(xValue, yValue));
		deadRobotLocations.add(Pair.create(xValue, yValue));
	}
	
	public static void setDeadRobotLocation(Integer xValue, Integer yValue){
		deadRobotLocations.add(Pair.create(xValue, yValue));
	}
	
	public static void removeDeadRobotLocation(Integer xValue, Integer yValue){
		deadRobotLocations.remove(Pair.create(xValue, yValue));
	}
	

}
