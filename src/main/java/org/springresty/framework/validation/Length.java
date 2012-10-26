package org.springresty.framework.validation;

import java.util.Map;

public class Length extends AbstractValidator{
	private int min;
	private int max;
	
	public Length(String name, int min, int max){
		this(name,  min, max, null);
	}
	
	public Length(String name, int min, int max, Map<String, String> messages){
		super(name, messages);
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(String value) {
		// TODO Auto-generated method stub
		
	}

}
