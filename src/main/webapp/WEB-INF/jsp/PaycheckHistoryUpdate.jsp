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

<html:form action="PaycheckHistoryUpdateAction">

  <html:hidden property="paycheckHistoryID"/>
            
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="PaycheckHistory ID"/>
	    </td>    
	    <td ALIGN="left">	       
	       <pas:text property="paycheckHistoryID" textbox="false" readonly="true"/>
	    </td>
	 </TR>
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Employer"/>
	    </td>
	    <td ALIGN="left">	       
	        <pas:text property="employer" textbox="false" readonly="true"/>
	    </td>
  	 </TR> 
		 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Paycheck Date"/>
	    </td>
	    <c:choose>
	      <c:when test="${paycheckHistoryShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:inputdate property="paycheckHistoryDate" tabindex="2"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:date property="paycheckHistoryDate" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Paycheck Type"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
            <td ALIGN="left">	    
    	       <html:select name="paycheckHistoryUpdateForm" property="paycheckTypeID" disabled="false" tabindex="5">
			     <html:option value="">-Select-</html:option>
			     <html:options collection="PaycheckTypes" property="id" labelProperty="description" />					    	   
			   </html:select>
			</td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	    
               <html:select name="paycheckHistoryUpdateForm" property="paycheckTypeID" disabled="true">
			      <html:option value="">-Select-</html:option>
			      <html:options collection="PaycheckTypes" property="id" labelProperty="description" />					    	   
			   </html:select>
			</td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Gross Pay"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="grossPay" textbox="true" readonly="false" width="150" tabindex="6"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="grossPay" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Federal Withholding"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="federalWithholding" textbox="true" readonly="false" width="150" tabindex="7"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="federalWithholding" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="State Withholding"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="stateWithholding" textbox="true" readonly="false" width="150" tabindex="8"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="stateWithholding" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Retirement Deferred (401k, etc)"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="retirementDeferred" textbox="true" readonly="false" width="150" tabindex="9"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="retirementDeferred" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Social Security Withholding (FICA)"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="ssWithholding" textbox="true" readonly="false" width="150" tabindex="10"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="ssWithholding" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Medicare Withholding"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="medicareWithholding" textbox="true" readonly="false" width="150" tabindex="11"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="medicareWithholding" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Medical Insurance Premium"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="medical" textbox="true" readonly="false" width="150" tabindex="12"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="medical" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Dental Insurance Premium"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="dental" textbox="true" readonly="false" width="150" tabindex="13"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="dental" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Group Life Insurance"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="groupLifeInsurance" textbox="true" readonly="false" width="150" tabindex="14"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="groupLifeInsurance" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Group Life Income"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="groupLifeIncome" textbox="true" readonly="false" width="150" tabindex="15"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="groupLifeIncome" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Vision"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="vision" textbox="true" readonly="false" width="150" tabindex="16"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="vision" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Parking"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="parking" textbox="true" readonly="false" width="150" tabindex="17"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="parking" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Cafeteria"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="cafeteria" textbox="true" readonly="false" width="150" tabindex="18"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="cafeteria" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Roth 401k"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="roth401k" textbox="true" readonly="false" width="150" tabindex="19"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="roth401k" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>  
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Flexible Spending"/>
	    </td>
	    <c:choose>
          <c:when test="${paycheckHistoryShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="fsaAmount" textbox="true" readonly="false" width="150" tabindex="19"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="fsaAmount" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 	 	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>	    
		<c:when test="${paycheckHistoryShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${paycheckHistoryShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${paycheckHistoryShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${paycheckHistoryShowParm =='update'}">
     <script language="javascript">
	     document.forms.paycheckHistoryUpdateForm.paycheckHistoryDate.appmonth.select();		      
	 </script>
  </c:if>
  
</html:form>

</BODY> 
