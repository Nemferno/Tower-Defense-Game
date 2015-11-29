package org.xodia.td.entity.turret.tier4;

import org.xodia.td.entity.turret.tier1.PIDar;

public class PIDarIV extends PIDar{

	public PIDarIV(float x, float y){
		super(x, y);
		
		setMaxHealth(140);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(1925);
		setDamage(17);
		setArmor(2);
		setTotalAmmo(115);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(15);
		setRestockCost(12);
		setRange(160);
	}
	
}
