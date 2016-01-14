package com.feeling.service.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.feeling.constants.Constants;
import com.feeling.dao.EventBaseDao;
import com.feeling.dao.EventCycleRecordDao;
import com.feeling.dto.EventBaseDto;
import com.feeling.enums.EventStatusEnum;
import com.feeling.utils.DateUtil;
import com.feeling.utils.DateUtil.Format;

 
/**
 * @author dutao
 *
 */
public class UptExpireEventJob {

	 private static final Logger logger = Logger
	            .getLogger(UptExpireEventJob.class);
	 @Autowired
	 EventCycleRecordDao eventCycleRecordDao;// 事件流转操作
	 @Autowired
	 EventBaseDao eventBaseDao;// 事件基本操作
	 
	 public void doProcess(){
		long begin = System.currentTimeMillis();
		Date d =  DateUtil.add(new Date(), Constants.EXPIRE_DAYS, Calendar.DATE);
		String dateStr= DateUtil.formatDate(d, Format.HYPHEN_YYYYMMDDHHMMSS);
		logger.info("UptExpireEventJob,now  to del create_date as early as :["+dateStr+"] datas !!!!");
		List<EventBaseDto> eids = eventBaseDao.getExpireEventList(d);
		if(eids!=null&&eids.size()>0){
			logger.info("UptExpireEventJob,now  to del create_date as early as :["+dateStr+"] datas,eids.size=["+eids.size()+"] !!!!");
			for(EventBaseDto edto:eids){
				edto.setStatus(EventStatusEnum.EXPIRE.getCode());
				try{
					eventBaseDao.updateByPk(edto);
					eventCycleRecordDao.updateEventCycleStatus(edto.getId(), EventStatusEnum.EXPIRE.getCode());
				}catch(Exception e){
					logger.error("UptExpireEventJob,now  to del create_date as early as :["+dateStr+"] datas,had error ,error is "+e.getMessage());
				}
				
			}
		}else{
			logger.info("UptExpireEventJob,now  to del create_date as early as :["+dateStr+"] datas,eids.size=[0] !!!!");
		}
		long use = System.currentTimeMillis()-begin;
		logger.info("UptExpireEventJob, to del create_date as early as :["+dateStr+"] datas,use time ["+use+"]ms !!!!");
	}
}
