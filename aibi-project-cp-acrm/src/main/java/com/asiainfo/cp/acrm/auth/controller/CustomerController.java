package com.asiainfo.cp.acrm.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.cp.acrm.auth.model.LableModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.auth.model.WithdrawRecordTestModel;
import com.asiainfo.cp.acrm.auth.service.IUserService;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import com.asiainfo.cp.acrm.base.utils.WebResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api(value = "客户画像相关接口")
@RequestMapping("api/customer")
@RestController
public class CustomerController extends BaseController<User> {

	@Autowired
	private IUserService userService;

	/**
	 * 用户画像接口
	 * @param reqModel
	 * @return
	 */
	@RequestMapping(value = "/portrayal", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public WebResult<List<LableModel>> portrayal(@ApiParam(name = "", value = "", required = true) @RequestBody PortrayalRequestModel reqModel) {
		WebResult<List<LableModel>> webResult = new WebResult<List<LableModel>>();
		//JSONObject jsonObject = new JSONObject(lableId);
		List<LableModel> labels = getTestLabels();
		return webResult.success("获取用户画像资源成功", labels);
	}

	private List<LableModel> getTestLabels() {
		List<LableModel> labels =new ArrayList<LableModel>();
		LableModel lable1=new LableModel("L_1211","用户名称","习大大");
		LableModel lable2=new LableModel("L_1212","用户年龄","64");
		labels.add(lable1);
		labels.add(lable2);
		return labels;
	}
	
	/**
	 * 360客户视图接口
	 * @param viewReqModel
	 * @return
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public WebResult<List> view(
			@ApiParam(name = "", value = "", required = true) @RequestBody ViewRequestModel viewReqModel) {
		WebResult<List> webResult = new WebResult<List>();
		//JSONObject jsonObject = new JSONObject(lableId);
		String labelId=viewReqModel.getLabelId();
		if ("L_1213".equals(labelId)){
			List list = getViewForlabelL_1213();
			return webResult.success("分页查询成功", list);
		}else if ("C_1001".equals(labelId)){
			List list = getViewForLabelC_1001();
			return webResult.success("分页查询成功", list);
			
		}else{
			return webResult.success("当前测试阶段只labelId只支持'L_1213'和'C_1001'",null );
		}
	}

	private List getViewForLabelC_1001() {
		List list=new ArrayList();
		list.add(new WithdrawRecordTestModel("公主坟支行","20171112100023","30000.0",11001));
		list.add(new WithdrawRecordTestModel("德阳门支行","20171115100023","20000.0",11001));
		return list;
	}

	private List getViewForlabelL_1213() {
		List list=new ArrayList();
		Map map1=new HashMap();
		map1.put("l_001", "北京市西长安街174号中南海");
		list.add(map1);
		Map map2=new HashMap();
		map2.put("l_001", "北京市海淀区香山南路99号");
		list.add(map2);
		return list;
	}
	
}
