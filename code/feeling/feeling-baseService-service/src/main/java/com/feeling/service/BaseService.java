package com.feeling.service;

import org.apache.commons.beanutils.ConvertUtils;

import com.feeling.convert.DateConverter;
import com.feeling.dto.BaseDto;
import com.feeling.utils.Geohash;

public class BaseService {
	
	Geohash  geoHash = new Geohash();
	static {
	      ConvertUtils.register(new DateConverter(null), java.util.Date.class);
	} 
	/**
	 * geohash
	 * @param lat
	 * @param lon
	 * @return String
	 */
	public String getGeoHash(Double lat ,Double lon){
		if(lat!=null&&lon!=null){
			return geoHash.encode(lat, lon);
		}
		return null;
	}
	/**
	 * longGeoHash
	 * @param lat
	 * @param lon
	 * @return
	 */
	public long getLongGeoHash(Double lat ,Double lon){
		if(lat!=null&&lon!=null){
			return geoHash.enLongCode(lat, lon);
		}
		return -1;
	}
}
