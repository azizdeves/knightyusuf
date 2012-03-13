package com.aljamaa.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.aljamaa.entity.Momin;
import com.aljamaa.shared.TaskException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class LoginFacebookServlet extends HttpServlet
{
  private static final long serialVersionUID = -1187933703374946249L;
  private static Logger log = Logger.getLogger(LoginFacebookServlet.class.getName());

  
  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(req, resp);
}


public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	  String go = request.getParameter("go");
	  if(go == null )
		  go="https://yawmlayla.appspot.com/Task.jsp?";
	  if(request.getParameter("openid")!=null){
		  UserService us = UserServiceFactory.getUserService();
		  User u = us.getCurrentUser();
		  log.info("openid login="+u.getEmail() );
		  Momin mmn = new Momin();
		  mmn.setId('o'+u.getUserId());
		  try {
			new LoginHelper().loginStarts(request.getSession(), mmn);
		} catch (TaskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect(go);
		return;
	  }
    String callbackURL = null, clientSecret = null, appId = null;

    appId = "201177699906669";
    clientSecret = "46b48f7f912108ba2dbdc8bb523ffc50";

    if(appId == null || clientSecret == null){
      response.setContentType("text/html");
//      response.getWriter().print(AppLib.INFONOTFOUND);
      return;
    }
    callbackURL = "https://yawmlayla.appspot.com/login?fb";
    String code = request.getParameter("code");

    if (code != null && !code.isEmpty()) {
      // Facebook authentication has been done and Facebook is calling us back here:
      String keyNValues = getParams(request);
      log.info("==== STEP 2/1: got params=" + keyNValues);
      log.info("==== STEP 2/2: got code=" + code);

      /*
       * Save code in session
       */
      request.getSession().setAttribute("facebook_code", code);

      /*
       * Get access token
       */
      String tokenURL = "https://graph.facebook.com/oauth/access_token" + "?client_id=" + appId + "&redirect_uri="
          + callbackURL + "&client_secret=" + clientSecret + "&code=" + code;

      log.info("requesting access token url=" + tokenURL);
      String resp = UrlFetcher.get(tokenURL);
      log.info("Response = " + resp);
      int beginIndex = "access_token=".length();
      String token = resp.substring(beginIndex);
      log.info("Extracted token = " + token);

      /*
       * Get user info
       */
      String url = "https://graph.facebook.com/me?access_token=" + token;
      log.info("requesting user info: " + url);
      resp = UrlFetcher.get(url);
      log.info("Response: " + resp);
      Momin connectr = extractUserInfo(resp);
      try {
		connectr = new LoginHelper().loginStarts(request.getSession(), connectr);
	} catch (TaskException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      log.info("User id is logged in:" + connectr.getId().toString());

      /*
       * All done. Let's go home.
       */
      response.sendRedirect(go+"&code="+code+"&fb");

    } else { 
      // Redirect to Facebook login page
      log.info("Starting FB authentication appid: " + appId + " - callback: " + callbackURL);
      String fbLoginPage = "https://graph.facebook.com/oauth/authorize" 
        + "?client_id=" + appId 
        + "&redirect_uri=" + callbackURL
      + "&go=" + go;

      response.sendRedirect(fbLoginPage);
    }

  }


  private Momin extractUserInfo(String resp) {
    log.info("Extracting user info");
    JSONObject j;
    Momin u = null;
    try {
      j = new JSONObject(resp);
      String first = j.getString("first_name");
      String last = j.getString("last_name");
      String id ='f'+ j.getString("id");
      log.info("User info from JSON: " + first + " " + last + " id = " + id);
      u = new Momin(id, AuthenticationProvider.FACEBOOK);
      u.setName(first + " " + last);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return u;
  }

  private String getParams(HttpServletRequest request) {
    Enumeration<?> all = request.getParameterNames();
    String msg = "";
    while (all.hasMoreElements()) {
      String key = (String) all.nextElement();
      msg += key + " = " + request.getParameter(key) + "<br/>\n";
    }
    return msg;
  }
}
