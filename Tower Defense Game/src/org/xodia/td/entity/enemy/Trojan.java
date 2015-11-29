package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.attribute.IAttribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class Trojan extends BasicEnemy implements Cannibal{

	private IAttribute subAttribute;
	
	public Trojan(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x, float y,
			GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		setMoneyGiven(35);
		setUpgradeGiven(2);
		setMaxHealth(50);
		setCurrentHealth(getMaxHealth());
		setAttackSpeed(750);
		setSpeed(FAST_SPEED);
		setDamage(8);
		setArmor(1);
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), getAngle());
		g.drawImage(Resources.TROJAN_IMAGE, getX(), getY());
		g.rotate(getBounds().getCenterX(), getBounds().getCenterY(), -getAngle());
	}

	public void setSubAttribute(IAttribute attribute) {
		subAttribute = attribute;
	}

	public IAttribute getSubAttribute() {
		return subAttribute;
	}

}
