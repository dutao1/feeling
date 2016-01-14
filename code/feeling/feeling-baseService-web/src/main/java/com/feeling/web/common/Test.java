package com.feeling.web.common;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.feeling.utils.CryptUtil;

public class Test {

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		// TODO Auto-generated method stub
		System.out.println(StringUtils.isNumericSpace("0"));
		System.out.println(StringUtils.isNumeric(""));
		System.out.println(StringUtils.isNumericSpace("1123"));
		System.out.println(StringUtils.isNumericSpace("-123"));
	}

}
