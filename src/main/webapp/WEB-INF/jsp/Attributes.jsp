<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>	

<%
  // Print all attributes in the request object
  out.println("<br> ");
  out.println("All Attributes in request scope:");
  out.println("<br> ");
  
  java.util.Enumeration paramNames = request.getAttributeNames();
  while (paramNames.hasMoreElements()) {
    String name = (String) paramNames.nextElement();
    Object values = request.getAttribute(name);
    out.println("<br> " + name + ":" + values);
  }
  
  // Print all attributes in the session object
  out.println("<br> ");
  out.println("All Attributes in session scope:");
  out.println("<br> ");
  
  paramNames = session.getAttributeNames();
  while (paramNames.hasMoreElements()) {
    String name = (String) paramNames.nextElement();
    Object values = session.getAttribute(name);
    out.println("<br> " + name + ":" + values);
  }

  out.println("<br> ");
  out.println("Data in ActionMessages:");
  out.println("<br> ");
  
  // Get the ActionMessages 
  Object o = request.getAttribute(org.apache.struts.Globals.MESSAGE_KEY);
  if (o != null) {
    org.apache.struts.action.ActionMessages ae = (org.apache.struts.action.ActionMessages)o;

    // Get the locale and message resources bundle
    java.util.Locale locale = (java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    org.apache.struts.util.MessageResources messages = (org.apache.struts.util.MessageResources)request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);

    // Loop thru all the labels in the ActionMessages 
    for (java.util.Iterator i = ae.properties(); i.hasNext();) {
      String property = (String)i.next();
      out.println("<br>property " + property + ": ");

      // Get all messages for this label
      for (java.util.Iterator it = ae.get(property); it.hasNext();) {
    	org.apache.struts.action.ActionMessage a = (org.apache.struts.action.ActionMessage)it.next();
        String key = a.getKey();
        Object[] values = a.getValues();
        out.println(" [key=" + key + ", message=" + messages.getMessage(locale,key,values) + "]");
      }
    }
  }
%> 
</body>
</html>