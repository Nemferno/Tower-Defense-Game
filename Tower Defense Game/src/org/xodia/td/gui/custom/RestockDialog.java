package org.xodia.td.gui.custom;

import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.level.Level;

import TWLSlick.RootPane;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.model.SimpleIntegerModel;

public class RestockDialog extends Dialog{

	private SimpleIntegerModel model;
	private ValueAdjusterInt valueAdjuster;
	
	public RestockDialog(final PopupWindow win, final RootPane root, final Level level, final BasicTurret turret) {
		super("How many ammo do you want to purchase? [" + (turret.getMaxAmmo() - turret.getCurrentAmmo()) + "] \nTotal Cost:"
				+ "N/A     All Repair Cost: " + ((turret.getMaxAmmo() - turret.getCurrentAmmo()) * turret.getRestockCost()));
		
		model = new SimpleIntegerModel(0, (turret.getMaxAmmo() - turret.getCurrentAmmo()), (turret.getMaxAmmo() - turret.getCurrentAmmo()));
		valueAdjuster = new ValueAdjusterInt(model);
		model.addCallback(new Runnable(){
			public void run(){
				RestockDialog.this.setText("How many ammo do you want to purchase? [" + (turret.getMaxAmmo() - turret.getCurrentAmmo()) + "] \nTotal Cost:"
				+ (model.getValue() * turret.getRestockCost()) + "     All Repair Cost: " + ((turret.getMaxAmmo() - turret.getCurrentAmmo()) * turret.getRestockCost()));
			}
		});
		
		setText("How many ammo do you want to purchase? [" + (turret.getMaxAmmo() - turret.getCurrentAmmo()) + "] \nTotal Cost:"
				+ (model.getValue() * turret.getRestockCost()) + "     All Repair Cost: " + ((turret.getMaxAmmo() - turret.getCurrentAmmo()) * turret.getRestockCost()));
		
		addWidget(valueAdjuster);
		addButton("Restock All", new Runnable(){
			public void run(){
				int totCurrency = level.getCurrency();
				
				int difference = totCurrency - ((turret.getMaxAmmo() - turret.getCurrentAmmo()) * turret.getRestockCost());
				
				if(difference >= 0){
					level.setCurrency(difference);
					turret.setCurrentAmmo(turret.getMaxAmmo());
				}else{
					int absolDifference = -difference;
					// This absolute difference is the amount that is not going to be restocked
					int realAmmunitionDifference = absolDifference / turret.getRestockCost();
					
					// When we get the amount of ammunition that will not be given to the turret
					// take it away from the total amount
					level.setCurrency(0);
					turret.setCurrentAmmo(turret.getMaxAmmo() - realAmmunitionDifference);
				}
				
				level.setPaused(false);
				
				root.removeChild(RestockDialog.this);
				win.closePopup();
			}
		});
		
		addButton("Restock", new Runnable(){
			public void run(){
				int totCurrency = level.getCurrency();
				
				int difference = totCurrency - (model.getValue() * turret.getRestockCost());
				
				if(difference >= 0){
					level.setCurrency(difference);
					turret.incCurrentAmmo(model.getValue());

					level.setPaused(false);
					root.removeChild(RestockDialog.this);
					win.closePopup();
				}
			}
		});
		
		addButton("Cancel", new Runnable(){
			public void run(){
				level.setPaused(false);
				root.removeChild(RestockDialog.this);
				win.closePopup();
			}
		});
	}

}
