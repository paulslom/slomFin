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

<html:form action="ReportDividendsAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportDividendsList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
      <display:table name="sessionScope.ReportDividendsList" export="true" id="row" requestURI=""
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
       
       <display:column title="Num">
          <c:out value="${row_rowNum}"/>
       </display:column> 
       
       <display:column property="dividendPayableYear" title="Year" group="1" sortable="true" />
       <display:column property="portfolioName" title="Portfolio" sortable="true" />          
       <display:column property="accountName" title="Account" sortable="true" />
       <display:column property="taxableYN" title="Taxable" sortable="true" />
       <display:column property="dividendDate" title="Date" format="{0,date,yyyy-MM-dd}" sortable="true" />
       <display:column property="investmentDescription" title="Investment" sortable="true" />
       <display:column property="units" title="Units" format="{0,number,######.####}"/>
       <display:column property="costProceeds" title="Amount" format="{0,number,currency}" sortable="true" total="true"/>  
      	      
       <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
