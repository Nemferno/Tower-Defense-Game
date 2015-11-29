package org.xodia.td.util;

// Has all the basic math that calculates it
public class CustomMath {

	public static float getAngle(float startX, float startY, float targetX, float targetY){
		float xDistance = targetX - startX;
		float yDistance = targetY - startY;
		
		return (float) Math.toDegrees(Math.atan2(yDistance, xDistance));
	}
	
}
