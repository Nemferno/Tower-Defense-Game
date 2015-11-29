package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.Bullet;
import org.xodia.td.entity.Bullet.BulletType;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;

public class FrostByte extends BasicTurret{

	private BulletType old;
	
	private float slowPercentage;
	private float painDamage;
	
	public FrostByte(float x, float y) {
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.FROSTBYTEII);
		setRange(128);
		setAttackSpeed(1650);
		setTotalAmmo(150);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(10);
		setRestockCost(8);
		setDamage(12);
		setMaxHealth(75);
		setCurrentHealth(getMaxHealth());
		setBulletType(BulletType.SLOW);
		setArmor(0);
		
		slowPercentage = 0.25f;
		painDamage = 2;
	}
	
	public void update(int delta){
		super.update(delta); // Call super class' update method since it controls currentState
		
		// Check all the bullets for slow ... and etc
		for(Bullet b : aliveBullets){
			if(b.isAlive()){
				if(b.getType() == BulletType.SLOW){
					if(!b.isOut()){
						// Check is the health of the turret is half way meaning
						// it can't use its abilities
						if(currentState == TurretState.NORMAL){
							if(bulletType == BulletType.NORMAL)
								b.setType(old);
							
							b.setPainDamage(painDamage);
							b.setSlowRate(slowPercentage);
						}else if(currentState == TurretState.SEMIDAMAGED){
							old = bulletType;
							b.setType(BulletType.NORMAL);
						}
					}
				}
			}
		}
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.FROSTBYTE_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
	public void setSlowPercentage(float percentage){
		slowPercentage = percentage;
	}
	
	public void setPainDamage(float damage){
		painDamage = damage;
	}
	
	public float getSlowPercentage(){
		return slowPercentage;
	}
	
	public float getPainDamage(){
		return painDamage;
	}

}
