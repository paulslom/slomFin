package com.pas.slomfin.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Menu;

public class Menu2Action extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside Menu2Action pre - process");
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		cache.setGoToDBInd(req.getSession(), true);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside Menu2Action postprocessAction");
	
		//get Value object from the request - in this case it is a list of menu components
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		List<Map> menuList = (List)cache.getObject("ResponseObject", req.getSession());		
		
		Map<String, List<Menu>> menuMap = menuList.get(0); //should only be one entry here
  		  		
		req.getSession().setAttribute(ISlomFinAppConstants.REPORTS_MENU_LIST, menuMap.get("Reports")); 
		req.getSession().setAttribute(ISlomFinAppConstants.WORK_MENU_LIST, menuMap.get("Work")); 
		req.getSession().setAttribute(ISlomFinAppConstants.MISC_MENU_LIST, menuMap.get("Misc")); 
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		log.debug("exiting MenuAction postprocessAction");		
		
	}
}
