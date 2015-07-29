package com.feeling.utils;

import java.util.Iterator;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.feeling.constants.SqlConstants;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;




public class SqlUtil {
	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public static String getInsertSqlStr(Object o) throws Exception {
	
		TreeMap hm = Reflector.getAllVals(o);
		if(hm==null||hm.isEmpty()){
			return null;
		}
		Iterator it = hm.keySet().iterator();
		StringBuilder  mainSql= new StringBuilder();
		StringBuilder insertSQL = new StringBuilder();// 插入sql
		StringBuilder insertPreSQL = new StringBuilder();// 插入字段
		while (it.hasNext()) {
			String key = (String) it.next();
			try {
				boolean b = Reflector.isInterface(Reflector.getFieldType(o, key), "java.util.Set");
				if (!b) {
					Object val = hm.get(key);
					boolean isCheckOk = false;
					if(val!=null){
						if (val.getClass().getName().equals("String") || val.getClass().getName().equals("java.lang.String")) {
							isCheckOk = val == null || val.toString().equals("") ? false : true;
						}else{
							if(val!=null){
								isCheckOk=true;
							}
						}
					}
					if (isCheckOk) {
						insertPreSQL.append(toTableColumnName(key)).append(",");
						insertSQL.append("#{"+SqlConstants.PARAM_NAME+".").append(key)
						.append("},");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		insertPreSQL.append(SqlConstants.CREATE_TIME);
		insertSQL.append("now()");
		mainSql.append("insert into ").append("").append("(").append(insertPreSQL.toString()).append(")")
		.append(" values ( ").append(insertSQL.toString()).append(")  ");
		return mainSql.toString();
	}
	
	
	/**
	 * 插入语句
	 * @param o
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public static String insertSqlObject(Object o, String tablename) throws Exception {
	
		TreeMap hm = Reflector.getAllVals(o);
		if(hm==null||hm.isEmpty()){
			return null;
		}
		Iterator it = hm.keySet().iterator();
		StringBuilder  mainSql= new StringBuilder();
		StringBuilder insertSQL = new StringBuilder();// 插入sql
		StringBuilder insertPreSQL = new StringBuilder();// 插入字段
		while (it.hasNext()) {
			String key = (String) it.next();
			try {
				boolean b = Reflector.isInterface(Reflector.getFieldType(o, key), "java.util.Set");
				if (!b) {
					Object val = hm.get(key);
					boolean isCheckOk = false;
					if(val!=null){
						if (val.getClass().getName().equals("String") || val.getClass().getName().equals("java.lang.String")) {
							isCheckOk = val == null || val.toString().equals("") ? false : true;
						}else{
							if(val!=null){
								isCheckOk=true;
							}
						}
					}
					if (isCheckOk) {
						insertPreSQL.append(toTableColumnName(key)).append(",");
						insertSQL.append("#{"+SqlConstants.PARAM_NAME+".").append(key)
						.append("},");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		insertPreSQL.append(SqlConstants.CREATE_TIME);
		insertSQL.append("now()");
		mainSql.append("insert into ").append(tablename).append("(").append(insertPreSQL.toString()).append(")")
		.append(" values ( ").append(insertSQL.toString()).append(")  ");
		return mainSql.toString();
	}
	
	/**
	 * 根据主键更新数据-格式化sql
	 * @param o
	 * @param tablename
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public static String updateSqlByPk(Object o,String pkName, String tablename) throws Exception {
	
		TreeMap hm = Reflector.getAllVals(o);
		if(hm==null||hm.isEmpty()){
			return null;
		}
		Object pk = hm.get(pkName);
		if(pk==null){
			throw new OptException(ReturnCodeEnum.SQL_PK_EMPTY,pkName);
		}
		Iterator it = hm.keySet().iterator();
		StringBuilder  mainSql= new StringBuilder();
		StringBuilder updateSQL = new StringBuilder();// 更新sql
		StringBuilder conditionKey = new StringBuilder();// update的条件语句
		while (it.hasNext()) {
			String key = (String) it.next();
			try {
				boolean b = Reflector.isInterface(Reflector.getFieldType(o, key), "java.util.Set");
				if (!b) {
					Object val = hm.get(key);
					boolean isCheckOk = false;
					boolean isAdd =false;
					if(val!=null){
						if (val.getClass().getName().equals("String") || val.getClass().getName().equals("java.lang.String")) {
							isCheckOk = val == null || val.toString().equals("") ? false : true;
						}else{
							if(val.toString().equals("0")){
								continue;
							}
							if (val.getClass().getName().equals("int") || 
									val.getClass().getName().equals("java.lang.Integer")) {
								if(key.equals(pkName)){
									isCheckOk=false;
								}else{
									//次数add
									if( key.equals("commentTimes")||
											key.equals("spreadTimes")||
											key.equals("skipTimes")||
											key.startsWith("votes")){
												isAdd = Integer.valueOf(val.toString()) ==1 ? true : false;
											}
									isCheckOk=true;
								}
							}
						 }
					}
					if (isCheckOk) {
						if(isAdd){
							updateSQL.append(toTableColumnName(key)).append(" = ").append(toTableColumnName(key)).append("+1").append(",");
						}else{
							updateSQL.append(toTableColumnName(key)).append("=#{"+SqlConstants.PARAM_NAME+".").append(key)
							.append("},");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String uptSql = updateSQL.toString();
		if(uptSql!=null&&uptSql.endsWith(",")){
			uptSql = uptSql.substring(0, uptSql.length()-1);
		}
		mainSql.append("update ").append(tablename).append(" set ").append(uptSql)
		.append(" where "+toTableColumnName(pkName)+"=#{"+SqlConstants.PARAM_NAME+"."+pkName+"} ");
		return mainSql.toString();
	}
	/**
	 * 将vo的属性名转成table字段名
	 * 大写改成前面加_ 比如 userId 改成user_id
	 * @param paramName
	 * @return
	 */
	private static String toTableColumnName(String paramName){
		StringBuilder sbr = new StringBuilder();
		if(StringUtils.isNotEmpty(paramName)){
			char[] chars = paramName.toCharArray();
			
			for(int i=0;i<chars.length;i++){
				char c = chars[i];
				if(c>='A'&&c<='Z'){
					sbr.append(SqlConstants.TABLE_SPLIT_LINE).append(String.valueOf(c).toLowerCase());
				}else{
					sbr.append(String.valueOf(c));
				}
			}
		}
		return sbr.toString();
	}
}
