package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.turret.BasicGenerator;
import org.xodia.td.entity.turret.Turret;

public class ReCyNergy extends BasicGenerator{

	public ReCyNergy(float x, float y){
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.RECYNERGYII);
		setRepairCost(10);
		setMaxHealth(75);
		setCurrentHealth(getMaxHealth());
		setMaxShield(50);
		setCurrentShield(getCurrentShield());
		setHasShieldRegen(true);
		setRange(192);
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.RECYNERGY_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
