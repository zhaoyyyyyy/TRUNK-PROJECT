var _parentKey = '';

var typeAndI = {
		1:1,
		2:2,
		4:4,
		5:0,
		6:5
}
var createConfigurationTemplate = [ {
	name : '新增目录',
	fields : [ {
		name : '请输入名称',
		code : '请输入编码',
		type : 'catalog',
		valueType : 5,
		value : '请输入配置项的值',
		desc : ''
	} ]
},{
	name : '开关配置',
	fields : [ {
		name : '请输入配置项名称',
		code : '{PARENT_CODE}_请输入',
		type : 'boolean',
		valueType : 1,
		value : '01',
		flag:''
	} ]
}, {
	name : '文本配置',
	fields : [ {
		name : '请输入名称',
		code : '请输入编码',
		type : 'text',
		valueType : 2,
		value : '请输入配置项的值',
		desc : '',
		flag:''
	} ]
}, /** {
	name : '枚举配置',
	fields : [ {
		name : '枚举',
		code : '{PARENT_CODE}_DRIVERCLASS',
		type : 'enum',
		dimCode : 'driverClass',
		value : 'http://xxx.xxx.xxx.xxx',
		flag:''
	} ]
},**/{
	name : '数据库配置',
	fields : [ {
		name : '数据库类型',
		code : 'TYPE',
		type : 'enum',
		valueType : 6,
		dimCode : 'SJKLX',
		value : '',
		flag:''
	},{
		name : '驱动',
		code : 'DRIVER',
		type : 'enum',
		valueType : 4,
		dimCode : 'driverClass',
		value : '',
		flag:''
	}, {
		name : '地址',
		code : 'URL',
		type : 'text',
		valueType : 2,
		value : 'root',
		flag:''
	}, {
		name : '用户名',
		code : 'USERNAME',
		type : 'text',
		valueType : 2,
		value : '请输入配置项的值',
		flag:''
	}, {
		name : '密码',
		code : 'PASSWORD',
		type : 'text',
		valueType : 2,
		value : '请输入配置项的值',
		flag:''
	} ]
},{
	name : '驱动配置',
	fields : [ {
		name : '驱动',
		code : '{PARENT_CODE}_DRIVERCLASS',
		type : 'driver',
		valueType : 4,
		dimCode : 'driverClass',
		value : 'http://xxx.xxx.xxx.xxx',
		flag:''
	} ]
},{
	name : '数据库类型',
	fields : [ {
		name : '数据库类型',
		code : '{PARENT_CODE}_TYPE',
		type : 'driver',
		valueType : 6,
		dimCode : 'SJKLX',
		value : 'http://xxx.xxx.xxx.xxx',
		flag:''
	} ]
}]

