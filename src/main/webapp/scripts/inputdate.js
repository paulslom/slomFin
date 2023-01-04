var isNN = (navigator.appName.indexOf("Netscape")!=-1);

function autoTab(input,len, e)
{
	var keyCode = (isNN) ? e.which : e.keyCode; 
	var filter = (isNN) ? [0,8,9] : [0,8,9,16,17,18,37,38,39,40,46];	
	if(input.value.length >= len && !containsElement(filter,keyCode))
	{
		input.value = input.value.slice(0, len);
		var nextTab = getNextTabIndex(input);
		if (input.form[nextTab].type != 'button' 
		&&  input.form[nextTab].type != 'select-one'
		&&  input.form[nextTab].type != 'submit')
		{
			//alert("type of element for .select is = " + input.form[nextTab].type);
			//alert("about to do .select on tabindex = " + nextTab);
			input.form[nextTab].select();
			//alert("after .select on tabindex = " + nextTab);
		}
		
		input.form[nextTab].focus();
		
	}
	
	function containsElement(arr, ele)
	{
		var found = false, index = 0;
		while(!found && (index < arr.length)){
			if(arr[index]== ele){
				found=true; 
			}else{
				index++;
			}
		}
		return found;
	}
	
	function getIndex(input) {
		var index=-1, i=0, found=false; 
		while (i < input.form.length && index== -1){
			if (input.form[i]== input){
				index=i; 
			}else{
				i++;
			}
		}
		return index;
	}
	
	function getNextTabIndex(input)
	{
		var index=999, i=0, found=false; 
		var inputTabIndex=input.tabIndex
		var firstTabIndex=0;
		var nextTabIndex=999;
		
		//debugger;
		
		for ( i = 0; i < document.forms[0].elements.length; i++ )
		{
		    //alert("i = " + i);
		    //alert("element name = " + document.forms[0].elements[i].name);
		    
			if (document.forms[0].elements[i].tagName == "INPUT"
			||  document.forms[0].elements[i].tagName == "SELECT")
			{
			   if (document.forms[0].elements[i].type != "hidden"
			   &&  document.forms[0].elements[i].tabIndex > inputTabIndex
			   &&  document.forms[0].elements[i].tabIndex < nextTabIndex )
			   {					      
			      //alert("element tagName = " + document.forms[0].elements[i].tagName); 
			      //alert("element type = " + document.forms[0].elements[i].type)
			      //alert("element tab index = " + document.forms[0].elements[i].tabIndex);
			      nextTabIndex = document.forms[0].elements[i].tabIndex;
			      index=i; 
			      //alert("index set to = " + index);
			   }
			   else
			   {
			      if (document.forms[0].elements[i].tabIndex == 1)
				  {
					 firstTabIndex = i;
				  }			
			   }
			}   
			 
		}
		
		if (index==999) //means the input was the highest tabIndex value; loop back to first
		{	
			index = firstTabIndex;
		}
		return index;
	}
	
	return true;
}

 

