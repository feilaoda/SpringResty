package org.springresty.model;

import java.util.Map;

public class QueryOrProperty extends QueryProperty {

	
	public QueryOrProperty()
	{}
	

	public int buildHql(StringBuffer hql, Map<String, Object> queryParams, int index){
		StringBuffer queryHql = new StringBuffer();
		for(QueryProperty property: propertyList){
			StringBuffer paramHql = new StringBuffer();
			int i = property.buildHql(paramHql, queryParams, index);
			if(queryHql.length()>0){
				queryHql.append(" or ");
			}
			queryHql.append(paramHql);
			index = i + 1;
		}
		hql.append(" ( ").append(queryHql).append(" )");
		return index;
	}
}
