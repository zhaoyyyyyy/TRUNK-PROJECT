package com.asiainfo.cp.acrm.label.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.SqlRunException;
import com.asiainfo.cp.acrm.base.utils.LogUtil;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.dao.IDimtableInfoDao;
import com.asiainfo.cp.acrm.label.dao.ILabelInfoDao;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;
import com.asiainfo.cp.acrm.label.entity.LabelInfo;
import com.asiainfo.cp.acrm.label.entity.MdaSysTable;
import com.asiainfo.cp.acrm.label.entity.MdaSysTableColumn;
import com.asiainfo.cp.acrm.label.service.ILabelMetaInfoService;
import com.asiainfo.cp.acrm.label.vo.LabelInfoVo;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
public class LabelMetaInfoServiceImpl implements ILabelMetaInfoService {
	
	@Value("${cache.isDimtableCached}")
	private Boolean isDimtableCached;
	
	@Autowired
	private ILabelInfoDao labelDao;

	@Autowired
	private IDimtableInfoDao dimDao;
	
	
	private String cust_id;

    @Value("${cust_id}")
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	@Override
	public LabelMetaDataInfo getHorizentalLabelMetaInfo(String labelId) throws BaseException {
		LabelInfo labelInfo = getLabelInfo(labelId);
		if (labelInfo==null) {
			return null;
		}
		List<MdaSysTableColumn> columns = labelInfo.getMdaSysTableColumns();
		if (columns == null || columns.size() == 0) {
			//throw new SqlRunException("标签" + labelInfo.getLabelId() + "没有对应的表列");
			return null;
		}
		LabelMetaDataInfo result = getLabelMetaInfo(labelInfo, columns).get(0);
		return result;
	}
	
	@Override
	public List<LabelMetaDataInfo> getHorizentalLabelMetaInfos(List<String> labelIds) throws BaseException {
		List<LabelInfo> labelInfos = getLabelInfos(labelIds);
		List<LabelMetaDataInfo> result=new ArrayList<LabelMetaDataInfo>();
		for (LabelInfo each:labelInfos) {
			List<MdaSysTableColumn> columns = each.getMdaSysTableColumns();
			if (columns == null || columns.size() == 0) {
				LogUtil.error("标签" + each.getLabelId() + "没有对应的表列");
				continue;
			}
			if (columns.size()>1) {
				LogUtil.error("宽表标签"+each.getLabelId()+"对应" +columns.size()+"列，错误");
			}
			List<LabelMetaDataInfo> eachResult = getLabelMetaInfo(each, columns);
			result.addAll(eachResult);
		}
		return result;
	}
	
	private LabelInfo getLabelInfo(String labelId) throws SqlRunException {
		LabelInfoVo labelInfoVo = new LabelInfoVo();
		labelInfoVo.setLabelId(labelId);
		List<LabelInfo> labelInfos = null;
		try {
			labelInfos = labelDao.findLabelInfoList(labelInfoVo);
		} catch (Exception e) {
			String errorMsg="获取标签数据错误"+e.getMessage();
			LogUtil.error(errorMsg,e);
			throw new SqlRunException(errorMsg);
		}
		if (labelInfos!=null && labelInfos.size()>0) {
			return labelInfos.get(0);
		}
		return null;
	}
	
	private List<LabelInfo> getLabelInfos(List<String> labelIds) throws SqlRunException {
		LabelInfoVo labelInfoVo = new LabelInfoVo();
		labelInfoVo.setLabelIds(labelIds);
		List<LabelInfo> labelInfos = null;
		try {
			labelInfos = labelDao.findLabelInfoList(labelInfoVo);
		} catch (Exception e) {
			String errorMsg="获取标签数据错误"+e.getMessage();
			LogUtil.error(errorMsg,e);
			throw new SqlRunException(errorMsg);
		}
		return labelInfos;
	}

