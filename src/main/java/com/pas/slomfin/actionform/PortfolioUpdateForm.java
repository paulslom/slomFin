package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.pas.slomfin.constants.ISlomFinAppConstants;


/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PortfolioUpdateForm extends SlomFinBaseActionForm
{
	public PortfolioUpdateForm()
	{		
	}
	
	private Integer portfolioID;
	private String  portfolioName;
	private boolean taxable;
		
	public void initialize()
	{
		//initialize all variables
		
		String methodName = "initialize :: ";
		log.debug(methodName + " In");
		log.debug(methodName + " Out");
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");
		
		ActionErrors ae = new ActionErrors();

		String reqParm = request.getParameter("operation");
		
		//do not perform validation when cancelling or returning from an inquire or delete
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_DELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			return ae;
		
		ae = super.validate(mapping, request);		
		
		return ae;
		
	}
	public Integer getPortfolioID()
	{
		return portfolioID;
	}
	public void setPortfolioID(Integer portfolioID)
	{
		this.portfolioID = portfolioID;
	}
	public String getPortfolioName()
	{
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
	}
	public boolean isTaxable()
	{
		return taxable;
	}
	public void setTaxable(boolean taxable)
	{
		this.taxable = taxable;
	}
	
	
}
