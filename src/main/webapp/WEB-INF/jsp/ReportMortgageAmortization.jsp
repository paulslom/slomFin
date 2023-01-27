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

<html:form action="ReportMortgageAmortizationAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
        
  <c:if test="${MortgageAmortizationList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.MortgageAmortizationList" export="true" class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator" uid="mgtamort" requestURI="">        
   
       <display:column property="mortgagePaymentYear" title="Year" group="1" />
	   <display:column property="mortgagePaymentDate" title="Date" format="{0,date,yyyy-MM-dd}"/>
	   <display:column property="mortgagePaymentFromDate" title="From" format="{0,date,yyyy-MM-dd}"/>
	   <display:column property="mortgagePaymentToDate" title="Through" format="{0,date,yyyy-MM-dd}"/>
	   <display:column property="principalPaid" title="Principal" format="{0,number,currency}" total="true"/>
       <display:column property="interestPaid" title="Interest" format="{0,number,currency}" total="true"/>
       <display:column property="extraPrincipalPaid" title="Extra Principal" format="{0,number,currency}" total="true"/>
       <display:column property="mortgageBalance" title="Principal Balance" format="{0,number,currency}"/>
      
      <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
