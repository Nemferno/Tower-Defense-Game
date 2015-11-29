package org.xodia.td.immunity;

import org.xodia.td.entity.enemy.BasicEnemy;

public class StimPack implements Immunity{

	private float attSpdPercent;
	private float healthLossPercent;
	
	public StimPack(float attSpdPercent, float healthLossPercent){
		this.attSpdPercent = attSpdPercent;
		this.healthLossPercent = healthLossPercent;
	}
	
	public void implement(BasicEnemy object) {
		object.incAttackSpeed(object.getAttackSpeed() * attSpdPercent);
		object.incCurrentHealth(object.getMaxHealth() * healthLossPercent * -1);
	}

}
