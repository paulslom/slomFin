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

<html:form action="MortgagePaymentListAction">

  <c:if test="${MortgagePaymentList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.MortgagePaymentList" export="true" uid="MortgagePayment" requestURI="">        
       
       <display:column title="View">
          <c:url value="/MortgagePaymentShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="mortgagePaymentShowParm" value="inquire"/>
            <c:param name="mortgagePaymentID" value="${MortgagePayment.imortgageHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/MortgagePaymentShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="mortgagePaymentShowParm" value="delete"/>
            <c:param name="mortgagePaymentID" value="${MortgagePayment.imortgageHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/MortgagePaymentShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="mortgagePaymentShowParm" value="update"/>
            <c:param name="mortgagePaymentID" value="${MortgagePayment.imortgageHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="tblmortgage.sdescription" title="Bank" />
	   <display:column property="dpaymentDate" title="Payment Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="mprincipalPaid" title="Principal" format="{0,number,currency}" sortable="true"/>
       <display:column property="minterestPaid" title="Interest" format="{0,number,currency}"sortable="true"/>
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
