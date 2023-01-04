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
public class MortgageUpdateForm extends SlomFinBaseActionForm
{
	public MortgageUpdateForm()
	{		
	}
	
	private Integer mortgageID;
	private String description;
	private String paymentAccountID;
	private String paymentAccountDescription;
	private String principalAccountID;
	private String principalAccountDescription;	
	private AppDate mortgageStartDate = new AppDate();
	private AppDate mortgageEndDate = new AppDate();
	private String originalLoanAmount;
	private String interestRate;
	private Integer termInYears;
	private String homeownersInsurance;
	private String pmi;
	private String propertyTaxes;
	private String extraPrincipal;
		
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
		
		String tempDate = mortgageStartDate.getAppmonth() + "-"
						+ mortgageStartDate.getAppday() + "-"
						+ mortgageStartDate.getAppyear();
		
		if (!DateUtil.isValidDate(tempDate))
		{
			ae.add(ISlomFinMessageConstants.MTGFORM_STARTDATE_FIELD_INVALID,
				new ActionMessage(ISlomFinMessageConstants.MTGFORM_STARTDATE_FIELD_INVALID));
		}
		
		if (mortgageEndDate.getAppday() != null)
		{	
			tempDate = mortgageEndDate.getAppmonth() + "-"
					 + mortgageEndDate.getAppday() + "-"
					 + mortgageEndDate.getAppyear();

			if (!DateUtil.isValidDate(tempDate))
			{
				ae.add(ISlomFinMessageConstants.MTGFORM_ENDDATE_FIELD_INVALID,
					new ActionMessage(ISlomFinMessageConstants.MTGFORM_ENDDATE_FIELD_INVALID));
			}
		}
		
		return ae;
		
	}
		
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the extraPrincipal.
	 */
	public String getExtraPrincipal() {
		return extraPrincipal;
	}
	/**
	 * @param extraPrincipal The extraPrincipal to set.
	 */
	public void setExtraPrincipal(String extraPrincipal) {
		this.extraPrincipal = extraPrincipal;
	}
	/**
	 * @return Returns the homeownersInsurance.
	 */
	public String getHomeownersInsurance() {
		return homeownersInsurance;
	}
	/**
	 * @param homeownersInsurance The homeownersInsurance to set.
	 */
	public void setHomeownersInsurance(String homeownersInsurance) {
		this.homeownersInsurance = homeownersInsurance;
	}
	/**
	 * @return Returns the interestRate.
	 */
	public String getInterestRate() {
		return interestRate;
	}
	/**
	 * @param interestRate The interestRate to set.
	 */
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	/**
	 * @return Returns the mortgageEndDate.
	 */
	public AppDate getMortgageEndDate() {
		return mortgageEndDate;
	}
	/**
	 * @param mortgageEndDate The mortgageEndDate to set.
	 */
	public void setMortgageEndDate(AppDate mortgageEndDate) {
		this.mortgageEndDate = mortgageEndDate;
	}
	/**
	 * @return Returns the mortgageStartDate.
	 */
	public AppDate getMortgageStartDate() {
		return mortgageStartDate;
	}
	/**
	 * @param mortgageStartDate The mortgageStartDate to set.
	 */
	public void setMortgageStartDate(AppDate mortgageStartDate) {
		this.mortgageStartDate = mortgageStartDate;
	}
	/**
	 * @return Returns the originalLoanAmount.
	 */
	public String getOriginalLoanAmount() {
		return originalLoanAmount;
	}
	/**
	 * @param originalLoanAmount The originalLoanAmount to set.
	 */
	public void setOriginalLoanAmount(String originalLoanAmount) {
		this.originalLoanAmount = originalLoanAmount;
	}
	/**
	 * @return Returns the pmi.
	 */
	public String getPmi() {
		return pmi;
	}
	/**
	 * @param pmi The pmi to set.
	 */
	public void setPmi(String pmi) {
		this.pmi = pmi;
	}
	/**
	 * @return Returns the propertyTaxes.
	 */
	public String getPropertyTaxes() {
		return propertyTaxes;
	}
	/**
	 * @param propertyTaxes The propertyTaxes to set.
	 */
	public void setPropertyTaxes(String propertyTaxes) {
		this.propertyTaxes = propertyTaxes;
	}
	/**
	 * @return Returns the termInYears.
	 */
	public Integer getTermInYears() {
		return termInYears;
	}
	/**
	 * @param termInYears The termInYears to set.
	 */
	public void setTermInYears(Integer termInYears) {
		this.termInYears = termInYears;
	}
	/**
	 * @return Returns the mortgageID.
	 */
	public Integer getMortgageID() {
		return mortgageID;
	}
	/**
	 * @param mortgageID The mortgageID to set.
	 */
	public void setMortgageID(Integer mortgageID) {
		this.mortgageID = mortgageID;
	}
	/**
	 * @return Returns the paymentAccountID.
	 */
	public String getPaymentAccountID() {
		return paymentAccountID;
	}
	/**
	 * @param paymentAccountID The paymentAccountID to set.
	 */
	public void setPaymentAccountID(String paymentAccountID) {
		this.paymentAccountID = paymentAccountID;
	}
	/**
	 * @return Returns the principalAccountID.
	 */
	public String getPrincipalAccountID() {
		return principalAccountID;
	}
	/**
	 * @param principalAccountID The principalAccountID to set.
	 */
	public void setPrincipalAccountID(String principalAccountID) {
		this.principalAccountID = principalAccountID;
	}
	public String getPaymentAccountDescription()
	{
		return paymentAccountDescription;
	}
	public void setPaymentAccountDescription(String paymentAccountDescription)
	{
		this.paymentAccountDescription = paymentAccountDescription;
	}
	public String getPrincipalAccountDescription()
	{
		return principalAccountDescription;
	}
	public void setPrincipalAccountDescription(String principalAccountDescription)
	{
		this.principalAccountDescription = principalAccountDescription;
	}
}
