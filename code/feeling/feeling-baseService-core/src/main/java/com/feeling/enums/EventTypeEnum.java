package com.feeling.enums;

/**
 * 事件类型
 * @author DUTAO
 *
 */
public enum EventTypeEnum {
	
	PIC("pic"),
	TEXT("text"),
	VOTE("vote"),
	AUDIO("audio"),
	VIDEO("video");
	
	private EventTypeEnum(String name){
		this.name = name;
	}
	private String  name;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
