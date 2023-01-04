<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
       
</HEAD>

<BODY>

<html:form action="InvestmentsOwnedUpdateAction">
  
   <TABLE align="center" id="invoTable" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=0 BORDER=0>
			 
	 <colgroup>
	    <col width="25%">
	    <col width="35%">
	    <col width="25%">			   
	    <col width="15%">
	 </colgroup>	
	 
	 <TR class="tableHeading">
	  	<td align="center">Investment Type</td>
	 	<td align="center">Description</td>
	 	<td align="center">Ticker</td>
	 	<td align="center">Price</td>			 	
	 </TR>
	 
	 <c:forEach var="invOwnedItem" items="${sessionScope.investmentsOwnedUpdateForm.invOwnedList}" varStatus="status">
	   <c:choose>
	     <c:when test="${status.count%2==0}">
	       <c:set var="rowclassjstl" value="rowOdd"/>
	     </c:when>
	     <c:otherwise>
	       <c:set var="rowclassjstl" value="rowEven"/>
	     </c:otherwise>
	   </c:choose>
	   <TR class="<c:out value="${rowclassjstl}"/>">
	      <td align="center">
	      	<c:out value="${invOwnedItem.tblinvestmenttype.sdescription}"/>		      	 
	      </td>	
	      <td align="center">
	      	<c:out value="${invOwnedItem.sdescription}"/>		      	 
	      </td>	
	      <td align="center">
	      	<c:out value="${invOwnedItem.stickerSymbol}"/>		      	 
	      </td>	
	      <td align="center">
	      	<html:text name="invOwnedItem" indexed="true" property="currentPriceAsString"/>		      	 
	      </td>			      	      		     
	   </TR>    			
	</c:forEach>
	
	<TR>
	  <TD align="right">
		<html:submit property="operation" value="Update" />
	  </TD>
	  <TD align="center" colspan="3">
	  	<html:submit property="operation" value="Cancel Update"/>
	  </TD>	 
  	</TR>	
  
  </TABLE>
     
</html:form>

</BODY> 
