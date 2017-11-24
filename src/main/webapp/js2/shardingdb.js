$(function() {
	getUserPermission();
});

function getUserPermission(){
	var url = "ShardingUserServlet";
	var params = {
		"type" : "checkShardingUserInfo"
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0 || data.status == 'succ')) {
			//console.log("flag"+data.flag);
			if(data.flag == 1){
				$("#shardingdbAddDiv").hide();
				$("#shardingSourceDBInfoAddDiv").hide();
				getSourceDBInfo();
				initTable();
			}else{
				$("#shardingdbAddDiv").hide();
				$("#shardingSourceDBInfoAddDiv").hide();
				$("#shardingdbShowDiv").hide();
			}
		} else {
			$("#shardingdbAddDiv").hide();
			$("#shardingSourceDBInfoAddDiv").hide();
			$("#shardingdbShowDiv").hide();
		}
	}, true);
};

function getSourceDBInfo() {
	var url = "ShardingDBServlet";
	var params = {
		"type" : "getSourceDBInfo"
	};
	$("#srcDB").empty();
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0)) {
			$("#srcDB").append('<option value="">=====All=====</option>');
			var i = data.length, j;
		    var tempExchangValID,tempExchangValDB;
		    while (i > 0) {
		        for (j = 0; j < i - 1; j++) {
		            if (data[j].sourceDB.localeCompare(data[j + 1].sourceDB)>0) {
		            	tempExchangValID = data[j].id;
		                tempExchangValDB = data[j].sourceDB;
		                data[j].id = data[j + 1].id;
		                data[j].sourceDB = data[j + 1].sourceDB;
		                data[j + 1].id = tempExchangValID;
		                data[j + 1].sourceDB = tempExchangValDB;
		            }
		        }
		        i--;
		    }
			for ( var i in data) {
				$("#srcDB").append(
						'<option value=' + data[i].id + '>' + data[i].sourceDB
								+ '</option>');
			}
		}
	}, true);
}
function initTable() {
	// 先销毁表格
	$('#cusTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#cusTable")
			.bootstrapTable(
					{
						method : "get", // 使用get请求到服务器获取数据
						url : 'ShardingDBServlet?type=getShardingSourceDBInfo', // 获取数据的Servlet地址
						striped : true, // 表格显示条纹
						pagination : true, // 启动分页
						pageSize : 10, // 每页显示的记录数
						pageNumber : 1, // 当前第几页
						pageList : [ 50, 100 ], // 记录数可选列表
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
								// city : $("#city").val(),//
								// $("#cityName").val()
								srcType : $("#srcType").val(),
								srcDB : $("#srcDB").val(),
							// startDatetime : $("#startDatetime").val(),
							// endDatetime : $("#endDatetime").val(),
							};
							return param;
						},
						rowAttributes : function(row, index) {
							// if (row.isValid == 'T') {
							// 	row.isValid = "有效";
							// } else {
							// 	row.isValid = "无效";
							// }
							row.operate = '<button type="button" class="btn btn-link" onclick=shardingdbSourceUpdate("'
									+ row.id
									+ '")>编辑</button>&nbsp;'
									+
									// '<button type="button" class="btn
									// btn-link"
									// onclick=shardingdbSourceDel("'+row.sourceDB+'","'+row.id+'")>删除</button>&nbsp;'+
									'<button type="button" class="btn btn-link" onclick=shardingdbAdd("'
									+ row.sourceDB
									+ '","'
									+ row.id
								    + '","'
									+ row.sourceType
									+ '")>ShardingDB配置</button>&nbsp;';
						}
					});
}

// function shardingdbAdd(sourceDB, id) {
// 	shardingdbPreView(id);
// 	$("#shardingPrefix").val('');
// 	$("#shardingNum").val('');
// 	$("#titleDiv").html('添加ShardingDB源（' + sourceDB + '）对应ShardingDB');
// 	$("#shardingdbAddDiv").show();
// 	$("#shardingdbShowDiv").hide();
// 	$("#addSourceDBID").val(id);
// 	$("#shardingdbBackBtn").hide();
// }

function shardingdbAdd(sourceDB, id, sourceType) {
	location.href = encodeURI("shardingdbselected.html?sourceType=" + sourceType + "&id=" + id + "&sourceDB=" + sourceDB);
}

