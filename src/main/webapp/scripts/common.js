function destroySession()
{
	if ((window.event.clientX<0) && (window.event.clientY<0))
	{
	    //alert("reached the inside of destroySession");
		newWindow = window.open("InvalidateSessionAction.do",null,"height=10, width=10");
		newWindow.close();
	}
}


function IsNumeric(strString)
//  check for valid numeric strings	
{
  var strValidChars = "0123456789.-";
  var strChar;
  var blnResult = true;

  if (strString.length == 0) return false;

  //  test strString consists of valid characters listed above
  for (i = 0; i < strString.length && blnResult == true; i++)
  {
     strChar = strString.charAt(i);
     if (strValidChars.indexOf(strChar) == -1)
     {
        blnResult = false;
     }
  }
  return blnResult;
}

function round_decimals(original_number, decimals)
{
    var result1 = original_number * Math.pow(10, decimals)
    var result2 = Math.round(result1)
    var result3 = result2 / Math.pow(10, decimals)
    return pad_with_zeros(result3, decimals)
}

function pad_with_zeros(rounded_value, decimal_places)
{
    // Convert the number to a string
    var value_string = rounded_value.toString()
    
    // Locate the decimal point
    var decimal_location = value_string.indexOf(".")

    // Is there a decimal point?
    if (decimal_location == -1)
    {
        
        // If no, then all decimal places will be padded with 0s
        decimal_part_length = 0
        
        // If decimal_places is greater than zero, tack on a decimal point
        value_string += decimal_places > 0 ? "." : ""
    }
    else
    {
        // If yes, then only the extra decimal places will be padded with 0s
        decimal_part_length = value_string.length - decimal_location - 1
    }
    
    // Calculate the number of decimal places that need to be padded with 0s
    var pad_total = decimal_places - decimal_part_length
    
    if (pad_total > 0)
    {        
        // Pad the string with 0s
        for (var counter = 1; counter <= pad_total; counter++) 
            value_string += "0"
    }
    return value_string
}
