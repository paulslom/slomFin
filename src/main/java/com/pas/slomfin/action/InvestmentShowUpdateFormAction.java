package com.pas.slomfin.action;

import java.util.List;
import java.text.DecimalFormat;
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
import com.pas.slomfin.actionform.InvestmentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;

public class InvestmentShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestmentShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		  
		InvestmentUpdateForm investmentForm = (InvestmentUpdateForm) form;		
		investmentForm.initialize();
		
		String investmentShowParm = req.getParameter("investmentShowParm");
		Integer invID = new Integer(0);
		
		if (investmentShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String investmentID = req.getParameter("investmentID");
		   invID = new Integer(investmentID);
		   cache.setGoToDBInd(req.getSession(), true);
		}   
						
		cache.setObject("RequestObject", invID, req.getSession());		
		
		log.debug("exiting InvestmentShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestmentShowUpdateFormAction postprocessAction");
	
		String investmentShowParm = req.getParameter("investmentShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tblinvestment> investmentList = null;
		
		if (!(investmentShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   investmentList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblinvestment investment = new Tblinvestment();
		
		InvestmentUpdateForm investmentForm = (InvestmentUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
				
		if (investmentShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			investmentForm.initialize();				
		}
		else //not an add	
		{	
		   	investment = investmentList.get(0);			
			
			investmentForm.setInvestmentID(investment.getIinvestmentId());		
						
			if (investment.getTblinvestmenttype().getIinvestmentTypeId() == null)
			   investmentForm.setInvestmentTypeID(null);
			else
				investmentForm.setInvestmentTypeID(investment.getTblinvestmenttype().getIinvestmentTypeId().toString());	
			
			if (investment.getStickerSymbol() == null)
			   investmentForm.setTickerSymbol(null);
			else
			   investmentForm.setTickerSymbol(investment.getStickerSymbol());
			
			investmentForm.setDescription(investment.getSdescription());
			
			if (investment.getDoptionMultiplier() == null)
			   investmentForm.setOptionMultiplier(null);
			else
			   investmentForm.setOptionMultiplier(investment.getDoptionMultiplier().toString());	
			
			if (investment.getMcurrentPrice() == null)
			   investmentForm.setCurrentPrice(null);
			else
			   investmentForm.setCurrentPrice(amtFormat.format(investment.getMcurrentPrice()));	
						
			if (investment.getIdividendsPerYear() == null)
			   investmentForm.setDividendsPerYear(null);
			else
			   investmentForm.setDividendsPerYear(investment.getIdividendsPerYear().toString());	
			
			if (investment.getTblassetclass().getIassetClassId() == null)
			   investmentForm.setAssetClassID(null);
			else
			   investmentForm.setAssetClassID(investment.getTblassetclass().getIassetClassId().toString());	
			
			if (investment.getTblassetclass().getSassetClass() == null)
			   investmentForm.setAssetClassDesc(null);
			else
			   investmentForm.setAssetClassDesc(investment.getTblassetclass().getSassetClass());
			
			if (investment.getTblinvestmenttype().getSdescription() == null)
			   investmentForm.setInvestmentTypeDesc(null);
			else
			   investmentForm.setInvestmentTypeDesc(investment.getTblinvestmenttype().getSdescription());
						
		}
		
		String invTypesListName = ISlomFinAppConstants.DROPDOWN_INVESTMENTTYPES;			
		List invTypes = (List) req.getSession().getServletContext().getAttribute(invTypesListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_INVESTMENTTYPES, invTypes);
		
		String assetClassesListName = ISlomFinAppConstants.DROPDOWN_ASSETCLASSES;			
		List assetClasses = (List) req.getSession().getServletContext().getAttribute(assetClassesListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_ASSETCLASSES, assetClasses);
		
		String underlyingStocksListName = ISlomFinAppConstants.DROPDOWN_INVESTMENTS_BYINVTYP + ISlomFinAppConstants.STOCK_INVTYPE_ID;
		List underlyingStocks = (List) req.getSession().getServletContext().getAttribute(underlyingStocksListName);	
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_UNDERLYINGSTOCKS, underlyingStocks);
		
		cache.setObject("ResponseObject", investment, req.getSession());
		req.getSession().setAttribute("investmentShowParm",investmentShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting InvestmentShowUpdateFormAction postprocessAction");
				
	}
						
}
