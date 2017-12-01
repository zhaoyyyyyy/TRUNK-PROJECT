package com.asiainfo.cp.acrm.label.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.SqlRunException;
import com.asiainfo.cp.acrm.label.dao.IDimtableInfoDao;
import com.asiainfo.cp.acrm.label.dao.ILabelInfoDao;
import com.asiainfo.cp.acrm.label.dao.IMdaSysTableColumnDao;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;
import com.asiainfo.cp.acrm.label.entity.LabelInfo;
import com.asiainfo.cp.acrm.label.entity.MdaSysTable;
import com.asiainfo.cp.acrm.label.entity.MdaSysTableColumn;
import com.asiainfo.cp.acrm.label.service.ILabelMetaInfoService;
import com.asiainfo.cp.acrm.label.vo.LabelInfoVo;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;
import com.asiainfo.cp.acrm.label.vo.MdaSysTableColumnVo;

@Service
public class LabelMetaInfoServiceImpl implements ILabelMetaInfoService {
	
	private static Logger logger = LoggerFactory.getLogger(LabelMetaInfoServiceImpl.class);

	@Autowired
	private ILabelInfoDao labelDao;

	@Autowired
	private IDimtableInfoDao dimDao;

	@Autowired
	private IMdaSysTableColumnDao columnDao;

	@Override
	public LabelMetaDataInfo getHorizentalLabelMetaInfo(String labelId) throws BaseException {
		LabelInfo labelInfo = getLabelInfo(labelId);
		List<MdaSysTableColumn> columns = labelInfo.getMdaSysTableColumns();
		if (columns == null || columns.size() == 0) {
			throw new SqlRunException("标签" + labelInfo.getLabelId() + "没有对应的表列");
		}
		LabelMetaDataInfo result = getLabelMetaInfo(labelInfo, columns).get(0);
		return result;
	}

	private LabelInfo getLabelInfo(String labelId) throws SqlRunException {
		LabelInfoVo labelInfoVo = new LabelInfoVo();
		labelInfoVo.setLabelId(labelId);
		List<LabelInfo> labelInfos = null;
		try {
			labelInfos = labelDao.findLabelInfoList(labelInfoVo);
		} catch (JpaSystemException e) {
			String errorMsg="获取标签数据错误"+e.getMessage();
			logger.error(errorMsg,e);
			throw new RuntimeException(errorMsg,e);
		}
		if (labelInfos == null || labelInfos.size() < 1) {
			throw new SqlRunException("标签未找到,labelId=" + labelId);
		}
		LabelInfo labelInfo = labelInfos.get(0);
		return labelInfo;
	}

	private List<LabelMetaDataInfo> getLabelMetaInfo(LabelInfo labelInfo, List<MdaSysTableColumn> columns)
			throws SqlRunException {
		if (columns == null || columns.size() < 1) {
			throw new SqlRunException("标签对应相关表中未找到,labelId=" + labelInfo.getLabelId());
		}
		List<LabelMetaDataInfo> result = new ArrayList<LabelMetaDataInfo>();
		for (int i = 0; i < columns.size(); i++) {
			MdaSysTableColumn column = columns.get(i);
			MdaSysTable table = column.getMdaSysTable();
			if (table == null) {
				throw new SqlRunException("标签对应表未找到,labelId=:" + labelInfo.getLabelId());
			}
			LabelMetaDataInfo metaDataInfo = new LabelMetaDataInfo();
			metaDataInfo.setTableShortName("t");
			metaDataInfo.setColumnName(column.getColumnName());
			metaDataInfo.setLabelId(labelInfo.getLabelId());
			metaDataInfo.setTableName(table.getTableName());
			metaDataInfo.setLabelName(labelInfo.getLabelName());
			if (column.getDimTransId() != null) {
				DimtableInfo dimInfo = dimDao.getDimtableInfo(column.getDimTransId());
				if (dimInfo == null) {
					dimInfo = dimDao.getDimtableInfoReload(column.getDimTransId());
					if (dimInfo == null) {
						throw new SqlRunException(
								"标签对应表列" + column.getColumnName() + "的维表" + column.getDimTransId() + "未找到");
					}
				}
				metaDataInfo.setDimtableName(dimInfo.getDimTablename());
				metaDataInfo.setDimCodeCol(dimInfo.getDimCodeCol());
				metaDataInfo.setDimValueCol(dimInfo.getDimValueCol());
				metaDataInfo.setDimtableShortName("tt" + i);
			}
			result.add(metaDataInfo);
		}
		return result;
	}

	@Override
	public List<LabelMetaDataInfo> getVerticalLabelMetaInfo(String labelId) throws BaseException {
		LabelInfo labelInfo = getLabelInfo(labelId);
		List<MdaSysTableColumn> columns = labelInfo.getVertialColumns();
		return getLabelMetaInfo(labelInfo, columns);
	}

	private String getTableSQL(List<LabelMetaDataInfo> lableMetaDataInfos, String userId, String filter) {
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer fromSQL = new StringBuffer();
		StringBuffer whereSQL = new StringBuffer();
		selectSQL.append("select ");
		fromSQL.append(" from ");
		whereSQL.append(" where 1=1");
		for (int i = 0; i < lableMetaDataInfos.size(); i++) {
			LabelMetaDataInfo each = lableMetaDataInfos.get(i);
			if (i == 0) {
				fromSQL.append(each.getTableName()).append(" ").append(each.getTableShortName());
				whereSQL.append(" and cust_Id='").append(userId).append("'");
				if (filter != null && filter.trim().length() != 0) {
					whereSQL.append(" and ").append(filter);
				}
			}
			if (each.getDimtableName() == null) {
				selectSQL.append(" ").append(each.getTableShortName()).append(".").append(each.getColumnName());
			} else {
				selectSQL.append(" ").append(each.getDimtableShortName()).append(".").append(each.getDimValueCol())
						.append(" cc").append(i);
				fromSQL.append(" left join ").append(each.getDimtableName()).append(" ")
						.append(each.getDimtableShortName()).append(" on ").append(each.getTableShortName()).append(".")
						.append(each.getColumnName()).append("=").append(each.getDimtableShortName()).append(".")
						.append(each.getDimCodeCol());
			}
			if (i != lableMetaDataInfos.size() - 1) {
				selectSQL.append(",");
			}
		}

		return selectSQL.toString() + fromSQL + whereSQL;
	}

	@Override
	public String getTableSQL(PortrayalRequestModel reqModel, LabelMetaDataInfo lableMetaDataInfo) {
		List<LabelMetaDataInfo> lableMetaDataInfos = new ArrayList<LabelMetaDataInfo>();
		lableMetaDataInfos.add(lableMetaDataInfo);
		String userId = reqModel.getCustomerId();
		return this.getTableSQL(lableMetaDataInfos, userId, null);
	}

	@Override
	public String getTableSQL(ViewRequestModel reqModel, List<LabelMetaDataInfo> lableMetaDataInfos) {
		String userId = reqModel.getCustomerId();
		return this.getTableSQL(lableMetaDataInfos, userId, reqModel.getFilter());
	}
}
