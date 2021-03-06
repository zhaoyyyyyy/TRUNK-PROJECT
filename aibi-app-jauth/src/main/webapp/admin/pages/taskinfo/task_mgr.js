window.jauth_onload = function() {
	var js = {
		/** 常量 */
		data:{
			/** 状态:1：启动; */
			EXE_STATUS_YES:1,
			/** 状态:2：停止; */
			EXE_STATUS_NO:2
		}
	}
	
	var parentExeId = '';
	$.commAjax({
		url : $.ctx + '/api/schedule/tree',
		isShowMask : false,
		type : 'POST',
		async : false,
		onSuccess : function(data) {
			$("li.rootd").append(data);
			mySimpleTree = $('#tree').simpleTree(
					{
						autoclose : false,
						afterClick : function(thi) {
							parentExeId = $(thi).attr('id'), $("#mainGrid")
									.setGridParam({
										postData : {
											parentExeId : parentExeId
										}
									}).trigger("reloadGrid", [ {
										page : 1
									} ]);
						}
					})
		}
	});

	var urlShow = $.ctx + '/api/schedule/taskExeInfo/query';
	var colNames = [ '名称', '参数', 'QUARZT表达式', '任务状态', '执行类型', '操作' ];
	var colModel = [
			{
				name : 'taskExeName',
				index : 'taskExeName',
				width : 20,
				align : 'left'
			},
			{
				name : 'sysId',
				index : 'sysId',
				width : 15,
				align : 'left',
				formatter : function(value, opts, data) {
					return $.toStr(data.sysId);
				}
			},
			{
				name : 'taskExeTime',
				index : 'taskExeTime',
				width : 120,
				fixed:true,
				align : 'left'
			},
			{
				name : 'exeStatus',
				index : 'exeStatus',
				width : 75,
				fixed:true,
				align : 'center',
				sortable : false,
				formatter : function(value, opts, data) {
					return $.getCodeDesc('RWZT', data.exeStatus);
				}
			},
			{
				name : 'exeType',
				index : 'exeType',
				width : 75,
				fixed:true,
				align : 'center',
				formatter : function(value, opts, data) {
					return $.getCodeDesc('ZXLX', data.exeType);
				}
			},
			{
				name : 'taskExeId',
				index : 'taskExeId',
				width : 300,
				align : 'left',
				fixed : true,
				formatter : function(value, opts, data) {
					var html = '';
					if (data.exeStatus == 0) {
						return "<a onclick='fun_to_update(\"" + data.taskExeId
								+ "\")' class='s_edit'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "<a onclick='fun_to_delete(\""
								+ data.taskExeId
								+ "\")' class='s_delete'>删除</a>";
					}
					if (data.exeStatus == 2) {
						html += "<a onclick='fun_to_update(\"" + data.taskExeId
								+ "\")' class='s_edit'>修改</a>";
					}else{
						html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}

					//启动停止
					if (data.exeStatus == js.data.EXE_STATUS_YES) {
						html += "<a href='javascript:void(0);' class='s_kqzc' onclick='fun_to_status(\""
							+ data.taskExeId + "\",\"" + data.status
							+ "\",\"" + data.taskExeName + "\",\"" + '停止'
							+ "\")' ><font color='green'>停止</font></a>";
					} else if (data.exeStatus == js.data.EXE_STATUS_NO) {
						html += "<a href='###' class='s_release' onclick='fun_to_status(\""
							+ data.taskExeId + "\",\"" + data.status
							+ "\",\"" + data.taskExeName + "\",\"" + '启动'
							+ "\")' ><font color='red'>启动</font></a>";
					}
					
					html += "<a onclick='fun_to_up(" + $.toStr(data)
							+ ")' class='s_song'>立即执行</a>"
							+ "<a onclick='fun_to_detail(\"" + data.taskExeId
							+ "\")' class='s_bohui'>调用明细</a>"
					if (data.exeStatus == 2) {
						html += "<a onclick='fun_to_delete(\"" + data.taskExeId
								+ "\")' class='s_delete'>删除</a>";
					}
					return html;
				}
			} ];
	$("#mainGrid").jqGrid({
		url : urlShow,
		colNames : colNames,
		colModel : colModel,
		rownumbers : true,
		autowidth : true,
		viewrecords : true,
		pager : '#mainGridPager'
	});
	// 新建
	$('#btn_add').click(
			function() {
				var dg = $.dialog('新增调度', $.ctx
						+ '/admin/pages/taskinfo/task_add.html?parentExeId='
						+ parentExeId, 1000, 530);
				dg.reload = function() {
					$("#mainGrid").setGridParam({
						postData : $("#formSearch").formToJson()
					}).trigger("reloadGrid", [ {
						page : 1
					} ]);
					window.location.reload();
				}
			});
	// 查询
	$("#btnSearch").click(function() {
		$("#mainGrid").setGridParam({
			postData : $("#formSearch").formToJson()
		}).trigger("reloadGrid", [ {
			page : 1
		} ]);
	});

}

