package org.xodia.td.util;

public class IdenticalKeyException extends RuntimeException{

	private static final long serialVersionUID = -2766806976821595726L;

	public IdenticalKeyException(String message){
		super(message);
	}
	
	public IdenticalKeyException(){
		super("Identical Key In Set");
	}
	
}
