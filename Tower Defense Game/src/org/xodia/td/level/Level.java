package org.xodia.td.level;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.AnimationLoader;
import org.xodia.td.Application;
import org.xodia.td.attribute.Attribute;
import org.xodia.td.attribute.BossAttribute;
import org.xodia.td.entity.Bullet;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.SubSecuritySystem;
import org.xodia.td.entity.enemy.BasicEnemy;
import org.xodia.td.entity.enemy.Broodling;
import org.xodia.td.entity.enemy.Cannibal;
import org.xodia.td.entity.enemy.ControlledTurret;
import org.xodia.td.entity.enemy.DarkRover;
import org.xodia.td.entity.enemy.EnemyFactory;
import org.xodia.td.entity.enemy.EnemyType;
import org.xodia.td.entity.enemy.Immortal;
import org.xodia.td.entity.enemy.Trojan;
import org.xodia.td.entity.enemy.Virus;
import org.xodia.td.entity.enemy.Worm;
import org.xodia.td.entity.enemy.boss.BasicBoss;
import org.xodia.td.entity.enemy.boss.BossFactory;
import org.xodia.td.entity.enemy.boss.BossType;
import org.xodia.td.entity.turret.AntiMine;
import org.xodia.td.entity.turret.BasicEnergyTurret;
import org.xodia.td.entity.turret.BasicGenerator;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;
import org.xodia.td.entity.turret.TurretFactory;
import org.xodia.td.entity.turret.Wall;
import org.xodia.td.entity.turret.tier1.AntiTroll;
import org.xodia.td.entity.turret.tier1.PIDar;
import org.xodia.td.graphics.FadeText2;
import org.xodia.td.immunity.ImmunityManager;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;
import org.xodia.td.util.Camera;
import org.xodia.td.util.CustomCountdownTimer;
import org.xodia.td.util.DualList;
import org.xodia.td.util.MultiAnimation;

// This class does not handle input class!
// This class will be the structure of the 
// rest of the classes with some minor adjustments
// and etc.
public class Level {

	public static enum LevelType{ SURVIVAL, REGULAR }
	
	public static final int 	NORMAL_STATE = 1,
								SSS_DAMAGE_STATE = 2,
								KNOCKBACK_STATE = 3;
	
	private MultiAnimation multiAnime;
	
	private int currentPoints;
	
	private int currentState;
	
	// Boss 
	private int bossRound;
	private BossType bossType;
	private boolean isBossRound;
	private boolean containsBossRound;
	private int rBossCurrency;
	
	// Game Over
	private boolean isGameOver;
	
	// The amount of mines used
	private int amountOfMinesUsed;
	
	// Description of the Level
	private String name;
	private int id;
	private int subID;
	
	// Map of the Turret
	private TempMap turretMap;
	
	private ArrayList<GameObject> queueObjects;
	private ArrayList<GameObject> aliveObjects;
	private ArrayList<GameObject> deadObjects;
	private ArrayList<Path> pathsList;
	
	private boolean isPaused;
	
	private BasicMap map;
	
	// Selected Object
	private GameObject selectedObject;
	
	// Cameras
	private Camera camera;
	private float cameraX, cameraY;
	
	// Sub Security System
	private SubSecuritySystem sss;
	
	// Regular Currency
	private int starterCurrency;
	private int currency;
	private int upgradeCurrency;
	  
	// Timers
	private CustomCountdownTimer roundTimer;
	private CustomCountdownTimer enemySpawnTimer;
	
	// Enemies
	private int[] unitAmountList;
	private int totalEnemies;
	//private int totalEnemiesPerRound;
	private int currentEnemiesSpawning;
	private int currentEnemiesRemaining;
	
	// Round
	private int currentRound;
	
	// Fade Texts
	private FadeText2 notificationText;
	
	// Enemies List
	private EnemyType[] enemies;
	private DualList<Integer, EnemyType[]> enemiesList;
	
	private DualList<Integer, Boolean> loopList;
	private int[] restTimeList;
	private int[] respawnTimeList;
	private int[] rCurrencyList;
	
	private int rewardCurrency;
	
	// Spawn List
	private int[] spawnsX;
	private int[] spawnsY;
	
	private int delta;
	
	private LevelType type;
	
