<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/taghead.jsp" %>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <>
    <title>要播日志管理</title>


	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		jQuery.ajaxSetup({cache:false});//ajax不缓存
		jQuery(function($){

		});

		function setmain(title,href){
//			$(".combo-panel").parent(".panel").remove(); //清楚所有class为combo-panel的class为panel的父元素，解决combobox在页面缓存的问题
//			var centerURL = href;
//			var centerTitle = title;
//			$('body').layout('panel','center').panel({
//				title:"所在位置："+centerTitle,
//				href:centerURL
//			});
//			$('body').layout('panel','center').panel('refresh');
//			return false;
			addTab(title, href);
		}


		function addTab(title,url){
			if ($('#tt').tabs('exists', title)){
				$('#tt').tabs('select', title);
			} else {
				var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
				$('#tt').tabs('add',{
					title:title,
					content:content,
					closable:true
				});
			}
		}



		//弹出窗口
		function showWindow(options){
			jQuery("#MyPopWindow").window(options);
		}
		//关闭弹出窗口
		function closeWindow(){
			$("#MyPopWindow").window('close');
		}
	</script>

  </head>
  <!-- easyui-layout 可分上下左右中五部分，中间的是必须的，支持href，这样就可以不用iframe了 -->
  <body class="easyui-layout" id="mainBody">
		<!-- 上 -->
		<div region="north" split="false" style="height:40px;text-align: center;" border="false">
			<%--<h1>欢迎：${message} sessionid:<%=session.getId()%> username:<%=session.getAttribute("username")%></h1>--%>
				<h3>要播日志管理</h3>
		</div>

		<!-- 左-->
		<div region="west" class="menudiv" split="true" title="系统菜单" style="width:200px;overflow:hidden;">
			<div id="menuDiv" class="easyui-accordion" fit="false" border="false" animate="false">


				<div title="全部" style="overflow:hidden;">
					<ul>
						<li id="rightLi${child.id}" style="cursor: pointer;"
							onclick="setmain('全部','${ctx}/webex/list.do')" >全部记录</li>
					</ul>
				</div>
				<div title="点播统计" style="overflow:hidden;">
					<ul>
						<li id="rightLi${child.id}" style="cursor: pointer;"
							onclick="setmain('点播统计','${ctx}/webex/agentList.do')" >点播统计</li>
					</ul>
				</div>
				<div title="直播统计" style="overflow:hidden;">
					<ul>
						<li id="leftTreeItemDeviceOEM" style="cursor: pointer;"
							onclick="setmain('直播统计','${ctx}/webex/oemdeviceList.do')" >直播统计</li>
					</ul>
				</div>
			</div>
		</div>

		<!-- 中 -->
		<div id="tt" region="center" class="easyui-tabs" style="width:400px;height:250px;">
			<div title="Home">
			</div>
		</div>

		<div id= "tt" region="center" class="maindiv" title="所在位置: 首页" style="overflow-x:hidden;padding: 0px;" href="${ctx}/webex/deviceMap.do" ></div>
		<div id="MyPopWindow" modal="true" shadow="false" minimizable="false" cache="false" maximizable="false" collapsible="false" resizable="false" style="margin: 0px;padding: 0px;overflow: auto;"></div>
		<%--显示或隐藏列表某个部位--%>
		<script>
			//ajax_post();
			function ajax_post(){
				$.post("${ctx}/webApiDevice/queryDeviceList.do",{page:1,rows:100},
						function(data){
							//$('#msg').html("please enter the email!");
							var len = data.total;
							var i;
							for(i = 0; i < len; i++){
									var row = data.rows[i];
									//alert(row.lng);
							}
							//alert(data.rows);
							//alert(data);
							//$('#msg').html(data);
						},
						"json");//这里返回的类型有：json,html,xml,text
			}

		</script>
  </body>
</html>
