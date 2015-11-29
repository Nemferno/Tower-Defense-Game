package org.xodia.td.entity.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.Resources;
import org.xodia.td.attribute.IAttribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.SubSecuritySystem;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;
import org.xodia.td.util.CustomCountdownTimer;
import org.xodia.td.util.CustomMath;
import org.xodia.td.util.CustomTimer;

/**
 * 
 * NOTE: Even though path does not contain wall, it goes ahead and checks for tower blockage
 * While checking tower blockage, it makes wall as an obstacle so the pathing will not get
 * mixed up!
 * 
 * -1 = indicates no blockage
 * 1 = indicates blockage
 * 
 * Passive & Aggressive Algorithm For AI Pathing
 * 1. Create path to target
 * 2. check if wall blocks path
 * 3. if it is, set all walls blocked and create path
 * 4. check if towers blocks path
 * 5. if it is, then check to see which path is fastest
 * 6. checks each path and sums all non-enemy objects' health and divide by damage and multiply
 * by rate of fire
 * 7. compare and if least, then this will be path. if least is the same, then use length()
 * from path object, if the length() is smaller then use it. If the length() is the same, use the
 * first one (Wall and then Tower). 
 * 
 * NOTE: Too complicated, this alogirthm is deprecated
 * 
 * @author Jasper Bae
 *
 */
public abstract class BasicEnemy extends GameObject implements Mover{

	public static final float FAST_SPEED = 1.5f;
	public static final float NORMAL_SPEED = 0.99f;
	public static final float SLOW_SPEED = 0.5f;
	
	// The time it takes for the enemy to be killed
	protected float timeKill;
	protected CustomTimer timeKillTimer;
	
	// The turret map, so that the enemies know where to go if there is an opening
	protected TempMap turretMap;
	
	// the number of kills it has
	protected int numOfKills;
	
	// The number of times it has taken freeze or slowness
	protected int numOfHitsOfFreezeSlow;
	
	protected GameObject target;
	
	protected IAttribute attribute;
	
	// This will be the motherboard
	protected GameObject initTarget;
	
	protected boolean isImmuneToSlowFreeze;
	
	protected int buffVisible;
	
	protected boolean isAggressor;
	protected boolean isAggro;
	
	protected Path path;
	protected AStarPathFinder pathFinder;
	
	protected BasicMap origMap;
	protected TempMap pathMap;
	
	protected ArrayList<Path> pathList;
	
	protected int[] stepsX;
	protected int[] stepsY;
	
	protected int[] oldStepsX;
	protected int[] oldStepsY;
	
	protected int currentStep;
	
	protected int moneyGiven;
	protected int upgradeGiven;
	
	protected float slowPercentage;
	protected float painDamage;
	
	protected CustomCountdownTimer freezeTimer;
	protected CustomCountdownTimer slowTimer;
	protected CustomCountdownTimer painDamageTimer;
	protected CustomCountdownTimer painTimer;
	protected CustomCountdownTimer concussionTimer;
	
	protected boolean isConcussed;
	protected boolean isFrozen;
	protected boolean isSlowed;
	protected boolean isBleeding;
	
	protected boolean isSpotted = true;
	
	public BasicEnemy(BasicMap origMap,	ArrayList<Path> pathList, TempMap turretMap, 
			float x, float y, GameObject initTarget, boolean isAggressor){
		this.isAggressor = isAggressor;
		this.initTarget = initTarget;
		this.target = initTarget;
		this.origMap = origMap;
		
		if(origMap != null)
			this.pathMap = new TempMap(origMap);
		
		this.pathList = pathList;
		this.turretMap = turretMap;
		
		setX(x);
		setY(y);
		setDamage(0);
		setMaxHealth(0);
		setAttackSpeed(1000);
		setCurrentHealth(getMaxHealth());
		setAlive(true);
		
		freezeTimer = new CustomCountdownTimer(2500);
		slowTimer = new CustomCountdownTimer(10000);
		painTimer = new CustomCountdownTimer(15000);
		painDamageTimer = new CustomCountdownTimer(1000);
		concussionTimer = new CustomCountdownTimer(5000);
		
		timeKillTimer = new CustomTimer();
		timeKillTimer.start();
		
		// This must require a pathMap in order to fully function and the pathList
		if(origMap != null && pathList != null && turretMap != null)
			updatePath();
	}
	
