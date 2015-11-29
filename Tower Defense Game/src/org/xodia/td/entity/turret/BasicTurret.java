package org.xodia.td.entity.turret;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.xodia.td.Resources;
import org.xodia.td.entity.Bullet;
import org.xodia.td.entity.Bullet.BulletType;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.enemy.BasicEnemy;
import org.xodia.td.util.CustomMath;

public abstract class BasicTurret extends GameObject{
	
	public static enum TurretState { DAMAGED, SEMIDAMAGED, NORMAL }
	
	protected BulletType bulletType;
	
	protected BasicEnemy enemyAttackingIt;
	
	protected boolean canBeControlled;
	protected boolean isManuallyControlled;
	
	protected boolean isUpgradeable;
	protected Turret upgradeTurret;
	
	protected Circle turretRange;
	protected BasicEnemy target;
	
	protected List<Bullet> aliveBullets;
		
	protected int currentAmmo;
	protected int totalAmmo;
	
	protected int repairCost;
	protected int restockCost;
	
	// Temp variables that contains values of the original before the buff
	protected boolean hasStoredOriginalValues;
	protected float tempAttackSpeed;
	protected float tempMaxHealth;
	protected float tempMaxShield;
	
	// Tells the game that the turret has already
	// changed its stats
	protected boolean hasBuffedTurret;
	
	// Tells the game that it is being buffed
	protected boolean isBuffed;
	
	// All Buff Variables
	protected boolean isStealthSpotter;
	protected boolean needRepair;
	protected boolean isUnabledToFire;
	protected boolean hasNoAmmo;
	
	protected float buffAttackSpeed;
	protected float buffMaxHealth;
	protected float buffMaxShield;
	
	protected int buffVisible;
	
	protected TurretState currentState;
	
