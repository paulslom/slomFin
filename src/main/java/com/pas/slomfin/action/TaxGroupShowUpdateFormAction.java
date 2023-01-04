package com.pas.slomfin.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxgroup;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TaxGroupUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class TaxGroupShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxGroupShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		   
		TaxGroupUpdateForm taxGroupForm = (TaxGroupUpdateForm) form;		
		taxGroupForm.initialize();
		
		String taxGroupShowParm = req.getParameter("taxGroupShowParm");
				
		if (taxGroupShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);		
		else
		{
		   String taxGroupID = req.getParameter("taxGroupID");
		   cache.setGoToDBInd(req.getSession(), true);
		   cache.setObject("RequestObject", new Integer(taxGroupID), req.getSession());		
		}   
						
		
		
		log.debug("exiting TaxGroupShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxGroupShowUpdateFormAction postprocessAction");
	
		String taxGroupShowParm = req.getParameter("taxGroupShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbltaxgroup> taxGroupList = null;
		
		if (!(taxGroupShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   taxGroupList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tbltaxgroup taxGroup = new Tbltaxgroup();
		
		TaxGroupUpdateForm taxGroupForm = (TaxGroupUpdateForm)form; 
						
		if (taxGroupShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			taxGroupForm.initialize();				
		}
		else //not an add	
		{	
		   	taxGroup = taxGroupList.get(0);			
			
		   	taxGroupForm.setTaxGroupID(taxGroup.getItaxGroupId());		
			taxGroupForm.setTaxGroupName(taxGroup.getStaxGroupName());
						
		}
		
		cache.setObject("ResponseObject", taxGroup, req.getSession());
		req.getSession().setAttribute("taxGroupShowParm",taxGroupShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxGroupShowUpdateFormAction postprocessAction");
				
	}
						
}
