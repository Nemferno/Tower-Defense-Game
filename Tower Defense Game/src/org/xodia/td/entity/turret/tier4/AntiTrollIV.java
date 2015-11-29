package org.xodia.td.entity.turret.tier4;

import org.xodia.td.entity.turret.tier1.AntiTroll;

public class AntiTrollIV extends AntiTroll{

	public AntiTrollIV(float x, float y){
		super(x, y);
		
		setMaxHealth(150);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(500);
		setDamage(6);
		setArmor(2);
		setTotalAmmo(600);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(6);
		setRange(160);
	}
	
}
