package org.xodia.td.entity.turret;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.xodia.td.Resources;

// Should this be considered as a turret?
public class Wall extends BasicTurret{

	public Wall(float x, float y){
		super(x, y);
		
		setMaxHealth(500);
		setCurrentHealth(getMaxHealth());
		setAbilityToControl(false);
	}
	
	public void render(Graphics g) {
		g.drawImage(Resources.WALL_IMAGE, getX(), getY());
	}

	public Shape getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
