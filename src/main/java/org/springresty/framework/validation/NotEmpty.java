package org.springresty.framework.validation;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springresty.exception.EmptyParameterException;
import org.springresty.utils.I18N;


public class NotEmpty extends AbstractValidator{
	public final static String Empty = "emtpy";
		
	public NotEmpty(String name){
		super(name);
	}
	
	public NotEmpty(String name, Map<String, String> messages){
		super(name, messages);
	}
	
	public void validate(String value) throws EmptyParameterException{		
		if(StringUtils.isEmpty(value)){
			String message = getMessage(Empty);
			throw new EmptyParameterException(StringUtils.isEmpty(message) ? I18N.T("parameter \"?\" is excepted", name) : I18N.T(message, name) );
		}
	}

}
