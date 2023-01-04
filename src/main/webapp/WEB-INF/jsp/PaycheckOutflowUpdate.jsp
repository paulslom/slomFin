<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>
 
   <script language="JavaScript">
     
   function populateHidden()
   {
      var trxTypeIDElem = document.getElementsByName('transactionTypeID')[0];
      var trxTypeTextElem = document.getElementsByName('trxTypeText')[0];
	  var selectedTrxType = trxTypeIDElem.options[trxTypeIDElem.selectedIndex].text;
	  //alert("selectedTrxType = " + selectedTrxType);
	  trxTypeTextElem.value=selectedTrxType;
	  //alert("trxTypeTextElem.value = " + trxTypeTextElem.value);
   }
   </script>
   
</HEAD>

<BODY>

<html:form action="PaycheckOutflowUpdateAction">

  <html:hidden property="paycheckOutflowID"/>
  <html:hidden property="trxTypeText" value=""/>
      
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${paycheckOutflowShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="PaycheckOutflow ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="paycheckOutflowID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="paycheckOutflowUpdateForm" property="accountID" tabindex="1">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="Accounts" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	           <pas:text property="accountName" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Transaction Type"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="paycheckOutflowUpdateForm" property="transactionTypeID" tabindex="2" onchange="javascript:populateHidden()">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="TrxTypes" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>                                     
	        <td ALIGN="left">	       
	           <pas:text property="transactionTypeDesc" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Transfer Account"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="paycheckOutflowUpdateForm" property="xferAccountID" tabindex="3">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="Accounts" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>                                     
	        <td ALIGN="left">	       
	           <pas:text property="xferAccountName" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Withdrawal Category"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="paycheckOutflowUpdateForm" property="wdCategoryID" tabindex="4">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="WDCategories" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>                                     
	        <td ALIGN="left">	       
	           <pas:text property="wdCategoryDesc" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Cash Deposit Type"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="paycheckOutflowUpdateForm" property="cashDepositTypeID" tabindex="5">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="CashDepositTypes" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>                                     
	        <td ALIGN="left">	       
	           <pas:text property="cashDepositTypeDesc" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Default Amount"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="defaultAmount" textbox="true" readonly="false" width="150" tabindex="6"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="defaultAmount" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Description"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="description" textbox="true" readonly="false" width="150" tabindex="8"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="description" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Default Day"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="defaultDay" textbox="true" readonly="false" width="150" tabindex="9"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="defaultDay" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	
	<TR>
	    <td ALIGN="right">
	        <pas:label value="Next Month Ind"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm=='update'}">
	    	<td ALIGN="left">	       
	       	  <html:checkbox property="nextMonthInd" tabindex="10"/>
	    	</td>
	      </c:when>
	      <c:otherwise>    
	        <td ALIGN="left">	       
	          <pas:text property="nextMonthInd" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	
	</TR>	   
  
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${paycheckOutflowShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${paycheckOutflowShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${paycheckOutflowShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${paycheckOutflowShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${paycheckOutflowShowParm=='add' || paycheckOutflowShowParm =='update'}">
     <script language="javascript">
	     document.forms.paycheckOutflowUpdateForm.description.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
