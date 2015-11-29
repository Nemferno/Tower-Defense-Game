package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.turret.tier1.AntiTroll;

public class AntiTrollIII extends AntiTroll{

	public AntiTrollIII(float x, float y){
		super(x, y);
		
		setMaxHealth(120);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(500);
		setDamage(5);
		setArmor(1);
		setTotalAmmo(550);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(5);
		setRange(160);
	}
	
}
