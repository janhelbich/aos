package cz.hel.aos.service;

import java.util.Date;

public class SimpleCriteria {

	public SimpleOrderBy order;
	public Date dateFrom;
	public Date dateTo;
	public Integer firstResult;
	public Integer offset;
	
	public static class SimpleOrderBy {
		
		public String attrName;
		public SimpleOrderType orderType;
		
		@Override
		public String toString() {
			return "SimpleOrderBy [attrName=" + attrName + ", orderType="
					+ orderType + "]";
		}
	}
	
	public static enum SimpleOrderType {
		ASC, DESC;
	}

	@Override
	public String toString() {
		return "SimpleCriteria [order=" + order + ", dateFrom=" + dateFrom
				+ ", dateTo=" + dateTo + ", firstResult=" + firstResult
				+ ", offset=" + offset + "]";
	}
	
	
}
