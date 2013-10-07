package com.tacohen.killbots.Logic;

import android.util.Pair;

public class CurrentPlayerLocation {

		public static Pair<Integer, Integer> currentPlayerLocation;
		
		public static void setPlayerLocation(Integer xValue, Integer yValue){
			currentPlayerLocation = Pair.create(xValue, yValue);
		}
}
