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

<html:form action="ReportGoalsAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportGoalsList!=null}">
   
    <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
      
      <display:table name="sessionScope.ReportGoalsList" export="true" uid="Goals"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
             
       <display:column property="portfolioName" title="Portfolio" group="1" />
       <display:column property="accountName" title="Account"/>
	   <display:column property="accountValue" title="Value" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
      
    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
