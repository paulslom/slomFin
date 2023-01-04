package com.pas.slomfin.action;

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

public class ReportGoalsNavigateAction extends SlomFinStandardAction
{
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportGoalsNavigateAction postprocessAction");
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportGoalsNavigateFormAction postprocessAction");
				
	}

}
