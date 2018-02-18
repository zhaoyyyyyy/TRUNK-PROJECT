//单点登录，如果#后面带着token则可以直接登录进入
$(function(){
	var hash = window.location.hash;
	if(hash){
		var token = hash.split("#")[1];
		var ssg = window.sessionStorage;
		if(ssg){
			ssg.setItem("token",token);
			location.href = "./admin/pages/index/index.html";
		}
	}else{
		 window.location.href = "./admin/pages/index/login.html";
	}
})