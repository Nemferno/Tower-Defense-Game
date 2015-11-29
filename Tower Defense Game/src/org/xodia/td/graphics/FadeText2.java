package org.xodia.td.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.xodia.td.util.CustomCountdownTimer;

/**
 * 
 * @author Jasper Bae
 *
 * This class can handle more than one text via
 * start(String, int) method.
 * 
 * No longer needs int constructor argument in order
 * to create the fadetext.
 * 
 */
public class FadeText2 {

	private String text;
	private float opacity = 1;
	
	private CustomCountdownTimer stillTimer;
	private CustomCountdownTimer fadeTimer;
	
	private boolean canRender;
	
	public FadeText2(){
		this(2000);
	}
	
	public FadeText2(int stillTime){
		fadeTimer = new CustomCountdownTimer(0);
		stillTimer = new CustomCountdownTimer(stillTime);
	}
	
	public void draw(Graphics g, float x, float y, int delta){
		if(canRender){
			if(stillTimer.canStart()){
				if(stillTimer.isTimeElapsed()){
					if(fadeTimer.canStart()){
						if(fadeTimer.isTimeElapsed()){
							if(opacity <= 0){
								canRender = false;
							}else{
								opacity -= 0.1f;
								fadeTimer.reset();
							}
						}else{
							fadeTimer.tick(delta);
						}
					}else{
						fadeTimer.start();
					}
				}else{
					stillTimer.tick(delta);
				}
			}else{
				stillTimer.start();
			}
			
			g.setColor(new Color(1, 1, 1, opacity));
			g.drawString(text, x, y);
		}
	}
	
	public void start(String text, int duration){
		this.text = text;
		
		fadeTimer.setTime(duration);
		
		stillTimer.reset();
		
		canRender = true;
		
		opacity = 1f;
	}
	
	public void reset(){
		canRender = false;
		stillTimer.reset();
		fadeTimer.reset();
		text = "";
		opacity = 1f;
	}
	
}
