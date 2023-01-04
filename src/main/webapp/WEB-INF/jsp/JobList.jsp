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

<html:form action="JobListAction">

  <c:if test="${JobList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
    <display:table name="sessionScope.JobList" export="true" uid="Job" requestURI="">        
       
       <display:column title="View">
          <c:url value="/JobShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="jobShowParm" value="inquire"/>
            <c:param name="jobID" value="${Job.ijobId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/JobShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="jobShowParm" value="delete"/>
            <c:param name="jobID" value="${Job.ijobId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/JobShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="jobShowParm" value="update"/>
            <c:param name="jobID" value="${Job.ijobId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="semployer" title="Employer"  sortable="true" />
       <display:column property="djobStartDate" title="Start Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="djobEndDate" title="End Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="mgrossPay" title="Gross Pay"  format="{0,number,currency}"/>
       <display:column property="sincomeState" title="Income State" />
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
