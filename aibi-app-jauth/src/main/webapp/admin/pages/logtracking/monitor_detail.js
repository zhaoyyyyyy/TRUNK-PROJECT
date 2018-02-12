window.jauth_onload = function(){
	var dg = frameElement.lhgDG;
	dg.removeBtn();
	dg.addBtn("cancel", "取消", function() {
		dg.cancel();
	});
	
	var data = dg.getData();
	
	$.each(data,function(k,v){
		$('#'+k).val(v);
	})
}