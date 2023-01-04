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
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.InvestmentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AssetClassDAO;
import com.pas.slomfin.dao.InvestmentTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;

public class InvestmentUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestmentUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblinvestment investment = (Tblinvestment)cache.getObject("ResponseObject",req.getSession()); //this had been set in investmentShowFormAction	
		
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
			InvestmentUpdateForm investmentForm = (InvestmentUpdateForm)form;
					
			InvestmentTypeDAO investmentTypeDAOReference;
			
			try
			{
				investmentTypeDAOReference = (InvestmentTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENTTYPE_DAO);
				List<Tblinvestmenttype> investmentTypeList = investmentTypeDAOReference.inquire(new Integer(investmentForm.getInvestmentTypeID()));
				investment.setTblinvestmenttype(investmentTypeList.get(0));	
				investment.setIinvestmentTypeId(investment.getTblinvestmenttype().getIinvestmentTypeId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			if (investmentForm.getTickerSymbol() == null)
			   investment.setStickerSymbol(null);
			else
			   investment.setStickerSymbol(investmentForm.getTickerSymbol());
			
			investment.setSdescription(investmentForm.getDescription());
			
			if (investmentForm.getOptionMultiplier() == null)
			   investment.setDoptionMultiplier(null);
			else
			   if (investmentForm.getOptionMultiplier().length()==0)
				   investment.setDoptionMultiplier(null);
			   else
				   investment.setDoptionMultiplier(new BigDecimal(investmentForm.getOptionMultiplier()));	
			
			if (investmentForm.getCurrentPrice() == null)
			   investment.setMcurrentPrice(null);
			else
			   if (investmentForm.getCurrentPrice().length()==0)
				   investment.setMcurrentPrice(null);
			   else	
				   investment.setMcurrentPrice(new BigDecimal(investmentForm.getCurrentPrice()));	
						
			if (investmentForm.getDividendsPerYear() == null)
			   investment.setIdividendsPerYear(null);
			else
				if (investmentForm.getDividendsPerYear().length()==0)
				   investment.setIdividendsPerYear(null);
				else		
				   investment.setIdividendsPerYear(new Integer(investmentForm.getDividendsPerYear()));	
			
			AssetClassDAO acDAOReference;
			
			try
			{
				acDAOReference = (AssetClassDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ASSETCLASS_DAO);
				List<Tblassetclass> assetClassList = acDAOReference.inquire(new Integer(investmentForm.getAssetClassID()));
				investment.setTblassetclass(assetClassList.get(0));			
				investment.setIassetClassId(investment.getTblassetclass().getIassetClassId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			cache.setGoToDBInd(req.getSession(), true);
												
		}
		
		cache.setObject("RequestObject", investment, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestmentUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("investmentShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting InvestmentUpdateAction postprocessAction");
				
	}

}
