$(function() {
	getUserPermission();
	getLoginName();
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
				$("#shardingtbAddDiv").hide();
				$("#shardingSourceTBInfoAddDiv").hide();
				$("#copyshardingScriptDiv").hide();
				getSourceDBInfo();
				getSourceTBInfo();
				TBinitTable();
			}else{
				$("#shardingtbAddDiv").hide();
				$("#shardingSourceTBInfoAddDiv").hide();
				$("#copyshardingScriptDiv").hide();
				$("#shardingtbShowDiv").hide();
			}
		} else {
			$("#shardingtbAddDiv").hide();
			$("#shardingSourceTBInfoAddDiv").hide();
			$("#copyshardingScriptDiv").hide();
			$("#shardingtbShowDiv").hide();
		}
	}, true);
};

function getLoginName() {
	var url = "ShardingLoginServlet";
	var params = {
		"type" : "getUserInfo"
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0)) {
			//console.log(data);
			$("#loginname").val(data.username);
		}
	}, true);
}

function getSourceDBInfo() {
	var url = "ShardingDBServlet";
	var params = {
		"type" : "getSourceDBInfo"
	};
	$("#srcDB").empty();
	slAjax("mainDiv", url, params,
			function(data, textStatus, request) {
				if ((data != '' || data.length != 0)) {
					$("#srcDB").append(
							'<option value="">=====All=====</option>');
					var i = data.length, j;
					var tempExchangValID, tempExchangValDB;
					while (i > 0) {
						for (j = 0; j < i - 1; j++) {
							if (data[j].sourceDB
									.localeCompare(data[j + 1].sourceDB) > 0) {
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
								'<option value=' + data[i].id + '>'
										+ data[i].sourceDB + '</option>');
					}
				}
			}, true);
}
function getSourceTBInfo() {
	var url = "ShardingTBServlet";
	var params = {
		"type" : "getSourceTBInfo"
	};
	$("#srcTB").empty();
	slAjax("mainDiv", url, params,
			function(data, textStatus, request) {
				if ((data != '' || data.length != 0)) {
					$("#srcTB").append(
							'<option value="">=====All=====</option>');
					var i = data.length, j;
					var tempExchangValID, tempExchangValTB;
					while (i > 0) {
						for (j = 0; j < i - 1; j++) {
							if (data[j].sourceTB
									.localeCompare(data[j + 1].sourceTB) > 0) {
								tempExchangValID = data[j].id;
								tempExchangValTB = data[j].sourceTB;
								data[j].id = data[j + 1].id;
								data[j].sourceTB = data[j + 1].sourceTB;
								data[j + 1].id = tempExchangValID;
								data[j + 1].sourceTB = tempExchangValTB;
							}
						}
						i--;
					}
					for ( var i in data) {
						$("#srcTB").append(
								'<option value=' + data[i].id + '>'
										+ data[i].sourceTB + '</option>');
					}
				}
			}, true);
}
function TBinitTable() {
	// 先销毁表格
	$('#cusTable').bootstrapTable('destroy');
	// 初始化表格,动态从服务器加载数据
	$("#cusTable").bootstrapTable(
	{
		method : "get", // 使用get请求到服务器获取数据
		url : 'ShardingSourceGroupServlet?type=getShardingSourceGroupList', // 获取数据的Servlet地址
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
		// sortable: true, //是否启用排序
		// sortName : 'ID',//初始化的时候排序的字段
		// sortOrder: "desc", //排序方式
		queryParams : function queryParams(params) { // 设置查询参数
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				// city : $("#city").val(),//
				// $("#cityName").val()
				srcDB : $("#srcDB").val(),
				srcTB : $("#srcTB").val(),
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
			row.operate = '<button type="button" class="btn btn-link" onclick=shardingtbSourceUpdate("'
					+ row.id
					+ '","'
					+ row.sourceDB
					+ '")>编辑</button>&nbsp;'
					+
					// '<button type="button" class="btn
					// btn-link"
					// onclick=shardingtbSourceDel("'+row.sourceDB+'","'+row.id+'")>删除</button>&nbsp;'+
					'<button type="button" class="btn btn-link" onclick=shardingtbAdd("'
					+ row.sourceTB
					+ '","'
					+ row.sourceDBID
					+ '","'
					+ row.id
					+ '")>ShardingTB配置</button>'
					+ '<button type="button" class="btn btn-link" onclick=copyshardingScriptDiv("'
					+ row.id + '","' + row.sourceTB + '","' + row.targetDB + '","' + row.targetTB + '")>生成Sharding脚本</button>';
		}
	});
}

function copyshardingScriptDiv(id, sourceTB, targetDB, targetTB) {
	getCreateTableScript(id, targetDB, targetTB);
	$("#titleDivForCode").html('生成ShardingTB源（' + sourceTB + '）取数脚本');
	$("#shardingtbShowDiv").hide();
	$("#copyshardingScriptDiv").show();
	var url = "ShardingSourceGroupServlet";
	var params = {
		"type" : "getShardingSourceGroupByID",
		"shardingSourceTBID" : id,
	};
	slAjax(
			"mainDiv",
			url,
			params,
			function(data, textStatus, request) {
				if (data != '' || data.length != 0 || data.status == 'succ') {
					function p(s) {
						return s < 10 ? '0' + s : s;
					}
					var myDate = new Date();
					// 获取当前年
					var year = myDate.getFullYear();
					// 获取当前月
					var month = myDate.getMonth() + 1;
					// 获取当前日
					var date = myDate.getDate();
					var ToDay = year + '-' + p(month) + "-" + p(date);
					$("#shardingScript").text(
"#!/bin/bash"
		+ '\n'
		+ "###############################################################################################注释的代码\n"
		+ ":<<EOF\n\n"
		+ "运行类型 ：日跑\n"
		+ "参数 : batchNum,ShardSourceTB,SelectCOLS,SQLSERVERorMYSQL,Condition,ErrorRetryCondition,basesize,basepath,CurrentDate\n"
		+ "功能描述: "
		+ data.comment
		+ ":"
		+ data.sourceDB
		+ "."
		+ data.sourceTB
		+ "，从N个sharding并行取数\n"
		+ "注意 ：\n"
		+ "输入表 : 源头N个sharding表\n"
		+ "输出表 ：N个对应的sharding目的表"
		+ data.targetDB
		+ "."
		+ data.targetTB
		+ '_base\n'
		+ "修改历史 ： 修改人             修改时间           主要改动说明\n"
		+ "1\t\t"+$("#loginname").val()+"\t\t"+ToDay+"\t\t"+"1、create\n\n"
		+ "EOF\n"
		+ "################################################################################################注释的代码\n\n"
		+ "##############################Sharding取数流程#####################################\n\n"
		+ "########################基础参数》》\n"
		+ "#取数批次定义，每批datax任务保持在30个以下，过多并发，容易出系统问题\n"
		+ "batchNum="
		+ data.batchNum
		+ '\n\n'
		+ "#Sharding的表名\n"
		+ "ShardSourceTB='"
		+ data.sourceTB
		+ "'\n\n"
		+ "#源表的字段列表\n"
		+ "SelectCOLS=\""
		+ data.columns
		+ "\"\n\n"
		+ "#数据源类型：SQLSERVER/MYSQL\n"
		+ "SQLSERVERorMYSQL='"
		+ data.sourceType
		+ "'\n\n"
		+ "######取数条件》》\n"
		+ data.incrCondition
		+ "\n"
		+ "######《《取数条件\n\n"
		+ "#错误重跑时可选择使用的条件，在输出日志中找到发生错误的ShardID，填入下面的条件\n"
		+ "#ErrorRetryCondition=\" AND ShardID IN()\"\n"
		+ "ErrorRetryCondition=\"\"\n"
		+ "#########################《《基础参数\n\n"
		+ "sh sharding_shell_batchcontrol.sh \"${batchNum}\" \"${ShardSourceTB}\" \"${SelectCOLS}\" \"${SQLSERVERorMYSQL}\" \"${Condition}\" \"${ErrorRetryCondition}\"\n\n\n"
		+ "###############################数据校验流程#######################################\n"
		+ "set -e\n\n"
		+ "########################校验参数》》\n"
		+ "#文件大小标准(1M=1024K)\n"
		+ "basesize="
		+ data.thresholdValue
		+ '\n'
		+ "#hdfs路径\n"
		+ "basepath='hdfs://ns/user/hive/warehouse'\n"
		+ "#校验数据时间\n"
		+ "CurrentDate=$(date +\"%Y-%m-%d\")\n"
		+ "#########################《《校验参数\n\n"
		+ "sh datacheck_sharding_v4.sh ${ShardSourceTB} ${basesize} ${basepath} ${CurrentDate}\n");
					var sourceStrArray = $.trim(data.columns).split(",");
					var targetStr = "";
					for (var i = 0; i < sourceStrArray.length; i++) {
						targetStr += "\ta."+sourceStrArray[i]+",\n"
			        }
					targetStr += "\ta.shardid";
					
					var pKColumnsArray = $.trim(data.pKColumns).split(",");
					var targetCondition = "";
					var targetConditionJ = "";
					targetConditionJ = pKColumnsArray[0];
					targetConditionJ = "b."+targetConditionJ+" IS NULL"
					for (var i = 0; i < pKColumnsArray.length; i++) {
						targetCondition += "a."+pKColumnsArray[i]+"=b."+pKColumnsArray[i]+" AND "
			        }
					targetCondition = targetCondition.substring(0,targetCondition.length-4);
					$("#shardingCombScript").text(
"#!/bin/bash\n"
+"###############################################################################################注释的代码\n"
+":<<EOF\n\n"
+"运行类型：日跑\n"
+"参数:\n"
+"功能描述：把所有sharding的表合并到一张表\n"
+"注意：\n"
+"输入表 ："+data.targetDB+"."+data.targetTB+"(历史分区)\n"
+"\t\t\t"+data.targetDB+"."+data.targetTB+"_base\n"
+"输出表："+data.targetDB+"."+data.targetTB
+"\n修改历史 ：\t修改人\t\t修改时间\t\t主要改动说明\n"
+"1\t\t"+$("#loginname").val()+"\t\t"+ToDay+"\t\t"+"1、create\n\n"
+"EOF\n"
+"################################################################################################注释的代码\n"
+"hive -v -e\"\n"
+"INSERT OVERWRITE TABLE "+data.targetDB+"."+data.targetTB+"\n"
+"partition(d = '${zdt.format(\"yyyy-MM-dd\")}')\n"
+"SELECT\n"
+targetStr
+"\nFROM "+data.targetDB+"."+data.targetTB+"_base AS a\n"
+"WHERE a.d='${zdt.format(\"yyyy-MM-dd\")}'\n\n"
+"UNION ALL\n\n"
+"SELECT\n"
+targetStr
+"\nFROM "+data.targetDB+"."+data.targetTB+" AS a\n"
+"LEFT JOIN "+data.targetDB+"."+data.targetTB+"_base AS b\n"
+"\t\t\t\t\t\tON "+targetCondition
+"\n\t\t\t\t\t\t\tAND a.shardid = b.shardid"
+"\n\t\t\t\t\t\t\tAND b.d = '${zdt.format(\"yyyy-MM-dd\")}'\n"
+"WHERE "+targetConditionJ
+"\n\t\tAND a.d = '${zdt.addDay(-1).format(\"yyyy-MM-dd\")}'\n"
+"\"\n");
	}
}, true);
}


function getCreateTableScript(id, targetDB, targetTB){
	var url = "OSGServlet";
	var params = {
		"type" : "getCreateTableScript",
		"shardingSourceTBID" : id,
		"targetDB" : targetDB,
		"targetTB" : targetTB
	};
	slAjax("mainDiv", url, params,
		function(data, textStatus, request) {
			if ((data != '' || data.length != 0)) {
				$("#createMidtargettbScript").text(data.createMidtargettbScript);
				$("#createtargettbScript").text(data.createtargettbScript);
			}
		}, true);
}

// function shardingtbAdd(sourceTB, id) {
// 	shardingtbPreView(id);
// 	$("#shardingPrefix").val('');
// 	$("#shardingNum").val('');
// 	$("#titleDiv").html('添加ShardingTB源（' + sourceTB + '）对应ShardingTB');
// 	$("#shardingtbAddDiv").show();
// 	$("#shardingtbShowDiv").hide();
// 	$("#addSourceTBID").val(id);
// 	$("#shardingtbBackBtn").hide();
//
// }
function shardingtbAdd(sourceTB, sourceDBID, id) {
	location.href = encodeURI("shardingtbselected.html?id=" + id + "&sourceTB=" + sourceTB + "&sourceDBID=" + sourceDBID);
}

// function shardingtbPreView(id) {
// 	if (id == -1 && $("#shardingPrefix").val() == '') {
// 		alert("请输入要生成的shardingtb前缀!");
// 		return;
// 	}
// 	var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
// 	if (id == -1 && $("#shardingNum").val() == '') {
// 		alert("请输入ShardingTB数量!");
// 		return;
// 	} else if ($("#shardingNum").val() != ''
// 			&& !r.test($("#shardingNum").val())) {
// 		alert("ShardingTB数量请输入为正整数!");
// 		return;
// 	}
// 	// 先销毁表格
// 	$('#shardingtbTable').bootstrapTable('destroy');
// 	// 初始化表格,动态从服务器加载数据
// 	$("#shardingtbTable").bootstrapTable({
// 		method : "get", // 使用get请求到服务器获取数据
// 		url : 'ShardingTBServlet?type=shardingtbPreView', // 获取数据的Servlet地址
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
// 				sourceTBID : (id == -1 ? $("#addSourceTBID").val() : id),
// 			};
// 			return param;
// 		},
// 		rowAttributes : function(row, index) {
// 			if (index >= 0) {
// 				$("#shardingtbSaveBtn").show();
// 				$("#shardingtbBackBtn").hide();
// 			}
// 			if (row.isValid == 'T') {
// 				row.isValid = "有效";
// 			} else {
// 				row.isValid = "无效";
// 			}
// 		}
// 	});
// }
// // save shardingtb
// function shardingtbSave() {
// 	if ($("#shardingPrefix").val() == '') {
// 		alert("请输入要生成的shardingtb前缀!");
// 		return;
// 	}
// 	var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
// 	if ($("#shardingNum").val() == '') {
// 		alert("请输入ShardingTB数量!");
// 		return;
// 	} else if ($("#shardingNum").val() != ''
// 			&& !r.test($("#shardingNum").val())) {
// 		alert("ShardingTB数量请输入为正整数!");
// 		return;
// 	}
// 	// 先销毁表格
// 	$('#shardingtbTable').bootstrapTable('destroy');
// 	// 初始化表格,动态从服务器加载数据
// 	$("#shardingtbTable").bootstrapTable({
// 		method : "get", // 使用get请求到服务器获取数据
// 		url : 'ShardingTBServlet?type=shardingtbSave', // 获取数据的Servlet地址
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
// 				sourceTBID : $("#addSourceTBID").val(),
// 			};
// 			return param;
// 		},
// 		rowAttributes : function(row, index) {
// 			if (index >= 0) {
// 				$("#shardingtbSaveBtn").hide();
// 				$("#shardingtbBackBtn").show();
// 			}
// 			if (row.isValid == 'T') {
// 				row.isValid = "有效";
// 			} else {
// 				row.isValid = "无效";
// 			}
// 		}
// 	});
// }
// // 删除tb源对应的shardingtb
// function shardingtbDel() {
// 	// alert($("#addSourceTBID").val());
// 	if (confirm('确定要删除[' + $("#titleDiv").html() + ']吗?')) {
// 		var url = "ShardingTBServlet";
// 		var params = {
// 			"type" : "shardingtbDel",
// 			"sourceTBID" : $("#addSourceTBID").val(),
// 		};
// 		slAjax("mainDiv", url, params, function(data, textStatus, request) {
// 			if (data != '' || data.length != 0 || data.status == 'succ') {
// 				alert(data.msg);
// 				window.location.href = "shardingtb.html?r=" + Math.random();
// 			}
// 		}, true);
// 	}
// }

// 添加ShardingSourceTB
function addShardingSourceTB() {
	if ($("#srcDB").val() == '') {
		alert("必须选择ShardingDB源!");
		return;
	}
	$("#isValidDiv").hide();
	$("#shardingtbShowDiv").hide();
	$("#titleDivForAdd").html(
			'添加ShardingDB源（' + $("#srcDB").find("option:selected").text()
					+ '）对应ShardingTB源');
	$("#shardingSourceTBInfoAddDiv").show();
	$("#updateType").val("add");
	$("#shardingSourceDBID").val($("#srcDB").val());
}

// 校验是否已经存在tb源
$("#sourceTB").blur(function() {
	var updateType = $("#updateType").val();
	if (updateType == "add") {
		var url = "ShardingTBServlet";
		var params = {
			"type" : "checkShardingSourceTB",
			"sourceTB" : $("#sourceTB").val(),
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

function saveShardingSourceTBInfo() {
	if ($("#sourceTB").val() == '') {
		alert("ShardingTB源名称不能为空!");
		return;
	}
	// if ($("#columns").val() == '') {
	// 	alert("字段列表不能为空!");
	// 	return;
	// }
	// if ($("#pKColumns").val() == '') {
	// 	alert("字段列表不能为空!");
	// 	return;
	// }
	if ($("#incrCondition").val() == '') {
		alert("取数条件不能为空!");
		return;
	}
	if ($("#thresholdValue").val() == '') {
		alert("校验阀值（K）不能为空!");
		return;
	} else {
		var r = /^[0-9]*[1-9][0-9]*$/;// 正整数
		if (!r.test($("#thresholdValue").val())) {
			alert("校验阀值（K）必须为正整数!");
			return;
		}
	}
	if ($("#targetDB").val() == '') {
		alert("HIVE目标库不能为空!");
		return;
	}
	if ($("#targetTB").val() == '') {
		alert("HIVE目标表不能为空!");
		return;
	}
	if ($("#comment").val() == '') {
		alert("备注不能为空!");
		return;
	}
	var url = "ShardingTBServlet";
	var params = {
		"type" : "saveShardingSourceTBInfo",
		"shardingSourceTBID" : $("#shardingSourceTBID").val(),
		"sourceDBID" : $("#shardingSourceDBID").val(),
		"sourceTB" : $("#sourceTB").val(),
		// "columns" : "",
		// "pKColumns" : "",
		"incrCondition" : $("#incrCondition").val(),
		"thresholdValue" : $("#thresholdValue").val(),
		"targetDB" : $("#targetDB").val(),
		"targetTB" : $("#targetTB").val(),
		"comment" : $("#comment").val(),
		"isValid" : $("#isValid").val(),
		"updateType" : $("#updateType").val(),
	};
	$("#srcTB").empty();
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0 || data.status == 'succ')) {
			alert(data.msg);
			$("#updateType").val("");
			window.location.href = "shardingtb.html?r=" + Math.random();
		} else {
			alert(data.msg);
		}
	}, true);

}
function saveShardingSourceTBInfoBack() {
	window.location.href = "shardingtb.html?r=" + Math.random();
}

// 编辑shardingtbSource
function shardingtbSourceUpdate(id, sourceDB) {
	$("#isValidDiv").show();
	$("#updateType").val("update");
	$("#shardingSourceTBID").val(id);
	$("#titleDivForAdd").html('编辑ShardingDB源（' + sourceDB + '）对应ShardingTB源');
	$("#shardingtbShowDiv").hide();
	$("#shardingSourceTBInfoAddDiv").show();
	var url = "ShardingTBServlet";
	var params = {
		"type" : "getShardingSourceTBByID",
		"shardingSourceTBID" : $("#shardingSourceTBID").val(),
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if (data != '' || data.length != 0 || data.status == 'succ') {
			$("#sourceDBID").val(data.sourceDBID);
			$("#sourceTB").val(data.sourceTB);
			// $("#columns").val(data.columns);
			// $("#pKColumns").val(data.pKColumns);
			$("#incrCondition").val(data.incrCondition);
			$("#thresholdValue").val(data.thresholdValue);
			$("#targetDB").val(data.targetDB);
			$("#targetTB").val(data.targetTB);
			$("#comment").val(data.comment);
			$("#isValid").val(data.isValid);
		}
	}, true);

}
// function shardingtbSourceDel(sourceTB, id) {
// 	if (confirm('确定要删除[' + sourceTB + ']吗?')) {
// 		var url = "ShardingTBServlet";
// 		var params = {
// 			"type" : "shardingtbSourceDel",
// 			"shardingSourceTBID" : id,
// 		};
// 		slAjax("mainDiv", url, params, function(data, textStatus, request) {
// 			if (data != '' || data.length != 0 || data.status == 'succ') {
// 				alert(data.msg);
// 				window.location.href = "shardingtb.html?r=" + Math.random();
// 			}
// 		}, true);
// 	}
// }

function copyShardingScriptInfo(id) {
	if (id == 1){
		var Script = document.getElementById("shardingScript");
	}else if (id == -1){
		var Script = document.getElementById("shardingCombScript");
	}else if (id == 2){
		var Script = document.getElementById("createtargettbScript");
	}else if (id == 3){
		var Script = document.getElementById("createMidtargettbScript");
	}
	Script.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
	alert("已复制好，可贴粘。");
}

function opendataxcreatetable(){
	window.open("http://dataquality.ops.ctripcorp.com/view/home/createtable.page")
}
