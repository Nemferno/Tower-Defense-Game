package org.xodia.td;

import java.net.URL;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import TWLSlick.TWLStateBasedGame;

public class Application extends TWLStateBasedGame{

	public static final String TITLE = "Tower Defense";
	
	public static final int SWIDTH = 800, SHEIGHT = 600;
	
	public static final int BUILD = 14;
	
	public static final int MENUSTATE = 1,
							GAMESTATE = 2,
							GAMEOVERSTATE = 3;
	
	public Application() {
		super(TITLE + " Build: " + BUILD);
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		try{
			Resources.loadResources();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		addState(new MenuState(MENUSTATE));
		addState(new GameState(GAMESTATE));
		addState(new GameOverState(GAMEOVERSTATE));
		enterState(MENUSTATE);
	}
	
	public static void main(String[] args) throws SlickException{
		AppGameContainer container = new AppGameContainer(new Application(), SWIDTH, SHEIGHT, false);
		container.setTargetFrameRate(60);
		container.setVSync(true);
		container.start();
	}

	@Override
	protected URL getThemeURL() {
		return ResourceLoader.getResource("assets/user-interface/ui-xmls/Structure-UI.xml");
	}
	
	
	
}
