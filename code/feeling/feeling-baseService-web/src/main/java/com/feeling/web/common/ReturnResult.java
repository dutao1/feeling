package com.feeling.web.common;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.utils.DataJSON;

/**
 * @author dutao
 * web返回结果定义
 *
 */
public class ReturnResult implements Serializable {
	private static final long serialVersionUID = -8379100651362895882L;

	protected Integer  code;
	protected Object data;

	private String errorDesc="";

	// 排除不输出的字段
	private String[] excludeProperties;
	// 包含只输出的字段
	private String[] includeProperties;

	public ReturnResult(){}

	public ReturnResult(Integer code,String errorDesc){
		this.code=code;
		this.errorDesc=errorDesc;
	}
	 

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	public void setResultEnu(ReturnCodeEnum resultEnu){
		if(resultEnu!=null){
			this.code = resultEnu.getCode();
			this.errorDesc =resultEnu.getMessage();
		}
	}
	
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the excludeProperties
	 */
	public String[] getExcludeProperties() {
		return excludeProperties;
	}

	/**
	 * @param excludeProperties
	 *            the excludeProperties to set
	 */
	public void setExcludeProperties(String[] excludeProperties) {
		this.excludeProperties = excludeProperties;
	}

	/**
	 * @return the includeProperties
	 */
	public String[] getIncludeProperties() {
		return includeProperties;
	}

	/**
	 * @param includeProperties
	 *            the includeProperties to set
	 */
	public void setIncludeProperties(String[] includeProperties) {
		this.includeProperties = includeProperties;
	}
	@Override
	public String toString() {
		JSONObject resultObject = new JSONObject();
		if (code == null) {
			resultObject.put("code", ReturnCodeEnum.SUCCESS.getCode());
			resultObject.put("message", ReturnCodeEnum.SUCCESS.getMessage()+" ");
		} else {
			resultObject.put("code", code);
			resultObject.put("message",errorDesc);
		}

		PropertyFilter myFilter = new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				if (includeProperties != null && includeProperties.length > 0) {
					for (String _name : includeProperties) {
						if (name.equalsIgnoreCase(_name)) {
							return true;
						}
					}
					return false;
				}
				if (excludeProperties != null && excludeProperties.length > 0) {
					for (String _name : excludeProperties) {
						if (name.equalsIgnoreCase(_name)) {
							return false;
						}
					}
					return true;
				}

				return true;
			}
		};
		SerializeFilter[] filters = { myFilter };
		SerializerFeature[] futures = {
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue };

		if (data != null) {
			 if (data instanceof OptException) {
				OptException exp = (OptException) data;
				JSONObject errorObject = new JSONObject();
				errorObject.put("code", exp.getCode());
				errorObject.put("message", exp.getMessage());
				resultObject.put("data", errorObject);
			} else {
				String jsonString = DataJSON.toJSONString(data, false, filters,
						futures);
				resultObject.put("data", JSON.parse(jsonString));
			}
		}

		return DataJSON.toJSONString(resultObject);
	}

}
