package com.asiainfo.biapp.si.coc.jauth.frame.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.Page;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;

public abstract class BaseServiceImpl<M,ID> implements BaseService<M,String> {
	
	public Logger logger = LoggerFactory.getLogger(super.getClass());
	
	
	protected abstract BaseDao<M,String> getBaseDao();
    
	@Override
    public void save(M model) {
        getBaseDao().save(model);
    }
    @Override
    public void saveOrUpdate(M model) {
    	getBaseDao().saveOrUpdate(model);
    }
    @Override
    public void update(M model) {
    	getBaseDao().update(model);
    }
    @Override
    public void delete(Object id) {
    	getBaseDao().delete(id);
    }
    @Override
    public void deleteObject(M model) {
    	this.deleteObject(model);
    }
    @Override
    public M get(Object id) {
    	return getBaseDao().get(id);
    }
    @Override
    public Integer excuteSql(String sql,Object... params) {
    		return getBaseDao().excuteSql(sql,params);
    }
    @Override
    public Integer getCountSql(String sql,Object... params) {
    	return getBaseDao().getCountSql(sql,params);
    }
    
    /**
     * 通过参数拿到分页器
     * eg: from Dic where :name = name
     * @param hql
     * @param params  <name='行政区划'>
     * @return
     */
    @Override
    public Page<M> findPageByHql(Page<M> page,String hql,Map<String,Object> params){
    	return getBaseDao().findPageByHql(page, hql, params);
    }
    
    /**
     * 通过参数查询集合
     * @param hql
     * @param args
     * @return
     */
    @Override
    public List<M> findListByHql(String hql,Object... args){
    	return getBaseDao().findListByHql(hql, args);
    }
    @Override
    public List<M> findListByHql(String hql,Map<String,Object> params){
    	return getBaseDao().findListByHql(hql, params);
    }

    
    
    
    
    /**
     * 通过参数查询集合
     * @param hql
     * @param args
     * @return
     */
    @Override
    public List<?> findListBySql(String sql,Object... args){
    	return (List<?>) getBaseDao().findListBySql(sql, args);
    }
    @Override
    public List<?> findListBySql(String sql,Map<String,Object> params){
    	return getBaseDao().findListBySql(sql, params);
    }
    
    

    
    /**
     * @describe 得到系统时间
     * @author zhougz
     * @param
     * @date 2013-5-17
     */
    public Date getSystemDate(){
    	return getBaseDao().getSystemDate();
    }
    
	/**
	 * 获取系统时间
	 * @return Timestamp
	 */
	 public Timestamp getSystemTime(){
		 return getBaseDao().getSystemTime();
	 }
    
    
	/**
	 * @see 其他操作(增删改返回受影响的行数)
	 * @author ljs
	 * @date 2013-6-25
	 * @param hql hql语句
	 * @param args 参数列表(可以为null)
	 * @return Object
	 */
    @Override
	public Integer excuteHql(String hql, Object... args){
    	return getBaseDao().excuteHql(hql, args);
	}
    
	/**
	 * @see 从数据库中获得一个实体或一个值
	 * @author ljs
	 * @date 2013-6-25
	 * @param hql hql语句
	 * @param args 参数列表(可以为null)
	 * @return Object
	 */
    @Override
	public M findOneByHql(String hql, Object... args){
		return getBaseDao().findOneByHql(hql, args);
	}
    
	/**
	 * 
	 * @describe 获取所有实体
	 * @author ljs
	 * @date 2013-8-12
	 * @return
	 */
	@Override
	public List<M> getAll(){
		return getBaseDao().getAll();
	}
	/**
	 * 
	 * @describe 获取当前路径
	 * @author liukai
	 * @param
	 * @date 2013-10-10
	 */
	public String getRealPath(String path){
		return path;
	}

	/**
	 * @describe 备份表
	 * 
	 * @param String oldTableName 旧表名
	 * @param String newTableName 新表名
	 * @param String isHasData 是否包含数据（true：包含,false:不包含（默认））
	 * @return
	 * 
	 * @author hongfb
	 * @date 2018-3-19
	 */
	public Integer CreateTable(String oldTableName, String newTabelName, boolean isHasData){
		return getBaseDao().CreateTable(oldTableName, newTabelName, isHasData);
	}
	
	/**
	 * @describe 清空表
	 * 
	 * @param String tableName 表名
	 * 
	 * @author hongfb
	 * @date 2018-3-19
	 */
	public Integer truncateTable(String tableName){
		return getBaseDao().truncateTable(tableName);
	}
	
	
}
