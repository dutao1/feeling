package com.feeling.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.feeling.dao.sqlProvider.BaseSqlProvider;

public interface BaseDao<T> {

	@UpdateProvider(type=BaseSqlProvider.class,method = "updateObjectById")
	public int updateByPk(@Param("tVo") T tVo);
	
	
	/**
	 * 根据对象，插入
	 * @param tVo
	 * @param id
	 */
	@InsertProvider(type=BaseSqlProvider.class,method = "insertObject")
	public void insertWithOutId(@Param("tVo") T tVo,@Param("id") Integer id);

	@SelectProvider(type=BaseSqlProvider.class,method = "selectByPk")
	public T selectByPk(@Param("tVo") T tVo);

}
