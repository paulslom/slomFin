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
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tbljob;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.JobUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.InvestorDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;

public class JobUpdateAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside JobUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbljob job = new Tbljob();
		Investor investor = cache.getInvestor(req.getSession());
		
		job = (Tbljob)cache.getObject("ResponseObject",req.getSession()); //this had been set in jobShowFormAction	
		
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
			JobUpdateForm jobForm = (JobUpdateForm)form;
		
			job.setSemployer(jobForm.getEmployer());
			job.setIpaydaysPerYear(new Integer(jobForm.getPaydaysPerYear()));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(jobForm.getJobStartDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				job.setDjobStartDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Job Start Date in JobUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Job Start date in JobUpdateAction pre-process");				
			}	
			
			if (jobForm.getJobEndDate().getAppday() != null)
				try
				{
					date = sdf.parse(jobForm.getJobEndDate().toStringYYYYMMDD());
					java.sql.Timestamp timestamp2 = new java.sql.Timestamp(date.getTime());
					job.setDjobEndDate(timestamp2);
				}
				catch (ParseException e)
				{	
					log.error("error parsing Job End date in JobUpdateAction pre-process");
					e.printStackTrace();
					throw new PresentationException("error parsing Job End date in JobUpdateAction pre-process");				
				}				
				
			InvestorDAO investorDAOReference;
			
			try
			{
				investorDAOReference = (InvestorDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTOR_DAO);
				List<Tblinvestor> investorList = investorDAOReference.inquire(new Integer(investor.getInvestorID()));
				job.setTblinvestor(investorList.get(0));			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			if (jobForm.getGrossPay() == null)
			   job.setMgrossPay(null);
			else
			   job.setMgrossPay(new BigDecimal(jobForm.getGrossPay()));	
			
			if (jobForm.getFederalWithholding() == null)
			   job.setMfederalWithholding(null);
			else
			   job.setMfederalWithholding(new BigDecimal(jobForm.getFederalWithholding()));	
			
			if (jobForm.getStateWithholding() == null)
			   job.setMstateWithholding(null);
			else
			   job.setMstateWithholding(new BigDecimal(jobForm.getStateWithholding()));	
			
			if (jobForm.getRetirementDeferred() == null)
			   job.setMretirementDeferred(null);
			else
			   job.setMretirementDeferred(new BigDecimal(jobForm.getRetirementDeferred()));	
			
			if (jobForm.getSsWithholding() == null)
			   job.setMsswithholding(null);
			else
			   job.setMsswithholding(new BigDecimal(jobForm.getSsWithholding()));	
			
			if (jobForm.getMedicareWithholding() == null)
			   job.setMmedicareWithholding(null);
			else
			   job.setMmedicareWithholding(new BigDecimal(jobForm.getMedicareWithholding()));	
			
			if (jobForm.getDental() == null)
			   job.setMdental(null);
			else
			   job.setMdental(new BigDecimal(jobForm.getDental()));	
			
			if (jobForm.getMedical() == null)
			   job.setMmedical(null);
			else
			   job.setMmedical(new BigDecimal(jobForm.getMedical()));	
			
			if (jobForm.getGroupLifeInsurance() == null)
			   job.setMgroupLifeInsurance(null);
			else
			   job.setMgroupLifeInsurance(new BigDecimal(jobForm.getGroupLifeInsurance()));	
			
			if (jobForm.getGroupLifeIncome() == null)
			   job.setMgroupLifeIncome(null);
			else
			   job.setMgroupLifeIncome(new BigDecimal(jobForm.getGroupLifeIncome()));	
			
			if (jobForm.getIncomeState() == null)
			   job.setSincomeState(null);
			else
			   job.setSincomeState(jobForm.getIncomeState());
			
			if (jobForm.getVision() == null)
				job.setMvision(null);
			else
				job.setMvision(new BigDecimal(jobForm.getVision()));
			
			if (jobForm.getParking() == null)
				job.setMparking(null);
			else
				job.setMparking(new BigDecimal(jobForm.getParking()));
			
			if (jobForm.getCafeteria() == null)
				job.setMcafeteria(null);
			else
				job.setMcafeteria(new BigDecimal(jobForm.getCafeteria()));
			
			if (jobForm.getRoth401k() == null)
				job.setMroth401k(null);
			else
				job.setMroth401k(new BigDecimal(jobForm.getRoth401k()));
			
			if (jobForm.getFsaAmount() == null)
				job.setMfsaAmount(null);
			else
				job.setMfsaAmount(new BigDecimal(jobForm.getFsaAmount()));
			
			cache.setGoToDBInd(req.getSession(), true);
									
		}
		
		cache.setObject("RequestObject", job, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside JobUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("jobShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting JobUpdateAction postprocessAction");
				
	}

}
