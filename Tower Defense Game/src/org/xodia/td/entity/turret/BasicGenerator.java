package org.xodia.td.entity.turret;

public class BasicGenerator extends BasicTurret{
	
	public BasicGenerator(float x, float y) {
		super(x, y);
		
		setAbilityToControl(false);
	}

	@Override
	public void update(int delta){
		if(getCurrentHealth() <= getMaxHealth() * 0.25){
			currentState = TurretState.DAMAGED;
			needRepair = true;
			isUnabledToFire = true;
		}else if(getCurrentHealth() <= getMaxHealth() * 0.50 && getCurrentHealth() > getMaxHealth() * 0.25){
			currentState = TurretState.SEMIDAMAGED;
			needRepair = false;
			isUnabledToFire = false;
		}else if(getCurrentHealth() <= getMaxHealth() && getCurrentHealth() > getMaxHealth() * 0.50){
			currentState = TurretState.NORMAL;
			needRepair = false;
			isUnabledToFire = false;
		}
		
		if(isBuffed){
			if(!hasBuffedTurret){
				if(!hasStoredOriginalValues){
					tempAttackSpeed = attackSpeed;
					tempMaxHealth = maxHealth;
					tempMaxShield = maxShield;
					
					hasStoredOriginalValues = true;
					
					// After we have stored the variables
					// we have to now buff it
					if(buffMaxHealth != 0){
						incMaxHealth(buffMaxHealth);
						incCurrentHealth(buffMaxHealth);
					}
					
					if(buffMaxShield != 0){
						incMaxShield(buffMaxShield);
						incCurrentShield(buffMaxShield);
					}
					
					if(buffAttackSpeed != 0)
						setAttackSpeed(getAttackSpeed() + (getAttackSpeed() * buffAttackSpeed));
					
					hasBuffedTurret = true;
				}
			}
		}else{
			if(hasBuffedTurret){
				if(hasStoredOriginalValues){
					// Revert back
					
					if(buffMaxHealth != 0){
						incMaxHealth(-buffMaxHealth);
						incCurrentHealth(-buffMaxHealth);
						
						if(getCurrentHealth() <= 0){
							setCurrentHealth(1);
						}
					}
					
					if(buffMaxShield != 0){
						incMaxShield(-buffMaxShield);
						incCurrentShield(-buffMaxShield);
						
						if(getCurrentShield() <= 0){
							setCurrentHealth(1);
						}
					}
					
					if(buffAttackSpeed != 0)
						setAttackSpeed(tempAttackSpeed);
					
					hasStoredOriginalValues = false;
					hasBuffedTurret = false;
				}
			}
		}
	}
	
}
