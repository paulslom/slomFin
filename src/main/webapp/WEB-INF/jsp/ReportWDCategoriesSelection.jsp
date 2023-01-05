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

<html:form action="ReportWDCategoriesAction">

  <BR/>
  <BR/>
  
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER="0">
	
	<colgroup>
	    <col width="50%">
	    <col width="50%">
	 </colgroup>
	 
	 <TR>
     
	    <td class="tableDataRight">
	        WD Categories Report Selections
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
	        Year
	    </td>
	 
	   	<td ALIGN="left">	       
	      <html:select name="reportWDCategoriesSelectionForm" property="year" tabindex="2">
		    <html:option value="0">-Select-</html:option>		    
			<html:options collection="LAST5YEARS" property="id" labelProperty="description" />							    	   
		  </html:select>
	    </td>
	      
	 </TR>
	 
	  <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
	 <TR>	
		
		<td class="tableDataRight">
		   <html:submit property="operation" value="Submit"/>
		</TD>
		<td>
		</td>
		 	
	</TR>
	 
  </TABLE>	 
        
</html:form>

</BODY> 
