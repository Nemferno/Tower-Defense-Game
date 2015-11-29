package org.xodia.td.entity.enemy.boss;

import java.util.ArrayList;

import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

public class BossFactory {

	private static final String ABERRATION = "aberration";
	
	private BossFactory(){}
	
	public static BasicBoss createBoss(BossType type, BasicMap map, TempMap turretMap, ArrayList<Path> pathList,
			float x, float y, GameObject initTarget){
		switch(type){
		case ABERRATION:
			// check if it has a spawn checkpoint available
			if(BossType.ABERRATION.getX() != -1 && BossType.ABERRATION.getY() != -1)
				return new Aberration(map, pathList, turretMap, BossType.ABERRATION.getX(), BossType.ABERRATION.getY(), initTarget, true);
			else
				return new Aberration(map, pathList, turretMap, x, y, initTarget, true);
		}
		
		return null;
	}
	
	public static BossType getType(BasicBoss boss){
		switch(boss.getClass().getSimpleName().toLowerCase()){
		case ABERRATION:
			return BossType.ABERRATION;
		}
		
		return null;
	}
	
}
