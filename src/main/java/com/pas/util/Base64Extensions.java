package com.pas.util;

import java.util.Base64;

public class Base64Extensions
{

	public String decryptBase64(String strEncrypted)
    {  
		byte[] decodedBytes = Base64.getDecoder().decode(strEncrypted);
		String decodedString = new String(decodedBytes);
		return decodedString;
    }

    public String encryptBase64(String str)
    {       
        String encodedString = Base64.getEncoder().encodeToString(str.getBytes());
        return encodedString;
    }

    
}
