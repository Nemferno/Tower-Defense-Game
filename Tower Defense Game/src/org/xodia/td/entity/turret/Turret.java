package org.xodia.td.entity.turret;

public enum Turret {
	
	// MINE CATEGORY
	ANTIMINE(250, 0, false, null, 0),
	
	// TIER FOUR
	CANONIV(800, 0, false, null, 108), TERAIV(1375, 0, false, null, 64), ANTITROLLIV(1900, 0, false, null, 160), PIDARIV(2210, 0, false, null, 160),
	
	// TIER THREE
	CANONIII(650, 6, true, CANONIV, 108), FROSTBYTEIII(800, 0, false, null, 160), TERAIII(1275, 18, true, TERAIV, 64), PIDARIII(2000, 20, true, PIDARIV, 160), ANTITROLLIII(1725, 19, true, ANTITROLLIV, 160), RECYNERGYIII(1550, 0, false, null, 192),
	
	// TIER TWO
	CANONII(500, 6, true, CANONIII, 96), FROSTBYTEII(750, 10, true, FROSTBYTEIII, 128), TERAII(1175, 14, true, TERAIII, 64), PIDARII(1950, 20, true, PIDARIII, 128), ANTITROLLII(1650, 19, true, ANTITROLLIII, 128), RECYNERGYII(1350, 16, true, RECYNERGYIII, 192),
 	
	// TIER ONE
	WALL(250, 0, false, null, 0), CANON(250, 3, true, CANONII, 96), FROSTBYTE(575, 7, true, FROSTBYTEII, 128), TERA(950, 10, true, TERAII, 64), PIDAR(1450, 15, true, PIDARII, 128), ANTITROLL(1250, 15, true, ANTITROLLII, 128), RECYNERGY(1250, 14, true, RECYNERGYII, 192),
	;
	
	private int cost;
	private int upgradeCost;
	private boolean isUpgradeable;
	private Turret upgradeTurret;
	private int range;
	
	Turret(int cost, int upgradeCost, boolean isUpgradeable, Turret upgradeTurret, int range){
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.isUpgradeable = isUpgradeable;
		this.upgradeTurret = upgradeTurret;
		this.range = range;
	}
	
	public int getRange(){
		return range;
	}
	
	public int getCost(){
		return cost;
	} 
	
	public int getUpgradeCost(){
		return upgradeCost;
	}
	
	public boolean isUpgradeable(){
		return isUpgradeable;
	}
	
	public Turret getUpgradeTurret(){
		return upgradeTurret;
	}
	
}
