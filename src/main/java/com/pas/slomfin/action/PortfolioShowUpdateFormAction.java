package com.pas.slomfin.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblportfolio;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.PortfolioUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class PortfolioShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PortfolioShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		     
		PortfolioUpdateForm portfolioForm = (PortfolioUpdateForm) form;		
		portfolioForm.initialize();
		
		String portfolioShowParm = req.getParameter("portShowParm");
				
		if (portfolioShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);		
		else
		{
		   String portfolioID = req.getParameter("portfolioID");
		   cache.setGoToDBInd(req.getSession(), true);
		   cache.setObject("RequestObject", new Integer(portfolioID), req.getSession());		
		}   								
		
		log.debug("exiting PortfolioShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PortfolioShowUpdateFormAction postprocessAction");
	
		String portfolioShowParm = req.getParameter("portShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tblportfolio> portfolioList = null;
		
		if (!(portfolioShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   portfolioList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblportfolio portfolio = new Tblportfolio();
		
		PortfolioUpdateForm portfolioForm = (PortfolioUpdateForm)form; 
						
		if (portfolioShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			portfolioForm.initialize();				
		}
		else //not an add	
		{	
		   	portfolio = portfolioList.get(0);			
			portfolioForm.setPortfolioID(portfolio.getIportfolioId());			
			portfolioForm.setPortfolioName(portfolio.getSportfolioName());
			portfolioForm.setTaxable(portfolio.isBtaxableInd() );
						
		}
		
		cache.setObject("ResponseObject", portfolio, req.getSession());
		req.getSession().setAttribute("portfolioShowParm",portfolioShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PortfolioShowUpdateFormAction postprocessAction");
				
	}
						
}
