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

<html:form action="ReportCapitalGainsAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportCapitalGainsList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
      <display:table name="sessionScope.ReportCapitalGainsList" export="true" id="row" requestURI=""
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
       
       <display:column title="Num">
          <c:out value="${row_rowNum}"/>
       </display:column> 
       
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
      	      
       <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
