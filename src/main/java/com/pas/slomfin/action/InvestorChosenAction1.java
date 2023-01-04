package com.pas.slomfin.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblinvestor;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.struts.Config;

/**
 * Title: 		InvestorChosenAction1
 * Description: Verifies User Login to the Portfolio database
 * Copyright: 	Copyright (c) 2006
 */
public class InvestorChosenAction1 extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestorChosenAction1 pre - process");
		
		Investor investor = new Investor();
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		String fromWhere = req.getParameter("fromWhere");
		
		//If fromWhere is null, then we came from a place such as CashSpentAction; investor will be in cache
		//If fromWhere is login, then we are choosing default investor from web.xml
		//If fromWhere is investorList, then the user chose a new investor from the InvestorList screen
		
		if (fromWhere == null)
			investor = cache.getInvestor(req.getSession());
		else
		{	
			if (fromWhere.equalsIgnoreCase("login"))
			{
				 Config config = Config.getInstance();
			     Map webXMLMap = config.getMap();
			     String strInvestorID = (String) webXMLMap.get(ISlomFinAppConstants.DEFAULT_INVESTOR);
			     investor.setInvestorID(strInvestorID);
			}
			else
			{
				if (fromWhere.equalsIgnoreCase("investorList"))
				{   
					investor.setInvestorID(req.getParameter("investorID"));
					cache.setInvestor(req.getSession(),investor);
				}
			}	
		}
		
		cache.setGoToDBInd(req.getSession(), true);
		
		cache.setObject("RequestObject", Integer.valueOf(investor.getInvestorID()), req.getSession());
		
		log.debug("exiting InvestorChosenAction1 pre - process");
		
		return true;
    }
    
    @SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestorChosenAction1 postprocessAction");
	
		//get Value object from the request - should just be a single investor object
		
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		List<Tblinvestor> investorList = (List)cache.getObject("ResponseObject", req.getSession());		
			
		Tblinvestor tblinvestor = investorList.get(0);
				
		//put the investor into cache for title jsp
		Investor investor = new Investor();
		investor.setFirstName(tblinvestor.getSfirstName());
		investor.setLastName(tblinvestor.getSlastName());
		investor.setPictureFile(tblinvestor.getSpictureFile());
		investor.setPictureFileSmall(tblinvestor.getSpictureFileSmall());
		investor.setInvestorID(tblinvestor.getIinvestorId().toString());
		investor.setTaxGroupID(tblinvestor.getItaxGroupId().toString());
		investor.setTaxGroupName(tblinvestor.getTbltaxgroup().getStaxGroupName());
		
		cache.setInvestor(req.getSession(), investor);
		
		//set the action forward
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		log.debug("exiting InvestorChosenAction1 postprocessAction");
		
		
	}
}