function fun_add(i, configKey) {
	var isEdit = 0;
	var dg;
	if (null != configKey) {
		var a = typeAndI[i];
		isEdit = 1;
		dg = $.dialog('编辑  [  ' + configKey + '  ]', $.ctx
				+ '/admin/pages/config/config_add.html', 1200, 500);
		dg.getParams = function() {
			return {
				'configFields' : createConfigurationTemplate[typeAndI[i]].fields,
				'coKey' : configKey,
				'isEdit' : isEdit,
			}
		}
	} else {
		dg = $.dialog('新增  [  ' + createConfigurationTemplate[i].name + '  ]',
				$.ctx + '/admin/pages/config/config_add.html', 800, 500);
		dg.getParams = function() {
			return {
				'configFields' : createConfigurationTemplate[i].fields,
				'coKey' : _parentKey,
				'isEdit' : isEdit,
			}
		}
	}
	dg.reload = function() {
		$("#mainGrid").setGridParam({
			postData : $("#formSearch").formToJson()
		}).trigger("reloadGrid", [ {
			page : 1
		} ]);
	}
}
var model = {
		btn : ""
}
window.jauth_onload = function() {
	
	new Vue({
		el : '#buttonA',
		data : model
	})
	$.commAjax({
		url : $.ctx + '/api/config/tree',
		isShowMask : false,
		type : 'POST',
		async : false,
		onSuccess : function(data) {
			$("li.root").append(data);
			mySimpleTree = $('#tree').simpleTree(
				{
					autoclose : false,
					afterClick : function(thi) {
						model.btn = "";
						_parentKey = $(thi).attr('id');
						$("#mainGrid").setGridParam({
							postData : {
								parentKey : _parentKey
							}
						}).trigger("reloadGrid", [ {
							page : 1
						} ]);
						$.each(createConfigurationTemplate,function(i) {
								var btnObj = createConfigurationTemplate[i]
								var btn = '<div class="left newButton"><div>'
										+ '<input type="button" class="add" value="'
										+ btnObj.name
										+ '" onclick="fun_add('+ i+ ')" /></div></div>';
								model.btn = model.btn
										+ btn;
						})
						model.btn = model.btn
								+ '<div class="clear"></div>';
					}
				})
		}
	});

	var colNames = [ '名称', '编码', '值', '描述', '类型', '操作' ];
	var colModel = [
			{
				name : 'configName',
				index : 'configName',
				width : 20,
				align : 'left'
			},
			{
				name : 'configKey',
				index : 'configKey',
				width : 50,
				align : 'left'
			},
			{
				name : 'configVal',
				index : 'configVal',
				width : 20,
				align : 'left'
			},
			{
				name : 'configDesc',
				index : 'configDesc',
				width : 50,
				align : 'left'
			},
			{
				name : 'configValType',
				index : 'configValType',
				width : 48,
				fixed : true,
				align : 'center',
				formatter : function(value, opts, data) {
					return $.getCodeDesc('PZLX', data.configValType);
				}
			},
			{
				name : 'configKey',
				index : 'configKey',
				width : 100,
				fixed : true,
				sortable : false,
				formatter : function(value, opts, data) {
					return "<a onclick='fun_add(\"" + data.configValType
							+ "\",\"" + data.configKey
							+ "\")' class='s_edit' >编辑</a>"
							+ "<a onclick='fun_del(\"" + data.configKey
							+ "\")' class='s_delete' >删除</a>"
				}
			} ];
	$("#mainGrid").jqGrid({
		url : $.ctx + '/api/config/configPage/query',
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
		}).trigger("reloadGrid", [ {
			page : 1
		} ]);
	});
}

function fun_del(configKey) {
	var mssssg = "";
	$.commAjax({
		url : $.ctx + '/api/config/get',
		postData : {
			"coKey" : configKey
		},
		type : 'post',
		cache : false,
		onSuccess : function(data) {
			if(data.config.children.length != 0){
				mssssg = "此操作会删除本节点及叶子节点数据，且不可恢复，是否继续？";
			}else{
				mssssg = "此操作会删除本叶子节点，确定删除？";
			}
			$.confirm(mssssg, function() {
				$.commAjax({
					url : $.ctx + '/api/config/delete',
					postData : {
						key : configKey
					},
					onSuccess : function(data) {
						$.success('删除成功。', function() {
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
	})
	
}
function fun_to_refresh(){
	$.confirm("该操作会关闭树并重新获取，确认继续吗？", function() {
		$("#LOC").remove();
		model.btn="";
		$("#refresh").html('<ul class="simpleTree" id="tree" style="margin-left: 5px; margin-top: 5px;">'
		+'<li class="root"><span class="text" id="root">配置项树</span><div style="float:right; margin-right:10px">'
		+'<input type="button" onclick="fun_to_refresh()" value="刷新"></input></div></li></ul>');
		$.commAjax({
			url : $.ctx + '/api/config/tree',
			isShowMask : false,
			type : 'POST',
			async : false,
			onSuccess : function(data) {
				$("li.root").append(data);
				mySimpleTree = $('#tree').simpleTree(
					{
						autoclose : false,
						afterClick : function(thi) {
							model.btn = "";
							_parentKey = $(thi).attr('id');
							$("#mainGrid").setGridParam({
								postData : {
									parentKey : _parentKey
								}
							}).trigger("reloadGrid", [ {
								page : 1
							} ]);
							$.each(
								createConfigurationTemplate,
								function(i) {
									var btnObj = createConfigurationTemplate[i]
									var btn = '<div class="left newButton"><div>'
											+ '<input type="button" class="add" value="'
											+ btnObj.name
											+ '" onclick="fun_add('
											+ i
											+ ')" /></div></div>';
									model.btn = model.btn
											+ btn;
							})
							model.btn = model.btn
									+ '<div class="clear"></div>';
						}
					})
			}
		});
	})
}