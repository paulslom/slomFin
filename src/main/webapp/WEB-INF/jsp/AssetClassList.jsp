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

<html:form action="AssetClassListAction">

  <c:if test="${AssetClassList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.AssetClassList" export="true" uid="AssetClass" requestURI="">        
       
       <display:column title="View">
          <c:url value="/AssetClassShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="assetClassShowParm" value="inquire"/>
            <c:param name="assetClassID" value="${AssetClass.iassetClassId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
              
       <display:column title="Chg">
          <c:url value="/AssetClassShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="assetClassShowParm" value="update"/>
            <c:param name="assetClassID" value="${AssetClass.iassetClassId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="sassetClass" title="Asset Class Name"  sortable="true" />
        
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
