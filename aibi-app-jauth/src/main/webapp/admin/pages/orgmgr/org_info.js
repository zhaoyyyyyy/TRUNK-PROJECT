window.jauth_onload = function() {
	var orgCode = $.getUrlParam("orgCode");
	$.commAjax({
		url:$.ctx+'/api/organization/get',
		postData:{"orgCode":orgCode},
		type:'post',
		cache:false,
		async:false,
		onSuccess:function(data){
			if(data.parentId != null && data.parentId != ""){
			$.commAjax({
				url:$.ctx+'/api/organization/parentOrg/get',
				postData:{"orgCode":orgCode},
				type:'post',
				cache:false,
				async:false,
				onSuccess:function(data1){
					data.createTime = (new Date(data.createTime*1000)).toString();
					data.parentName = data1.simpleName;
					data.orgType = $.getCodeDesc('ZZLXZD',data.orgType);
					data.orgStatus = $.getCodeDesc('ZZZTZD',data.orgStatus);
					new Vue({ el:'#saveDataForm', data: data });
				}
			});
		}else{
			data.createTime = (new Date(data.createTime*1000)).toString();
			data.orgType = $.getCodeDesc('ZZLXZD',data.orgType);
			data.orgStatus = $.getCodeDesc('ZZZTZD',data.orgStatus);
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
  	
  	$("#btn_deleteThis").click(function(){
  		$.confirm('删除此节点后,节点数据将全部删除,确定要删除吗？', function() {
			$.commAjax({
				url:$.ctx+'/api/organization/get',
				postData:{"orgCode":orgCode},
				type:'post',
				onSuccess:function(data){
					if(data.children.length !=0){
						$.alert("当前节点下含有下级节点，不可删除");
					}else{
						$.commAjax({
							url:$.ctx+'/api/organization/delete',
							postData:{"orgCode":orgCode},
							type:'post',
							onSuccess:function(data){
								$.success('删除成功。',function(){
									parent.window.location.reload();
								});
							}
						});
					}
				}
			});
  		});
  	});
};