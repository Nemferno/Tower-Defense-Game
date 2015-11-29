package org.xodia.td;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.xodia.td.gui.custom.Dialog;

import TWLSlick.BasicTWLGameState;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ToggleButton;

public class MenuState extends BasicTWLGameState{

	private int id;
	
	private Button play;
	private Button exit;
	
	private ToggleButton sound;
	private ToggleButton music;
	
	public MenuState(int id){
		this.id = id;
	}
	
	public void init(final GameContainer gc, final StateBasedGame sg)
			throws SlickException {
		sound = new ToggleButton();
		sound.setTheme("soundbutton");
		sound.setPosition(700, 550);
		sound.setSize(25, 25);
		sound.addCallback(new Runnable(){
			public void run(){
				if(!gc.isSoundOn())
					gc.setSoundOn(true);
				else
					gc.setSoundOn(false);
			}
		});
	
		getRootPane().add(sound);
		
		music = new ToggleButton();
		music.setTheme("musicbutton");
		music.setPosition(750, 550);
		music.setSize(25, 25);
		music.addCallback(new Runnable(){
			public void run(){
				if(!gc.isMusicOn())
					gc.setMusicOn(true);
				else
					gc.setMusicOn(false);
			}
		});
		
		getRootPane().add(music);
		
		play = new Button("Play!");
		play.addCallback(new Runnable(){
			public void run(){
				GameState.setLevelData(Resources.AlphaData);
				sg.enterState(Application.GAMESTATE);
			}
		});
		
		exit = new Button("Exit");
		exit.addCallback(new Runnable(){
			public void run(){
				final PopupWindow popup = new PopupWindow(getRootPane());
				popup.setTheme("exitpopup");
				popup.setCloseOnClickedOutside(false);

				final Dialog dialog = new Dialog("Do you want to exit the game?");
				dialog.setPosition(300, 350);
				dialog.setSize(400, 100);
				dialog.addButton("Confirm", new Runnable(){
					public void run(){
						gc.exit();
					}
				});
				dialog.addButton("Cancel", new Runnable(){
					public void run(){
						dialog.setVisible(false);
						popup.closePopup();
					}
				});
				
				popup.add(dialog);
				popup.openPopupCentered();
			}
		});
		
		getRootPane().add(play);
		getRootPane().add(exit);
	}
	
	public void update(GameContainer gc, StateBasedGame sg, int delta)
			throws SlickException {

	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		
	}
	
	protected void layoutRootPane() {
		play.setPosition(300, 350);
		play.setSize(200, 50);
		
		exit.setPosition(300, 425);
		exit.setSize(200, 50);
	}

	public int getID() {
		return id;
	}
	
}
