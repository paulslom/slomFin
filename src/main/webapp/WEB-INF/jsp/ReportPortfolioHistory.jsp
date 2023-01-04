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

<html:form action="ReportPortfolioHistoryAction">

  <c:if test="${ReportPortfolioHistoryList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.ReportPortfolioHistoryList"  defaultsort="1" defaultorder="descending" export="true" uid="ReportPortfolioHistory" requestURI="">        
             
       <display:column property="portfolioHistoryDate" title="Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="netWorth" title="Net Worth"  format="{0,number,currency}"/>
       <display:column property="yearlyDollarsGainLoss" title="This Year Dollars"  format="{0,number,currency}"/>
       <display:column property="yearlyPercentageGainLoss" title="This Year Percent" format="{0,number,##0.00'%'}"/>
        
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
