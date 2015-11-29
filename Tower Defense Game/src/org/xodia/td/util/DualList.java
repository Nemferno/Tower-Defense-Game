package org.xodia.td.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DualList<K, V> {

	private Set<K> keys;
	private List<V> values;
	
	// Instead of List, Set? Only for Keys
	// Since There Can Only Be One Key!
	public DualList(int capacity){
		keys = new LinkedHashSet<K>(capacity);
		values = new ArrayList<V>(capacity);
	}
	
	public DualList(){
		this(16);
	}
	
	public void add(K key, V value){
		// We must check if the key is added
		if(keys.add(key)){
			values.add(value);
		}else{
			// Remove it and give out an error
			throw new IdenticalKeyException();
		}
	}
	
	public void remove(int index){
		int subIndex = 0;
		K removedKey = null;
		
		for(K k : keys){
			if(subIndex == index){
				removedKey = k;
				break;
			}
			
			subIndex++;
		}
		
		if(removedKey != null){
			keys.remove(removedKey);
		}
		
		values.remove(index);
	}
	
	public void remove(K key){		
		keys.remove(key);
		values.remove(getIndexFrom(key));
	}
	
	public void setValue(V value, int index){
		values.set(index, value);
	}
	
	public void setValue(K key, V value){
		values.set(getIndexFrom(key), value);
	}
	
	public V getValueFrom(K key){
		System.out.println("object");
		return values.get(getIndexFrom(key));
	}
	
	public K getKeyFrom(int index){
		K key = null;
		
		int subIndex = 0;
		
		for(K k : keys){
			if(index == subIndex){
				key = k;
				break;
			}
			
			subIndex++;
		}
		
		return key;
	}
	
	public int getLength(){
		return keys.size();
	}
	
	private int getIndexFrom(K key){
		int index = 0;
		
		for(K k : keys){
			if(k == key){
				break;
			}
			
			index++;
		}
		
		return index;
	}
	
}
