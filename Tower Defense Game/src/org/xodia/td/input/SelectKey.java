package org.xodia.td.input;

/**
 * 
 * This type of key class is a generic class.
 * 
 * @author JasperBae
 *
 */
public class SelectKey<T extends Enum<?>> extends Key{

	private T type;
	
	public SelectKey(String name, int key, T t){
		super(name, key);
		
		type = t;
	}
	
	public void setType(T t){
		type = t;
	}
	
	public T getType(){
		return type;
	}
	
}
