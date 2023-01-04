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
import com.pas.dbobjects.Tblpaychecktype;
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.PaycheckHistoryUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.PaycheckHistoryDAO;
import com.pas.slomfin.dao.PaycheckTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;

public class PaycheckHistoryUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckHistoryUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblpaydayhistory paycheckHistory = new Tblpaydayhistory();
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when canceling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.DELETE_ACTION
		||	operation == IAppConstants.UPDATE_ACTION)
		{
			String pchID = req.getParameter("paycheckHistoryID"); //hidden field on jsp
			PaycheckHistoryDAO pchDAOReference;
			
			try
			{
				pchDAOReference = (PaycheckHistoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PAYCHECKHISTORY_DAO);
				List<Tblpaydayhistory> pchList = pchDAOReference.inquire(new Integer(pchID));
				paycheckHistory = pchList.get(0);
				paycheckHistory.setIpaydayHistoryId(paycheckHistory.getIpaydayHistoryId());
				paycheckHistory.setIjobId(paycheckHistory.getIjobId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
		}
		
		if (operation == IAppConstants.UPDATE_ACTION)
		{
			PaycheckHistoryUpdateForm paycheckHistoryForm = (PaycheckHistoryUpdateForm)form;
			
			PaycheckTypeDAO paycheckTypeDAOReference;
			
			try
			{
				paycheckTypeDAOReference = (PaycheckTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PAYCHECKTYPE_DAO);
				List<Tblpaychecktype> pctList = paycheckTypeDAOReference.inquire(new Integer(paycheckHistoryForm.getPaycheckTypeID()));
				paycheckHistory.setTblpaychecktype(pctList.get(0));
				paycheckHistory.setIpaychecktypeid(paycheckHistory.getIpaychecktypeid());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			paycheckHistory.setMfederalWithholding(new BigDecimal(paycheckHistoryForm.getFederalWithholding()));
			paycheckHistory.setMgrossPay(new BigDecimal(paycheckHistoryForm.getGrossPay()));
			paycheckHistory.setMmedicareWithholding(new BigDecimal(paycheckHistoryForm.getMedicareWithholding()));
			paycheckHistory.setMsswithholding(new BigDecimal(paycheckHistoryForm.getSsWithholding()));

			if (paycheckHistoryForm.getGroupLifeIncome() == null)
			    paycheckHistory.setMgroupLifeIncome(null);
			else
			    paycheckHistory.setMgroupLifeIncome(new BigDecimal(paycheckHistoryForm.getGroupLifeIncome()));
			
			if (paycheckHistoryForm.getGroupLifeInsurance() == null)
			    paycheckHistory.setMgroupLifeInsurance(null);
			else
			    paycheckHistory.setMgroupLifeInsurance(new BigDecimal(paycheckHistoryForm.getGroupLifeInsurance()));
			
			if (paycheckHistoryForm.getMedical() == null)
			    paycheckHistory.setMmedical(null);
			else
			    paycheckHistory.setMmedical(new BigDecimal(paycheckHistoryForm.getMedical()));
			
			if (paycheckHistoryForm.getDental() == null)
			    paycheckHistory.setMdental(null);
			else
			    paycheckHistory.setMdental(new BigDecimal(paycheckHistoryForm.getDental()));
			
			if (paycheckHistoryForm.getRetirementDeferred() == null)
			    paycheckHistory.setMretirementDeferred(null);
			else
			    paycheckHistory.setMretirementDeferred(new BigDecimal(paycheckHistoryForm.getRetirementDeferred()));
			
			if (paycheckHistoryForm.getStateWithholding() == null)
			    paycheckHistory.setMstateWithholding(null);
			else
			    paycheckHistory.setMstateWithholding(new BigDecimal(paycheckHistoryForm.getStateWithholding()));
			
			if (paycheckHistoryForm.getVision() == null)
			    paycheckHistory.setMvision(null);
			else
			    paycheckHistory.setMvision(new BigDecimal(paycheckHistoryForm.getVision()));
			
			if (paycheckHistoryForm.getParking() == null)
			    paycheckHistory.setMparking(null);
			else
			    paycheckHistory.setMparking(new BigDecimal(paycheckHistoryForm.getParking()));
			
			if (paycheckHistoryForm.getCafeteria() == null)
			    paycheckHistory.setMcafeteria(null);
			else
			    paycheckHistory.setMcafeteria(new BigDecimal(paycheckHistoryForm.getCafeteria()));
			
			if (paycheckHistoryForm.getRoth401k() == null)
			    paycheckHistory.setMroth401k(null);
			else
			    paycheckHistory.setMroth401k(new BigDecimal(paycheckHistoryForm.getRoth401k()));
			
			if (paycheckHistoryForm.getFsaAmount() == null)
			    paycheckHistory.setMfsaAmount(null);
			else
			    paycheckHistory.setMfsaAmount(new BigDecimal(paycheckHistoryForm.getFsaAmount()));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(paycheckHistoryForm.getPaycheckHistoryDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				paycheckHistory.setDpaydayHistoryDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing paycheck history date in PaycheckHistoryUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Paycheck History date in PaycheckHistoryUpdateAction pre-process");				
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
		log.debug("inside PaycheckHistoryUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("paycheckHistoryShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PaycheckHistoryUpdateAction postprocessAction");
				
	}

}