	// The BULK of the AI Pathing
	protected void updatePath(){
		// This time, this will be less complex and easy to code and easy for the player
		if(pathList.size() == 0){
			pathFinder = new AStarPathFinder(turretMap, 100, false);
			path = pathFinder.findPath(this, ((int) getX() / 32), ((int) getY() / 32), ((int) target.getX() / 32), ((int) target.getY() / 32));
			
			if(path != null){
				int[] tempStepsX = new int[path.getLength()];
				int[] tempStepsY = new int[path.getLength()];
				
				for(int i = 0; i < path.getLength(); i++){
					tempStepsX[i] = path.getX(i);
					tempStepsY[i] = path.getY(i);
				}
				
				pathList.add(path);
				
				stepsX = tempStepsX;
				stepsY = tempStepsY;
				
				oldStepsX = stepsX;
				oldStepsY = stepsY;
			}else{		
				// If there is not paths for the enemy to have, then it has to make up one!
				pathFinder = new AStarPathFinder(origMap, 100, false);
				path = pathFinder.findPath(this, ((int) getX() / 32), ((int) getY() / 32), ((int) target.getX() / 32), ((int) target.getY() / 32));
				
				int[] tempStepsX = new int[path.getLength()];
				int[] tempStepsY = new int[path.getLength()];
				
				for(int i = 0; i < path.getLength(); i++){
					tempStepsX[i] = path.getX(i);
					tempStepsY[i] = path.getY(i);
				}
				
				pathList.add(path);
				
				stepsX = tempStepsX;
				stepsY = tempStepsY;
				
				oldStepsX = stepsX;
				oldStepsY = stepsY;
			}
		}else{
			Random rand = new Random();
			int wtd = rand.nextInt(2);
			boolean goDefault = false;
			
			if(wtd == 0){
				pathFinder = new AStarPathFinder(turretMap, 100, false);
				path = pathFinder.findPath(this, ((int) getX() / 32), ((int) getY() / 32), ((int) target.getX() / 32), ((int) target.getY() / 32));
				
				if(path != null){
					int[] tempStepsX = new int[path.getLength()];
					int[] tempStepsY = new int[path.getLength()];
					
					for(int i = 0; i < path.getLength(); i++){
						tempStepsX[i] = path.getX(i);
						tempStepsY[i] = path.getY(i);
					}
					
					int similarSteps = 0;
					
					for(int i = 0; i < path.getLength(); i++){
						for(int x = 0; x < pathList.size(); x++){
							if(path.getLength() == pathList.get(x).getLength()){
								if(path.getX(i) == pathList.get(x).getX(i) && path.getY(i) == pathList.get(x).getY(i)){
									similarSteps++;
								}
							}
						}
					}
					
					if(similarSteps != path.getLength())
						pathList.add(path);
					
					stepsX = tempStepsX;
					stepsY = tempStepsY;
					
					oldStepsX = stepsX;
					oldStepsY = stepsY;
				}else{
					goDefault = true;
				}
			}else{
				goDefault = true;
			}
			
			if(goDefault){
				// Clear the map from the previous pathing
				pathMap.clear();
				
				// We randomly get 2-3 (if the path has 1 or 2, then this is an exception)
				Random random = new Random();
				
				int[] indexOfList = new int[3];
				
				// Before, set everything to -1
				for(int i = 0; i < indexOfList.length; i++){
					indexOfList[i] = -1;
				}
				
				for(int i = 0; i < indexOfList.length; i++){
					indexOfList[i] = random.nextInt(pathList.size());
				}
				
				// Next we iterate them to get the path, and then randomly block two of them!
				for(int i : indexOfList){
					if(i != -1){
						Path path = pathList.get(i);
						
						// We have to get that path and randomly block two of them
						Random blockRandom = new Random();
						
						int[] blockIndex = new int[2];
						
						// Get them
						for(int x = 0; x < blockIndex.length; x++){
							blockIndex[x] = blockRandom.nextInt(path.getLength() - 1);
						}
						
						int oldBlock = -1;
						
						// Then we block them
						for(int y : blockIndex){
							if(oldBlock != y){
								pathMap.registerCoordinates(path.getX(y) * 32, path.getY(y) * 32);
								
								// Register and then save coordinate
								oldBlock = y;
							}else if(oldBlock == y){
								// If we get a block that is the same
								int newY = y + 1;
								
								// If the new index is less than length - 1 (less than the last one, not equal to last, but less than total size)
								if(newY < path.getLength() - 1){
									pathMap.registerCoordinates(path.getX(newY) * 32, path.getY(newY) * 32);
									
									// Register and then save coordinate
									oldBlock = newY;
								}else{
									newY = y - 1;
									
									// Make sure this is not less than 0
									if(newY < 0){
										newY = 0;
										
										pathMap.registerCoordinates(path.getX(newY) * 32, path.getY(newY) * 32);
										
										// Register and then save coordinate
										oldBlock = newY;
									}else{
										pathMap.registerCoordinates(path.getX(newY) * 32, path.getY(newY) * 32);
										
										// Register and then save coordinate
										oldBlock = newY;
									}
								}
							}
						}	
					}
				}
				
				pathFinder = new AStarPathFinder(pathMap, 100, false);
				path = pathFinder.findPath(this, ((int) getX() / 32), ((int) getY() / 32), ((int) target.getX() / 32), ((int) target.getY() / 32));
				
				if(path != null){
					int[] tempStepsX = new int[path.getLength()];
					int[] tempStepsY = new int[path.getLength()];
					
					for(int i = 0; i < path.getLength(); i++){
						tempStepsX[i] = path.getX(i);
						tempStepsY[i] = path.getY(i);
					}
					
					int similarSteps = 0;
					
					// Before we register this path, make sure this path does not have the exact same step loc.
					for(int i = 0; i < path.getLength(); i++){
						for(int x = 0; x < pathList.size(); x++){
							if(path.getLength() == pathList.get(x).getLength()){
								if(path.getX(i) == pathList.get(x).getX(i) && path.getY(i) == pathList.get(x).getY(i)){
									similarSteps++;
								}
							}
						}
					}
					
					if(similarSteps != path.getLength()){
						pathList.add(path);
					}
					
					stepsX = tempStepsX;
					stepsY = tempStepsY;
					
					oldStepsX = stepsX;
					oldStepsY = stepsY;
				}else{
					// If the path is null, then what we have to do is ignore the randomness and do the default
					pathFinder = new AStarPathFinder(origMap, 100, false);
					path = pathFinder.findPath(this, ((int) getX() / 32), ((int) getY() / 32), ((int) target.getX() / 32), ((int) target.getY() / 32));
					
					if(path != null){
						int[] tempStepsX = new int[path.getLength()];
						int[] tempStepsY = new int[path.getLength()];
						
						for(int i = 0; i < path.getLength(); i++){
							tempStepsX[i] = path.getX(i);
							tempStepsY[i] = path.getY(i);
						}
						
						stepsX = tempStepsX;
						stepsY = tempStepsY;
						
						oldStepsX = stepsX;
						oldStepsY = stepsY;
					}else{
						// If the target is null assuming that it is an unreachable turret, go back to motherboard
						target = initTarget;
						
						stepsX = oldStepsX;
						stepsY = oldStepsY;
						
						return;
					}
				}
			}
		}
		
		if(stepsX.length >= 3)
			currentStep = 1;
		else
			currentStep = 0;
	}
	
