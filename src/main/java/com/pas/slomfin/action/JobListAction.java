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
import com.pas.dbobjects.Tbljob;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Investor;

public class JobListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside JobListAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = new Investor();
		investor = cache.getInvestor(req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		cache.setObject("RequestObject", investor, req.getSession());
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside JobListAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tbljob> jobList = (List<Tbljob>)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		if (jobList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
					
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_JOBLIST, jobList);	
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting JobListAction postprocessAction");
				
	}

}
