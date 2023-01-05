<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>
    
</HEAD>

<BODY>

<script language="JavaScript">

	function trColors(ckBox)
	{ 
	  ckBoxClicked = ckBox;	  
	  	   
	  //go up 2 levels to change the row color -- parent of checkbox is TD element, parent of TD is the TR
	  rowElement = ckBoxClicked.parentNode.parentNode
	  	  
	  if (rowElement.className == "rowGreen")
	  { 	    
	  	rowElement.className = "rowRed";
	  }
	  else
	  {	
	    rowElement.className = "rowGreen";
	  }
	    
	}

</script>

<html:form action="PaycheckAddAction1">
   
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
		 
	 <TR>
	 
	   <TD>

		  <TABLE align="center" id="pcoTable" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
			 
			 <colgroup>
			    <col width="5%">
			    <col width="15%">
			    <col width="10%">
			    <col width="15%">
			    <col width="10%">
			    <col width="5%">
			    <col width="25%">
			    <col width="15%">
			 </colgroup>	
			 
			 <TR>
			 	<td align="center">Y/N</td>
			 	<td align="center">Account</td>
			 	<td align="center">TrxType</td>
			 	<td align="center">Date</td>
			 	<td align="center">Amount</td>
			 	<td align="center">ChkNo</td>
			 	<td align="center">Description</td>		 	
			 	<td align="center">Xfer To</td>
			 </TR>
			 
			 <c:forEach var="pcoAddItem" items="${sessionScope.paycheckAddForm.pcoAddList}">
			   <c:choose>
			     <c:when test="${pcoAddItem.processInd==true}">
			       <c:set var="rowclassjstl" value="rowGreen"/>
			     </c:when>
			     <c:otherwise>
			       <c:set var="rowclassjstl" value="rowRed"/>
			     </c:otherwise>
			   </c:choose>
			   <TR class="<c:out value="${rowclassjstl}"/>">
			      <td align="center">
			      	<html:checkbox name="pcoAddItem" indexed="true" property="processInd" onclick="javascript:trColors(this)"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.accountName}"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.transactionTypeDesc}"/>		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="pcoAddDate" size="8" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="amount" size="5" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="checkNo" size="3" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="description" size="20"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.xferAccountName}"/>		      	 
			      </td>			      		     
			   </TR>    			
			</c:forEach>	
		  
		  </TABLE>
		  
		</TD>
		
	  </TR>	  
	  	 
	  <TR>
		  <TD align="center">
			<html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		  	<html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
  	  </TR>
 
  </TABLE>
     
</html:form>

</BODY> 
