package com.asiainfo.cp.acrm.base.utils;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class StringUtil  extends StringUtils{
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	  public static String[] split(String seperators, String list, boolean include)
	  {
	    StringTokenizer tokens = new StringTokenizer(list, seperators, include);
	    String[] result = new String[tokens.countTokens()];
	    int i = 0;

	    while (tokens.hasMoreTokens())
	      result[(i++)] = tokens.nextToken();
	    return result;
	  }
	  
	  public static String[] split(String src, String flag)
	  {
	    return StringUtils.splitPreserveAllTokens(src, flag);
	  }
	  
	  /**
		 * 将字符串的byte数组转换成十六进制串
		 * 
		 * @param data
		 *            byte数组
		 * @return 十六进制字符串
		 * @author miaofc
		 * @date Nov 28, 2011 4:40:26 PM
		 */
		public static String byteArray2HexStr(byte[] data) {
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			int l = data.length;
			char[] out = new char[l << 1];
			// two characters form the hex value.
			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = digits[(0xF0 & data[i]) >>> 4];
				out[j++] = digits[0x0F & data[i]];
			}
			return new String(out);
		}
}
