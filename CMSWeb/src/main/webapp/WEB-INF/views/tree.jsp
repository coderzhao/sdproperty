<%@ page contentType="text/html;charset=GB2312" %> 
<HTML> 
<BODY bgcolor=green><FONT size=1>    
<P>��ȡ�ı����ύ����Ϣ��   80
   <%String textContent=request.getParameter("boy"); 
   %> 
<BR> 
   <%=textContent%>  
<P>  ��ȡ��ť�����֣� 
   <%String buttonName=request.getParameter("submit"); 
   %> 
<BR> 
   <%=buttonName%>  
</FONT> 
</BODY> 
</HTML> 