<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <script type="text/javascript" src="scripts/ajax_search.js"></script>
   <script type="text/javascript" src="scripts/common.js"></script>
   <script type="text/javascript" src="scripts/inputdate.js"></script>

   <script language="JavaScript">
   
   function calcCostProceeds()
   {
      var tempTrxAmount;
      var tempUnits;
      var tempPrice;
   
      var elemUnits = document.getElementsByName('decUnits')[0];
      var elemPrice = document.getElementsByName('price')[0];
      var elemCostProceeds = document.getElementsByName('costProceeds')[0];
      
	  //alert("for debugging");
	 
      tempUnits = elemUnits.value;
      tempPrice = elemPrice.value;
      
      if ((IsNumeric(tempUnits) == true) && (IsNumeric(tempUnits) == true))
      { 
         tempTrxAmount = tempUnits * tempPrice; 
         elemCostProceeds.value = round_decimals(tempTrxAmount, 2);      		
      }
   }
   
   </script> 
</HEAD>

<BODY>

<html:form action="TrxUpdateAction">

  <html:hidden property="trxListOrigin" value="4"/>
  <html:hidden property="transactionID"/>
  <html:hidden property="portfolioName"/>
  <html:hidden property="accountName"/>
  <html:hidden property="trxTypeDesc"/>
  <html:hidden property="invTypeDesc"/>
  <html:hidden property="accountID"/>
  <html:hidden property="transactionTypeID"/>
     
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${trxShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Transaction ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="transactionID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Portfolio"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="portfolioName" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="accountName" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Trx Type"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="trxTypeDesc" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Inv Type"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="invTypeDesc" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	<c:if test="${trxShowInvestmentID==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Investment"/>
		    </td>
		    <c:choose>
		      <c:when test="${(trxShowParm=='add' || trxShowParm =='update') && trxShowCashFields==false}">
		    	<td ALIGN="left">	       
		       	  <html:select name="trxUpdateForm" property="investmentID" tabindex="1">
				    <html:option value="0">-Select-</html:option>
				    <html:options collection="Investments" property="id" labelProperty="description" />					    	   
				  </html:select>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		       		<pas:text property="invDesc" textbox="false" readonly="true"/>
		    	</td>
		      </c:otherwise>   
		    </c:choose> 
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Trx Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="transactionDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="transactionDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Posted Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="transactionPostedDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="transactionPostedDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <c:if test="${trxShowUnits==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Units"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="decUnits" textbox="true" readonly="false" width="100" tabindex="5"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:text property="decUnits" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    
		 </TR>
	 </c:if>
	 
	 <c:if test="${trxShowPrice==true}">	 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Price"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	   <pas:text property="price" textbox="true" readonly="false" onchange="javascript:calcCostProceeds()" width="150" tabindex="6"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="price" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    		    
		 </TR>
	 </c:if>
	 
	 <c:if test="${trxShowAmount==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="costProceeds" textbox="true"  readonly="false" width="150" tabindex="7"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="costProceeds" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    		    
		 </TR>
	 </c:if>
  
     <c:if test="${trxShowCheckNo==true}">
  	    <TR>
	       <td ALIGN="right">
	           <pas:label value="Check Number"/>
	       </td>
	       <c:choose>
	         <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	   <td ALIGN="left">	       
	       	      <pas:text property="checkNo" textbox="true" readonly="false" width="50" tabindex="8"/>
	    	   </td>
	         </c:when>
	         <c:otherwise>
	           <td ALIGN="left">	       
	              <pas:text property="checkNo" textbox="false" readonly="true"/>
	           </td>
	         </c:otherwise>   
	       </c:choose> 	    
	    </TR>
	 </c:if>
	 
	  <c:if test="${trxShowDividendTaxableYear==true}">
  	    <TR>
	       <td ALIGN="right">
	           <pas:label value="Dividend Taxable Year"/>
	       </td>
	       <c:choose>
	         <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	   <td ALIGN="left">	       
	       	      <pas:text property="dividendTaxableYear" textbox="true" readonly="false" width="50" tabindex="9"/>
	    	   </td>
	         </c:when>
	         <c:otherwise>
	           <td ALIGN="left">	       
	              <pas:text property="dividendTaxableYear" textbox="false" readonly="true"/>
	           </td>
	         </c:otherwise>   
	       </c:choose> 	    
	    </TR>
	 </c:if>
	 
	 <c:if test="${trxShowCashDepositType==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Cash Deposit Type"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <html:select name="trxUpdateForm" property="cashDepositTypeID" tabindex="10">
				    <html:option value="0">-Select-</html:option>
				    <html:options collection="CashDepositTypes" property="id" labelProperty="description" />					    	   
				  </html:select>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:text property="cashDepositTypeDescription" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose>			    
		 </TR>
	 </c:if>
	 
	 <c:if test="${trxShowWDCategory==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Withdrawal Category"/>
		    </td> 
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <html:select name="trxUpdateForm" property="wdCategoryID" tabindex="11">
				    <html:option value="0">-Select-</html:option>
				    <html:options collection="WDCategories" property="id" labelProperty="description" />					    	   
				  </html:select>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:text property="categoryDescription" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose>		   			    
		 </TR>
	 </c:if>
	 
	 <c:if test="${trxShowXferAccount==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Transfer Account"/>
		    </td> 
		   	<td ALIGN="left">	       
		        <html:select name="trxUpdateForm" property="xferAccountID" tabindex="12">
					<html:option value="0">-Select-</html:option>
					<html:options collection="XferAccounts" property="id" labelProperty="description" />					    	   
				</html:select>
		    </td>		      	   			    
		 </TR>
	 </c:if>
	 
	 <c:if test="${trxShowCashDescription==true}"> 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Description"/>
		    </td>
		    <c:choose>
	          <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	    <td ALIGN="left">	       
	               <pas:text property="cashDescription" readonly="false" tabindex="13" width="500" size="50" autocomplete="off" onkeyup="searchSuggest(this)" onclick="setSearch(this.value)"/>
	            </td>	    
	  	        <div id="search_suggest" style="float: right; font-family: Arial, Helvetica, sans-serif; font-size: 8pt; background-color:#f2f2e6; text-align:left;">
                </div>	 
	          </c:when>
	          <c:otherwise>
	            <td ALIGN="left">	       
		           <pas:text property="cashDescription" textbox="false" width="500" readonly="true"/>
		        </td>
	          </c:otherwise>   
	        </c:choose> 	       			    
		 </TR>
	 </c:if>
	 
	  <c:if test="${trxShowLastBillingCycleYN==true}"> 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Last Trx of Billing cycle"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <html:checkbox property="finalTrxOfBillingCycle" tabindex="19"/>
		    	</td>
		      </c:when>
		      <c:otherwise>    
		        <td ALIGN="left">	       
		          <pas:text property="finalTrxOfBillingCycle" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	
		 </TR>
	 </c:if>
		 
	 
	 <c:if test="${trxShowOptionFields==true}">
	     
	     <TR>
		    <td ALIGN="right">
		        <pas:label value="Option Type"/>
		    </td>
		    <c:choose>
			   <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
			      <td ALIGN="left">	       
			      	  <html:select name="trxUpdateForm" property="optionTypeID" tabindex="14">
					    <html:option value="0">-Select-</html:option>
					    <html:options collection="OptionTypes" property="id" labelProperty="description" />			    	   
					  </html:select>
			      </td>
			   </c:when>
			   <c:otherwise>
			      <td ALIGN="left">	       
		            <pas:text property="optionTypeDesc" textbox="false" readonly="true"/>
		          </td>
			   </c:otherwise>   
			</c:choose>		       		    
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Strike Price"/>
		    </td>
		    <c:choose>
	          <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
	    	    <td ALIGN="left">	       
	       	       <pas:text property="strikePrice" textbox="true" readonly="false" width="150" tabindex="15"/>
	    	    </td>
	          </c:when>
	          <c:otherwise>
	            <td ALIGN="left">	       
		          <pas:amount property="strikePrice" textbox="false" readonly="true"/>
		        </td>
	          </c:otherwise>   
	        </c:choose> 	    
		    
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Expiration Date"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:inputdate property="expirationDate" tabindex="16"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		          <pas:date property="expirationDate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		  
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Opening Trx"/>
		    </td>
		    <c:choose>
		      <c:when test="${trxShowParm=='add' || trxShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <html:checkbox property="openingTrxInd" tabindex="19"/>
		    	</td>
		      </c:when>
		      <c:otherwise>    
		        <td ALIGN="left">	       
		          <pas:text property="openingTrxInd" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	
		 </TR>
		 	
	 </c:if>	  	 
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${trxShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${trxShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${trxShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${trxShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${trxShowParm=='add'}">
    <script language="javascript">
        document.getElementById("trxListOrigin").value="5"
    </script>	
    <c:choose>
	    <c:when test="${trxShowInvestmentID==true}">
		    <script language="javascript">			  
			   document.forms.trxUpdateForm.investmentID.select(); 			   	  
		    </script>
	   </c:when>	    
       <c:otherwise>
	       <script language="javascript">	          
	           document.forms.trxUpdateForm.costProceeds.select(); 	      
		   </script>
	   </c:otherwise>	   
	</c:choose>	  
  </c:if>
  
</html:form>

</BODY> 
