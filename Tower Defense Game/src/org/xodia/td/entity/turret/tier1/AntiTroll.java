package org.xodia.td.entity.turret.tier1;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;
import org.xodia.td.entity.turret.BasicEnergyTurret;
import org.xodia.td.entity.turret.Turret;

public class AntiTroll extends BasicEnergyTurret{

	public AntiTroll(float x, float y) {
		super(x, y);
		
		setUpgradeable(true);
		setUpgradedTurret(Turret.ANTITROLLII);
		setRange(128);
		setAttackSpeed(500);
		setTotalAmmo(500);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(5);
		setDamage(3);
		setMaxHealth(110);
		setCurrentHealth(getMaxHealth());
		setArmor(0);
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.ANTITROLL_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
	public void update(int delta){
		super.update(delta); // Call super class' update method since it controls currentState
		
		if(hasEnergy()){
			isStealthSpotter = true;
		}else{
			isStealthSpotter = false;
		}
	}

}
