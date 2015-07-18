package com.feeling.exception;

import org.apache.commons.lang3.ArrayUtils;

import com.feeling.enums.ReturnCodeEnum;

public class OptException extends RuntimeException {

	private static final long serialVersionUID = 3457521120315037413L;
 // 错误信息
    private String message;
    
 // 错误代码
    private Integer code;
    
    
    public OptException(ReturnCodeEnum returnCodeEnum, Object... params) {
        if (ArrayUtils.isNotEmpty(params)) {
            this.message = String.format(returnCodeEnum.getMessage(), params);
        } else {
            this.message = returnCodeEnum.getMessage();
        }
        this.code=returnCodeEnum.getCode();
    }
    
    public Integer getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

	
}


