window.jauth_onload = function() {
	var id = $.getUrlParam("id");
	var parentId = $.getUrlParam("parentId");
	if(id !=null && id != "" && id != undefined){
		$.commAjax({
			url:$.ctx+'/api/resource/get',
			postData:{"id":id},
			type:'post',
			cache:false,
			async:false,
			onSuccess:function(data){
				$.commAjax({
						url:$.ctx+'/api/resource/parentResource/get',
						postData:{"id":id},
						type:'post',
						cache:false,
						async:false,
						onSuccess:function(data1){
							if(data.parentId != null && data.parentId != ""){
								data.parentName = data1.resourceName;
							}
							new Vue({ 
								el:'#saveDataForm', 
								data : {
									resourceId : data.id ,
									parentId : data.parentId ,
									resourceName : data.resourceName ,
									resourceCode : data.resourceCode ,
									address : data.address ,
									parentName : data.parentName,
									dispOrder : data.dispOrder,
									type : data.type
								}
							});
						}
				});
			}
		});
	}else{
		if(parentId != null && parentId != "" && parentId != undefined){
			$.commAjax({
				url:$.ctx+'/api/resource/get',
				postData:{"id":parentId},
				type:'post',
				cache:false,
				async:false,
				onSuccess:function(data){
					new Vue({
						el : '#saveDataForm',
						data : {
							resourceId : null ,
							parentId : parentId ,
							resourceName : null ,
							resourceCode : null ,
							address : null ,
							parentName : data.resourceName ,
							type : data.type ,
							dispOrder : null
						}
					});
				}
			});
		}else{
			new Vue({
				el : '#saveDataForm',
				data : {
					resourceId : null ,
					parentId : null ,
					resourceName : null ,
					resourceCode : null ,
					address : null ,
					parentName : null ,
					type : null ,
					dispOrder : null
				}
			});
		}
	}
	var proscenium = "LOC_MENU"; // 前台
	var app = "JAUTH_API"; //APP
	var backstage = "JAUTH_MENU"; // 后台
	var proelement = "LOC_DOM";  //前台页面元素
	var mySimpleTree;
	var myTree;
	var appTree;
	// 所选节点
	var selectTrees = $("#selectTrees").val();
	var dg = frameElement.lhgDG;
	dg.removeBtn();
	
	var cascadeParentChecked = true;
	var cascadeChildrenChecked = true;
	
	// 前台
	var url1 = $.ctx + '/api/resource/renderOrgTree';
	$.commAjax({
		url : url1,
		postData:{"resourceId":proscenium},
		isShowMask : true,
		type : 'POST',
		async:false,
		onSuccess : function(data) {
			$("#sRoot").append(data);
			mySimpleTree = $('#tree').simpleTree({
				autoclose : false,
				cascadeParentChecked : cascadeParentChecked,
				cascadeChildrenChecked : cascadeChildrenChecked,
				afterClick: function(node){
					$('#parentName').val($('span:first', node).text());
					$('#parentId').val(node.attr('id'));
				},
				ignoreIndeterminate : false
			});

		},
		onFailure : function() {
		},
		maskMassage : '数据加载中...'
	});
	// 后台
	$.commAjax({
		url : url1,
		postData:{"resourceId":backstage},
		isShowMask : true,
		type : 'POST',
		async:false,
		onSuccess : function(data) {
			$("#simpleRoot").append(data);
			myTree = $('#simpleTree').simpleTree({
				autoclose : false,
				cascadeParentChecked : cascadeParentChecked,
				cascadeChildrenChecked : cascadeChildrenChecked,
				afterClick: function(node){
					$('#parentName').val($('span:first', node).text());
					$('#parentId').val(node.attr('id'));
				},
				ignoreIndeterminate : false
			});

		},
		onFailure : function() {
		},
		maskMassage : '数据加载中...'
	});
	// app
	$.commAjax({
		url : url1,
		postData:{"resourceId":app},
		isShowMask : true,
		type : 'POST',
		async:false,
		onSuccess : function(data) {
			$("#simpleRootAPP").append(data);
			appTree = $('#appTree').simpleTree({
				autoclose : true,
				cascadeParentChecked : cascadeParentChecked,
				cascadeChildrenChecked : cascadeChildrenChecked,
				afterClick: function(node){
					$('#parentName').val($('span:first', node).text());
					$('#parentId').val(node.attr('id'));
				},
				ignoreIndeterminate : false
			});
		
		},
		onFailure : function() {
		},
		maskMassage : '数据加载中...'
	});
	//页面
	$.commAjax({
		url : url1,
		postData:{"resourceId":proelement},
		isShowMask : true,
		type : 'POST',
		async:false,
		onSuccess : function(data) {
			$("#simpleEle").append(data);
			eleTree = $('#elementTree').simpleTree({
				autoclose : true,
				cascadeParentChecked : cascadeParentChecked,
				cascadeChildrenChecked : cascadeChildrenChecked,
				afterClick: function(node){
					$('#parentName').val($('span:first', node).text());
					$('#parentId').val(node.attr('id'));
				},
				ignoreIndeterminate : false
			});
		
		},
		onFailure : function() {
		},
		maskMassage : '数据加载中...'
	});


	var dg = frameElement.lhgDG;
	dg.removeBtn();

	dg.addBtn("save", "保存",
					function() {
						if ($('#saveDataForm').validateForm()) {
								$.commAjax({
									url : $.ctx + '/api/resource/save',
									postData :  $("#saveDataForm").formToJson(),
									onSuccess : function(data) {
										if (data == 'success') {
											$.success('保存成功。', function() {
												dg.cancel();
												dg.reload();
											})
										} else if (data == 'haveSameCode') {
											$.alert("已存在的菜单编码");
										}
									}
								});
						}
					});
	dg.addBtn("cancel", "取消", function() {
		dg.cancel();
	});
}
