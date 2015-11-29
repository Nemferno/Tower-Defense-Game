package org.xodia.td.immunity;

import org.xodia.td.entity.enemy.BasicEnemy;

public class Insulator implements Immunity{

	public void implement(BasicEnemy object) {
		object.setImmuneToSlowFreeze(true);
	}

}
