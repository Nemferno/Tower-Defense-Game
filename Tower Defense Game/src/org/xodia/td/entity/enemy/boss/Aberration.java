package org.xodia.td.entity.enemy.boss;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.attribute.BossAttribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Aberration extends BasicBoss{
	
	public Aberration(BasicMap origMap, ArrayList<Path> pathList, TempMap map, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, map, x, y, initTarget, isAggressor);
		
		setMaxHealth(325);
		setCurrentHealth(getMaxHealth());
		setMaxShield(150);
		setCurrentShield(getMaxShield());
		setDamage(50);
		setAttackSpeed(1000);
		setUpgradeGiven(5);
		setMoneyGiven(125);
		setSpeed(SLOW_SPEED);
		
		addBossAttribute(BossAttribute.RESURRECTION);
		addBossAttribute(BossAttribute.DOUBLEATTACK);
		addBossAttribute(BossAttribute.MINIMIZER);
		addBossAttribute(BossAttribute.ENLARGER);
		
		setAttribute(getBossAttributeList().get(new Random().nextInt(getBossAttributeList().size())));
	}
	
	
}
