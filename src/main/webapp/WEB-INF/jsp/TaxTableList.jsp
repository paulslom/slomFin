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

<html:form action="TaxTableListAction">

  <c:if test="${TaxTableList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
     
    <display:table name="sessionScope.TaxTableList" export="true" uid="TaxTable" requestURI="">        
       
       <display:column title="View">
          <c:url value="/TaxTableShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxTableShowParm" value="inquire"/>
            <c:param name="taxFormulaID" value="${TaxTable.itaxFormulaId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/TaxTableShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxTableShowParm" value="delete"/>
            <c:param name="taxFormulaID" value="${TaxTable.itaxFormulaId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/TaxTableShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="taxTableShowParm" value="update"/>
            <c:param name="taxFormulaID" value="${TaxTable.itaxFormulaId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="itaxYear" title="Year" sortable="true" />
	   <display:column property="tbltaxformulatype.sformulaDescription" title="Tax Formula Type" sortable="true"/>
	   <display:column property="dtaxRate" title="Tax Rate" format="{0,number,#.####}" sortable="true"/>
	 
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
