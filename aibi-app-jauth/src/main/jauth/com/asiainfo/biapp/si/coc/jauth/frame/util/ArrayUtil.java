/*
 * @(#)ArrayUtil.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.frame.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Title : ArrayUtil
 * <p/>
 * Description : 数组相关的工具类
 * <p/>
 * CopyRight : CopyRight (c) 2018
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 7.0 +
 * <p/>
 * Modification History :
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2018年1月6日    hongfb        Created</pre>
 * <p/>
 *
 * @author  hongfb
 * @version 1.0.0.2018年1月6日
 */

public final class ArrayUtil extends ArrayUtils {
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static String[] toStringArray(String value, String delim) {
		if (value != null) {
			return StringUtil.split(delim, value);
		}
		return EMPTY_STRING_ARRAY;
	}

	public static Object[] removeElement(Object[] arr, Object target) {
		List<Object> list = new ArrayList<Object>(arr.length);
		for (int i = 0; i < arr.length; ++i) {
			if ((target == arr[i])
					|| ((target != null) && (target.equals(arr[i])))) {
				continue;
			}
			list.add(arr[i]);
		}

		return list.toArray();
	}

	/**
	 * @description 把数组转化为以分隔符连接的字符串
	 * @param array 数组
	 * @param delim 分隔符
	 * @return 以分隔符连接的字符串
	 */
	public static String join(Object[] array, String delim) {
	    StringBuilder result = new StringBuilder();
	    if (isEmpty(array)) {
	        return result.toString();
	    }

        for (int i = 0; i < array.length; i++) {
            result.append(array[i]).append(delim);
        }
        
        return result.deleteCharAt(result.length()-1).toString();
	}
	
	public static String[] toString(Object[] array) {
		String[] result = (String[]) null;
		if (isEmpty(array)) {
			return result;
		}
		result = new String[array.length];
		Object[] arrayOfObject = array;
		int j = array.length;
		for (int i = 0; i < j; ++i) {
			Object obj = arrayOfObject[i];
			result[i] = String.valueOf(obj);
			++i;
		}

		return result;
	}

	public static List<Object> toList(Object array) {
		if (array == null) {
			return new ArrayList<Object>();
		}

		if (array instanceof Object[]) {
			return Arrays.asList((Object[]) array);
		}

		int size = Array.getLength(array);
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < size; ++i) {
			list.add(Array.get(array, i));
		}

		return list;
	}

	public static boolean isEmptyStringArray(String[] array) {
		if ((array == null) || (array.length == 0)) {
			return true;
		}

		String[] arrayOfString = array;
		int j = array.length;
		for (int i = 0; i < j; ++i) {
			String item = arrayOfString[i];
			if (!StringUtil.isEmpty(item)) {
				return false;
			}
		}

		return true;
	}

	public static Object[] removeElements(Object[] arr, Object target) {
		List<Object> list = new ArrayList<Object>(arr.length);
		for (int i = 0; i < arr.length; ++i) {
			if ((target == arr[i])
					|| ((target != null) && (target.equals(arr[i])))) {
				continue;
			}
			list.add(arr[i]);
		}

		return list.toArray();
	}
}
