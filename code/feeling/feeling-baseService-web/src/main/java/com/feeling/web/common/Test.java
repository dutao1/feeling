package com.feeling.web.common;

import java.util.Random;

import com.feeling.utils.CryptUtil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mobile="12345";
		if(mobile.length()>4){
			System.out.println(mobile.substring(mobile.length()-4));
		}
	}

}
