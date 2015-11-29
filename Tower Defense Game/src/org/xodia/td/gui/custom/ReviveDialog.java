package org.xodia.td.gui.custom;

import java.util.Random;

import de.matthiasmann.twl.EditField;

public class ReviveDialog extends Dialog{

	private final int 	ADD_ADD = 0,
						ADD_SUBTRACT = 1,
						ADD_MULTIPLY = 2,
						ADD_DIVIDE = 3,
						SUBTRACT_SUBTRACT = 4,
						SUBTRACT_ADD = 5,
						SUBTRACT_MULTIPLY = 6,
						SUBTRACT_DIVIDE = 7;
	
	private Random random;
	private float answer;
	private String strQuestion;
	private float x, y, z;
	private int type;
	private EditField textfield;
	
	public ReviveDialog(){
		super("");

		random = new Random();
		type = random.nextInt(8);
		x = random.nextInt(350) + 1;
		y = random.nextInt(250) + 1;
		z = random.nextInt(150) + 1;
		
		switch(type){
		case ADD_ADD:
			strQuestion = x + " + " + y + " + " + z;
			answer = x + y + z;
			break;
		case ADD_SUBTRACT:
			strQuestion = x + " + " + y + " - " + z;
			answer = x + y - z;
			break;
		case ADD_MULTIPLY:
			strQuestion = x + " + " + y + " * " + z;
			answer = x + y * z;
			break;
		case ADD_DIVIDE:
			strQuestion = x + " + " + y + " / " + z;
			answer = x + y / z;
			break;
		case SUBTRACT_SUBTRACT:
			strQuestion = x + " - " + y + " - " + z;
			answer = x - y - z;
			break;
		case SUBTRACT_ADD:
			strQuestion = x + " - " + y + " + " + z;
			answer = x - y + z;
			break;
		case SUBTRACT_MULTIPLY:
			strQuestion = x + " - " + y + " * " + z;
			answer = x - y * z;
			break;
		case SUBTRACT_DIVIDE:
			strQuestion = x + " - " + y + " / " + z;
			answer = x - y / z;
			break;
		}
		
		setText("What is: " + strQuestion);
		
		answer = (int) Math.round(answer);
		
		textfield = new EditField();
		
		addWidget(textfield);
	}
	
	public void setTime(int time){
		setText(strQuestion + " (Remaining: " + time + "s)");
	}
	
	public String getText(){
		return textfield.getText().trim();
	}
	
	public float getAnswer(){
		return answer;
	}
	
}
