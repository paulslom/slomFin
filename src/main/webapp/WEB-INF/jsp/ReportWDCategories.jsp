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

<html:form action="ReportWDCategoriesAction">

  <table>
    <tr>
       <td class="bodyBold">
  		 <pas:text property="reportTitle" textbox="false" readonly="true"/>
       </td>
    </tr>
  </table>
  
  <c:if test="${ReportWDCategoriesList!=null}">
   
   <div  style="OVERFLOW: auto; WIDTH: 100%; HEIGHT:100%">
     
      <display:table name="sessionScope.ReportWDCategoriesList" export="true" uid="WDCategories" class="totalTable" decorator="org.displaytag.decorator.TotalTableDecorator" requestURI="">      
       <display:column property="tblwdcategory.swdcategoryDescription" title="Category" group="1" />
       <display:column property="tblwdcategory.iwdcategoryId" title="Cat Num" group="2" />
       <display:column property="dtransactionDate" title="Date" format="{0,date,yyyy-MM-dd}"/>       
       <display:column property="tblaccount.saccountName" title="Account"/>
       <display:column property="tbltransactiontype.sdescription" title="Trx Type"/> 
       <display:column property="sdescription" title="Trx Description"/> 
	   <display:column property="meffectiveAmount" title="Amount" format="{0,number,currency}" total="true"/>
	      
       <display:setProperty name="sort.behavior" value="list" />
 	    	   	      	
     </display:table>

    </div>
            
  </c:if> 
        
</html:form>

</BODY> 
