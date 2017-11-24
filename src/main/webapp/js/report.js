$(function() {
	getCity();
});
function getCity() {
	var url = "ReportServlet";
	var params = {
		"type" : "getCity"
	};
	$("#city").empty();
	$("#province").empty();
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0)) {
			for ( var j in data[0]) {
				var datas = data[0][j];
				if('city'==j){
					$("#city").append('<option value="">=====请选择=====</option>');
					for ( var k in datas) {
						$("#city").append('<option value=' + datas[k].cityID + '>'+ datas[k].cityName + '</option>');
					}
				}else if("province"==j){
					$("#province").append('<option value="">=====请选择=====</option>');
					for ( var k in datas) {
						$("#province").append('<option value=' + datas[k].province + '>'+ datas[k].provinceName + '</option>');
					}
					
				}
			}
		}
	}, true);
}
function initTable() {
	// 先销毁表格
	$('#cusTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#cusTable").bootstrapTable({
		method : "get", // 使用get请求到服务器获取数据
		url : 'ReportServlet?type=getBidData', // 获取数据的Servlet地址
		striped : true, // 表格显示条纹
		pagination : true, // 启动分页
		pageSize : 10, // 每页显示的记录数
		pageNumber : 1, // 当前第几页
		pageList : [ 10, 50, 100 ], // 记录数可选列表
		search : false, // 是否启用查询
		showColumns : true, // 显示下拉框勾选要显示的列
		showRefresh : false, // 显示刷新按钮
		sidePagination : "server", // 表示服务端请求
		// 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
		// 设置为limit可以获取limit, offset, search, sort, order
		queryParamsType : "undefined",
		queryParams : function queryParams(params) { // 设置查询参数
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				city : $("#city").val(),// $("#cityName").val()
				province:$("#province").val(),
				startDatetime : $("#startDatetime").val(),
				endDatetime : $("#endDatetime").val(),
			};
			return param;
		}
	});
	getTotalBidIncome();
}

$(document).ready(function() {
	// 调用函数，初始化表格
	initTable();
	// 当点击查询按钮的时候执行
	$("#search").bind("click", initTable);
});
/* 获取统计竞价收益 */
function getTotalBidIncome() {
	var url = "ReportServlet";
	var params = {
		"type" : "getTotalBidIncome",
		"startDatetime" : $("#startDatetime").val(),
		"endDatetime" : $("#endDatetime").val(),
		"city":$("#city").find("option:selected").val(),
		"province":$('#province').find("option:selected").val()
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0) && data.result == 'succ') {
			$("#stsDiv").html(
					'<span><Strong>共有' + data.hotelNum + '家酒店参与竞价，产生的竞价收益'
							+ data.total + '元</Strong></s></span>');
		}
	}, true);
}

function getBidData() {
	// var url = "ReportServlet";
	// var params = {"type":"getBidData"};
	// slAjax("mainDiv",url,params,function(data, textStatus, request){
	// if((data!=''||data.length!=0)&&data.result=='succ'){
	// }
	// },true);
}