package org.xodia.td.util;

import java.util.ArrayList;

/** A list object that has two keys and one value. Two keys to make sure that this
 * value is unique. DOES NOT DUPLICATE objects with the same x and y coordinates as
 * another object. What it will do is replace the value. 
**/
public class Triad<K, K2, V>{
	
	private ArrayList<K> keyOneList;
	private ArrayList<K2> keyTwoList;
	private ArrayList<V> valueList;
	
	public Triad(int capacity){
		keyOneList = new ArrayList<K>(capacity);
		keyTwoList = new ArrayList<K2>(capacity);
		valueList = new ArrayList<V>(capacity);
	}
	
	public boolean register(K k, K2 k2, V v){
		// We have to check if the keys are in the list!
		for(int i = 0; i < keyOneList.size(); i++){
			if(keyOneList.get(i) == k && keyTwoList.get(i) == k2){
				valueList.set(i, v);
				
				return true;
			}
		}
		
		keyOneList.add(k);
		keyTwoList.add(k2);
		valueList.add(v);
		
		return true;
	}
	
	public boolean remove(V v){
		for(int i = 0; i < keyOneList.size(); i++){
			if(valueList.get(i) == v){
				valueList.remove(i);
				keyOneList.remove(i);
				keyTwoList.remove(i);
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean remove(K k, K2 k2){
		for(int i = 0; i < keyOneList.size(); i++){
			if(keyOneList.get(i) == k && keyTwoList.get(i) == k2){
				valueList.remove(i);
				keyOneList.remove(i);
				keyTwoList.remove(i);
				
				return true;
			}
		}
		
		return false;
	}
	
	public V get(K k, K2 k2){
		for(int i = 0; i < keyOneList.size(); i++){
			if(keyOneList.get(i) == k && keyTwoList.get(i) == k2){
				return valueList.get(i);
			}
		}
		
		return null;
	}
	
	public void clear(){
		keyOneList = new ArrayList<K>();
		keyTwoList = new ArrayList<K2>();
		valueList = new ArrayList<V>();
	}
	
}
