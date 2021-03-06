var jsView = {
	data:{
        /** 执行类型:EXE_TYPE:1.延迟执行；2.按执行时间周期执行 */
        /** 执行类型:EXE_TYPE:1.延迟执行 */
        EXE_TYPE_DELAY : '1',
        EXE_TYPE_DELAY_TEXT : '延迟执行',
        /** 执行类型:EXE_TYPE:2.按执行时间周期执行 */
        EXE_TYPE_CIRCLE : '2',
        	EXE_TYPE_CIRCLE_TEXT : '周期执行'
	}
};

var model = {
	taskExeName : '请从左侧树中选择节点',
	taskExeTime : '',
	sysId : ''
}
window.jauth_onload = function() {
	new Vue({
		el : '#saveDataDiv',
		data : model
	})
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
							
						taskExtId = $(thi).attr('id'), $("#mainGrid")
							.setGridParam({
								postData : {
									taskExtId : taskExtId
								}
							}).trigger("reloadGrid", [ {
								page : 1
							} ]);
							
							$.commAjax({
								url : $.ctx+ '/api/schedule/taskExeInfo/get',
								isShowMask : false,
								type : 'POST',
								async : false,
								postData : {
									exeId : $(thi).attr('id')
								},
								onSuccess : function(data) {
									model.taskExeName = data.locTaskExeInfo.taskExeName,
									model.taskExeTime = data.locTaskExeInfo.taskExeTime,
									model.sysId = data.locTaskExeInfo.sysId
														}
										});
							
							
						}
					})
		}
	});

	var urlShow = $.ctx + '/api/log/taskexecute/taskexecutePage/query';
	var colNames = [ '执行时间', '执行方式', '响应状态', '响应信息', '执行人' ];
	var colModel = [ {
		name : 'startTime',
		index : 'startTime',
		width : 125,
		fixed:true,
		align : 'center',
		formatter : function(cellvalue) {  
			 return cellvalue.substr(0,19);
		}
	}, {
		name : 'exeType',
		index : 'exeType',
		width : 3,
		align : 'center',
		formatter : function(value, opts, data) {
			var showVal = "";
			if (data.exeType==jsView.data.EXE_TYPE_CIRCLE) {
				showVal = jsView.data.EXE_TYPE_CIRCLE_TEXT;
			} else if (data.exeType==jsView.data.EXE_TYPE_DELAY) {
				showVal = jsView.data.EXE_TYPE_DELAY_TEXT
			}
			return showVal;
		}
	}, {
		name : 'status',
		index : 'status',
		width : 4,
		align : 'center',
	    formatter:function(v){return $.getCodeDesc('STATUS',v);}
	}, {
		name : 'returnMsg',
		index : 'returnMsg',
		width : 10,
		align : 'center',
		formatter : function(value, opts, data) {
			return $.toStr(data.returnMsg);
		}
	}, {
		name : 'userId',
		index : 'userId',
		fixed : true,
		width : 86,
		align : 'center'
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
	// 查询按钮
	$("#btnSearch").click(function() {
		$("#mainGrid").setGridParam({
			postData : $("#formSearch").formToJson()
		}).trigger("reloadGrid", [{
			page : 1
		}]);
	});
}