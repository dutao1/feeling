package com.feeling.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {
	private static final String Algorithm = "DESede";
	public static String DecryptionKey = "FEAEBA37AD8B31CBCF633AFE044A445BA55BE3781672145A";
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			
			Cipher c1 = Cipher.getInstance(Algorithm);
			
//			byte[] iv=getInitVector();
			
			c1.init(Cipher.ENCRYPT_MODE, deskey);

			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}

		return null;
		
	}

	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static String encrypt(String src) {
		return GetHexString(Encrypt(src.getBytes()));
	}
	public static String decrypt(String src) {
		if(src==null||src.length()%16 !=0){
			return "" ;
		}
		byte[] decrypt = Decrypt(src);
		try {
			return new String(decrypt,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "" ;
	}
	public static byte[] Encrypt(String src) {
		return Encrypt(src.getBytes());
	}

	public static byte[] Encrypt(byte[] src) {
		byte[] keybyte=GetBytes(DecryptionKey,DecryptionKey.length());
		return encryptMode(keybyte,src);
	}

	public static byte[] Decrypt(byte[] src) {
		byte[] keybyte=GetBytes(DecryptionKey,DecryptionKey.length());
		return decryptMode(keybyte,src);
	}

	public static byte[] Decrypt(String src) {
		return Decrypt(GetBytes(src,src.length()));
	}

	public static byte[] GetBytes(String key, int len) {

		char[] keys = key.toCharArray();
		byte[] buffer = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			buffer[i / 2] = (byte) (ToHexValue(keys[i], true) + ToHexValue(
					keys[i + 1], false));
		}
		return buffer;
	}

	public static String GetHexString(byte[] b) {
		String hs = "";//
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte ToHexValue(char c, boolean high) {
		byte num;
		if ((c >= '0') && (c <= '9')) {
			num = (byte) (c - '0');
		} else if ((c >= 'a') && (c <= 'f')) {
			num = (byte) ((c - 'a') + 10);
		} else {
			if ((c < 'A') || (c > 'F')) {
				throw new RuntimeException();
			}
			num = (byte) ((c - 'A') + 10);
		}
		if (high) {
			num = (byte) (num << 4);
		}
		return num;
	}
	
	public static void main(String[] args){
		String s= encrypt("6");
		System.out.println(s);//AE499229BB22B4AA
		System.out.println(s.length());
		try {
			
			System.out.println(decrypt("1489B7CDEFDD5F2D"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
