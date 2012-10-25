package org.springresty.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class QueryProperty {

	protected List<QueryProperty> propertyList;

	public List<QueryProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<QueryProperty> propertyList) {
		this.propertyList = propertyList;
	}

	public void add(QueryParameter queryParameter) {
		if (propertyList == null) {
			propertyList = new ArrayList<QueryProperty>(1);
		}
		propertyList.add(new QueryValueProperty(queryParameter));
	}

	public void add(QueryProperty queryProperty) {
		if (propertyList == null) {
			propertyList = new ArrayList<QueryProperty>(1);
		}
		propertyList.add(queryProperty);
	}
	
	
	public void add(String name, Object value) {
		QueryParameter parameter = new QueryParameter(name, value);
		add(parameter);
	}
	
	public void add(String name, Object value, QueryMatcher matcher) {
		QueryParameter parameter = new QueryParameter(name, value, matcher);
		add(parameter);
	}

	public abstract int buildHql(StringBuffer hql, Map<String, Object> queryParams, int index);
}
