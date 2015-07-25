package com.feeling.dao;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.feeling.convert.DateConverter;
import com.feeling.dto.EventBaseDto;
import com.feeling.dto.EventCycleRecordDto;
import com.feeling.vo.EventCycleRecordVo;
import com.feeling.vo.EventTextVo;
import com.feeling.vo.UserEventVo;

public class Test {
	static {
	      ConvertUtils.register(new DateConverter(null), java.util.Date.class);
	} 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventBaseDto eventBaseDto = new EventBaseDto();
		UserEventVo eventVo = new UserEventVo();
		eventVo.setDeviceId("adf");
		eventVo.setLat(123.2);
		eventVo.setEventTextVo(new EventTextVo());
		try {
			BeanUtils.copyProperties(eventBaseDto, eventVo);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(eventVo);
	}

}