	// What does a level need?
	// A level needs a name, id, subid, specific map, types of enemies per round
	public Level(LevelData data){
		this.name = data.getName();
		this.id = data.getID();
		this.subID = data.getSubID();
		this.starterCurrency = data.getStartCurrency();
		this.type = data.getType();
		this.loopList = data.getLoopList();
		this.restTimeList = data.getRestTimeList();
		this.bossRound = data.getBossRound();
		this.bossType = data.getBossType();
		this.containsBossRound = data.containsBossRound();
		this.respawnTimeList = data.getRespawnTimeList();
		this.rCurrencyList = data.getRewardCurrencyList();
		this.rBossCurrency = data.getRBossCurrency();
		
		try {
			this.map = new BasicMap(new TiledMap(ResourceLoader.getResourceAsStream(data.getMapLoc()), data.getMapTileLoc()));
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		this.enemiesList = data.getEnemiesList();
		this.unitAmountList = data.getUnitAmountList();
		
		init();
	}
	
	private void init(){
		cameraX = Application.SWIDTH / 2;
		cameraY = Application.SHEIGHT / 2;
		camera = new Camera(map, Application.SWIDTH, Application.SHEIGHT);
		
		spawnsX = map.getVirusSpawnsX();
		spawnsY = map.getVirusSpawnsY();
		
		multiAnime = new MultiAnimation();
		
		aliveObjects = new ArrayList<GameObject>();
		deadObjects = new ArrayList<GameObject>();
		queueObjects = new ArrayList<GameObject>();
		pathsList = new ArrayList<Path>();
		
		turretMap = new TempMap(map);
		
		currency = starterCurrency;
		
		for(GameObject o : map.getPrePlacedObjects()){
			aliveObjects.add(o);
			turretMap.registerObject(o);
		}
		
		currentPoints = 0;
		
		sss = new SubSecuritySystem(map.getSSSLocationX(), map.getSSSLocationY());
		aliveObjects.add(sss);
		
		roundTimer = new CustomCountdownTimer(restTimeList[currentRound]);
		enemySpawnTimer = new CustomCountdownTimer(2500);
		
		currentState = NORMAL_STATE;
		
		notificationText = new FadeText2();
		
		ImmunityManager.getInstance().start();
	}
	
	public void update(StateBasedGame sg, int delta){
		this.delta = delta;
		
		if(!isPaused){
			if(currentState == NORMAL_STATE){
				updateMovement(delta);
				checkCollision();
				checkExplosives();
				checkEnergy();
				checkBuffs();
				checkBossAttributes();
				checkAttributes();
				checkStealthAndControlled();
				checkAndRemoveObjects();
				addObjectsFromQueue();
				checkRoundAndSpawn(sg, delta);
				
				if(isGameOver){
					sg.enterState(Application.GAMEOVERSTATE);
				}
			}else if(currentState == KNOCKBACK_STATE){
				applyKnockback();
				currentState = NORMAL_STATE;
			}
		}
	}
	
	// Knocks back all enemies that are in the SSS's perimeter
	private void applyKnockback(){
		// We have to see if the viruses are near the SSS's perimeter 1 tile
		int sssPX = (int) (sss.getX() / 32);
		int sssPY = (int) (sss.getY() / 32);
		
		ArrayList<BasicEnemy> knockbackedList = new ArrayList<BasicEnemy>();
		
		for(GameObject o : aliveObjects){
			if(o instanceof BasicEnemy){
				int eX = (int) (o.getX() / 32);
				int eY = (int) (o.getY() / 32);
				
				if(eX == sssPX - 1 && eY == sssPY){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX + 1 && eY == sssPY){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX - 1 && eY == sssPY - 1){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX + 1 && eY == sssPY + 1){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX - 1 && eY == sssPY - 1){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX && eY == sssPY + 1){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX && eY == sssPY - 1){
					knockbackedList.add((BasicEnemy) o);
				}else if(eX == sssPX + 1 && eY == sssPY - 1){
					knockbackedList.add((BasicEnemy) o);
				}
			}
		}
		
		// After we have obtained the list for the enemies that will be knocked back,
		// we have to for each individual check their position and direction
		for(BasicEnemy e : knockbackedList){
			int tileX = (int) (e.getX() / 32);
			int tileY = (int) (e.getY() / 32);
			int tempX = 0, tempY = 0;
			
			switch(e.getCurrentFacePos()){
			case BasicEnemy.NORTH:
				tempX = tileX;
				tempY = tempY + 1;
				break;
			case BasicEnemy.SOUTH:
				tempX = tileX;
				tempY = tempY - 1;
				break;
			case BasicEnemy.EAST:
				tempX = tileX - 1;
				tempY = tileY;
				break;
			case BasicEnemy.WEST:
				tempX = tileX + 1;
				tempY = tileY;
				break;
			}
			
			// Check if the temporary tiles are blocked or not
			if(map.blocked(tempX, tempY) || turretMap.blocked(tempX, tempY)){
				tempX = tileX;
				tempY = tileY;
			}
			
			e.setX(tempX * 32);
			e.setY(tempY * 32);
			e.setConcussed(true);
		}
		
	}
	
	public void render(Graphics g){
		camera.centerOnLocation(cameraX, cameraY);
		camera.translate(g);
		
		map.render();
		
		for(GameObject object : aliveObjects){
			if(object.isAlive()){
				if(object instanceof BasicTurret){
					for(Bullet b : ((BasicTurret) object).getBullets())
						b.render(g);
				}
				
				object.render(g);
				
				if(selectedObject == object){
					if(object instanceof BasicTurret){
						g.setColor(Color.red);
						g.draw(((BasicTurret) object).getRange());
					}
				}
			}
		}
		
		multiAnime.render(g);
		
		camera.detranslate(g);
		
		if(isPaused){
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, 800, 600);
			
			g.setColor(Color.white);
			g.drawString("Paused", 400, 300);
		}else{
			notificationText.draw(g, 250, 350, delta);
		}
	}
	
