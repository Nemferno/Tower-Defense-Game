package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Broodling extends BasicEnemy{

	public Broodling(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		setMoneyGiven(1);
		setUpgradeGiven(0);
		setMaxHealth(10);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(250);
		setSpeed(FAST_SPEED);
		setDamage(2);
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.BROODLING_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}

}
