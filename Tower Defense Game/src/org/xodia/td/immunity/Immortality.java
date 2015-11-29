package org.xodia.td.immunity;

import org.xodia.td.entity.enemy.BasicEnemy;

// This Introduces Armor Which Will Be Implemented Later
/**
 * 
 * ARMOR
 * 0 = 0%
 * 1 = 5%
 * 2 = 10%
 * 3 = 15%
 * 4 = 20%
 * 5 = 25%
 * ... = ...
 * 
 * @author JasperBae
 *
 */
public class Immortality implements Immunity{

	private int armor;

	public Immortality(int armor){
		this.armor = armor;
	}
	
	public void implement(BasicEnemy object) {
		object.incArmor(armor);
	}
	
}
