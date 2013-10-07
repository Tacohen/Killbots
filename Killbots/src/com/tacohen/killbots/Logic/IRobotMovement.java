package com.tacohen.killbots.Logic;

import android.util.Pair;

public interface IRobotMovement {
	
		/**
		 * Can robot move left?
		 * @param Pair representing robot's current location
		 * @return boolean
		 */
		boolean canRobotMoveLeft(Pair<Integer, Integer> p);
		/**
		 * Can robot move right?
		 * @param Pair representing robot's current location
		 * @return boolean
		 */
		boolean canRobotMoveRight(Pair<Integer, Integer> p);
		/**
		 * Can robot move up?
		 * @param Pair representing robot's current location
		 * @return boolean
		 */
		boolean canRobotMoveUp(Pair<Integer, Integer> p);
		/**
		 * Can robot move down?
		 * @param Pair representing robot's current location
		 * @return boolean
		 */
		boolean canRobotMoveDown(Pair<Integer, Integer> p);
		/**
		 * Is there another robot in this location?
		 * @param Pair representing location desired
		 * @return boolean
		 */
		boolean isRobotHere(Pair<Integer, Integer> p);
		/**
		 * Is there a dead robot in this location?
		 * @param Pair representing location desired
		 * @return boolean
		 */
		boolean isDeadRobotHere(Pair<Integer, Integer> p);
		/**
		 * Get the robot's position
		 * @param Robot's id number
		 * @return pair
		 */
		Pair<Integer, Integer> getRobotPosition(int id);
		/**
		 * Set the robot's position
		 * @param Robot's id number, desired location
		 */
		void setRobotPosition(int id, Pair<Integer, Integer> p);
		/**
		 * Set the robot to dead
		 * @param Robot's id number
		 */
		void setRobotToDead(int id);
		/**
		 * Get the robot's death status
		 * @param Robot's id number
		 * @return boolean
		 */
		boolean isDead(int id);
		/**
		 * Get the robot's id number
		 * @return Robot's id number
		 */
		int getRobotID();
		/**
		 * Set the robot's id number position
		 * @param Robot's new id number
		 */
		void setRobotID(int id);
		/**
		 * Get the player's position
		 * @return pair
		 */
		Pair<Integer, Integer> getPlayerPosition();
		/**
		 * Get a single direction to the player
		 * @return "up,"down","left,"right"
		 */
		String getPlayerDirection();
		/**
		 * Set the player as dead
		 */
		void killPlayer();

}
