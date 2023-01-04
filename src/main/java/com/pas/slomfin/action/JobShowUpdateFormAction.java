package com.pas.slomfin.action;

import java.util.Calendar;
import java.util.List;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbljob;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.JobUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.valueObject.AppDate;


public class JobShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside JobShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		  
		JobUpdateForm jobForm = (JobUpdateForm) form;		
		jobForm.initialize();
		
		Integer jobInt = new Integer(0);
		
		String jobShowParm = req.getParameter("jobShowParm");
				
		if (jobShowParm.equalsIgnoreCase(IAppConstants.ADD))
		   cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String jobID = req.getParameter("jobID");
		   jobInt = Integer.valueOf(jobID);
		   cache.setGoToDBInd(req.getSession(), true);
		}   
						
		cache.setObject("RequestObject", jobInt, req.getSession());		
		
		log.debug("exiting JobShowUpdateFormAction pre - process");
		
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside JobShowUpdateFormAction postprocessAction");
	
		String jobShowParm = req.getParameter("jobShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbljob> jobList = null;
		
		if (!(jobShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   jobList = (List<Tbljob>)cache.getObject("ResponseObject",req.getSession());		
		
		Tbljob job = new Tbljob();
		
		JobUpdateForm jobForm = (JobUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
				
		if (jobShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			jobForm.initialize();				
		}
		else //not an add	
		{	
		   	job = jobList.get(0);			
			
			jobForm.setJobID(job.getIjobId());			
			jobForm.setEmployer(job.getSemployer());
			jobForm.setPaydaysPerYear(String.valueOf(job.getIpaydaysPerYear()));
			
			AppDate appDate = new AppDate();
			Calendar tempCal = Calendar.getInstance();
			
			if (job.getDjobStartDate() == null)
				jobForm.setJobStartDate(null);
			else
			{	
				tempCal.setTimeInMillis(job.getDjobStartDate().getTime());
				appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
				appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
				appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
				jobForm.setJobStartDate(appDate);
			}
			
			AppDate appDate2 = new AppDate();
			Calendar tempCal2 = Calendar.getInstance();
			
			if (job.getDjobEndDate() == null)
				jobForm.setJobEndDate(null);
			else
			{	
				tempCal2.setTimeInMillis(job.getDjobEndDate().getTime());
				appDate2.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
				appDate2.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
				appDate2.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
				jobForm.setJobEndDate(appDate2);
			}
			
			if (job.getMgrossPay() == null)
			   jobForm.setGrossPay(null);
			else
			   jobForm.setGrossPay(amtFormat.format(job.getMgrossPay()));	
			
			if (job.getMfederalWithholding() == null)
			   jobForm.setFederalWithholding(null);
			else
			   jobForm.setFederalWithholding(amtFormat.format(job.getMfederalWithholding()));	
			
			if (job.getMstateWithholding() == null)
			   jobForm.setStateWithholding(null);
			else
			   jobForm.setStateWithholding(amtFormat.format(job.getMstateWithholding()));	
			
			if (job.getMretirementDeferred() == null)
			   jobForm.setRetirementDeferred(null);
			else
			   jobForm.setRetirementDeferred(amtFormat.format(job.getMretirementDeferred()));	
			
			if (job.getMsswithholding() == null)
			   jobForm.setSsWithholding(null);
			else
			   jobForm.setSsWithholding(amtFormat.format(job.getMsswithholding()));	
			
			if (job.getMmedicareWithholding() == null)
			   jobForm.setMedicareWithholding(null);
			else
			   jobForm.setMedicareWithholding(amtFormat.format(job.getMmedicareWithholding()));	
			
			if (job.getMdental() == null)
			   jobForm.setDental(null);
			else
			   jobForm.setDental(amtFormat.format(job.getMdental()));	
			
			if (job.getMmedical() == null)
			   jobForm.setMedical(null);
			else
			   jobForm.setMedical(amtFormat.format(job.getMmedical()));	
			
			if (job.getMgroupLifeInsurance() == null)
			   jobForm.setGroupLifeInsurance(null);
			else
			   jobForm.setGroupLifeInsurance(amtFormat.format(job.getMgroupLifeInsurance()));	
			
			if (job.getMgroupLifeIncome() == null)
			   jobForm.setGroupLifeIncome(null);
			else
			   jobForm.setGroupLifeIncome(amtFormat.format(job.getMgroupLifeIncome()));	
			
			if (job.getSincomeState() == null)
			   jobForm.setIncomeState(null);
			else
			   jobForm.setIncomeState(job.getSincomeState());
			
			if (job.getMvision() == null)
			   jobForm.setVision(null);
			else
				jobForm.setVision(amtFormat.format(job.getMvision()));	
			
			if (job.getMparking() == null)
			   jobForm.setParking(null);
			else
				jobForm.setParking(amtFormat.format(job.getMparking()));	
			
			if (job.getMcafeteria() == null)
			   jobForm.setCafeteria(null);
			else
				jobForm.setCafeteria(amtFormat.format(job.getMcafeteria()));
			
			if (job.getMroth401k() == null)
			   jobForm.setRoth401k(null);
			else
			   jobForm.setRoth401k(amtFormat.format(job.getMroth401k()));
			
			if (job.getMfsaAmount() == null)
			   jobForm.setFsaAmount(null);
			else
			   jobForm.setFsaAmount(amtFormat.format(job.getMfsaAmount()));
						
		}
		
		cache.setObject("ResponseObject", job, req.getSession());
		req.getSession().setAttribute("jobShowParm",jobShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting JobShowUpdateFormAction postprocessAction");
				
	}
						
}
