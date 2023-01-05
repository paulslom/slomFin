package com.pas.slomfin.actionform;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.dbobjects.Tblinvestment;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.util.PASUtil;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InvestmentsOwnedUpdateForm extends SlomFinBaseActionForm
{
	public InvestmentsOwnedUpdateForm()
	{	
		initialize();
	}
		
	private List<Tblinvestment> invOwnedList = new ArrayList<Tblinvestment>();
		
	public void initialize()
	{
		//initialize all variables
		
		String methodName = "initialize :: ";
		log.debug(methodName + " In");
		invOwnedList.clear();
		log.debug(methodName + " Out");
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");
		
		ActionErrors ae = new ActionErrors();

		String reqParm = request.getParameter("operation");
		
		//do not perform validation when cancelling
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
			return ae;		
		
		for (int i=0; i<invOwnedList.size(); i++)
		{	
			Tblinvestment investment = new Tblinvestment();
			investment = invOwnedList.get(i);
			
			if (investment.getMcurrentPrice().toString().length() == 0)
			{
				ae.add(ISlomFinMessageConstants.INVOWNEDUPDATEFORM_PRICE_MISSING,
					new ActionMessage(ISlomFinMessageConstants.INVOWNEDUPDATEFORM_PRICE_MISSING));
			}
			else
			   if (!PASUtil.isValidDecimalForUnits(investment.getMcurrentPrice().toString()))
			   {
				   log.info("Invalid price detected on: " + investment.getStickerSymbol() + " with price: " + investment.getMcurrentPrice().toString());
				 
				   ae.add(ISlomFinMessageConstants.INVOWNEDUPDATEFORM_PRICE_INVALID,
					new ActionMessage(ISlomFinMessageConstants.INVOWNEDUPDATEFORM_PRICE_INVALID));
			   }						 
		}
		
		return ae;
	}	   
	
    public List<Tblinvestment> getInvOwnedList()
    {
        return invOwnedList;
    }
    public void setInvOwnedList(List<Tblinvestment> invOwnedList)
    {
        this.invOwnedList = invOwnedList;
    }
    
    //  this is the method that will be called to save
    //  the indexed properties when the form is saved
    public Tblinvestment getInvOwnedItem(int index)
    {
        // make sure that orderList is not null
        if(this.invOwnedList == null)
        {
            this.invOwnedList = new ArrayList<Tblinvestment>();
        }
 
        // indexes do not come in order, populate empty spots
        while(index >= this.invOwnedList.size())
        {
            this.invOwnedList.add(new Tblinvestment());
        }
 
        // return the requested item
        return invOwnedList.get(index);
    }
    	
}
