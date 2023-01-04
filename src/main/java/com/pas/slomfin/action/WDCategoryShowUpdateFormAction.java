package com.pas.slomfin.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.WDCategoryUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class WDCategoryShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside WDCategoryShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		WDCategoryUpdateForm wdCategoryForm = (WDCategoryUpdateForm) form;		
		wdCategoryForm.initialize();
		
		String wdCategoryShowParm = req.getParameter("wdCategoryShowParm");
				
		if (wdCategoryShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String wdCategoryID = req.getParameter("wdCategoryID");
		   cache.setObject("RequestObject", new Integer(wdCategoryID), req.getSession());		
		   cache.setGoToDBInd(req.getSession(), true);
		}
		
		log.debug("exiting WDCategoryShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside WDCategoryShowUpdateFormAction postprocessAction");
	
		String wdCategoryShowParm = req.getParameter("wdCategoryShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tblwdcategory> wdCategoryList = null;
		
		if (!(wdCategoryShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   wdCategoryList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblwdcategory wdCategory = new Tblwdcategory();
		
		WDCategoryUpdateForm wdCategoryForm = (WDCategoryUpdateForm)form; 
		
		if (wdCategoryShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			wdCategoryForm.initialize();
		}
		else //not an add	
		{	
		   	wdCategory = wdCategoryList.get(0);			
			
			wdCategoryForm.setWdCategoryID(wdCategory.getIwdcategoryId());		
			wdCategoryForm.setWdCategoryDescription(wdCategory.getSwdcategoryDescription());
		}	
		
		cache.setObject("ResponseObject", wdCategory, req.getSession());
		req.getSession().setAttribute("wdCategoryShowParm",wdCategoryShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting WDCategoryShowUpdateFormAction postprocessAction");
				
	}
						
}
