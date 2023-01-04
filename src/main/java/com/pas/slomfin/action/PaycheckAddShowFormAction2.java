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
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblpayday;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.PaycheckAddForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.PaycheckOutflowAdd;
import com.pas.valueObject.AppDate;
import com.pas.valueObject.DropDownBean;

public class PaycheckAddShowFormAction2 extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckAddShowFormAction2 pre - process");
		
		log.debug("putting the current investor object into cache as request object");
		log.debug("This is done in order to retrieve PaycheckOutflow List for this investor");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = new Investor();
		investor = cache.getInvestor(req.getSession());
		
		cache.setGoToDBInd(req.getSession(), true);
		
		cache.setObject("RequestObject", investor, req.getSession());
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckAddShowFormAction2 postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		//first, take care of paycheck details.
		//this is done by retrieving from cache the job object for this investor
		//this was previously set in PaycheckAddShowFormAction1
		
		Tbljob job = (Tbljob)cache.getObject("currentJobObject",req.getSession());
	    
	 	PaycheckAddForm paycheckAddForm = (PaycheckAddForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
					
		paycheckAddForm.setEmployer(job.getSemployer());
		
		AppDate paycheckDate = new AppDate();
		paycheckDate.initAppDateToToday();
		paycheckAddForm.setPaycheckHistoryDate(paycheckDate);
		
		paycheckAddForm.setJobID(job.getIjobId());
		paycheckAddForm.setGrossPay(amtFormat.format(job.getMgrossPay()));
		paycheckAddForm.setFederalWithholding(amtFormat.format(job.getMfederalWithholding()));
		paycheckAddForm.setStateWithholding(amtFormat.format(job.getMstateWithholding()));
		
		if (job.getMretirementDeferred() == null)
		   paycheckAddForm.setRetirementDeferred(null);
		else
		   paycheckAddForm.setRetirementDeferred(amtFormat.format(job.getMretirementDeferred()));	
						
		paycheckAddForm.setSsWithholding(amtFormat.format(job.getMsswithholding()));
		paycheckAddForm.setMedicareWithholding(amtFormat.format(job.getMmedicareWithholding()));
		
		if (job.getMmedical() == null)
		   paycheckAddForm.setMedical(null);
		else
		   paycheckAddForm.setMedical(amtFormat.format(job.getMmedical()));	
		
		if (job.getMdental() == null)
		   paycheckAddForm.setDental(null);
		else
		   paycheckAddForm.setDental(amtFormat.format(job.getMdental()));	
		
		if (job.getMgroupLifeInsurance() == null)
		   paycheckAddForm.setGroupLifeInsurance(null);
		else
		   paycheckAddForm.setGroupLifeInsurance(amtFormat.format(job.getMgroupLifeInsurance()));	
		
		if (job.getMgroupLifeIncome() == null)
		   paycheckAddForm.setGroupLifeIncome(null);
		else
		   paycheckAddForm.setGroupLifeIncome(amtFormat.format(job.getMgroupLifeIncome()));	
	
		if (job.getMvision() == null)
		   paycheckAddForm.setGroupLifeIncome(null);
		else
		   paycheckAddForm.setGroupLifeIncome(amtFormat.format(job.getMgroupLifeIncome()));	
		
		if (job.getMgroupLifeIncome() == null)
		   paycheckAddForm.setGroupLifeIncome(null);
		else
		   paycheckAddForm.setGroupLifeIncome(amtFormat.format(job.getMgroupLifeIncome()));	
		
		if (job.getMgroupLifeIncome() == null)
		   paycheckAddForm.setGroupLifeIncome(null);
		else
		   paycheckAddForm.setGroupLifeIncome(amtFormat.format(job.getMgroupLifeIncome()));	
		
		if (job.getMvision() == null)
		   paycheckAddForm.setVision(null);
		else
		   paycheckAddForm.setVision(amtFormat.format(job.getMvision()));
		
		if (job.getMparking() == null)
		   paycheckAddForm.setParking(null);
		else
		   paycheckAddForm.setParking(amtFormat.format(job.getMparking()));	
		
		if (job.getMcafeteria() == null)
		   paycheckAddForm.setCafeteria(null);
		else
		   paycheckAddForm.setCafeteria(amtFormat.format(job.getMcafeteria()));	
		
		if (job.getMroth401k() == null)
		   paycheckAddForm.setRoth401k(null);
		else
		   paycheckAddForm.setRoth401k(amtFormat.format(job.getMroth401k()));	
		
		if (job.getMfsaAmount() == null)
		   paycheckAddForm.setFsaAmount(null);
		else
		   paycheckAddForm.setFsaAmount(amtFormat.format(job.getMfsaAmount()));	
		
		String paycheckTypeListName = ISlomFinAppConstants.DROPDOWN_PAYCHECKTYPES;			
		List<DropDownBean> pcTypes = (List<DropDownBean>) req.getSession().getServletContext().getAttribute(paycheckTypeListName);
		for (int i = 0; i < pcTypes.size(); i++)
		{
			String pcTypeDesc = ((DropDownBean)pcTypes.get(i)).getDescription();
			Integer pcTypeID = new Integer(((DropDownBean)pcTypes.get(i)).getId());
			if (pcTypeDesc.equalsIgnoreCase("Regular"))
			{
				paycheckAddForm.setPaycheckTypeID(pcTypeID);
				break;
			}	
		}
		
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_PAYCHECKTYPES, pcTypes);
		
		//Next, populate the "Outflow" section
		//This section tells us where the money from this paycheck is going
		//populate the list in the form object from the paycheckOutflowList retrieved by this action
		
		String cashInvID = (String) req.getSession().getServletContext().getAttribute(ISlomFinAppConstants.CASHINVESTMENTID);
		List paycheckOutflowList = null;
		paycheckOutflowList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		List<PaycheckOutflowAdd> pcoAddList = new ArrayList<PaycheckOutflowAdd>();
		
		for (int i = 0; i < paycheckOutflowList.size(); i++)
		{
		    Tblpayday paycheckOutflow = (Tblpayday) paycheckOutflowList.get(i);
			
		    PaycheckOutflowAdd paycheckOutflowAdd = new PaycheckOutflowAdd();
		    paycheckOutflowAdd.setAccountID(paycheckOutflow.getTblaccountByIAccountId().getIaccountId());
		    paycheckOutflowAdd.setInvestmentID(new Integer(cashInvID));
		    paycheckOutflowAdd.setAccountName(paycheckOutflow.getTblaccountByIAccountId().getSaccountName());
		    paycheckOutflowAdd.setAmount(amtFormat.format(paycheckOutflow.getMdefaultAmt()));
		    
		    if (paycheckOutflow.getTblcashdeposittype() != null)
		        paycheckOutflowAdd.setCashDepositTypeID(paycheckOutflow.getTblcashdeposittype().getIcashDepositTypeId());
		    
		    paycheckOutflowAdd.setDescription(paycheckOutflow.getSdescription());
		    
		    AppDate outflowDate = new AppDate();
			outflowDate.initAppDateToToday();
			
			if (paycheckOutflow.getIdefaultDay() != null)
			    outflowDate.setAppday(paycheckOutflow.getIdefaultDay().toString());
			
			if (paycheckOutflow.getBnextMonthInd() == 1)
			{
			    int tempMonth = Integer.parseInt(outflowDate.getAppmonth());
			    int tempYear = Integer.parseInt(outflowDate.getAppyear());
			    
			    if (tempMonth == 12)
			    {
			        tempMonth = 1;
			        tempYear = tempYear + 1;
			        outflowDate.setAppyear(Integer.toString(tempYear));
			    }
			    else
			        tempMonth = tempMonth + 1;
			    
			    outflowDate.setAppmonth(Integer.toString(tempMonth));
			}
			
			paycheckOutflowAdd.setPcoAddDate(outflowDate.toStringMMDDYYYY());
		    
			paycheckOutflowAdd.setPcoAddID(paycheckOutflow.getIpaydayId());
			
			if (paycheckOutflow.getIdefaultDay() == null) //if no specific day defined, assume you always want it.
			    paycheckOutflowAdd.setProcessInd(true);
			else
		        paycheckOutflowAdd.setProcessInd(false);
		    
			paycheckOutflowAdd.setTransactionTypeDesc(paycheckOutflow.getTbltransactiontype().getSdescription());
		    paycheckOutflowAdd.setTransactionTypeID(paycheckOutflow.getTbltransactiontype().getItransactionTypeId());
		    
		    if (paycheckOutflow.getTblwdcategory() == null)
		       paycheckOutflowAdd.setWdCategoryID(null);
			else
			   paycheckOutflowAdd.setWdCategoryID(paycheckOutflow.getTblwdcategory().getIwdcategoryId());	
		    
		    if (paycheckOutflow.getTblaccountByIXferAccountId() == null)
			   paycheckOutflowAdd.setXferAccountID(null);
			else
			   paycheckOutflowAdd.setXferAccountID(paycheckOutflow.getTblaccountByIXferAccountId().getIaccountId());	
		    
		    if (paycheckOutflow.getTblaccountByIXferAccountId() == null)
			   paycheckOutflowAdd.setXferAccountName(null);
			else
			   paycheckOutflowAdd.setXferAccountName(paycheckOutflow.getTblaccountByIXferAccountId().getSaccountName());	
		    		    
		    pcoAddList.add(paycheckOutflowAdd);
		    
		}
		
		paycheckAddForm.setPcoAddList(pcoAddList); 
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting PaycheckAddShowUpdateFormAction2 postprocessAction");
				
	}
						
}
