package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.turret.tier1.Tera;

public class TeraII extends Tera{

	public TeraII(float x, float y){
		super(x, y);
		
		setRange(64);
		setAttackSpeed(250);
		setTotalAmmo(1175);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(1);
		setRestockCost(4);
		setArmor(2);
		setDamage(2);
		setMaxShield(180);
		setCurrentShield(getMaxShield());
		setMaxHealth(10);
		setCurrentHealth(getMaxHealth());
	}
	
}
