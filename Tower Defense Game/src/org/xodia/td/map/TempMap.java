package org.xodia.td.map;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.xodia.td.entity.GameObject;
import org.xodia.td.util.Triad;

// Creates a map that is the same thing as the Basic Map
// Adds on other objects so that it does not tamper
// with the original map that is in the basic map.
public class TempMap implements TileBasedMap{

	private BasicMap map;
	private boolean[][] blocked;
	
	private Triad<Integer, Integer, GameObject> triad;
	
	public TempMap(BasicMap map){
		this.map = map;
		
		blocked = new boolean[map.getWidthInTiles()][map.getHeightInTiles()];
		triad = new Triad<Integer, Integer, GameObject>(map.getWidthInTiles() * map.getHeightInTiles());
		
		for(int x = 0; x < map.getWidthInTiles(); x++){
			for(int y = 0; y < map.getHeightInTiles(); y++){
				blocked[x][y] = map.blocked(x, y);
			}
		}
	}
	
	public void registerObject(GameObject object){
		blocked[(int) (object.getX() / 32)][(int) (object.getY() / 32)] = true;
		triad.register((int) (object.getX() / 32), (int) (object.getY() / 32), object);
	}
	
	public void registerCoordinates(float x, float y){
		int tileX = (int) (x / 32);
		int tileY = (int) (y / 32);
		
		blocked[tileX][tileY] = true;
		triad.register(tileX, tileY, null);
	}
	
	public void removeObject(GameObject object){
		blocked[(int) (object.getX() / 32)][(int) (object.getY() / 32)] = false;
		triad.remove(object);
	}
	
	public void removeCoordinates(float x, float y){
		int tileX = (int) (x / 32);
		int tileY = (int) (y / 32);
		
		blocked[tileX][tileY] = false;
		triad.remove(tileX, tileY);
	}
	
	public void clear(){
		for(int x = 0; x < map.getWidthInTiles(); x++){
			for(int y = 0; y < map.getHeightInTiles(); y++){
				blocked[x][y] = map.blocked(x, y);
			}
		}
		
		triad.clear();
	}
	
	public GameObject getAt(int x, int y){
		return triad.get(x, y);
	}
	
	public boolean blocked(int x, int y){
		return blocked[x][y];
	}
	
	public boolean blocked(PathFindingContext arg0, int x, int y) {
		return blocked[x][y];
	}

	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 10;
	}

	public int getHeightInTiles() {
		return blocked[0].length;
	}

	public int getWidthInTiles() {
		return blocked.length;
	}

	public void pathFinderVisited(int arg0, int arg1) {}
	
}
