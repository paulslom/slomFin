package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.PaycheckHistoryListForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Investor;

public class PaycheckHistoryListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckHistoryListAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		String jobIdentifier = req.getParameter("ijobID");
		Tbljob job = new Tbljob();
		
		if (jobIdentifier==null) //returning from paycheckhistoryupdateform
		{
			Object obj = cache.getObject("ResponseObject", req.getSession());
			if (obj instanceof Tblpaydayhistory)
			{
				Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)obj;
			    job.setIjobId(paycheckHistory.getTbljob().getIjobId());
			}
			else //if it's not that, then we should get the currentJob object out of cache
			{
				obj = cache.getObject("currentJobObject", req.getSession());
				Tbljob tbljob = (Tbljob)obj;
			    job.setIjobId(tbljob.getIjobId());
			}
			
		}
		else	
			job.setIjobId(Integer.valueOf(jobIdentifier));
		
		cache.setGoToDBInd(req.getSession(), true);
		cache.setObject("RequestObject", job, req.getSession());
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckOutflowListAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());
		
		List<Tblpaydayhistory> paycheckHistoryList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		if (paycheckHistoryList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{	
			Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)paycheckHistoryList.get(0);
		
			String jobsListName = ISlomFinAppConstants.DROPDOWN_JOBS_BYINVESTOR + investor.getInvestorID();		
			List jobs = (List) req.getSession().getServletContext().getAttribute(jobsListName);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_JOBS, jobs);		
		
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_PAYCHECKHISTORYLIST, paycheckHistoryList);	
			
			PaycheckHistoryListForm paycheckHistoryListForm = (PaycheckHistoryListForm)form; 
			
			paycheckHistoryListForm.setIjobID(paycheckHistory.getTbljob().getIjobId());
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		}
		
		log.debug("exiting PaycheckOutflowListAction postprocessAction");
				
	}

}
