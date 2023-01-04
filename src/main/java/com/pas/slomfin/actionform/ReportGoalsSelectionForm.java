package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportGoalsSelectionForm extends SlomFinBaseActionForm
{
	private String rateOfReturn;
	private String projectionYears;
	
	public ReportGoalsSelectionForm()
	{		
	}
		
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{
		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
		
		ae = super.validate(mapping, request);
		
		return ae;

	}

	public String getProjectionYears()
	{
		return projectionYears;
	}

	public void setProjectionYears(String projectionYears)
	{
		this.projectionYears = projectionYears;
	}

	public String getRateOfReturn()
	{
		return rateOfReturn;
	}

	public void setRateOfReturn(String rateOfReturn)
	{
		this.rateOfReturn = rateOfReturn;
	}
	
}
