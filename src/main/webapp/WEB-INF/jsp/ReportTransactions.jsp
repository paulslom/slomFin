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

<html:form action="ReportTransactionsAction">

  <c:if test="${ReportTrxList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.ReportTrxList" defaultsort="5" defaultorder="descending" export="true" uid="rptTrx" requestURI="">        
       
      <display:caption style="font-weight:bold;"> <c:out value="${reportTrxTitle}"/> </display:caption> 
     
      <display:column title="Num">
          <c:out value="${rptTrx_rowNum}"/>
       </display:column> 
             
      <display:column title="View">
          <c:url value="/TrxShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxUpdateOrigin" value="fwdRptTrx"/>
            <c:param name="trxShowParm" value="inquire"/>
            <c:param name="transactionID" value="${rptTrx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/TrxShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxUpdateOrigin" value="fwdRptTrx"/>
            <c:param name="trxShowParm" value="delete"/>
            <c:param name="transactionID" value="${rptTrx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/TrxShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxUpdateOrigin" value="fwdRptTrx"/>
            <c:param name="trxShowParm" value="update"/>
            <c:param name="transactionID" value="${rptTrx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="dtransactionDate" title="Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
       <display:column property="tbltransactiontype.sdescription" title="Trx Type" sortable="true"/>
	   
	   <c:if test="${showCheckNo==true}">
	      <display:column property="icheckNo" title="Check No" sortable="true"/>
	   </c:if>
	   
	   <c:if test="${showDescription==true}">
	       <display:column property="sdescription" title="Description" sortable="true"/>
	   </c:if>
	   
	   <c:if test="${showUnits==true}">
	      <display:column property="decUnits" title="Units" format="{0,number,#.####}" sortable="true"/>
	   </c:if>
	   
	   <c:if test="${showPrice==true}">
	      <display:column property="mprice" title="Price" sortable="true"/>
	   </c:if>
       
       <display:column property="meffectiveAmount" title="Amount" format="{0,number,currency}" sortable="true"/>   
       
       <c:if test="${showAccountBalance==true}">
          <display:column property="macctBalance" title="Acct Balance" format="{0,number,currency}" sortable="true"/>     
       </c:if>
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
