/* 
 * div: 自定义DIV ID,把本次ajax请求的相关html包含在里面，用于遮罩层
 * async: false 同步； true 异步
*/
function slAjax(div, url, param, successFun, async){
	var pDiv= $("#"+div);
	pDiv.css('position','relative');
	//if( pDiv.attr("style").indexOf("width")==-1) //给默认宽度100%，否则IE显示异常
	//	pDiv.css('width', '100%');
	if($("#menuBar_"+div).length==0){
		pDiv.attr('name','1');
		pDiv.append('<div id="menuBar_'+ div +'" class="progressBar" style="top:80px;">处理中，请稍等...</div>');
		pDiv.append('<div id="menuGround_'+ div +'" class="background"></div> ');
	}
	$("#menuGround_"+div).height( pDiv.height() );
	$("#menuGround_"+div+",#menuBar_"+div).show();
	$("#menuGround_"+div+",#menuBar_"+div).removeClass('display');
	pDiv.attr('name', parseInt(pDiv.attr('name'))+1 ); //通过name来控制类似update再load的类型
	$.ajax({
		type:"POST",
		async:async,
		dataType:'json',
		url:url,
		data:param,
		success:successFun, //TD 错误
		error:function(XMLHttpRequest, textStatus, errorThrown){
//			alert("系统内部异常" +
//					  "</br>status:" + XMLHttpRequest.status +
//					  "</br>textStatus:" + textStatus +
//					  "</br>message:"+ errorThrown.message );
		},
		complete:function(){
			pDiv.attr('name', parseInt(pDiv.attr('name'))-1 );
			if( pDiv.attr('name')==1 )
				$("#menuGround_"+div+",#menuBar_"+div).hide(); 
		}
	});
}
//fill data
function fillData(data, divID) {
	deleteRows(divID);
	var t = document.getElementById("datatable");
	var tb = document.createElement('tbody');
	t.appendChild(tb);
	
	for ( var o in data) {
		var nCloneTr = document.createElement('tr');
		nCloneTr.className = "center";
		for ( var obj in data[o]) {
			if (obj != "totalCounts") {
				var nCloneTd = document.createElement('td');
				nCloneTd.appendChild(document.createTextNode(data[o][obj]));
				nCloneTd.className = "center";
				nCloneTr.appendChild(nCloneTd);
			}
		}
		tb.appendChild(nCloneTr);
		//$('#' + divID + ' tbody').each(function() {
			//this.insertBefore(nCloneTr.cloneNode(true), this.childNodes[0]);
			
		//});
	}
}
//delete tr from tbody
function deleteRows(divID) {
	var dt = document.getElementById(divID).childNodes;
	for(var n in dt)
		{
		if(dt[n].nodeName=="TBODY")
			document.getElementById(divID).removeChild(dt[n]);
		
		}
}

