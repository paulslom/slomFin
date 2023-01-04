package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.util.DateUtil;
import com.pas.valueObject.AppDate;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MortgagePaymentUpdateForm extends SlomFinBaseActionForm
{
	public MortgagePaymentUpdateForm()
	{		
	}
	
	private Integer mortgagePaymentID;
	private Integer mortgageID;
	private String mortgageDescription;
	private AppDate mortgagePaymentDate = new AppDate();
	private String principalPaid;
	private String interestPaid;
	private String propertyTaxesPaid;
	private String pmiPaid;
	private String homeownersInsurancePaid;
	
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
		
		String tempDate = mortgagePaymentDate.getAppmonth() + "-"
						+ mortgagePaymentDate.getAppday() + "-"
						+ mortgagePaymentDate.getAppyear();
		
		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.MTGPMTFORM_PAYMENTDATE_FIELD_INVALID,
				new ActionMessage(ISlomFinMessageConstants.MTGPMTFORM_PAYMENTDATE_FIELD_INVALID));
		}
		
		return ae;
		
	}
	public String getHomeownersInsurancePaid()
	{
		return homeownersInsurancePaid;
	}
	public void setHomeownersInsurancePaid(String homeownersInsurancePaid)
	{
		this.homeownersInsurancePaid = homeownersInsurancePaid;
	}
	public String getInterestPaid()
	{
		return interestPaid;
	}
	public void setInterestPaid(String interestPaid)
	{
		this.interestPaid = interestPaid;
	}
	public String getMortgageDescription()
	{
		return mortgageDescription;
	}
	public void setMortgageDescription(String mortgageDescription)
	{
		this.mortgageDescription = mortgageDescription;
	}
	public Integer getMortgageID()
	{
		return mortgageID;
	}
	public void setMortgageID(Integer mortgageID)
	{
		this.mortgageID = mortgageID;
	}
	public AppDate getMortgagePaymentDate()
	{
		return mortgagePaymentDate;
	}
	public void setMortgagePaymentDate(AppDate mortgagePaymentDate)
	{
		this.mortgagePaymentDate = mortgagePaymentDate;
	}
	public Integer getMortgagePaymentID()
	{
		return mortgagePaymentID;
	}
	public void setMortgagePaymentID(Integer mortgagePaymentID)
	{
		this.mortgagePaymentID = mortgagePaymentID;
	}
	public String getPmiPaid()
	{
		return pmiPaid;
	}
	public void setPmiPaid(String pmiPaid)
	{
		this.pmiPaid = pmiPaid;
	}
	public String getPrincipalPaid()
	{
		return principalPaid;
	}
	public void setPrincipalPaid(String principalPaid)
	{
		this.principalPaid = principalPaid;
	}
	public String getPropertyTaxesPaid()
	{
		return propertyTaxesPaid;
	}
	public void setPropertyTaxesPaid(String propertyTaxesPaid)
	{
		this.propertyTaxesPaid = propertyTaxesPaid;
	}
		
	
}
