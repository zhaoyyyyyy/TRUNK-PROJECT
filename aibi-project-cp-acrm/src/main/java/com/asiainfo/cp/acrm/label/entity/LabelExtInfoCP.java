/*
 * @(#)ILabelExtInfoDao.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.asiainfo.cp.acrm.base.entity.BaseEntity;

import io.swagger.annotations.ApiParam;



/**
 * @Description: 中国邮政标签扩展信息
 * @author jinbh
 *
 */
@Entity
@Table(name = "LOC_CP_LABEL_EXT_INFO")
public class LabelExtInfoCP extends BaseEntity {

    private static final long serialVersionUID = 2035013017939483936L;

    /**
     * 标签ID
     */
    @Id
    @Column(name = "LABEL_ID")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @ApiParam(value = "标签ID")
    private String labelId;

    /**
     * 标签排序描述
     */
    @Column(name = "sort_desc")
    @ApiParam(value = "排序sql语句，如：order_date asc")
    private String sortDesc;

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(String sortDesc) {
		this.sortDesc = sortDesc;
	}

}
