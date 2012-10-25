package org.springresty.exception;

public class RestyException extends Exception{
	private String message;
	public RestyException(String message){
		super(message);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
}
