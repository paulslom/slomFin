<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>
   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
</HEAD>

<BODY>

<html:form action="MenuAction2">

  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	 
	<colgroup>
	   <col width="40%">
	   <col width="60%">
	</colgroup>   
	
    <c:forEach var="investor" items="${investorList}" varStatus="status">
	  <c:choose>
	     <c:when test="${status.count%2==0}">
	       <c:set var="rowclassjstl" value="rowEven"/>
	     </c:when>
	     <c:otherwise>
	       <c:set var="rowclassjstl" value="rowOdd"/>
	     </c:otherwise>
	   </c:choose>
	   <TR class="<c:out value="${rowclassjstl}"/>">
	      <td align="right"><IMG src="<c:url value="/images/${investor.spictureFileSmall}"/>"/></td>
	      <TD align="left">
	         <c:set var="invLink" value="${pageContext.request.contextPath}/InvestorChosenAction1.do?operation=inquire&fromWhere=investorList&investorID=${investor.iinvestorId}"/>
	         <html:link href="${invLink}">
	             <c:out value="${investor.sfullName}"/>
	         </html:link>
	      </TD>
	   </TR>    			
	</c:forEach>	
        
			
  </TABLE>
  <!-- End Table Detail  -->

</html:form> 
