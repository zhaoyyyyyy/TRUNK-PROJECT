window.jauth_onload = function() {
	var parentOrgCode = $.getUrlParam("parentOrgCode");
	var orgCode = $.getUrlParam("orgCode");
	var level = [];
	level[1]=1;level[2]=2;level[3]=3;level[4]=4;level[5]=5;
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
						"id":null,
						"parentName":data.simpleName,
						"parentId":data.id,
						"orgCode":null,
						"simpleName":null,
						"orgStatus":null,
						"orgType":null,
						"orderNum":null,
						"levelId":1
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
						"id":data.id,
						"parentName":data.simpleName,
						"parentId":data.parentId,
						"orgCode":data.orgCode,
						"simpleName":data.simpleName,
						"orgStatus":data.orgStatus,
						"orgType":$.getCodeDesc('ZZLXZD',data.orgType),
						"orderNum":data.orderNum,
						"levelId":level[data.levelId]
					} 
				});
			}
		});
	}
	
	//保存按钮
	$("#btn_save").click(function(){
		if ($('#saveDataForm').validateForm()){
			var url_ = "";
			if(parentOrgCode!=null&&parentOrgCode!=""&&parentOrgCode!=undefined){
				$("#orgid").removeAttr("name");
				url_ = $.ctx+'/api/organization/add?poc='+parentOrgCode;
			}else if(orgCode!=null&&orgCode!=""&&orgCode!=undefined){
				url_ = $.ctx+'/api/organization/update';
			}
			$.commAjax({
				url : url_,
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