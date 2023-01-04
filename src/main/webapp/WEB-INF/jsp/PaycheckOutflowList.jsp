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

<html:form action="PaycheckOutflowListAction">

  <c:if test="${PaycheckOutflowList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
     
    <display:table name="sessionScope.PaycheckOutflowList" export="true" uid="PaycheckOutflow" requestURI="">        
       
       <display:column title="View">
          <c:url value="/PaycheckOutflowShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckOutflowShowParm" value="inquire"/>
            <c:param name="paycheckOutflowID" value="${PaycheckOutflow.ipaydayId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/PaycheckOutflowShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckOutflowShowParm" value="delete"/>
            <c:param name="paycheckOutflowID" value="${PaycheckOutflow.ipaydayId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/PaycheckOutflowShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckOutflowShowParm" value="update"/>
            <c:param name="paycheckOutflowID" value="${PaycheckOutflow.ipaydayId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="sdescription" title="Description"  sortable="true" />
	   <display:column property="tblaccountByIAccountId.saccountName" title="Account" sortable="true"/>
	   <display:column property="tbltransactiontype.sdescription" title="Trx Type" sortable="true"/>
       <display:column property="idefaultDay" title="Day" sortable="true"/>
       <display:column property="mdefaultAmt" title="Amount"  format="{0,number,currency}"/>
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
