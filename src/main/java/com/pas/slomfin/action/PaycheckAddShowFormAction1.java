package com.pas.slomfin.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbljob;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class PaycheckAddShowFormAction1 extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckAddShowFormAction1 pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		   	
		String currentJobID = req.getParameter("jobID");
			
		cache.setGoToDBInd(req.getSession(), true);
		cache.setObject("RequestObject", new Integer(currentJobID), req.getSession());		
		
		log.debug("exiting PaycheckAddShowFormAction1 pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckAddShowFormAction1 postprocessAction");
	
			
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbljob> jobList = (List)cache.getObject("ResponseObject",req.getSession());  //should just be one in this list		 
		
		Tbljob job = jobList.get(0);

		log.debug("setting currentJobObject into cache from Response object");

		cache.setObject("currentJobObject",job,req.getSession());		
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting PaycheckAddShowFormAction1 postprocessAction");
				
	}
						
}
