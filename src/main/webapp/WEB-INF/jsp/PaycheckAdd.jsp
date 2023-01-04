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

<script language="JavaScript">

	function trColors(ckBox)
	{ 
	  ckBoxClicked = ckBox;	  
	  	   
	  //go up 2 levels to change the row color -- parent of checkbox is TD element, parent of TD is the TR
	  rowElement = ckBoxClicked.parentNode.parentNode
	  	  
	  if (rowElement.className == "rowGreen")
	  { 	    
	  	rowElement.className = "rowRed";
	  }
	  else
	  {	
	    rowElement.className = "rowGreen";
	  }
	    
	}

</script>

<html:form action="PaycheckAddAction1">
  
  <html:hidden property="jobID"/>
  
  <TABLE align="center" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
	 
	 <colgroup>
	    <col width="23%">
	    <col width="67%">
	 </colgroup>	  
	 
	 <TR>
	 
	   <TD>
	         
		 <TABLE align="left" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
			 
			 <colgroup>
			    <col width="20%">
			    <col width="80%">
			 </colgroup>		 
				 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Date"/>
			    </td>
			   	<td ALIGN="left">	       
					<pas:inputdate property="paycheckHistoryDate" tabindex="1"/>
			    </td>		      
			 </TR>
			 
			 <TR>
	    		<td ALIGN="right">
	        		<pas:label value="Paycheck Type"/>
	    		</td>
	    		<td ALIGN="left">	 
	        		<html:select name="paycheckAddForm" property="paycheckTypeID" tabindex="4">
			    		<html:option value="">-Select-</html:option>
			    		<html:options collection="PaycheckTypes" property="id" labelProperty="description" />					    	   
			 		</html:select>
          		</td>  
	 		</TR>   
	 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Gross"/>
			    </td>
			    <td ALIGN="left">	       
		       	    <pas:text property="grossPay" textbox="true" readonly="false" width="70" tabindex="5"/>
		    	</td>	           
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="FedWH"/>
			    </td>
			    <td ALIGN="left">	       
		       	    <pas:text property="federalWithholding" textbox="true" readonly="false" width="70" tabindex="6"/>
		    	</td>	         
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="StWH"/>
			    </td>
			    <td ALIGN="left">	       
		            <pas:text property="stateWithholding" textbox="true" readonly="false" width="70" tabindex="7"/>
		    	</td>	        
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Defer"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="retirementDeferred" textbox="true" readonly="false" width="70" tabindex="8"/>
		    	</td>	        
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="SS WH"/>
			    </td>
	    	    <td ALIGN="left">	       
		       	    <pas:text property="ssWithholding" textbox="true" readonly="false" width="70" tabindex="9"/>
		    	</td>	          
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Mdcare"/>
			    </td>
			    <td ALIGN="left">	       
		       	    <pas:text property="medicareWithholding" textbox="true" readonly="false" width="70" tabindex="10"/>
		    	</td>	           
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="MedIns"/>
			    </td>
			    <td ALIGN="left">	       
		       	    <pas:text property="medical" textbox="true" readonly="false" width="70" tabindex="11"/>
		    	</td>	           
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="DntIns"/>
			    </td>
		   	    <td ALIGN="left">	       
		   	       <pas:text property="dental" textbox="true" readonly="false" width="70" tabindex="12"/>
		   	    </td>
			 </TR>   
			 	 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="GrpLifeIns"/>
			    </td>
	    	    <td ALIGN="left">	       
		       	    <pas:text property="groupLifeInsurance" textbox="true" readonly="false" width="70" tabindex="13"/>
		    	</td>	           
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="GrpLifeInc"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="groupLifeIncome" textbox="true" readonly="false" width="70" tabindex="14"/>
		    	</td>	         
			 </TR> 
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Vision"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="vision" textbox="true" readonly="false" width="70" tabindex="15"/>
		    	</td>	         
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Parking"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="parking" textbox="true" readonly="false" width="70" tabindex="16"/>
		    	</td>	         
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Cafeteria"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="cafeteria" textbox="true" readonly="false" width="70" tabindex="17"/>
		    	</td>	         
			 </TR>   
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Roth 401k"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="roth401k" textbox="true" readonly="false" width="70" tabindex="18"/>
		    	</td>	         
			 </TR>  
			 
			 <TR>
			    <td ALIGN="right">
			        <pas:label value="Flex Spend"/>
			    </td>
		   	    <td ALIGN="left">	       
		       	    <pas:text property="fsaAmount" textbox="true" readonly="false" width="70" tabindex="18"/>
		    	</td>	         
			 </TR>  
			 		   
		  </TABLE>
	  
	  </TD>
	  
	  <TD>	   
	  
		  <TABLE align="center" id="pcoTable" CLASS="myBox" WIDTH="100%" CELLSPACING=0 CELLPADDING=2 BORDER=1>
			 
			 <colgroup>
			    <col width="5%">
			    <col width="15%">
			    <col width="10%">
			    <col width="15%">
			    <col width="10%">
			    <col width="5%">
			    <col width="25%">
			    <col width="15%">
			 </colgroup>	
			 
			 <TR>
			 	<td align="center">Y/N</td>
			 	<td align="center">Account</td>
			 	<td align="center">TrxType</td>
			 	<td align="center">Date</td>
			 	<td align="center">Amount</td>
			 	<td align="center">ChkNo</td>
			 	<td align="center">Description</td>		 	
			 	<td align="center">Xfer To</td>
			 </TR>
			 
			 <c:forEach var="pcoAddItem" items="${sessionScope.paycheckAddForm.pcoAddList}">
			   <c:choose>
			     <c:when test="${pcoAddItem.processInd==true}">
			       <c:set var="rowclassjstl" value="rowGreen"/>
			     </c:when>
			     <c:otherwise>
			       <c:set var="rowclassjstl" value="rowRed"/>
			     </c:otherwise>
			   </c:choose>
			   <TR class="<c:out value="${rowclassjstl}"/>">
			      <td align="center">
			      	<html:checkbox name="pcoAddItem" indexed="true" property="processInd" onclick="javascript:trColors(this)"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.accountName}"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.transactionTypeDesc}"/>		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="pcoAddDate" size="8" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="amount" size="5" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="checkNo" size="3" />		      	 
			      </td>	
			      <td align="center">
			      	<html:text name="pcoAddItem" indexed="true" property="description" size="20"/>		      	 
			      </td>	
			      <td align="center">
			      	<c:out value="${pcoAddItem.xferAccountName}"/>		      	 
			      </td>			      		     
			   </TR>    			
			</c:forEach>	
		  
		  </TABLE>
		  
		</TD>
		
	  </TR>	  
	  	 
	  <TR>
		  <TD align="center">
			<html:submit property="operation" value="Add" />
		  </TD>
		  <TD align="center">
		  	<html:submit property="operation" value="Cancel Add"/>
		  </TD>	 
  	  </TR>
 
  </TABLE>
  
  <script language="javascript">
     document.forms.paycheckAddForm.ssWithholding.select(); 
  </script>
   
</html:form>

</BODY> 
