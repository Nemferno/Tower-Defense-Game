package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.turret.tier1.ReCyNergy;

public class ReCyNergyIII extends ReCyNergy{

	public ReCyNergyIII(float x, float y){
		super(x, y);
		
		setMaxHealth(100);
		setCurrentHealth(getMaxHealth());
		setArmor(2);
		setRepairCost(12);
		setRange(192);
		setMaxShield(80);
		setCurrentShield(getMaxShield());
	}
	
}
