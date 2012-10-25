package org.springresty.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryFilter {
	private List<QueryProperty> propertyList = new ArrayList<QueryProperty>(0);

	public QueryFilter(){
		
	}
	
	public void add(QueryParameter parameter){		
		propertyList.add(new QueryValueProperty(parameter));
	}
	
	public void add(QueryProperty property){
		propertyList.add(property);
	}
	
	public void add(String name, Object value) {
		QueryParameter parameter = new QueryParameter(name, value);
		add(parameter);
	}
	
	public void add(String name, Object value, QueryMatcher matcher) {
		QueryParameter parameter = new QueryParameter(name, value, matcher);
		add(parameter);
	}
	
	public List<QueryProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<QueryProperty> propertyList) {
		this.propertyList = propertyList;
	}
	
	public StringBuffer buildQueryHQL(final StringBuffer hql, final Map<String, Object> queryParams){
		
		StringBuffer queryHql = new StringBuffer();		
		int index=1;			
		for(QueryProperty property: propertyList){
			StringBuffer paramHql = new StringBuffer();
			int i = property.buildHql(paramHql, queryParams, index);
			if(queryHql.length()>0){
				queryHql.append(" and ");
			}
			queryHql.append(paramHql);
			index = i + 1;
		}
		
		hql.append(queryHql);
		return hql;
	}
}
