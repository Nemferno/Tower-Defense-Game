package org.xodia.td.util;

import org.newdawn.slick.Graphics;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;

public class Camera {
	
	private float cameraX;
	private float cameraY;
	
	private float screenWidth;
	private float screenHeight;
	
	private BasicMap map;
	
	public Camera(BasicMap map, float screenWidth, float screenHeight){
		this.map = map;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void setMap(BasicMap map){
		this.map = map;
	}
	
	public void centerOnLocation(float x, float y){
		cameraX = x - screenWidth / 2;
		cameraY = y - screenHeight / 2;
		
		if(cameraX < 0){
			cameraX = 0;
		}
		
		if(cameraX + screenWidth > map.getWidthInTiles() * 32 + 150){
			cameraX = (map.getWidthInTiles() * 32) - screenWidth + 150;
		}
		
		if(cameraY < 0 - 75){
			cameraY = 0 - 75;
		}
		
		if(cameraY + screenHeight > map.getHeightInTiles() * 32 + 120){
			cameraY = (map.getHeightInTiles() * 32) - screenHeight + 120;
		}
		
	}
	
	public void centerOnObject(GameObject object){
		centerOnLocation(object.getBounds().getCenterX(),
				object.getBounds().getCenterY());
	}
	
	public void translate(Graphics g){
		g.translate(-cameraX, -cameraY);
	}
	
	public void detranslate(Graphics g){
		g.translate(cameraX, cameraY);
	}
	
	public float getCameraX(){
		return cameraX;
	}
	
	public float getCameraY(){
		return cameraY;
	}
}
