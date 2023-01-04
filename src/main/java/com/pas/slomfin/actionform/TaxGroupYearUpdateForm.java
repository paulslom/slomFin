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
public class TaxGroupYearUpdateForm extends SlomFinBaseActionForm
{
	public TaxGroupYearUpdateForm()
	{		
	}
	
	private Integer taxGroupYearID;
	private Integer taxGroupID;
	private String taxGroupName;
	private Integer taxYear;
	private String filingStatus;
	private String dependents;
	private String capitalLossCarryover;
	private String iraDistribution;
	private String prevYearStateRefund;
	private String otherItemized;
	private String otherItemizedDesc;
	private String carTax;
	private String dividendTaxRate;
	private String otherIncome;
	private String otherIncomeDesc;
	private String dayCareExpensesPaid;
	private String qualifiedDividends;
	private String dividendsForeignTax;
	private String dividendsReturnOfCapital;
		
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
		
		if (otherItemized != null)
			if (otherItemized.length() > 0) //means there's an entry in otherItemized; desc is required
			   if (otherItemizedDesc == null)
			   {
				   ae.add(ISlomFinMessageConstants.TGYFORM_OTHERITEMIZED_MISSING,
					  new ActionMessage(ISlomFinMessageConstants.TGYFORM_OTHERITEMIZED_MISSING));
			   }
			   else
				   if (otherItemizedDesc.length() == 0)
				   {
					   ae.add(ISlomFinMessageConstants.TGYFORM_OTHERITEMIZED_MISSING,
						  new ActionMessage(ISlomFinMessageConstants.TGYFORM_OTHERITEMIZED_MISSING));
				   }
		
		if (otherIncome != null)
			if (otherIncome.length() > 0) //means there's an entry in otherIncome; desc is required
			   if (otherIncomeDesc == null)
			   {
				   ae.add(ISlomFinMessageConstants.TGYFORM_OTHERINCOME_MISSING,
					  new ActionMessage(ISlomFinMessageConstants.TGYFORM_OTHERINCOME_MISSING));
			   }
			   else
				   if (otherIncomeDesc.length() == 0)
				   {
					   ae.add(ISlomFinMessageConstants.TGYFORM_OTHERINCOME_MISSING,
						  new ActionMessage(ISlomFinMessageConstants.TGYFORM_OTHERINCOME_MISSING));
				   }
		
		return ae;
		
	}
	public String getCapitalLossCarryover()
	{
		return capitalLossCarryover;
	}
	public void setCapitalLossCarryover(String capitalLossCarryover)
	{
		this.capitalLossCarryover = capitalLossCarryover;
	}
	public String getCarTax()
	{
		return carTax;
	}
	public void setCarTax(String carTax)
	{
		this.carTax = carTax;
	}
	public String getDayCareExpensesPaid()
	{
		return dayCareExpensesPaid;
	}
	public void setDayCareExpensesPaid(String dayCareExpensesPaid)
	{
		this.dayCareExpensesPaid = dayCareExpensesPaid;
	}
	public String getDependents()
	{
		return dependents;
	}
	public void setDependents(String dependents)
	{
		this.dependents = dependents;
	}
	public String getDividendTaxRate()
	{
		return dividendTaxRate;
	}
	public void setDividendTaxRate(String dividendTaxRate)
	{
		this.dividendTaxRate = dividendTaxRate;
	}
	public String getFilingStatus()
	{
		return filingStatus;
	}
	public void setFilingStatus(String filingStatus)
	{
		this.filingStatus = filingStatus;
	}
	public String getIraDistribution()
	{
		return iraDistribution;
	}
	public void setIraDistribution(String iraDistribution)
	{
		this.iraDistribution = iraDistribution;
	}
	public String getOtherIncome()
	{
		return otherIncome;
	}
	public void setOtherIncome(String otherIncome)
	{
		this.otherIncome = otherIncome;
	}
	public String getOtherIncomeDesc()
	{
		return otherIncomeDesc;
	}
	public void setOtherIncomeDesc(String otherIncomeDesc)
	{
		this.otherIncomeDesc = otherIncomeDesc;
	}
	public String getOtherItemized()
	{
		return otherItemized;
	}
	public void setOtherItemized(String otherItemized)
	{
		this.otherItemized = otherItemized;
	}
	public String getOtherItemizedDesc()
	{
		return otherItemizedDesc;
	}
	public void setOtherItemizedDesc(String otherItemizedDesc)
	{
		this.otherItemizedDesc = otherItemizedDesc;
	}
	public String getPrevYearStateRefund()
	{
		return prevYearStateRefund;
	}
	public void setPrevYearStateRefund(String prevYearStateRefund)
	{
		this.prevYearStateRefund = prevYearStateRefund;
	}
	public Integer getTaxGroupID()
	{
		return taxGroupID;
	}
	public void setTaxGroupID(Integer taxGroupID)
	{
		this.taxGroupID = taxGroupID;
	}
	public Integer getTaxGroupYearID()
	{
		return taxGroupYearID;
	}
	public void setTaxGroupYearID(Integer taxGroupYearID)
	{
		this.taxGroupYearID = taxGroupYearID;
	}
	public Integer getTaxYear()
	{
		return taxYear;
	}
	public void setTaxYear(Integer taxYear)
	{
		this.taxYear = taxYear;
	}
	public String getTaxGroupName()
	{
		return taxGroupName;
	}
	public void setTaxGroupName(String taxGroupName)
	{
		this.taxGroupName = taxGroupName;
	}
	public String getQualifiedDividends()
	{
		return qualifiedDividends;
	}
	public void setQualifiedDividends(String qualifiedDividends)
	{
		this.qualifiedDividends = qualifiedDividends;
	}
	public String getDividendsForeignTax()
	{
		return dividendsForeignTax;
	}
	public void setDividendsForeignTax(String dividendsForeignTax)
	{
		this.dividendsForeignTax = dividendsForeignTax;
	}
	public String getDividendsReturnOfCapital()
	{
		return dividendsReturnOfCapital;
	}
	public void setDividendsReturnOfCapital(String dividendsReturnOfCapital)
	{
		this.dividendsReturnOfCapital = dividendsReturnOfCapital;
	}
	
	
	
}
