package org.xodia.td.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.xodia.td.util.CustomCountdownTimer;

public class Bullet extends GameObject{

	public static enum BulletType {
		NORMAL, SLOW, SLOWBLEEDING, SLOWICEBLEEDING
	}
	
	// Describes to the game that it is already out and not in its creation process
	private boolean isOut;
	
	private Circle targetBounds;
	
	private CustomCountdownTimer deathTimer;
	
	private float slowRate;
	private float painDamage;
	
	private BulletType currentType;
	
	public Bullet(float x, float y, float targetX, float targetY, float damage, BulletType type){
		setX(x);
		setY(y);
		setSpeed(0.1f);
		setVelX(targetX - x);
		setVelY(targetY - y);
		setDamage(damage);
		setAlive(true);
		
		currentType = type;
		
		targetBounds = new Circle(targetX, targetY, 5);
		deathTimer = new CustomCountdownTimer(3000);
		deathTimer.start();
	}
	
	public void render(Graphics g){
		g.setColor(Color.red);
		g.draw(getBounds());
	}
	
	public void update(int delta){
		if(!isOut)
			isOut = true;
		
		setX(getX() + getVelX() * getSpeed());
		setY(getY() + getVelY() * getSpeed());
		
		if(getBounds().intersects(targetBounds)){
			setAlive(false);
		}
		
		if(deathTimer.isTimeElapsed()){
			setAlive(false);
		}
		
		deathTimer.tick(delta);
	}
	
	public void setSlowRate(float rate){
		this.slowRate = rate;
	}
	
	public void setPainDamage(float damage){
		painDamage = damage;
	}
	
	public void setType(BulletType type){
		this.currentType = type;
	}
	
	public boolean isOut(){
		return isOut;
	}
	
	public BulletType getType(){
		return currentType;
	}
	
	public float getSlowRate(){
		return slowRate;
	}
	
	public float getPainDamage(){
		return painDamage;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, 1, 1);
	}
	
}
