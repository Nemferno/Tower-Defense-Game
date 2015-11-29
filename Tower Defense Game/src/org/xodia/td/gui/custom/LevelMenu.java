package org.xodia.td.gui.custom;

import java.util.HashMap;

import org.newdawn.slick.state.StateBasedGame;
import org.xodia.td.Application;
import org.xodia.td.GameState;
import org.xodia.td.entity.turret.Turret;
import org.xodia.td.input.Control;
import org.xodia.td.level.Level;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;

public class LevelMenu extends Widget{

	public static final String 	CANON = "CANON",
								TERA = "TERA",
								FROSTBYTE = "FROSTBYTE",
								PIDAR = "PIDAR",
								ANTITROLLL = "ANTITROLL",
								RECYNERGY = "RECYNERGY",
								ANTIMINE = "ANTIVIRUS",
								WALL = "WALL";
	
	private HashMap<String, ToggleButton> buttonList;
	private HashMap<String, Turret> typeList;
	
	private Label levelStat;
	
	public LevelMenu(final StateBasedGame sg, final GameState state, final Level level){
		setPosition(650, 0);
		setSize(150, 600);
		
		buttonList = new HashMap<String, ToggleButton>();
		typeList = new HashMap<String, Turret>();
		
		typeList.put(WALL, Turret.WALL);
		typeList.put(CANON, Turret.CANON);
		typeList.put(FROSTBYTE, Turret.FROSTBYTE);
		typeList.put(TERA, Turret.TERA);
		typeList.put(ANTITROLLL, Turret.ANTITROLL);
		typeList.put(ANTIMINE, Turret.ANTIMINE);
		typeList.put(RECYNERGY, Turret.RECYNERGY);
		typeList.put(PIDAR, Turret.PIDAR);
		
		final ToggleButton wall = new ToggleButton("WALL [" + Control.getControl().getType(Control.CONTROL_SELECT_WALL).getCost() + "C]");
		wall.addCallback(new Runnable(){
			public void run(){
				if(!wall.isActive())
					state.setCurrentType(typeList.get(WALL));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != wall)
						b.setActive(false);
				}
			}
		});
		wall.setPosition(10, 5);
		wall.setSize(130, 35);
		
		final ToggleButton canon = new ToggleButton("CANON [" + Control.getControl().getType(Control.CONTROL_SELECT_CANON).getCost() + "C]");
		canon.addCallback(new Runnable(){
			public void run(){
				if(!canon.isActive())
					state.setCurrentType(typeList.get(CANON));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != canon)
						b.setActive(false);
				}
			}
		});
		
		canon.setSize(130, 35);
		canon.setPosition(10, 45);
		
		final ToggleButton frostbyte = new ToggleButton("FROSTBYTE [" + Control.getControl().getType(Control.CONTROL_SELECT_FROSTBYTE).getCost() + "C]");
		frostbyte.addCallback(new Runnable(){
			public void run(){
				if(!frostbyte.isActive())
					state.setCurrentType(typeList.get(FROSTBYTE));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != frostbyte)
						b.setActive(false);
				}
			}
		});
		
		frostbyte.setSize(130, 35);
		frostbyte.setPosition(10, 85);
		
		final ToggleButton tera = new ToggleButton("TERA [" + Control.getControl().getType(Control.CONTROL_SELECT_TERA).getCost() + "C]");
		tera.addCallback(new Runnable(){
			public void run(){
				if(!tera.isActive())
					state.setCurrentType(typeList.get(TERA));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != tera)
						b.setActive(false);
				}
			}
		});
		
		tera.setSize(130, 35);
		tera.setPosition(10, 125);
		
		final ToggleButton pidar = new ToggleButton("PIDAR [" + Control.getControl().getType(Control.CONTROL_SELECT_PIDAR).getCost() + "C]");
		pidar.addCallback(new Runnable(){
			public void run(){
				if(!pidar.isActive())
					state.setCurrentType(typeList.get(PIDAR));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != pidar)
						b.setActive(false);
				}
			}
		});
		
		pidar.setSize(130, 35);
		pidar.setPosition(10, 165);
		
		final ToggleButton antitroll = new ToggleButton("ANTITROLL [" + Control.getControl().getType(Control.CONTROL_SELECT_ANTITROLL).getCost() + "C]");
		antitroll.addCallback(new Runnable(){
			public void run(){
				if(!antitroll.isActive())
					state.setCurrentType(typeList.get(ANTITROLLL));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != antitroll)
						b.setActive(false);
				}
			}
		});
		
		antitroll.setSize(130, 35);
		antitroll.setPosition(10, 205);
		
		final ToggleButton recynergy = new ToggleButton("RECYNERGY [" + Control.getControl().getType(Control.CONTROL_SELECT_RECYNERGY).getCost() + "C]");
		recynergy.addCallback(new Runnable(){
			public void run(){
				if(!recynergy.isActive())
					state.setCurrentType(typeList.get(RECYNERGY));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != recynergy)
						b.setActive(false);
				}
			}
		});
		
		recynergy.setSize(130, 35);
		recynergy.setPosition(10, 245);
		
		final ToggleButton antivirus = new ToggleButton("ANTIMINE [" + Control.getControl().getType(Control.CONTROL_SELECT_MINE).getCost() + "C]");
		antivirus.addCallback(new Runnable(){
			public void run(){
				if(!antivirus.isActive())
					state.setCurrentType(typeList.get(ANTIMINE));
				else
					state.setCurrentType(null);
				
				for(ToggleButton b : buttonList.values()){
					if(b != antivirus)
						b.setActive(false);
				}
			}
		});
		
		antivirus.setSize(130, 35);
		antivirus.setPosition(10, 285);
		
		buttonList.put(WALL, wall);
		buttonList.put(CANON, canon);
		buttonList.put(FROSTBYTE, frostbyte);
		buttonList.put(TERA, tera);
		buttonList.put(PIDAR, pidar);
		buttonList.put(ANTITROLLL, antitroll);
		buttonList.put(RECYNERGY, recynergy);
		buttonList.put(ANTIMINE, antivirus);
		
		for(ToggleButton b : buttonList.values())
			add(b);
		
		levelStat = new Label(
				"Round: " + level.getRound() + "\n" +
				"Remaining: " + level.getCurrentEnemiesRemaining() + "\n" +
				"Currency: " + level.getCurrency() + "\n" +
				"U-Currency: " + level.getUpgradeCurrency() + "\n" +
				"Points: " + level.getCurrentPoints()
				);
		levelStat.setPosition(0, 325);
		levelStat.setSize(150, 150);
		
		add(levelStat);
		
		Button pause = new Button("Pause");
		pause.addCallback(new Runnable(){
			public void run(){
				if(level.isPaused())
					level.setPaused(false);
				else
					level.setPaused(true);
			}
		});
		pause.setPosition(5, 480);
		pause.setSize(140, 35);
		
		Button restart = new Button("Restart");
		restart.addCallback(new Runnable(){
			public void run(){
				state.restart(sg);
			}
		});
		restart.setPosition(5, 520);
		restart.setSize(140, 35);
		
		Button exit = new Button("Exit");
		exit.addCallback(new Runnable(){
			public void run(){
				sg.enterState(Application.MENUSTATE);
			}
		});
		exit.setPosition(5, 560);
		exit.setSize(140, 35);
		
		add(pause);
		add(restart);
		add(exit);
	}
	
	public void setLevelStat(int round, int enemyRemaining, int currency, int ucurrency, int points){
		levelStat.setText(
				"Round: " + round + "\n" +
				"Remaining: " + enemyRemaining + "\n" +
				"Currency: " + currency + "\n" +
				"U-Currency: " + ucurrency + "\n" +
				"Points: " + points
				);
	}
	
	public void changeType(String sType, Turret type){
		System.out.println(sType);
		buttonList.get(sType).setText(type + " [" + type.getCost() + "C]");
		typeList.put(sType, type);
	}
	
}
