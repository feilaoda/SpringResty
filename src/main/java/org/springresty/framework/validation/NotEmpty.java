package org.springresty.framework.validation;

import org.springresty.exception.EmptyParameterException;
import org.springresty.utils.I18N;


public class NotEmpty implements Validator{
	private String name;
	private String message;

	public NotEmpty(String name){
		this.name = name;
	}
	
	public NotEmpty(String name, String message){
		this.name = name;
		this.message = message;
	}
	
	public void validate(String value) throws EmptyParameterException{
		if(value == null){
			throw new EmptyParameterException(this.message == null ? I18N.T("parameter \"?\" is excepted", name) : this.message);
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
