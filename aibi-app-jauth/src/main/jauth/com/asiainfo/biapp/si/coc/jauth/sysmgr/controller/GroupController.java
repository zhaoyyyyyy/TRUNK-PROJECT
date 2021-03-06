/**
 * 
 */
package com.asiainfo.biapp.si.coc.jauth.sysmgr.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.biapp.si.coc.jauth.frame.controller.BaseController;
import com.asiainfo.biapp.si.coc.jauth.frame.json.JSONResult;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Group;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Organization;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.User;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.GroupService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.OrganizationService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.utils.SessionInfoHolder;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.GroupVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @describe 数据范围
 * @author liukai
 * @date 2013-6-27
 */
@Api(value = "30.04-数据权限管理",description="数据权限相关操作")
@RequestMapping("api/group")
@RestController
public class GroupController extends BaseController<Group> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private GroupService groupService;
	@Autowired
	private SessionInfoHolder sessionInfoHolder;
	@Autowired
	private OrganizationService organizationService;



	/**
	 * 
	 * @describe 查询数据范围
	 * @author liukai
	 * @param
	 * 
	 * @date 2013-6-27
	 */
	@ApiOperation(value="显示数据范围的列表（分页形式）")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cols", value = "列名称", required = true, paramType = "query" ,dataType = "string",defaultValue="groupName,createUserId,createTime,groupDesc,id"),
		@ApiImplicitParam(name = "groupName", value = "数据权限名称", required = false, paramType = "query" ,dataType = "string"),
		@ApiImplicitParam(name = "createTimeStart", value = "创建时间（开始）", required = false, paramType = "query" ,dataType = "string"),
		@ApiImplicitParam(name = "createTimeEnd", value = "创建时间（结束）", required = false, paramType = "query" ,dataType = "string")
	})
	@RequestMapping(value="/groupPage/query", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
	public String list(@ModelAttribute JQGridPage<Group> page,@ModelAttribute GroupVo groupVo,String cols) {
		
		//zn
		User user = sessionInfoHolder.getLoginUser();
		if(user.getIsAdmin()!=1){
		    groupVo.setOrginfoId(user.getOrginfoId());
		    groupVo.setCreateUserId(user.getUserName());
		}
		JQGridPage<Group> groupList = groupService.findGroupList(page,groupVo);
		for(int i=0 ; i<groupList.getData().size() ; i++){
		    groupList.getData().get(i).getOrganizationSet();
		    int k = 0;
		    for(Organization g : groupList.getData().get(i).getOrganizationSet()){
		        if(g.getOrgCode().equals("999")||g.getOrgCode().equals("1")||g.getOrgCode().equals("2")){
		            continue;
		        }
		        String name = g.getFullName();
		        if(g.getFullName()==null){
		            name = g.getSimpleName();
		        }
		        if(k==0){
                    groupList.getData().get(i).setOrganizationStr(name);
                    k++;
		        }else{
		            groupList.getData().get(i).setOrganizationStr(groupList.getData().get(i).getOrganizationStr()+"/"+name);
		        }
		    }
		}
        return JSONResult.page2Json(groupList, cols);
		//zn
	}

	/**
	 * 
	 * @describe 删除数据范围
	 * @author liukai
	 * @param
	 * @date 2013-6-27
	 */
	@ApiOperation(value="删除")
	@ApiImplicitParam(name = "id", value = "数据范围主键", required = true, paramType = "query" ,dataType = "string")
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public void deleteGroup(String id) {
		groupService.delete(id);
	}

	/**
	 * 
	 * @describe 进入变更及新建数据范围页面
	 * @author liukai
	 * @param
	 * @date 2013-6-28
	 */
	@ApiOperation(value="查询数据范围")
	@ApiImplicitParam(name = "id", value = "数据范围主键", required = true, paramType = "query" ,dataType = "string")
	@RequestMapping(value="/query", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
	public Map<String,Object> initEditGroup(String id) {
		String tree = null;
		Group group = groupService.get(id);
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		for (Organization g : group.getOrganizationSet()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(g.getOrgCode());
		}
		tree = sb.toString();
		map.put("group",group);
		map.put("tree", tree);
		return map;
	}
	
	/**
	 * 
	 * @describe 编辑数据
	 * @author liukai
	 * @param
	 * @date 2013-6-28
	 */
	@ApiOperation(value="保存数据范围")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据范围主键", required = false, paramType = "query" ,dataType = "string"),
		@ApiImplicitParam(name = "groupName", value = "数据范围名称", required = false, paramType = "query" ,dataType = "string"),
		@ApiImplicitParam(name = "tree", value = "组织ID", required = false, paramType = "query" ,dataType = "string"),
		@ApiImplicitParam(name = "groupDesc", value = "数据范围描述", required = false, paramType = "query" ,dataType = "string"),
	})
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(String groupName,String id,String tree,String groupDesc) {
			User user = sessionInfoHolder.getLoginUser();
			List<Group> groupList = null;
			Group groupE = groupService.get(id);
			if(StringUtil.isBlank(id)){
				groupList = groupService.findGroupByName(groupName, id);
			}else if(!groupName.equals(groupE.getGroupName())){
			    groupList = groupService.findGroupByName(groupName, null);
			}
			// 数据范围重复
			if (groupList != null && groupList.size() > 0) {
				return "haveSameName";
			} else {
				Set<Organization> organizationSet = new HashSet<Organization>();
				// 前台有选中节点
				if (StringUtils.isNotBlank(tree)) {
					// 获取所选节点
					String[] trees = tree.split(",");
					for (String t : trees) {
						// 根据代码查找组织
						Organization o = groupService.findOrganizationByCode(t);
//						if(!organizationSet.contains(o)){
//							organizationSet.add(o);
//						}
						getOrgset(organizationSet,o);
						
					}
				}
				Group group = new Group();
				if (StringUtils.isNotBlank(id)) {
					group = groupService.get(id);
				} 
				group.setGroupName(groupName);
				group.setGroupDesc(groupDesc);
				group.setOrganizationSet(organizationSet);
				group.setAppSysCode("1");
				group.setOrginfoId(user.getOrginfoId());
				group.setCreateUserId(user.getUserName());
				group.setCreateOrgId(user.getOrginfoId());
				groupService.saveOrUpdate(group);
				return "success";
			}

	}
	
	public Set<Organization> getOrgset(Set<Organization> organizationSet,Organization organization){
        if (!organizationSet.contains(organization)) {
            organizationSet.add(organization);
            if (organization.getParentId() != null && organization.getParentId() != "") {
                Organization org = organizationService.get(organization.getParentId());
                getOrgset(organizationSet, org);
            }
        }
        return organizationSet; 
    }


	@Override
	protected BaseService<Group, String> getBaseService() {
		return groupService;
	}
}
