package com.tacohen.killbots.Logic;

import android.util.Pair;

public interface IPlayerMovement {
	/**
	 * Can player move left?
	 * @param Pair representing player's current location
	 * @return boolean
	 */
	boolean canPlayerMoveLeft(Pair<Integer, Integer> p);
	/**
	 * Can player move right?
	 * @param Pair representing player's current location
	 * @return boolean
	 */
	boolean canPlayerMoveRight(Pair<Integer, Integer> p);
	/**
	 * Can player move up?
	 * @param Pair representing player's current location
	 * @return boolean
	 */
	boolean canPlayerMoveUp(Pair<Integer, Integer> p);
	/**
	 * Can player move down?
	 * @param Pair representing player's current location
	 * @return boolean
	 */
	boolean canPlayerMoveDown(Pair<Integer, Integer> p);
	/**
	 * Is the player trapped?
	 * @param Pair representing player's current location
	 * @return boolean
	 */
	boolean isPlayerTrapped(int playerNumber);	
	/**
	 * Set the player's new position
	 * @param Pair representing player's new location
	 */
	void setPlayerPosition(Pair<Integer, Integer> p, int playerNumber);
	/**
	 * Get the player's position
	 * @param Pair representing player's current location
	 * @return Pair representing player's current location
	 */
	Pair<Integer, Integer> getPlayerPosition(Pair<Integer, Integer> p,
			int playerNumber);
	/**
	 * Is there a robot at this location?
	 * @param Pair representing location in question
	 * @return boolean
	 */
	boolean isRobotHere(Pair<Integer, Integer> p);
	/**
	 * Is there a dead robot at this location?
	 * @param Pair representing location in question
	 * @return boolean
	 */
	boolean isDeadRobotHere(Pair<Integer, Integer> p);
	/**
	 * Teleport player to random position on the board.New position
	 * is not guaranteed to be safe. Remember to set the new location
	 * afterwards!
	 * @return Pair representing new player location
	 */
	Pair<Integer,Integer> teleportPlayer();
	
	
}
