<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>测试</title>
</head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<body>
<%String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<table>
	<tr>
		<td>测试方法:</td>
		<td>
			<select id="changeUrlSelect" onchange="javascript:changeUrl();">
				<option value="0">请选择</option>
				<option value="1">sendMessage私聊</option>
				<option value="101">sendMessage群聊</option>
				<option value="2">createMUC创建修改会议</option>
				<option value="3">exitFromMUC退出某个会议</option>
				<option value="4">invite2MUC将好友加入到会议中</option>
				<option value="5">getListMUC获取我的会议列表</option>
				<option value="6">kickFromMUC将某个用户踢出会议 </option>
				<option value="7">getMUCDetail获取会议详情</option>
				<option value="8">getMUCMessage获取会议聊天记录</option>
				<option value="9">getChatMessage获取私聊聊天记录</option>
				<option value="11">getListIMRecord获取首页畅聊列表</option>
				<option value="12">modifyMuc修改群聊详情</option>
				<option value="13">modifyConference修改会议详情</option>
				<option value="17">getNewGroupCount获取新群数量</option>
				<option value="18">deleteIMFromList删除畅聊列表中的畅聊</option>
				<option value="19">setChannelID设置channelID</option>
				<option value="20">notifySendPushMessage业务平台通知推送</option>
				<option value="21">notifyCleanPushMessage业务平台通知取消推送</option>
				<option value="22">getListNotify获取所有未读的通知数据</option>
				<option value="23">cleanIMRecord清空畅聊中的聊天记录</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>测试地址:</td>
		<td><input id="url" size="100" value=""/></td>
	</tr>
	<tr>
		<td>测试地址全路径:</td>
		<td><input id="allUrl" size="100" value=""/></td>
	</tr>
	<tr>
		<td>操作人ID:</td>
		<td><input id="userId" size="10" value=""/></td>
	</tr>
</table>
<br/>
<br/>
<br/>
<div id="showResultDiv" >
	<div style="float: left">
		<div style="margin-top: 100px;float: left">输入参数：</div>
		<textarea rows="20" cols="40" id="intextarea" onchange="changein();"></textarea>
	</div>
	<div style="float: right;margin-right: 300px">
		<div style="margin-top: 100px;float: left;">输出参数：</div>
		<iframe id="showResult" style="margin-right: 40px;float: right" width="400" height="330" frameborder=1 scrolling=auto src="<%=request.getContextPath() %>/mobile/result"></iframe>
	</div>
	<input type="button" style="margin-top: 200px;margin-left: 50px;" onclick="getMethodReturnJsonValue();" value="提交">
</div>

