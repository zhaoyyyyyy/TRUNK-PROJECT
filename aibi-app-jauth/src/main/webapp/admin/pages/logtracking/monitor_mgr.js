window.jauth_onload = function(){
	// 列表
	var urlShow = $.ctx + '/api/log/monitor/monitorPage/query';
	
	var colNames = ['timestamp', 'userid', 'host', 
	                'level',
	                'thread_name',
	                'nodename',
	                'logger_na',
	                'message'];
	var colModel = [{
		name : 'opTime',
		index : 'opTime',
		width : 125,
		fixed:true,
		align : 'center',
		formatter : function(cellvalue) {  
			 return cellvalue.substr(0,19);
		}
	},{
		name : 'userId',
		index : 'userId',
		width : 20,
		align : 'center',
	}, {
		name : 'ipAddr',
		index : 'ipAddr',
		width : 35,
		align : 'center'
	}, {
		name : 'levelId',
		index : 'levelId',
		width : 20,
		align : 'center',
		formatter:function(v){return $.getCodeDesc('LEVEL',v);}
	}, {
		name : 'threadName',
		index : 'threadName',
		width : 40,
		align : 'center'
	},{
		name : 'nodeName',
		index : 'nodeName',
		width : 25,
		align : 'center'
	}, {
		name : 'interfaceUrl',
		index : 'interfaceUrl',
		width : 70,
		align : 'center'
	},{
		name : 'errorMsg',
		index : 'errorMsg',
		fixed : true,
		width : 100,
		align : 'center'
	}];
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
