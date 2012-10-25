package org.springresty.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;



public class QueryValueProperty extends QueryProperty {

	private QueryParameter parameter;
	
	public QueryValueProperty(QueryParameter parameter){
		this.parameter = parameter;
	}
	
	@SuppressWarnings("unchecked")
	public int buildHql(StringBuffer hql, Map<String, Object> queryParams, int index){
		
		StringBuffer queryHql = new StringBuffer();
		String parameterName = parameter.getName();
		
		QueryMatcher matcher = parameter.getMatcher();
		switch(matcher){
			case IN:
				queryHql.append(parameterName);
		 		queryHql.append(" ");
		 		queryHql.append(matcher.getMatcherString());
		 		
	 			Iterator<Object> iterator = null;
	 			if(parameter.getValue() instanceof List){
	 				List<Object> values = (List<Object>)parameter.getValue();
	 				iterator = values.iterator();
	 			}
	 			if(parameter.getValue() instanceof Set){
	 				Set<Object> values = (Set<Object>)parameter.getValue();
	 				iterator = values.iterator();
	 			}
	 			if(iterator.hasNext()){				 			
	 				queryHql.append(" (");		
	 				List<String> paramList = new ArrayList<String>();
	 				
	 				
		 			while(iterator.hasNext()){
		 				Object obj = iterator.next();
		 				paramList.add(":param"+index);
			 			
				 		queryParams.put("param"+index, obj);
				 		index ++;					 		
		 			}		
		 			String paramString = StringUtils.join(paramList.toArray(new String[0]), ",");
		 			queryHql.append(paramString);		 			
	 				queryHql.append(") ");		 			
	 			}
	 		
		 			
			break;
		 	default:
		 		queryHql.append(parameterName);
		 		queryHql.append(" ");
		 		queryHql.append(matcher.getMatcherString());
		 		queryHql.append(" :param");
		 		queryHql.append(index);
		 		queryParams.put("param"+index, parameter.getValue());		 		
		}
		
		hql.append(queryHql);
		
		return index;
	}
}
