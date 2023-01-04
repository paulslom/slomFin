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
public class PaycheckHistoryUpdateForm extends SlomFinBaseActionForm
{
	public PaycheckHistoryUpdateForm()
	{		
	}
	
	private Integer paycheckHistoryID;
	private Integer paycheckTypeID;
	private String employer;
	private AppDate paycheckHistoryDate = new AppDate();
	private String grossPay;
	private String federalWithholding;
	private String stateWithholding;
	private String retirementDeferred;
	private String ssWithholding;
	private String medicareWithholding;
	private String dental;
	private String medical;
	private String groupLifeInsurance;
	private String groupLifeIncome;
	private String vision;
	private String parking;
	private String cafeteria;
	private String roth401k;
	private String fsaAmount; //flexible spending account amount
		
	
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
		
		String tempDate = paycheckHistoryDate.getAppmonth() + "-"
						+ paycheckHistoryDate.getAppday() + "-"
						+ paycheckHistoryDate.getAppyear();
		
		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.PAYCHECKHISTORYFORM_DATE_FIELD_INVALID,
				new ActionMessage(ISlomFinMessageConstants.PAYCHECKHISTORYFORM_DATE_FIELD_INVALID));
		}
		
		if (paycheckTypeID != null)
			if (paycheckTypeID.compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)	  
			{
			   ae.add(ISlomFinMessageConstants.PAYCHECKHISTORYFORM_PAYCHECKTYPEID_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.PAYCHECKHISTORYFORM_PAYCHECKTYPEID_MISSING));
			}
		
		return ae;
		
	}
		
	
    public String getDental()
    {
        return dental;
    }
    public void setDental(String dental)
    {
        this.dental = dental;
    }
    public String getEmployer()
    {
        return employer;
    }
    public void setEmployer(String employer)
    {
        this.employer = employer;
    }
    public String getFederalWithholding()
    {
        return federalWithholding;
    }
    public void setFederalWithholding(String federalWithholding)
    {
        this.federalWithholding = federalWithholding;
    }
    public String getGrossPay()
    {
        return grossPay;
    }
    public void setGrossPay(String grossPay)
    {
        this.grossPay = grossPay;
    }
    public String getGroupLifeIncome()
    {
        return groupLifeIncome;
    }
    public void setGroupLifeIncome(String groupLifeIncome)
    {
        this.groupLifeIncome = groupLifeIncome;
    }
    public String getGroupLifeInsurance()
    {
        return groupLifeInsurance;
    }
    public void setGroupLifeInsurance(String groupLifeInsurance)
    {
        this.groupLifeInsurance = groupLifeInsurance;
    }    
    public String getMedical()
    {
        return medical;
    }
    public void setMedical(String medical)
    {
        this.medical = medical;
    }
    public String getMedicareWithholding()
    {
        return medicareWithholding;
    }
    public void setMedicareWithholding(String medicareWithholding)
    {
        this.medicareWithholding = medicareWithholding;
    }
    public String getRetirementDeferred()
    {
        return retirementDeferred;
    }
    public void setRetirementDeferred(String retirementDeferred)
    {
        this.retirementDeferred = retirementDeferred;
    }
    public String getSsWithholding()
    {
        return ssWithholding;
    }
    public void setSsWithholding(String ssWithholding)
    {
        this.ssWithholding = ssWithholding;
    }
    public String getStateWithholding()
    {
        return stateWithholding;
    }
    public void setStateWithholding(String stateWithholding)
    {
        this.stateWithholding = stateWithholding;
    }
    public AppDate getPaycheckHistoryDate()
    {
        return paycheckHistoryDate;
    }
    public void setPaycheckHistoryDate(AppDate paycheckHistoryDate)
    {
        this.paycheckHistoryDate = paycheckHistoryDate;
    }
    public Integer getPaycheckHistoryID()
    {
        return paycheckHistoryID;
    }
    public void setPaycheckHistoryID(Integer paycheckHistoryID)
    {
        this.paycheckHistoryID = paycheckHistoryID;
    }
	public Integer getPaycheckTypeID()
	{
		return paycheckTypeID;
	}
	public void setPaycheckTypeID(Integer paycheckTypeID)
	{
		this.paycheckTypeID = paycheckTypeID;
	}
	public String getVision() {
		return vision;
	}
	public void setVision(String vision) {
		this.vision = vision;
	}
	public String getParking() {
		return parking;
	}
	public void setParking(String parking) {
		this.parking = parking;
	}
	public String getCafeteria()
	{
		return cafeteria;
	}
	public void setCafeteria(String cafeteria)
	{
		this.cafeteria = cafeteria;
	}
	
	public String getRoth401k()
	{
		return roth401k;
	}
	public void setRoth401k(String roth401k)
	{
		this.roth401k = roth401k;
	}
	public String getFsaAmount() 
	{
		return fsaAmount;
	}
	public void setFsaAmount(String fsaAmount) 
	{
		this.fsaAmount = fsaAmount;
	}
}
