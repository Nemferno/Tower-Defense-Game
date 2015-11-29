package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.turret.tier1.PIDar;

public class PIDarII extends PIDar{

	public PIDarII(float x, float y){
		super(x, y);
		
		setRange(128);
		setAttackSpeed(1925);
		setTotalAmmo(75);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(12);
		setArmor(1);
		setDamage(15);
		setMaxHealth(115);
		setCurrentHealth(getMaxHealth());
	}
	
}
