<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <link rel="stylesheet" type="text/css" href="styles/displaytag.css">
   <script type="text/javascript" src="scripts/common.js"></script>
      
</HEAD>

<BODY>

<html:form action="ReportUnitsOwnedAction">

  <c:if test="${ReportUnitsOwnedList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.ReportUnitsOwnedList" defaultsort="1" export="true" cellspacing="0" >        
       
       <display:column property="investmentDescription" title="Investment"/>
	   <display:column property="unitsOwned" title="Units Owned" format="{0,number,######.####}"/>
	        	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
