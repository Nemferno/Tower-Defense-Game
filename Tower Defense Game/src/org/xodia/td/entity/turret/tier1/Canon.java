package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;

public class Canon extends BasicTurret{

	public Canon(float x, float y){
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.CANONII);
		setRange(96);
		setAttackSpeed(1000);
		setTotalAmmo(500);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(5);
		setRestockCost(5);
		setDamage(5);
		setMaxHealth(50);
		setCurrentHealth(getMaxHealth());
		setArmor(0);
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.CANON_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
