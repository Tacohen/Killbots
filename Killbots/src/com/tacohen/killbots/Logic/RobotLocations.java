package com.tacohen.killbots.Logic;

import java.util.List;

import android.util.Pair;

public class RobotLocations{
	
	public static List<Pair<Integer, Integer>> liveRobotLocations;
	public static List<Pair<Integer, Integer>> deadRobotLocations;

	public static List<Pair<Integer, Integer>> liveRobotLocations() {
		return liveRobotLocations;
	}

	public static List<Pair<Integer, Integer>> deadRobotLocations() {
		return deadRobotLocations;
	}

}
