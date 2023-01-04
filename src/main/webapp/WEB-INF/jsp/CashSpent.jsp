<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/ajax_search.js"></script>
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>   

</HEAD>

<BODY>

<html:form action="CashSpentAction">

  <html:hidden property="trxListOrigin" value="2"/>
  <html:hidden property="accountID"/>
  <html:hidden property="transactionTypeID"/>
  
    
  <div id="search_suggest" style="float: right; font-family: Arial, Helvetica, sans-serif; font-size: 8pt; background-color:#f2f2e6; text-align:left;">
  </div>
     
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	 
	 <colgroup>
	    <col width="20%">
	    <col width="80%">
	 </colgroup>
	 
	 <TR>
	    <td align="left" colspan="2">
	      <IMG src="<c:url value="/images/${sessionScope.cashSpentPictureFile}"/>"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account ID"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="accountID" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account Name"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="accountName" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Trx Type ID"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="transactionTypeID" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Trx Type Desc"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="transactionTypeDesc" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Trx Date"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:inputdate property="transactionDate" tabindex="1"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Posted Date"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:inputdate property="transactionPostedDate" tabindex="4"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Amount"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="costProceeds" id="costProceeds" readonly="false" tabindex="7"/>
	    </td>
	 </TR>
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="WD Category"/>
	    </td>    
	    <td ALIGN="left">	       
	        <html:select name="cashSpentForm" property="wdCategoryID" tabindex="8">
			   <html:option value="">-Select-</html:option>
			   <html:options collection="WDCategories" property="id" labelProperty="description" />			   
			</html:select>
	    </td>
	 </TR>
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Description"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="cashDescription" readonly="false" tabindex="9" width="350" autocomplete="off" onkeyup="searchSuggest(this)" onclick="setSearch(this.value)"/>
	    </td>	    
	 </TR>
	      	 
  </TABLE>
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
		<TD align="center">
		   <html:submit property="operation" tabindex="10" value="Add Cash Transaction" />
		</TD>
		<TD align="center">
		   <html:submit property="operation" tabindex="11" value="Add then Add another Cash Transaction" />
		</TD>
		<TD align="center">
		   <html:cancel>Cancel</html:cancel>
		</TD>
	</TR>		 
  </TABLE>
  
  
  
  <script language="javascript">
      document.forms.cashSpentForm.costProceeds.select();           
  </script>
  
</html:form>

</BODY> 
