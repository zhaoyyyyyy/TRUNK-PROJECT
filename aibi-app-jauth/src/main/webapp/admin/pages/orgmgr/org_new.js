window.jauth_onload = function() {
	var parentOrgCode = $.getUrlParam("parentOrgCode");
	var orgCode = $.getUrlParam("orgCode");
	if(parentOrgCode!=null&&parentOrgCode!=""&&parentOrgCode!=undefined){
		$.commAjax({
			url:$.ctx+'/api/organization/get',
			postData:{"orgCode":parentOrgCode},
			type:'post',
			cache:false,
			async:false,
			onSuccess:function(data){
				new Vue({ 
					el:'#saveDataForm', 
					data: {
						"parentName":data.organization.simpleName,
						"parentId":data.organization.id,
						"orgCode":null,
						"simpleName":null,
						"orgStatus":null,
						"orgType":null,
						"orderNum":null
					} 
				});
			}
		});
	}else if(orgCode!=null&&orgCode!=""&&orgCode!=undefined){
		$.commAjax({
			url:$.ctx+'/api/organization/get',
			postData:{"orgCode":orgCode},
			type:'post',
			cache:false,
			async:false,
			onSuccess:function(data){
				new Vue({ 
					el:'#saveDataForm', 
					data: {
						"parentName":data.organization.simpleName,
						"parentId":data.organization.parentId,
						"orgCode":data.organization.orgCode,
						"simpleName":data.organization.simpleName,
						"orgStatus":data.organization.orgStatus,
						"orgType":data.organization.orgType,
						"orderNum":data.organization.orderNum
					} 
				});
			}
		});
	}
	
	//保存按钮
	$("#btn_save").click(function(){
		if ($('#saveDataForm').validateForm()){
			$.commAjax({
				url : $.ctx+'/api/organization/add?poc='+parentOrgCode,
				postData:$('#saveDataForm').formToJson(),
				onSuccess:function(data){
					if(data == "success"){
						$.success('保存成功。',function(){
							parent.window.location.reload();
						});
					}else if(data == "orgCodeExist"){
						$.err('组织编码已存在，请重新输入！',function(){
							$("orgCode").focus();
						});
					}
				}
			});
		}
  	});
  	$("#btn_rtn").click(function(){
  		var code = "";
  		if(parentOrgCode!=null&&parentOrgCode!=""&&parentOrgCode!=undefined){
  			code = parentOrgCode;
  		}else if(orgCode!=null&&orgCode!=""&&orgCode!=undefined){
  			code = orgCode;
  		}
  		location.href = $.ctx+'/admin/pages/orgmgr/org_info.html?orgCode=' + code;
  	});
};