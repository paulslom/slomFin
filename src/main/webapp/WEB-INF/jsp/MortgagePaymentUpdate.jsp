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

<html:form action="MortgagePaymentUpdateAction">

  <html:hidden property="mortgagePaymentID"/>
  <html:hidden property="mortgageID"/> 
     
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${mortgagePaymentShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Mortgage Payment ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="mortgagePaymentID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Bank"/>
	    </td>
	    <td ALIGN="left">	       
	        <pas:text property="mortgageDescription" textbox="false" readonly="true"/>
	    </td>	       
	 </TR>
		 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Payment Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="mortgagePaymentDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="mortgagePaymentDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Principal Paid"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="principalPaid" textbox="true" readonly="false" width="150" tabindex="10"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="principalPaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Interest Paid"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="interestPaid" textbox="true" readonly="false" width="150" tabindex="11"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="interestPaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Escrowed Property Taxes"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="propertyTaxesPaid" textbox="true" readonly="false" width="150" tabindex="15"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="propertyTaxesPaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="PMI"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="pmiPaid" textbox="true" readonly="false" width="150" tabindex="14"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="pmiPaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="HomeOwners Insurance Paid"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="homeownersInsurancePaid" textbox="true" readonly="false" width="150" tabindex="13"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="homeownersInsurancePaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
		 	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${mortgagePaymentShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${mortgagePaymentShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${mortgagePaymentShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${mortgagePaymentShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${mortgagePaymentShowParm=='add' || mortgagePaymentShowParm =='update'}">
     <script language="javascript">
	     document.forms.mortgagePaymentUpdateForm.mortgagePaymentDate.appmonth.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
