package com.asiainfo.biapp.si.coc.jauth.frame.entity;

import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;

/**
 * 
 * @author 
 *
 * @version 1.0, 2010-8-12
 */
public abstract class BaseEntity implements java.io.Serializable,Cloneable {
    
	private static final long serialVersionUID = 2035013017939483936L;
	
    private String id;
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	@Override
    public String toString() {
		return super.toString();
    }

	@Override
	public Object clone(){ 
	    BaseEntity o = null; 
	   try{ 
	       o = (BaseEntity)super.clone(); 
	   }catch(CloneNotSupportedException e){ 
	       LogUtil.error(e); 
	   }
	   
	   return o; 
	} 
	
	
}