// function shardingdbPreView(id) {
// 	if (id == -1 && $("#shardingPrefix").val() == '') {
// 		alert("请输入要生成的shardingdb前缀!");
// 		return;
// 	}
// 	var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
// 	if ($("#shardingNum").val() != '' && !r.test($("#shardingNum").val())) {
// 		alert("请输入正整数!");
// 		return;
// 	}
// 	// 先销毁表格
// 	$('#shardingdbTable').bootstrapTable('destroy');
// 	// 初始化表格,动态从服务器加载数据
// 	$("#shardingdbTable").bootstrapTable({
// 		method : "get", // 使用get请求到服务器获取数据
// 		url : 'ShardingDBServlet?type=shardingdbPreView', // 获取数据的Servlet地址
// 		striped : true, // 表格显示条纹
// 		pagination : true, // 启动分页
// 		pageSize : 50, // 每页显示的记录数
// 		pageNumber : 1, // 当前第几页
// 		pageList : [ 50, 100 ], // 记录数可选列表
// 		search : false, // 是否启用查询
// 		showColumns : false, // 显示下拉框勾选要显示的列
// 		showRefresh : false, // 显示刷新按钮
// 		sidePagination : "server", // 表示服务端请求
// 		// 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
// 		// 设置为limit可以获取limit, offset, search, sort, order
// 		queryParamsType : "undefined",
// 		queryParams : function queryParams(params) { // 设置查询参数
// 			var param = {
// 				pageNumber : params.pageNumber,
// 				pageSize : params.pageSize,
// 				shardingPrefix : $("#shardingPrefix").val(),
// 				shardingNum : $("#shardingNum").val(),
// 				sourceDBID : (id == -1 ? $("#addSourceDBID").val() : id),
// 			};
// 			return param;
// 		},
// 		rowAttributes : function(row, index) {
// 			if (index >= 0) {
// 				$("#shardingdbSaveBtn").show();
// 				$("#shardingdbBackBtn").hide();
// 			}
// 			if (row.isValid == 'T') {
// 				row.isValid = "有效";
// 			} else {
// 				row.isValid = "无效";
// 			}
// 		}
// 	});
// }
// // save shardingdb
// function shardingdbSave() {
// 	if ($("#shardingPrefix").val() == '') {
// 		alert("请输入要生成的shardingdb前缀!");
// 		return;
// 	}
// 	var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
// 	if ($("#shardingNum").val() == '') {
// 		alert("请输入ShardingDB数量!");
// 		return;
// 	} else if ($("#shardingNum").val() != ''
// 			&& !r.test($("#shardingNum").val())) {
// 		alert("ShardingDB数量请输入正整数!");
// 		return;
// 	}
// 	// 先销毁表格
// 	$('#shardingdbTable').bootstrapTable('destroy');
// 	// 初始化表格,动态从服务器加载数据
// 	$("#shardingdbTable").bootstrapTable({
// 		method : "get", // 使用get请求到服务器获取数据
// 		url : 'ShardingDBServlet?type=shardingdbSave', // 获取数据的Servlet地址
// 		striped : true, // 表格显示条纹
// 		pagination : true, // 启动分页
// 		pageSize : 50, // 每页显示的记录数
// 		pageNumber : 1, // 当前第几页
// 		pageList : [ 50, 100 ], // 记录数可选列表
// 		search : false, // 是否启用查询
// 		showColumns : false, // 显示下拉框勾选要显示的列
// 		showRefresh : false, // 显示刷新按钮
// 		sidePagination : "server", // 表示服务端请求
// 		// 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
// 		// 设置为limit可以获取limit, offset, search, sort, order
// 		queryParamsType : "undefined",
// 		queryParams : function queryParams(params) { // 设置查询参数
// 			var param = {
// 				pageNumber : params.pageNumber,
// 				pageSize : params.pageSize,
// 				shardingPrefix : $("#shardingPrefix").val(),
// 				shardingNum : $("#shardingNum").val(),
// 				sourceDBID : $("#addSourceDBID").val(),
// 			};
// 			return param;
// 		},
// 		rowAttributes : function(row, index) {
// 			if (index >= 0) {
// 				$("#shardingdbSaveBtn").hide();
// 				$("#shardingdbBackBtn").show();
// 			}
// 			if (row.isValid == 'T') {
// 				row.isValid = "有效";
// 			} else {
// 				row.isValid = "无效";
// 			}
// 		}
// 	});
// }
// // 删除db源对应的shardingdb
// function shardingdbDel() {
// 	// alert($("#addSourceDBID").val());
// 	if (confirm('确定要删除[' + $("#titleDiv").html() + ']吗?')) {
// 		var url = "ShardingDBServlet";
// 		var params = {
// 			"type" : "shardingdbDel",
// 			"sourceDBID" : $("#addSourceDBID").val(),
// 		};
// 		slAjax("mainDiv", url, params, function(data, textStatus, request) {
// 			if (data != '' || data.length != 0 || data.status == 'succ') {
// 				alert(data.msg);
// 				window.location.href = "shardingdb.html?r=" + Math.random();
// 			}
// 		}, true);
// 	}
// }

