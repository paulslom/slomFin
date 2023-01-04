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

<html:form action="ReportTransactionsByInvestmentAction">

  <BR/>
  <BR/>
  
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	     
     <TR>
     
	    <td ALIGN="right">
	        <pas:label value="Investment"/>
	    </td>
	 
	   	<td ALIGN="left">	       
	      <html:select name="investmentChoiceForm" property="investmentID" tabindex="1">
		    <html:option value="0">-Select-</html:option>		    
		    <html:options collection="INVM" property="id" labelProperty="description" />					    	   
		  </html:select>
	    </td>
	      
	 </TR>
	 
	  <TR>
     
	    <td ALIGN="right">
	        <pas:label value="Account"/>
	    </td>
	 
	   	<td ALIGN="left">	       
	      <html:select name="investmentChoiceForm" property="accountID" tabindex="2">
		    <html:option value="0">-Select-</html:option>		    
			<html:options collection="InvestmentChoiceAccounts" property="id" labelProperty="description" />							    	   
		  </html:select>
	    </td>
	      
	 </TR>
  
  </TABLE>	 
 	
  <BR/>
  <BR/>
   
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>

	 <TR>	
		
		<TD align="center">
		   <html:submit property="operation" value="Submit"/>
		</TD>
		 	
	</TR>
	 
  </TABLE>	 
        
</html:form>

</BODY> 
