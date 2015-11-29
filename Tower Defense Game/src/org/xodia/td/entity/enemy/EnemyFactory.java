package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.entity.GameObject;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;

// Creates Enemies
// Also accounts for the immunities placed
// by the virus type
public class EnemyFactory {

	private static final String VIRUS = "virus",
								BROODLING = "broodling",
								DARKROVER = "darkrover",
								IMMORTAL = "immortal",
								WORM = "worm",
								TROJAN = "trojan";
	
	private EnemyFactory(){}
	
	public static BasicEnemy createEnemy(EnemyType type, BasicMap map, ArrayList<Path> pathList, TempMap turretMap, 
			float x, float y, GameObject initTarget){
		
		BasicEnemy enemy = null;
		
		switch(type){
		case VIRUS:
			
			enemy = new Virus(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		case BROODLING:
			
			enemy = new Broodling(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		case DARKROVER:
			
			enemy = new DarkRover(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		case IMMORTAL:
			
			enemy = new Immortal(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		case WORM:
			
			enemy = new Immortal(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		case CONTROLLEDTURRET:
			
			enemy = new ControlledTurret(x, y);
			
			break;
		case TROJAN:
			
			enemy = new Trojan(map, pathList, turretMap, x, y, initTarget, false);
			
			break;
		}
		
		if(enemy != null){
			// Implement Immunities
			if(type.getImmunityCount() > 0){
				for(int i = 0; i < type.getImmunityCount(); i++){
					type.pollImmunity(i).implement(enemy);
				}
			}
		}
		
		// Done
		
		return enemy;
	}
	
	public static EnemyType getEnemyType(BasicEnemy enemy){
		switch(enemy.getClass().getSimpleName().toLowerCase()){
		case BROODLING:
			return EnemyType.BROODLING;
		case DARKROVER:
			return EnemyType.DARKROVER;
		case IMMORTAL:
			return EnemyType.IMMORTAL;
		case TROJAN:
			return EnemyType.TROJAN;
		case VIRUS:
			return EnemyType.VIRUS;
		case WORM:
			return EnemyType.WORM;
		}
		
		return null;
	}
	
}
