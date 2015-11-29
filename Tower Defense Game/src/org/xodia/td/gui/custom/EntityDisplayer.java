package org.xodia.td.gui.custom;

import org.xodia.td.GameState;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.turret.BasicGenerator;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;
import org.xodia.td.entity.turret.TurretFactory;
import org.xodia.td.entity.turret.Wall;
import org.xodia.td.input.Control;
import org.xodia.td.level.Level;

import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.Widget;

public class EntityDisplayer extends Widget{

	private GameObject object;
	private Label label;
	
	public EntityDisplayer(final RootPane root, final LevelMenu menu,
			final GameState state, final Level level, final GameObject object){
		this.object = object;
		
		setPosition(75, 510);
		setSize(575, 120);
		
		label = new Label(
				"Name: " + object.getClass().getSimpleName() 
				+ "\n" + "Health: " + object.getCurrentHealth() + "/" + object.getMaxHealth() +
				"\n" + "Shield: " + object.getCurrentShield() + "/" + object.getMaxShield() +
				"\n" + "Damage: " + object.getDamage() +
				"\n" + "Armor: " + object.getArmor()
				);
		label.setSize(175, 120);

		int currentX = 185;
		
		// Upgrade:
		// Only for turrets && if they actually have an upgrade. If so, it will appear
		// Restock:
		// Only for turrets that are not wall or SSS
		// Repair:
		// Only for turrets and not SSS and Wall (Makes it OP)
		// Sell:
		// Only for turrets and not SSS
		
		if((object instanceof BasicTurret)){
			
			if(!(object instanceof Wall)){
				Button upgrade = new Button("Upgrade");
				upgrade.addCallback(new Runnable(){
					public void run(){
						level.setPaused(true);
						
						final PopupWindow popup = new PopupWindow(EntityDisplayer.this);
						popup.setTheme("dialogpopup");
						popup.setCloseOnClickedOutside(false);
						
						final Dialog dialog = new Dialog("Do you want to upgrade the turret or upgrade its type?");
						dialog.setPosition(300, 350);
						dialog.setSize(300, 100);
						dialog.addButton("Upgrade Turret", new Runnable(){
							public void run(){
								BasicTurret turret = (BasicTurret) object;
								Turret selectType = Turret.valueOf(turret.getClass().getSimpleName().toUpperCase());
								
								if(selectType.isUpgradeable()){
									int difference = 0;
									
									if((difference = (level.getUpgradeCurrency() - selectType.getUpgradeCost())) >= 0){
										BasicTurret upgradeTurret = TurretFactory.getTurret(turret.getX(), turret.getY(), selectType.getUpgradeTurret());
										upgradeTurret.setCurrentHealth(turret.getCurrentHealth());
										upgradeTurret.setCurrentShield(turret.getCurrentShield());
										upgradeTurret.setCurrentAmmo(turret.getCurrentAmmo());
										turret.setAlive(false);
										level.addObject(upgradeTurret);
										level.setUpgradeCurrency(difference);
										
										state.setDisplayStat(false);
										root.removeChild(EntityDisplayer.this);
										
										level.setPaused(false);
										
										dialog.setVisible(false);
										popup.closePopup();
									}else{
										// Give an Alert Dialog
										final PopupWindow alert = new PopupWindow(popup);
										alert.setTheme("alertpopup");
										alert.setCloseOnClickedOutside(false);
										
										final Dialog alertDialog = new Dialog("Not enough upgrades to create the parts. Requires: " + -difference);
										alertDialog.addButton("Confirm", new Runnable(){
											public void run(){
												alertDialog.setVisible(false);
												alert.closePopup();
											}
										});
										
										alert.add(alertDialog);
										alert.openPopupCentered();
									}
								}else{
									// Give an Alert Dialog
									final PopupWindow alert = new PopupWindow(popup);
									alert.setTheme("alertpopup");
									alert.setCloseOnClickedOutside(false);
									
									final Dialog alertDialog = new Dialog("The turret has already been upgraded to its max.");
									alertDialog.addButton("Confirm", new Runnable(){
										public void run(){
											alertDialog.setVisible(false);
											alert.closePopup();
										}
									});
									
									alert.add(alertDialog);
									alert.openPopupCentered();
								}
							}
						});
						dialog.addButton("Upgrade Type", new Runnable(){
							public void run(){
								Turret selecttype = Turret.valueOf(object.getClass().getSimpleName().toUpperCase());
								
								if(selecttype.isUpgradeable()){
									
									int difference = 0;
									
									if((difference = (level.getUpgradeCurrency() - Control.getControl().getType(Control.CONTROL_SELECT_CANON).getUpgradeCost())) >= 0){
										Turret type = selecttype.getUpgradeTurret();
										Control.getControl().setKeyType(Control.getControl().getInputName(selecttype), type);
										level.setUpgradeCurrency(difference);
										menu.changeType(selecttype.toString(), type);
										
										level.setPaused(false);
										
										state.setDisplayStat(false);
										root.removeChild(EntityDisplayer.this);
										popup.closePopup();
									}else{
										// Give an Alert Dialog
										final PopupWindow alert = new PopupWindow(popup);
										alert.setTheme("alertpopup");
										alert.setCloseOnClickedOutside(false);
										
										final Dialog alertDialog = new Dialog("Not enough upgrades to create the parts. Requires: " + -difference);
										alertDialog.addButton("Confirm", new Runnable(){
											public void run(){
												alertDialog.setVisible(false);
												alert.closePopup();
											}
										});
										
										alert.add(alertDialog);
										alert.openPopupCentered();
									}
								}else{
									// Give an Alert Dialog
									final PopupWindow alert = new PopupWindow(popup);
									alert.setTheme("alertpopup");
									alert.setCloseOnClickedOutside(false);
									
									final Dialog alertDialog = new Dialog("The turret has already been upgraded to its max.");
									alertDialog.addButton("Confirm", new Runnable(){
										public void run(){
											alertDialog.setVisible(false);
											alert.closePopup();
										}
									});
									
									alert.add(alertDialog);
									alert.openPopupCentered();
								}
							}
						});
						dialog.addButton("Cancel", new Runnable(){
							public void run(){
								level.setPaused(false);
								
								dialog.setVisible(false);
								popup.closePopup();
							}
						});
						
						popup.add(dialog);
						popup.openPopupCentered();
					}
				});
				upgrade.setPosition(currentX, 25);
				upgrade.setSize(75, 75);
				add(upgrade);
				
				currentX += 85;
			}
			
			if(!(object instanceof BasicGenerator) && !(object instanceof Wall)){
				Button restock = new Button("Restock");
				restock.addCallback(new Runnable(){
					public void run(){
						/*if(((BasicTurret) object).getCurrentAmmo() != ((BasicTurret) object).getMaxAmmo()){
							int cost = (int) (((BasicTurret) object).getRestockCost() * (((BasicTurret) object).getMaxAmmo() - ((BasicTurret) object).getCurrentAmmo()));
							int difference = 0;
							
							if((difference = level.getCurrency() - cost) >= 0){
								level.setCurrency(difference);
								
								((BasicTurret) object).setCurrentAmmo(((BasicTurret) object).getMaxAmmo());
							}else{
								int restockAmount = (level.getCurrency() / ((BasicTurret) object).getRestockCost());
								
								level.setCurrency(0);
								
								((BasicTurret) object).incCurrentAmmo(restockAmount);
								
								if(((BasicTurret) object).getCurrentAmmo() > ((BasicTurret) object).getMaxAmmo()){
									((BasicTurret) object).setCurrentAmmo(((BasicTurret) object).getMaxAmmo());
								}
							}
						}*/
						
						level.setPaused(true);
						
						PopupWindow popup = new PopupWindow(root);
						popup.setTheme("restockpopup");
						popup.setCloseOnClickedOutside(false);
						
						RestockDialog dialog = new RestockDialog(popup, root, level, (BasicTurret) object);
						
						popup.add(dialog);
						popup.openPopupCentered();
					}
				});
				
				restock.setPosition(currentX, 25);
				restock.setSize(75, 75);
				add(restock);
				
				currentX += 85;
			}
			
			if(!(object instanceof Wall) && object instanceof BasicTurret){
				Button repair = new Button("Repair");
				repair.addCallback(new Runnable(){
					public void run(){
						/*if(object.getCurrentHealth() != object.getMaxHealth()){
							int cost = (int) (((BasicTurret) object).getRepairCost() * (object.getMaxHealth() - object.getCurrentHealth()));
							int difference = 0;
							
							if((difference = level.getCurrency() - cost) >= 0){
								level.setCurrency(difference);
								
								object.setCurrentHealth(object.getMaxHealth());
							}else{
								int repairAmount = (level.getCurrency() / ((BasicTurret) object).getRepairCost());
								
								level.setCurrency(0);
								
								object.incCurrentHealth(repairAmount);
								
								if(object.getCurrentHealth() > object.getMaxHealth()){
									object.setCurrentHealth(object.getMaxHealth());
								}
							}
						}*/
						
						level.setPaused(true);
						
						PopupWindow popup = new PopupWindow(root);
						popup.setCloseOnClickedOutside(false);
						popup.setTheme("repairpopup");
						
						RepairDialog dialog = new RepairDialog(popup, root, level, (BasicTurret) object);
						
						popup.add(dialog);
						popup.openPopupCentered();
					}
				});
				repair.setPosition(currentX, 25);
				repair.setSize(75, 75);
				add(repair);
				
				currentX += 85;
			}
			
			Button sell = new Button("Sell");
			sell.addCallback(new Runnable(){
				public void run(){
					if(object.getCurrentHealth() == object.getMaxHealth()){
						// REFUND FULL
						int refund = Turret.valueOf(object.getClass().getSimpleName().toUpperCase()).getCost();
						level.incCurrency(refund);
						object.setAlive(false);
					}else{
						int refund = Turret.valueOf(object.getClass().getSimpleName().toUpperCase()).getCost() / 2;
						level.incCurrency(refund);
						object.setAlive(false);
					}
					
					state.setDisplayStat(false);
					root.removeChild(EntityDisplayer.this);
				}
			});
			sell.setPosition(currentX, 25);
			sell.setSize(75, 75);
			
			add(sell);
			
			currentX += 85;
		}
		
		add(label);
	}
	
	public void update(){
		label.setText(
				"Name: " + object.getClass().getSimpleName() 
				+ "\n" + "Health: " + object.getCurrentHealth() + "/" + object.getMaxHealth() +
				"\n" + "Shield: " + object.getCurrentShield() + "/" + object.getMaxShield() +
				"\n" + "Damage: " + object.getDamage() +
				"\n" + "Armor: " + object.getArmor()
				);
	}
	
	public GameObject getObject(){
		return object;
	}
	
}
