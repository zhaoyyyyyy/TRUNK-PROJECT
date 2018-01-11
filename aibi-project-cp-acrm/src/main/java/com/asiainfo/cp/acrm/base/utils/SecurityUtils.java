package com.asiainfo.cp.acrm.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.asiainfo.cp.acrm.base.utils.StringUtil;



public class SecurityUtils {
	
	public static String encryptByMD5(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] data = encryptByMD5(plaintext.getBytes());
		String hexStr = StringUtil.byteArray2HexStr(data);
		return hexStr;
	}
	
	public static byte[] encryptByMD5(byte[] plaintext) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plaintext);
		byte[] result = md.digest();
		return result;
	}
	
	public static boolean verifyDigest(String keyInfo, String digest) {
		String digestStr1 = null;
		try {
			digestStr1 =encryptByMD5(keyInfo);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!digest.equals(digestStr1)) {
			return false;
		}
		return true;
	}
}
