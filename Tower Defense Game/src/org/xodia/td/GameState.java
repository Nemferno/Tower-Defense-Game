package org.xodia.td;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.turret.BasicGenerator;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.entity.turret.Turret;
import org.xodia.td.entity.turret.TurretFactory;
import org.xodia.td.entity.turret.Wall;
import org.xodia.td.graphics.FadeText2;
import org.xodia.td.gui.custom.EntityDisplayer;
import org.xodia.td.gui.custom.HealthDisplayer;
import org.xodia.td.gui.custom.LevelMenu;
import org.xodia.td.gui.custom.ReviveDialog;
import org.xodia.td.immunity.ImmunityManager;
import org.xodia.td.input.Control;
import org.xodia.td.level.Level;
import org.xodia.td.level.LevelData;
import org.xodia.td.util.CustomCountdownTimer;
import org.xodia.td.util.CustomMath;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.Widget;

public class GameState extends BasicTWLGameState{

	private final int DEFAULT_SSS_REVIVAL_CURRENCY_REWARD = 250;
	
	private static LevelData data;
	
	private Level level;
	
	private Control controller;
	
	private Turret currentType;
	
	private GameObject selectedObject;
	
	private boolean isControllingTurret;
	
	private FadeText2 notificationText;
	
	private int delta;
	
	private int numOfRevivalSSS;
	
	private boolean hasCreatedSSSReviveDialog;
	private CustomCountdownTimer sssReviveTimer;
	
	// <---------------------------> NEW UI
	private int id;
	
	private EntityDisplayer eDisplayer;
	private boolean isDisplayStat;
	private Image ePortrait;
	
	private LevelMenu menu;
	
	private ReviveDialog rDialog;
	private PopupWindow rPopup;
	
	private HealthDisplayer healthDisplay;
	
