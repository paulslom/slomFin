package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.util.DateUtil;
import com.pas.util.PASUtil;
import com.pas.valueObject.AppDate;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TrxUpdateForm extends SlomFinBaseActionForm
{
	public TrxUpdateForm()
	{		
	}
	
	private Integer transactionID;
	private Integer cashTranID;
	private Integer optionTranID;
	private Integer accountID;
	private Integer investmentID;
	private Integer transactionTypeID;
	private AppDate transactionDate = new AppDate();
	private AppDate transactionPostedDate = new AppDate();
	private String decUnits;
	private String price;
	private String costProceeds;
	private String trxAmount;
	private Integer checkNo;
	private String cashDescription;
	private Integer optionTypeID;
	private String strikePrice;
	private AppDate expirationDate = new AppDate();
	private boolean openingTrxInd;
	private Integer xferAccountID;
	private Integer wdCategoryID;
	private Integer cashDepositTypeID;	
	private String trxTypeDesc;
	private String invDesc;
	private String invTypeDesc;
	private Integer portfolioID;
	private Integer investmentTypeID;
	private String accountName;
	private String portfolioName;	
	private String categoryDescription;
	private String cashDepositTypeDescription;
	private String optionTypeDesc;
	private Integer dividendTaxableYear;
	private boolean finalTrxOfBillingCycle;
	
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
		
		if (investmentID != null)
		   if (investmentID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_INVESTMENT_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_INVESTMENT_FIELD_MISSING));
		   }

		String tempDate = transactionDate.getAppmonth() + "-"
						+ transactionDate.getAppday() + "-"
						+ transactionDate.getAppyear();
				
		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.INVALID_TRANSACTION_DATE,
				new ActionMessage(ISlomFinMessageConstants.INVALID_TRANSACTION_DATE));
		}
		
		String tempPostedDate = transactionPostedDate.getAppmonth() + "-"
							  + transactionPostedDate.getAppday() + "-"
							  + transactionPostedDate.getAppyear();

		if (!DateUtil.isValidDate(tempPostedDate))
		{
			ae.add(ISlomFinMessageConstants.INVALID_TRANSACTION_POSTED_DATE,
					new ActionMessage(ISlomFinMessageConstants.INVALID_TRANSACTION_POSTED_DATE));
		}
		
		if (decUnits != null)
		   if (decUnits.length() == 0)
	 	   {
	 		   ae.add(ISlomFinMessageConstants.TRXFORM_UNITS_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_UNITS_FIELD_MISSING));
		   }
		   else
				if (!PASUtil.isValidDecimalForUnits(decUnits))
				{
					ae.add(ISlomFinMessageConstants.TRXFORM_UNITS_FIELD_INVALID,
					   new ActionMessage(ISlomFinMessageConstants.TRXFORM_UNITS_FIELD_INVALID));
				}
		
		if (price != null)
		   if (price.length() == 0)
	 	   {
	 		   ae.add(ISlomFinMessageConstants.TRXFORM_PRICE_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_PRICE_FIELD_MISSING));
		   }
		   else
				if (!PASUtil.isValidDecimalForUnits(price))
				{
					ae.add(ISlomFinMessageConstants.TRXFORM_PRICE_FIELD_INVALID,
					   new ActionMessage(ISlomFinMessageConstants.TRXFORM_PRICE_FIELD_INVALID));
				}
		
		if (costProceeds != null)
		   if (costProceeds.length() == 0)
	 	   {
	 		   ae.add(ISlomFinMessageConstants.TRXFORM_COSTPROCEEDS_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_COSTPROCEEDS_FIELD_MISSING));
		   }
		   else
				if (!PASUtil.isValidDecimal(costProceeds))
				{
					ae.add(ISlomFinMessageConstants.TRXFORM_COSTPROCEEDS_FIELD_INVALID,
					   new ActionMessage(ISlomFinMessageConstants.TRXFORM_COSTPROCEEDS_FIELD_INVALID));
				}
		
		if (checkNo != null)
		   if (checkNo.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0) //integer field left empty is zero
		   {
	 		   ae.add(ISlomFinMessageConstants.TRXFORM_CHECKNUMBER_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_CHECKNUMBER_FIELD_MISSING));
		   }
		   else
				if (!PASUtil.isValidNumeric(checkNo.toString()))
				{
					ae.add(ISlomFinMessageConstants.TRXFORM_CHECKNUMBER_FIELD_INVALID,
					   new ActionMessage(ISlomFinMessageConstants.TRXFORM_CHECKNUMBER_FIELD_INVALID));
				}
		
		if (dividendTaxableYear != null)
			   if (dividendTaxableYear.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0) //integer field left empty is zero
			   {
		 		   ae.add(ISlomFinMessageConstants.TRXFORM_DIVIDENDYEAR_FIELD_MISSING,
					  new ActionMessage(ISlomFinMessageConstants.TRXFORM_DIVIDENDYEAR_FIELD_MISSING));
			   }
			   else
					if (!PASUtil.isValidNumeric(dividendTaxableYear.toString()))
					{
						ae.add(ISlomFinMessageConstants.TRXFORM_DIVIDENDYEAR_FIELD_INVALID,
						   new ActionMessage(ISlomFinMessageConstants.TRXFORM_DIVIDENDYEAR_FIELD_INVALID));
					}
		
		if (cashDepositTypeID != null)
		   if (cashDepositTypeID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0
		   && wdCategoryID == null
		   && (!trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)))	  
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_CASHDEPOSITTYPE_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_CASHDEPOSITTYPE_FIELD_MISSING));
		   }
		
		if (wdCategoryID != null)
		   if (wdCategoryID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0
		   && cashDepositTypeID == null)	   
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_WDCATEGORY_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_WDCATEGORY_FIELD_MISSING));
		   }
		
		if (xferAccountID != null)
		   if (xferAccountID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_XFERACCOUNT_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_XFERACCOUNT_FIELD_MISSING));
		   }
		
		if (optionTypeID != null)
		   if (optionTypeID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
		   {
			   ae.add(ISlomFinMessageConstants.TRXFORM_OPTIONTYPE_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_OPTIONTYPE_FIELD_MISSING));
		   }
		
		if (strikePrice != null)
		   if (strikePrice.length() == 0)
	 	   {
	 		   ae.add(ISlomFinMessageConstants.TRXFORM_STRIKEPRICE_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXFORM_STRIKEPRICE_FIELD_MISSING));
		   }
		   else
				if (!PASUtil.isValidDecimal(strikePrice))
				{
					ae.add(ISlomFinMessageConstants.TRXFORM_STRIKEPRICE_FIELD_INVALID,
					   new ActionMessage(ISlomFinMessageConstants.TRXFORM_STRIKEPRICE_FIELD_INVALID));
				}

		if (expirationDate.getAppday() != null)
		{	
			tempDate = expirationDate.getAppmonth() + "-"
					 + expirationDate.getAppday() + "-"
					 + expirationDate.getAppyear();

			if (!DateUtil.isValidDate(tempDate))
			{
				ae.add(ISlomFinMessageConstants.TRXFORM_EXPIRATIONDATE_FIELD_INVALID,
					new ActionMessage(ISlomFinMessageConstants.TRXFORM_EXPIRATIONDATE_FIELD_INVALID));
			}
		}
		
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
	public String getCashDepositTypeDescription()
	{
		return cashDepositTypeDescription;
	}
	public void setCashDepositTypeDescription(String cashDepositTypeDescription)
	{
		this.cashDepositTypeDescription = cashDepositTypeDescription;
	}
	public Integer getCashDepositTypeID()
	{
		return cashDepositTypeID;
	}
	public void setCashDepositTypeID(Integer cashDepositTypeID)
	{
		this.cashDepositTypeID = cashDepositTypeID;
	}
	public String getCashDescription()
	{
		return cashDescription;
	}
	public void setCashDescription(String cashDescription)
	{
		this.cashDescription = cashDescription;
	}
	public Integer getCashTranID()
	{
		return cashTranID;
	}
	public void setCashTranID(Integer cashTranID)
	{
		this.cashTranID = cashTranID;
	}
	public String getCategoryDescription()
	{
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription)
	{
		this.categoryDescription = categoryDescription;
	}
	public Integer getCheckNo()
	{
		return checkNo;
	}
	public void setCheckNo(Integer checkNo)
	{
		this.checkNo = checkNo;
	}
	public String getCostProceeds()
	{
		return costProceeds;
	}
	public void setCostProceeds(String costProceeds)
	{
		this.costProceeds = costProceeds;
	}
	public String getDecUnits()
	{
		return decUnits;
	}
	public void setDecUnits(String decUnits)
	{
		this.decUnits = decUnits;
	}
	public AppDate getExpirationDate()
	{
		return expirationDate;
	}
	public void setExpirationDate(AppDate expirationDate)
	{
		this.expirationDate = expirationDate;
	}
	public String getInvDesc()
	{
		return invDesc;
	}
	public void setInvDesc(String invDesc)
	{
		this.invDesc = invDesc;
	}
	public Integer getInvestmentID()
	{
		return investmentID;
	}
	public void setInvestmentID(Integer investmentID)
	{
		this.investmentID = investmentID;
	}
	public Integer getInvestmentTypeID()
	{
		return investmentTypeID;
	}
	public void setInvestmentTypeID(Integer investmentTypeID)
	{
		this.investmentTypeID = investmentTypeID;
	}
	public String getInvTypeDesc()
	{
		return invTypeDesc;
	}
	public void setInvTypeDesc(String invTypeDesc)
	{
		this.invTypeDesc = invTypeDesc;
	}
	public boolean isOpeningTrxInd()
	{
		return openingTrxInd;
	}
	public void setOpeningTrxInd(boolean openingTrxInd)
	{
		this.openingTrxInd = openingTrxInd;
	}
	public Integer getOptionTranID()
	{
		return optionTranID;
	}
	public void setOptionTranID(Integer optionTranID)
	{
		this.optionTranID = optionTranID;
	}
	public Integer getOptionTypeID()
	{
		return optionTypeID;
	}
	public void setOptionTypeID(Integer optionTypeID)
	{
		this.optionTypeID = optionTypeID;
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
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getStrikePrice()
	{
		return strikePrice;
	}
	public void setStrikePrice(String strikePrice)
	{
		this.strikePrice = strikePrice;
	}	
	public AppDate getTransactionDate()
	{
		return transactionDate;
	}
	public void setTransactionDate(AppDate transactionDate)
	{
		this.transactionDate = transactionDate;
	}
	public Integer getTransactionID()
	{
		return transactionID;
	}
	public void setTransactionID(Integer transactionID)
	{
		this.transactionID = transactionID;
	}
	public Integer getTransactionTypeID()
	{
		return transactionTypeID;
	}
	public void setTransactionTypeID(Integer transactionTypeID)
	{
		this.transactionTypeID = transactionTypeID;
	}
	public String getTrxAmount()
	{
		return trxAmount;
	}
	public void setTrxAmount(String trxAmount)
	{
		this.trxAmount = trxAmount;
	}
	public String getTrxTypeDesc()
	{
		return trxTypeDesc;
	}
	public void setTrxTypeDesc(String trxTypeDesc)
	{
		this.trxTypeDesc = trxTypeDesc;
	}
	public Integer getWdCategoryID()
	{
		return wdCategoryID;
	}
	public void setWdCategoryID(Integer wdCategoryID)
	{
		this.wdCategoryID = wdCategoryID;
	}
	public Integer getXferAccountID()
	{
		return xferAccountID;
	}
	public void setXferAccountID(Integer xferAccountID)
	{
		this.xferAccountID = xferAccountID;
	}
	public String getOptionTypeDesc()
	{
		return optionTypeDesc;
	}
	public void setOptionTypeDesc(String optionTypeDesc)
	{
		this.optionTypeDesc = optionTypeDesc;
	}
	public Integer getDividendTaxableYear()
	{
		return dividendTaxableYear;
	}
	public void setDividendTaxableYear(Integer dividendTaxableYear)
	{
		this.dividendTaxableYear = dividendTaxableYear;
	}
	public AppDate getTransactionPostedDate()
	{
		return transactionPostedDate;
	}
	public void setTransactionPostedDate(AppDate transactionPostedDate)
	{
		this.transactionPostedDate = transactionPostedDate;
	}
	
	public boolean isFinalTrxOfBillingCycle() 
	{
		return finalTrxOfBillingCycle;
	}
	
	public void setFinalTrxOfBillingCycle(boolean finalTrxOfBillingCycle) 
	{
		this.finalTrxOfBillingCycle = finalTrxOfBillingCycle;
	}
	
}
