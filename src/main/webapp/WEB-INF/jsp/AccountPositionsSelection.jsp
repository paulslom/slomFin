<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <link rel="stylesheet" type="text/css" href="styles/displaytag.css">
   <script type="text/javascript" src="scripts/common.js"></script>
      
</HEAD>

<script type="text/javascript">  
 
</script>

<BODY>

<html:form action="ReportAccountPositionsAction">

  <BR/>
  <BR/>
   
  <input type="hidden" id="portID" value="${portfolioID}"/>
   
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER="0">
	
	<colgroup>
	    <col width="50%">
	    <col width="50%">
	 </colgroup>
	 
	 <TR>
     
	    <td class="tableDataRight">
	        Account Positions : Select an account
	    </td>
	 
	   	<td ALIGN="left">	       
	        
	    </td>
	      
	 </TR>
	 
	 <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
     <TR>
     
	    <td class="tableDataRight">
	        Pick an account
	    </td>
	 
	   	<td ALIGN="left">	       
	      <select id="idSelectAccount" name="accountID" tabindex="1">
	          <option value="0">Select</option>	
	          <c:forEach items="${Accounts}" var="accts">
	            <option value="${accts.id}">${accts.description}</option>
	          </c:forEach>			    	   
		  </select>
	    </td>
	      
	 </TR>
	 
	 <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
	 <TR>	
		
		<td>
	    </td>
	 
	   	<td ALIGN="left">	       
	 	     <html:submit property="operation" value="Submit" />
		</TD>
				 	
	</TR>
	 
  </TABLE>	 
        
</html:form>

</BODY> 
