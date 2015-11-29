package org.xodia.td.entity.turret.tier3;

import org.xodia.td.entity.Bullet.BulletType;
import org.xodia.td.entity.turret.tier1.FrostByte;

public class FrostByteIII extends FrostByte{

	public FrostByteIII(float x, float y){
		super(x, y);
		
		setMaxHealth(85);
		setCurrentHealth(getMaxHealth());
		setDamage(17);
		setAttackSpeed(1710);
		setArmor(1);
		setTotalAmmo(200);
		setCurrentAmmo(getMaxAmmo());
		setRepairCost(10);
		setRestockCost(8);
		setRange(160);
		setBulletType(BulletType.SLOWICEBLEEDING);
	}
	
}
