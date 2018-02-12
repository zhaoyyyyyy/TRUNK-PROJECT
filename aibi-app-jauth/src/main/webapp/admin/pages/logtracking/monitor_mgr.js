window.jauth_onload = function(){
	// 列表
	var urlShow = $.ctx + '/api/log/monitor/monitorPage/query';
	
	var colNames = ['timestamp', 'userid', 'host', 
	                'level',
	                'thread_name',
	                'nodename',
	                'logger_na',
	                'message','opt'];
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
		fixed :true,
		width : 90,
		align : 'center',
	}, {
		name : 'ipAddr',
		index : 'ipAddr',
		fixed :true,
		width : 150,
		align : 'center'
	}, {
		name : 'levelId',
		index : 'levelId',
		fixed :true,
		width : 90,
		align : 'center',
		formatter:function(v){return $.getCodeDesc('LEVEL',v);}
	}, {
		name : 'threadName',
		index : 'threadName',
		width : 40,
		hidden: true,
		align : 'center'
	},{
		name : 'nodeName',
		index : 'nodeName',
		fixed :true,
		width : 90,
		align : 'center'
	}, {
		name : 'interfaceUrl',
		index : 'interfaceUrl',
		width : 70,
		align : 'left'
	},{
		name : 'errorMsg',
		index : 'errorMsg',
		align : 'left',
		width : 100
	},{
		name : 'nodeName',
		index : 'nodeName',
		fixed : true,
		width : 70,
		align : 'center',
		formatter:function(v,co,data){
			return  "<a onclick='fun_detail(\"" + co.rowId
			+ "\")' class='s_ls' >详情</a>";
		}
	}];
	
	$("#mainGrid").jqGrid({
		url : urlShow,
		colNames : colNames,
		colModel : colModel,
		rownumbers : true,
		autowidth : true,
		viewrecords : true,
		
		rownumbers :true,
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

function fun_detail(rowid){
	var dg = $.dialog('日志详情', $.ctx + '/admin/pages/logtracking/monitor_detail.html',
			600, 600);
	dg.getData = function() {
		return $('#mainGrid').getRowData(rowid);
	}
}
