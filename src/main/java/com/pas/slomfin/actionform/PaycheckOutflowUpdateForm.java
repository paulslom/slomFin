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
public class PaycheckOutflowUpdateForm extends SlomFinBaseActionForm
{
	public PaycheckOutflowUpdateForm()
	{		
	}
	
	private Integer paycheckOutflowID;
	private String transactionTypeID;
	private String transactionTypeDesc;
	private String accountID;
	private String accountName;
	private String defaultAmount;
	private String defaultDay;
	private boolean nextMonthInd;
	private String description;
	private String xferAccountID;
	private String xferAccountName;
	private String wdCategoryID;
	private String wdCategoryDesc;
	private String cashDepositTypeID;
	private String cashDepositTypeDesc;
		
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public String getCashDepositTypeDesc()
	{
		return cashDepositTypeDesc;
	}
	public void setCashDepositTypeDesc(String cashDepositTypeDesc)
	{
		this.cashDepositTypeDesc = cashDepositTypeDesc;
	}
	public String getTransactionTypeDesc()
	{
		return transactionTypeDesc;
	}
	public void setTransactionTypeDesc(String transactionTypeDesc)
	{
		this.transactionTypeDesc = transactionTypeDesc;
	}
	public String getWdCategoryDesc()
	{
		return wdCategoryDesc;
	}
	public void setWdCategoryDesc(String wdCategoryDesc)
	{
		this.wdCategoryDesc = wdCategoryDesc;
	}
	public String getXferAccountName()
	{
		return xferAccountName;
	}
	public void setXferAccountName(String xferAccountName)
	{
		this.xferAccountName = xferAccountName;
	}
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
		
		String trxTypeText = request.getParameter("trxTypeText");
		
		if (cashDepositTypeID != null)
		   if (cashDepositTypeID.length()==0)
		      if (trxTypeText.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT))	  
		   {
			   ae.add(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_CASHDEPTYPE_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_CASHDEPTYPE_FIELD_MISSING));
		   }
		
		if (wdCategoryID != null)
		   if (wdCategoryID.length()==0)
		       if (trxTypeText.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
		       ||  trxTypeText.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL))	  
		       {
		           ae.add(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_WDCATEGORY_FIELD_MISSING,
		              new ActionMessage(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_WDCATEGORY_FIELD_MISSING));
		       }
		
		if (xferAccountID != null)
		   if (xferAccountID.length()==0)
		      if (trxTypeText.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
		      ||  trxTypeText.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))	 
			  {
				   ae.add(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_XFERACCT_FIELD_MISSING,
					  new ActionMessage(ISlomFinMessageConstants.PAYCHECKOUTFLOWFORM_XFERACCT_FIELD_MISSING));
			  }
		      
		return ae;
		
	}
		
	
    public String getAccountID()
    {
        return accountID;
    }
    public void setAccountID(String accountID)
    {
        this.accountID = accountID;
    }
    public String getCashDepositTypeID()
    {
        return cashDepositTypeID;
    }
    public void setCashDepositTypeID(String cashDepositTypeID)
    {
        this.cashDepositTypeID = cashDepositTypeID;
    }
    public String getDefaultAmount()
    {
        return defaultAmount;
    }
    public void setDefaultAmount(String defaultAmount)
    {
        this.defaultAmount = defaultAmount;
    }
    public String getDefaultDay()
    {
        return defaultDay;
    }
    public void setDefaultDay(String defaultDay)
    {
        this.defaultDay = defaultDay;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public boolean isNextMonthInd()
    {
        return nextMonthInd;
    }
    public void setNextMonthInd(boolean nextMonthInd)
    {
        this.nextMonthInd = nextMonthInd;
    }
    public Integer getPaycheckOutflowID()
    {
        return paycheckOutflowID;
    }
    public void setPaycheckOutflowID(Integer paycheckOutflowID)
    {
        this.paycheckOutflowID = paycheckOutflowID;
    }
    public String getTransactionTypeID()
    {
        return transactionTypeID;
    }
    public void setTransactionTypeID(String transactionTypeID)
    {
        this.transactionTypeID = transactionTypeID;
    }
    public String getWdCategoryID()
    {
        return wdCategoryID;
    }
    public void setWdCategoryID(String wdCategoryID)
    {
        this.wdCategoryID = wdCategoryID;
    }
    public String getXferAccountID()
    {
        return xferAccountID;
    }
    public void setXferAccountID(String xferAccountID)
    {
        this.xferAccountID = xferAccountID;
    }
}
