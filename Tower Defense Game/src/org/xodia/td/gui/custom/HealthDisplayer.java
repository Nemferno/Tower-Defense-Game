package org.xodia.td.gui.custom;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.xodia.td.Resources;
import org.xodia.td.entity.GameObject;

public class HealthDisplayer {

	private Image image;
	private GameObject object;
	
	public HealthDisplayer(GameObject object) {
		setObject(object);
	}
	
	public void setObject(GameObject object){
		this.object = object;
		
		if(object.getPortraitImage() != null){
			image = object.getPortraitImage();
		}else{
			image = Resources.UNAVAILABLE_PORTRAIT;
		}
	}
	
	public void render(GUIContext gc, Graphics g) throws SlickException{
		g.drawImage(image, 0, 0);
		
		if(object != null){
			g.setColor(Color.black);
			g.drawRect(150, 0 + 50, 350 * (object.getCurrentHealth() / object.getMaxHealth()), 15);
			
			g.setColor(Color.red);
			g.fillRect(150, 0 + 50, 350 * (object.getCurrentHealth() / object.getMaxHealth()), 15);
			
			g.setColor(Color.white);
			g.drawString(object.getClass().getSimpleName(), 150, 10);
			g.drawString((int) object.getCurrentHealth() + "/" + (int) object.getMaxHealth(), 100 + 175, 0 + 50);
		}
	}

}
