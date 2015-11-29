package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;

public class Tera extends BasicTurret{

	public Tera(float x, float y){
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.TERAII);
		setRange(64);
		setAttackSpeed(250);
		setTotalAmmo(1000);
		setCurrentAmmo(getMaxAmmo());
		setMaxShield(175);
		setCurrentShield(getMaxShield());
		setRepairCost(1);
		setRestockCost(2);
		setDamage(1);
		setMaxHealth(10);
		setCurrentHealth(getMaxHealth());
		setArmor(2);
		setHasShieldRegen(true);
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.TERA_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
