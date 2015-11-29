package org.xodia.td;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

// Loads all animations
public class AnimationLoader {

	private AnimationLoader(){}

	public static Animation TURRET_EXPLOSION;
	
	public static void load() throws SlickException{
		TURRET_EXPLOSION = new Animation(new Image[]{ new Image(ResourceLoader.getResourceAsStream("asset/Animation/Explosion/Explosion1.png"), "Explosion1", false),
				new Image(ResourceLoader.getResourceAsStream("asset/Animation/Explosion/Explosion2.png"), "Explosion2", false), new Image(ResourceLoader.getResourceAsStream("asset/Animation/Explosion/Explosion3.png"), "Explosion3", false),
				new Image(ResourceLoader.getResourceAsStream("asset/Animation/Explosion/Explosion4.png"), "Explosion4", false), new Image(ResourceLoader.getResourceAsStream("asset/Animation/Explosion/Explosion5.png"), "Explosion5", false)}, 200);
		
		TURRET_EXPLOSION.setLooping(false);
	}
	
	public static Animation getCopyOf(Animation anime){
		return anime.copy();
	}
	
}
