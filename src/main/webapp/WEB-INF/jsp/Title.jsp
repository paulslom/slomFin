<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="Common_Tags.jsp" %> 
<HTML>
<HEAD>
<%@ page language="java" contentType="text/html"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>Slomkowski Financial</TITLE>
</HEAD>
<BODY class="bodyTag">
	<table width="100%" border="0" CELLPADDING="0" CELLSPACING="0">
	   <colgroup>
	      <col width="25%">
	      <col width="15%">
	      <col width="10%">
	      <col width="10%">
	      <col width="8%">
	      <col width="8%">
	      <col width="8%">
	      <col width="8%">
	      <col width="8%">
	   </colgroup>
	  <tr> 
		<td valign="top"><img src="<c:url value="/images/QQQ.gif"/>"/></td>
		<td class="pageTitle" align="center" width="350" height="22">
		    <i>
		      <font size="6" color="#990000">S</font><font size="4">lomkowski</font> 
              <font size="6" color="#990000">F</font><font size="4">inancial</font>
            </i> 
        </td>
        <c:if test="${sessionScope.SFInvestor!=null}">        
	       <td align="left"><c:out value="${sessionScope.SFInvestor.fullName}"/></td>
	       <td align="left">
	           <img src="<c:url value="images/${sessionScope.SFInvestor.pictureFileSmall}"/>"/>	                  
	       </td>
	    </c:if>
	    <c:if test="${sessionScope.AppUser!=null}">	      
	       <td class="blueSmall" align="right">Database:</td>
		   <td class="redSmall"  align="left" nowrap><font size="4"><c:out value="${sessionScope.CurrentDB}"/></font></td>
		   <td class="blueSmall" align="center">[<html:link action="/InvestorListAction.do?operation=inquire"> Investors</html:link>]</td>
		   <td class="blueSmall" align="center">[<html:link action="/InvestorChosenAction2.do?operation=inquire"> Home</html:link>]</td>
	 	   <td class="blueSmall" align="left">[<html:link action="/LogoutAction">Logout</html:link>]</td>
		</c:if>		
	 </tr>
	 <tr>
	    <td colspan="9"><img src="<c:out value="${pageContext.request.contextPath}/images/banner_bottom.gif"/>" width="100%" height="4"></td>
	 </tr>
	</table>

</body>

</html>
