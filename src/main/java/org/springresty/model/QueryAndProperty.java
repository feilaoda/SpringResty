package org.springresty.model;

import java.util.Map;

public class QueryAndProperty extends QueryProperty {

	public QueryAndProperty()
	{
		
	}
	
	
	public int buildHql(StringBuffer hql, Map<String, Object> queryParams, int index){
		StringBuffer queryHql = new StringBuffer();
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
		return index;
	}
	
}
