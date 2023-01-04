package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InvestmentChoiceForm extends SlomFinBaseActionForm
{
	private Integer investmentID;
	private Integer accountID;
	

	public InvestmentChoiceForm()
	{		
	}
		
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
		
		if (investmentID != null)
		   if (investmentID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_INVESTMENT_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_INVESTMENT_FIELD_MISSING));
		   }
		
		return ae;

	}
	
	public Integer getInvestmentID()
	{
		return investmentID;
	}

	public void setInvestmentID(Integer investmentID) 
	{
		this.investmentID = investmentID;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}
	
}
