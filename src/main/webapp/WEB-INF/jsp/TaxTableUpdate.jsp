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

<html:form action="TaxTableUpdateAction">

  <html:hidden property="taxFormulaID"/>
      
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Formula ID"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="taxFormulaID" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	  <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Year"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="taxYear" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Tax Formula Type"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="taxFormulaTypeDesc" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 
	 <c:if test="${tRate==true}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Low Threshold"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="incomeLow" textbox="true" readonly="false" width="100" tabindex="1"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="incomeLow" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="High Threshold"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="incomeHigh" textbox="true" readonly="false" width="100" tabindex="2"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="incomeHigh" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Tax Rate"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="taxRate" textbox="true" readonly="false" width="100" tabindex="3"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:text property="taxRate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Fixed Tax Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="fixedTaxAmount" textbox="true" readonly="false" width="100" tabindex="4"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="fixedTaxAmount" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	    
		 </TR>
			 
	 </c:if>	 
	
	 <c:if test="${tAmount==true && (tNC==true || tFederal==true)}">	 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Child Credit Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="childCredit" textbox="true" readonly="false" width="100" tabindex="5"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="childCredit" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Standard Deduction"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="standardDeduction" textbox="true" readonly="false" width="100" tabindex="6"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="standardDeduction" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
	 </c:if>
	 
	<c:if test="${tAmount==true && tFederal==true}">	 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Exemption Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="exemption" textbox="true" readonly="false" width="100" tabindex="7"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="exemption" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Social Security Wage Limit"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="socialSecurityWageLimit" textbox="true" readonly="false" width="100" tabindex="14"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="socialSecurityWageLimit" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Federal Daycare Credit Rate"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="federalDaycareCreditRate" textbox="true" readonly="false" width="100" tabindex="15"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="federalDaycareCreditRate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Federal Recovery Credit Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="federalRecoveryAmount" textbox="true" readonly="false" width="100" tabindex="15"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="federalRecoveryAmount" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 
	</c:if>	 	 
	
	<c:if test="${tAmount==true && tCT==true}">	 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="State Tax Credit Rate"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="stateTaxCreditRate" textbox="true" readonly="false" width="100" tabindex="8"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:text property="stateTaxCreditRate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="State Property Tax Limit"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="statePropertyTaxLimit" textbox="true" readonly="false" width="100" tabindex="9"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="statePropertyTaxLimit" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
	</c:if>	 	 
	
	<c:if test="${tAmount==true && tNC==true}">	 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC Exemption Threshold"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncExemptionThreshold" textbox="true" readonly="false" width="100" tabindex="9"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncExemptionThreshold" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC Over Exemption Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncOverExemptionAmount" textbox="true" readonly="false" width="100" tabindex="10"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncOverExemptionAmount" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC Under Exemption Amount"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncUnderExemptionAmount" textbox="true" readonly="false" width="100" tabindex="11"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncUnderExemptionAmount" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC ChildCredit Threshold"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncChildCreditThreshold" textbox="true" readonly="false" width="100" tabindex="12"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncChildCreditThreshold" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC 529 Deduction"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="nc529DeductionAmount" textbox="true" readonly="false" width="100" tabindex="13"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="nc529DeductionAmount" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC Daycare Credit Rate"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncDaycareCreditRate" textbox="true" readonly="false" width="100" tabindex="14"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncDaycareCreditRate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="NC Surtax Rate"/>
		    </td>
		    <c:choose>
		      <c:when test="${taxTableShowParm =='update'}">
		    	<td ALIGN="left">	       
		       	  <pas:text property="ncSurtaxRate" textbox="true" readonly="false" width="100" tabindex="15"/>
		    	</td>
		      </c:when>
		      <c:otherwise>
		        <td ALIGN="left">	       
		           <pas:amount property="ncSurtaxRate" textbox="false" readonly="true"/>
		        </td>
		      </c:otherwise>   
		    </c:choose> 	      
		 </TR>
		 
	</c:if>	 	 
	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${taxTableShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${taxTableShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${taxTableShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${taxTableShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
    
</html:form>

</BODY> 
