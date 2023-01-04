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

<html:form action="TaxGroupYearUpdateAction">

  <html:hidden property="taxGroupYearID"/>
      
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${tgyShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="TaxGroupYear ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="taxGroupYearID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>	 
	
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Group Name"/>
	    </td>
	    <td ALIGN="left">	       
	         <html:select name="taxGroupYearUpdateForm" property="taxGroupID" disabled="true">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="TaxGroups" property="id" labelProperty="description" />					    	   
			  </html:select>
	    </td>	     
	 </TR>
		
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Year"/>
	    </td>
	   <c:choose>
          <c:when test="${tgyShowParm=='add'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="taxYear" textbox="true" readonly="false" width="150" tabindex="1"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="taxYear" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	   	     
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Filing Status"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="filingStatus" textbox="true" readonly="false" width="150" tabindex="2"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="filingStatus" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	   
	 </TR>
	 
		 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Dependents"/>
	    </td>
	   <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dependents" textbox="true" readonly="false" width="150" tabindex="3"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="dependents" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Capital Loss Carryover"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="capitalLossCarryover" textbox="true" readonly="false" width="150" tabindex="4"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="capitalLossCarryover" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 		    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="IRA Distribution"/>
	    </td>
	   <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="iraDistribution" textbox="true" readonly="false" width="150" tabindex="5"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="iraDistribution" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 		    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Previous Year State Refund"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="prevYearStateRefund" textbox="true" readonly="false" width="150" tabindex="6"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="prevYearStateRefund" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Other Itemized"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="otherItemized" textbox="true" readonly="false" width="150" tabindex="7"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="otherItemized" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Other Itemized Desc"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="otherItemizedDesc" textbox="true" readonly="false" width="150" tabindex="8"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="otherItemizedDesc" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Car Tax"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="carTax" textbox="true" readonly="false" width="150" tabindex="9"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="carTax" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Dividend Tax Rate"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dividendTaxRate" textbox="true" readonly="false" width="150" tabindex="10"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="dividendTaxRate" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Other Income"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="otherIncome" textbox="true" readonly="false" width="150" tabindex="11"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="otherIncome" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Other Income Desc"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="otherIncomeDesc" textbox="true" readonly="false" width="150" tabindex="12"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="otherIncomeDesc" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	   
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Daycare Expenses Paid"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dayCareExpensesPaid" textbox="true" readonly="false" width="150" tabindex="13"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="dayCareExpensesPaid" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Qualified Dividends"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="qualifiedDividends" textbox="true" readonly="false" width="150" tabindex="14"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="qualifiedDividends" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Foreign Tax Paid on Dividends"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dividendsForeignTax" textbox="true" readonly="false" width="150" tabindex="15"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="dividendsForeignTax" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Return Of Capital on Dividends"/>
	    </td>
	    <c:choose>
          <c:when test="${tgyShowParm=='add' || tgyShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dividendsReturnOfCapital" textbox="true" readonly="false" width="150" tabindex="16"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="dividendsReturnOfCapital" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>
	      
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${tgyShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${tgyShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${tgyShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${tgyShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${tgyShowParm=='add'}">
     <script language="javascript">
	     document.forms.taxGroupYearUpdateForm.taxYear.select();	      
	 </script>
  </c:if>
  
  <c:if test="${tgyShowParm =='update'}">
     <script language="javascript">	   
	     document.forms.taxGroupYearUpdateForm.filingStatus.select();	  
	 </script>
  </c:if>
  
</html:form>

</BODY> 
