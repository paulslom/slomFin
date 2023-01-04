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
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.PaycheckHistoryUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.valueObject.AppDate;


public class PaycheckHistoryShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckHistoryShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		  
		PaycheckHistoryUpdateForm paycheckHistoryForm = (PaycheckHistoryUpdateForm) form;		
		paycheckHistoryForm.initialize();
		
		String paycheckHistoryID = req.getParameter("paycheckHistoryID");
									
		cache.setObject("RequestObject", new Integer(paycheckHistoryID), req.getSession());		
		cache.setGoToDBInd(req.getSession(), true);
		
		log.debug("exiting PaycheckHistoryShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckHistoryShowUpdateFormAction postprocessAction");
	
		String paycheckHistoryShowParm = req.getParameter("paycheckHistoryShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tblpaydayhistory> paycheckHistoryList = (List<Tblpaydayhistory>)cache.getObject("ResponseObject",req.getSession());		
					
		PaycheckHistoryUpdateForm paycheckHistoryForm = (PaycheckHistoryUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		
		Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)paycheckHistoryList.get(0);			
			
		paycheckHistoryForm.setPaycheckHistoryID(paycheckHistory.getIpaydayHistoryId());			
		paycheckHistoryForm.setEmployer(paycheckHistory.getTbljob().getSemployer());
		paycheckHistoryForm.setPaycheckTypeID(paycheckHistory.getTblpaychecktype().getIpaycheckTypeId());
			
		AppDate appDate = new AppDate();
		Calendar tempCal = Calendar.getInstance();
			
		if (paycheckHistory.getDpaydayHistoryDate() == null)
			paycheckHistoryForm.setPaycheckHistoryDate(null);
		else
		{	
			tempCal.setTimeInMillis(paycheckHistory.getDpaydayHistoryDate().getTime());
			appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
			appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
			appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
			paycheckHistoryForm.setPaycheckHistoryDate(appDate);
		}
				
		if (paycheckHistory.getMgrossPay() == null)
		   paycheckHistoryForm.setGrossPay(null);
		else
		   paycheckHistoryForm.setGrossPay(amtFormat.format(paycheckHistory.getMgrossPay()));	
		
		if (paycheckHistory.getMfederalWithholding() == null)
		   paycheckHistoryForm.setFederalWithholding(null);
		else
		   paycheckHistoryForm.setFederalWithholding(amtFormat.format(paycheckHistory.getMfederalWithholding()));	
		
		if (paycheckHistory.getMstateWithholding() == null)
		   paycheckHistoryForm.setStateWithholding(null);
		else
		   paycheckHistoryForm.setStateWithholding(amtFormat.format(paycheckHistory.getMstateWithholding()));	
		
		if (paycheckHistory.getMretirementDeferred() == null)
		   paycheckHistoryForm.setRetirementDeferred(null);
		else
		   paycheckHistoryForm.setRetirementDeferred(amtFormat.format(paycheckHistory.getMretirementDeferred()));	
		
		if (paycheckHistory.getMsswithholding() == null)
		   paycheckHistoryForm.setSsWithholding(null);
		else
		   paycheckHistoryForm.setSsWithholding(amtFormat.format(paycheckHistory.getMsswithholding()));	
		
		if (paycheckHistory.getMmedicareWithholding() == null)
		   paycheckHistoryForm.setMedicareWithholding(null);
		else
		   paycheckHistoryForm.setMedicareWithholding(amtFormat.format(paycheckHistory.getMmedicareWithholding()));	
		
		if (paycheckHistory.getMdental() == null)
		   paycheckHistoryForm.setDental(null);
		else
		   paycheckHistoryForm.setDental(amtFormat.format(paycheckHistory.getMdental()));	
		
		if (paycheckHistory.getMmedical() == null)
		   paycheckHistoryForm.setMedical(null);
		else
		   paycheckHistoryForm.setMedical(amtFormat.format(paycheckHistory.getMmedical()));	
		
		if (paycheckHistory.getMgroupLifeInsurance() == null)
		   paycheckHistoryForm.setGroupLifeInsurance(null);
		else
		   paycheckHistoryForm.setGroupLifeInsurance(amtFormat.format(paycheckHistory.getMgroupLifeInsurance()));	
		
		if (paycheckHistory.getMgroupLifeIncome() == null)
		   paycheckHistoryForm.setGroupLifeIncome(null);
		else
		   paycheckHistoryForm.setGroupLifeIncome(amtFormat.format(paycheckHistory.getMgroupLifeIncome()));	
		
		if (paycheckHistory.getMvision() == null)
		   paycheckHistoryForm.setVision(null);
		else
		   paycheckHistoryForm.setVision(amtFormat.format(paycheckHistory.getMvision()));
		
		if (paycheckHistory.getMparking() == null)
		   paycheckHistoryForm.setParking(null);
		else
		   paycheckHistoryForm.setParking(amtFormat.format(paycheckHistory.getMparking()));
		
		if (paycheckHistory.getMcafeteria() == null)
		   paycheckHistoryForm.setCafeteria(null);
		else
		   paycheckHistoryForm.setCafeteria(amtFormat.format(paycheckHistory.getMcafeteria()));
		
		if (paycheckHistory.getMroth401k() == null)
		   paycheckHistoryForm.setRoth401k(null);
		else
		   paycheckHistoryForm.setRoth401k(amtFormat.format(paycheckHistory.getMroth401k()));
		
		if (paycheckHistory.getMfsaAmount() == null)
		   paycheckHistoryForm.setFsaAmount(null);
		else
		   paycheckHistoryForm.setFsaAmount(amtFormat.format(paycheckHistory.getMfsaAmount()));
			
		String paycheckTypeListName = ISlomFinAppConstants.DROPDOWN_PAYCHECKTYPES;			
		List pcTypes = (List) req.getSession().getServletContext().getAttribute(paycheckTypeListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_PAYCHECKTYPES, pcTypes);
		
		cache.setObject("ResponseObject", paycheckHistory, req.getSession());
		req.getSession().setAttribute("paycheckHistoryShowParm",paycheckHistoryShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PaycheckHistoryShowUpdateFormAction postprocessAction");
				
	}
						
}
