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

<html:form action="InvestmentUpdateAction">

  <html:hidden property="investmentID"/>
        
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="35%">
	    <col width="65%">
	 </colgroup>	
	 	  
	 <c:if test="${investmentShowParm!='add'}">
		 <TR>
		    <td ALIGN="right">
		        <pas:label value="Investment ID"/>
		    </td>    
		    <td ALIGN="left">	       
		       <pas:text property="investmentID" textbox="false" readonly="true"/>
		    </td>
		 </TR>
	 </c:if>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Description"/>
	    </td>
	    <c:choose>
          <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="description" textbox="true" readonly="false" width="200" tabindex="1"/>
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
	        <pas:label value="Investment Type"/>
	    </td>
	    <c:choose>
          <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	      <html:select name="investmentUpdateForm" property="investmentTypeID" tabindex="2">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="InvestmentTypes" property="id" labelProperty="description" />					    	   
			  </html:select>       	       
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="investmentTypeDesc" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Asset Class"/>
	    </td>
	    <c:choose>
          <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	      <html:select name="investmentUpdateForm" property="assetClassID" tabindex="3">
			    <html:option value="">-Select-</html:option>
			    <html:options collection="AssetClasses" property="id" labelProperty="description" />					    	   
			  </html:select>       	       
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="assetClassDesc" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR> 
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Ticker Symbol"/>
	    </td>
	    <c:choose>
	      <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="tickerSymbol" textbox="true" width="50" readonly="false" tabindex="4"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:text property="tickerSymbol" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Dividends Per Year"/>
	    </td>
	    <c:choose>
	      <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
	    	<td ALIGN="left">	       
	       	  <pas:text property="dividendsPerYear" textbox="true" width="50" readonly="false" tabindex="5"/>
	    	</td>
	      </c:when>
	      <c:otherwise>
	        <td ALIGN="left">	       
	          <pas:text property="dividendsPerYear" textbox="false" readonly="true"/>
	        </td>
	      </c:otherwise>   
	    </c:choose> 	    
	 </TR>
	 
	 	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Option Multiplier"/>
	    </td>
	    <c:choose>
          <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="optionMultiplier" textbox="true" readonly="false" width="50" tabindex="6"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:text property="optionMultiplier" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 
	 <TR>
	    <td ALIGN="right">
	        <pas:label value="Current Price"/>
	    </td>
	    <c:choose>
          <c:when test="${investmentShowParm=='add' || investmentShowParm =='update'}">
    	    <td ALIGN="left">	       
       	       <pas:text property="currentPrice" textbox="true" readonly="false" width="50" tabindex="7"/>
    	    </td>
          </c:when>
          <c:otherwise>
            <td ALIGN="left">	       
	          <pas:amount property="currentPrice" textbox="false" readonly="true"/>
	        </td>
          </c:otherwise>   
        </c:choose> 	    
	 </TR>   
	 	 	 	   
  </TABLE> 
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=4 BORDER=0>
	<TR>
	 <c:choose>
	    <c:when test="${investmentShowParm=='add'}">
		  <TD align="center">
		     <html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		     <html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
		</c:when>
		<c:when test="${investmentShowParm=='update'}">    
		  <TD align="center">
		    <html:submit property="operation" value="Update" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Update" />
		  </TD>
		</c:when>
		<c:when test="${investmentShowParm=='delete'}">
		  <TD align="center">
		    <html:submit property="operation" value="Delete" />
		  </TD>
		  <TD align="center">
		    <html:submit property="operation" value="Cancel Delete" />
		  </TD>
		</c:when>
		<c:when test="${investmentShowParm=='inquire'}">
		  <TD colspan="2" align="center">
		     <html:submit property="operation" value="Return"/>
		  </TD>
		</c:when>
	 </c:choose>	 	
	</TR>
	
  </TABLE>
  
  <c:if test="${investmentShowParm=='add' || investmentShowParm =='update'}">
     <script language="javascript">	    
	     document.forms.investmentUpdateForm.description.select();	         
	 </script>
  </c:if>
  
</html:form>

</BODY> 
