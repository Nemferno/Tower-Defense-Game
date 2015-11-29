package org.xodia.td.immunity;

import org.xodia.td.entity.enemy.BasicEnemy;

public class DefenseMatrix implements Immunity{
	
	private float percentage;
	
	public DefenseMatrix(float percentage){
		this.percentage = percentage;
	}
	
	// Increase Shield Depending on Health
	public void implement(BasicEnemy object){
		float newShield = (object.getMaxHealth() * percentage);
		
		object.incMaxShield(newShield);
		object.incCurrentShield(newShield);
	}
	
}
