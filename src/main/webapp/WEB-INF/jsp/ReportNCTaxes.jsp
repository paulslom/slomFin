<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ include file="Common_Tags.jsp" %> 
 
<HEAD>

   <%@ page language="java" contentType="text/html" %>
   <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <META http-equiv="Content-Style-Type" content="text/css">
   <link rel="stylesheet" type="text/css" href="styles/displaytag.css">
   <script type="text/javascript" src="scripts/common.js"></script>
      
</HEAD>

<BODY>

<html:form action="ReportNCTaxesAction">

  <table>
    
    <colgroup>
    	<col width="33%">
	    <col width="33%">
	    <col width="34%">
    </colgroup>
    
    <tr>
       <td colspan="2" class="bodyBold" align="center">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
    
    <tr>
    	<td>
    		<br/>
    	</td>
    </tr>	
    
    <tr>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="taxableIncome" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="deductions" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="ncTaxesOwed" textbox="false" readonly="true"/>
       </td>      
    </tr>
   
    <tr>
       <td class="body8pt" align="left">
  		 <pas:text property="allStatesWithholding" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="additionsPlusFederalMinusStateRefund" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="ncTaxCredits" textbox="false" readonly="true"/>
       </td>  
       </tr>
    
    <tr>
       <td class="body8pt" align="left">
  		 <pas:text property="personalExemptionAdjustment" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="residencyRatio" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="ncTaxesOwedMinusCredits" textbox="false" readonly="true"/>
       </td>
    </tr>
    
    <tr>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="additions" textbox="false" readonly="true"/>
       </td>
        <td class="bodyBold8pt" align="left">
  		 <pas:text property="ncTaxableIncome" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="totalStateWithholding" textbox="false" readonly="true"/>
       </td>  
    </tr>
    
    <tr>
       <td class="body8pt" align="left">
  		 <pas:text property="additionsPlusFederal" textbox="false" readonly="true"/>
       </td>    
       <td class="body8pt" align="left">
  		 <pas:text property="ncSurtaxOwed" textbox="false" readonly="true"/>
       </td>      
    </tr>
     
    <tr>
    	<td>
    		<br/>
    	</td>
    </tr>	
    
    <tr>
       <td colspan="2" class="bodyBold" align="center">
  		 <pas:text property="bottomLineString" textbox="false" readonly="true"/>
       </td>
    </tr>
    
  </table>
    
  <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 100%">
  
  <c:if test="${ReportNCWagesList!=null}">
        
      <display:table name="sessionScope.ReportNCWagesList" export="true" uid="Wages"
        class="totalTable" defaultsort="2" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" group="1" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="2" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	   <display:column property="federalWithholding" title="Fed WH" format="{0,number,currency}" total="true"/>
	   <display:column property="stateWithholding" title="St WH" format="{0,number,currency}" total="true"/>
	   <display:column property="retirementDeferred" title="Ret PreTax" format="{0,number,currency}" total="true"/>
	   	   	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if>
  
  <c:if test="${ReportNCDividendsList!=null}">
      
      <display:table name="sessionScope.ReportNCDividendsList" export="true" uid="Dividends"
        class="totalTable" defaultsort="4" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	  	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportNCInterestList!=null}">
        
      <display:table name="sessionScope.ReportNCInterestList" export="true" uid="Interest"
        class="totalTable" defaultsort="4" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportNCCapitalGainsList!=null}">
      
      <display:table name="sessionScope.ReportNCCapitalGainsList" export="true" uid="CapitalGains"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportNCCreditsList!=null}">
      
      <display:table name="sessionScope.ReportNCCreditsList" export="true" uid="Credits"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
        
  </div>
 
</html:form>

</BODY> 
