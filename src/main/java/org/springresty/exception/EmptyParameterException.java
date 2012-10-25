package org.springresty.exception;


public class EmptyParameterException extends RestyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000581108170147135L;
	
	public EmptyParameterException(String message){
		super(message);
	}
}
