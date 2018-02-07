//$(function() {
//	bindMenuEvent();
//	bindTitleEvent();
//	
//	$('.tit_xker_com:eq(0)').click();
//});

window.jauth_onload = function(){
		
		//$('.tit_xker_com:eq(0)').click();
		
	
		$.commAjax({
			url:$.ctx+'/api/resource/userResource/get',
			type:'post',
			cache:false,
			onSuccess:function(data){
//				console.log(data);
				new Vue({ el:'#container', data: data });
				bindMenuEvent();
				bindTitleEvent();
				bindDisEvent();
			}
		})
		
}


/**
 * 收缩展开事件
 * hongfb
 */
function bindDisEvent(){
	$('.headerDisplay').click(function(){
		var $clickEl = $(this);
		if($clickEl.attr("status") == "1"){  //原状态展开，要收缩
			$clickEl.prev().slideUp('fast',function(){
				$clickEl.attr("status","0");
				$clickEl.css("top", "2px");
				$(".main").css("top", "0px");
			});
		}else{	//原状态收缩,展开
			$clickEl.prev().slideDown('fast',function(){
				$clickEl.attr("status","1");
				$clickEl.css("top", "78px");
				$(".main").css("top", "88px");
			});
		}
	});

	$('.leftDisplay').click(function(){
		var $clickEl = $(this);
		var status = 1;
		var clickElLeft = "2px";
		var rightElLeft = "0px";
		if($clickEl.attr("status") == "1"){  //原状态展开，要收缩
			$clickEl.prev().hide('fast');
			
			status = 0;
			clickElLeft = "2px";
			rightElLeft = "0px";
		}else{	//原状态收缩,展开
			$clickEl.prev().show('fast');
			
			status = 1;
			clickElLeft = "178px";
			rightElLeft = "186px";
		}
		$clickEl.attr("status",status);
		$clickEl.css("left", clickElLeft);
		$("#right").css("left", rightElLeft);
	});
	
}

/**
 * 菜单事件
 * zhougz
 */
function bindMenuEvent(){
	var $nextXker = null;
		$('.tit_xker_com').click(function(){
			var $clickXker = $(this);
			if($nextXker){
				$nextXker.slideUp('fast',function(){
					$nextXker = $clickXker.next();
					$nextXker.slideDown('fast');
				});
			}else{
				$nextXker = $clickXker.next();
				$nextXker.slideDown('fast');
			}
	});
}

/**
 * 导航事件
 * zhougz
 */
function bindTitleEvent(){
	$('.menuson a').click(function(){
		var $thi = $(this);
		var t1 = $thi.parents('.menuson').prev().text();
		var t2 = $(this).text();
		top.titleUrlArray = {};
		top.titleUrlArray[t2] = $(this).attr('href');
		top.titleUrlArray[t1] = '';
	});
}

/**
 * 页面设置导航
 * zhougz
 */
window.top.titleUrlArray = {};
window.top.getTitleHtml = function(titleObj,hasreturn){
	top.titleUrlArray[titleObj.title] = titleObj.url;
	var html = '';
	$.each(top.titleUrlArray,function(k,v){
		k=k.replace(/\s+/g,"");
		v=v.replace(/\s+/g,"");
		if( k!="" && v!="~~~~~"){
			if(v != ""){
				html +='<li><a href="'+v+'" >'+k+'</a></li>';
			}else{
				html ='<li><a >'+k+'</a></li>'+html;
			}
		}
	});
	
	var returnhtml = ' <div class="place"> <span>位置：</span> <ul class="placeul"><li><a href="../index/workstand_index.html">首页</a></li>'+html;
	if(hasreturn){
  		 returnhtml += '<li><a href="javascript:history.back(-1);top.titleUrlArray[\''+titleObj.title+'\']=\'~~~~~\';">返回</a></li>';
	}  
	 returnhtml +=' </ul> </div>';
	 
	 return returnhtml;
}

