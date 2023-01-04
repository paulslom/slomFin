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

<html:form action="MortgageUpdateAction">

  <html:hidden property="mortgageID"/>
      
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${mortgageShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Mortgage ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="mortgageID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Bank"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="description" textbox="true" readonly="false" size="50" width="250" tabindex="1"/>
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
	        <pas:label value="Start Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="mortgageStartDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="mortgageStartDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
		 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="End Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="mortgageEndDate" tabindex="5"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="mortgageEndDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Payment Account"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="mortgageUpdateForm" property="paymentAccountID" tabindex="8">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="mortgageAccounts" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	           <pas:text property="paymentAccountDescription" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Principal Account"/>
	    </td>
	    <c:choose>
	      <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <html:select name="mortgageUpdateForm" property="principalAccountID" tabindex="9">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="mortgageAccounts" property="id" labelProperty="description" />					    	   
			  </html:select>
	    	</td>
	      </c:when>
	      <c:otherwise>                                     
	        <td ALIGN="left">	       
	           <pas:text property="principalAccountDescription" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose>			    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Original Loan Amount"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="originalLoanAmount" textbox="true" readonly="false" width="150" tabindex="10"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="originalLoanAmount" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Interest Rate"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="interestRate" textbox="true" readonly="false" width="150" tabindex="11"/>
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
	        <pas:label value="Term In Years"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="termInYears" textbox="true" readonly="false" width="150" tabindex="12"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="termInYears" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="HomeOwners Insurance"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="homeownersInsurance" textbox="true" readonly="false" width="150" tabindex="13"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="homeownersInsurance" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="PMI"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="pmi" textbox="true" readonly="false" width="150" tabindex="14"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="pmi" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Property Taxes"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="propertyTaxes" textbox="true" readonly="false" width="150" tabindex="15"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="propertyTaxes" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Extra Principal"/>
	    </td>
	    <c:choose>
          <c:when test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="extraPrincipal" textbox="true" readonly="false" width="150" tabindex="16"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="extraPrincipal" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	   
	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${mortgageShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${mortgageShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${mortgageShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${mortgageShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${mortgageShowParm=='add' || mortgageShowParm =='update'}">
     <script language="javascript">
	     document.forms.mortgageUpdateForm.description.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
