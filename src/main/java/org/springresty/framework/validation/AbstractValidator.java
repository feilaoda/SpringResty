package org.springresty.framework.validation;

import java.util.Map;


public abstract class AbstractValidator  implements Validator {
	protected String name;
	protected Map<String, String> messages;
	public AbstractValidator(String name){
		this(name, null);
	}
	public AbstractValidator(String name, Map<String, String> messages){
		this.name = name;
		this.messages = messages;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public String getMessage(String key){
		if(this.messages == null){
			return null;
		}
		return messages.get(key);
	}

	

}
