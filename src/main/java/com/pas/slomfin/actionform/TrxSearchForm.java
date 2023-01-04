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
public class TrxSearchForm extends SlomFinBaseActionForm
{
	public TrxSearchForm()
	{		
	}
	
	private String trxSearchText;
	private AppDate fromDate = new AppDate();
	private AppDate toDate = new AppDate();
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();

		if (fromDate == null)
			log.debug(methodName + " from date is null; this is ok");
		else
			if (fromDate.isEmpty())
				log.debug(methodName + " End date is spaces (no date entered); this is ok");
			else
			{	
				String tempDate = fromDate.getAppmonth() + "-" + fromDate.getAppday() + "-" + fromDate.getAppyear();
				if (!(DateUtil.isValidDate(tempDate)))
					ae.add(ISlomFinMessageConstants.INVALID_FROM_DATE,
						new ActionMessage(ISlomFinMessageConstants.INVALID_FROM_DATE));					
			}
		
		if (toDate == null)
			log.debug(methodName + " to date is null; this is ok");
		else
			if (toDate.isEmpty())
				log.debug(methodName + " To date is spaces (no date entered); this is ok");
			else
			{	
				String tempDate = toDate.getAppmonth() + "-" + toDate.getAppday() + "-" + toDate.getAppyear();
				if (!(DateUtil.isValidDate(tempDate)))
					ae.add(ISlomFinMessageConstants.INVALID_TO_DATE,
						new ActionMessage(ISlomFinMessageConstants.INVALID_TO_DATE));					
			}	
		
		if (trxSearchText != null)
			if (trxSearchText.length() == 0)
		 	{
		 	   ae.add(ISlomFinMessageConstants.TRXSEARCHFORM_SEARCHTEXT_FIELD_MISSING,
				  new ActionMessage(ISlomFinMessageConstants.TRXSEARCHFORM_SEARCHTEXT_FIELD_MISSING));
			}
		
		return ae;

	}

	public String getTrxSearchText() {
		return trxSearchText;
	}

	public void setTrxSearchText(String trxSearchText) {
		this.trxSearchText = trxSearchText;
	}

	public AppDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(AppDate fromDate) {
		this.fromDate = fromDate;
	}

	public AppDate getToDate() {
		return toDate;
	}

	public void setToDate(AppDate toDate) {
		this.toDate = toDate;
	}

		
	
	
}
