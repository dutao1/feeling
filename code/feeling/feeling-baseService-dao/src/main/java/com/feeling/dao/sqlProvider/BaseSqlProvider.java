package com.feeling.dao.sqlProvider;

import java.util.Map;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.utils.Reflector;
import com.feeling.utils.SqlUtil;

 


/**
 * packagesql
 *@author dutao
 *@date 2015年7月3日 下午5:33:00
 *@description 
 */
public class BaseSqlProvider {
	
	
	public String selectByPk(Map<String,Object> params) throws Exception {
		//获取参数
		Object param = params.get(SqlConstants.PARAM_NAME);
		if(param==null){
			throw new OptException(ReturnCodeEnum.SQL_PARAM_NAME_ERROR);
		}
		Class clazz = param.getClass();
		//未定义表
		Table tableAnno = (Table) clazz.getAnnotation(Table.class);
		if(tableAnno==null){
			throw new OptException(ReturnCodeEnum.SQL_TABLE_NAME_EMPTY,clazz.getSimpleName());
		}
		String tableName = tableAnno.name();//表名
		if (param != null&&tableName!=null) {
			// 更新
			return "select * from "+tableName+" where id=#{"+SqlConstants.PARAM_NAME+".id}";
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String insertObject(Map<String,Object> params) throws Exception {
		//获取参数
		Object param = params.get(SqlConstants.PARAM_NAME);
		if(param==null){
			throw new OptException(ReturnCodeEnum.SQL_PARAM_NAME_ERROR);
		}
		Class clazz = param.getClass();
		//未定义表
		Table tableAnno = (Table) clazz.getAnnotation(Table.class);
		if(tableAnno==null){
			throw new OptException(ReturnCodeEnum.SQL_TABLE_NAME_EMPTY,clazz.getSimpleName());
		}
		String tableName = tableAnno.name();//表名
		if (param != null&&tableName!=null) {
			// 更新
			return SqlUtil.insertSqlObject(param, tableName);
		}
		return null;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String updateObjectById(Map<String,Object> params) throws Exception {
		//获取参数 
		Object param = params.get(SqlConstants.PARAM_NAME);
		if(param==null){
			throw new OptException(ReturnCodeEnum.SQL_PARAM_NAME_ERROR);
		}
		Class clazz = param.getClass();
		//未定义表
		Table tableAnno = (Table) clazz.getAnnotation(Table.class);
		if(tableAnno==null){
			throw new OptException(ReturnCodeEnum.SQL_TABLE_NAME_EMPTY,clazz.getSimpleName());
		}
		String tableName = tableAnno.name();//表名
		String pKName = tableAnno.pkName();//主键
		if (param != null&&tableName!=null) {
			// 更新
			return SqlUtil.updateSqlByPk(param,pKName, tableName);
		}
		return null;
	}
}
