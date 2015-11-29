package org.xodia.td.entity.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.attribute.Attribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class DarkRover extends BasicEnemy{
	
	// Type 0 == Blade
	// Type 1 == Dual Scythe
	private int type;
	
	public DarkRover(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		setMoneyGiven(35);
		setUpgradeGiven(2);
		setMaxHealth(25);
		setCurrentHealth(getMaxHealth());
		
		Random rand = new Random();
		type = rand.nextInt(2);
		
		if(type == 0)
			setAttackSpeed(1000);
		else if(type == 1)
			setAttackSpeed(500);
			
		setSpeed(NORMAL_SPEED);
		setDamage(15);
		setAttribute(Attribute.CLOAK);
		setSpotted(false);
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.DARKROVER_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}

}
