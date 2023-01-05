<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <link rel="stylesheet" type="text/css" href="styles/displaytag.css">
   <script type="text/javascript" src="scripts/common.js"></script>
      
</HEAD>

<script type="text/javascript">
  
  function addAccount()
  {
	  document.forms[0].action="<%=request.getContextPath()%>/AccountShowUpdateFormAction.do?&operation=inquire&accountShowParm=add&portfolioID=" + document.getElementById("portID").value;
      document.forms[0].submit();      
  }
  
  function updateAccount()
  {
	  var ddElement = document.getElementById("idSelectAccount");
	  var accountid = ddElement.value;
	    
	  if (accountid == '0')
	  {
	  	alert('no account selected');
	  	return;
	  }
	  
      document.forms[0].action="<%=request.getContextPath()%>/AccountShowUpdateFormAction.do?operation=inquire&accountShowParm=update&accountID=" + accountid;
      document.forms[0].submit();      
  }
  
  function listTransactions()
  {
    var ddElement = document.getElementById("idSelectAccount");
    var accountid = ddElement.value;
    
    if (accountid == '0')
    {
    	alert('no account selected');
    	return;
    }
    
    document.forms[0].action="<%=request.getContextPath()%>/TrxListAction.do?operation=inquire&trxListOrigin=1&acctID=" + accountid;
    document.forms[0].submit();      
  }
  
  function addTransaction()
  {
	  var ddElement = document.getElementById("idSelectAccount");
      var accountid = ddElement.value;
    
      if (accountid == '0')
      {
    	alert('no account selected');
    	return;
      }
      
      document.forms[0].action="<%=request.getContextPath()%>/TrxAddSelectionAction.do?operation=inquire&acctID="  + accountid;
      document.forms[0].submit();      
  }

</script>

<BODY>

<html:form action="TrxListAction">

  <BR/>
  <BR/>
   
  <input type="hidden" id="portID" value="${portfolioID}"/>
   
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER="0">
	
	<colgroup>
	    <col width="50%">
	    <col width="50%">
	 </colgroup>
	 
	 <TR>
     
	    <td class="tableDataRight">
	        Account Selection
	    </td>
	 
	   	<td ALIGN="left">	       
	        
	    </td>
	      
	 </TR>
	 
	 <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
     <TR>
     
	    <td class="tableDataRight">
	        Pick an account
	    </td>
	 
	   	<td ALIGN="left">	       
	      <select id="idSelectAccount" name="accountID" tabindex="1">
	          <option value="0">Select</option>	
	          <c:forEach items="${Accounts}" var="accts">
	            <option value="${accts.id}">${accts.description}</option>
	          </c:forEach>			    	   
		  </select>
	    </td>
	      
	 </TR>
	 
	  <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
	 <TR>	
		
		<td class="tableDataRight">
		   <html:button property="listTrx" value="List Transactions for selected account" onclick="listTransactions()"/>
		</TD>
		<td>
		   <c:if test="${showTrxItems==true}">
		      <html:button property="addTrx" value="Add New Transaction" onclick="addTransaction()"/>
		   </c:if>
		</td>
		 	
	</TR>
	
	<TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 <TR>
	 	<td colspan="2">
	 	    <BR/>
	 	</td>
	 </TR>
	 
	 <c:if test="${showTrxItems==true}">
		 <TR>	
			
			<td class="tableDataRight">
			   <html:button property="addAcct" value="Add New Account" onclick="addAccount()"/>
			</TD>
			<td>
			   <html:button property="updateAcct" value="Update Selected Account" onclick="updateAccount()"/>
			</td>
			 	
		 </TR>
	 </c:if>
	 
  </TABLE>	 
        
</html:form>

</BODY> 
