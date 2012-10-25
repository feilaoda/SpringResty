/**
 * 
 */
package org.springresty.model;

import org.apache.commons.lang.StringUtils;


 
public class Pagination {
	//public static int DefaultPageMinSize = 10;
	public static int DefaultPageSize = 30;	
	public static int DefaultPageIndex = 1;
	
	public static String SortModeDesc = "DESC";
	public static String SortModeAesc = "AESC";
	
	public Pagination(){
		pageIndex = DefaultPageIndex;
		pageSize = DefaultPageSize;
		sinceId = 0;
		maxId = 0;
	}
	
	public Pagination(String page, String count, String sinceId, String maxId){
		if (!StringUtils.isEmpty(page)) {
			this.setPageIndex(Integer.parseInt(page));
		}

		if (!StringUtils.isEmpty(count)) {
			this.setPageSize(Integer.parseInt(count));
		}

		if (!StringUtils.isEmpty(sinceId)) {
			this.setSinceId(Integer.parseInt(sinceId));
		}

		if (!StringUtils.isEmpty(maxId)) {
			this.setMaxId(Integer.parseInt(maxId));
		}
	}
		
	
	//每页多少条
	private int pageSize;
	//当前页
	private int pageIndex;
	private int sinceId;
	private int maxId;
	
	//排序字段
	private String sortName = "id";
	//排序方式
	private String sortMode = "DESC";
	
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getSortMode() {
		return sortMode;
	}
	public void setSortMode(String sortMode) {
		this.sortMode = sortMode;
	}

	public void setSinceId(int sinceId) {
		this.sinceId = sinceId;
	}

	public int getSinceId() {
		return sinceId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}

	public int getMaxId() {
		return maxId;
	}
	
 
	
	

}
