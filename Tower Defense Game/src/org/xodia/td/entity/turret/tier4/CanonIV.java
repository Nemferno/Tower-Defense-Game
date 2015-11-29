package org.xodia.td.entity.turret.tier4;

import org.xodia.td.entity.turret.tier1.Canon;

public class CanonIV extends Canon{

	public CanonIV(float x, float y){
		super(x, y);
		
		setMaxHealth(75);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(1100);
		setDamage(10);
		setArmor(2);
		setTotalAmmo(800);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(6);
		setRestockCost(10);
		setRange(108);
	}
	
}
