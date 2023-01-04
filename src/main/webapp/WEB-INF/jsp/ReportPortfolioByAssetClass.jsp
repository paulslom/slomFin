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

<html:form action="ReportPortfolioByAssetClassAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportPortByAssetClassList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
     <display:table name="sessionScope.ReportPortByAssetClassList" export="true" defaultsort="2" defaultorder="descending" requestURI=""
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
             
       <display:column property="assetClass" title="Asset Class"/>
	   <display:column property="assetClassPercentage" title="Percent" format="{0,number,##0.00'%'}" total="true"/>
	   <display:column property="currentValue" title="Value" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
