package org.springresty.framework.validation;

public class Length implements Validator{
	private String name;
	private String message;
	private int min;
	private int max;
	
	public Length(String name, int min, int max){
		this(name, null, min, max);
	}
	
	public Length(String name, String message, int min, int max){
		this.name = name;
		this.message = message;
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
