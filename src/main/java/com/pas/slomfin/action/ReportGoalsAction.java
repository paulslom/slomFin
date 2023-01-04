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
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.ReportGoalsForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Goals;
import com.pas.slomfin.valueObject.GoalsSelection;
import com.pas.slomfin.valueObject.Investor;

public class ReportGoalsAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportGoalsAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		GoalsSelection goalsSelection = new GoalsSelection();
		Investor investor = cache.getInvestor(req.getSession());
		goalsSelection.setGoalsInvestorID(new Integer(investor.getInvestorID()));
		goalsSelection.setGoalsProjectionYears(Integer.parseInt(req.getParameter("projectionYears")));
		goalsSelection.setGoalsRateOfReturn(Integer.parseInt(req.getParameter("rateOfReturn")));
		cache.setObject("RequestObject", goalsSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportGoalsAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Goals> goalsList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		ReportGoalsForm repForm = (ReportGoalsForm)form;
		
		if (goalsList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{					
			repForm.setReportTitle("Goals Report - Rate of Return = " + req.getParameter("rateOfReturn") + "% and Projection years = " + req.getParameter("projectionYears"));
		}
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTGOALSLIST, goalsList);	
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportGoalsAction postprocessAction");
				
	}

}
