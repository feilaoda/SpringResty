package org.springresty.model;

public class QueryParameter {

	private QueryMatcher matcher;
	private String name;
	private Object value;
	
	public QueryParameter(String name, Object value){
		this.matcher = QueryMatcher.EQ;
		this.name = name;
		this.value = value;
	}
	
	public QueryParameter(String name, Object value, QueryMatcher matcher)	{		
		this.matcher = matcher;		
		this.name = name;
		this.value = value;
	}

	public void setMatcher(QueryMatcher matcher) {
		this.matcher = matcher;
	}

	public QueryMatcher getMatcher() {
		return matcher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
