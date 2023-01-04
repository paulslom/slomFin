package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.WDCategoryUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.InvestorDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.WDCategoryDAO;
import com.pas.slomfin.valueObject.Investor;

public class WDCategoryUpdateAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside WDCategoryUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblwdcategory wdCategory = new Tblwdcategory();
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
			WDCategoryUpdateForm wdCategoryForm = (WDCategoryUpdateForm)form;
			if (operation == IAppConstants.UPDATE_ACTION)
			{
				String wdID = req.getParameter("wdCategoryID");
				wdCategory.setIwdcategoryId(new Integer(wdID));				
			}
			wdCategory.setSwdcategoryDescription(wdCategoryForm.getWdCategoryDescription());
			
			InvestorDAO investorDAOReference;
			
			try
			{
				investorDAOReference = (InvestorDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTOR_DAO);
				List<Tblinvestor> investorList = investorDAOReference.inquire(new Integer(investor.getInvestorID()));
				wdCategory.setTblinvestor(investorList.get(0));	
				wdCategory.setiInvestorID(wdCategory.getTblinvestor().getIinvestorId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
							
			cache.setGoToDBInd(req.getSession(), true);
						   									
		}
		
		if (operation == IAppConstants.DELETE_ACTION)
		{
			String wdcID = req.getParameter("wdCategoryID"); //hidden field on jsp
			WDCategoryDAO wdcDAOReference;
			
			try
			{
				wdcDAOReference = (WDCategoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.WDCATEGORY_DAO);
				List<Tblwdcategory> wdcList = wdcDAOReference.inquire(new Integer(wdcID));
				wdCategory = wdcList.get(0);			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
		}
		
		cache.setObject("RequestObject", wdCategory, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside WDCategoryUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("wdCategoryShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting WDCategoryUpdateAction postprocessAction");
				
	}

}
