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

<html:form action="TaxPaymentUpdateAction">

  <html:hidden property="taxPaymentID"/>
      
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${taxPaymentShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Tax Payment ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="taxPaymentID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
		 <td ALIGN="right">
		    <pas:label value="Tax Group"/>
	    </td>    
	    <td ALIGN="left">	       
	         <html:select name="taxPaymentUpdateForm" property="taxGroupID" disabled="true">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="TaxGroups" property="id" labelProperty="description" />					    	   
			 </html:select>
	    </td>	 
	 </TR>	
	
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Year"/>
	    </td>
	    <c:choose>
	      <c:when test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="taxYear" textbox="true" readonly="false" size="50" width="100" tabindex="1"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	           <pas:text property="taxYear" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
		 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Payment Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="taxPaymentDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="taxPaymentDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
		
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Payment Type"/>
	    </td>
	    <c:choose>
	      <c:when test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="taxPaymentUpdateForm" property="taxPaymentTypeID" tabindex="8">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="TaxPaymentTypes" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	           <pas:text property="taxPaymentTypeDesc" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Payment Amount"/>
	    </td>
	    <c:choose>
          <c:when test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="taxPaymentAmount" textbox="true" readonly="false" width="150" tabindex="6"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="taxPaymentAmount" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Payment Description"/>
	    </td>
	    <c:choose>
          <c:when test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="taxPaymentDesc" textbox="true" readonly="false" width="150" tabindex="7"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="taxPaymentDesc" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${taxPaymentShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${taxPaymentShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${taxPaymentShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${taxPaymentShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${taxPaymentShowParm=='add' || taxPaymentShowParm =='update'}">
     <script language="javascript">
	     document.forms.taxPaymentUpdateForm.taxYear.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
