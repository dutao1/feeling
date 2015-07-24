package com.feeling.service.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feeling.service.UserService;
import com.feeling.vo.UserVo;

/**
 *@author dutao
 *@date 2015年7月6日 下午2:11:09
 *@description 
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class UserBaseServiceTest  extends ServiceTestBase {


	@Autowired
	UserService userService;
	
	@Test
	public void testReg(){
		UserVo uvo = new UserVo();
		uvo.setAvatar("asdf");
		uvo.setBirthday(new Date());
		uvo.setCityCode("123");
		uvo.setDeviceId("a");
		uvo.setLat(120.21);
		uvo.setLon(-110.123);
		uvo.setGender(1);
		uvo.setDeviceImei("a");
		uvo.setDeviceType("android");
		uvo.setMobile("15810205088");
		uvo.setPwd("abc");
		Integer uid = userService.regNewUser(uvo);
		System.out.println(uid);
	}
}
