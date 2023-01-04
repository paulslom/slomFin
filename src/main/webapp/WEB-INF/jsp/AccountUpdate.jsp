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

<html:form action="AccountUpdateAction">

  <html:hidden property="accountID"/>
        
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${accountShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Account ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="accountID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Portfolio"/>
	    </td>
	    <c:choose>
	      <c:when test="${(accountShowParm=='add' || accountShowParm =='update')}">
	    	<td ALIGN="left">	       
	       	  <html:select name="accountUpdateForm" property="portfolioID" tabindex="1">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="Portfolios" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	       		<pas:text property="portfolioName" textbox="false" readonly="true"/>
	    	</td>
	      </c:otherwise>   
	    </c:choose> 
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Broker"/>
	    </td>
	    <c:choose>
	      <c:when test="${(accountShowParm=='add' || accountShowParm =='update')}">
	    	<td ALIGN="left">	       
	       	  <html:select name="accountUpdateForm" property="brokerID" tabindex="2">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="Brokers" property="id" labelProperty="description" />			    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	       		<pas:text property="brokerName" textbox="false" readonly="true"/>
	    	</td>
	      </c:otherwise>   
	    </c:choose> 
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account Type"/>
	    </td>
	    <c:choose>
	      <c:when test="${(accountShowParm=='add' || accountShowParm =='update')}">
	    	<td ALIGN="left">	       
	       	  <html:select name="accountUpdateForm" property="accountTypeID" tabindex="1">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="AccountTypes" property="id" labelProperty="description" />				    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	       		<pas:text property="accountTypeDescription" textbox="false" readonly="true"/>
	    	</td>
	      </c:otherwise>   
	    </c:choose> 
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account Name"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="accountName" textbox="true" readonly="false" width="150" size="40" tabindex="3"/>
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
	        <pas:label value="Account Name Abbreviation"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="accountNameAbbr" textbox="true" readonly="false" width="150" size="10" tabindex="4"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="accountNameAbbr" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Starting Check No"/>
	    </td>
	    <c:choose>
	      <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="startingCheckNo" textbox="true" readonly="false" width="50" tabindex="5"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:text property="startingCheckNo" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Account Number"/>
	    </td>
	    <c:choose>
	      <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="accountNumber" textbox="true" readonly="false" width="150" size="20" tabindex="6"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:text property="accountNumber" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="PIN"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="pin" textbox="true" readonly="false" width="50" size="20" tabindex="7"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="pin" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Closed Indicator"/>
	    </td>
	    <c:choose>
	      <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:checkbox property="closed" tabindex="8"/>
	    	</td>
	      </c:when>
	      <c:otherwise>    
	        <td ALIGN="left">	       
	          <pas:text property="closed" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	
	 </TR>
		   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Interest Payments per Year"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="interestPaymentsPerYear" textbox="true" readonly="false" width="50" tabindex="9"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="interestPaymentsPerYear" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Interest Rate"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="interestRate" textbox="true" readonly="false" width="50" tabindex="10"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="interestRate" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Minimum Interest Balance"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="minInterestBalance" textbox="true" readonly="false" width="150" tabindex="11"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="minInterestBalance" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Taxable Indicator"/>
	    </td>
	    <c:choose>
	      <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:checkbox property="taxable" tabindex="12"/>
	    	</td>
	      </c:when>
	      <c:otherwise>    
	        <td ALIGN="left">	       
	          <pas:text property="taxable" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="New Money Per Year"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="newMoneyPerYear" textbox="true" readonly="false" width="150" tabindex="13"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="newMoneyPerYear" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Estimated Rate of Return"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="estimatedRateofReturn" textbox="true" readonly="false" width="150" tabindex="14"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="estimatedRateofReturn" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Vesting Percentage"/>
	    </td>
	    <c:choose>
          <c:when test="${accountShowParm=='add' || accountShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="vestingPercentage" textbox="true" readonly="false" width="150" tabindex="15"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="vestingPercentage" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 
	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${accountShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${accountShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${accountShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${accountShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${accountShowParm=='add' || accountShowParm =='update'}">
     <script language="javascript">	    
	     document.forms.accountUpdateForm.portfolioID.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