	public GameState(int id) {
		this.id = id;
	}

	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		initGame(game);
	}
	
	private void initGame(StateBasedGame sg){
		numOfRevivalSSS = 0;
		controller.resetControlStats();
		controller.resetTypeStats();
		ImmunityManager.getInstance().stop();
		ImmunityManager.getInstance().reset();
		currentType = null;
		selectedObject = null;
		isControllingTurret = false;
		notificationText.reset();
		isDisplayStat = false;
		isControllingTurret = false;
		hasCreatedSSSReviveDialog = false;
		
		level = new Level(data);
		
		healthDisplay = new HealthDisplayer(level.getSubSecuritySystem());
		
		if(menu != null)
			getRootPane().removeChild(menu);
		if(eDisplayer != null)
			getRootPane().removeChild(eDisplayer);
		
		menu = new LevelMenu(sg, this, level);
		
		getRootPane().add(menu);
	}
	
	public void restart(StateBasedGame sg){
		initGame(sg);
	}
	
	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		// Init the objects that will not be in initGame
		notificationText = new FadeText2();
		controller = Control.getControl();
	}

	public void update(final GameContainer gc, final StateBasedGame sg, int delta)
			throws SlickException {
		this.delta = delta;
		
		checkInput(gc.getInput());
		
		level.update(sg, delta);
		
		menu.setLevelStat(level.getRound(), level.getCurrentEnemiesRemaining(), level.getCurrency(), level.getUpgradeCurrency(), level.getCurrentPoints());
		
		if(isDisplayStat && eDisplayer != null)
			eDisplayer.update();
		
		if(level.getCurrentState() == Level.SSS_DAMAGE_STATE){
			if(!hasCreatedSSSReviveDialog){
				hasCreatedSSSReviveDialog = true;
				
				rPopup = null;
				rDialog = null;
				
				if(sssReviveTimer == null){
					sssReviveTimer = new CustomCountdownTimer(20000);
				}else{
					sssReviveTimer.reset();
				}
				
				level.setPaused(true);
				
				rPopup = new PopupWindow(getRootPane());
				rPopup.setTheme("revivepopup");
				rPopup.setCloseOnClickedOutside(false);
				rPopup.setCloseOnEscape(false);
				
				rDialog = new ReviveDialog();
				rDialog.addButton("Revive", new Runnable(){
					public void run(){
						int answer = 0;
						
						try{
							answer = Integer.parseInt(rDialog.getText());
						}catch(Exception e){
							e.printStackTrace();
						}
						
						if(answer == rDialog.getAnswer()){
							numOfRevivalSSS++;
							
							if(numOfRevivalSSS <= 2){
								level.incCurrency(DEFAULT_SSS_REVIVAL_CURRENCY_REWARD);
							}
							
							level.getSubSecuritySystem().setCurrentHealth(level.getSubSecuritySystem().getMaxHealth() - (level.getSubSecuritySystem().getMaxHealth() * (numOfRevivalSSS / 10f)));
							level.getSubSecuritySystem().setAlive(true); 
							level.addObject(level.getSubSecuritySystem());
							level.setCurrentState(Level.KNOCKBACK_STATE);
							hasCreatedSSSReviveDialog = false;
							
							level.setPaused(false);
							
							getRootPane().removeChild(rDialog);
							rPopup.closePopup();
						}else{
							level.setPaused(false);
							
							getRootPane().removeChild(rDialog);
							rPopup.closePopup();
							
							sg.enterState(Application.GAMEOVERSTATE);
						}
					}
				});
				rDialog.addButton("Suicide", new Runnable(){
					public void run(){
						level.setPaused(false);
						
						getRootPane().removeChild(rDialog);
						rPopup.closePopup();
						
						sg.enterState(Application.GAMEOVERSTATE);
					}
				});
				
				rPopup.add(rDialog);
				rPopup.openPopupCentered();
			}else{
				if(sssReviveTimer.canStart()){
					if(sssReviveTimer.isTimeElapsed()){
						level.setPaused(false);
						
						getRootPane().removeChild(rDialog);
						rPopup.closePopup();
						hasCreatedSSSReviveDialog = false;
						sg.enterState(Application.GAMEOVERSTATE);
					}else{
						rDialog.setTime(sssReviveTimer.getTimeLeftInSeconds());
						sssReviveTimer.tick(delta);
					}
				}else{
					sssReviveTimer.start();
				}
			}
		}
		
		ArrayList<Widget> rWidgets = new ArrayList<Widget>();
		
		for(int i = 0; i < getRootPane().getNumChildren(); i++){
			if(getRootPane().getChild(i) instanceof EntityDisplayer){
				EntityDisplayer d = (EntityDisplayer) getRootPane().getChild(i);
				if(!isDisplayStat || !d.getObject().isAlive()){
					rWidgets.add(d);
				}
			}
		}
		
		for(Widget w : rWidgets)
			getRootPane().removeChild(w);
	}
	
	private void checkInput(Input input){
		if(input.isKeyDown(controller.getInput(Control.CONTROL_MOVE_SCREEN_LEFT))){
			level.incLevelCameraX(-5f); 
		}else if(input.isKeyDown(controller.getInput(Control.CONTROL_MOVE_SCREEN_RIGHT))){
			level.incLevelCameraX(5f);
		}else if(input.isKeyDown(controller.getInput(Control.CONTROL_MOVE_SCREEN_UP))){
			level.incLevelCameraY(-5f);
		}else if(input.isKeyDown(controller.getInput(Control.CONTROL_MOVE_SCREEN_DOWN))){
			level.incLevelCameraY(5f);
		}
		
		if(isControllingTurret){
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				if(!isMouseAtGUI(input.getMouseX(), input.getMouseY())){
					if(input.isKeyPressed(controller.getInput(Control.CONTROL_EXIT_MANUAL_CONTROL_OF_TURRET))){
						isControllingTurret = false;
						((BasicTurret) selectedObject).setControlledManually(false);
						notificationText.start("Uncontrolling Turret", 250);
					}
					
					if(((BasicTurret) selectedObject).getRange().contains(level.getCameraX() + input.getMouseX(), level.getCameraY() + input.getMouseY())){
						((BasicTurret) selectedObject).manualFire(level.getCameraX() + input.getMouseX(), level.getCameraY() + input.getMouseY());
					}
				}
			}
			
			if(((BasicTurret) selectedObject).getRange().contains(level.getCameraX() + input.getMouseX(), level.getCameraY() + input.getMouseY())){
				selectedObject.setAngle(CustomMath.getAngle(selectedObject.getBounds().getCenterX(), selectedObject.getBounds().getCenterY(), 
						level.getCameraX() + input.getMouseX(), level.getCameraY() + input.getMouseY()));
			}
		}else{
			if(input.isKeyPressed(controller.getInput(Control.CONTROL_MANUAL_CONTROL_OF_TURRET))){
				if(selectedObject instanceof BasicTurret && ((BasicTurret) selectedObject).canBeControlled()){
					((BasicTurret) selectedObject).setControlledManually(true);
					isControllingTurret = true;
					currentType = null;
					notificationText.start("Controlling Turret", 250);
				}
			}
			
			if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_WALL))){
				currentType = controller.getType(Control.CONTROL_SELECT_WALL);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_CANON))){
				currentType = controller.getType(Control.CONTROL_SELECT_CANON);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_FROSTBYTE))){
				currentType = controller.getType(Control.CONTROL_SELECT_FROSTBYTE);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_TERA))){
				currentType = controller.getType(Control.CONTROL_SELECT_TERA);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_PIDAR))){
				currentType = controller.getType(Control.CONTROL_SELECT_PIDAR);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_ANTITROLL))){
				currentType = controller.getType(Control.CONTROL_SELECT_ANTITROLL);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_RECYNERGY))){
				currentType = controller.getType(Control.CONTROL_SELECT_RECYNERGY);
				notificationText.start("Selected: " + currentType, 250);
			}else if(input.isKeyPressed(controller.getInput(Control.CONTROL_SELECT_MINE))){
				currentType = controller.getType(Control.CONTROL_SELECT_MINE);
				notificationText.start("Selected: " + currentType, 250);
			}
			
			// Restocking & Reparing Turret
			if(input.isKeyPressed(controller.getInput(Control.CONTROL_REPAIR_TURRET))){
				if(selectedObject != null && selectedObject instanceof BasicTurret && !(selectedObject instanceof Wall)){
					if(selectedObject.getCurrentHealth() != selectedObject.getMaxHealth()){
						int cost = (int) (((BasicTurret) selectedObject).getRepairCost() * (selectedObject.getMaxHealth() - selectedObject.getCurrentHealth()));
						int difference = 0;
						
						if((difference = level.getCurrency() - cost) >= 0){
							level.setCurrency(difference);
							
							selectedObject.setCurrentHealth(selectedObject.getMaxHealth());
						}else{
							int repairAmount = (level.getCurrency() / ((BasicTurret) selectedObject).getRepairCost());
							
							level.setCurrency(0);
							
							selectedObject.incCurrentHealth(repairAmount);
							
							if(selectedObject.getCurrentHealth() > selectedObject.getMaxHealth()){
								selectedObject.setCurrentHealth(selectedObject.getMaxHealth());
							}
						}
					}
				}
			}
			
			if(input.isKeyPressed(controller.getInput(Control.CONTROL_RESTOCK_TURRET))){
				if(selectedObject != null && selectedObject instanceof BasicTurret && !(selectedObject instanceof Wall) &&
						!(selectedObject instanceof BasicGenerator)){
					if(((BasicTurret) selectedObject).getCurrentAmmo() != ((BasicTurret) selectedObject).getMaxAmmo()){
						int cost = (int) (((BasicTurret) selectedObject).getRestockCost() * (((BasicTurret) selectedObject).getMaxAmmo() - ((BasicTurret) selectedObject).getCurrentAmmo()));
						int difference = 0;
						
						if((difference = level.getCurrency() - cost) >= 0){
							level.setCurrency(difference);
							
							((BasicTurret) selectedObject).setCurrentAmmo(((BasicTurret) selectedObject).getMaxAmmo());
						}else{
							int restockAmount = (level.getCurrency() / ((BasicTurret) selectedObject).getRestockCost());
							
							level.setCurrency(0);
							
							((BasicTurret) selectedObject).incCurrentAmmo(restockAmount);
							
							if(((BasicTurret) selectedObject).getCurrentAmmo() > ((BasicTurret) selectedObject).getMaxAmmo()){
								((BasicTurret) selectedObject).setCurrentAmmo(((BasicTurret) selectedObject).getMaxAmmo());
							}
						}
					}
				}
			}
			
			if(input.isKeyPressed(Input.KEY_U)){
				if(selectedObject != null && selectedObject instanceof BasicTurret){
					BasicTurret turret = (BasicTurret) selectedObject;
					
					if(turret.isUpgradeable()){
						BasicTurret upgradeTurret = TurretFactory.getTurret(turret.getX(), turret.getY(), turret.getUpgradeTurret());
						upgradeTurret.setCurrentHealth(turret.getCurrentHealth());
						upgradeTurret.setCurrentShield(turret.getCurrentShield());
						upgradeTurret.setCurrentAmmo(turret.getCurrentAmmo());
						turret.setAlive(false);
						selectedObject = upgradeTurret;
						level.addObject(upgradeTurret);
					}
				}
			}
			
			// Or Upgrade Via Whole Thing!
			if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_CANON)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_CANON)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_CANON).isUpgradeable()){
					
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_CANON).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_CANON).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_CANON, type);
						notificationText.start("Upgraded Canon", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_CANON + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}else if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_FROSTBYTE)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_FROSTBYTE)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_FROSTBYTE).isUpgradeable()){
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_FROSTBYTE).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_FROSTBYTE).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_FROSTBYTE, type);
						notificationText.start("Upgraded FrostByte", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_FROSTBYTE + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}else if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_TERA)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_TERA)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_TERA).isUpgradeable()){
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_TERA).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_TERA).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_TERA, type);
						notificationText.start("Upgraded Tera", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_TERA + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}else if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_PIDAR)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_PIDAR)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_PIDAR).isUpgradeable()){
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_PIDAR).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_PIDAR).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_PIDAR, type);
						notificationText.start("Upgraded PIDar", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_PIDAR + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}else if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_ANTITROLL)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_ANTITROLL)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_ANTITROLL).isUpgradeable()){
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_ANTITROLL).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_ANTITROLL).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_ANTITROLL, type);
						notificationText.start("Upgraded AntiTroll", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_ANTITROLL + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}else if(input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_RECYNERGY)[0]) && input.isKeyDown(controller.getKeyInputs(Control.CONTROL_UPGRADE_RECYNERGY)[1])){ 
				if(controller.getType(Control.CONTROL_SELECT_RECYNERGY).isUpgradeable()){
					int difference = 0;
					
					if((difference = (level.getUpgradeCurrency() - controller.getType(Control.CONTROL_SELECT_RECYNERGY).getUpgradeCost())) >= 0){
						Turret type = controller.getType(Control.CONTROL_SELECT_RECYNERGY).getUpgradeTurret();
						controller.setKeyType(Control.CONTROL_SELECT_RECYNERGY, type);
						notificationText.start("Upgraded ReCyNergy", 250);
					}else{
						notificationText.start("Unabled to upgrade " + controller.getType(Control.CONTROL_SELECT_RECYNERGY + "! [" + "Need " + -difference + "]"), 250);
					}
				}
			}
			
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				if(!isMouseAtGUI(input.getMouseX(), input.getMouseY())){
					if(!level.placeTower(currentType, level.getCameraX() + input.getMouseX(), 
							level.getCameraY() + input.getMouseY())){
						selectedObject = level.getObjectFrom(level.getCameraX() + input.getMouseX(), 
								level.getCameraY() + input.getMouseY());
						
						if(selectedObject != null){
							eDisplayer = new EntityDisplayer(getRootPane(), menu, this, level, selectedObject);
							getRootPane().add(eDisplayer);
							
							if(selectedObject.getPortraitImage() != null){
								ePortrait = selectedObject.getPortraitImage();
							}else{
								ePortrait = Resources.UNAVAILABLE_PORTRAIT;
							}
							
							ePortrait = ePortrait.getScaledCopy(75, 120);
							
							isDisplayStat = true;
						}else{
							if(eDisplayer != null)
								getRootPane().removeChild(eDisplayer);
							
							isDisplayStat = false;
						}
					}else{
						currentType = null;
						
						if(eDisplayer != null)
							getRootPane().removeChild(eDisplayer);
						
						isDisplayStat = false;
					}
				}
			}
			
			if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
				currentType = null;
				
				notificationText.start("Deselected Turret", 250);
			}
		}
		
	}
	
	public static void setLevelData(LevelData data){
		GameState.data = data;
	}

	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		level.render(g);
		
		if(isDisplayStat){
			// TODO Need to make it so that when mouse press here,
			// it will consume the input
			g.drawImage(ePortrait, 0, 480);
		}
		
		healthDisplay.render(gc, g);

		if(currentType != null){
			if(level.canPlaceTower(level.getCameraX() + gc.getInput().getMouseX(), 
					level.getCameraY() + gc.getInput().getMouseY())){		
				g.translate(-level.getCameraX(), -level.getCameraY());
				g.setColor(new Color(Color.blue.r, Color.blue.g, Color.blue.b, 0.5f));
				g.fillRect((((int) (level.getCameraX() + gc.getInput().getMouseX()) / 32)) * 32, 
						(((int) (level.getCameraY() + gc.getInput().getMouseY()) / 32)) * 32, 32, 32);
				
				if(currentType.getRange() != 0){
					g.setColor(new Color(Color.red.r, Color.red.g, Color.red.b, 0.5f));

					Circle c = new Circle(((int) ((level.getCameraX() + gc.getInput().getMouseX()) / 32)) * 32 + 16, 
							((int) ((level.getCameraY() + gc.getInput().getMouseY()) / 32)) * 32 + 16,
							currentType.getRange());
					
					g.draw(c);
				}

				g.translate(level.getCameraX(), level.getCameraY());
			}
		}
		
		if(!level.isPaused())
			notificationText.draw(g, 50, 300, delta);
	}
	
	public void setSelectedObject(GameObject object){
		this.selectedObject = object;
		
		if(selectedObject != null){
			eDisplayer = new EntityDisplayer(getRootPane(), menu, this, level, selectedObject);
			getRootPane().add(eDisplayer);
			
			if(selectedObject.getPortraitImage() != null){
				ePortrait = selectedObject.getPortraitImage();
			}else{
				ePortrait = Resources.UNAVAILABLE_PORTRAIT;
			}
			
			ePortrait = ePortrait.getScaledCopy(75, 120);
			
			isDisplayStat = true;
		}
	}
	
	public void setDisplayStat(boolean stat){
		isDisplayStat = stat;
	}
	
	public void setCurrentType(Turret type){
		currentType = type;
	}
	
	public void createText(String text){
		notificationText.start(text, 250);
	}
	
	public boolean isMouseAtGUI(float x, float y){
		RootPane root = getRootPane();
		
		for(int i = 0; i < root.getNumChildren(); i++){
			if(root.getChild(i).isInside((int) x, (int) y)){
				
				return true;
			}
		}
		
		// Check for popups!
		if(root.hasOpenPopups()){
			return true;
		}
		
		return false;
	}

	public int getID() {
		return id;
	}
	
}
