/*
 * @(#)BeanUtils.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.frame.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;

import com.asiainfo.biapp.si.coc.jauth.frame.entity.BaseEntity;


/**
 * Title : BeanUtils
 * <p/>
 * Description : JavaBean的工具类。对BeanUtils的copyProperties方法进行重写，不复制null属性
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

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
    
    private static final String DELIM = ",";

	public static void copyProperties(Object source, Object target)
			throws BeansException {
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source
						.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass()
								.getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						Method writeMethod = targetPd.getWriteMethod();//获取写方法(set)
						if (value != null && !(value instanceof BaseEntity)) {
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}else if (value instanceof BaseEntity) {
							Object targetChd = readMethod.invoke(target);
							copyProperties(value, targetChd);//对自己所写的子对象进行递归
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(targetChd, value);
						}
					} catch (Throwable ex) {
//						throw new FatalBeanException(
//								"Could not copy properties from source to target",
//								ex);
						System.out.println("Could not copy property " + targetPd.getName() + " from source to target");
					}
				}
			}
		}
	}
	
	/**
     * 获取JavaBean的所有属性和类型
     * @param model JavaBean
     * @return JavaBean的属性类型
     * @throws Exception
     */
    public static Map<String,String> getModelAttriButeType(Object model) throws Exception{
        Field[] field = model.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组  
        Map<String,String> map = new HashMap<String, String>();
        for(int j=0 ; j<field.length ; j++){     //遍历所有属性
              String name = field[j].getName();    //获取属性的名字
              
              //System.out.print("attribute name:"+name);     
              name = name.substring(0,1).toUpperCase()+name.substring(1); //将属性的首字符大写，方便构造get，set方法
              String type = field[j].getGenericType().toString();    //获取属性的类型
              /*if(type.equals("class java.lang.String")){   //如果type是类类型，则前面包含"class "，后面跟类名
                  Method m = model.getClass().getMethod("get"+name);
                  String value = (String) m.invoke(model);    //调用getter方法获取属性值
                  if(value != null){

                      System.out.println("attribute value:"+value);
                  }
              }*/
              type = type.replace("class ", "");
              map.put(name, type);
              
        }
        
        return map;
    }
    
    /**
     * 获取JavaBean的所有属性的逗号分隔的字符串
     * @param model JavaBean
     * @return JavaBean的所有属性的逗号分隔的字符串
     * @throws Exception
     */
    public static String getModelAttriButeStr(Object model) throws Exception{
        
        return BeanUtils.getModelAttriButeStr(model, DELIM);
    }
    
    /**
     * 获取JavaBean的所有属性的以分隔符连接的字符串
     * @param model JavaBean
     * @param delim 分隔符
     * @return JavaBean的所有属性的以分隔符连接的字符串
     * @throws Exception
     */
    public static String getModelAttriButeStr(Object model, String delim) throws Exception{
        if (StringUtil.isBlank(delim)) {
            delim = DELIM;
        }
        
        return ArrayUtil.join(BeanUtils.getModelAttriButeType(model).keySet().toArray(), delim);
    }
    
    
}