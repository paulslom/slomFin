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
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblpaychecktype;
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.PaycheckAddForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.JobDAO;
import com.pas.slomfin.dao.PaycheckTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;

public class PaycheckAddAction1 extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckAddAction1 pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblpaydayhistory paycheckHistory = new Tblpaydayhistory();
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an add
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.ADD_ACTION)
		{
			PaycheckAddForm paycheckAddForm = (PaycheckAddForm)form;
			
			JobDAO jobDAOReference;
			PaycheckTypeDAO paycheckTypeDAOReference;
			
			try
			{
				jobDAOReference = (JobDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.JOB_DAO);
				paycheckTypeDAOReference = (PaycheckTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PAYCHECKTYPE_DAO);
				List<Tbljob> jobList = jobDAOReference.inquire(new Integer(paycheckAddForm.getJobID()));
				paycheckHistory.setTbljob(jobList.get(0));	
				paycheckHistory.setIjobId(paycheckHistory.getTbljob().getIjobId());
				List<Tblpaychecktype> pctList = paycheckTypeDAOReference.inquire(new Integer(paycheckAddForm.getPaycheckTypeID()));
				paycheckHistory.setTblpaychecktype(pctList.get(0));	
				paycheckHistory.setIpaychecktypeid(paycheckHistory.getTblpaychecktype().getIpaycheckTypeId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
				
			paycheckHistory.setMfederalWithholding(new BigDecimal(paycheckAddForm.getFederalWithholding()));
			paycheckHistory.setMgrossPay(new BigDecimal(paycheckAddForm.getGrossPay()));
			paycheckHistory.setMmedicareWithholding(new BigDecimal(paycheckAddForm.getMedicareWithholding()));
			paycheckHistory.setMsswithholding(new BigDecimal(paycheckAddForm.getSsWithholding()));

			if (paycheckAddForm.getGroupLifeIncome() == null)
			    paycheckHistory.setMgroupLifeIncome(null);
			else
			    paycheckHistory.setMgroupLifeIncome(new BigDecimal(paycheckAddForm.getGroupLifeIncome()));
			
			if (paycheckAddForm.getGroupLifeInsurance() == null)
			    paycheckHistory.setMgroupLifeInsurance(null);
			else
			    paycheckHistory.setMgroupLifeInsurance(new BigDecimal(paycheckAddForm.getGroupLifeInsurance()));
			
			if (paycheckAddForm.getMedical() == null)
			    paycheckHistory.setMmedical(null);
			else
			    paycheckHistory.setMmedical(new BigDecimal(paycheckAddForm.getMedical()));
			
			if (paycheckAddForm.getDental() == null)
			    paycheckHistory.setMdental(null);
			else
			    paycheckHistory.setMdental(new BigDecimal(paycheckAddForm.getDental()));
			
			if (paycheckAddForm.getRetirementDeferred() == null)
			    paycheckHistory.setMretirementDeferred(null);
			else
			    paycheckHistory.setMretirementDeferred(new BigDecimal(paycheckAddForm.getRetirementDeferred()));
			
			if (paycheckAddForm.getStateWithholding() == null)
			    paycheckHistory.setMstateWithholding(null);
			else
			    paycheckHistory.setMstateWithholding(new BigDecimal(paycheckAddForm.getStateWithholding()));
			
			if (paycheckAddForm.getVision() == null)
			    paycheckHistory.setMvision(null);
			else
			    paycheckHistory.setMvision(new BigDecimal(paycheckAddForm.getVision()));
			
			if (paycheckAddForm.getParking() == null)
			    paycheckHistory.setMparking(null);
			else
			    paycheckHistory.setMparking(new BigDecimal(paycheckAddForm.getParking()));
			
			if (paycheckAddForm.getCafeteria() == null)
			    paycheckHistory.setMcafeteria(null);
			else
			    paycheckHistory.setMcafeteria(new BigDecimal(paycheckAddForm.getCafeteria()));
			
			if (paycheckAddForm.getRoth401k() == null)
			    paycheckHistory.setMroth401k(null);
			else
			    paycheckHistory.setMroth401k(new BigDecimal(paycheckAddForm.getRoth401k()));
			
			if (paycheckAddForm.getFsaAmount() == null)
			    paycheckHistory.setMfsaAmount(null);
			else
			    paycheckHistory.setMfsaAmount(new BigDecimal(paycheckAddForm.getFsaAmount()));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(paycheckAddForm.getPaycheckHistoryDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				paycheckHistory.setDpaydayHistoryDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing paycheck history date in PaycheckAddAction1 pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Paycheck History date in PaycheckAddAction1 pre-process");				
			}
			
			cache.setGoToDBInd(req.getSession(), true);
															
		}
		
		cache.setObject("RequestObject", paycheckHistory, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckAddAction1 postprocessAction");
		
		String reqParm = req.getParameter("operation");
		
		//when canceling an add go back to list, not paycheckaddaction2
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
			ac.setActionForward(mapping.findForward(IAppConstants.AF_CANCEL));		
		else
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PaycheckAddAction1 postprocessAction");
				
	}

}
