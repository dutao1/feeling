package com.feeling.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.UserBaseDto;



/**
 * 用户基本资料dao
 * @author dutao
 */
public interface UserBaseDao  extends  BaseDao<UserBaseDto>{

	@Insert(SqlConstants.INSERT_USER_BASE_SQL)
	public void insertWithId(UserBaseDto userBaseDto);
	
	@Select(SqlConstants.CHECK_PWD_SQL)
	public UserBaseDto checkPwd(@Param("uid")Integer uid,@Param("pwd")String pwd);
	
	@Select(SqlConstants.CHECK_PWD_BYMOBILE_SQL)
	public UserBaseDto checkPwdByMobile(@Param("mobile")String mobile,@Param("pwd")String pwd);
	
}
