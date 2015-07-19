package com.feeling.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feeling.dto.EventBaseDto;
import com.feeling.dto.UserBaseDto;

/**
 *@author dutao
 *@date 2015年7月6日 下午2:11:09
 *@description 
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class UserBaseDaoTest  extends DaoTestBase {


	@Autowired
	UserBaseDao userBaseDao;
	
	@Autowired
	EventBaseDao eventBaseDao;
	
	//@Test
	public void testInsertUserBase(){
		UserBaseDto u= new UserBaseDto();
		u.setMobile("13212312313");
		u.setBirthday(new Date());
		u.setNickName("adf");
		userBaseDao.insertWithId(u);
		System.out.println(u.getId());
		
	}
	
	
	@Test
	public void testInsertEventBase(){
		eventBaseDao.getEventListByUid(1, 1, 1);
		/*EventBaseDto u= new EventBaseDto();
		u.setUid(1);
		u.setEventType("123");
		Integer i=1;
		eventBaseDao.insert(u);
		System.out.println(u.getId());*/
		
	}
}
