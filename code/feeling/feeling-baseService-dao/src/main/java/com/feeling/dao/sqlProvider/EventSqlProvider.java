package com.feeling.dao.sqlProvider;

import java.util.Map;

import com.feeling.constants.SqlConstants;
import com.google.common.base.Joiner;

public class EventSqlProvider {
	
	/**
	 * 根据事件id 查询列表
	 * @param params
	 * @return
	 */
	public String getEventListByIdList(Map<String,Object> params){
		
		Integer[] ids = (Integer[]) params.get("ids");
		StringBuilder mainSql = new StringBuilder();
		mainSql.append(SqlConstants.GET_EVENT_BASE_LIST).append(SqlConstants.WHERE);
		if (ids==null||ids.length<1) {
			mainSql.append(SqlConstants.NO_EQUAL_SQL);
		} else {
			mainSql.append( " id in (");
		    mainSql.append(  Joiner.on(",").skipNulls().join(ids));
		    mainSql.append(" ) ");
		}
		return mainSql.toString();
	}
}
