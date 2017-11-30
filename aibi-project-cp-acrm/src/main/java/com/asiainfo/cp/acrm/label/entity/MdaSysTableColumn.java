/*
 * @(#)MdaSysTableColumn.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.asiainfo.cp.acrm.base.entity.BaseEntity;

import io.swagger.annotations.ApiParam;

/**
 * Title : MdaSysTableColumn
 * <p/>
 * Description :
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History :
 * <p/>
 * 
 * <pre>
 * NO.    Date    Modified By    Why & What is modified
 * </pre>
 * 
 * <pre>
 * 1    2017年11月21日    lilin7        Created
 * </pre>
 * <p/>
 *
 * @author lilin7
 * @version 1.0.0.2017年11月21日
 */
@Entity
@Table(name = "LOC_MDA_SYS_TABLE_COLUMN")
public class MdaSysTableColumn extends BaseEntity {

    /**  */
    private static final long serialVersionUID = 2249256048929416837L;

    /**
     * 元数据表列id
     */
    @Id
    @Column(name = "COLUMN_ID")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @ApiParam(value = "列Id")
    private String columnId;





	/**
     * 列名
     */
    @Column(name = "COLUMN_NAME")
    @ApiParam(value = "列名")
    private String columnName;

    /**
     * 列中文名
     */
    @Column(name = "COLUMN_CN_NAME")
    @ApiParam(value = "列中文名")
    private String columnCnName;

    /**
     * 列数据类型Id
     */
    @Column(name = "COLUMN_DATA_TYPE_ID")
    @ApiParam(value = "列数据类型Id")
    private Integer columnDataTypeId;


    /**
     * 单位
     */
    @Column(name = "UNIT")
    @ApiParam(value = "单位")
    private String unit;

    /**
     * 数据类型
     */
    @Column(name = "DATA_TYPE")
    @ApiParam(value = "数据类型")
    private String dataType;

    /**
     * 列状态
     */
    @Column(name = "COLUMN_STATUS")
    @ApiParam(value = "列状态")
    private Integer columnStatus;

//    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)  
//    @JoinColumn(name="lABEL_ID")
    @Column(name = "LABEL_ID")
    private String labelId;   
    
    

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	@ManyToMany(fetch=FetchType.LAZY)  
	@JoinTable(name="loc_label_vertical_column_rel",  
	joinColumns={@JoinColumn(name="COLUMN_ID")},inverseJoinColumns={@JoinColumn(name="LABEL_ID")})  
	private List<LabelInfo> labelInfos;
	
	
    public List<LabelInfo> getLabelInfos() {
		return labelInfos;
	}

	public void setLabelInfos(List<LabelInfo> labelInfos) {
		this.labelInfos = labelInfos;
	}

//	public LabelInfo getLabelInfo() {
//		return labelInfo;
//	}
//
//	public void setLabelInfo(LabelInfo labelInfo) {
//		this.labelInfo = labelInfo;
//	}
    
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)  
    @JoinColumn(name="TABLE_ID")  
    private MdaSysTable mdaSysTable;
    
    private String dimTransId;
    
    public String getDimTransId() {
		return dimTransId;
	}

	public void setDimTransId(String dimTransId) {
		this.dimTransId = dimTransId;
	}

	@Transient
    private DimtableInfo dimtableInfo;
    
	public DimtableInfo getDimtableInfo() {
		return dimtableInfo;
	}

	public void setDimtableInfo(DimtableInfo dimtableInfo) {
		this.dimtableInfo = dimtableInfo;
	}

	public MdaSysTable getMdaSysTable() {
		return mdaSysTable;
	}

	public void setMdaSysTable(MdaSysTable mdaSysTable) {
		this.mdaSysTable = mdaSysTable;
	}

	public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }



    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnCnName() {
        return columnCnName;
    }

    public void setColumnCnName(String columnCnName) {
        this.columnCnName = columnCnName;
    }

    public Integer getColumnDataTypeId() {
        return columnDataTypeId;
    }

    public void setColumnDataTypeId(Integer columnDataTypeId) {
        this.columnDataTypeId = columnDataTypeId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getColumnStatus() {
        return columnStatus;
    }

    public void setColumnStatus(Integer columnStatus) {
        this.columnStatus = columnStatus;
    }

}
