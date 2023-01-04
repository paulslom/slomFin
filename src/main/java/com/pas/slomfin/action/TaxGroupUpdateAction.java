package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxgroup;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.TaxGroupUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TaxGroupDAO;

public class TaxGroupUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxGroupUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
	
		Tbltaxgroup taxGroup = new Tbltaxgroup();
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.DELETE_ACTION
		||	operation == IAppConstants.UPDATE_ACTION)
		{
			String taxGroupID = req.getParameter("taxGroupID"); //hidden field on jsp
			TaxGroupDAO taxGroupDAOReference;
			
			try
			{
				taxGroupDAOReference = (TaxGroupDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXGROUP_DAO);
				List<Tbltaxgroup> txgList = taxGroupDAOReference.inquire(new Integer(taxGroupID));
				taxGroup = txgList.get(0);			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
		}
		
		if (operation == IAppConstants.ADD_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			TaxGroupUpdateForm taxGroupForm = (TaxGroupUpdateForm)form;		
			taxGroup.setStaxGroupName(taxGroupForm.getTaxGroupName());			
			cache.setGoToDBInd(req.getSession(), true);					   									
		}
		
		cache.setObject("RequestObject", taxGroup, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxGroupUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("taxGroupShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxGroupUpdateAction postprocessAction");
				
	}

}
