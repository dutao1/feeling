package com.feeling.dao;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.feeling.convert.DateConverter;
import com.feeling.dto.EventCycleRecordDto;
import com.feeling.vo.EventCycleRecordVo;

public class Test {
	static {
	      ConvertUtils.register(new DateConverter(null), java.util.Date.class);
	} 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventCycleRecordVo eventCycleRecordVo = new EventCycleRecordVo();
		eventCycleRecordVo.setDeviceId("adf");
		eventCycleRecordVo.setLat(123.2);
		EventCycleRecordDto eventCycleRecordDto=new EventCycleRecordDto();
		try {
			BeanUtils.copyProperties(eventCycleRecordDto, eventCycleRecordVo);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
