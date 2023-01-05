package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class TrxAddSelectionForm extends SlomFinBaseActionForm
{
	private static final long serialVersionUID = 1L;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
				
		return ae;
	}

}
