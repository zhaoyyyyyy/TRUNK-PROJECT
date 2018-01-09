/**
 * 
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.coyote.http11.filters.VoidInputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.asiainfo.biapp.si.coc.jauth.frame.controller.BaseController;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Group;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Organization;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.User;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.OrganizationService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.utils.SessionInfoHolder;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.OrganizationVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @describe 组织机构控制层
 * @author zhougz
 * @date 2013-6-19
 */
@Api(value = "30.05-组织管理", description = "组织相关操作")
@RequestMapping("api/organization")
@RestController
public class OrganizationController extends BaseController<Organization> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SessionInfoHolder sessionInfoHolder;

    @Override
    protected BaseService<Organization, String> getBaseService() {
        return organizationService;
    }

    /**
     * @describe 输出树
     * @author zhougz
     * @param
     * @date 2013-6-19
     */
    @ApiOperation(value = "构建组织树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "treeType", value = "树类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isAsynchron", value = "树类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sec", value = "树类型", required = false, paramType = "query", dataType = "string") })
    @RequestMapping(value = "/renderOrgTree", method = RequestMethod.POST, produces = { MediaType.ALL_VALUE })
    public String renderOrgTree(String orgCode, String treeType, String isAsynchron, String sec) {
        // 如果是管理员有所有
        User user = sessionInfoHolder.getLoginUser();
        String root = this.getRequest().getParameter("rootId");
        List<String> list = new ArrayList<>();
        if (user.getIsAdmin() == 1) {
            List<Organization> orgList = organizationService.findOrgList();
            for (Organization organization : orgList) {
                list.add(organization.getId());
            }
        } else {
            Set<Organization> orgSet = getOrgSet(user);
            for (Organization organization : orgSet) {
                list.add(organization.getId());
            }
        }
        if ("true".equals(sec)) {
            StringBuffer sb = new StringBuffer();
            Organization organization = organizationService.getOrgByOrgCode(orgCode);
            return this.getSubTree(organization.getChildren(), "true", treeType, sb,
                "true".equals(isAsynchron) ? true : false, list);
        }
        Organization organization = null;
        if (root!=null&& root!="") {
            organization = organizationService.get(root);
        }else {
            organization = organizationService.getOrgByOrgCode(orgCode);
        }
        StringBuffer sb = new StringBuffer();
        return this.getTree(organization, "true", treeType, sb, "true".equals(isAsynchron) ? true : false, list);
    }

    /**
     * 
     * Description:获取用户拥有组织集合
     *
     * @param user
     * @return
     */
    public Set<Organization> getOrgSet(User user) {
        Set<Organization> organizations = new HashSet<>();
        for (Group group : user.getGroupSet()) {
            Set<Organization> organizationSet = group.getOrganizationSet();
            for (Organization organization : organizationSet) {
                organizations = getOrg(organization, organizations);
            }
        }
        return organizations;
    }

    public Set<Organization> getOrg(Organization organization, Set<Organization> organizations) {
        if (!organizations.contains(organization)) {
            if (organization != null) {
                organizations.add(organization);
            }
            if (organization.getParentId() != null && organization.getParentId() != "") {
                Organization org = organizationService.get(organization.getParentId());
                getOrg(org, organizations);
            }
        }
        return organizations;
    }

    /**
     * 组织详情页面
     * 
     * @author ljs
     * @return
     */
    @ApiOperation(value = "得到组织信息")
    @ApiImplicitParam(name = "orgCode", value = "组织信息主键", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> detail(String orgCode) {
        Map<String, Object> map = new HashMap<>();
        Organization organization = organizationService.getOrgByOrgCode(orgCode);
        map.put("organization", organization);
        return map;
    }

    @ApiOperation(value = "得到父组织信息")
    @ApiImplicitParam(name = "orgCode", value = "组织信息主键", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/parentOrg/get", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> queryParent(String orgCode) {
        Map<String, Object> map = new HashMap<>();
        Organization organization = organizationService.getOrgByOrgCode(orgCode);
        Organization parent;
        if (organization.getParentId() != null && organization.getParentId() != "") {
            parent = organizationService.get(organization.getParentId());
        } else {
            parent = null;
        }
        map.put("parent", parent);
        return map;
    }

    /**
     * @describe 取得叶子节点
     * @author zhougz
     * @param
     * @date 2013-6-19
     */
    private String getTree(Organization common, String selectable, String treeType, StringBuffer htmlCon,
            boolean isAsynchron, List<String> list) {
        if (!list.isEmpty() && list.contains(common.getId())) {
            String orgType = common.getOrgType();
            String status = common.getInterrogateType();
            htmlCon.append("<li id='").append(common.getOrgCode()).append("' name='").append(common.getId())
                .append("' selectable='").append(selectable);
            if (orgType != null) {
                htmlCon.append("' orgType='").append(orgType);
            }
            if (status != null) {
                htmlCon.append("' status='").append(status);
            }
            htmlCon.append("'>");
            htmlCon.append("<span class='text'>");
            htmlCon.append(common.getSimpleName());
            htmlCon.append("</span>");
            if (isAsynchron) {
                htmlCon.append("</span><ul  class='ajax'>");
                htmlCon.append("<li>{url: " + this.getRequest().getContextPath()
                        + "/api/organization/renderOrgTree?sec=true&orgCode=" + common.getOrgCode() + "&isAsynchron="
                        + isAsynchron + "}</li>");
            } else {
                htmlCon.append("<ul>");
                getSubTree(common.getChildren(), selectable, treeType, htmlCon, isAsynchron, list);
            }
            htmlCon.append("</ul></li>");
        }

        return htmlCon.toString();
    }

    private String getLeaf(Organization common, StringBuffer htmlCon, List<String> list) {
        if (list.contains(common.getId())) {
            String orgType = common.getOrgType();
            String status = common.getInterrogateType();
            htmlCon.append("<li id='").append(common.getOrgCode()).append("' name='").append(common.getId())
                .append("' selectable='true'");
            if (orgType != null) {
                htmlCon.append(" orgType='").append(orgType).append("'");
            }
            if (status != null) {
                htmlCon.append(" status='").append(status).append("'");
            }
            htmlCon.append(">");
            htmlCon.append("<span class='text'>");
            htmlCon.append(common.getSimpleName());
            htmlCon.append("</span>");
            htmlCon.append("</li>");
        }
        return htmlCon.toString();
    }

    /**
     * @describe 取得叶子节点
     * @author zhougz
     * @param
     * @date 2013-6-19
     */
    private String getSubTree(Set<Organization> set, String selectable, String treeType, StringBuffer htmlCon,
            boolean isAsynchron, List<String> list) {
        for (Organization common : set) {
            if (StringUtil.isNotEmpty(treeType) && treeType.indexOf(common.getOrgType()) == -1) {
                continue;
            }
            if (!common.getChildren().isEmpty()) {
                getTree(common, selectable, treeType, htmlCon, isAsynchron, list);
            } else {
                getLeaf(common, htmlCon, list);
            }
        }
        return htmlCon.toString();
    }

    /**
     * 创建组织
     * 
     * @describe createOrg
     * @author ljs
     * @date 2013-8-1
     */
    @ApiOperation(value = "新增组织")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgCode", value = "组织CODE", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "simpleName", value = "组织名称缩写", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderNum", value = "排序序号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orgType", value = "组织类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "interrogateType", value = "审核方式", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orgStatus", value = "组织状态", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "parentOrgCode", value = "父组织ID", required = false, paramType = "query", dataType = "string") })
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public String createOrg(OrganizationVo organization) {
        organization.setParentId(organizationService.getOrgByOrgCode(request.getParameter("poc")).getId());
        Organization organizations = organizationService.getOrgByOrgCode(organization.getOrgCode());
        if (organizations != null) {// 如果orgCode存在返回orgCode已存在
            return "orgCodeExist";
        } else {// orgCode不存在执行新建然后返回success
            organizationService.createAllTypeOrgByVo(organization);
            return "success";
        }
    }
    
    @ApiOperation(value = "修改组织")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组织ID", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orgCode", value = "组织CODE", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "simpleName", value = "组织名称缩写", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderNum", value = "排序序号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orgType", value = "组织类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "interrogateType", value = "审核方式", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orgStatus", value = "组织状态", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "parentOrgCode", value = "父组织ID", required = false, paramType = "query", dataType = "string") })
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public String updateOrg(Organization organization) {
        Organization organizations = organizationService.getOrgByOrgCode(organization.getOrgCode());
        if (organizations != null) {// 如果orgCode存在返回orgCode已存在
            return "orgCodeExist";
        }else{// orgCode不存在执行修改然后返回success
            Organization old = organizationService.get(organization.getId());
            if(StringUtil.isNotBlank(organization.getOrgCode())){
                old.setOrgCode(organization.getOrgCode());
            }
            if(StringUtil.isNotBlank(organization.getSimpleName())){
                old.setSimpleName(organization.getSimpleName());
            }
            if(null!=organization.getOrderNum()){
                old.setOrderNum(organization.getOrderNum());
            }
            if(StringUtil.isNotBlank(organization.getOrgType())){
                old.setOrgType(organization.getOrgType());
            }
            if(StringUtil.isNotBlank(organization.getInterrogateType())){
                old.setInterrogateType(organization.getInterrogateType());
            }
            if(StringUtil.isNotBlank(organization.getOrgStatus())){
                old.setOrgStatus(organization.getOrgStatus());
            }
            organizationService.saveOrUpdate(old);
            return "success";
        }
    }

    /**
     * 删除组织
     * 
     * @describe deleteOrg
     * @author ljs
     * @date 2013-7-30
     */
    @ApiOperation(value = "删除组织")
    @ApiImplicitParam(name = "orgCode", value = "组织信息编码", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteOrg(String orgCode) {
        Organization organization = organizationService.getOrgByOrgCode(orgCode);
        if (StringUtil.isBlank(organization.getId())) {
            return "fail--NoExist";
        }
        organizationService.delete(organization.getId());
        return "success";
    }

}
