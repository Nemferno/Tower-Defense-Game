package org.xodia.td.entity.turret;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.xodia.td.util.CustomCountdownTimer;

// 500 cost (cost increases by 250 after each use)
public class AntiMine extends BasicTurret{

	private CustomCountdownTimer delayTimer;
	
	public AntiMine(float x, float y){
		super(x, y);
		
		setMaxHealth(1);
		setCurrentHealth(getMaxHealth());
		setDamage(450);
		
		delayTimer = new CustomCountdownTimer(1000);
		delayTimer.start();
	}
	
	public void render(Graphics g) {
		g.setColor(Color.cyan);
		g.draw(getBounds());
	}

	public void update(int delta) {
		delayTimer.tick(delta);
	}
	
	public boolean isDelayElapsed(){
		return delayTimer.isTimeElapsed();
	}

	public Shape getBounds() {
		return new Rectangle(getX(), getY(), 32, 32);
	}

}