	private void checkBossAttributes(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicBoss){
				// ONE OF A BOSS'S SPECIAL ABILITY (ONLY IN ENDLESS MODE!)
				// RESURRECTION IS NOT HERE
			}
		}
	}
	
	private void checkExplosives(){
		for(GameObject object : aliveObjects){
			if(object instanceof AntiMine){
				AntiMine mine = (AntiMine) object;
				
				if(mine.isDelayElapsed()){
					 // Explode
					for(GameObject object2 : aliveObjects){
						if((int) (object2.getX() / 32) == (int) (mine.getX() / 32) &&
								(int) (object2.getY() / 32) == (int) (mine.getY() / 32)){
							
							float difference = object2.getCurrentShield() - mine.getDamage();
							float leftOver = 0;
							
							if(difference < 0){
								leftOver = (difference * -1) - (object2.getArmor() * 2);
								
								if(leftOver <= 0)
									leftOver = 0.1f;
								
								object2.setCurrentShield(0);
								object2.incCurrentHealth(-leftOver);
							}else{
								object2.setCurrentShield(difference);
							}
						}
					}
					
					// Check the radius!
					int tileX = (int) (mine.getX() / 32);
					int tileY = (int) (mine.getY() / 32);
					
					int starterX, starterY, endX, endY;
					starterX = starterY = endX = endY = 0;
					
					if(tileX - 1 >= 0 && tileY - 1 >= 0 && tileX + 1 <= map.getWidthInTiles() && tileY + 1 <= map.getHeightInTiles()){
						starterX = tileX - 1;
						starterY = tileY - 1;
						endX = tileX + 1;
						endY = tileY + 1;
					}else{
						if(tileX - 1 >= 0){
							starterX = tileX - 1;
							
							if(tileY - 1 >= 0){
							  	starterY = tileY - 1;
								
								if(tileX + 1 <= map.getWidthInTiles()){
									endX = tileX + 1;
									
									if(tileY + 1 <= map.getHeightInTiles()){
										endY = tileY + 1;
									}else{
										endY = map.getHeightInTiles();
									}
								}else{
									endX = map.getWidthInTiles();
								}
							}else{
								starterY = 0;
							}
						}else{
							starterX = 0;
						}
					}
					
					for(int x = starterX; x < endX; x++){
						for(int y = starterY; y < endY; y++){
							for(GameObject object2 : aliveObjects){
								if((int) (object2.getX() / 32) == x && (int) (object2.getY() / 32) == y){
									float difference = object2.getCurrentShield() - (mine.getDamage() * 0.3f);
									float leftOver = 0;
									
									if(difference < 0){
										leftOver = (difference * -1) - (object2.getArmor() * 2);
										
										if(leftOver <= 0)
											leftOver = 0.1f;
										
										object2.setCurrentShield(0);
										object2.incCurrentHealth(-leftOver);
									}else{
										object2.setCurrentShield(difference);
									}
								}
							}
						}
					}
					
					mine.setAlive(false);
				}
			}
		}
	}
	
	private void checkStealthAndControlled(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicEnemy){
				if(((BasicEnemy) object).getAttribute() == Attribute.CLOAK){
					((BasicEnemy) object).setSpotted(false);
				}else if(object instanceof Cannibal){
					if(((Cannibal) object).getSubAttribute() == Attribute.CLOAK){
						((BasicEnemy) object).setSpotted(false);
					}
				}
			}
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof AntiTroll){
				if(((AntiTroll) object).hasEnergy()){
					for(GameObject enemy : aliveObjects){
						if(enemy instanceof BasicEnemy){
							if(((BasicEnemy) enemy).getAttribute() == Attribute.CLOAK){
								if(((AntiTroll) object).getRange().contains(enemy.getBounds().getCenterX(), 
										enemy.getBounds().getCenterY())){
									((BasicEnemy) enemy).setSpotted(true);
								}
							}else if(enemy instanceof Cannibal){
								if(((Cannibal) enemy).getSubAttribute() == Attribute.CLOAK){
									if(((AntiTroll) object).getRange().contains(enemy.getBounds().getCenterX(),
											enemy.getBounds().getCenterY())){
										((BasicEnemy) enemy).setSpotted(true);
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof ControlledTurret){
				((ControlledTurret) object).setSpotted(false);
			}
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof AntiTroll){
				if(((AntiTroll) object).hasEnergy()){
					for(GameObject enemy : aliveObjects){
						if(enemy instanceof ControlledTurret){
							if(((AntiTroll) object).getRange().intersects(enemy.getBounds())){
								((ControlledTurret) enemy).setSpotted(true);
							}
						}
					}
				}
			}
		}
		
	}
	
	private void addObjectsFromQueue(){
		for(GameObject object : queueObjects){
			aliveObjects.add(object);
		}
		
		queueObjects = new ArrayList<GameObject>();
	}
	
	private void checkAttributes(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicEnemy){
				BasicEnemy enemy = (BasicEnemy) object;
				
				if(enemy.getAttribute() == Attribute.PUPPETEER){
					// Check the surrounding area
					// Get itself's location
					int tileX = (int) (enemy.getX() / 32);
					int tileY = (int) (enemy.getY() / 32);
					
					int starterX = 0, starterY = 0, endX = 0, endY = 0;
					
					if(tileX - 1 >= 0 && tileY - 1 >= 0 && tileX + 1 <= map.getWidthInTiles() && tileY + 1 <= map.getHeightInTiles()){
						// Then it is safe to do so
						starterX = tileX - 1;
						starterY = tileY - 1;
						endX = tileX + 1;
						endY = tileY + 1;
					}else{
						if(tileX - 1 >= 0){
							starterX = tileX - 1;
							
							if(tileY - 1 >= 0){
							  	starterY = tileY - 1;
								
								if(tileX + 1 <= map.getWidthInTiles()){
									endX = tileX + 1;
									
									if(tileY + 1 <= map.getHeightInTiles()){
										endY = tileY + 1;
									}else{
										endY = map.getHeightInTiles();
									}
								}else{
									endX = map.getWidthInTiles();
								}
							}else{
								starterY = 0;
							}
						}else{
							starterX = 0;
						}
					}
						
					for(int x = starterX; x < endX; x++){
						for(int y = starterY; y < endY; y++){
							for(GameObject turretObject : aliveObjects){
								if(turretObject instanceof BasicTurret && !(turretObject instanceof Wall) && !(turretObject instanceof AntiMine)){
									if(turretObject.isAlive() && (turretObject.getCurrentHealth() / turretObject.getMaxHealth()) <= 0.25f){
										BasicTurret turret = (BasicTurret) turretObject;
										
										turret.setAlive(false);
										
										BasicEnemy controlledEnemy = EnemyFactory.createEnemy(EnemyType.CONTROLLEDTURRET, null, null, turretMap, turret.getX(), turret.getY(), null);
										controlledEnemy.setMaxHealth(turret.getMaxHealth());
										controlledEnemy.setCurrentHealth(turret.getCurrentHealth());
										controlledEnemy.setMaxShield(turret.getMaxShield());
										controlledEnemy.setCurrentShield(turret.getCurrentShield());
										controlledEnemy.setAttackSpeed(turret.getAttackSpeed());
										controlledEnemy.setDamage(turret.getDamage());
										
										queueObjects.add(controlledEnemy);
										
										// Consume Itself
										enemy.setAlive(false);
									
										break;
									}
								}
							}
						}
					}
				}else if(enemy.getAttribute() == Attribute.CANNIBAL && enemy instanceof Cannibal){
					// We can safely assume that this entity is a cannibal
					// This will be very long!
					
					// First Check Is Attributes
					// We must check the area if the enemy will want to
					// eat up an enemy with cloak
					if(((Cannibal) enemy).getSubAttribute() != Attribute.CLOAK){
						int tileX = (int) (enemy.getX() / 32);
						int tileY = (int) (enemy.getY() / 32);
						
						int starterX, starterY, endX, endY;
						starterX = starterY = endX = endY = 0;
						
						if(tileX - 2 >= 0 && tileY - 2 >= 0 && tileX + 2 <= map.getWidthInTiles() && tileY + 2 <= map.getHeightInTiles()){
							// Then it is safe to do so
							starterX = tileX - 2;
							starterY = tileY - 2;
							endX = tileX + 2;
							endY = tileY + 2;
						}else{
							if(tileX - 2 >= 0){
								starterX = tileX - 2;
								
								if(tileY - 2 >= 0){
								  	starterY = tileY - 2;
									
									if(tileX + 2 <= map.getWidthInTiles()){
										endX = tileX + 2;
										
										if(tileY + 2 <= map.getHeightInTiles()){
											endY = tileY + 2;
										}else{
											endY = map.getHeightInTiles();
										}
									}else{
										endX = map.getWidthInTiles();
									}
								}else{
									starterY = 0;
								}
							}else{
								starterX = 0;
							}
						}
						
						int turretCount = 0;
						
						for(int x = starterX; x < endX; x++){
							for(int y = starterY; y < endY; y++){
								for(GameObject turret : aliveObjects){
									if(turret instanceof BasicTurret && !(turret instanceof Wall) && !(turret instanceof AntiMine)){
										if((int) (turret.getX() / 32) == x && (int) (turret.getY() / 32) == y){
											if(turret.isAlive()){
												turretCount++;
												break;
											}
										}
									}
								}
							}
						}
						
						// Now check if we should require cloak
						if(turretCount >= 15){
							// FIND CLOAK UNIT!
							for(GameObject e : aliveObjects){
								if(e instanceof BasicEnemy){
									if(((BasicEnemy) e).getAttribute() == Attribute.CLOAK){
										if(e.isAlive()){
											if((int) (e.getX() / 32) == enemy.getNextStepX() && (int) (e.getY() / 32) == enemy.getNextStepY()){
												// Consume it
												e.setAlive(false);
												
												// And Set Sub-Attribute
												((Cannibal) enemy).setSubAttribute(((BasicEnemy) e).getAttribute());
												
												break;
											}
										}
									}
								}
							}
						}
					}
					
					if(((Cannibal) enemy).getSubAttribute() != Attribute.BORNBRINGER){
						// Check how many enemies are there, there must be no spawning enemies
						if(currentEnemiesSpawning == 0 && currentEnemiesRemaining <= 6){
							// The Virus will now consume an entity that has bornbringer elements
							for(GameObject e : aliveObjects){
								if(e instanceof BasicEnemy){
									if(((BasicEnemy) e).getAttribute() == Attribute.BORNBRINGER){
										if(e.isAlive()){
											if((int) (e.getX() / 32) == enemy.getNextStepX() && (int) (e.getY() / 32) == enemy.getNextStepY()){
												e.setAlive(false);
												
												((Cannibal) enemy).setSubAttribute(((BasicEnemy) e).getAttribute());
												
												break;
											}
										}
									}
								}
							}
						}
					}
					
				}else if(enemy.getAttribute() == Attribute.TELEPORTATION){
					// If the enemy is attacking a turret that has a lot of damage
					// and has a lot of health, then teleport away. But it cannot teleport
					// if the next future step is blocked!
					// Check if it is attacking...
					if(!(enemy.getTarget() instanceof SubSecuritySystem)){
						// Okay we have to store its old target
						GameObject oldTurret = enemy.getTarget();
						
						// Set the target to sss
						enemy.setTarget(null);
						
						// Then do this while ignoring all turrets
						// If the target is not an instanceof a subsecuritysystem, search if the next future index is available?
						int nextStepX = enemy.getStepX(enemy.getCurrentStep() + 1); // The turret it is battling and the new one
						int nextStepY = enemy.getStepY(enemy.getCurrentStep() + 1);
						
						if(nextStepX != -1 && nextStepY != -1){
							// Check if it is empty
							if(!turretMap.blocked(nextStepX, nextStepY)){
								// If it is indeed empty, then we can move it
								// Plus, we have to validate that it has moved a step
								enemy.nextStep();
								
								enemy.setX(nextStepX * 32);
								enemy.setY(nextStepY * 32);
								enemy.setTarget(null); // Ignore if was being attacked or it was attacking it
							}else{
								enemy.setTarget(oldTurret);
							}
						}else{
							enemy.setTarget(oldTurret);
						}
					}
				}
				
			}
		}
	}
	
	private void updateMovement(int delta){
		for(GameObject object : aliveObjects){
			if(object.isAlive()){
				if(object instanceof BasicTurret){
					BasicTurret turret = (BasicTurret) object;
					
					for(Bullet bullet : turret.getBullets())
						bullet.update(delta);
				}
				
				object.update(delta);
			}
		}
	}
	
	private void checkCollision(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicTurret){
				BasicTurret turret = (BasicTurret) object;
				
					for(Bullet bullet : turret.getBullets()){
						for(GameObject target : aliveObjects){
							if(target instanceof BasicEnemy){
								BasicEnemy enemy = (BasicEnemy) target;
								
								if(bullet.isAlive() && enemy.isAlive() && enemy.isSpotted()){
									if(bullet.getBounds().intersects(enemy.getBounds())){
										if(enemy.isAggressor()){
											if(enemy.getTarget() == null){
												enemy.setTarget(turret);
											}else{
												if(enemy.getTarget() instanceof SubSecuritySystem){
													enemy.setTarget(turret);
												}
											}
										}
										
										if(enemy instanceof BasicBoss){
											if(enemy.getAttribute() == BossAttribute.ENLARGER){
												((BasicBoss) enemy).setProvoked(true);
											}
										}
										
										/*
										float leftOver;
										float difference = enemy.getCurrentShield() - bullet.getDamage();
										
										
										if(enemy.getCurrentShield() <= 0)
											enemy.incCurrentHealth(-bullet.getDamage());
										else
											enemy.incCurrentShield(-bullet.getDamage());*/
										
										float difference = enemy.getCurrentShield() - bullet.getDamage();
										float leftOver = 0;
										
										if(difference < 0){
											leftOver = (difference * -1) - (enemy.getArmor() * 2);
											
											if(leftOver <= 0)
												leftOver = 0.1f; // Has to do some damage
											
											enemy.setCurrentShield(0);
											enemy.incCurrentHealth(-leftOver);
										}else{
											enemy.setCurrentShield(difference);
										}
										
										// Before we set anything, we have to check if it does not
										// have an immunity protecting it
										
										// Before we set the bullet dead
										// We have to make sure that this bullet is ice bullet
										
										if(bullet.getType() == Bullet.BulletType.SLOWBLEEDING){
											if(!enemy.isImmuneToSlowFreeze()){
												enemy.setSlowed(true);
												enemy.incNumOfHitsOfFreezeSlow(1);
												enemy.setSlownessPercentage(bullet.getSlowRate());
											}
											
											enemy.setBleeding(true);
											enemy.setPainDamage(bullet.getPainDamage());
											
										}else if(bullet.getType() == Bullet.BulletType.SLOW){
											if(!enemy.isImmuneToSlowFreeze()){
												enemy.setSlowed(true);
												enemy.incNumOfHitsOfFreezeSlow(1);
												enemy.setSlownessPercentage(bullet.getSlowRate());
											}
										}else if(bullet.getType() == Bullet.BulletType.SLOWICEBLEEDING){
											if(!enemy.isImmuneToSlowFreeze()){
												enemy.setFrozen(true);
												enemy.setSlowed(true);
												enemy.incNumOfHitsOfFreezeSlow(1);
												enemy.setSlownessPercentage(bullet.getSlowRate());
											}
											
											enemy.setBleeding(true);
											enemy.setPainDamage(bullet.getPainDamage());
										}
										
										bullet.setAlive(false);
									}
								}
							}
						}
					}
				
					for(GameObject e : aliveObjects){
						if(e instanceof BasicEnemy){
							BasicEnemy enemy = (BasicEnemy) e;
							
							if(enemy.isSpotted()){
								if(turret.getTarget() == null){
									if(turret.getRange().intersects(enemy.getBounds())){
										turret.setTarget(enemy);
									}
								}else{
									if(turret.getTarget() == enemy){
										turret.setTarget(enemy);
									}
								}
							}
						}
					}
					
					continue;
			}
			
			if(object instanceof BasicEnemy){
				BasicEnemy enemy = (BasicEnemy) object;
				
				if(!(enemy instanceof ControlledTurret)){
					for(GameObject o : aliveObjects){
						if(o instanceof Wall){
							if(enemy.getTarget() != o && o.isAlive()){
								if(enemy.getNextStepX() == ((int) o.getX() / 32) &&
										enemy.getNextStepY() == ((int) o.getY() / 32)){
									enemy.setTarget(o);
								}
								
								if(enemy.getCurrentStepX() == ((int) o.getX() / 32) &&
										enemy.getCurrentStepY() == ((int) o.getY() / 32)){
									enemy.setX(enemy.getPreviousStepX() * 32);
									enemy.setY(enemy.getPreviousStepY() * 32);
									enemy.setTarget(o);
								}
							}
						}
						
						if(o instanceof BasicTurret && !(o instanceof AntiMine)){
							if(enemy.getTarget() != o && o.isAlive()){
								if(enemy.getNextStepX() == ((int) o.getX() / 32) &&
										enemy.getNextStepY() == ((int) o.getY() / 32)){
									enemy.setTarget(o);
								}
								
								if(enemy.getCurrentStepX() == ((int) o.getX() / 32) &&
										enemy.getCurrentStepY() == ((int) o.getY() / 32)){
									enemy.setX(enemy.getPreviousStepX() * 32);
									enemy.setY(enemy.getPreviousStepY() * 32);
									enemy.setTarget(o);
								}
							}
						}
					}
	
					continue;
				}
			}
		}
	}
	
	private void checkEnergy(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicEnergyTurret){
				((BasicEnergyTurret) object).setHasEnergy(false);
			}
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof BasicGenerator){
				for(GameObject object2 : aliveObjects){
					if(object2 instanceof BasicEnergyTurret){
						if(((BasicGenerator) object).getRange().contains(object2.getX(), object2.getY())){
							((BasicEnergyTurret) object2).setHasEnergy(true);
							continue;
						}
					}
				}
			}
		}
	}
	
	private void checkBuffs(){
		for(GameObject object : aliveObjects){
			if(object instanceof BasicTurret){
				((BasicTurret) object).setBuffed(false);
			}
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof PIDar){
				PIDar pidar = (PIDar) object;
				
				if(pidar.hasEnergy()){
					for(GameObject object2 : aliveObjects){
						if(object2 instanceof BasicTurret && !(object2 instanceof PIDar)){
							if(pidar.getRange().contains(object2.getBounds().getCenterX(), object2.getBounds().getCenterY())){
								((BasicTurret) object2).setBuffed(true);
								((BasicTurret) object2).setBuffAttackSpeed(pidar.getAttackSpeedGiven());
								((BasicTurret) object2).setBuffMaxHealth(pidar.getMaxHealthGiven());
								((BasicTurret) object2).setBuffMaxShield(pidar.getMaxShieldGiven());
								
								continue;
							}
						}else if(object2 instanceof ControlledTurret){
							if(pidar.getRange().contains(object2.getBounds().getCenterX(), object2.getBounds().getCenterY())){
								((ControlledTurret) object2).setBuffed(true);
								((ControlledTurret) object2).setBuffAttackSpeed(pidar.getAttackSpeedGiven());
								((ControlledTurret) object2).setBuffMaxHealth(pidar.getMaxHealthGiven());
								((ControlledTurret) object2).setBuffMaxShield(pidar.getMaxShieldGiven());
								
								continue;
							}
						}
					}
				}
			}
		}
	}
	
	private void checkAndRemoveObjects(){
		for(GameObject object : aliveObjects){			
			if(object instanceof BasicTurret){
				((BasicTurret) object).clearBullets();
			}
			
			if(object.getCurrentHealth() <= 0 || !object.isAlive()){
				if(object instanceof SubSecuritySystem){
					currentState = SSS_DAMAGE_STATE;
				}
				
				if(object instanceof BasicEnemy && !(object instanceof BasicBoss)){
					BasicEnemy enemy = (BasicEnemy) object;
					
					if(enemy.getAttribute() == Attribute.BORNBRINGER){
						// Creates a new spawn
						int newSpawnX = (int) (enemy.getX() / 32);
						int newSpawnY = (int) (enemy.getY() / 32);
						
						boolean canSetSpawn = true;
						
						// Check if a spawn point is on it
						for(int i = 0; i < spawnsX.length; i++){
							if(spawnsX[i] == newSpawnX && spawnsY[i] == newSpawnY){
								canSetSpawn = false;
								break;
							}
						}
						
						if(canSetSpawn)
							addSpawn(newSpawnX, newSpawnY);
					}else if(enemy instanceof Cannibal && ((Cannibal) enemy).getSubAttribute() == Attribute.BORNBRINGER){
						// Creates a new spawn
						int newSpawnX = (int) (enemy.getX() / 32);
						int newSpawnY = (int) (enemy.getY() / 32);
						
						boolean canSetSpawn = true;
						
						// Check if a spawn point is on it
						for(int i = 0; i < spawnsX.length; i++){
							if(spawnsX[i] == newSpawnX && spawnsY[i] == newSpawnY){
								canSetSpawn = false;
								break;
							}
						}
						
						if(canSetSpawn)
							addSpawn(newSpawnX, newSpawnY);
					}
					
					determinePoints((BasicEnemy) object);
					
					currency += ((BasicEnemy) object).getMoneyGiven();
					upgradeCurrency += ((BasicEnemy) object).getUpgradeGiven();
					
					if(!(enemy instanceof Broodling) && !(enemy instanceof ControlledTurret))
						currentEnemiesRemaining--;
					
					EnemyType type = EnemyFactory.getEnemyType(enemy);
					
					if(type != null){
						type.incrementKill(enemy.getNumOfKills());
						type.incrementDeath();
						type.addTimeKill((int) enemy.getTimeKill());
						type.addSlowFreezeTimes(enemy.getNumOfHitsofFreezeSlow());
					}
				}
				
				if(object instanceof BasicBoss){
					if(((BasicBoss) object).getAttribute() == BossAttribute.MINIMIZER){
						if(((BasicBoss) object).canMinimize()){
							((BasicBoss) object).setMinimized(true);
							continue;
						}
					}else if(((BasicBoss) object).getAttribute() == BossAttribute.RESURRECTION){
						BossFactory.getType((BasicBoss) object).setX((int) object.getX());
						BossFactory.getType((BasicBoss) object).setY((int) object.getY());
						
						currentPoints += 20;
						
						currency += ((BasicEnemy) object).getMoneyGiven();
						upgradeCurrency += ((BasicEnemy) object).getUpgradeGiven();
						
						currentEnemiesRemaining--;
						
					}else{
						BossFactory.getType((BasicBoss) object).setX(-1);
						BossFactory.getType((BasicBoss) object).setY(-1);
						
						currentPoints += 20;
						
						currency += ((BasicEnemy) object).getMoneyGiven();
						upgradeCurrency += ((BasicEnemy) object).getUpgradeGiven();
						
						currentEnemiesRemaining--;
						
						// Boss Immunities Later
					}
				}
				
				if(object instanceof BasicTurret){
					BasicTurret turret = (BasicTurret) object;
					
					multiAnime.add(AnimationLoader.getCopyOf(AnimationLoader.TURRET_EXPLOSION), (int) turret.getX(), (int) turret.getY());
					
					if(turret.getEnemyAttackingIt() != null){
						if(turret.getEnemyAttackingIt().getAttribute() == Attribute.KILLSPAWNER){
							// Create two units of Broodlings at the same spawn as the turret
							queueObjects.add(EnemyFactory.createEnemy(EnemyType.BROODLING, map, pathsList, turretMap, turret.getX(), turret.getY(), sss));
							queueObjects.add(EnemyFactory.createEnemy(EnemyType.BROODLING, map, pathsList, turretMap, turret.getX(), turret.getY(), sss));
						}
						
						turretMap.removeObject(turret);
						
						turret.getEnemyAttackingIt().incNumOfKills(1);
					}
				}
				
				object.setAlive(false);
				deadObjects.add(object);
			}
		}
		
		aliveObjects.removeAll(deadObjects);
		
		deadObjects = null;
		deadObjects = new ArrayList<GameObject>();
	}
	
	private long roundTimeSecond;
	private long oldRoundTimeSecond;
	private int roundLoop;
	private boolean isLooping;
	private boolean isRCurrencyGiven;
	
	private void checkRoundAndSpawn(StateBasedGame sg, int delta){
		if(currentEnemiesSpawning == 0 && currentEnemiesRemaining == 0){
			///////////////////////////////////////// ROUND SWITCHING ////////////////////////////////////////
			if(!isRCurrencyGiven){
				incCurrency(rewardCurrency);
				isRCurrencyGiven = true;
			}
			
			if(roundTimer.canStart()){
				if(roundTimer.isTimeElapsed()){
					if(!isLooping){
						if(++currentRound > enemiesList.getLength()){
							if(type == LevelType.REGULAR){
								isGameOver = true;
								sg.enterState(Application.GAMEOVERSTATE);
								return;
							}
						}
						
						if(bossRound != -1 && currentRound % bossRound == 0){
							isBossRound = true;
						}else if(bossRound == -1 && enemiesList.getValueFrom(currentRound) == null){
							// Wait until the end
							isBossRound = true;
						}else{
							isBossRound = false;
							
							rewardCurrency = rCurrencyList[currentRound - 1];
							totalEnemies = unitAmountList[currentRound - 1];
							enemies = enemiesList.getValueFrom(currentRound);
						}
					}else{
						currentRound++;

						rewardCurrency = rCurrencyList[roundLoop];
						totalEnemies = unitAmountList[roundLoop];
						enemies = enemiesList.getValueFrom(roundLoop);
					}
					
					if(isBossRound){
						currentEnemiesSpawning = 1;
						currentEnemiesRemaining = 0;
						rewardCurrency = rBossCurrency;
						
						notificationText.start("BOSS ROUND!!!", 250);
					}else{
						currentEnemiesSpawning = totalEnemies;
						currentEnemiesRemaining = 0;
						
						notificationText.start("Round " + currentRound + " is starting!", 250);
					}
					
					if(type == LevelType.SURVIVAL){
						if(currentRound <= enemiesList.getLength()){
							if(loopList.getValueFrom(currentRound)){
								roundLoop = currentRound - 1;
								isLooping = true;
							}else{
								if(currentRound == enemiesList.getLength()){
									roundLoop = currentRound - 1;
									isLooping = true;
								}
							}
						}
					}
					
					if(containsBossRound){
						if(isLooping){
							roundTimer.setTime(restTimeList[roundLoop]);
							enemySpawnTimer.setTime(respawnTimeList[roundLoop]);
						}else{
							roundTimer.setTime(restTimeList[currentRound - 1]);
							enemySpawnTimer.setTime(respawnTimeList[currentRound - 1]);
						}
						
						roundTimer.reset();
					}else{
						if(isBossRound){
							roundTimer.setTime(0);
							enemySpawnTimer.setTime(0);
						}else{
							roundTimer.setTime(restTimeList[currentRound - 1]);
							enemySpawnTimer.setTime(respawnTimeList[currentRound - 1]);
						}
						
						roundTimer.reset();
					}
					
					isRCurrencyGiven = false;
				}else{
					roundTimer.tick(delta);
					
					roundTimeSecond = roundTimer.getTimeLeftInSeconds();
					
					if(roundTimeSecond != oldRoundTimeSecond){
						notificationText.start("Round Starting! " + roundTimeSecond, 250);
						
						oldRoundTimeSecond = roundTimeSecond;
					}
				}
			}else{
				roundTimer.start();
			}
			///////////////////////////////////////// ROUND SWITCHING //////////////////////////////////
		}else{
			///////////////////////////////////////// MONSTER SPAWN /////////////////////////////////////
			if(currentEnemiesSpawning != 0){
				if(enemySpawnTimer.canStart()){
					if(enemySpawnTimer.isTimeElapsed()){
						Random random = new Random();
						
						int currentSpawnLocation = random.nextInt(spawnsX.length);
						
						int spawnX = spawnsX[currentSpawnLocation];
						int spawnY = spawnsY[currentSpawnLocation];
						
						for(GameObject o : aliveObjects){
							if(o instanceof BasicTurret){
								if(o.getX() == spawnX && o.getY() == spawnY){
									o.setAlive(false);
								}
							}
						}
						
						if(isBossRound){
							aliveObjects.add(BossFactory.createBoss(bossType, map, turretMap, pathsList, spawnX, spawnY, sss));
						}else{
							EnemyType enemy = enemies[random.nextInt(enemies.length)];
							
							aliveObjects.add(EnemyFactory.createEnemy(enemy, map, pathsList, turretMap, spawnX, spawnY, sss));
						}

						currentEnemiesRemaining++;
						currentEnemiesSpawning--;
						
						enemySpawnTimer.reset();
					}else{
						enemySpawnTimer.tick(delta);
					}
				}else{
					enemySpawnTimer.start();
				}
			}
			///////////////////////////////////////// MONSTER SPAWN /////////////////////////////////////
		}
	}
	
	public boolean placeTower(Turret type, float x, float y){
		if(type == null)
			return false;
		
		int tileX = (int) x / 32;
		int tileY = (int) y / 32;
		
		// Check if it is over
		if(tileX < 0 || tileX >= map.getWidthInTiles()){
			return false;
		}else if(tileY < 0 || tileY >= map.getHeightInTiles()){
			return false;
		}
		
		if(map.blocked(tileX, tileY)){
			return false;
		}
		
		for(GameObject object : aliveObjects){
			if(object instanceof BasicTurret || object instanceof ControlledTurret){
				if((int) (object.getX() / 32) == tileX && (int) (object.getY() / 32) == tileY){
					return false;
				}
			}
		}
		
		int difference = 0;
		
		if(type != Turret.ANTIMINE){
			if((difference = currency - type.getCost()) >= 0){
				BasicTurret turret = TurretFactory.getTurret(tileX *  32, tileY * 32, type);
				
				turretMap.registerObject(turret);
				aliveObjects.add(turret);
				
				currency = difference;
			}else{
				notificationText.start("You have not enough resources to place " + type + "! [Need " + -difference + "]", 250);
			}
		}else{
			if((difference = currency - (type.getCost() * amountOfMinesUsed++)) >= 0){
				AntiMine antimine = new AntiMine(tileX * 32, tileY * 32);
				
				aliveObjects.add(antimine);
				
				currency = difference;
			}else{
				notificationText.start("You have not enough resources to place " + type + "! [Need " + -difference + "]", 250);
			}
		}
		
		selectedObject = null;
				
		return true;
	}
	
	public boolean canPlaceTower(float x, float y){
		int tileX = (int) x / 32;
		int tileY = (int) y / 32;
		
		// Check if it is over
		if(tileX < 0 || tileX >= map.getWidthInTiles()){
			return false;
		}else if(tileY < 0 || tileY >= map.getHeightInTiles()){
			return false;
		}
		
		if(map.blocked(tileX, tileY)){
			return false;
		}
		
		for(GameObject object : aliveObjects){
			if(!(object instanceof Bullet) || !(object instanceof AntiMine)){
				if(((int) object.getX() / 32) == tileX && ((int) object.getY() / 32) == tileY){
					return false;
				}
			}
		}
				
		return true;
	}
	
	public GameObject getObjectFrom(float x, float y){
		GameObject select = null;
		
		for(GameObject object : aliveObjects){
			if(object.getBounds().contains(x, y))
				select = object;
		}
		
		selectedObject = select;
		
		return select;
	}
	
	public void addObject(GameObject object){
		aliveObjects.add(object);
	}
	
	// Has to be in tile, not pixels
	public void addSpawn(int x, int y){
		int[] tempSpawnsX = new int[spawnsX.length + 1];
		int[] tempSpawnsY = new int[spawnsY.length + 1];
		
		for(int i = 0; i < spawnsX.length; i++){
			tempSpawnsX[i] = spawnsX[i];
			tempSpawnsY[i] = spawnsY[i];
		}
		
		tempSpawnsX[tempSpawnsX.length - 1] = x;
		tempSpawnsY[tempSpawnsY.length - 1] = y;
		
		spawnsX = tempSpawnsX;
		spawnsY = tempSpawnsY;
	}
	
	public void setLevelCameraX(float x){
		cameraX = x;
		
		if(cameraX <= getMinLevelCameraX()){
			cameraX = getMinLevelCameraX();
		}
		
		if(cameraX >= getMaxLevelCameraX()){
			cameraX = getMaxLevelCameraX();
		}
	}
	
	public void setLevelCameraY(float y){
		cameraY = y;
		
		if(cameraY <= getMinLevelCameraY()){
			cameraY = getMinLevelCameraY();
		}
		
		if(cameraY >= getMaxLevelCameraY()){
			cameraY = getMaxLevelCameraY();
		}
	}
	
	public void incLevelCameraX(float x){
		cameraX += x;
		
		if(cameraX <= getMinLevelCameraX()){
			cameraX = getMinLevelCameraX();
		}
		
		if(cameraX >= getMaxLevelCameraX()){
			cameraX = getMaxLevelCameraX();
		}
	}
	
	public void incLevelCameraY(float y){
		cameraY += y;
		
		if(cameraY <= getMinLevelCameraY()){
			cameraY = getMinLevelCameraY();
		}
		
		if(cameraY >= getMaxLevelCameraY()){
			cameraY = getMaxLevelCameraY();
		}
	}
	private int getMinLevelCameraX(){
		return 400;
	}
	
	private int getMaxLevelCameraX(){
		return (map.getWidthInTiles() * 32) - 400 + 150;
	}
	
	private int getMaxLevelCameraY(){
		return (map.getHeightInTiles() * 32) - 300 + 120;
	}
	
	private int getMinLevelCameraY(){
		return 300 - 100;
	}
	
	public void setCurrency(int currency){
		this.currency = currency;
	}
	
	public void incCurrency(int currency){
		this.currency += currency;
	}
	
	public void setUpgradeCurrency(int currency){
		upgradeCurrency = currency;
	}
	
	public void incUpgradeCurrency(int currency){
		upgradeCurrency += currency;
	}
	
	public float getLevelCameraX(){
		return cameraX;
	}
	
	public float getLevelCameraY(){
		return cameraY;
	}
	
	public float getCameraX(){
		return camera.getCameraX();
	}
	
	public float getCameraY(){
		return camera.getCameraY();
	}

	public int getStarterCurrency(){
		return starterCurrency;
	}
	
	public int getCurrency(){
		return currency;
	}
	
	public int getUpgradeCurrency(){
		return upgradeCurrency;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
	
	public int getSubID(){
		return subID;
	}
	
	public int getRound(){
		return currentRound;
	}
	
	public int getCurrentEnemiesRemaining(){
		return currentEnemiesRemaining;
	}
	
	public int getCurrentEnemiesSpawning(){
		return currentEnemiesSpawning;
	}
	
	public SubSecuritySystem getSubSecuritySystem(){
		return sss;
	}
	
	public void setPaused(boolean pause){
		isPaused = pause;
	}
	
	public boolean isPaused(){
		return isPaused;
	}
	
	public void setCurrentState(int state){
		currentState = state;
	}
	
	public int getCurrentState(){
		return currentState;
	}
	
	public int getAmountOfMinesUsed(){
		return amountOfMinesUsed;
	}
	
	public int getCurrentPoints(){
		return currentPoints;
	}
	
	private void determinePoints(BasicEnemy e){
		if(e instanceof Broodling)
			currentPoints += 1;
		else if(e instanceof Virus)
			currentPoints += 2;
		else if(e instanceof Immortal)
			currentPoints += 5;
		else if(e instanceof Worm)
			currentPoints += 6;
		else if(e instanceof DarkRover || e instanceof Trojan)
			currentPoints += 10;
		else if(e instanceof ControlledTurret)
			currentPoints += 5;
	}
	
}
