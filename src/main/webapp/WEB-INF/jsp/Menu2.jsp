<!DOCTYPE html>
<%@ include file="Common_Tags.jsp" %> 
<html>

<head>
        
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />        
    <META http-equiv="Content-Style-Type" content="text/css">
    <link rel=stylesheet href="styles/general.css" type="text/css"> 
 
</head>

<body>

  <nav class="menu" role='navigation'>
	
	<ol>
	  
	  	<li class="menu-item" aria-haspopup="true">
	     
	        <c:url value="/AccountSelectionShowFormAction.do" var="viewURL">
               <c:param name="operation" value="inquire"/>    
               <c:param name="taxableInd" value="Y"/>    
            </c:url>
            
            <a href="<c:out value='${viewURL}'/>">
               Open Taxable Accounts
            </a>      
	        
	    </li> 
	     
	    <li class="menu-item" aria-haspopup="true">
	     
	         <c:url value="/AccountSelectionShowFormAction.do" var="viewURL">
	            <c:param name="operation" value="inquire"/>    
	            <c:param name="taxableInd" value="N"/>    
	          </c:url>
	          
	          <a href="<c:out value='${viewURL}'/>">
	             Open Retirement Accounts
	          </a>     
	        
	     </li> 
	     
	     <li class="menu-item" aria-haspopup="true">
	     
	        <a href="#0">Reports</a>
	        <ol class="sub-menu" aria-label="submenu">
	     
	        <c:forEach var="mItem" items="${sessionScope.ReportsMenuList}">	 
	        	<li class="menu-item"><a href = '<c:out value="${mItem.menuLocation}"/>'>	<c:out value="${mItem.menuTitle}"/></a></li>	     
	        </c:forEach>
	       	            
	        </ol>
	        
	     </li> 
	     
	      <li class="menu-item" aria-haspopup="true">
	     
	        <a href="#0">Work</a>
	        <ol class="sub-menu" aria-label="submenu">
	     
	        <c:forEach var="mItem" items="${sessionScope.WorkMenuList}">
	         	<li class="menu-item"><a href = '<c:out value="${mItem.menuLocation}"/>'>	<c:out value="${mItem.menuTitle}"/></a></li>	     
	        </c:forEach>
	            
	        </ol>
	        
	     </li> 
	     
	      <li class="menu-item" aria-haspopup="true">
	     
	        <a href="#0">Misc</a>
	        <ol class="sub-menu" aria-label="submenu">
	     
	        <c:forEach var="mItem" items="${sessionScope.MiscMenuList}">
	         	<li class="menu-item"><a href = '<c:out value="${mItem.menuLocation}"/>'>	<c:out value="${mItem.menuTitle}"/></a></li>	     
	        </c:forEach>
	            
	        </ol>
	        
	     </li> 
	     
	     <li class="menu-item" aria-haspopup="true">
	     
	        <c:url value="/AccountSelectionShowFormAction.do" var="viewURL">
               <c:param name="operation" value="inquire"/>    
               <c:param name="closedInd" value="Y"/>    
            </c:url>
            
            <a href="<c:out value='${viewURL}'/>">
               Closed Accounts
            </a>      
	        
	    </li>      
	    
	</ol>
	  
  </nav>
    
</BODY>
</html>

	