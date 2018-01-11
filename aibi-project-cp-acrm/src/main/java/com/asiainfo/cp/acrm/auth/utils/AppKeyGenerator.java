package com.asiainfo.cp.acrm.auth.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.asiainfo.cp.acrm.base.utils.SecurityUtils;

public class AppKeyGenerator {
	
	public static void main(String[] argv) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String appKey=SecurityUtils.encryptByMD5("sys_ocrm_360view");
		System.out.println(appKey);
		//585845C5E542F5ECA94E613D6AC4CD90
	}

}
