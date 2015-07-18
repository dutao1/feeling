package com.feeling.dao;


import org.apache.ibatis.annotations.Insert;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.UserBaseDto;



/**
 * 用户基本资料dao
 * @author dutao
 */
public interface UserBaseDao  extends  BaseDao<UserBaseDto>{

	@Insert(SqlConstants.INSERT_USER_BASE_SQL)
	public void insertWithId(UserBaseDto userBaseDto);
}
