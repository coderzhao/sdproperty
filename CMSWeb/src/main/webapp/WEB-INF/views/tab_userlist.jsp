<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>cms test</title>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/color.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/demo/demo.css">
<!--     <script type="text/javascript" src="../js/home.js"></script> -->
    <script type="text/javascript" src="../js/user.js"></script>
</head>
<body>
	<div style="margin:10px 0;"></div>
	<div class="easyui-layout" style="width:1890px;height:700px;">
			<div class="easyui-layout" style="width:1591px;height:570px;">
				<div data-options="region:'north'" style="height:50px">
				请输入用户id :
					<input type="text" id="userId" name="userId">
					<input type="submit" onclick="findUser();" value="Find">
				</div>
				<div data-options="region:'center',iconCls:'icon-ok'">
				
				<br>
				<table id="test1" class="easyui-datagrid" title="Basic DataGrid" style="width:700px;height:auto"
	            data-options="singleSelect:true,collapsible:true,url:'http://localhost:8080/hhit/user/getUsersAll.do',method:'get'">
		        <thead>
		            <tr>
		                <th data-options="field:'id',width:80">ID</th>
		                <th data-options="field:'username',width:100">用户名</th>
		                <th data-options="field:'password',width:80,align:'right'">密码</th>
		                <th data-options="field:'mobile',width:80,align:'right'">手机号</th>
		                <th data-options="field:'nick',width:250">昵称</th>
		                <th data-options="field:'email',width:60,align:'center'">邮箱</th>
		            </tr>
		        </thead>
		        </table>
		        <br>
		        
<!-- 		        <br> -->
<!-- 						<table id="test"></table> -->
<!-- 						<br> -->
						
<!-- 						<script type="text/javascript"> -->
<!-- 					$('#addwindow').window('close'); -->
<!-- 						$('#updatewindow').window('close'); -->
<!-- 					$('#checkwindow').window('close'); -->
<!-- 						</script> -->

<!-- 						<div id="addwindow" class="easyui-window" title="添加用户" style="width:400px;height:300px;padding:10px"> -->
<!-- 							用户ID :<input type="text" id="userId_1" name="userId"><br /> <br /> 用户姓名 :<input type="text" id="username_1" name="username"><br /> <br /> 用户密码 :<input type="text" id="password_1" name="password"><br /> <br /> <input type="submit" onclick="addUser();" value="确定"> -->
<!-- 						</div> -->
					
<!-- 						<div id="updatewindow" class="easyui-window" title="修改用户" style="width:400px;height:300px;padding:10px"> -->
<%-- 							用户ID :<input type="text" id="userId_2" name="userId" value="${user.userId}"><br /> <br /> 用户姓名 :<input type="text" id="username_2" name="username" value="${user.username}"><br /> <br /> 用户密码 :<input type="text" id="password_2" name="password" value="${user.password}"><br /> <br /> <input type="submit" onclick="updateUser();" value="确定"> --%>
<!-- 						</div> -->
						
<!-- 						<div id="checkwindow" class="easyui-window" title="查看用户" style="width:400px;height:300px;padding:10px"> -->
<%-- 							用户ID :<input type="text" id="userId_3" name="userId" value="${user.userId}" disabled="disabled"><br /> <br /> 用户姓名 :<input type="text" id="username_3" name="username" value="${user.username}" disabled="disabled"><br /> <br /> 用户密码 :<input type="text" id="password_3" name="password" value="${user.password}" disabled="disabled"> --%>
<!-- 						</div> -->
						 
						 
				</div>
			</div>
	</div>
   
</body>
</html>