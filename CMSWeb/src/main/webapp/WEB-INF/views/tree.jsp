<%@ page contentType="text/html;charset=GB2312" %> 
<HTML> 
<BODY bgcolor=green><FONT size=1>    
<P>获取文本框提交的信息：   80
   <%String textContent=request.getParameter("boy"); 
   %> 
<BR> 
   <%=textContent%>  
<P>  获取按钮的名字： 
   <%String buttonName=request.getParameter("submit"); 
   %> 
<BR> 
   <%=buttonName%>  
</FONT> 
</BODY> 
</HTML> 