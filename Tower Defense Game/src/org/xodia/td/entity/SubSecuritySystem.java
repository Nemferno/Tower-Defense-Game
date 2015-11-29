package org.xodia.td.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.xodia.td.entity.enemy.BasicEnemy;
import org.xodia.td.util.CustomCountdownTimer;

public class SubSecuritySystem extends GameObject{

	protected BasicEnemy target;
	protected CustomCountdownTimer rateOfFire;
	
	public SubSecuritySystem(float x, float y){
		setX(x);
		setY(y);
		setAttackSpeed(5000);
		setDamage(50);
		setMaxHealth(1000);
		setCurrentHealth(getMaxHealth());
		setAlive(true);
		
		rateOfFire = new CustomCountdownTimer((int) getAttackSpeed());
		rateOfFire.start();
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.draw(getBounds());
	}

	public void update(int delta) {
		if(getTarget() != null){
			if(rateOfFire.isTimeElapsed()){
				if(target.getCurrentShield() <= 0)
					target.incCurrentHealth(-getDamage());
				else
					target.incCurrentShield(-getDamage());
				
				rateOfFire.reset();
			}
		}
		
		rateOfFire.tick(delta);
	}
	
	public void setTarget(BasicEnemy target){
		this.target = target;
	}
	
	public BasicEnemy getTarget(){
		if(target != null && target.isAlive()){
			return target;
		}else
			return null;
	}

	public Shape getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
