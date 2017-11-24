$(function() {
	var id = getParam("id");
	var sourceTB = getParam("sourceTB");
	var sourceDBID = getParam("sourceDBID");
	getOSGShardingTBInfoList(sourceDBID);
    shardingtbPreView(id);
	$("#addSourceTBID").val(id);
	$("#shardingtbBackBtn").hide();
	$("#titleDiv").html('添加ShardingTB源（' + sourceTB + '）对应ShardingTB');
});
/**
 * 获取指定的URL参数值
 * URL:http://www.quwan.com/index?name=tyler
 * 参数：paramName URL参数
 * 调用方法:getParam("name")
 * 返回值:tyler
 */
function getParam(paramName) {
	paramValue = "", isFound = !1;
	if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
		arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
		while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
	}
	return paramValue == "" && (paramValue = null), paramValue
}
//获取对应ShardingDB的一张配置表
// function getShardingDBInfoTopOne(sourceDBID) {
// 	$("#shardingDBInfoTopOne").val("");
// 	var url = "ShardingDBServlet";
// 	var params = {
// 		"type" : "getShardingDBInfoTopOne",
// 		"sourceDBID" : sourceDBID
// 	};
// 	slAjax("mainDiv", url, params, function(data, textStatus, request) {
// 		if (data != '' || data.length != 0) {
// 			//console.log(data.shardingDB);
// 			getShardingDBInfoByDBName(data.shardingDB)
// 		}
// 	}, true);
// }
//
// function getShardingDBInfoByDBName(dbName) {
//     var url = "DataxTitanDBMappingServlet";
//     var params = {
//         "type" : "getShardingDBInfoByDBName",
//         "dbName" : dbName,
//     };
//     slAjax("mainDiv", url, params, function(data, textStatus, request) {
//         if (data != '' || data.length != 0) {
// 			getOSGShardingTBInfoList(data.dbName,data.dbType.toLowerCase(),data.server);
//         }
//     }, true);
// }
function getOSGShardingTBInfoList(sourceDBID) {
	var url = "OSGServlet";
	var params = {
		"type" : "getOSGShardingTBInfoList",
		"sourceDBID" : sourceDBID
	};
	//console.log(params);
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if (data != '' || data.length != 0) {
			var txt='';
			for ( var i in data) {
				txt += '<option value='+"{\"table_name\":\"" + data[i].table_name + "\",\"machine_name\":\"" + data[i].machine_name + "\",\"db_id\":\"" + data[i].db_id + "\",\"object_id\":\"" + data[i].object_id + "\",\"db_name\":\"" + data[i].db_name + "\"}" + '>' + data[i].table_name + '</option>';
			}
			//console.log(txt);
			document.getElementById('id_select').innerHTML=txt;
			$('.selectpicker').selectpicker('refresh');
		}
	}, true);
}



function shardingtbPreView(id) {
    var ShardingTBArray = $("#id_select").val();
    if (id == -1 && ShardingTBArray == null) {
        alert("请选择相应ShardingDB！");
        return;
    }
    var ALLShardingTB = 0;
    if (id == -1){
        ALLShardingTB = ShardingTBArray.join(",");
		//alert($("#addSourceTBID").val());
    }

    // 先销毁表格
	$('#shardingtbTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#shardingtbTable").bootstrapTable({
		method : "get", // 使用get请求到服务器获取数据
		url : 'ShardingTBServlet?type=shardingtbPreView', // 获取数据的Servlet地址
		striped : true, // 表格显示条纹
		pagination : false, // 启动分页
		pageSize : 50, // 每页显示的记录数
		pageNumber : 1, // 当前第几页
		pageList : [ 50, 100 ], // 记录数可选列表
		search : false, // 是否启用查询
		showColumns : false, // 显示下拉框勾选要显示的列
		showRefresh : false, // 显示刷新按钮
		sidePagination : "server", // 表示服务端请求
		// 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
		// 设置为limit可以获取limit, offset, search, sort, order
		queryParamsType : "undefined",
		queryParams : function queryParams(params) { // 设置查询参数
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
                ALLShardingTB : ALLShardingTB,
				sourceTBID : (id == -1 ? $("#addSourceTBID").val() : id),
			};
			//console.log(param);
			return param;
		},
		rowAttributes : function(row, index) {
			if (index >= 0) {
				$("#shardingtbSaveBtn").show();
				$("#shardingtbBackBtn").hide();
			}
		}
	});
}

