package org.xodia.td.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.xodia.td.util.CustomCountdownTimer;

/**
 * 
 * Is it worth making a package of tiers?
 * 
 * 
 * @author Jasper Bae
 *
 */
public abstract class GameObject {

	public static enum UnitType { WATER, LAND }
	
	protected Image portaitImage;
	
	protected float x, y;
	protected float velX, velY;
	protected float speed;
	
	protected boolean isAlive;
	
	protected float armor;
	
	protected float maxHealth;
	protected float currentHealth;
	protected float maxShield;
	protected float currentShield;
	protected float attackSpeed;
	protected float damage;
	
	protected float angle;
	
	protected boolean hasShieldRegen;
	
	protected CustomCountdownTimer rateOfFire;
	protected CustomCountdownTimer shieldRegenTimer;
	
	public GameObject(){
		rateOfFire = new CustomCountdownTimer(0);
		rateOfFire.start();
		
		shieldRegenTimer = new CustomCountdownTimer(1000);
		shieldRegenTimer.start();
	}
	
	public void setPortraitImage(Image portrait){ portaitImage = portrait; }
	
	public void setHasShieldRegen(boolean regen){ hasShieldRegen = regen; }
	
	public void setAlive(boolean alive){ isAlive = alive; }
	
	public void setArmor(float armor){ this.armor = armor; }
	public void incArmor(float armor){ this.armor += armor; }
	
	public void setAngle(float angle){ this.angle = angle; }
	public void incAngle(float angle){ this.angle += angle; }
	
	public void setX(float x){ this.x = x; }
	public void setY(float y){ this.y = y; }
	public void incX(float x){ this.x += x; }
	public void incY(float y){ this.y += y; }
	
	public void setVelX(float velX){ this.velX = velX; }
	public void setVelY(float velY){ this.velY = velY; }
	public void incVelX(float velX){ this.velX += velX; }
	public void incVelY(float velY){ this.velY += velY; }
	
	public void setSpeed(float speed){ this.speed = speed; }
	public void incSpeed(float speed){ this.speed += speed; }
	
	public void setMaxHealth(float health){ maxHealth = health; }
	public void incMaxHealth(float health){ maxHealth += health; }
	
	public void setCurrentHealth(float health){ currentHealth = health; }
	public void incCurrentHealth(float health){ currentHealth += health; }
	
	public void setMaxShield(float shield){ maxShield = shield; }
	public void incMaxShield(float shield){ maxShield += shield; }
	
	public void setCurrentShield(float shield){ currentShield = shield; }
	public void incCurrentShield(float shield){ currentShield += shield; }
	
	public void setAttackSpeed(float attackSpeed){ 
		this.attackSpeed = attackSpeed; 
		
		rateOfFire.setTime((int) getAttackSpeed());
	}
	
	public void incAttackSpeed(float attackSpeed){ 
		this.attackSpeed += attackSpeed; 
		
		rateOfFire.setTime((int) getAttackSpeed());
	}
	
	public void setDamage(float damage){ this.damage = damage; }
	public void incDamage(float damage){ this.damage += damage; }
	
	public boolean isAlive(){ return isAlive; }
	
	public boolean hasShieldRegen(){ return hasShieldRegen; }
	
	public float getArmor(){ return armor; }
	public float getAngle(){ return angle; }
	
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getVelX(){ return velX; }
	public float getVelY(){ return velY; }
	public float getSpeed(){ return speed; }
	public float getMaxHealth(){ return maxHealth; }
	public float getCurrentHealth(){ return currentHealth; }
	public float getMaxShield(){ return maxShield; }
	public float getCurrentShield(){ return currentShield; }
	public float getAttackSpeed(){ return attackSpeed; }
	public float getDamage(){ return damage; }
	
	public Image getPortraitImage(){ return portaitImage; }
	
	public abstract void render(Graphics g);
	public abstract void update(int delta);
	public abstract Shape getBounds();
	
}
