<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
     
</HEAD>

<BODY>

<html:form action="ReportGoalsNavigateAction">

  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="50%">
	    <col width="50%">
	 </colgroup>	
		
	 <TR>
		 <td ALIGN="right">
		   <pas:label value="Percent Rate of Return"/>
		 </td>    
		 <td ALIGN="left">	       
		   <pas:dropdown property="rateOfReturn" collection="Integers1To15" tabindex="1" readonly="false" />
		 </td>
	 </TR>
	
	<TR>
		 <td ALIGN="right">
		   <pas:label value="Projection Years"/>
		 </td>    
		 <td ALIGN="left">	       
		   <pas:dropdown property="projectionYears" collection="Integers1To15" tabindex="2" readonly="false" />
		 </td>
	 </TR>
	 	
	 <TR>
	    <TD colspan="2" align="center">
		    <html:submit property="operation" value="Submit"/>
		</TD>
	 </TR>
	
  </TABLE>
   
</html:form>

</BODY> 
