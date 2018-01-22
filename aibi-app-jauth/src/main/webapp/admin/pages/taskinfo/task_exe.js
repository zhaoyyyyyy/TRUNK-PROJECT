window.jauth_onload = function() {
	var dg = frameElement.lhgDG;
	
	// 定义vue
	new Vue({
		el : '#exeForm',
		data : {// 定义Model
			taskExeId:dg.getParam().taskExeId,
			sysId:Boolean(dg.getParam().sysId)?$.toStr(dg.getParam().sysId):""
		},
		mounted:function(){
			this.$nextTick(function () {
				this.addSaveBtns();
			});
		},
		methods:{
			addSaveBtns:function(){
				dg.removeBtn();
				dg.addBtn("save", "执行", function() {
					$.commAjax({
						url:$.ctx + '/api/schedule/taskExeInfo/start',
						postData:$("#exeForm").formToJson(),
						onSuccess:function(data) {
							if (data.res) {
								$.alert("执行成功");
							} else {
								$.alert("执行失败");
							}
							dg.cancel();
						}
					});
				});
				dg.addBtn("cancel", "取消", function() {
					dg.cancel();
				});
			}
		}
	});
}
