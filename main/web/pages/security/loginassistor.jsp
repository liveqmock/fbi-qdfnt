<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*" %>
<%@ page import="pub.platform.form.config.SystemAttributeNames" %>
<%@ page import="pub.platform.security.OperatorManager" %>
<%
    String path = request.getContextPath();
    String username = request.getParameter("username");
    if (username == null) {
        out.println("<script language=\"javascript\">if(top){ top.location.href='" + path + "/pages/security/loginPage.jsp'; } else { location.href = '" + path + "/pages/security/loginPage_nocas.jsp';} </script>");
        return;
    }
    String password = request.getParameter("password");
    String cookieFlag = request.getParameter("DropExpiration");
    Cookie cookies[] = request.getCookies();
    int cookieMaxAge = 0;
    if ("Month".equalsIgnoreCase(cookieFlag)) {
        cookieMaxAge = 30 * 24 * 60 * 60;
    } else if ("Year".equalsIgnoreCase(cookieFlag)) {
        cookieMaxAge = 365 * 24 * 60 * 60;
    }
    if (!"None".equalsIgnoreCase(cookieFlag)) {
        boolean isFound = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("usernamecookie").equals(cookie.getName())) {
                    if (!username.equals(cookie.getValue())) {
                        cookie.setValue(username);
                        cookie.setPath(request.getContextPath() + "/pages");
                        cookie.setMaxAge(cookieMaxAge);
                        response.addCookie(cookie);
                    }
                    isFound = true;
                }
            }
        }
        if (!isFound) {
            Cookie cookie = new Cookie("usernamecookie", username);
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath(request.getContextPath() + "/pages");
            response.addCookie(cookie);
        }
    }

%>

<%
    OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

    if (om == null) {

        om = new OperatorManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
    }
    //String imgsign = request.getParameter("imgsign");
    boolean isLogin = false;
    try {
        //if(!om.ImgSign(imgsign))
        //	out.println("<script language=\"javascript\">alert ('����У��������'); if(top){ top.location.href='/index.jsp'; } else { location.href = '/index.jsp';} </script>");
        om.setRemoteAddr(request.getRemoteAddr());
        om.setRemoteHost(request.getRemoteHost());
        isLogin = om.login(username, password);
        if (!isLogin) {
            out.println("<script language=\"javascript\">alert ('�����û�������������'); if(top){ top.location.href='" + path + "/pages/security/loginPage_nocas.jsp'; } else { location.href = '" + path + "/pages/security/loginPage_nocas.jsp';} </script>");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
