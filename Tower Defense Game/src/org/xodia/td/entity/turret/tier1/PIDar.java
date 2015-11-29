package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.turret.BasicEnergyTurret;
import org.xodia.td.entity.turret.Turret;

// Cannot buff the following turrets
// SubSecuritySystem
// Wall
// PIDar
public class PIDar extends BasicEnergyTurret{

	private float attackSpeedGiven = 0.5f;
	private float maxHealthGiven = 0f;
	private float maxShieldGiven = 0f;
	
	public PIDar(float x, float y) {
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.PIDARII);
		setRange(128);
		setAttackSpeed(1950);
		setTotalAmmo(50);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(12);
		setDamage(15);
		setMaxHealth(110);
		setCurrentHealth(getMaxHealth());
		setArmor(0);
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.PIDAR_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
	public float getAttackSpeedGiven(){ return attackSpeedGiven; }
	public float getMaxShieldGiven(){ return maxShieldGiven; }
	public float getMaxHealthGiven(){ return maxHealthGiven; }
	
}
