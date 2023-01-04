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

<html:form action="TrxShowSearchFormAction">

  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	 	 
	 <TR>
	        
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
	    
	    <td ALIGN="right">
	        <pas:label value="Search Text"/>
	    </td>    
	    <td ALIGN="left">
	       <pas:text property="trxSearchText" textbox="true" readonly="false" size="50" width="150" tabindex="7"/>
	    </td>
	    
	    <td ALIGN="left">	       
	       <html:submit property="operation" value="Submit" tabindex="8"/>
	    </td>
	    
	 </TR>	 	 
     	 
  </TABLE>
  
  <c:if test="${TransactionList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
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
       
       <display:column property="dtransactionDate" title="Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="tblaccount.saccountName" title="Acct"/>
	   <display:column property="tbltransactiontype.sdescription" title="Trx Type"/>
       <display:column property="meffectiveAmount" title="Amount" format="{0,number,currency}" sortable="true"/>
       <display:column property="sdescription" title="Description" sortable="true"/>
          
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
  
  <script language="javascript">
	 document.forms.trxSearchForm.fromDate.appmonth.select(); 	  	  
  </script>
      
</html:form>

</BODY> 
