<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login to YawmLayla</title>
</head>
<body>
<%
if(request.getParameter("out")!=null)
	request.getSession().removeAttribute("mmn");
//if(request.getParameter("fb")!=null)
//	response.sendRedirect("https://yawmlayla.appspot.com/login");
String go = request.getParameter("go");
if(go == null)
	go ="https://yawmlayla.appspot.com/Task.jsp?"; 
	//go="https://yawmlayla.appspot.com/login?go="+go;
	%>
	<div align="center">
<div align="center" >
    <img align="center"  alt="Day and Night Schedule of the Believer" src="img/logoAr.jpg">
    </div>
<br>
<br>
<br>
<br>
Please login using your Google or Yahoo account:
<br>
<br>
<a style="color : white;" href="https://yawmlayla.appspot.com/_ah/login_redir?claimid=gmail.com&continue=https://yawmlayla.appspot.com/login?openid&go=<%=go%>">
<img src="/img/google.png" ></a>&nbsp;&nbsp;
<a style="color : white;" href="https://yawmlayla.appspot.com/_ah/login_redir?claimid=yahoo.com&continue=https://yawmlayla.appspot.com/login?openid&go=<%=go%>">
<img src="/img/yahoo.png" ></a>&nbsp;&nbsp; 
<a style="color : white;" href="https://yawmlayla.appspot.com/login?&go=<%=go%>">facebook</a>
	</div>
    <br>
</body>
</html>