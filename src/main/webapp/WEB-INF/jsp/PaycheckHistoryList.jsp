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

<html:form action="PaycheckHistoryListAction">

  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	 
	 <colgroup>
	    <col width="45%">
	    <col width="35%">
	    <col width="20%">	    
	 </colgroup>
	 
	 <TR>
	   	    
	    <td class="tableDataRight">
	       <pas:label value="Employer"/>
	    </td>    
	    <td ALIGN="left">	       
	       <html:select name="paycheckHistoryListForm" property="ijobID" tabindex="1">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="Jobs" property="id" labelProperty="description" />					    	   
		   </html:select>			        	  		   
	    </td>	    
	    	    
	    <td ALIGN="left">	       
	       <html:submit property="operation" value="Re-submit" tabindex="7"/>
	    </td>
	    
	 </TR>	 	 
     	 
  </TABLE>
  
  <c:if test="${PaycheckHistoryList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
     
    <display:table name="sessionScope.PaycheckHistoryList" export="true" uid="paycheckHistory" requestURI="">        
       
       <display:column title="View">
          <c:url value="/PaycheckHistoryShowUpdateFormAction.do" var="viewURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckHistoryShowParm" value="inquire"/>
            <c:param name="paycheckHistoryID" value="${paycheckHistory.ipaydayHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${viewURL}'/>">
             View
          </a>      
       </display:column>
       
       <display:column title="Del">
          <c:url value="/PaycheckHistoryShowUpdateFormAction.do" var="deleteURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckHistoryShowParm" value="delete"/>
            <c:param name="paycheckHistoryID" value="${paycheckHistory.ipaydayHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${deleteURL}'/>">
             Del
          </a>      
       </display:column>
       
       <display:column title="Chg">
          <c:url value="/PaycheckHistoryShowUpdateFormAction.do" var="updateURL">
            <c:param name="operation" value="inquire"/>
            <c:param name="paycheckHistoryShowParm" value="update"/>
            <c:param name="paycheckHistoryID" value="${paycheckHistory.ipaydayHistoryId}"/>         
          </c:url>
          <a href="<c:out value='${updateURL}'/>">
             Chg
          </a>      
       </display:column>
       
       <display:column property="dpaydayHistoryDate" title="Date" format="{0,date,yyyy-MM-dd}" sortable="true"/>
	   <display:column property="mgrossPay" title="Gross Pay"  format="{0,number,currency}"/>
	   <display:column property="mfederalWithholding" title="Federal Withholding"  format="{0,number,currency}"/>
	   <display:column property="mstateWithholding" title="State Withholding"  format="{0,number,currency}"/>
       
       <display:setProperty name="sort.behavior" value="list" />
 	   <display:setProperty name="paging.banner.include_first_last" value="" />   
 	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
