package org.springresty.model;

public enum QueryMatcher {


	/**
	 * 查询匹配枚举
	 * 
	 */

	    /**
	     * = 等于
	     */
	    EQ(QueryMatcherString.EQUAL),
	    /**
	     * < 小于
	     */
	    LT(QueryMatcherString.LESS),
	    /**
	     * < 大于
	     */
	    GT(QueryMatcherString.GREATER),
	    /**
	     * <= 小于等于
	     */
	    LE(QueryMatcherString.LESS_EQUAL),
	    /**
	     * >= 大于等于
	     */
	    GE(QueryMatcherString.GREATER_EQUAL),
	    /**
	     * like查询
	     */
	    LIKE(QueryMatcherString.LIKE),
	    /**
	     * in 查询
	     */
	    IN(QueryMatcherString.IN),
	    /**
	     * != 不等于
	     */
	    NE(QueryMatcherString.NOT_EQUAL),
	    /**
	     * <>不等于
	     */
	    LG(QueryMatcherString.LESS_GREATER),
	    /**
	     * is null 为空
	     */
	    IS_NULL(QueryMatcherString.IS_NULL),
	    /**
	     * is not null 不为空
	     */
	    IS_NOT_NULL(QueryMatcherString.IS_NOT_NULL);

	    
	    private String matcherString;

	    private QueryMatcher(String s) {
	        this.setMatcherString(s);
	    }

	    

	    public String getMatcherString() {
			return matcherString;
		}



		public void setMatcherString(String matcherString) {
			this.matcherString = matcherString;
		}



		public static QueryMatcher getType(String input) {
	        if (null == input || input.trim().length() == 0) {
	            return null;
	        }
	        input = input.toLowerCase();
	        QueryMatcher matchType = null;
	        QueryMatcher[] types = QueryMatcher.values();
	        for (QueryMatcher type : types) {
	            if (type.getMatcherString().equals(input)) {
	                matchType = type;
	                break;
	            }
	        }
	        return matchType;
	    }
	 

	
}
