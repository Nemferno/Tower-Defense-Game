package org.xodia.td.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.xodia.td.util.CustomCountdownTimer;

// Does not work that well in terms of timing since it depends on the delta variable
public class FadeText {

	private String text;
	private float opacity = 1;
	
	private CustomCountdownTimer stillTimer;
	private CustomCountdownTimer fadeTimer;
	
	private boolean canRender = false;
	
	/**
	 * 
	 * @param text
	 *  The text that will appear
	 * @param duration
	 *  The number of seconds the text will show (1000 = 1 sec)
	 */
	public FadeText(int duration){
		fadeTimer = new CustomCountdownTimer(duration);
		
		// Still Timer is when the text appears first and gives the user
		// time to see what the message is
		stillTimer = new CustomCountdownTimer(2000); // 2 seconds is preferable or more
		stillTimer.start();
	}
	
	public void draw(Graphics g, float x, float y, int delta){
		if(canRender){
			if(stillTimer.isTimeElapsed()){
				if(fadeTimer.canStart()){
					if(fadeTimer.isTimeElapsed()){
						if(opacity <= 0){
							canRender = false;
							
							fadeTimer.reset();
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
			
			g.setColor(new Color(1, 1, 1, opacity));
			g.drawString(text, x, y);
		}
	}
	
	// This method resets, when called
	public void setRendering(boolean render){
		canRender = render;
		
		stillTimer.reset();
		fadeTimer.reset();
		
		opacity = 1;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public boolean canRender(){
		return canRender;
	}
	
	public float getOpacity(){
		return opacity;
	}
	
}
