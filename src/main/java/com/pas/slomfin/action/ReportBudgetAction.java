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
import com.pas.slomfin.actionform.ReportBudgetForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Budget;
import com.pas.slomfin.valueObject.BudgetSelection;
import com.pas.slomfin.valueObject.Investor;

public class ReportBudgetAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportBudgetAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		BudgetSelection budgetSelection = new BudgetSelection();
		Investor investor = cache.getInvestor(req.getSession());
		budgetSelection.setBudgetInvestorID(new Integer(investor.getInvestorID()));
		budgetSelection.setBudgetYear(Integer.parseInt(req.getParameter("Year")));
		cache.setObject("RequestObject", budgetSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportBudgetAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Budget> budgetList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		ReportBudgetForm repForm = (ReportBudgetForm)form;
		
		if (budgetList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{					
			repForm.setReportTitle("Budget for Year " + req.getParameter("Year"));
		}
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTBUDGETLIST, budgetList);	
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportBudgetAction postprocessAction");
				
	}

}
