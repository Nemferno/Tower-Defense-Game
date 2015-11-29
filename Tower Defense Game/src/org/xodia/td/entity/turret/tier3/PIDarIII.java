package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.turret.tier1.PIDar;

public class PIDarIII extends PIDar{

	public PIDarIII(float x, float y){
		super(x, y);
		
		setMaxHealth(120);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(1925);
		setDamage(16);
		setArmor(1);
		setTotalAmmo(100);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(12);
		setRange(160);
	}
	
}