function fun_to_status(id, status, name, action) {
	$.confirm('你确认  ' + action + '  任务:[  ' + name + '  ]吗？', function() {
		$.commAjax({
			url : $.ctx + '/api/schedule/taskExeInfo/updateStatus',
			postData : {
				taskExeId : id,
			},
			onSuccess : function(data) {
				$.success('修改任务状态成功。', function() {
					$("#mainGrid").setGridParam({
						postData : $("#formSearch").formToJson()
					}).trigger("reloadGrid", [ {
						page : 1
					} ]);
				});
			}
		});
	});
}
function fun_to_update(id) {
	var dg = $.dialog('修改调度', $.ctx
			+ '/admin/pages/taskinfo/task_add.html?taskExeId=' + id, 1000, 530);
	dg.reload = function() {
		$("#mainGrid").setGridParam({
			postData : $("#formSearch").formToJson()
		}).trigger("reloadGrid", [ {
			page : 1
		} ]);
		window.location.reload();
	}
}
function fun_to_up(item) {
	if (item.sysId) {
		var dg = $.dialog('立即执行-参数', $.ctx + '/admin/pages/taskinfo/task_exe.html', 430, 210);
		dg.getParam = function () {
			return item;
		}
	} else {
		$.commAjax({
			url:$.ctx + '/api/schedule/taskExeInfo/start',
			postData:{
				taskExeId:item.taskExeId,
				sysId:$.toStr(item.sysId)
			},
			onSuccess:function(data) {
				if (data.res) {
					$.alert("执行成功");
				} else {
					$.alert("执行失败");
				}
			}
		});
	}
}
function fun_to_detail(id) {
	var dg = $.dialog('调用明细',
			$.ctx + '/admin/pages/taskinfo/task_detail.html?taskExeId='+id, 800, 500);
	dg.reload = function() {
		$("#mainGrid").setGridParam({
			postData : $("#formSearch").formToJson()
		}).trigger("reloadGrid", [ {
			page : 1
		} ]);
	}
}
function fun_to_delete(id) {
	var mssssg = "";
	$.commAjax({
		url : $.ctx + '/api/schedule/taskExeInfo/get',
		postData : {
			"exeId" : id
		},
		type : 'post',
		cache : false,
		onSuccess : function(data) {
			if(data.locTaskExeInfo.children.length != 0){
				mssssg = "此操作会删除本节点及叶子节点数据，且不可恢复，是否继续？";
			}else{
				mssssg = "此操作会删除本叶子节点，确定删除？";
			}
			$.confirm(mssssg, function() {
				$.commAjax({
					url : $.ctx + '/api/schedule/taskExeInfo/delete',
					postData : {
						"taskExeId" : id
					},
					onSuccess : function(data) {
						$.success('删除成功。', function() {
							$("#mainGrid").setGridParam({
								postData : $("#formSearch").formToJson()
							}).trigger("reloadGrid", [ {
								page : 1
							} ]);
							window.location.reload();
						});
					}
				});
			})
		}
	})
	
}