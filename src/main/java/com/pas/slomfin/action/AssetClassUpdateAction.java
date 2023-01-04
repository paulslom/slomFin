package com.pas.slomfin.action;

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
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;

public class AssetClassUpdateAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside AssetClassUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblassetclass assetClass = new Tblassetclass();
		Investor investor = cache.getInvestor(req.getSession());
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
				
		if (operation == IAppConstants.ADD_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			AssetClassUpdateForm assetClassForm = (AssetClassUpdateForm)form;
		
			if (operation == IAppConstants.UPDATE_ACTION)
			{
				String portID = req.getParameter("assetClassID");
				assetClass.setIassetClassId(new Integer(portID));
			}
			assetClass.setSassetClass(assetClassForm.getassetClassName());			
			
			cache.setGoToDBInd(req.getSession(), true);
						   									
		}
		
		cache.setObject("RequestObject", assetClass, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside AssetClassUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("assetClassShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting AssetClassUpdateAction postprocessAction");
				
	}

}
