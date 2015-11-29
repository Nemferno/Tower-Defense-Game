package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.turret.tier1.ReCyNergy;

public class ReCyNergyII extends ReCyNergy{

	public ReCyNergyII(float x, float y){
		super(x, y);
		
		setRepairCost(10);
		setMaxHealth(75);
		setArmor(1);
		setCurrentHealth(getMaxHealth());
		setRange(192);
		setMaxShield(65);
		setCurrentShield(getMaxShield());
	}
	
}
