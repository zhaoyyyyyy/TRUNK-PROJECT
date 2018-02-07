window.jauth_onload = function(){
	// 列表
	var urlShow = $.ctx + '/api/log/interface/interfacePage/query';
	
	var colNames = ['时间', '用户名', 'IP地址', 
	                '接口名称',
	                '接口路径', 
	                '输入参数',
	                '输出参数','操作'];
	var colModel = [{
		name : 'opTime',
		index : 'opTime',
		width : 180,
		fixed :true,
		align : 'center',
		formatter : function(cellvalue) {  
			 return cellvalue.substr(0,19);
		}
	},{
		name : 'userId',
		index : 'userId',
		width : 80,
		fixed :true,
		align : 'center',
	}, {
		name : 'ipAddr',
		index : 'ipAddr',
		width : 120,
		fixed :true,
		align : 'center'
	}, {
		name : 'interfaceName',
		index : 'interfaceName',
		width : 180,
		fixed :true,
		align : 'center'
	}, {
		name : 'interfaceUrl',
		index : 'interfaceUrl',
		width : 30,
		align : 'center'
	},{
		name : 'inputParams',
		index : 'inputParams',
		width : 30,
		align : 'center',
		formatter : function(value, opts, data) {
			return $.toStr(data.inputParams);
		}
	}, {
		name : 'outputParams',
		index : 'outputParams',
		width : 200,
		fixed : true,
		align : 'center',
		formatter : function(value, opts, data) {
			return $.toStr(data.outputParams);
		}
	},{
		name : 'ipAddr',
		index : 'ipAddr',
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
	var dg = $.dialog('日志详情', $.ctx + '/admin/pages/logtracking/interface_detail.html',
			600, 600);
	dg.getData = function() {
		return $('#mainGrid').getRowData(rowid);
	}
}
