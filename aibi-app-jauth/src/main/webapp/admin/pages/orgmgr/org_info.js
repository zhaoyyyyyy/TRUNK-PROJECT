window.jauth_onload = function() {
	var orgCode = $.getUrlParam("orgCode");
	$.commAjax({
		url:$.ctx+'/api/organization/get',
		postData:{"orgCode":orgCode},
		type:'post',
		cache:false,
		async:false,
		onSuccess:function(data){
			if(data.organization.parentId != null && data.organization.parentId != ""){
			$.commAjax({
				url:$.ctx+'/api/organization/parentOrg/get',
				postData:{"orgCode":orgCode},
				type:'post',
				cache:false,
				async:false,
				onSuccess:function(data1){
					data.organization.createTime = (new Date(data.organization.createTime*1000)).toString();
					data.organization.parentName = data1.parent.simpleName;
					data.organization.orgType = $.getCodeDesc('ZZLXZD',data.organization.orgType);
					data.organization.orgStatus = $.getCodeDesc('ZZZTZD',data.organization.orgStatus);
					new Vue({ el:'#saveDataForm', data: data });
				}
			});
		}else{
			data.organization.createTime = (new Date(data.organization.createTime*1000)).toString();
			data.organization.orgType = $.getCodeDesc('ZZLXZD',data.organization.orgType);
			data.organization.orgStatus = $.getCodeDesc('ZZZTZD',data.organization.orgStatus);
			new Vue({ el:'#saveDataForm', data: data });
		}
		}
	});

	
  	$("#btn_creatChild").click(function(){
		location.href = $.ctx+'/admin/pages/orgmgr/org_new.html?parentOrgCode=' + $("#orgCode").val();
  	});
  	
  	$("#btn_updateThis").click(function(){
  		location.href = $.ctx+'/admin/pages/orgmgr/org_new.html?orgCode=' + $("#orgCode").val();
  	});
};