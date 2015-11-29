package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Immortal extends BasicEnemy{

	public Immortal(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		setMoneyGiven(20);
		setUpgradeGiven(0);
		setMaxHealth(100);
		setCurrentHealth(getMaxHealth());
		setMaxShield(200);
		setCurrentShield(getMaxShield());
		setAttackSpeed(2500);
		setSpeed(SLOW_SPEED);
		setDamage(20);
		setHasShieldRegen(true);
		setArmor(1);
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.IMMORTAL_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
