<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8 />
<!--
首页

Copyright (c) 2014 by anonymous (http://jsbin.com/egaqub/7/edit)

Released under the MIT license: http://jsbin.mit-license.org
-->
<title>JS Bin</title>
<script class="jsbin" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
<script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/home.js"></script>
<!--[if IE]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<style>
  article, aside, figure, footer, header, hgroup, 
  menu, nav, section { display: block; }
  .west{
    width:200px;
    padding:10px;
  }
  .north{
    height:100px;
  }
</style>

<style id="jsbin-css">

</style>
</head>
<body class="easyui-layout">
  <div region="north" class="north">
    <h1>最简单的左右结构实现，及tab的右键菜单实现，右键查看源码</h1>
  </div>
  <div region="center" >
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
      <div title="首页"></div>
    </div>
  </div>
  <div region="west" class="west" title="栏目管理">
    <ul id="tree"></ul>
  </div>
  
  <div id="tabsMenu" class="easyui-menu" style="width:120px;">  
    <div name="close">关闭</div>  
    <div name="Other">关闭其他</div>  
    <div name="All">关闭所有</div>
  </div>  
<script>
$(function(){
  //动态菜单数据
  var treeData = [
        {
          text:"用户管理",
          state:"closed",
          children:[
            {
              text:"核心用户",
              attributes:{
                url:"../home/home_tab_userlist.do"
              }
            },
            {
              text:"普通用户",
              attributes:{
                url:"../home/home.do"
              }
            }
          ]
    	},
    	{
            text:"区域管理",
            state:"closed",
            children:[
              {
                text:"区域关系",
                attributes:{
                	url:"../index.html"
                }
              },
              {
                text:"区域列表",
                attributes:{
                  url:""
                }
              }
            ]
      	}
  ];
  
  //实例化树形菜单
  $("#tree").tree({
    data:treeData,
    lines:true,
    onClick:function(node){
      if(node.attributes){
        Open(node.text,node.attributes.url);
      }
    }
  });
  //在右边center区域打开菜单，新增tab
  function Open(text,url){
	//  alert(text + ":" + url);
    if($("#tabs").tabs('exists',text)){
        $('#tabs').tabs('select', text);
    }else{
    	//var url="http://www.baidu.com";
    	   var content = '<iframe scrolling="no" frameborder="0"'+
    	           'src="'+url+'" style="width:100%;height:100%;"></iframe>';
      $('#tabs').tabs('add', {
        title:text,
        closable:true,
        //href:'login.jsp'
        content:content
      });
    }
  }
  
  //绑定tabs的右键菜单
  $("#tabs").tabs({
    onContextMenu:function(e,title){
      e.preventDefault();
      $('#tabsMenu').menu('show', {  
        left: e.pageX,  
        top: e.pageY  
      }).data("tabTitle",title);
    }
  });
  
  //实例化menu的onClick事件
  $("#tabsMenu").menu({
    onClick:function(item){
      CloseTab(this,item.name);
    }
  });
  
  //几个关闭事件的实现
  function CloseTab(menu,type){
    var curTabTitle = $(menu).data("tabTitle");
    var tabs = $("#tabs");
  
    if(type === "close"){
       tabs.tabs("close",curTabTitle);
      return;
    }
    
    var allTabs = tabs.tabs("tabs");
    var closeTabsTitle = [];
    
    $.each(allTabs,function(){
      var opt = $(this).panel("options");
      if(opt.closable && opt.title != curTabTitle && type === "Other"){
        closeTabsTitle.push(opt.title);
      }else if(opt.closable && type === "All"){
        closeTabsTitle.push(opt.title);
      }
    });
    
    for(var i = 0;i<closeTabsTitle.length;i++){
      tabs.tabs("close",closeTabsTitle[i]);
    }
  }
});
</script>
<script src="http://static.jsbin.com/js/render/edit.js?3.18.42"></script>


<script>
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
ga('create', 'UA-1656750-34', 'jsbin.com');
ga('require', 'linkid', 'linkid.js');
ga('require', 'displayfeatures');
ga('send', 'pageview');

</script>

</body>
</html>