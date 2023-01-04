package com.pas.slomfin.action;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblpayday;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.PaycheckOutflowUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.DropDownBean;

public class PaycheckOutflowShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckOutflowShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
	   
		PaycheckOutflowUpdateForm paycheckOutflowForm = (PaycheckOutflowUpdateForm) form;		
		paycheckOutflowForm.initialize();
		
		String paycheckOutflowShowParm = req.getParameter("paycheckOutflowShowParm");
				
		if (paycheckOutflowShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String paycheckOutflowID = req.getParameter("paycheckOutflowID");
		   cache.setObject("RequestObject", new Integer(paycheckOutflowID), req.getSession());	
		   cache.setGoToDBInd(req.getSession(), true);			
		}   
				
		log.debug("exiting PaycheckOutflowShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckOutflowShowUpdateFormAction postprocessAction");
	
		String paycheckOutflowShowParm = req.getParameter("paycheckOutflowShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());
		
		List<Tblpayday> paycheckOutflowList = null;
		
		if (!(paycheckOutflowShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   paycheckOutflowList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		PaycheckOutflowUpdateForm paycheckOutflowForm = (PaycheckOutflowUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
				
		if (paycheckOutflowShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			paycheckOutflowForm.initialize();				
		}
		else //not an add	
		{	
			Tblpayday paycheckOutflow = paycheckOutflowList.get(0);			
			
			paycheckOutflowForm.setPaycheckOutflowID(paycheckOutflow.getIpaydayId());
			
			paycheckOutflowForm.setDescription(paycheckOutflow.getSdescription());
			paycheckOutflowForm.setTransactionTypeID(paycheckOutflow.getTbltransactiontype().getItransactionTypeId().toString());
			paycheckOutflowForm.setTransactionTypeDesc(paycheckOutflow.getTbltransactiontype().getSdescription());
			paycheckOutflowForm.setAccountID(paycheckOutflow.getTblaccountByIAccountId().getIaccountId().toString());
			paycheckOutflowForm.setAccountName(paycheckOutflow.getTblaccountByIAccountId().getSaccountName());
			
			if (paycheckOutflow.getTblaccountByIXferAccountId() != null)
			{	
				paycheckOutflowForm.setXferAccountID(paycheckOutflow.getTblaccountByIXferAccountId().getIaccountId().toString());
				paycheckOutflowForm.setXferAccountName(paycheckOutflow.getTblaccountByIXferAccountId().getSaccountName());
			}
			
			if (paycheckOutflow.getTblwdcategory() != null)
			{	
				paycheckOutflowForm.setWdCategoryID(paycheckOutflow.getTblwdcategory().getIwdcategoryId().toString());
				paycheckOutflowForm.setWdCategoryDesc(paycheckOutflow.getTblwdcategory().getSwdcategoryDescription());
			}
			
			if (paycheckOutflow.getTblcashdeposittype() != null)
			{	
				paycheckOutflowForm.setCashDepositTypeID(paycheckOutflow.getTblcashdeposittype().getIcashDepositTypeId().toString());
				paycheckOutflowForm.setCashDepositTypeDesc(paycheckOutflow.getTblcashdeposittype().getScashDepositTypeDesc());
			}
			
			if (paycheckOutflow.getIdefaultDay() != null)
			    paycheckOutflowForm.setDefaultDay(paycheckOutflow.getIdefaultDay().toString());
		
			if (paycheckOutflow.getMdefaultAmt() == null)
			   paycheckOutflowForm.setDefaultAmount(null);
			else
			   paycheckOutflowForm.setDefaultAmount(amtFormat.format(paycheckOutflow.getMdefaultAmt()));	
			
			if (paycheckOutflow.getBnextMonthInd() == 1)
				paycheckOutflowForm.setNextMonthInd(true);
			else
				paycheckOutflowForm.setNextMonthInd(false);
			
			cache.setObject("ResponseObject", paycheckOutflow, req.getSession());
									
		}
		
		if (paycheckOutflowShowParm.equalsIgnoreCase(IAppConstants.ADD)
		||	paycheckOutflowShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
		{	
			String trxTypesListName = ISlomFinAppConstants.DROPDOWN_TRANSACTIONTYPES;
			List trxTypes = (List) req.getSession().getServletContext().getAttribute(trxTypesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_TRANSACTIONTYPES, trxTypes);	
			
			String wdCatListName = ISlomFinAppConstants.DROPDOWN_WDCATEGORIES_BYINVESTOR + investor.getInvestorID();
			List wdCats = (List) req.getSession().getServletContext().getAttribute(wdCatListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_WDCATEGORIES, wdCats);		
			
			String cashdeptypesListName = ISlomFinAppConstants.DROPDOWN_CASHDEPOSITTYPES;
			List cashDepTypes = (List) req.getSession().getServletContext().getAttribute(cashdeptypesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_CASHDEPOSITTYPES, cashDepTypes);	
			
			List accts = null;
			List<DropDownBean> ddAccts = new ArrayList<DropDownBean>();
			
			try
			{
			   accts = getOpenAccountsByInvestor(investor);			   
			   for (int i=0; i<accts.size(); i++)
			   {
					DropDownBean ddBean = new DropDownBean();
		            ddBean.setId(((Tblaccount)accts.get(i)).getIaccountId().toString());
		            ddBean.setDescription(((Tblaccount)accts.get(i)).getSaccountName());
		            ddAccts.add(ddBean);
			   }
			   req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_ACCOUNTS, ddAccts);	
			   req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_XFERACCOUNTS, ddAccts);	
			}
			catch (SystemException e)
			{
			   log.error("PaycheckOutflowShowUpdateFormAction SystemException" + e.getMessage());
			   e.printStackTrace();
			   throw new PASSystemException(e);
			}
									
		}		
			
		req.getSession().setAttribute("paycheckOutflowShowParm",paycheckOutflowShowParm);
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting PaycheckOutflowShowUpdateFormAction postprocessAction");
				
	}
	private List<Tblaccount> getOpenAccountsByInvestor(Investor investor)
	   throws PASSystemException, DAOException, SystemException
	{		
		
		AccountDAO daoReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			
		log.debug("retrieving a list of Open Accounts by investor" + investor.getInvestorID());
			
		return daoReference.inquire(investor);
	}
					
}
