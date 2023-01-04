<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <link rel="stylesheet" type="text/css" href="styles/displaytag.css">
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>
   
</HEAD>

<BODY>

<html:form action="TrxListAction">

  <html:hidden property="accountID"/>
  <html:hidden property="accountName"/>
  <html:hidden property="portfolioName"/>
  
  <html:hidden property="trxListOrigin" value="3"/>
 
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=1>
	 
	 <colgroup>
	    <col width="5%">
	    <col width="15%">
	    <col width="5%">
	    <col width="15%">
	    <col width="10%">
	    <col width="15%">
	    <col width="10%">
	    <col width="15%">
	    <col width="10%">
	 </colgroup>
	 
	 <TR>
	    <td class="bodyBold" ALIGN="center">
	       <pas:label value="Portfolio"/>
	    </td>    
	   <td ALIGN="left">	       
	       <pas:text property="portfolioName" readonly="true"/>
	   </td>
	    
	   <td class="bodyBold" ALIGN="center">
	       <pas:label value="Account"/>
	    </td>    
	   <td ALIGN="left">	       
	       <pas:text property="accountName" readonly="true"/>
	    </td>
	    
	    <td ALIGN="right">
	        <pas:label value="From Date"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:inputdate property="fromDate" tabindex="1"/>
	    </td>
	    
	    <td ALIGN="right">
	        <pas:label value="To Date"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:inputdate property="toDate" tabindex="4"/>
	    </td>
	    
	     <td ALIGN="left">	       
	       <html:submit property="operation" value="Re-submit" tabindex="7"/>
	    </td>
	    
	 </TR>	 	 
     	 
  </TABLE>
  
  <c:if test="${TransactionList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
     
    <display:table name="sessionScope.TransactionList" defaultsort="5" defaultorder="descending" export="true" uid="trx" requestURI="">        
       
       <display:column title="View">
          <c:url value="/TrxShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxShowParm" value="inquire"/>
            <c:param name="transactionID" value="${trx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/TrxShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxShowParm" value="delete"/>
            <c:param name="transactionID" value="${trx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/TrxShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="trxShowParm" value="update"/>
            <c:param name="transactionID" value="${trx.itransactionId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="trxDateDayOfWeek" title="Day"/>
       <display:column property="dtranPostedDate" title="Posted Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
       <display:column property="dtransactionDate" title="Trx Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="tblinvestment.stickerSymbol" title="Investment" sortable="true"/>
	   <display:column property="tbltransactiontype.sdescriptionAbbr" title="Trx Type" sortable="true"/>
       <display:column property="meffectiveAmount" title="Amount" format="{0,number,currency}" sortable="true"/>
       <display:column property="sdescription" title="Description" sortable="true"/>
       
       <c:choose>
          <c:when test="${trx.bfinalTrxOfBillingCycle}">
             <display:column class="bodyBold" property="macctBalance" title="Balance" format="{0,number,currency}" sortable="true"/>
          </c:when>
          <c:otherwise>
             <display:column class="body" property="macctBalance" title="Balance" format="{0,number,currency}" sortable="true"/>
          </c:otherwise>
       </c:choose>
     
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
  
  <script language="javascript">
	 document.forms.trxListForm.fromDate.appmonth.select(); 	  	  
  </script>
      
</html:form>

</BODY> 
