package com.pas.slomfin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.PaycheckAddForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.PaycheckOutflowAdd;
import com.pas.slomfin.valueObject.TransactionList;

public class PaycheckAddAction2 extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckAddAction2 pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltransaction transaction = new Tbltransaction();
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an add
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
		{    
			cache.setGoToDBInd(req.getSession(), false);
			cache.setObject("RequestObject", transaction, req.getSession());	
		}
		
		if (operation == IAppConstants.ADD_ACTION)
		{
			List<PaycheckOutflowAdd> trxList = new ArrayList<PaycheckOutflowAdd>();
			List<PaycheckOutflowAdd> trxList2 = new ArrayList<PaycheckOutflowAdd>();
			
			PaycheckAddForm pcaForm  = new PaycheckAddForm();
			pcaForm = (PaycheckAddForm)form;
			trxList = pcaForm.getPcoAddList();
			
			for (int i=0; i<trxList.size(); i++)
			{	
				PaycheckOutflowAdd paycheckOutflowAdd = new PaycheckOutflowAdd();
				paycheckOutflowAdd = trxList.get(i);			
				if (paycheckOutflowAdd.isProcessInd())
				{	
					if (paycheckOutflowAdd.getCheckNo() != null)	
					   if (paycheckOutflowAdd.getCheckNo().length() == 0)
						   paycheckOutflowAdd.setCheckNo(null);
										
					if (paycheckOutflowAdd.getXferAccountID() != null)	
						if (paycheckOutflowAdd.getXferAccountID().compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
						   paycheckOutflowAdd.setXferAccountID(null);
					
					if (paycheckOutflowAdd.getWdCategoryID() != null)	
						if (paycheckOutflowAdd.getWdCategoryID().compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
							paycheckOutflowAdd.setWdCategoryID(null);
					
					if (paycheckOutflowAdd.getCashDepositTypeID() != null)	
						if (paycheckOutflowAdd.getCashDepositTypeID().compareTo(ISlomFinAppConstants.DROPDOWN_NOT_SELECTED) == 0)
							paycheckOutflowAdd.setCashDepositTypeID(null);
					
					trxList2.add(paycheckOutflowAdd);				
				}
				cache.setGoToDBInd(req.getSession(), true);
			}
			TransactionList tListValObj = new TransactionList();
			tListValObj.setPcoAddList(trxList2);
			cache.setObject("RequestObject", tListValObj, req.getSession());										
		}
				
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckAddAction2 postprocessAction");
		
		req.getSession().removeAttribute("pcoAddList");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PaycheckAddAction2 postprocessAction");
				
	}

}
