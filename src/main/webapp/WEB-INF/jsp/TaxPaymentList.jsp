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

<html:form action="TaxPaymentListAction">

  <c:if test="${TaxPaymentList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
     
    <display:table name="sessionScope.TaxPaymentList" export="true" uid="TaxPayment" requestURI="">        
       
       <display:column title="View">
          <c:url value="/TaxPaymentShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxPaymentShowParm" value="inquire"/>
            <c:param name="taxPaymentID" value="${TaxPayment.itaxPaymentId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/TaxPaymentShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxPaymentShowParm" value="delete"/>
            <c:param name="taxPaymentID" value="${TaxPayment.itaxPaymentId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/TaxPaymentShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxPaymentShowParm" value="update"/>
            <c:param name="taxPaymentID" value="${TaxPayment.itaxPaymentId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="tbltaxgroup.staxGroupName" title="Tax Group Name" />
	   <display:column property="dtaxPaymentDate" title="Start Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="itaxYear" title="Tax Year" sortable="true" />
	   <display:column property="tbltaxpaymenttype.staxPaymentTypeDesc" title="Type" sortable="true" />
	   <display:column property="mtaxPaymentAmount" title="Payment Amount" format="{0,number,currency}" sortable="true"/>
        
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