	public BasicTurret(float x, float y){
		aliveBullets = new ArrayList<Bullet>();
		turretRange = new Circle(x + 16, y + 16, 0);
		
		setX(x);
		setY(y);
		setAbilityToControl(true);
		setAlive(true);
		setAngle(90);
		
		currentState = TurretState.NORMAL;
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.draw(getBounds());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
		
		buffVisible = 0;
		
		if(isBuffed){
			
			g.drawImage(Resources.BUFF_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
		
		if(isStealthSpotter){
			
			g.drawImage(Resources.STEALTHSPOTTER_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
		
		if(needRepair){

			g.drawImage(Resources.REPAIR_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
		
		if(isUnabledToFire){

			g.drawImage(Resources.UNABLEFIRE_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
		
		if(hasNoAmmo){

			g.drawImage(Resources.NOAMMO_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
	}
	
	// Can only control current state
	public void update(int delta){
		if(getCurrentHealth() <= getMaxHealth() * 0.25){
			currentState = TurretState.DAMAGED;
			needRepair = true;
			isUnabledToFire = true;
		}else if(getCurrentHealth() <= getMaxHealth() * 0.50 && getCurrentHealth() > getMaxHealth() * 0.25){
			currentState = TurretState.SEMIDAMAGED;
			needRepair = false;
			isUnabledToFire = false;
		}else if(getCurrentHealth() <= getMaxHealth() && getCurrentHealth() > getMaxHealth() * 0.50){
			currentState = TurretState.NORMAL;
			needRepair = false;
			isUnabledToFire = false;
		}
		
		if(getCurrentAmmo() <= 0){
			hasNoAmmo = true;
		}
		
		if(isBuffed){
			if(!hasBuffedTurret){
				if(!hasStoredOriginalValues){
					tempAttackSpeed = attackSpeed;
					tempMaxHealth = maxHealth;
					tempMaxShield = maxShield;
					
					hasStoredOriginalValues = true;
					
					// After we have stored the variables
					// we have to now buff it
					if(buffMaxHealth != 0){
						incMaxHealth(buffMaxHealth);
						incCurrentHealth(buffMaxHealth);
					}
					
					if(buffMaxShield != 0){
						incMaxShield(buffMaxShield);
						incCurrentShield(buffMaxShield);
					}
					
					if(buffAttackSpeed != 0)
						setAttackSpeed(getAttackSpeed() + (getAttackSpeed() * buffAttackSpeed));
					
					hasBuffedTurret = true;
				}
			}
		}else{
			if(hasBuffedTurret){
				if(hasStoredOriginalValues){
					// Revert back
					
					if(buffMaxHealth != 0){
						incMaxHealth(-buffMaxHealth);
						incCurrentHealth(-buffMaxHealth);
						
						if(getCurrentHealth() <= 0){
							setCurrentHealth(1);
						}
					}
					
					if(buffMaxShield != 0){
						incMaxShield(-buffMaxShield);
						incCurrentShield(-buffMaxShield);
						
						if(getCurrentShield() <= 0){
							setCurrentHealth(1);
						}
					}
					
					if(buffAttackSpeed != 0)
						setAttackSpeed(tempAttackSpeed);
					
					hasStoredOriginalValues = false;
					hasBuffedTurret = false;
				}
			}
		}
		
		if(!isManuallyControlled){
			if(currentState != TurretState.DAMAGED){
				if(getTarget() != null){
					if(getTarget().getBounds().intersects(getRange())){
						if(rateOfFire.isTimeElapsed()){
							if(!hasNoAmmo){
								aliveBullets.add(new Bullet(getBounds().getCenterX(), getBounds().getCenterY(),
										getTarget().getBounds().getCenterX(), getTarget().getBounds().getCenterY(),
										getDamage(), bulletType));
								
								rateOfFire.reset();
								
								incCurrentAmmo(-1);
							}
						}
					}else{
						setTarget(null);
					}
				}
			}
		}
		
		if(getTarget() != null){
			setAngle(CustomMath.getAngle(getBounds().getCenterX(), getBounds().getCenterY(), 
					getTarget().getBounds().getCenterX(), getTarget().getBounds().getCenterY()));
		}
		
		if(hasShieldRegen){
			if(shieldRegenTimer.isTimeElapsed()){
				if(getCurrentShield() < getMaxShield()){
					incCurrentShield(1);
				}
			}else{
				shieldRegenTimer.tick(delta);
			}
		}
		
		rateOfFire.tick(delta);
	}
	
	public void manualFire(float mouseX, float mouseY){
		if(isManuallyControlled){
			if(currentState != TurretState.DAMAGED){
				if(rateOfFire.isTimeElapsed()){
					if(!hasNoAmmo){
						aliveBullets.add(new Bullet(getBounds().getCenterX(), getBounds().getCenterY(),
								mouseX, mouseY, getDamage(), bulletType));
						
						rateOfFire.reset();
						
						incCurrentAmmo(-1);
					}
				}
			}
		}
	}
	
	public void setAbilityToControl(boolean control){ canBeControlled = control; }
	public void setControlledManually(boolean control){ isManuallyControlled = control; }
	
	public void setBulletType(BulletType type){ bulletType = type; }
	
	public void setEnemyAttackingTurret(BasicEnemy enemy){ this.enemyAttackingIt = enemy; }
	
	public void setUpgradeable(boolean upgradeable){ isUpgradeable = upgradeable; }
	public void setUpgradedTurret(Turret turret){ upgradeTurret = turret; }
	
	public void setBuffed(boolean buffed){ isBuffed = buffed; }
	
	public void setBuffAttackSpeed(float speed){ buffAttackSpeed = speed; }
	public void setBuffMaxHealth(float health){ buffMaxHealth = health; }
	public void setBuffMaxShield(float shield){ buffMaxShield = shield; }
	
	public void setState(TurretState state){ currentState = state; }
		
	public void setRestockCost(int cost){ restockCost = cost; }
	public void incRestockCost(int cost){ restockCost += cost; }
	
	public void setCurrentAmmo(int ammo){ currentAmmo = ammo; }
	public void incCurrentAmmo(int ammo){ currentAmmo += ammo; }
	
	public void setTotalAmmo(int ammo){ totalAmmo = ammo; }
	public void incTotalAmmo(int ammo){ totalAmmo += ammo; }
	
	public void setRepairCost(int cost){ repairCost = cost; }
	public void incRepairCost(int cost){ repairCost += cost; }
	
	public void setTarget(BasicEnemy target){ this.target = target;}
	
	public void setRange(int radius){
		turretRange.setRadius(radius);
		turretRange.setCenterX(x + 16);
		turretRange.setCenterY(y + 16);
	}
	
	public boolean canBeControlled(){
		return canBeControlled;
	}
	
	public boolean isManuallyControlled(){
		return isManuallyControlled;
	}
	
	public BulletType getBulletType(){
		return bulletType;
	}
	
	public BasicEnemy getEnemyAttackingIt(){
		return enemyAttackingIt;
	}
	
	public boolean isUpgradeable(){
		return isUpgradeable;
	}
	
	public Turret getUpgradeTurret(){
		return upgradeTurret;
	}
	
	public TurretState getCurrentState(){
		return currentState;
	}
	
	public void clearBullets(){
		List<Bullet> bullets = new ArrayList<Bullet>();
		
		for(Bullet b : aliveBullets){
			if(!b.isAlive()){
				bullets.add(b);
			}
		}
		
		aliveBullets.removeAll(bullets);
	}
	
	public List<Bullet> getBullets(){
		return aliveBullets;
	}
	
	public BasicEnemy getTarget(){
		if(target != null && target.isAlive())
			return target;
		else
			return null;
	}
	
	public Circle getRange(){
		return turretRange;
	}
	
	// To make sure the buff is not stacked 2 or more times
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
	
	public int getRestockCost(){
		return restockCost;
	}
	
	public int getCurrentAmmo(){
		return currentAmmo;
	}
	
	public int getMaxAmmo(){
		return totalAmmo;
	}
	
	public int getRepairCost(){
		return repairCost;
	}

	public Shape getBounds() {
		return new Rectangle(x, y, 32, 32);
	}
	
}
