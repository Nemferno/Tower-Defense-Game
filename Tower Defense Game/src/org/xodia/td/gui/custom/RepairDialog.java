package org.xodia.td.gui.custom;

import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.level.Level;

import TWLSlick.RootPane;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.model.SimpleIntegerModel;

public class RepairDialog extends Dialog{

	private SimpleIntegerModel model;
	private ValueAdjusterInt valueAdjuster;
	
	public RepairDialog(final PopupWindow win, final RootPane root,
			final Level level, final BasicTurret turret){
		super("How much health do you want to restore? [" + (turret.getMaxHealth() - turret.getCurrentHealth()) + "] \nTotal Cost:"
				+ "N/A     All Repair Cost: " + ((turret.getMaxHealth() - turret.getCurrentHealth()) * turret.getRepairCost()));
		
		model = new SimpleIntegerModel(0, (int) (turret.getMaxHealth() - turret.getCurrentHealth()), (int) (turret.getMaxHealth() - turret.getCurrentHealth()));
		valueAdjuster = new ValueAdjusterInt(model);
		model.addCallback(new Runnable(){
			public void run(){
				RepairDialog.this.setText("How much health do you want to restore? [" + (turret.getMaxHealth() - turret.getCurrentHealth()) + "] \nTotal Cost:"
				+ (model.getValue() * turret.getRepairCost()) + "     All Repair Cost: " + ((turret.getMaxHealth() - turret.getCurrentHealth()) * turret.getRepairCost()));
			}
		});
		
		setText("How much health do you want to restore? [" + (turret.getMaxHealth() - turret.getCurrentHealth()) + "] \nTotal Cost:"
				+ (model.getValue() * turret.getRepairCost()) + "     All Repair Cost: " + ((turret.getMaxHealth() - turret.getCurrentHealth()) * turret.getRepairCost()));
		
		addWidget(valueAdjuster);
		addButton("Repair All", new Runnable(){
			public void run(){
				int totCurrency = level.getCurrency();
				
				int difference = totCurrency - (int) ((turret.getMaxHealth() - turret.getCurrentHealth()) * turret.getRepairCost());
				
				if(difference >= 0){
					level.setCurrency(difference);
					turret.setCurrentHealth(turret.getMaxHealth());
				}else{
					int absolDifference = -difference;
					
					int realHealthDifference = absolDifference / turret.getRepairCost();
					
					level.setCurrency(0);
					turret.setCurrentHealth(turret.getMaxHealth() - realHealthDifference);
				}

				level.setPaused(false);
				root.removeChild(RepairDialog.this);
				win.closePopup();
			}
		});
		
		addButton("Repair", new Runnable(){
			public void run(){
				int totCurrency = level.getCurrency();
				
				int difference = totCurrency - (int) (model.getValue() * turret.getRepairCost());
				
				if(difference >= 0){
					level.setCurrency(difference);
					turret.incCurrentHealth(model.getValue());

					level.setPaused(false);
					root.removeChild(RepairDialog.this);
					win.closePopup();
				}
			}
		});
		
		addButton("Cancel", new Runnable(){
			public void run(){
				level.setPaused(false);
				root.removeChild(RepairDialog.this);
				win.closePopup();
			}
		});
	}
	
}
