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

<html:form action="ReportCostBasisAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportCostBasisList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
      <display:table name="sessionScope.ReportCostBasisList" export="true" uid="CostBasis" class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator" requestURI="">      
             
       <display:column property="accountName" title="Account" group="1" />
       <display:column property="investmentDescription" title="Investment"/>
       <display:column property="unitsOwned" title="Units" format="{0,number,######.####}"/>
       <display:column property="totalCost" title="Total Cost" format="{0,number,currency}" total="true"/>
       <display:column property="costBasis" title="Cost Basis" format="{0,number,currency}"/>
	   <display:column property="currentValue" title="Current Value" format="{0,number,currency}" total="true"/>
	   <display:column property="gainLoss" title="Gain-Loss" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
