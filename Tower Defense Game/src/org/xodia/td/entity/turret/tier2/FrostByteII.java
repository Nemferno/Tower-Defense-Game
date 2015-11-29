package org.xodia.td.entity.turret.tier2;

import org.xodia.td.entity.Bullet.BulletType;
import org.xodia.td.entity.turret.tier1.FrostByte;

public class FrostByteII extends FrostByte{

	public FrostByteII(float x, float y){
		super(x, y);
		
		setRange(128);
		setAttackSpeed(1700);
		setTotalAmmo(175);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(10);
		setRestockCost(5);
		setArmor(1);
		setDamage(15);
		setMaxHealth(78);
		setCurrentHealth(getMaxHealth());
		setBulletType(BulletType.SLOWBLEEDING);
	}
	
} 