function shardingtbSaveUpdateColumns(){
	var ShardingTBArray = $("#id_select").val();
	if (ShardingTBArray == null) {
		alert("请选择相应ShardingDB！");
		return;
	}
	var ALLShardingTB = ShardingTBArray.join(",");
	console.log(ALLShardingTB);
	// 先销毁表格
	$('#shardingtbTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#shardingtbTable").bootstrapTable({
		method : "get", // 使用get请求到服务器获取数据
		url : 'PubliceDBServlet?type=shardingtbSaveUpdateColumns', // 获取数据的Servlet地址
		striped : true, // 表格显示条纹
		pagination : false, // 启动分页
		pageSize : 50, // 每页显示的记录数
		pageNumber : 1, // 当前第几页
		pageList : [ 50, 100 ], // 记录数可选列表
		search : false, // 是否启用查询
		showColumns : false, // 显示下拉框勾选要显示的列
		showRefresh : false, // 显示刷新按钮
		sidePagination : "server", // 表示服务端请求
		// 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
		// 设置为limit可以获取limit, offset, search, sort, order
		queryParamsType : "undefined",
		queryParams : function queryParams(params) { // 设置查询参数
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				ALLShardingTB : ALLShardingTB,
				sourceTBID : $("#addSourceTBID").val(),
			};
			console.log(param);
			return param;
		},
		rowAttributes : function(row, index) {
			if (index >= 0) {
				$("#shardingtbSaveBtn").hide();
				$("#shardingtbBackBtn").show();
			}
		}
	});
}
// save shardingtb
// function shardingtbSave() {
//     var ShardingTBArray = $("#id_select").val();
//     if (ShardingTBArray == null) {
//         alert("请选择相应ShardingDB！");
//         return;
//     }
//     var ALLShardingTB = ShardingTBArray.join(",");
//
// 	// 先销毁表格
// 	$('#shardingdbTable').bootstrapTable('destroy');
// 	// 初始化表格,动态从服务器加载数据
// 	$("#shardingdbTable").bootstrapTable({
// 		method : "get", // 使用get请求到服务器获取数据
// 		url : 'ShardingTBServlet?type=shardingtbSave', // 获取数据的Servlet地址
// 		striped : true, // 表格显示条纹
// 		pagination : false, // 启动分页
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
//                 ALLShardingTB : ALLShardingTB,
//                 sourceTBID : (id == -1 ? $("#addSourceTBID").val() : id),
// 			};
// 			console.log(param);
// 			return param;
// 		},
// 		rowAttributes : function(row, index) {
// 			if (index >= 0) {
// 				$("#shardingtbSaveBtn").hide();
// 				$("#shardingtbBackBtn").show();
// 			}
// 		}
// 	});
// }
// 删除db源对应的shardingtb
function shardingtbDel() {
	// alert($("#addSourceDBID").val());
	if (confirm('确定要删除[' + $("#titleDiv").html() + ']吗?')) {
		var url = "ShardingTBServlet";
		var params = {
			"type" : "shardingtbDel",
			"sourceTBID" : $("#addSourceTBID").val(),
		};
		slAjax("mainDiv", url, params, function(data, textStatus, request) {
			if (data != '' || data.length != 0 || data.status == 'succ') {
				alert(data.msg);
				//window.location.href = "shardingdb.html?r=" + Math.random();
                shardingtbPreView($("#addSourceTBID").val());
			}
		}, true);
	}
}

function saveShardingSourceTBInfoBack() {
    window.location.href = "shardingtb.html?r=" + Math.random();
}