//show pages
function initPager(cpage,pageSize,total){
	if(cpage == "" || cpage == 0){
		cpage = 1;
	}
	var pager = {};
	
	var pageCount = parseInt(total%pageSize==0?(total/pageSize):(total/pageSize+1));
	var array = [];
	for(var i=0;i<pageCount;i++){
		array.push(i+1);
	}
	pager.pageCount = pageCount;
	pager.cpage = cpage;
	pager.pageSize = pageSize;
	pager.pages = array;
	pager.haspreviouPage = cpage>1&&total>pageSize;
	pager.hasnextPage = cpage<pager.pages.length;
	pager.previousPage = cpage-1;
	pager.nextPage = cpage+1;
	return pager;
}
//分页处理
function showPage(showid,pager,searchMethod){
	var first = 1;
	var previousPage = pager.previousPage<=0?1:pager.previousPage;
	var nextPage = pager.nextPage>pager.pageCount?(pager.pageCount<=0?1:pager.pageCount):pager.nextPage;
	var pageCount = pager.pageCount<=0?1:pager.pageCount;
	var str = "<tr>";
    str+='<td width="50%">共 <span class="right-text09">'+pager.pageCount+'</span> 页 | 第 <span class="right-text09">'+pager.cpage+'</span> 页</td>';
    str+='<td width="49%" align="right">[<a href="javascript:void(0)" class="right-font08" onclick="'+searchMethod+'('+first+','+pager.pageSize+')">首页</a> | <a href="javascript:void(0)" class="right-font08" onclick="'+searchMethod+'('+previousPage+','+pager.pageSize+')">上一页</a> | <a href="javascript:void(0)" class="right-font08" onclick="'+searchMethod+'('+nextPage+','+pager.pageSize+')">下一页</a> | <a href="javascript:void(0)" class="right-font08" onclick="'+searchMethod+'('+pageCount+','+pager.pageSize+')">末页</a>] 转至：</td>'
    str+='<td width="1%"><table width="20" border="0" cellspacing="0" cellpadding="0">';
    str+='<tr>';
    str+='<td width="1%"><input id="searchAppointPageId"name="textfield3" type="text" class="right-textfield03" size="1" /></td>';
    str+='<td width="87%"><input name="Submit23222" type="button" class="right-button06" value="" onclick="searchAppointPage()" />';
    str+='</td>';
    str+='</tr>';
    $("#"+showid).html(str);
}
function showPageTool(showid,cid,pager,searchMethod){
	var container = $('#'+cid);
	container.attr('align','center');
	var str = "";
	if(pager.haspreviouPage && pager.previousPage!=-1){
		str += '<li class="prev active"><a href="javascript:void(0)" onclick="'+searchMethod+'('+pager.previousPage+','+pager.pageSize+')"><span	class="fa fa-angle-left"></span>&nbsp;Previous</a></li>';
	}else{
		str += '<li class="prev disabled"><a href="#"><span	class="fa fa-angle-left"></span>&nbsp;Previous</a></li>';
	}
	var pages = pager.pages;
	var pageSize = pager.pageSize;
	for(var i=pager.cpage-11;i<pages.length&&i<pager.cpage+10;i++){
		if(i<0)continue;
		if(i==0){
			$('#'+showid).html('Showing '+((pages[i]-1)*pageSize+1)+' to '+(pages[i]*pageSize)+' of '+pages.length+' entries');
		}
		if(pages[i]!=pager.cpage){
			if(pager.cpage==""&&i==0){
				str += '<li class="active"><a href="javascript:void(0)" onclick="'+searchMethod+'('+pages[i]+','+pager.pageSize+')">'+pages[i]+'</a></li>';
			}else{
				str += '<li><a href="javascript:void(0)" onclick="'+searchMethod+'('+pages[i]+','+pager.pageSize+')">'+pages[i]+'</a></li>';
			}
		}else{
			str += '<li class="active"><a href="javascript:void(0)" onclick="'+searchMethod+'('+pages[i]+','+pager.pageSize+')">'+pages[i]+'</a></li>';
			//str += '<span class="current">'+pages[i]+'</span>';
			$('#'+showid).html('Showing '+((pages[i]-1)*pageSize+1)+' to '+(pages[i]*pageSize)+' of '+pages.length+' entries');
		}
	}
	if(pager.hasnextPage){
		str += '<li class="next active"><a href="javascript:void(0)" onclick="'+searchMethod+'('+pager.nextPage+','+pager.pageSize+')">Next&nbsp;<span class="fa fa-angle-right"></span></a></li>';
	}else{
		str += '<li class="next disabled"><a href="#">Next&nbsp;<span class="fa fa-angle-right"></span></a></li>';
	}
	container.html(str);
}
//string => array
function str2Array(str){
	var v = str.replace("[","").replace("]","");
	var dataStr = v.split(',');
	var dataArray = [];
	dataStr.forEach(function(data,index,arr){  
		dataArray.push(+data);  
    });  
	return dataArray;
}

//checkRight
function checkRight(classid){
	var result = false;
	$.ajax({  
		type: "post",
		url : "../userRightServlet",  
		dataType:'json',
		data: {"classid":classid,"type":'checkRight'}, 
		async: false,
		success: function(data, textStatus, request){
			var flag = request.getResponseHeader('result');
			if(flag=="suc"){
				result = true;
			}else if(flag=="noRight"){
				alert("对不起，你没有权限！");
				window.location = '../home.jsp';
			}else{
				alert("对不起，系统出问题请联系管理员！");
				window.location = '../home.jsp';
			}
		}
	});
	return result;
}
//获取当前日期，向后n天的日期，参数为整数
//如果为负整数为向前，0是当天，正整数向后
function getPreDate(_days){
	//格式化日期函数将20120405格式化成2012-04-05
	Date.prototype.format = function(format) // author : meizz
	{
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		// millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}
	//获取当前日期
	var myDate = new Date();
	var _yyyy = myDate.getFullYear();
	var _mth = myDate.getMonth();
	var _dd = myDate.getDate();
	//转换Date类型
	var _theCurrentDateNoFmt = _date = new Date(_yyyy, _mth, _dd);
	//格式化日期格式
	//var _theCurrentDateFmt = new Date(_yyyy, _mth, _dd).format("yyyy-MM-dd");
	var _theCurrentDateFmt = new Date(_yyyy, _mth, _dd).format("MM/dd/yyyy");
	//(24 * 60 * 60 * 1000)毫秒后的日期
	var _thePreDateTimes = _theCurrentDateNoFmt.getTime() + (_days * (24 * 60 * 60 * 1000));
	//格式化日期
	var _thePreDateDateFmt = new Date(_thePreDateTimes).format("MM/dd/yyyy");
	return _thePreDateDateFmt;
}

/* 计算图表的tickInterval */
function calcTickInterval(total){
	var interval = 1;
	if(total > 13){
		total = total/13;
		var times = total/7;
		//console.log(times);
		times = Math.ceil(times);
		//console.log("after ceil is : " + times);
		
		interval = times*7;
	}
	//console.log("interval : " + interval);
	return interval;
}

