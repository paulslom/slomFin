package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportDividendsForm extends SlomFinBaseActionForm
{
	private String reportTitle;
	
	public String getReportTitle()
	{
		return reportTitle;
	}

	public void setReportTitle(String reportTitle)
	{
		this.reportTitle = reportTitle;
	}
			
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
				
		return ae;

	}
	
}
