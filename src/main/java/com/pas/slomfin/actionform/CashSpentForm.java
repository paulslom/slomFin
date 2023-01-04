package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.util.DateUtil;
import com.pas.valueObject.AppDate;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CashSpentForm extends SlomFinBaseActionForm
{
	public CashSpentForm()
	{		
	}
	
	private AppDate transactionDate = new AppDate();
	private AppDate transactionPostedDate = new AppDate();
	private String costProceeds;
	private String wdCategoryID;
	private String cashDescription;
	private String accountID;	
	private String accountName;
	private String transactionTypeID;
	private String transactionTypeDesc;
	
	public void initialize()
	{
		//initialize all variables
		
		String methodName = "initialize :: ";
		log.debug(methodName + " In");

		transactionDate.initAppDateToToday();
		costProceeds = "";
		wdCategoryID = "";
		cashDescription = "";

	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");
		
		ActionErrors ae = super.validate(mapping, request);
		
		if (ae==null)
		    ae = new ActionErrors();

		String tempDate = transactionDate.getAppmonth() + "-"
						+ transactionDate.getAppday() + "-"
						+ transactionDate.getAppyear();
		
		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.INVALID_TRANSACTION_DATE,
				new ActionMessage(ISlomFinMessageConstants.INVALID_TRANSACTION_DATE));
		}
		
		return ae;

	}

	public AppDate getTransactionDate()
	{
		return transactionDate;
	}
	public void setTransactionDate(AppDate transactionDate)
	{
		this.transactionDate = transactionDate;
	}
	public String getWdCategoryID()
	{
		return wdCategoryID;
	}
	public void setWdCategoryID(String wdCategoryID)
	{
		this.wdCategoryID = wdCategoryID;
	}
	public String getCostProceeds()
	{
		return costProceeds;
	}
	public void setCostProceeds(String costProceeds)
	{
		this.costProceeds = costProceeds;
	}
	public String getCashDescription()
	{
		return cashDescription;
	}
	public void setCashDescription(String cashDescription)
	{
		this.cashDescription = cashDescription;
	}
	public String getAccountID()
	{
		return accountID;
	}
	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public String getTransactionTypeDesc()
	{
		return transactionTypeDesc;
	}
	public void setTransactionTypeDesc(String transactionTypeDesc)
	{
		this.transactionTypeDesc = transactionTypeDesc;
	}
	public String getTransactionTypeID()
	{
		return transactionTypeID;
	}
	public void setTransactionTypeID(String transactionTypeID)
	{
		this.transactionTypeID = transactionTypeID;
	}
	public AppDate getTransactionPostedDate()
	{
		return transactionPostedDate;
	}
	public void setTransactionPostedDate(AppDate transactionPostedDate)
	{
		this.transactionPostedDate = transactionPostedDate;
	}
	
	
}
