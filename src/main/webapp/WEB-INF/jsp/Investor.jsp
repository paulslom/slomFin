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
	   <col width="70%">
	   <col width="30%">
	</colgroup>
	   
	<!--  
	
	<logic:iterate name="TrxCommonList" id="trxCommon">
	  <li><bean:write name="trxCommon" property="spictureName" /></li>
	  <li><bean:write name="trxCommon" property="tblaccount.saccountName"/></li>
	</logic:iterate>

	-->

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
	         <c:set var="invLink" value="${pageContext.request.contextPath}/CashSpentShowFormAction.do?operation=inquire&trxCommonID=${trxCommon.itrxCommonId}"/>
	         <html:link href="${invLink}">
	             <c:out value="${trxCommon.tblaccount.saccountName}"/>
	         </html:link>
	      </TD>
	
	   </TR>    			
	
	</c:forEach>
			
  </TABLE>

</html:form>

</BODY> 
