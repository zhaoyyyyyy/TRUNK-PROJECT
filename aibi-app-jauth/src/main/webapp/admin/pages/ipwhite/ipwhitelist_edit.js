window.jauth_onload=function() {
	var dg = frameElement.lhgDG;
	dg.removeBtn();
	var id = $.getUrlParam("whiteListId");
	if(id!=null){
		$.commAjax({
			url:$.ctx+'/api/ipWhiteList/whitelistInfo/query',
			postData:{"whiteListId":id},
			type:'post',
			cache:false,
			async:false,
			onSuccess:function(data){
				new Vue({ el:'#saveDataForm', data: data });
			}
		});
	}
	dg.addBtn("save", "保存", function() {
		if ($('#saveDataForm').validateForm()) {
			if($("#ipAddress").val()==""){
				$.alert("ip白名单不能为空");
				return false;
			}
			if($("#requestAddress").val()==""){
				$.alert("请求地址不能为空");
				return false;
			}
			var listId = document.getElementById("listId").value;
			if(listId!=null&&listId!=""){
				var url = $.ctx+'/api/ipWhiteList/whitelist/update';
			}else{
				var url = $.ctx+'/api/ipWhiteList/whitelist/save';
			}
			$.commAjax({
				url : url,
				postData:$('#saveDataForm').formToJson(),
				onSuccess:function(data){
					if (data == 'success') {
						$.success('保存成功。', function() {
							dg.cancel();
							dg.reload();
						})
					} else if (data == 'exist'){
						$.err('IP地址已存在，请重新输入！',function(){
							$("ipAddress").focus();
						});
					}
				}
			})
	}	
		
		
	});
	dg.addBtn("cancel", "取消", function() {
		dg.cancel();
	});	
			     
}

