package org.xodia.td.entity.turret.tier4;

import org.xodia.td.entity.turret.tier1.Tera;

public class TeraIV extends Tera{

	public TeraIV(float x, float y){
		super(x, y);
		
		setMaxShield(250);
		setCurrentShield(getMaxShield());
		setMaxHealth(10);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(200);
		setDamage(5);
		setArmor(3);
		setTotalAmmo(1400);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(1);
		setRestockCost(6);
		setRange(64);
	}
	
}
