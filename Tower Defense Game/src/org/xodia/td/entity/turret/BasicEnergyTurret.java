package org.xodia.td.entity.turret;

import org.newdawn.slick.Graphics;
import org.xodia.td.Resources;

// Turrets that require energy!
public abstract class BasicEnergyTurret extends BasicTurret{

	protected boolean hasEnergy;
	
	public BasicEnergyTurret(float x, float y){
		super(x, y);
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(!hasEnergy){
			
			g.drawImage(Resources.UNPOWERED_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}else{
			g.drawImage(Resources.POWERED_ICON, (getX() - 16) + (buffVisible * 16), getY() - 16);
			
			buffVisible++;
		}
		
	}
	
	public void setHasEnergy(boolean has){
		hasEnergy = has;
	}
	
	public boolean hasEnergy(){
		return hasEnergy;
	}
	
}
