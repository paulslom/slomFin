package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Dividend;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.TaxesSelection;

public class ReportCapitalGainsAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportCapitalGainsAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TaxesSelection federalTaxesSelection = new TaxesSelection();
		Investor investor = cache.getInvestor(req.getSession());
		federalTaxesSelection.setTaxGroupID(new Integer(investor.getTaxGroupID()));
		federalTaxesSelection.setTaxYear(Integer.parseInt(req.getParameter("year")));
		
		String taxableInd = req.getParameter("taxable");
		
		if (taxableInd != null && taxableInd.equalsIgnoreCase("on"))
		{
			federalTaxesSelection.setTaxableInd(true);
		}
		else
		{
			federalTaxesSelection.setTaxableInd(false);
		}
		
		cache.setObject("RequestObject",federalTaxesSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportCapitalGainsAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Dividend> dataLayerList = (List)cache.getObject("ResponseObject",req.getSession());	
		
		if (dataLayerList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTCAPITALGAINSLIST, dataLayerList);	
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportCapitalGainsAction postprocessAction");
				
	}

}
