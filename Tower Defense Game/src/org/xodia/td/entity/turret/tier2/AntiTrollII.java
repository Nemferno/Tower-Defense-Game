package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.turret.tier1.AntiTroll;

public class AntiTrollII extends AntiTroll{

	public AntiTrollII(float x, float y){
		super(x, y);
		
		setRange(128);
		setAttackSpeed(500);
		setTotalAmmo(500);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(5);
		setArmor(1);
		setDamage(4);
		setMaxHealth(115);
		setCurrentHealth(getMaxHealth());
	}
	
}
