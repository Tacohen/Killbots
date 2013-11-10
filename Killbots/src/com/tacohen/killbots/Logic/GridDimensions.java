package com.tacohen.killbots.Logic;

import android.util.Pair;

public class GridDimensions {
	
	public static Pair<Integer, Integer> gridDimensions;
	public static int width;
	public static int height;
	public static Pair<Integer, Integer> getGridDimensions() {
		return gridDimensions;
	}
	public static void setGridDimensions(Pair<Integer, Integer> gridDimensions) {
		GridDimensions.gridDimensions = gridDimensions;
	}
	public static int getWidth() {
		return width;
	}
	public static void setWidth(int width) {
		GridDimensions.width = width;
	}
	public static int getHeight() {
		return height;
	}
	public static void setHeight(int height) {
		GridDimensions.height = height;
	}
	
	

}
