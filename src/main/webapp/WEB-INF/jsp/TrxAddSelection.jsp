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
  
  function addTransaction()
  {
	  var ownedElement = document.getElementById("ownedID");
      var owned = ownedElement.checked;
      
      //alert('owned checked: ' + owned);
      
      var accElement = document.getElementById("accountid");
      var accountid = accElement.value;
      
      var trxTypeElement = document.getElementById("idSelectTrxType");
      var trxTypeId = trxTypeElement.value;
      var trxTypeDesc = trxTypeElement.options[trxTypeElement.selectedIndex].text;
    
      if (trxTypeId == '0')
      {
    	 alert('no trx type selected');
    	 return;
      }
      
      var invTypeElement = document.getElementById("idSelectInvType");
      var invTypeId = invTypeElement.value;
    
      if (invTypeId == '0')
      {
    	 alert('no investment type selected');
    	 return;
      }
      
      if (trxTypeDesc == 'Reinvest' 
      ||  trxTypeDesc == 'Split' 
      ||  trxTypeDesc == 'Sell' 
      ||  trxTypeDesc == 'Cash Dividend' 
      ||  trxTypeDesc == 'Exercise Option' 
      ||  trxTypeDesc == 'Expire Option')
      {
    	  owned = "true";
      }
      
      var actionString = "<%=request.getContextPath()%>/TrxShowUpdateFormAction.do?operation=inquire&trxShowParm=add&acctID="  + accountid + "&owned=" + owned + "&ttID=" + trxTypeId + "&itID=" + invTypeId;  
      
      //alert('action we will use: ' + actionString);
      
      document.forms[0].action=actionString;
      document.forms[0].submit();      
  }

</script>

<BODY>

<html:form action="TrxListAction">

  <BR/>
  <BR/>
   
  <input type="hidden" id="accountid" value="${trxAddAcctID}"/>
   
  <TABLE WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER="0">
	
	<colgroup>
	    <col width="50%">
	    <col width="50%">
	 </colgroup>
	 
	 <TR>
     
	    <td class="tableDataRight">
	        Add Transaction selections
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
	        Pick transaction type
	    </td>
	 
	   	<td ALIGN="left">	       
	      <select id="idSelectTrxType" name="transactionTypeID" tabindex="1">
	          <option value="0">Select</option>	
	          <c:forEach items="${TrxTypes}" var="trxTypes">
	            <option value="${trxTypes.id}">${trxTypes.description}</option>
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
	        Pick investment type
	    </td>
	 
	   	<td ALIGN="left">	       
	      <select id="idSelectInvType" name="investmentTypeID" tabindex="1">
	          <option value="0">Select</option>	
	          <c:forEach items="${InvestmentTypes}" var="invTypes">
	            <option value="${invTypes.id}">${invTypes.description}</option>
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
	      <input type="checkbox" id="ownedID" name="owned" value=""> 
	      <label for="owned">Investment owned already?</label><br>
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
		   <html:button property="addTrx" value="Add New Transaction" onclick="addTransaction()"/>
		</TD>
		<td>		  
		</td>
		 	
	</TR>
	
	
  </TABLE>	 
        
</html:form>

</BODY> 
