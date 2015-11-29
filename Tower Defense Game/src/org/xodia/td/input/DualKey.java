package org.xodia.td.input;

// Has two input keys instead of one key
public class DualKey extends Key{

	private int key2;
	
	public DualKey(String name, int key, int key2) {
		super(name, key);
		
		this.key2 = key2;
	}
	
	public void setKey2(int key){
		this.key2 = key;
	}
	
	public int getKey2(){
		return key2;
	}

}
