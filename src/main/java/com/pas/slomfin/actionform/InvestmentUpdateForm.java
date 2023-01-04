package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.pas.slomfin.constants.ISlomFinAppConstants;


/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InvestmentUpdateForm extends SlomFinBaseActionForm
{
	public InvestmentUpdateForm()
	{		
	}
	
	private Integer investmentID;
	private String investmentTypeID;
	private String investmentTypeDesc;
	private String tickerSymbol;	
	private String description;
	private String optionMultiplier;
	private String currentPrice;
	private String underlyingStockID;
	private String underlyingStockDesc;
	private String dividendsPerYear;
	private String assetClassID;
	private String assetClassDesc;
		
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
	public String getAssetClassID()
	{
		return assetClassID;
	}
	public void setAssetClassID(String assetClassID)
	{
		this.assetClassID = assetClassID;
	}
	public String getCurrentPrice()
	{
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice)
	{
		this.currentPrice = currentPrice;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDividendsPerYear()
	{
		return dividendsPerYear;
	}
	public void setDividendsPerYear(String dividendsPerYear)
	{
		this.dividendsPerYear = dividendsPerYear;
	}
	public Integer getInvestmentID()
	{
		return investmentID;
	}
	public void setInvestmentID(Integer investmentID)
	{
		this.investmentID = investmentID;
	}
	public String getInvestmentTypeID()
	{
		return investmentTypeID;
	}
	public void setInvestmentTypeID(String investmentTypeID)
	{
		this.investmentTypeID = investmentTypeID;
	}
	public String getOptionMultiplier()
	{
		return optionMultiplier;
	}
	public void setOptionMultiplier(String optionMultiplier)
	{
		this.optionMultiplier = optionMultiplier;
	}
	public String getTickerSymbol()
	{
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol)
	{
		this.tickerSymbol = tickerSymbol;
	}
	public String getUnderlyingStockID()
	{
		return underlyingStockID;
	}
	public void setUnderlyingStockID(String underlyingStockID)
	{
		this.underlyingStockID = underlyingStockID;
	}
	public String getAssetClassDesc()
	{
		return assetClassDesc;
	}
	public void setAssetClassDesc(String assetClassDesc)
	{
		this.assetClassDesc = assetClassDesc;
	}
	public String getInvestmentTypeDesc()
	{
		return investmentTypeDesc;
	}
	public void setInvestmentTypeDesc(String investmentTypeDesc)
	{
		this.investmentTypeDesc = investmentTypeDesc;
	}
	public String getUnderlyingStockDesc()
	{
		return underlyingStockDesc;
	}
	public void setUnderlyingStockDesc(String underlyingStockDesc)
	{
		this.underlyingStockDesc = underlyingStockDesc;
	}
	
		
}
