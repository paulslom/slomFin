package com.pas.slomfin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ReportCapitalGainsSelectionAction extends DispatchAction
{	
	protected static Logger log = LogManager.getLogger(ReportCapitalGainsSelectionAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		log.debug("inside ReportCapitalGainsSelectionAction");		
		return mapping.findForward("success");
	}
	

}
