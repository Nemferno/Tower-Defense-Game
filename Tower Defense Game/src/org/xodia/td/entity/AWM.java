package org.xodia.td.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * 
 * Is there a point of adding an object for AWM
 * It will explode and preform an animation.
 * 
 * Note: For now, this class will be deprecated
 * 
 * @author Jasper Bae
 *
 * @deprecated
 *
 */
public class AWM extends GameObject {

	public AWM(float x, float y){
		
	}
	
	public void render(Graphics g) {
		
	}

	// No update method since it explodes on impact!
	public void update(int delta) {}

	// Does not have bounds since it explodes on tile
	public Shape getBounds() {
		return new Rectangle(getX(), getY(), 0, 0);
	}

	
	
}
