package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.turret.tier1.Tera;

public class TeraIII extends Tera{

	public TeraIII(float x, float y){
		super(x, y);
		
		setMaxShield(200); 
		setCurrentShield(getMaxShield());
		setMaxHealth(10);
		setCurrentHealth(getMaxHealth());
		setDamage(3);
		setAttackSpeed(250);
		setArmor(2);
		setTotalAmmo(1300);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(1);
		setRestockCost(6);
		setRange(64);
	}
	
}
