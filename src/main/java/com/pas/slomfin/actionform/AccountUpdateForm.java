package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.pas.slomfin.constants.ISlomFinAppConstants;


/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccountUpdateForm extends SlomFinBaseActionForm
{
	public AccountUpdateForm()
	{		
	}
	
	private Integer accountID;
	private String portfolioID;
	private String portfolioName;
	private String brokerID;
	private String brokerName;
	private String accountTypeID;
	private String accountTypeDescription;
	private String accountName;
	private String accountNameAbbr;
	private String startingCheckNo;
	private String accountNumber;
	private String pin;
	private boolean closed;
	private String interestPaymentsPerYear;
	private String interestRate;
	private String minInterestBalance;
	private boolean taxable;
	private String newMoneyPerYear;
	private String estimatedRateofReturn;
	private String vestingPercentage; 
	private String accountOwnerName; 	
		
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
	public Integer getAccountID()
	{
		return accountID;
	}
	public void setAccountID(Integer accountID)
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
	public String getAccountNameAbbr()
	{
		return accountNameAbbr;
	}
	public void setAccountNameAbbr(String accountNameAbbr)
	{
		this.accountNameAbbr = accountNameAbbr;
	}
	public String getAccountNumber()
	{
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}
	public String getAccountOwnerName()
	{
		return accountOwnerName;
	}
	public void setAccountOwnerName(String accountOwnerName)
	{
		this.accountOwnerName = accountOwnerName;
	}
	public String getAccountTypeDescription()
	{
		return accountTypeDescription;
	}
	public void setAccountTypeDescription(String accountTypeDescription)
	{
		this.accountTypeDescription = accountTypeDescription;
	}
	public String getBrokerName()
	{
		return brokerName;
	}
	public void setBrokerName(String brokerName)
	{
		this.brokerName = brokerName;
	}
	public boolean isClosed()
	{
		return closed;
	}
	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}
	public String getEstimatedRateofReturn()
	{
		return estimatedRateofReturn;
	}
	public void setEstimatedRateofReturn(String estimatedRateofReturn)
	{
		this.estimatedRateofReturn = estimatedRateofReturn;
	}
	public String getInterestRate()
	{
		return interestRate;
	}
	public void setInterestRate(String interestRate)
	{
		this.interestRate = interestRate;
	}
	public String getMinInterestBalance()
	{
		return minInterestBalance;
	}
	public void setMinInterestBalance(String minInterestBalance)
	{
		this.minInterestBalance = minInterestBalance;
	}
	public String getNewMoneyPerYear()
	{
		return newMoneyPerYear;
	}
	public void setNewMoneyPerYear(String newMoneyPerYear)
	{
		this.newMoneyPerYear = newMoneyPerYear;
	}
	public String getPin()
	{
		return pin;
	}
	public void setPin(String pin)
	{
		this.pin = pin;
	}
	public boolean isTaxable()
	{
		return taxable;
	}
	public void setTaxable(boolean taxable)
	{
		this.taxable = taxable;
	}
	public String getVestingPercentage()
	{
		return vestingPercentage;
	}
	public void setVestingPercentage(String vestingPercentage)
	{
		this.vestingPercentage = vestingPercentage;
	}	
	public String getPortfolioName()
	{
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
	}
	public String getAccountTypeID()
	{
		return accountTypeID;
	}
	public void setAccountTypeID(String accountTypeID)
	{
		this.accountTypeID = accountTypeID;
	}
	public String getBrokerID()
	{
		return brokerID;
	}
	public void setBrokerID(String brokerID)
	{
		this.brokerID = brokerID;
	}
	public String getInterestPaymentsPerYear()
	{
		return interestPaymentsPerYear;
	}
	public void setInterestPaymentsPerYear(String interestPaymentsPerYear)
	{
		this.interestPaymentsPerYear = interestPaymentsPerYear;
	}
	public String getPortfolioID()
	{
		return portfolioID;
	}
	public void setPortfolioID(String portfolioID)
	{
		this.portfolioID = portfolioID;
	}
	public String getStartingCheckNo()
	{
		return startingCheckNo;
	}
	public void setStartingCheckNo(String startingCheckNo)
	{
		this.startingCheckNo = startingCheckNo;
	}
		
	
}
