package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblinvestment;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.InvestmentsOwnedUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;

public class InvestmentsOwnedUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestmentsOwnedUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblinvestment investment = new Tblinvestment();
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an add
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE))
		{	
			cache.setGoToDBInd(req.getSession(), false);
			cache.setObject("RequestObject", investment, req.getSession());	
		}
		else
		{
			InvestmentsOwnedUpdateForm investmentsOwnedUpdateForm = (InvestmentsOwnedUpdateForm)form;
				
			List<Tblinvestment> invListFromForm = investmentsOwnedUpdateForm.getInvOwnedList();
			List<Tblinvestment> invListToDAO = new ArrayList<Tblinvestment>();
						
			for (int i=0; i<invListFromForm.size(); i++)
			{	
				Tblinvestment investmentFromForm = invListFromForm.get(i);
				
				log.debug("Investment = " + investmentFromForm.getStickerSymbol() + " and price as string = " + investmentFromForm.getCurrentPriceAsString());
				
				investmentFromForm.setMcurrentPrice(new BigDecimal(investmentFromForm.getCurrentPriceAsString()));				
			
				invListToDAO.add(investmentFromForm);	
				
			}	
			
			cache.setGoToDBInd(req.getSession(), true);			
			cache.setObject("RequestObject", invListToDAO, req.getSession());
		}
	
		return true;
	
	}	
	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
		HttpServletRequest req, HttpServletResponse res, int operation,
		ActionComposite ac) throws PresentationException,
		BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestmentsOwnedUpdateAction postprocessAction");
	
		req.getSession().removeAttribute("investmentList");
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
	
		log.debug("exiting InvestmentsOwnedUpdateAction postprocessAction");
			
	}
}
