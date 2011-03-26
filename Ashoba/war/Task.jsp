<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

  <head>

  <%
	String locale = request.getParameter("locale");
  if( locale == null ){
	  Cookie ck[] = request.getCookies();
	  if(ck!=null)
	  for(int i = 0; i<ck.length;i++){
		  if(ck[i].getName().equals("locale")){
			  String query = request.getQueryString();
			  query = query == null? "":query;
			  response.sendRedirect(request.getRequestURL().append("?"+query).append("&locale="+ck[i].getValue()).toString());
		  }
	  }
	  locale="fr";
	  
  }
  %>
  
<script type="text/javascript">
function reload(localSlct){
	// response.addCookie(new Cookie("locale",localSlct.value));
	document.cookie="locale="+localSlct.value;
window.location = location.href.replace("locale=<%=locale%>","locale="+localSlct.value);
}

</script>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
   
    <link type="text/css" rel="stylesheet" href="Task.css">

    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Day and Night Schedule of the Believer</title>
    
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="task/task.nocache.js"></script>
  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body class="<%="ar".equals(locale)?"rtl":"ltr" %>">
<div align="right">
	<select onchange="reload(this);">
	<option value = "en" <%="en".equals(locale)?"selected":"" %>>English</option>
	<option value = "fr" <%="fr".equals(locale)?"selected":"" %>>Francais</option>
	<option value = "ar" <%="ar".equals(locale)?"selected":"" %>>العربية</option>
	</select>
	<a  href="https://yawmlayla.appspot.com/login.jsp?out">
	LogOut</a>
</div>
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
<div align="center" >
    <img align="center"  alt="Day and Night Schedule of the Believer" src="img/logoAr.jpg">
    </div>
    <br>


<div align="center" id="mainBar"></div>
<div align="center" id="main"></div>

  </body>

</html>

