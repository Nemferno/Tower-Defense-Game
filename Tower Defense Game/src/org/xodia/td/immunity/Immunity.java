package org.xodia.td.immunity;

import org.xodia.td.entity.enemy.BasicEnemy;

/**
 * 
 * An Immunity is something that is generated
 * after an event or its unability to wreck havoc
 * on the motherboard. This can alter the 
 * virus' ability to deal damage
 * 
 * @author Jasper Bae
 *
 */
public interface Immunity {
	
	void implement(BasicEnemy object);
	
}
