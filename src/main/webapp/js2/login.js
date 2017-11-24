$(function() {
	getLoginName();
});

function getLoginName() {
	var url = "ShardingLoginServlet";
	var params = {
		"type" : "getUserInfo"
	};
	slAjax("mainDiv", url, params, function(data, textStatus, request) {
		if ((data != '' || data.length != 0)) {
			//console.log(data);
			$("#loginname").text(data.username);
		}
	}, true);
}