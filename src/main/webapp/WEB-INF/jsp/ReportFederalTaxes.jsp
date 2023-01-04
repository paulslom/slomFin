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

<html:form action="ReportFederalTaxesAction">

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
       <td class="body8pt" align="left">
  		 <pas:text property="totalGrossWages" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="prevStateRefund" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="totalItemizedDeductions" textbox="false" readonly="true"/>
       </td>      
    </tr>
   
    <tr>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="totalTaxableWages" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="totalCapitalGains" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="taxableIncome" textbox="false" readonly="true"/>
       </td>  
       </tr>
    
    <tr>
       <td class="body8pt" align="left">
  		 <pas:text property="totalInterest" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="agi" textbox="false" readonly="true"/>
       </td>
       <td class="body8pt" align="left">
  		 <pas:text property="taxCredits" textbox="false" readonly="true"/>
       </td>
    </tr>
    
    <tr>
       <td class="body8pt" align="left">
  		 <pas:text property="totalDividends" textbox="false" readonly="true"/>
       </td>
        <td class="body8pt" align="left">
  		 <pas:text property="totalExemptions" textbox="false" readonly="true"/>
       </td>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="totalFederalTaxesOwed" textbox="false" readonly="true"/>
       </td>  
    </tr>
    
    <tr>
       <td class="bodyBold8pt" align="left">
  		 <pas:text property="totalFederalWithholding" textbox="false" readonly="true"/>
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
  
  <c:if test="${ReportFTXW2List!=null}">
        
      <display:table name="sessionScope.ReportFTXW2List" export="true" uid="w2">      
        
       <display:caption>W2 Forms</display:caption> 
       
       <display:column property="employer" title="Job"/>     
       <display:column property="box1TaxableWgs" title="Box1" format="{0,number,currency}"/>
	   <display:column property="box2FedWH" title="Box2" format="{0,number,currency}"/>
	   <display:column property="box3SSWages" title="Box3" format="{0,number,currency}"/>
	   <display:column property="box4SocSecWH" title="Box4" format="{0,number,currency}"/>
	   <display:column property="box5TotalWgs" title="Box5" format="{0,number,currency}"/>
	   <display:column property="box6MedicareWH" title="Box6" format="{0,number,currency}"/>
	   <display:column property="box12cLifeIns" title="Box12c" format="{0,number,currency}"/>
	   <display:column property="box12dRetDeferred" title="Box12d" format="{0,number,currency}"/>	 
	   <display:column property="box16StateWages" title="Box16" format="{0,number,currency}"/>	   	 
	   <display:column property="box17StateWH" title="Box17" format="{0,number,currency}"/>	   	   	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if>
  
  <c:if test="${ReportFTXWagesList!=null}">
        
      <display:table name="sessionScope.ReportFTXWagesList" export="true" uid="Wages"
        class="totalTable" defaultsort="2" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" group="1" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description"/>
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	   <display:column property="federalWithholding" title="Fed WH" format="{0,number,currency}" total="true"/>
	   <display:column property="stateWithholding" title="St WH" format="{0,number,currency}" total="true"/>
	   <display:column property="retirementDeferred" title="Ret PreTax" format="{0,number,currency}" total="true"/>   	   	      
       
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if>
  
  <c:if test="${ReportFTXDividendsList!=null}">
      
      <display:table name="sessionScope.ReportFTXDividendsList" export="true" uid="Dividends"
        class="totalTable" defaultsort="4" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	  	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportFTXInterestList!=null}">
        
      <display:table name="sessionScope.ReportFTXInterestList" export="true" uid="Interest"
        class="totalTable" defaultsort="4" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportFTXCapitalGainsList!=null}">
      
      <display:table name="sessionScope.ReportFTXCapitalGainsList" export="true" uid="CapitalGains"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
            
  </c:if> 
  
  <c:if test="${ReportFTXMiscIncomeList!=null}">
      
      <display:table name="sessionScope.ReportFTXMiscIncomeList" export="true" uid="MiscIncome"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
             
  </c:if> 
  
  <c:if test="${ReportFTXDeductionsList!=null}">
      
      <display:table name="sessionScope.ReportFTXDeductionsList" export="true" uid="Deductions"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description"/>
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
              
  </c:if> 
  
  <c:if test="${ReportFTXExemptionsList!=null}">
       
      <display:table name="sessionScope.ReportFTXExemptionsList" export="true" uid="Exemptions"
        class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator">      
            
       <display:column property="taxDate" title="Date"  format="{0,date,yyyy-MM-dd}"/>
       <display:column property="investorName" title="Name" />
       <display:column property="type" title="Type"/>
       <display:column property="description" title="Description" group="1" />
	   <display:column property="grossAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />

      </display:table>
     
  </c:if> 
  
  <c:if test="${ReportFTXCreditsList!=null}">
       
      <display:table name="sessionScope.ReportFTXCreditsList" export="true" uid="Credits"
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
