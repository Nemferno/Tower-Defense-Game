package org.xodia.td.util;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

/**
 * 
 * Can handle multiple animations at the same time
 * 
 * @author Jasper Bae
 *
 */
public class MultiAnimation {

	private ArrayList<Animation> animationList;
	private ArrayList<Integer> xList;
	private ArrayList<Integer> yList;
	
	public MultiAnimation(){
		animationList = new ArrayList<Animation>();
		xList = new ArrayList<Integer>();
		yList = new ArrayList<Integer>();
	}
	
	public void render(Graphics g){
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		
		for(int i = 0; i < animationList.size(); i++){
			animationList.get(i).draw(xList.get(i), yList.get(i));
			
			if(animationList.get(i).isStopped()){
				removeList.add(i);
			}
		}
		
		for(Integer i : removeList){
			remove(i);
		}
	}
	
	public void add(Animation a, int x, int y){
		a.start();
		animationList.add(a);
		xList.add(x);
		yList.add(y);
	}
	
	public void remove(int index){
		animationList.remove(index);
		xList.remove(index);
		yList.remove(index);
	}
	
}
