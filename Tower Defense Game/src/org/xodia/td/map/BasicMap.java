package org.xodia.td.map;

import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.turret.Wall;

// As of Build #3, this class can hold virus' spawn locations
// as well as motherboard spawns
public class BasicMap implements TileBasedMap{
	
	private TiledMap map;
	
	private boolean[][] blocked;
		
	private ArrayList<GameObject> preplacedObjects;
	
	private int[] virusSpawnsX;
	private int[] virusSpawnsY;
	
	private int sssLocationX;
	private int sssLocationY;
	
	public BasicMap(TiledMap map){
		if(map == null)
			return;
		
		this.map = map;
		
		blocked = new boolean[map.getWidth()][map.getHeight()];
		preplacedObjects = new ArrayList<GameObject>();
		
		for(int x = 0; x < map.getWidth(); x++){
			for(int y = 0; y < map.getHeight(); y++){
				int tileID = map.getTileId(x, y, 0);
				
				if(map.getTileProperty(tileID, "isBlocked", "false").equals("true")){
					blocked[x][y] = true;
				}
			}
		}
		
		// We have to find the object that contains the virus spawns
		int objectGroupCount = map.getObjectGroupCount();
		for(int i = 0; i < objectGroupCount; i++){
			// We need to setup a structure so that the program
			// knows which layer # is which according to the number of
			// ascending
			
			// #0 Refers to the virus spawns
			// #1 Refers to the sss
			// #2 Refers to the pre-placed walls
			
			int objectCount = map.getObjectCount(i);
			
			// Init the variables
			if(i == 0){
				virusSpawnsX = new int[objectCount];
				virusSpawnsY = new int[objectCount];
			}
			
			for(int i2 = 0; i2 < objectCount; i2++){
				if(i == 0){
					// Virus Spawns
					virusSpawnsX[i2] = map.getObjectX(i, i2);
					virusSpawnsY[i2] = map.getObjectY(i, i2);
				}else if(i == 1){
					// sss Spawn
					sssLocationX = map.getObjectX(i, i2);
					sssLocationY = map.getObjectY(i, i2);
				}else if(i == 2){
					Wall wall = new Wall(map.getObjectX(i, i2), map.getObjectY(i, i2));
					preplacedObjects.add(wall);
				}
			}
		}
	}
	
	public void render(int x, int y, int layer){
		map.render(x, y, layer);
	}
	
	public void render(){
		render(0, 0, 0);
	}

	public boolean blocked(int x, int y){
		return blocked[x][y];
	}
	
	public boolean blocked(PathFindingContext arg0, int x, int y) {
		return blocked(x, y);
	}

	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 10;
	}

	public int getHeightInTiles() {
		if(map == null)
			return 0;
		
		return map.getHeight();
	}

	public int getWidthInTiles() {
		if(map == null)
			return 0;
		
		return map.getWidth();
	}

	public void pathFinderVisited(int arg0, int arg1) {}
	
	public GameObject[] getPrePlacedObjects(){
		GameObject[] objects = new GameObject[preplacedObjects.size()];
		return preplacedObjects.toArray(objects);
	}
	
	public int[] getVirusSpawnsX(){
		return virusSpawnsX;
	}
	
	public int[] getVirusSpawnsY(){
		return virusSpawnsY;
	}
	
	public int getSSSLocationX(){
		return sssLocationX;
	}
	
	public int getSSSLocationY(){
		return sssLocationY;
	}
	
}
