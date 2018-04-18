
package com.asiainfo.biapp.si.coc.jauth.sysmgr.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Coconfig;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.CoconfigService;

public class ConfigUtil {
	
	private Map<String,String> configMap = new HashMap<String,String>();
	
	/**
	 * 构造方法查询缓存
	 * Description: 
	 *
	 */
	private ConfigUtil(){
		CoconfigService coconfigService = (CoconfigService)SpringContextHolder.getBean("coconfigServiceImpl");
		List<Coconfig> configList = coconfigService.getAllConfig();
		for(Coconfig coconfig : configList){
			configMap.put(coconfig.getConfigKey(), coconfig.getConfigVal());
		}
	}
	
	//弄成单例的  
    private static ConfigUtil config;  
    public static ConfigUtil getInstance(){  
        if(config==null) {
            config= new ConfigUtil();  
        }  
        return config;  
    }
    public static void refush(){
        LogUtil.info("清空了");
        config = null;
    }  
    
    //通过Key拿到value
    public String getValue(String key){  
        return configMap.get(key);
    } 
    

}
