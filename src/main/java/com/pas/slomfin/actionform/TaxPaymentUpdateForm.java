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
public class TaxPaymentUpdateForm extends SlomFinBaseActionForm
{
	public TaxPaymentUpdateForm()
	{		
	}
	
	private Integer taxPaymentID;
	private Integer taxGroupID;
	private String taxGroupName;
	private Integer taxYear;
	private Integer taxPaymentTypeID;
	private String taxPaymentTypeDesc;
	private String taxPaymentDesc;
	private String taxPaymentAmount;
	private AppDate taxPaymentDate = new AppDate();
		
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
		
		String tempDate = taxPaymentDate.getAppmonth() + "-"
		+ taxPaymentDate.getAppday() + "-"
		+ taxPaymentDate.getAppyear();

		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.TAXPMTFORM_PAYMENTDATE_FIELD_INVALID,
				new ActionMessage(ISlomFinMessageConstants.TAXPMTFORM_PAYMENTDATE_FIELD_INVALID));
		}

		return ae;
		
	}
	public Integer getTaxGroupID()
	{
		return taxGroupID;
	}
	public void setTaxGroupID(Integer taxGroupID)
	{
		this.taxGroupID = taxGroupID;
	}
	public String getTaxPaymentAmount()
	{
		return taxPaymentAmount;
	}
	public void setTaxPaymentAmount(String taxPaymentAmount)
	{
		this.taxPaymentAmount = taxPaymentAmount;
	}
	public AppDate getTaxPaymentDate()
	{
		return taxPaymentDate;
	}
	public void setTaxPaymentDate(AppDate taxPaymentDate)
	{
		this.taxPaymentDate = taxPaymentDate;
	}
	public String getTaxPaymentDesc()
	{
		return taxPaymentDesc;
	}
	public void setTaxPaymentDesc(String taxPaymentDesc)
	{
		this.taxPaymentDesc = taxPaymentDesc;
	}
	public Integer getTaxPaymentID()
	{
		return taxPaymentID;
	}
	public void setTaxPaymentID(Integer taxPaymentID)
	{
		this.taxPaymentID = taxPaymentID;
	}
	public Integer getTaxPaymentTypeID()
	{
		return taxPaymentTypeID;
	}
	public void setTaxPaymentTypeID(Integer taxPaymentTypeID)
	{
		this.taxPaymentTypeID = taxPaymentTypeID;
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
	public String getTaxPaymentTypeDesc()
	{
		return taxPaymentTypeDesc;
	}
	public void setTaxPaymentTypeDesc(String taxPaymentTypeDesc)
	{
		this.taxPaymentTypeDesc = taxPaymentTypeDesc;
	}
		
}
