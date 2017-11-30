package com.asiainfo.cp.acrm.label.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the loc_dimtable_info database table.
 * 
 */
@Entity
@Table(name="loc_dimtable_info")
public class DimtableInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DIM_ID")
	private String dimId;

	@Column(name="CODE_COL_TYPE")
	private String codeColType;

	@Column(name="CONFIG_ID")
	private String configId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER_ID")
	private String createUserId;

	@Column(name="DIM_CODE_COL")
	private String dimCodeCol;

	@Column(name="DIM_COMMENT")
	private String dimComment;

	@Column(name="DIM_TABLENAME")
	private String dimTablename;

	@Column(name="DIM_VALUE_COL")
	private String dimValueCol;

	@Column(name="DIM_WHERE")
	private String dimWhere;

	public DimtableInfo() {
	}

	public String getDimId() {
		return this.dimId;
	}

	public void setDimId(String dimId) {
		this.dimId = dimId;
	}

	public String getCodeColType() {
		return this.codeColType;
	}

	public void setCodeColType(String codeColType) {
		this.codeColType = codeColType;
	}

	public String getConfigId() {
		return this.configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getDimCodeCol() {
		return this.dimCodeCol;
	}

	public void setDimCodeCol(String dimCodeCol) {
		this.dimCodeCol = dimCodeCol;
	}

	public String getDimComment() {
		return this.dimComment;
	}

	public void setDimComment(String dimComment) {
		this.dimComment = dimComment;
	}

	public String getDimTablename() {
		return this.dimTablename;
	}

	public void setDimTablename(String dimTablename) {
		this.dimTablename = dimTablename;
	}

	public String getDimValueCol() {
		return this.dimValueCol;
	}

	public void setDimValueCol(String dimValueCol) {
		this.dimValueCol = dimValueCol;
	}

	public String getDimWhere() {
		return this.dimWhere;
	}

	public void setDimWhere(String dimWhere) {
		this.dimWhere = dimWhere;
	}

}