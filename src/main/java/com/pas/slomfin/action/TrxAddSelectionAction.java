package com.pas.slomfin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblaccttypeinvtype;
import com.pas.dbobjects.Tblaccttypetrxtype;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.AccountTypeInvTypeDAO;
import com.pas.slomfin.dao.AccountTypeTrxTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.valueObject.DropDownBean;

public class TrxAddSelectionAction extends DispatchAction
{	
	protected static Logger log = LogManager.getLogger(TrxAddSelectionAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		log.debug("inside");	
		
		List<DropDownBean> investmentTypes = new ArrayList<>();
		List<DropDownBean> transactionTypes = new ArrayList<>();
		Tblaccount tblaccount = new Tblaccount();
		
		AccountDAO accountDAOReference;
			
		try 
		{
			accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			List<Tblaccount> acctList = accountDAOReference.inquire(new Integer(request.getParameter("acctID")));
			tblaccount = acctList.get(0);	
		} 
		catch (SystemException e) 
		{
			log.error("error retrieving accounts", e);
			throw new DAOException(e);
		}
		
		AccountTypeTrxTypeDAO accTypeTrxTypeDAOReference; 		
		AccountTypeInvTypeDAO accTypeInvTypeDAOReference;  
				
		try
		{
			accTypeTrxTypeDAOReference = (AccountTypeTrxTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNTTYPE_TRXTYPE_DAO);
			List<Tblaccttypetrxtype> rsAccTypeTrxType = accTypeTrxTypeDAOReference.inquire(tblaccount.getIaccountTypeId());
			
			for (int i = 0; i < rsAccTypeTrxType.size(); i++) 
			{
				Tblaccttypetrxtype tblacctyptrxtype = rsAccTypeTrxType.get(i);
				DropDownBean ddBean = new DropDownBean();
				ddBean.setId(String.valueOf(tblacctyptrxtype.getiTransactionTypeID()));
				ddBean.setDescription(tblacctyptrxtype.getTbltransactiontype().getSdescription());
				transactionTypes.add(ddBean);
			}
			accTypeInvTypeDAOReference = (AccountTypeInvTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNTTYPE_INVTYPE_DAO);
			List<Tblaccttypeinvtype> rsAccTypeInvType = accTypeInvTypeDAOReference.inquire(tblaccount.getIaccountTypeId());	
			
			for (int i = 0; i < rsAccTypeInvType.size(); i++) 
			{
				Tblaccttypeinvtype tblacctypinvtype = rsAccTypeInvType.get(i);
				DropDownBean ddBean = new DropDownBean();
				ddBean.setId(String.valueOf(tblacctypinvtype.getiInvestmentTypeID()));
				ddBean.setDescription(tblacctypinvtype.getTblinvestmenttype().getSdescription());
				investmentTypes.add(ddBean);
			}
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}
		
		request.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_INVESTMENTTYPES, investmentTypes);	
		request.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_TRANSACTIONTYPES, transactionTypes);	
		request.getSession().setAttribute("trxAddAcctID", request.getParameter("acctID"));	
		
		return mapping.findForward("success");
	}
	
}