function addShardingSource() {
	$("#isValidDiv").hide();
	$("#shardingdbShowDiv").hide();
	$("#shardingSourceDBInfoAddDiv").show();
	$("#updateType").val("add");
}


// 校验是否已经存在db源
$("#sourceDB").blur(function() {
	var sourceDBid = $("#shardingSourceID").val();
	if (sourceDBid == "") {
		var url = "ShardingDBServlet";
		var params = {
			"type" : "checkShardingSourceDB",
			"sourceDB" : $("#sourceDB").val(),
		};
		$("#srcDB").empty();
		slAjax("mainDiv", url, params, function(data, textStatus, request) {
			if ((data != '' || data.length != 0 || data.status == 'succ')) {
				alert(data.msg);
			} else {
				alert(data.msg);
			}
		}, true);
	}
});

function saveShardingSourceDBInfo() {
	if ($("#sourceDB").val() == '') {
		alert("ShardingDB源名称不能为空!");
		return;
	}
	if ($("#batchNum").val() == '') {
		alert("batchNum不能为空!");
		return;
	} else {
		var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
		if (!r.test($("#batchNum").val())) {
			alert("batchNum必须为正整数!");
			return;
		}
	}
	if ($("#comment").val() == '') {
		alert("Comment不能为空!");
		return;
	}
	var url = "ShardingDBServlet";
	var params = {
		"type" : "saveShardingSourceDBInfo",
		"shardingSourceID" : $("#shardingSourceID").val(),
		"sourceType" : $("#sourceType").val(),
		"sourceDB" : $("#sourceDB").val(),
		"batchNum" : $("#batchNum").val(),
		"comment" : $("#comment").val(),
		"isValid" : $("#isValid").val(),
		"updateType" : $("#updateType").val(),
	};
	$("#srcDB").empty();
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0 || data.status == 'succ')) {
			alert(data.msg);
			$("#updateType").val("");
			window.location.href = "shardingdb.html?r=" + Math.random();
		} else {
			alert(data.msg);
		}
	}, true);

}
function saveShardingSourceDBInfoBack() {
	window.location.href = "shardingdb.html?r=" + Math.random();
}
function shardingdbSourceUpdate(id) {
	$("#isValidDiv").show();
	$("#titleDivForadd").html("编辑ShardingDB源");
	$("#updateType").val("update");
	$("#shardingSourceID").val(id);
	$("#shardingdbShowDiv").hide();
	$("#shardingSourceDBInfoAddDiv").show();
	var url = "ShardingDBServlet";
	var params = {
		"type" : "getShardingSourceDBByID",
		"shardingSourceID" : $("#shardingSourceID").val(),
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if (data != '' || data.length != 0 || data.status == 'succ') {
			$("#sourceType").val(data.sourceType);
			$("#sourceDB").val(data.sourceDB);
			$("#batchNum").val(data.batchNum);
			$("#comment").val(data.comment);
			$("#isValid").val(data.isValid);
		}
	}, true);

}
// function shardingdbSourceDel(sourceDB, id) {
// 	if (confirm('确定要删除[' + sourceDB + ']吗?')) {
// 		var url = "ShardingDBServlet";
// 		var params = {
// 			"type" : "shardingdbSourceDel",
// 			"shardingSourceID" : id,
// 		};
// 		slAjax("mainDiv", url, params, function(data, textStatus, request) {
// 			if (data != '' || data.length != 0 || data.status == 'succ') {
// 				alert(data.msg);
// 				window.location.href = "shardingdb.html?r=" + Math.random();
// 			}
// 		}, true);
// 	}
// }
