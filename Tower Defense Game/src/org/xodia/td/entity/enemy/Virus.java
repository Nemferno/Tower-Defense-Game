package org.xodia.td.entity.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Virus extends BasicEnemy{

	private boolean hasWeapon;
	
	public Virus(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		Random rand = new Random();
		hasWeapon = rand.nextBoolean();
		
		setMoneyGiven(10);
		setUpgradeGiven(0);
		setMaxHealth(35);
		setCurrentHealth(getMaxHealth());
		setArmor(0);
		
		if(hasWeapon){
			setAttackSpeed(1000);
			setDamage(9);
		}else{
			setAttackSpeed(500);
			setDamage(5);
		}

		setSpeed(NORMAL_SPEED);
	}

	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.VIRUS_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}
	
}
