$(function() {
	var sourceType = getParam("sourceType");
	var id = getParam("id");
	var sourceDB = getParam("sourceDB");
	getShardingDBInfoList(sourceType);
    shardingdbPreView(id);
	$("#addSourceDBID").val(id);
	$("#shardingdbBackBtn").hide();
	$("#titleDiv").html('添加ShardingDB源（' + sourceDB + '）对应ShardingDB');
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

function getShardingDBInfoList(sourceType) {
    var url = "DataxTitanDBMappingServlet";
    var params = {
        "type" : "getShardingDBInfoList",
        "dbType" : sourceType,
    };
    slAjax("mainDiv", url, params, function(data, textStatus, request) {
        if (data != '' || data.length != 0) {
            var txt='';
            for ( var i in data) {
                txt += '<option value=' + data[i].dbName + '>' + data[i].dbName +'</option>';
            }
            //console.log(txt);
            document.getElementById('id_select').innerHTML=txt;
            $('.selectpicker').selectpicker('refresh');
        }
    }, true);
}



function shardingdbPreView(id) {
    var ShardingDBArray = $("#id_select").val();
    if (id == -1 && ShardingDBArray == null) {
        alert("请选择相应ShardingDB！");
        return;
    }
    var ALLShardingDB = 0;
    if (id == -1){
        ALLShardingDB = ShardingDBArray.join(",");
    }

    // 先销毁表格
	$('#shardingdbTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#shardingdbTable").bootstrapTable({
		method : "get", // 使用get请求到服务器获取数据
		url : 'DataxTitanDBMappingServlet?type=shardingdbPreView', // 获取数据的Servlet地址
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
                ALLShardingDB : ALLShardingDB,
				sourceDBID : (id == -1 ? $("#addSourceDBID").val() : id),
			};
			//console.log(param);
			return param;
		},
		rowAttributes : function(row, index) {
			if (index >= 0) {
				$("#shardingdbSaveBtn").show();
				$("#shardingdbBackBtn").hide();
			}
		}
	});
}
// save shardingdb
function shardingdbSave() {
    var ShardingDBArray = $("#id_select").val();
    if (ShardingDBArray == null) {
        alert("请选择相应ShardingDB！");
        return;
    }
    var ALLShardingDB = ShardingDBArray.join(",");

	// 先销毁表格
	$('#shardingdbTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#shardingdbTable").bootstrapTable({
		method : "get", // 使用get请求到服务器获取数据
		url : 'DataxTitanDBMappingServlet?type=shardingdbSave', // 获取数据的Servlet地址
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
                ALLShardingDB : ALLShardingDB,
                sourceDBID : $("#addSourceDBID").val(),
			};
			return param;
		},
		rowAttributes : function(row, index) {
			if (index >= 0) {
				$("#shardingdbSaveBtn").hide();
				$("#shardingdbBackBtn").show();
			}
		}
	});
}
// 删除db源对应的shardingdb
function shardingdbDel() {
	// alert($("#addSourceDBID").val());
	if (confirm('确定要删除[' + $("#titleDiv").html() + ']吗?')) {
		var url = "ShardingDBServlet";
		var params = {
			"type" : "shardingdbDel",
			"sourceDBID" : $("#addSourceDBID").val(),
		};
		slAjax("mainDiv", url, params, function(data, textStatus, request) {
			if (data != '' || data.length != 0 || data.status == 'succ') {
				alert(data.msg);
				//window.location.href = "shardingdb.html?r=" + Math.random();
                shardingdbPreView($("#addSourceDBID").val());
			}
		}, true);
	}
}

function saveShardingSourceDBInfoBack() {
    window.location.href = "shardingdb.html?r=" + Math.random();
}
