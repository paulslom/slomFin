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

<html:form action="ReportBudgetAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportBudgetList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
      <display:table name="sessionScope.ReportBudgetList" export="true" uid="Budget"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
             
       <display:column property="budgetType" title="Type" group="1" />
       <display:column property="budgetDescription" title="Description"/>
	   <display:column property="budgetTotal" title="Total" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
