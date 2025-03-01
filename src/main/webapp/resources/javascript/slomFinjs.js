function calcCostProceeds()
{
   var trxAmountElement = document.getElementById('transactionAddUpdateForm:amtID');
   
   var tempUnits = document.getElementById('transactionAddUpdateForm:unitsID').value;
   var tempPrice = document.getElementById('transactionAddUpdateForm:priceID').value;
   
   //alert('tempPrice = ' + tempPrice);
   //alert('tempUnits = ' + tempUnits);
  
   if (!(isNaN(tempUnits)) && !(isNaN(tempPrice)))
   {
	   var tempTrxAmount = tempUnits * tempPrice; 
	   //alert('tempTrxAmount = ' + tempTrxAmount);
	   trxAmountElement.value = round_decimals(tempTrxAmount, 2);      		
   }   
   
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