	protected int currentFacePos;
	public static final int 	NORTH = 1,
								SOUTH = 2,
								EAST = 3,
								WEST = 4;
	
	protected void updateVelocity(){
		if((int) getX() > getCurrentStepX() * 32){
			setVelX(-1);
			currentFacePos = WEST;
		}else if((int) getX() < getCurrentStepX() * 32){
			setVelX(1);
			currentFacePos = EAST;
		}else{
			setVelX(0);
		}
		
		if((int) getY() > getCurrentStepY() * 32){
			setVelY(-1);
			currentFacePos = NORTH;
		}else if((int) getY() < getCurrentStepY() * 32){
			setVelY(1);
			currentFacePos = SOUTH;
		}else{
			setVelY(0);
		}
		
		if(currentFacePos == NORTH){
			setAngle(-90);
		}else if(currentFacePos == SOUTH){
			setAngle(90);
		}else if(currentFacePos == EAST){
			setAngle(0);
		}else if(currentFacePos == WEST){
			setAngle(180);
		}
	}
	
	public void render(Graphics g) {
/*		g.setColor(Color.red);
		g.draw(getBounds());
		*/
/*		if(stepsX != null){
			g.setColor(Color.magenta);
			
			for(int i = 0; i < stepsX.length; i++){
				g.drawRect(stepsX[i] * 32, stepsY[i] * 32, 32, 32);
			}
			
		}*/
		
		buffVisible = 0;
		
		if(isSlowed){
			g.drawImage(Resources.SLOWNESS_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			buffVisible++;
		}
		
		if(isFrozen){
			g.drawImage(Resources.FREEZE_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			buffVisible++;
		}
		
		if(isBleeding){
			g.drawImage(Resources.BLEEDING_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			buffVisible++;
		}
		
	}

	public void update(int delta) {
		if(!isConcussed){
			// If the enemy is slowed, then slow it down!
			if(isFrozen){
				// Just stop!
				
				if(freezeTimer.canStart()){
					if(freezeTimer.isTimeElapsed()){
						isFrozen = false;
						
						freezeTimer.reset();
					}else{
						freezeTimer.tick(delta);
					}
				}else{
					freezeTimer.start();
				}
				
			}else if(isSlowed){
				incX(getVelX() * getSpeed() * slowPercentage);
				incY(getVelY() * getSpeed() * slowPercentage);
				
				if(slowTimer.canStart()){
					if(slowTimer.isTimeElapsed()){
						isSlowed = false;
						
						slowTimer.reset();
					}else{
						slowTimer.tick(delta);
					}
				}else{
					slowTimer.start();
				}
			}else{
				incX(getVelX() * getSpeed());
				incY(getVelY() * getSpeed());
			}
			
			if(isBleeding){
				if(painTimer.canStart()){
					if(painTimer.isTimeElapsed()){
						isBleeding = false;
						
						painTimer.reset();
					}else{
						
						if(painDamageTimer.canStart()){
							if(painDamageTimer.isTimeElapsed()){
								
								if(getCurrentShield() <= 0){
									incCurrentHealth(-painDamage);
								}else{
									incCurrentShield(-painDamage);
								}
								
								painDamageTimer.reset();
								
							}else{
								painDamageTimer.tick(delta);
							}
						}else{
							painDamageTimer.start();
						}
						
						painTimer.tick(delta);
					}
				}else{
					painTimer.start();
				}
			}
			 
			if((int) getX() == getCurrentStepX() * 32 && (int) getY() == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() - 1 == getCurrentStepX() * 32 && (int) getY() == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() + 1 == getCurrentStepX() * 32 && (int) getY() == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() == getCurrentStepX() * 32 && (int) getY() - 1 == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() == getCurrentStepX() * 32 && (int) getY() + 1== getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() - 1 == getCurrentStepX() * 32 && (int) getY() + 1 == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() + 1 == getCurrentStepX() * 32 && (int) getY() + 1 == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() - 1 == getCurrentStepX() * 32 && (int) getY() - 1 == getCurrentStepY() * 32){
				nextStep();
			}else if((int) getX() + 1 == getCurrentStepX() * 32 && (int) getY() - 1 == getCurrentStepY() * 32){
				nextStep();
			}
			
			updateVelocity();
			
			if(isCurrentStepSecondToLast()){
				attack(delta);
				
				if(getTarget() != null){
					setAngle(CustomMath.getAngle(getBounds().getCenterX(), getBounds().getCenterY(), 
							getTarget().getBounds().getCenterX(), getTarget().getBounds().getCenterY()));
				}
			}
			
			if(getTarget() == null){
				setTarget(initTarget);
			}
			
			if(hasShieldRegen){
				if(shieldRegenTimer.isTimeElapsed()){
					if(getCurrentShield() < getMaxShield()){
						incCurrentShield(1);
					}
				}else{
					shieldRegenTimer.tick(delta);
				}
			}
			
			rateOfFire.tick(delta);
			timeKillTimer.tick(delta);
		}else{
			if(concussionTimer.canStart()){
				if(concussionTimer.isTimeElapsed()){
					concussionTimer.reset();
					isConcussed = false;
				}else{
					concussionTimer.tick(delta);
				}
			}else{
				concussionTimer.start();
			}
		}
	}
	
	protected void attack(int delta){
		if(rateOfFire.isTimeElapsed()){
			if(getTarget() != null){
				if(getTarget() instanceof SubSecuritySystem){
					if(((SubSecuritySystem) getTarget()).getTarget() == null)
						((SubSecuritySystem) getTarget()).setTarget(this);
				}
				
				if(getTarget() instanceof BasicTurret){
					((BasicTurret) getTarget()).setEnemyAttackingTurret(this);
				}
				
				float difference = getTarget().getCurrentShield() - getDamage();
				float leftOver = 0;
				
				if(difference < 0){
					leftOver = (difference * -1) - (getTarget().getArmor() * 2);
					
					if(leftOver <= 0)
						leftOver = 0.1f;
					
					getTarget().setCurrentShield(0);
					getTarget().incCurrentHealth(-leftOver);
				}else{
					getTarget().setCurrentShield(difference);
				}
				
				/*if(getTarget().getCurrentShield() <= 0)
					getTarget().incCurrentHealth(-getDamage());
				else
					// If the entity has shields
					getTarget().incCurrentShield(-getDamage());*/
			}
			
			rateOfFire.reset();
		}
	}
	
	public void setSlownessPercentage(float percentage){
		slowPercentage = percentage;
	}
	
	public void setPainDamage(float pain){
		painDamage = pain;
	}

	public void setAggro(boolean aggro){
		isAggro = aggro;
	}
	
	public void setNumOfKills(int kills){
		this.numOfKills = kills;
	}
	
	public void incNumOfKills(int kills){
		this.numOfKills += kills;
	}
	
	public void setNumOfHitsOfFreezeSlow(int times){
		numOfHitsOfFreezeSlow = times;
	}
	
	public void incNumOfHitsOfFreezeSlow(int times){
		numOfHitsOfFreezeSlow += times;
	}
	
	public void setTarget(GameObject target){
		this.target = target;
		
		// Check if it is null
		if(target == null)
			// set it to sss
			this.target = initTarget;
		
		updatePath();
	}
	
	public void setImmuneToSlowFreeze(boolean isImmune){
		isImmuneToSlowFreeze = isImmune;
	}
	
	public void setMoneyGiven(int given){
		moneyGiven = given;
	}
	
	public void setAttribute(IAttribute attribute){
		this.attribute = attribute;
	}
	
	public void setSpotted(boolean spot){
		isSpotted = spot;
	}
	
	public void incMoneyGiven(int given){
		moneyGiven += given;
	}
	
	public void setUpgradeGiven(int given){
		upgradeGiven = given;
	}
	
	public void incUpgradeGiven(int given){
		upgradeGiven += given;
	}
	
	public void setFrozen(boolean frozen){
		isFrozen = frozen;
	}
	
	public void setSlowed(boolean slowed){
		isSlowed = slowed;
	}
	
	public void setBleeding(boolean bleeding){
		isBleeding = bleeding;
	}
	
	public void nextStep(){
		if(currentStep < stepsX.length - 1){
			if(!(isCurrentStepSecondToLast())){
				currentStep++;
			}
		}
	}
	
	// These two accessor methods
	// are used to obtain data instead of
	// using previous step, current step, and next step
	public int getStepX(int index){
		if(index > stepsX.length - 1 || index < 0){
			return -1;
		}else{
			return stepsX[index];
		}
	}
	
	public int getStepY(int index){
		if(index > stepsX.length - 1 || index < 0){
			return -1;
		}else{
			return stepsY[index];
		}
	}
	
	public boolean isImmuneToSlowFreeze(){
		return isImmuneToSlowFreeze;
	}
	
	public int getCurrentStep(){
		return currentStep;
	}
	
	public int getCurrentStepX(){
		return stepsX[currentStep];
	}
	
	public int getCurrentStepY(){
		return stepsY[currentStep];
	}
	
	public int getNextStepX(){
		int futureStep = currentStep + 1;
		
		if(futureStep < stepsX.length){
			return stepsX[futureStep];
		}else
			return -1;
	}
	
	public int getNextStepY(){
		int futureStep = currentStep + 1;
		
		if(futureStep < stepsY.length){
			return stepsY[futureStep];
		}else
			return -1;
	}
	
	public int getPreviousStepX(){
		int previousStep = currentStep - 1;
		
		if(previousStep > -1){
			return stepsX[previousStep];
		}else
			return -1;
	}
	
	public int getPreviousStepY(){
		int previousStep = currentStep - 1;
		
		if(previousStep > -1){
			return stepsY[previousStep];
		}else
			return -1;
	}
	
	public boolean isCurrentStepLast(){
		if(currentStep == stepsX.length - 1){
			if(stepsX.length - 1 <= 0){
				return true;
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isCurrentStepSecondToLast(){
		if(currentStep == stepsX.length - 2){
			if(stepsX.length - 2 <= 0){
				return true;
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isConcussed(){
		return isConcussed;
	}
	
	public void setConcussed(boolean concussed){
		isConcussed = concussed;
	}
	
	public boolean isAggro(){
		return isAggro;
	}
	
	public boolean isAggressor(){
		return isAggressor;
	}
	
	public boolean isSpotted(){
		return isSpotted;
	}
	
	public boolean isFrozen(){
		return isFrozen;
	}
	
	public boolean isSlowed(){
		return isSlowed;
	}
	
	public GameObject getTarget(){
		if(target != null && target.isAlive())
			return target;
		else
			return null;
	}
	
	public int getNumOfKills(){
		return numOfKills;
	}
	
	public int getNumOfHitsofFreezeSlow(){
		return numOfHitsOfFreezeSlow;
	}
	
	public int getMoneyGiven(){
		return moneyGiven;
	}
	
	public int getUpgradeGiven(){
		return upgradeGiven;
	}
	
	public int getBuffCount(){
		int count = 0;
		
		if(isFrozen)
			count++;
		if(isBleeding)
			count++;
		if(isSlowed)
			count++;
		
		return count;
	}
	
	public int getCurrentFacePos(){
		return currentFacePos;
	}
	
	public double getTimeKill(){
		return timeKillTimer.getTimeInMilliSeconds();
	}
	
	public IAttribute getAttribute(){
		return attribute;
	}
	
	public Shape getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
