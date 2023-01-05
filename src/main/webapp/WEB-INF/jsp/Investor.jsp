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

<html:form action="InvestorChosenAction2">
   
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	 
	<colgroup>
	   <col width="33%">
	   <col width="33%">
	   <col width="34%">
	</colgroup>

    <c:forEach var="trxCommon" items="${sessionScope.TrxCommonList}" varStatus="status">
    
	  <c:choose>
	     <c:when test="${status.count%2==0}">
	       <c:set var="rowclassjstl" value="rowEven"/>
	     </c:when>
	     <c:otherwise>
	       <c:set var="rowclassjstl" value="rowOdd"/>
	     </c:otherwise>
	   </c:choose>
	
	   <TR class="<c:out value="${rowclassjstl}"/>">
	
	      <td align="right"><IMG src="<c:url value="/images/${trxCommon.spictureName}"/>"/></td>
	      
	      <TD align="left">
	         <c:set var="link1" value="${pageContext.request.contextPath}/CashSpentShowFormAction.do?operation=inquire&trxCommonID=${trxCommon.itrxCommonId}"/>
	         <html:link href="${link1}">
	             <c:out value="${trxCommon.tblaccount.saccountName}"/>
	         </html:link>
	      </TD>
	      
	       <TD align="left">
	         <c:set var="link2" value="${pageContext.request.contextPath}/TrxListAction.do?operation=inquire&trxListOrigin=1&accountID=${trxCommon.itrxCommonAccountId}"/>
	         <html:link href="${link2}">
	             <c:out value="View-Chg-Del ${trxCommon.tblaccount.saccountName}"/>
	         </html:link>
	      </TD>
	
	   </TR>    			
	
	</c:forEach>
			
  </TABLE>

</html:form>

</BODY> 