</body>
<script type="text/javascript">
var loginInfo = "";
var returnDeftMsg = "";
function changeUrl() {
	var selectValue = $("#changeUrlSelect option:selected").val();
	var url = "";
	switch (selectValue) {
	case '0':
		url="";
		loginInfo="";
		$("#intextarea").val("");
		returnDeftMsg = '';
		break;
	case '1':
		url = "/mobile/im/sendMessage.action";
		loginInfo = '{"text":"测试发送消息","messageID":"4D2F1AAD2BA60AA0","fromTime":"2014-04-17 16:00:00","jtFile":{"type":0,"suffixName":"","url":"","fileName":"","fileSize":"","moduleType":"","taskId":"","reserved1":"","reserved2":"","reserved3":""},"type":0,"jtContactID":"5"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"listChatMessage":[{"content":"hello","index":"1","recvID":"102","senderID":"101","time":"2014-04-16","type":"0"},{"JTFile":{"suffixName":"jpg","type":2,"url":""},"content":"hi","index":"1","recvID":"101","senderID":"102","time":"2014-04-16","type":"0"}]},"notification":{"notifCode":"","notifInfo":""}}';
		break;
	case '101':
		url = "/mobile/im/sendMessage.action";
		loginInfo = '{"text":"测试发送群消息","messageID":"4345A6AA4DAC104D","fromTime":"2014-04-17 16:00:00","jtFile":{"type":0,"suffixName":"","url":"","fileName":"","fileSize":"","moduleType":"","taskId":"","reserved1":"","reserved2":"","reserved3":""},"type":0,"mucID":"5"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"listChatMessage":[{"content":"hello","index":"1","recvID":"102","senderID":"101","time":"2014-04-16","type":"0"},{"JTFile":{"suffixName":"jpg","type":2,"url":""},"content":"hi","index":"1","recvID":"101","senderID":"102","time":"2014-04-16","type":"0"}]},"notification":{"notifCode":"","notifInfo":""}}';
		break;
	case '2':
		url = "/mobile/im/createMUC.action";
		loginInfo = '{"content":"测试聊天","listJTContactID":["14359","14464"],"title":"测试随便聊聊","orderTime":"2014-04-21 06:49:10","listJTFile":[{"type":3,"suffixName":"png","url":"http://192.168.101.130/02.jpg","fileName":"qwe","fileSize":"33","moduleType":"2","taskId":"2","reserved1":"","reserved2":"","reserved3":""},{"type":3,"suffixName":"png","url":"http://192.168.101.130/02.jpg","fileName":"qq","fileSize":"22","moduleType":"1","taskId":"123","reserved1":"","reserved2":"","reserved3":""},{"type":3,"suffixName":"png","url":"http://192.168.101.130/02.jpg","fileName":"","fileSize":"","moduleType":"","taskId":"","reserved1":"","reserved2":"","reserved3":""}],"subject":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"mucDetail":{"title":"技术讨论","subject":"java","isConference":true,"max":"30","isStick":true,"isNotifyNews":true,"isAutoRecord":false,"listConnections":[{"id":"221","type":"2","sourceFrom":"关系来源","jtContactMini":{"id":"123","isOnline":false,"isWorkmate":false}}]}},"notification":{"notifCode":"","notifInfo":""}}';
		break;
	case '3':
		url = "/mobile/im/exitFromMUC.action";
		loginInfo = '{"userID":"0","mucID":"0"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"是否成功"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '4':
		url = "/mobile/im/invite2MUC.action";
		loginInfo = '{"listID":[23,22],"mucID":5}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"是否成功"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '5':
		url = "/mobile/im/getListMUC.action";
		loginInfo = '{}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"listMUCDetail":"获取我的会议列表"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '6':
		url = "/mobile/im/kickFromMUC.action";
		loginInfo = '{"userID":"0","mucID":"0"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"是否成功"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '7':
		url = "/mobile/im/getMUCDetail.action";
		loginInfo = '{"id":"5"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"MUCDetail":{}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '8':
		url = "/mobile/im/getMUCMessage.action";
		loginInfo = '{"id":"5","fromTime":"2014-04-21 11:13:36","isBackward":true}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"listMUCMessage":{}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '9':
		url = "/mobile/im/getChatMessage.action";
		loginInfo = '{"id":"5","fromTime":"2014-04-21 11:13:36","isBackward":true}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"listChatMessage":{}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '11':
		url = "/mobile/im/getListIMRecord.action";
		loginInfo = '{"index":"0","size":"20"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '12':
		url = "/mobile/im/modifyMuc.action";
		loginInfo = '{"id":"5","title":"修改测试","stick":"false","notifyNewMessage":"true","cleanRecord":"false"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '13':
		url = "/mobile/im/modifyConference.action";
		loginInfo = '{"id":"5","subject":"测试会议主题","startTime":"2014-05-01 8:00:00","content":"测试讨论","listAddRequirementID":[1],"listDeleteRequirementID":[1],"listAddJTFile":[{"type":1,"suffixName":"111","url":"111"}],"listDeleteJTFile":[{"type":0,"suffixName":"","url":""}]}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '17':
		url = "/mobile/im/getNewGroupCount.action";
		loginInfo = '{}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '18':
		url = "/mobile/im/deleteIMFromList.action";
		loginInfo = '{"userID":"","mucID":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '19':
		url = "/push/public/setChannelID.action";
		loginInfo = '{"userID":"","channelID":"123123123","clientType":"0","baiduUserID":"123123123"}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"","tag":"云推送aes加密秘钥"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '20':
		url = "/push/public/notifySendPushMessage.action";
		loginInfo = '{"listUserID":["", ""],"type":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"","tag":"云推送aes加密秘钥"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '21':
		url = "/push/public/notifyCleanPushMessage.action";
		loginInfo = '{"userID":"","type":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"","tag":"云推送aes加密秘钥"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '22':
		url = "/push/public/getListNotify.action";
		loginInfo = '{"userID":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"succeed":"","tag":"云推送aes加密秘钥"},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	case '23':
		url = "/mobile/im/cleanIMRecord.action";
		loginInfo = '{"userID":"","mucID":""}';
		$("#intextarea").val(loginInfo);
		returnDeftMsg = '{"responseData":{"Page":{"total":"总数","index":"当前页码","size":"该页内容数","listIMRecord":[]}},"notification":{"notifCode":"通知信息代号","notifInfo":"通知信息描述"}}';
		break;
	}
	
	
	$("#url").val(url);
	
	//$("#allUrl").val('http://localhost:8080/ImServer/mobile'+url);
	//$("#allUrl").val('http://192.168.101.131:2222/ImServer/mobile'+url);
	$("#allUrl").val('<%=basePath %>'+url);
	
	$("#showResult").val("");
}

function getMethodReturnJsonValue() {
	var selectValue = $("#changeUrlSelect option:selected").val();
	if(selectValue=='0'){
		alert("请选择方法!");
		return;
	}
	if(returnDeftMsg==""){
	  $("#showResult").attr("src",returnDeftMsg);
	}else{
	
		var url = $("#allUrl").val();
		var json = $("#intextarea").val();
		var user = 14386;
		var userId = $("#userId").val();
		if(userId!=''){
			user = userId;
		}
		$.ajax({
			url : url,
			data : json,
			async : false,
			beforeSend: function(xhr){
						xhr.setRequestHeader('sessionID', 'eXNzLTM0MzQtZHNmNTUtMjIyNTY1OTAzMTM5ODQwNDU5MTc3OQ==');
						xhr.setRequestHeader('jtUserId', user);
						xhr.setRequestHeader('jtNickName', 'test');
						},
			type : 'POST',
			dataType : "text",
			cache : false,
			error:function(){     
		        alert('error');     
		    }, 
			success : function(data) {
				//$("#showResult").attr("src",url);
				$("#showResult").contents().find("#resultDiv").html(data);
			}
		});
	}
}

function changein(){
	 $("#showResult").val("");
}
</script>




</html>

