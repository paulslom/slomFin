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
import com.pas.dbobjects.Tblmortgage;
import com.pas.dbobjects.Tblmortgagehistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.MortgagePaymentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.MortgageDAO;
import com.pas.slomfin.dao.MortgagePaymentDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;

public class MortgagePaymentUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside MortgagePaymentUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblmortgagehistory mortgagePayment = new Tblmortgagehistory();
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		//if cancelling an add mortgage payment, then show the user all the mortgage payments for the one selected
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
		{
			cache.setGoToDBInd(req.getSession(), false);
		}
		
		if (operation == IAppConstants.DELETE_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			String mortgagePaymentID = req.getParameter("mortgagePaymentID"); //hidden field on jsp
			MortgagePaymentDAO mortPmtDAOReference;
			
			try
			{
				mortPmtDAOReference = (MortgagePaymentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGEPAYMENT_DAO);
				List<Tblmortgagehistory> mhList = mortPmtDAOReference.inquire(new Integer(mortgagePaymentID));
				mortgagePayment = mhList.get(0);			
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
			MortgagePaymentUpdateForm mortgagePaymentForm = (MortgagePaymentUpdateForm)form;
			
			if (operation == IAppConstants.ADD_ACTION)
			{	
				MortgageDAO mortDAOReference;
				
				try
				{
					mortDAOReference = (MortgageDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGE_DAO);
					List<Tblmortgage> mhList = mortDAOReference.inquire(new Integer(mortgagePaymentForm.getMortgageID()));
					mortgagePayment.setTblmortgage(mhList.get(0));			
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(mortgagePaymentForm.getMortgagePaymentDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				mortgagePayment.setDpaymentDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Mortgage Payment Date in MortgagePaymentUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Mortgage Payment date in MortgagePaymentUpdateAction pre-process");				
			}	
							
			mortgagePayment.setMprincipalPaid(new BigDecimal(mortgagePaymentForm.getPrincipalPaid()));	
			mortgagePayment.setMinterestPaid(new BigDecimal(mortgagePaymentForm.getInterestPaid()));
			mortgagePayment.setMpropertyTaxesPaid(new BigDecimal(mortgagePaymentForm.getPropertyTaxesPaid()));	
			mortgagePayment.setMhomeownersInsPaid(new BigDecimal(mortgagePaymentForm.getHomeownersInsurancePaid()));	
			
			if (mortgagePaymentForm.getPmiPaid() == null)
				mortgagePayment.setMpmipaid(new BigDecimal(0));
			else
				if (mortgagePaymentForm.getPmiPaid().length()==0)
					mortgagePayment.setMpmipaid(new BigDecimal(0));
				else
			        mortgagePayment.setMpmipaid(new BigDecimal(mortgagePaymentForm.getPmiPaid()));
			
			cache.setGoToDBInd(req.getSession(), true);
									
		}		
			
		cache.setObject("RequestObject", mortgagePayment, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgagePaymentUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("mortgagePaymentShowParm");
		
		String mtgPmtUpdateOrig = (String)req.getSession().getAttribute("mtgPmtUpdateOrigin");
		
		if (operation == IAppConstants.ADD_ACTION)
			ac.setActionForward(mapping.findForward(ISlomFinAppConstants.AF_SUCCESSAFTERADD));
		else if (mtgPmtUpdateOrig.equalsIgnoreCase(ISlomFinAppConstants.AF_RPTMTGPMT))
			ac.setActionForward(mapping.findForward(ISlomFinAppConstants.AF_RPTMTGPMT));		
		else	
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));	
		
		log.debug("exiting MortgagePaymentUpdateAction postprocessAction");
				
	}

}
