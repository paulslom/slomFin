<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>
    
</HEAD>

<BODY>

<html:form action="PortfolioUpdateAction">

  <html:hidden property="portfolioID"/>
        
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${portfolioShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Portfolio ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="portfolioID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Portfolio Name"/>
	    </td>
	    <c:choose>
          <c:when test="${portfolioShowParm=='add' || portfolioShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="portfolioName" textbox="true" readonly="false" width="150" tabindex="1"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="portfolioName" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Taxable"/>
	    </td>
	    <c:choose>
          <c:when test="${portfolioShowParm=='add' || portfolioShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <html:checkbox property="taxable" tabindex="2"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="taxable" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${portfolioShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${portfolioShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${portfolioShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${portfolioShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${portfolioShowParm=='add' || portfolioShowParm =='update'}">
     <script language="javascript">
	      document.forms.portfolioUpdateForm.portfolioName.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
