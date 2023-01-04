package com.pas.slomfin.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblassetclass;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.AssetClassUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class AssetClassShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside AssetClassShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		     
		AssetClassUpdateForm assetClassForm = (AssetClassUpdateForm) form;		
		assetClassForm.initialize();
		
		String assetClassShowParm = req.getParameter("assetClassShowParm");
				
		if (assetClassShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);		
		else
		{
		   String assetClassID = req.getParameter("assetClassID");
		   cache.setGoToDBInd(req.getSession(), true);
		   cache.setObject("RequestObject", new Integer(assetClassID), req.getSession());		
		}   								
		
		log.debug("exiting AssetClassShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside AssetClassShowUpdateFormAction postprocessAction");
	
		String assetClassShowParm = req.getParameter("assetClassShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tblassetclass> assetClassList = null;
		
		if (!(assetClassShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   assetClassList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblassetclass assetClass = new Tblassetclass();
		
		AssetClassUpdateForm assetClassForm = (AssetClassUpdateForm)form; 
						
		if (assetClassShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			assetClassForm.initialize();				
		}
		else //not an add	
		{	
		   	assetClass = assetClassList.get(0);			
			assetClassForm.setassetClassID(assetClass.getIassetClassId());			
			assetClassForm.setassetClassName(assetClass.getSassetClass());						
		}
		
		cache.setObject("ResponseObject", assetClass, req.getSession());
		req.getSession().setAttribute("assetClassShowParm",assetClassShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting AssetClassShowUpdateFormAction postprocessAction");
				
	}
						
}
