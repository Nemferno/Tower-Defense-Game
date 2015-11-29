package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.turret.tier1.Canon;

public class CanonIII extends Canon{

	public CanonIII(float x, float y) {
		super(x, y);
		
		setMaxHealth(60);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(1000);
		setDamage(7);
		setArmor(1);
		setTotalAmmo(700);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(5);
		setRestockCost(9);
		setRange(108);
	}

}
