package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.attribute.Attribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Worm extends BasicEnemy{

	public Worm(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x, float y,
			GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);

		setMoneyGiven(25);
		setUpgradeGiven(0);
		setMaxHealth(25);
		setCurrentHealth(getMaxHealth());
		setDamage(3);
		setSpeed(FAST_SPEED);
		setAttackSpeed(750);
		setAttribute(Attribute.BORNBRINGER);
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.WORM_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
