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

<html:form action="PortfolioListAction">

  <c:if test="${PortfolioList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.PortfolioList" export="true" uid="Portfolio" requestURI="">        
       
       <display:column title="View">
          <c:url value="/PortfolioShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="portShowParm" value="inquire"/>
            <c:param name="portfolioID" value="${Portfolio.iportfolioId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
              
       <display:column title="Chg">
          <c:url value="/PortfolioShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="portShowParm" value="update"/>
            <c:param name="portfolioID" value="${Portfolio.iportfolioId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="sportfolioName" title="Portfolio Name"  sortable="true" />
       <display:column property="btaxableInd" title="Taxable" sortable="true" />
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
