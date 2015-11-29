package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.turret.tier1.Canon;

public class CanonII extends Canon{

	public CanonII(float x, float y) {
		super(x, y);
		
		setRange(96);
		setAttackSpeed(1000);
		setTotalAmmo(600);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(5);
		setRestockCost(7);
		setArmor(1);
		setDamage(6);
		setMaxHealth(55);
		setCurrentHealth(getMaxHealth());
	}

}