	private List<LabelMetaDataInfo> getLabelMetaInfo(LabelInfo labelInfo, List<MdaSysTableColumn> columns)
			throws SqlRunException {
		final String DIMTABLE_SHORTNAME_DEFAULT = "dimt";
		final String TABLE_SHORTNAME_DEFAULT = "t";
		List<LabelMetaDataInfo> result = new ArrayList<LabelMetaDataInfo>();
		for (int i = 0; i < columns.size(); i++) {
			MdaSysTableColumn column = columns.get(i);
			MdaSysTable table = column.getMdaSysTable();
			if (table == null) {
				LogUtil.error("标签对应表未找到,labelId=:" + labelInfo.getLabelId());
				continue;
			}
			LabelMetaDataInfo metaDataInfo = new LabelMetaDataInfo();
			metaDataInfo.setDimtableId(column.getDimTransId());
			metaDataInfo.setTableShortName(TABLE_SHORTNAME_DEFAULT);
			metaDataInfo.setColumnName(column.getColumnName());
			metaDataInfo.setLabelId(labelInfo.getLabelId());
			metaDataInfo.setTableName(table.getTableName());
			metaDataInfo.setLabelName(labelInfo.getLabelName());
			if (StringUtil.isNotEmpty(column.getDimTransId())) {
				DimtableInfo dimInfo = dimDao.getDimtableInfo(column.getDimTransId());
				if (dimInfo == null) {
					LogUtil.error("标签对应表列" + column.getColumnName() + "的维表" + column.getDimTransId() + "未找到");
					continue;
				}
				metaDataInfo.setDimtableName(dimInfo.getDimTablename());
				metaDataInfo.setDimCodeCol(dimInfo.getDimCodeCol());
				metaDataInfo.setDimValueCol(dimInfo.getDimValueCol());
				metaDataInfo.setDimtableShortName(DIMTABLE_SHORTNAME_DEFAULT + RandomUtils.nextInt());
				metaDataInfo.setDimValueColAliasName(column.getColumnName()+DIM_COLUMNVALUE_ALIAS_NAME_DEFAULT+RandomUtils.nextInt());
			}
			result.add(metaDataInfo);
		}
		return result;
	}

	@Override
	public List<LabelMetaDataInfo> getVerticalLabelMetaInfo(String labelId) throws BaseException {
		LabelInfo labelInfo = getLabelInfo(labelId);
		if (labelInfo==null) {
			return null;
		}
		List<MdaSysTableColumn> columns = labelInfo.getVertialColumns();
		return getLabelMetaInfo(labelInfo, columns);
	}

	private String getTableSQL(List<LabelMetaDataInfo> lableMetaDataInfos, String userId, String filter,String sortCol,String sortOrder) {
		
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer fromSQL = new StringBuffer();
		StringBuffer whereSQL = new StringBuffer();
		StringBuffer orderbySQL = new StringBuffer();
		selectSQL.append("select ");
		fromSQL.append(" from ");
		whereSQL.append(" where 1=1");
		for (int i = 0; i < lableMetaDataInfos.size(); i++) {
			LabelMetaDataInfo each = lableMetaDataInfos.get(i);
			if (i == 0) {
				fromSQL.append(each.getTableName()).append(" ").append(each.getTableShortName());
				whereSQL.append(" and ").append(this.cust_id).append("='").append(userId).append("'");
				if (filter!=null && !StringUtil.isEmpty(filter.trim())) {
					whereSQL.append(" and ").append(filter);
				}
			}
			if (each.getDimtableName() == null||isDimtableCached) {
				selectSQL.append(" ").append(each.getTableShortName()).append(".").append(each.getColumnName());
			} else {
				selectSQL.append(" ").append(each.getDimtableShortName()).append(".").append(each.getDimValueCol())
						.append(" ").append(each.getDimValueColAliasName()).append(i);
				fromSQL.append(" left join ").append(each.getDimtableName()).append(" ")
						.append(each.getDimtableShortName()).append(" on ").append(each.getTableShortName()).append(".")
						.append(each.getColumnName()).append("=").append(each.getDimtableShortName()).append(".")
						.append(each.getDimCodeCol());
			}
			if (i != lableMetaDataInfos.size() - 1) {
				selectSQL.append(",");
			}
		}
		if (sortCol!=null && !StringUtil.isEmpty(sortCol.trim())) {
			orderbySQL.append(" order by ").append(sortCol);
			if (sortOrder!=null && !StringUtil.isEmpty(sortOrder.trim())) {
				orderbySQL.append(" ").append(sortOrder);
			}
		}
		return selectSQL.toString() + fromSQL + whereSQL+orderbySQL;
	}

	@Override
	public String getTableSQL(PortrayalRequestModel reqModel, LabelMetaDataInfo lableMetaDataInfo) {
		List<LabelMetaDataInfo> lableMetaDataInfos = new ArrayList<LabelMetaDataInfo>();
		lableMetaDataInfos.add(lableMetaDataInfo);
		String userId = reqModel.getCustomerId();
		return this.getTableSQL(lableMetaDataInfos, userId, null,null,null);
	}
	
	@Override
	public String getTableSQL(PortrayalRequestModel reqModel,List<LabelMetaDataInfo> lableMetaDataInfos) {
		String userId = reqModel.getCustomerId();
		return this.getTableSQL(lableMetaDataInfos, userId, null,null,null);
	}

	@Override
	public String getTableSQL(ViewRequestModel reqModel, List<LabelMetaDataInfo> lableMetaDataInfos) {
		String userId = reqModel.getCustomerId();
		if (reqModel.getPageInfo()!=null) {
			return this.getTableSQL(lableMetaDataInfos, userId, reqModel.getFilter(),reqModel.getPageInfo().getSortCol(),reqModel.getPageInfo().getSortOrder());
		}else {
			return this.getTableSQL(lableMetaDataInfos, userId, reqModel.getFilter(),null,null);
		}
	}
}
