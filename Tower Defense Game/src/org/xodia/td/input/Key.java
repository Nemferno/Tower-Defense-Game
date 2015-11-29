package org.xodia.td.input;

public class Key {

	protected String name;
	protected int key;
	
	public Key(String name, int key){
		this.name = name;
		this.key = key;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setKey(int key){
		this.key = key;
	}
	
	public String getName(){
		return name;
	}
	
	public int getKey(){
		return key;
	}
	
}
