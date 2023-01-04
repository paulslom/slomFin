package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxgroup;
import com.pas.dbobjects.Tbltaxpayment;
import com.pas.dbobjects.Tbltaxpaymenttype;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.TaxPaymentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TaxGroupDAO;
import com.pas.slomfin.dao.TaxPaymentDAO;
import com.pas.slomfin.dao.TaxPaymentTypeDAO;
import com.pas.slomfin.valueObject.Investor;

public class TaxPaymentUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxPaymentUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltaxpayment taxPayment = new Tbltaxpayment();				
		
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
			String taxpaymentID = req.getParameter("taxPaymentID"); //hidden field on jsp
			TaxPaymentDAO taxpaymentDAOReference;
			
			try
			{
				taxpaymentDAOReference = (TaxPaymentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXPAYMENT_DAO);
				List<Tbltaxpayment> txpList = taxpaymentDAOReference.inquire(new Integer(taxpaymentID));
				taxPayment = txpList.get(0);			
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
			TaxPaymentUpdateForm taxPaymentForm = (TaxPaymentUpdateForm)form;
		
			if (operation == IAppConstants.ADD_ACTION)
			{
				Investor investor = cache.getInvestor(req.getSession());
				TaxGroupDAO txgDAOReference;
				
				try
				{
					txgDAOReference = (TaxGroupDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXGROUP_DAO);
					List<Tbltaxgroup> txgList = txgDAOReference.inquire(new Integer(investor.getTaxGroupID()));
					taxPayment.setTbltaxgroup(txgList.get(0));			
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
			}
			
			taxPayment.setItaxYear(taxPaymentForm.getTaxYear());
			taxPayment.setStaxPaymentDesc(taxPaymentForm.getTaxPaymentDesc());
			taxPayment.setMtaxPaymentAmount(new BigDecimal(taxPaymentForm.getTaxPaymentAmount()));
			
			TaxPaymentTypeDAO txptDAOReference;
			
			try
			{
				txptDAOReference = (TaxPaymentTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXPAYMENTTYPE_DAO);
				List<Tbltaxpaymenttype> txptList = txptDAOReference.inquire(new Integer(taxPaymentForm.getTaxPaymentTypeID()));
				taxPayment.setTbltaxpaymenttype(txptList.get(0));			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(taxPaymentForm.getTaxPaymentDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				taxPayment.setDtaxPaymentDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Tax Payment date in TaxPaymentUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Tax Payment date in TaxPaymentUpdateAction pre-process");				
			}
			cache.setGoToDBInd(req.getSession(), true);			
		}
		
		cache.setObject("RequestObject", taxPayment, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxPaymentUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("taxPaymentShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxPaymentUpdateAction postprocessAction");
				
	}

}
