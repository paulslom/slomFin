package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblassetclass;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tblinvestmenttype;
import com.pas.dbobjects.Tblinvestor;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.InvestmentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.AssetClassDAO;
import com.pas.slomfin.dao.InvestmentTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.DropDownBean;

public class InvestmentChoiceShowFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestmentChoiceAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
				
		cache.setGoToDBInd(req.getSession(), false);
				
		return true;
	}	
	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestmentChoiceAction postprocessAction");		
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());
		
		List<DropDownBean> investorAccounts = null; 
		try
		{
		   investorAccounts = getInvestorAccounts(Integer.parseInt(investor.getInvestorID()));
		   req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_INVESTMENTCHOICEACCOUNTS, investorAccounts);	
		}
		catch (SystemException e)
		{
		   log.error("TrxShowUpdateFormAction SystemException" + e.getMessage());
		   e.printStackTrace();
		   throw new PASSystemException(e);
		}
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting InvestmentChoiceAction postprocessAction");
				
	}
	@SuppressWarnings("unchecked")
	private List<DropDownBean> getInvestorAccounts(Integer investorID)
	   throws PASSystemException, DAOException, SystemException
	{			
		AccountDAO daoReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			
		log.debug("retrieving the possible transfer accounts for investor " + investorID);
			
		return daoReference.getAllAccountsByInvestor(investorID);
	}

}
