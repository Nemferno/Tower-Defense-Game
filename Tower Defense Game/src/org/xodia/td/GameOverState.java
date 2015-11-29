package org.xodia.td;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.BasicTWLGameState;
import de.matthiasmann.twl.Button;

public class GameOverState extends BasicTWLGameState{
	
	private int id;
	
	public GameOverState(int id) {
		this.id = id;
	}

	public void init(final GameContainer gc, final StateBasedGame sg)
			throws SlickException {
		Button restart = new Button("Restart");
		restart.addCallback(new Runnable(){
			public void run(){
				sg.enterState(Application.GAMESTATE);
			}
		});
		restart.setSize(150, 50);
		restart.setPosition(225, 450);
		
		Button back = new Button("Back");
		back.addCallback(new Runnable(){
			public void run(){
				sg.enterState(Application.MENUSTATE);
			}
		});
		back.setSize(150, 50);
		back.setPosition(425, 450);
		
		getRootPane().add(restart);
		getRootPane().add(back);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
	}

	public void render(GameContainer gc, StateBasedGame sg, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("GAME OVER", gc.getWidth() / 2 - 40, gc.getHeight() / 2);
	}
	
	public int getID(){
		return id;
	}

}
