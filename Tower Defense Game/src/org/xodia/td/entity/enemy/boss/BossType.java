package org.xodia.td.entity.enemy.boss;

public enum BossType {
	
	ABERRATION();
	
	private int x = -1, y = -1;
	
	public void setX(int x){ this.x = x; }
	public void setY(int y){ this.y = y; }
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	
}
