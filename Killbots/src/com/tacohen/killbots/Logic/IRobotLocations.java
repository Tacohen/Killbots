package com.tacohen.killbots.Logic;

import java.util.List;

import android.util.Pair;

public interface IRobotLocations {
	
	/**
	 * @return The locations of every live robot in the game
	 * as a list of location pairs
	 */
	public List<Pair<Integer, Integer>> liveRobotLocations();
	
	/**
	 * @return The locations of every dead robot in the game
	 * as a list of location pairs
	 */
	public List<Pair<Integer, Integer>> deadRobotLocations();
	
}
