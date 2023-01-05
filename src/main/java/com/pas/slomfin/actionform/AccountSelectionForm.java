package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.slomfin.constants.ISlomFinMessageConstants;

public class AccountSelectionForm extends SlomFinBaseActionForm
{
	private static final long serialVersionUID = 1L;
	
	private String accountID;
	private int portfolioID;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
				
		return ae;
	}

	public int getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(int portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	
	
}
