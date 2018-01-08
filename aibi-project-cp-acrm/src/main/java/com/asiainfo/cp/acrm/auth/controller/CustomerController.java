package com.asiainfo.cp.acrm.auth.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageResponseModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalResponseModel;
import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewResponseModel;
import com.asiainfo.cp.acrm.auth.service.ICustomerService;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.WebResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "02 客户视图接口",description="360客户视图接口,包括1:1及1:N")
@RequestMapping("api/customer")
@RestController
public class CustomerController extends BaseController<User> {

	@Autowired
	private ICustomerService customerSvc;

	/**
	 * 360客户视图接口 1:1
	 * @param reqModel
	 * @return
	 */
	@ApiOperation(value="360客户视图接口 1:1")
	@RequestMapping(value = "/portrayal", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public WebResult<PortrayalResponseModel> portrayal(@ApiParam(name = "", value = "", required = true) @RequestBody PortrayalRequestModel reqModel) {
		WebResult<PortrayalResponseModel> webResult = new WebResult<PortrayalResponseModel>();
		List<LabelModel> labels=null;
		try{
			labels =customerSvc.getLabelModels(reqModel);
		} catch (BaseException e) {
			return webResult.fail(e);
		}
		
		PortrayalResponseModel repData=new PortrayalResponseModel();
		repData.setAmount(""+labels.size());
		repData.setDataList(labels);
		return webResult.success("获取用户画像资源成功", repData);
	}

	/**
	 * 360客户视图接口 1:N
	 * @param viewReqModel
	 * @return
	 */
	@ApiOperation(value="360客户视图接口 1:N")
	@RequestMapping(value = "/view", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public WebResult<ViewResponseModel> view(
			@ApiParam(name = "", value = "", required = true) @RequestBody ViewRequestModel viewReqModel) {
		WebResult<ViewResponseModel> webResult = new WebResult<ViewResponseModel>();
		Page<Object> page=null;
		try{
			page=customerSvc.view(viewReqModel);
		} catch (BaseException e) {
			return webResult.fail(e);
		}
		
		ViewResponseModel respData=new ViewResponseModel();
		
		if (page!=null && page.getData()!=null&& page.getData().size()!=0){
			respData.setAmount(""+page.getData().size());
			respData.setDataList(page.getData());
		}else{
			respData.setAmount("0");
			respData.setDataList(new ArrayList());
		}
		if (viewReqModel.getPageInfo()!=null){
			PageResponseModel respModel=new PageResponseModel();
			respData.setPageInfo(respModel);
			respModel.setCurrentPage(""+viewReqModel.getPageInfo().getCurrentPage());
			respModel.setPageSize(""+page.getPageSize());
			respModel.setTotalCount(""+page.getTotalCount());
		}
		return  webResult.success("分页查询成功", respData);
	}

}
