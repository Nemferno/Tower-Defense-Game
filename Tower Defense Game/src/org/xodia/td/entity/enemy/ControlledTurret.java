package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.xodia.td.Resources;
import org.xodia.td.entity.Bullet;
import org.xodia.td.entity.Bullet.BulletType;
import org.xodia.td.util.CustomMath;


public class ControlledTurret extends BasicEnemy{

	private Circle rangeBound;
	
	private ArrayList<Bullet> aliveBullets;
	
	private boolean isBuffed;
	
	private float buffAttackSpeed;
	private float buffMaxHealth;
	private float buffMaxShield;
	
	/**
	 * 
	 * This constructor is not needed, since it is static and is a turret.
	 * Thus, this eliminates the following method parameters:
	 * 
	 * origMap, pathList, initTarget, isAggressor parameters
	 * 
	 */
	public ControlledTurret(float x, float y) {
		super(null, null, null, x, y, null, false);
		
		setMoneyGiven(10);
		setUpgradeGiven(0);
		
		isSpotted = false;
		
		aliveBullets = new ArrayList<Bullet>();
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.CONTROLLEDTURRET_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}

	public void update(int delta) {
		if(getTarget() != null){
			if(getTarget().getBounds().intersects(rangeBound)){
				if(rateOfFire.isTimeElapsed()){
					aliveBullets.add(new Bullet(getBounds().getCenterX(), getBounds().getCenterY(),
							getTarget().getBounds().getCenterX(), getTarget().getBounds().getCenterY(),
							getDamage(), BulletType.NORMAL));
					
					rateOfFire.reset();
				}
			}else{
				setTarget(null);
			}
		}
		
		rateOfFire.tick(delta);
		
		if(getTarget() != null){
			setAngle(CustomMath.getAngle(getBounds().getCenterX(), getBounds().getCenterY(), 
					getTarget().getBounds().getCenterX(), getTarget().getBounds().getCenterY()));
		}
	}
	
	public void setBuffed(boolean buffed){ isBuffed = buffed; }
	
	public void setBuffAttackSpeed(float speed){ buffAttackSpeed = speed; }
	public void setBuffMaxHealth(float health){ buffMaxHealth = health; }
	public void setBuffMaxShield(float shield){ buffMaxShield = shield; }
	
	public boolean isBuffed(){
		return isBuffed;
	}
	
	public float getBuffAttackSpeed(){
		return buffAttackSpeed;
	}
	
	public float getBuffMaxShield(){
		return buffMaxShield;
	}
	
	public float getBuffMaxHealth(){
		return buffMaxHealth;
	}
	
	public void clearBullets(){
		ArrayList<Bullet> deadBullets = new ArrayList<Bullet>();
		
		for(Bullet b : aliveBullets){
			if(!b.isAlive()){
				deadBullets.add(b);
			}
		}
		
		aliveBullets.removeAll(deadBullets);
	}
	
	public ArrayList<Bullet> getBullets(){
		return aliveBullets;
	}
	
}
