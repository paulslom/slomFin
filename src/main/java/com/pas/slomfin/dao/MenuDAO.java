package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblJobRowMapper;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblaccttypeinvtype;
import com.pas.dbobjects.Tblaccttypetrxtype;
import com.pas.dbobjects.Tblinvestmenttype;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblmortgage;
import com.pas.dbobjects.Tblportfolio;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.AccountSelection;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.Menu;
import com.pas.util.MenuComparator;

/**
 * Title: 		MenuDAO
 * Project: 	Slomkowski Financial Application
 * Description: Menu DAO extends BaseDBDAO. 
 * Copyright: 	Copyright (c) 2006
 */
public class MenuDAO extends BaseDBDAO
{
  private Integer investorID;
  private Integer cashInvestmentTypeID;
  private Integer stockInvestmentTypeID;
  private Integer optionInvestmentTypeID;
  
  private static final String CONTEXT_ROOT = "/portfolio";

  private static final MenuDAO currentInstance = new MenuDAO();

    private MenuDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return LoginDAO
     */
    public static MenuDAO getDAOInstance()
    {
        return currentInstance;
    }
        
	@SuppressWarnings("unchecked")
	public List inquire(Object investor) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		log.debug("entering MenuDAO inquire");
		
		PortfolioDAO portfolioDAOReference;
		AccountDAO accountDAOReference;
		InvestmentTypeDAO investmentTypeDAOReference;
		AccountTypeTrxTypeDAO accTypeTrxTypeDAOReference;
		AccountTypeInvTypeDAO accTypeInvTypeDAOReference;
		JobDAO jobDAOReference;
		
		try 
		{
			portfolioDAOReference = (PortfolioDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PORTFOLIO_DAO);
			accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			investmentTypeDAOReference = (InvestmentTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENTTYPE_DAO);
			accTypeTrxTypeDAOReference = (AccountTypeTrxTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNTTYPE_TRXTYPE_DAO);
			accTypeInvTypeDAOReference = (AccountTypeInvTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNTTYPE_INVTYPE_DAO);
			jobDAOReference = (JobDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.JOB_DAO);
		} 
		catch (SystemException e) 
		{
			log.error("error retrieving portfolios in MenuDAO", e);
			throw new DAOException(e);
		}	
		List<Menu> menuList = new ArrayList<Menu>();					
		
		log.debug(methodName + "before inquiring for Menu. Investor ID parameter is = " + (((Investor)investor).getInvestorID()));
		investorID = Integer.valueOf (((Investor)investor).getInvestorID());
		
		log.debug("get Cash Investment ID - will need this later...");
		
		String invtypeQuery = "where sdescription in ('" + ISlomFinAppConstants.INVTYP_CASH + "', '" + ISlomFinAppConstants.INVTYP_STOCK + "', '" + ISlomFinAppConstants.INVTYP_OPTION + "')";
		
		List<Tblinvestmenttype> rsinvtype = investmentTypeDAOReference.inquire(invtypeQuery);
		
		for (int a = 0; a < rsinvtype.size(); a++)
		{
			Tblinvestmenttype tinv = rsinvtype.get(a);
			if (tinv.getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))
			{	
				cashInvestmentTypeID = tinv.getIinvestmentTypeId();
			}	
			if (tinv.getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_STOCK))
			{	
				stockInvestmentTypeID = tinv.getIinvestmentTypeId();
			}	
			if (tinv.getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_OPTION))
			{	
				optionInvestmentTypeID = tinv.getIinvestmentTypeId();
			}	
		}
				
 		List<Menu> trxList = menuStrutsCreateTransactions(portfolioDAOReference, accountDAOReference, accTypeTrxTypeDAOReference, accTypeInvTypeDAOReference);
 		List<Menu> reportsList = menuStrutsCreateReports(portfolioDAOReference, accountDAOReference);
 		List<Menu> workList = menuStrutsCreateWork(jobDAOReference);
 		//List<Menu> mortgagesList = menuStrutsCreateMortgages();
 		//List<Menu> taxesList = menuStrutsCreateTaxes();
 		List<Menu> miscList = menuStrutsCreateMisc((Investor)investor);
 		
		log.debug(methodName + "combining all lists into one");
				
		for (int i = 0; i < trxList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = trxList.get(i);
			menuList.add(menuDetail);
		}	
		
		for (int i = 0; i < reportsList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = reportsList.get(i);
			menuList.add(menuDetail);
		}			
		
		for (int i = 0; i < workList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = workList.get(i);
			menuList.add(menuDetail);
		}	
		/*
		for (int i = 0; i < mortgagesList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = mortgagesList.get(i);
			menuList.add(menuDetail);
		}			
		
		for (int i = 0; i < taxesList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = taxesList.get(i);
			menuList.add(menuDetail);
		}			
		*/
		
		for (int i = 0; i < miscList.size(); i++)
		{
			Menu menuDetail = new Menu();
			menuDetail = miscList.get(i);
			menuList.add(menuDetail);
		}			
		
		log.debug("have the full list, now need to sort it..");
		
		Collections.sort(menuList, new MenuComparator());
		
		log.debug("final list size is = " + menuList.size());
		log.debug(methodName + "out");
		
		return menuList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateTransactions(PortfolioDAO portfolioDaoReference, AccountDAO accountDAOReference,
			AccountTypeTrxTypeDAO accTypeTrxTypeDAOReference, AccountTypeInvTypeDAO accTypeInvTypeDAOReference) throws DAOException
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Transactions");
		menuDetail.setMenuTitle("Transaction");
		menuDetail.setMenuOrder(1);
		menuDetail.setMenuSubOrder(0);		
		mList.add(menuDetail);
		
		//portfolios submenu
		menuDetail = new Menu();
        menuDetail.setMenuParentName("Transactions");
		menuDetail.setMenuName("Portfolios");
		menuDetail.setMenuTitle("Portfolios");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PortfolioListAction.do?operation=inquire");		
		menuDetail.setMenuOrder(1);
		menuDetail.setMenuSubOrder(1);		
		mList.add(menuDetail);
		
		//Now derive submenu options for Transactions
		//now specific to portfolios for this investor		
		
		log.debug("before inquiring for portfolios in menu build process for reports...");	
		
		Tblinvestor tblinvestor = new Tblinvestor();
		tblinvestor.setIinvestorId(investorID);
		List<Tblportfolio> rsPortfolios = portfolioDaoReference.inquire(tblinvestor);		
		
		log.debug("looping through recordset to build Portfolio menu items for reports");
		
		int i;
		
		for (i = 0; i < rsPortfolios.size(); i++)
		{
			Tblportfolio port = rsPortfolios.get(i);
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("Portfolios");
			menuDetail.setMenuName("Portfolio" + port.getIportfolioId());
			menuDetail.setMenuTitle(port.getSportfolioName());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/PortfolioShowUpdateFormAction.do?&operation=inquire&portShowParm=update&portfolioID=" + port.getIportfolioId());
			menuDetail.setMenuOrder(1);
			menuDetail.setMenuSubOrder(2+i);
			mList.add(menuDetail);
			
			log.debug("added Menu: Portfolio ID = " + port.getIportfolioId() + " name = " + port.getSportfolioName());
		}

		//add new portfolio
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Portfolios");
		menuDetail.setMenuName("PortfolioAddNew");
		menuDetail.setMenuTitle("Add New Portfolio");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PortfolioShowUpdateFormAction.do?&operation=inquire&portShowParm=add");
		menuDetail.setMenuOrder(1);
		menuDetail.setMenuSubOrder(2+i);
		mList.add(menuDetail);	
			
		//finally accounts for this investor
		
		AccountSelection accSel = new AccountSelection();
		accSel.setInvestorID(investorID);
			
		log.debug("before inquiring for accounts in menu build process for transactions...");
				
		List<Tblaccount> rsAccounts = accountDAOReference.inquire(accSel);
		
		log.debug("looping through recordset to build Account menu items for transactions");
		
		String lastPortfolioName = "xxx";
		String currentParent = "";
		int openAccountsCounter = 0;		
		int closedAccountsCounter = 0;
		int currentSubOrder = 0;
		
		log.debug("There are " + rsAccounts.size() + " accounts in this list..");

		for (i = 0; i < rsAccounts.size(); i++)
		{
			Tblaccount acct = rsAccounts.get(i);
			
			log.debug("Working on Account ID = " + acct.getIaccountId() + " name = " + acct.getSaccountName());
			
			if (!lastPortfolioName.equalsIgnoreCase(acct.getTblportfolio().getSportfolioName()))
			{				
				openAccountsCounter = 0;		
				closedAccountsCounter = 0;
				lastPortfolioName = acct.getTblportfolio().getSportfolioName();	
				
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Portfolio" + acct.getTblportfolio().getIportfolioId().toString());
				menuDetail.setMenuName("AcctAddNewPort" + acct.getTblportfolio().getIportfolioId().toString());
				menuDetail.setMenuTitle("Add New Account");
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/AccountShowUpdateFormAction.do?&operation=inquire&accountShowParm=add&portfolioID=" + acct.getTblportfolio().getIportfolioId().toString());
			  	menuDetail.setMenuOrder(1);
				menuDetail.setMenuSubOrder(300);
				mList.add(menuDetail);			
		        
			}
			
			if (acct.isBclosed()) //closed accounts
			{
				log.debug("account is closed");
				
				if (closedAccountsCounter == 0)
				{
					menuDetail = new Menu();
					menuDetail.setMenuParentName("Portfolio" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuName("TrxClosedAccPort" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuTitle("Closed Accounts");
					menuDetail.setMenuLocation(CONTEXT_ROOT + "/AccountListAction.do?&accStatus=closed&operation=inquire&portfolioID=" + acct.getTblportfolio().getIportfolioId().toString());
				  	menuDetail.setMenuOrder(1);
					menuDetail.setMenuSubOrder(200);
					mList.add(menuDetail);			
				}
				closedAccountsCounter++;
				currentSubOrder = 200 + i;
				currentParent = "TrxClosedAccPort" + acct.getTblportfolio().getIportfolioId();
			}
			else //these are open accounts
			{
				log.debug("acccount is open");
				
				if (openAccountsCounter == 0)
				{
					menuDetail = new Menu();
					menuDetail.setMenuParentName("Portfolio" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuName("TrxOpenAccPort" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuTitle("Open Accounts");
					menuDetail.setMenuLocation(CONTEXT_ROOT + "/AccountListAction.do?&accStatus=open&operation=inquire&portfolioID=" + acct.getTblportfolio().getIportfolioId().toString());
			    	menuDetail.setMenuOrder(1);
					menuDetail.setMenuSubOrder(100);
					mList.add(menuDetail);	   
				}
				openAccountsCounter++;
				currentSubOrder = 100 + i;
				currentParent = "TrxOpenAccPort" + acct.getTblportfolio().getIportfolioId();
			}
		   
			menuDetail = new Menu();
			menuDetail.setMenuParentName(currentParent);
			menuDetail.setMenuName("Account" + acct.getIaccountId().toString());
			menuDetail.setMenuTitle(acct.getSaccountName());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/AccountShowUpdateFormAction.do?operation=inquire&accountShowParm=update&accountID=" + acct.getIaccountId().toString());		
			menuDetail.setMenuOrder(1);
			menuDetail.setMenuSubOrder(currentSubOrder);
			mList.add(menuDetail);

			menuDetail = new Menu();
			menuDetail.setMenuParentName("Account" + acct.getIaccountId().toString());
			menuDetail.setMenuName("ViewChangeDelAcct" + acct.getIaccountId().toString());
			menuDetail.setMenuTitle("View-Chg-Del");
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/TrxListAction.do?trxListOrigin=1&operation=inquire&AccountID=" + acct.getIaccountId().toString());		
			menuDetail.setMenuOrder(1);
			menuDetail.setMenuSubOrder(currentSubOrder+100);
			mList.add(menuDetail);
			
			//if account is closed, do not do anything with trx types
			if (acct.isBclosed())
			{
				continue;
			}
			
			//Add trx types allowable for this account type
						
			List<Tblaccttypetrxtype> rsAccTypeTrxType = accTypeTrxTypeDAOReference.inquire(acct.getIaccountTypeId());
			
			log.debug("looping through recordset to build Account menu items for reports");
	
			log.debug("there are " + rsAccTypeTrxType.size() + " valid trx types for this account type = " + acct.getTblaccounttype().getIaccountTypeId().toString());
			
			
			for (int j = 0; j < rsAccTypeTrxType.size(); j++)
			{
				Tblaccttypetrxtype atxt = rsAccTypeTrxType.get(j);
				
				//use this to hold the URL
				StringBuffer menuLoc = new StringBuffer();
				menuLoc.append(CONTEXT_ROOT + "/TrxShowUpdateFormAction.do?trxShowParm=add&operation=inquire");
                menuLoc.append("&portID=" + acct.getTblportfolio().getIportfolioId().toString());
                menuLoc.append("&portName=" + acct.getTblportfolio().getSportfolioName());
                menuLoc.append("&acctID=" + acct.getIaccountId().toString());
                menuLoc.append("&acctName=" + acct.getSaccountName());
                menuLoc.append("&ttID=" + atxt.getTbltransactiontype().getItransactionTypeId().toString());
                menuLoc.append("&ttDesc=" + atxt.getTbltransactiontype().getSdescription()); 
                
                if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
        		||  atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)
        		||  atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST))
                { 
                	log.debug("Trx Type is buy or sell, don't add itID and itDesc");
                }	
                else if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT))
                {	
                	menuLoc.append("&itID=" + stockInvestmentTypeID.toString());
                	menuLoc.append("&itDesc=Stock");
                }
                else if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
            	||  atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)) 
                {	
                	menuLoc.append("&itID=" + optionInvestmentTypeID.toString());
                	menuLoc.append("&itDesc=Option");
                }
                else // the default is use cash
                {	
                	menuLoc.append("&itID=" + cashInvestmentTypeID.toString());
                	menuLoc.append("&itDesc=Cash");
                }
                
                log.debug("This account's name is: " + acct.getSaccountName());
                
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Account" + acct.getIaccountId().toString());
				menuDetail.setMenuName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString());
				menuDetail.setMenuTitle("Add: " + atxt.getTbltransactiontype().getSdescription());
				menuDetail.setMenuLocation(menuLoc.toString());
			   	menuDetail.setMenuOrder(1);
				menuDetail.setMenuSubOrder(currentSubOrder+150);
				mList.add(menuDetail);
				
				log.debug("added new menu = Add: " + atxt.getTbltransactiontype().getSdescription());
				
				//for buy, sell, and reinvest need to insert applicable investment types.
				
				if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
				||  atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))
				{
					//Add inv types allowable for this account type
													 
					log.debug("before inquiring for investment types...");
							
					List<Tblaccttypeinvtype> rsAccTypeInvType = accTypeInvTypeDAOReference.inquire(acct.getIaccountTypeId());
					
					log.debug("looping through recordset to build buy-sell-reinvest menu items for trx");
			
					log.debug("there are " + rsAccTypeInvType.size() + " valid inv types for this account type = " + acct.getTblaccounttype().getIaccountTypeId().toString());
					
					for (int k = 0; k < rsAccTypeInvType.size(); k++)
					{
						Tblaccttypeinvtype atit = rsAccTypeInvType.get(k);
						
						String invDesc = atit.getTblinvestmenttype().getSdescription();
						
						if (invDesc.equalsIgnoreCase("Cash"))
							continue;
						
						//owned first (add that "Y" at end of menuName)
						menuDetail = new Menu();
						menuDetail.setMenuParentName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString());
						menuDetail.setMenuName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString() + "InvType" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString() + "Y");
						
						if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase("Buy"))
						   menuDetail.setMenuTitle(atit.getTblinvestmenttype().getSdescription() + ": Add to Position");
						else
						   menuDetail.setMenuTitle(atit.getTblinvestmenttype().getSdescription() + ": Existing Position");
						
						menuDetail.setMenuLocation(menuLoc.toString() + "&itID=" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString() + "&itDesc=" + atit.getTblinvestmenttype().getSdescription() + "&owned=yes");
					   	menuDetail.setMenuOrder(1);
						menuDetail.setMenuSubOrder(currentSubOrder+550);
						mList.add(menuDetail);
						
						//unowned next (add that "N" at end of menuName)
						menuDetail = new Menu();
						menuDetail.setMenuParentName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString());
						menuDetail.setMenuName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString() + "InvType" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString() + "N");
						menuDetail.setMenuTitle(atit.getTblinvestmenttype().getSdescription() + ": New Holding");
						menuDetail.setMenuLocation(menuLoc.toString() + "&itID=" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString() + "&itDesc=" + atit.getTblinvestmenttype().getSdescription() + "&owned=no");
					   	menuDetail.setMenuOrder(1);
						menuDetail.setMenuSubOrder(currentSubOrder+650);
						mList.add(menuDetail);						
													
					}	
				}
				
				if (atxt.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST))
				{
					//Add inv types allowable for this account type
				
					log.debug("before inquiring for investment types...");
							
					List<Tblaccttypeinvtype> rsAccTypeInvType = accTypeInvTypeDAOReference.inquire(acct.getIaccountTypeId());
					
					log.debug("looping through recordset to build buy-sell-reinvest menu items for trx");
			
					log.debug("there are " + rsAccTypeInvType.size() + " valid inv types for this account type = " + acct.getTblaccounttype().getIaccountTypeId().toString());
					
					for (int k = 0; k < rsAccTypeInvType.size(); k++)
					{
						Tblaccttypeinvtype atit = rsAccTypeInvType.get(k);
						
						String invDesc = atit.getTblinvestmenttype().getSdescription();
						
						if (invDesc.equalsIgnoreCase("Cash"))
						{
							continue;
						}
												
						menuDetail = new Menu();
						menuDetail.setMenuParentName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString());
						menuDetail.setMenuName("AddTrxTypeAcc" + acct.getIaccountId().toString() + "TrxType" + atxt.getTbltransactiontype().getItransactionTypeId().toString() + "InvType" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString());
						menuDetail.setMenuTitle(atit.getTblinvestmenttype().getSdescription());
						menuDetail.setMenuLocation(menuLoc.toString() + "&itID=" + atit.getTblinvestmenttype().getIinvestmentTypeId().toString() + "&itDesc=" + atit.getTblinvestmenttype().getSdescription());
					   	menuDetail.setMenuOrder(1);
						menuDetail.setMenuSubOrder(currentSubOrder+750);
						mList.add(menuDetail);
						
					}	
				}
			
			}
					
		}
		
		return mList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateReports(PortfolioDAO portfolioDAOReference, AccountDAO accountDAOReference) throws DAOException
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Reports");
		menuDetail.setMenuTitle("Reports");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(0);		
		mList.add(menuDetail);
		
		//submenus
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptTransactions");
		menuDetail.setMenuTitle("Transactions");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(1);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptTransactionsByInvestment");
		menuDetail.setMenuTitle("Trx By Investment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentChoiceShowFormAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(2);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptPortfolioSummary");
		menuDetail.setMenuTitle("Portfolio Summary");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioSummaryAction.do?&operation=inquire&investorID=" + investorID);		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(3);
		mList.add(menuDetail);	
			
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptBudget");
		menuDetail.setMenuTitle("Budget");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(4);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptCapitalGainsTaxable");
		menuDetail.setMenuTitle("Capital Gains Taxable");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(5);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptCapitalGainsNonTaxable");
		menuDetail.setMenuTitle("Capital Gains Non-Taxable");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(6);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptCostBasis");
		menuDetail.setMenuTitle("Cost Basis");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportCostBasisAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(7);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptDividends");
		menuDetail.setMenuTitle("Dividends");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(8);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptGoals");
		menuDetail.setMenuTitle("Goals");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportGoalsShowSelectionFormAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(9);
		mList.add(menuDetail);	
		
		/*
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptMtgPayments");
		menuDetail.setMenuTitle("Mortgage Payments");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(9);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptMtgAmortization");
		menuDetail.setMenuTitle("Mortgage Amortization");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(10);
		mList.add(menuDetail);	
		*/
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptPortfolioHistory");
		menuDetail.setMenuTitle("Portfolio History");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioHistoryAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(10);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptPortSummAssetClass");
		menuDetail.setMenuTitle("Portfolio By Asset Class");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioByAssetClassAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(11);
		mList.add(menuDetail);	
		
		/*
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptFederalTaxes");
		menuDetail.setMenuTitle("Federal Taxes");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(13);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptNCTaxes");
		menuDetail.setMenuTitle("NC Taxes");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(14);
		mList.add(menuDetail);	
		*/
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptWDCategory");
		menuDetail.setMenuTitle("Withdrawal Categories");
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(15);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Reports");
		menuDetail.setMenuName("RptUnitsOwned");
		menuDetail.setMenuTitle("Units Owned");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportUnitsOwnedAction.do?operation=inquire");		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(16);
		mList.add(menuDetail);	
		
		//now derive submenus of items that need the last 5 years
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		
		int counter = 0;
		
		//first add an ALL selection for Dividends
		menuDetail = new Menu();
		menuDetail.setMenuParentName("RptDividends");
		menuDetail.setMenuName("RptDividends0");
		menuDetail.setMenuTitle("All");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportDividendsAction.do?operation=inquire&Year=" + -1);		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(30+counter);
		mList.add(menuDetail);	
		
		for (int i=nowYear; i>nowYear-5; i--)
		{			  
			counter++;
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptBudget");
			menuDetail.setMenuName("RptBudget" + String.valueOf(i));
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportBudgetAction.do?operation=inquire&Year=" + i);		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptCapitalGainsTaxable");
			menuDetail.setMenuName("RptCapitalGainsTaxable" + String.valueOf(i));
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportCapitalGainsAction.do?operation=inquire&Year=" + i + "&taxable=Y");		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptCapitalGainsNonTaxable");
			menuDetail.setMenuName("RptCapitalGainsNonTaxable" + String.valueOf(i));
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportCapitalGainsAction.do?operation=inquire&Year=" + i + "&taxable=N");		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptDividends");
			menuDetail.setMenuName("RptDividends" + String.valueOf(i));
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptDividends" + String.valueOf(i));
			menuDetail.setMenuName("RptDividends" + String.valueOf(i) + "All");
			menuDetail.setMenuTitle("All");
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportDividendsAction.do?operation=inquire&Year=" + i + "&taxableOnly=No");	
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptDividends" + String.valueOf(i));
			menuDetail.setMenuName("RptDividends" + String.valueOf(i) + "TxblOnly");
			menuDetail.setMenuTitle("Taxable Only");
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportDividendsAction.do?operation=inquire&Year=" + i + "&taxableOnly=Yes");	
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(30+counter);
			mList.add(menuDetail);	
			
			/*
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptFederalTaxes");
			menuDetail.setMenuName("RptFederalTaxes" + String.valueOf(i));
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportFederalTaxesAction.do?operation=inquire&Year=" + i);		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(100+counter);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptNCTaxes");
			menuDetail.setMenuName("RptNCTaxes" + i);
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportNCTaxesAction.do?operation=inquire&Year=" + i);		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(110+counter);
			mList.add(menuDetail);	
			*/
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptWDCategory");
			menuDetail.setMenuName("RptWDCategory" + i);
			menuDetail.setMenuTitle(String.valueOf(i));
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportWDCategoriesAction.do?operation=inquire&Year=" + i);		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(120+counter);
			mList.add(menuDetail);	
		}
		
		//add a future year for Federal Taxes
		
		/*
		int nextYear = nowYear + 1;		
				
		menuDetail = new Menu();
		menuDetail.setMenuParentName("RptFederalTaxes");
		menuDetail.setMenuName("RptFederalTaxes" + String.valueOf(nowYear+1));
		menuDetail.setMenuTitle(String.valueOf(nextYear));
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportFederalTaxesAction.do?operation=inquire&Year=" + nextYear);		
		menuDetail.setMenuOrder(2);
		menuDetail.setMenuSubOrder(100+counter); //put the future year at the bottom
		mList.add(menuDetail);	
		
		StringBuffer sbuf = new StringBuffer();	
		sbuf.append("select * from Tblmortgage WHERE iinvestorId = " + investorID);
					
		log.debug("before inquiring for Mortgages in menu build process for reports...");
				
		List<Tblmortgage> rsMortgages = new ArrayList();		
		
		log.debug("looping through recordset to build Mortgage menu items");
				
		for (int i = 0; i < rsMortgages.size(); i++)
		{
			Tblmortgage mtg = rsMortgages.get(i);
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptMtgPayments");
			menuDetail.setMenuName("RptMtgPmt" + mtg.getImortgageId());
			menuDetail.setMenuTitle(mtg.getSdescription());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportMortgagePaymentsAction.do?operation=inquire&mortgageID=" + mtg.getImortgageId());		
			menuDetail.setMenuOrder(3);
			menuDetail.setMenuSubOrder(i+10);
			mList.add(menuDetail);	
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptMtgAmortization");
			menuDetail.setMenuName("RptMtgAmort" + mtg.getImortgageId());
			menuDetail.setMenuTitle(mtg.getSdescription());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportMortgageAmortizationAction.do?operation=inquire&mortDesc=" + mtg.getSdescription() + "&mortgageID=" + mtg.getImortgageId());		
			menuDetail.setMenuOrder(3);
			menuDetail.setMenuSubOrder(i+20);
			mList.add(menuDetail);	
		}
		*/
		//Now derive submenu options for Reporting on Transactions
		//now specific to portfolios for this investor
		
		log.debug("before inquiring for portfolios in menu build process for reports...");
		
		Tblinvestor tblinvestor = new Tblinvestor();
		tblinvestor.setIinvestorId(investorID);
		List<Tblportfolio> rsPortfolios = portfolioDAOReference.inquire(tblinvestor);		
		
		log.debug("looping through recordset to build Portfolio menu items for reports");
				
		for (int i = 0; i < rsPortfolios.size(); i++)
		{
			Tblportfolio port = rsPortfolios.get(i);
			
			menuDetail = new Menu();
			menuDetail.setMenuParentName("RptTransactions");
			menuDetail.setMenuName("RptTrxPortfolio" + port.getIportfolioId());
			menuDetail.setMenuTitle(port.getSportfolioName());
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(1+i);
			mList.add(menuDetail);	
		}
		
		//finally accounts for this investor
		AccountSelection accSel = new AccountSelection();
		accSel.setInvestorID(investorID);
			
		log.debug("before inquiring for accounts in menu build process for reports...");
				
		List<Tblaccount> rsAccounts = accountDAOReference.inquire(accSel);
		
		log.debug("looping through recordset to build Account menu items for reports");
		
		String lastPortfolioName = "xxx";
		String currentParent = "";
		int openAccountsCounter = 0;		
		int closedAccountsCounter = 0;
		int currentSubOrder = 0;
		
		for (int i = 0; i < rsAccounts.size(); i++)
		{
			Tblaccount acct = rsAccounts.get(i);
			
			if (!lastPortfolioName.equalsIgnoreCase(acct.getTblportfolio().getSportfolioName()))
			{				
				openAccountsCounter = 0;		
				closedAccountsCounter = 0;
				lastPortfolioName = acct.getTblportfolio().getSportfolioName();			
			}
			
			if (acct.isBclosed()) //closed accounts
			{
				if (closedAccountsCounter == 0)
				{
					menuDetail = new Menu();
					menuDetail.setMenuParentName("RptTrxPortfolio" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuName("RptTrxClosedAccPort" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuTitle("Closed Accounts");
					menuDetail.setMenuOrder(2);
					menuDetail.setMenuSubOrder(200);
					mList.add(menuDetail);			
				}
				closedAccountsCounter++;
				currentSubOrder = 200 + i;
				currentParent = "RptTrxClosedAccPort" + acct.getTblportfolio().getIportfolioId();
			}
			else //these are open accounts
			{
				if (openAccountsCounter == 0)
				{
					menuDetail = new Menu();
					menuDetail.setMenuParentName("RptTrxPortfolio" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuName("RptTrxOpenAccPort" + acct.getTblportfolio().getIportfolioId().toString());
					menuDetail.setMenuTitle("Open Accounts");
					menuDetail.setMenuOrder(2);
					menuDetail.setMenuSubOrder(100);
					mList.add(menuDetail);	   
				}
				openAccountsCounter++;
				currentSubOrder = 100 + i;
				currentParent = "RptTrxOpenAccPort" + acct.getTblportfolio().getIportfolioId();
			}
		   
			menuDetail = new Menu();
			menuDetail.setMenuParentName(currentParent);
			menuDetail.setMenuName("RptTrxAccount" + acct.getIaccountId().toString());
			menuDetail.setMenuTitle(acct.getSaccountName());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportTransactionsAction.do?operation=inquire&accountID=" + acct.getIaccountId().toString());		
			menuDetail.setMenuOrder(2);
			menuDetail.setMenuSubOrder(currentSubOrder);
			mList.add(menuDetail);	
			
		}
		
		return mList;
	}	
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateWork(JobDAO jobDAOReference) throws DAOException
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Work");
		menuDetail.setMenuTitle("Work");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(0);		
		mList.add(menuDetail);
		
		//submenus
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Work");
		menuDetail.setMenuName("PaycheckOutflow");
		menuDetail.setMenuTitle("Paycheck Outflow");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(2);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("PaycheckOutflow");
		menuDetail.setMenuName("PaycheckOutflowViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckOutflowListAction.do?operation=inquire&paycheckOutflowShowParm=inquire");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(21);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("PaycheckOutflow");
		menuDetail.setMenuName("PaycheckOutflowAdd");
		menuDetail.setMenuTitle("Add");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckOutflowShowUpdateFormAction.do?operation=inquire&paycheckOutflowShowParm=add");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(22);
		mList.add(menuDetail);	

		menuDetail = new Menu();
		menuDetail.setMenuParentName("Work");
		menuDetail.setMenuName("Jobs");
		menuDetail.setMenuTitle("Jobs");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(3);
		mList.add(menuDetail);	

	    menuDetail = new Menu();
		menuDetail.setMenuParentName("Jobs");
		menuDetail.setMenuName("JobsViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/JobListAction.do?operation=inquire&jobShowParm=inquire");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(31);
		mList.add(menuDetail);		

	    menuDetail = new Menu();
		menuDetail.setMenuParentName("Work");
		menuDetail.setMenuName("JobsAddNew");
		menuDetail.setMenuTitle("Add New Job");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/JobShowUpdateFormAction.do?&operation=inquire&jobShowParm=add");
		menuDetail.setMenuOrder(3);
		menuDetail.setMenuSubOrder(5);
		mList.add(menuDetail);	

		//now specific to jobs for this investor
				
		log.debug("before inquiring for Jobs in menu build process...");
		
		Tblinvestor tblinvestor = new Tblinvestor();
		tblinvestor.setIinvestorId(investorID);
		List<Tbljob> rsJobs = jobDAOReference.inquire(tblinvestor);
		
		log.debug("looping through recordset to build Job menu items");
				
		for (int i = 0; i < rsJobs.size(); i++)
		{
			Tbljob job = rsJobs.get(i);
			
			if (job.getDjobEndDate() == null) //only do current jobs
			{	
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Jobs");
				menuDetail.setMenuName("Job" + job.getIjobId().toString());
				menuDetail.setMenuTitle(job.getSemployer());
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/JobShowUpdateFormAction.do?operation=inquire&jobShowParm=update&jobID=" + job.getIjobId().toString());
				menuDetail.setMenuOrder(3);
				menuDetail.setMenuSubOrder(i+100);
				mList.add(menuDetail);			
	    	
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Work");
				menuDetail.setMenuName("AddPaycheck" + job.getIjobId().toString());
				menuDetail.setMenuTitle("Add Paycheck " + job.getSemployer().substring(0,6));
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckAddShowFormAction1.do?operation=inquire&jobID=" + job.getIjobId().toString());
				menuDetail.setMenuOrder(3);
				menuDetail.setMenuSubOrder(i+200);
				mList.add(menuDetail);
				
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Work");
				menuDetail.setMenuName("PaycheckHistoryViewChgDel"+ job.getIjobId().toString());
				menuDetail.setMenuTitle("Pchk Hist View-Chg-Del " + job.getSemployer().substring(0,3));
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckHistoryListAction.do?&operation=inquire&paycheckHistoryShowParm=inquire&ijobID=" +  job.getIjobId().toString());
				menuDetail.setMenuOrder(3);
				menuDetail.setMenuSubOrder(i+300);
				mList.add(menuDetail);
			}
		         
		}
		
		return mList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateMortgages()
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Mortgages");
		menuDetail.setMenuTitle("Mortgages");
		menuDetail.setMenuOrder(4);
		menuDetail.setMenuSubOrder(0);
		mList.add(menuDetail);	
		
		//submenu items
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Mortgages");
		menuDetail.setMenuName("MortgagesAddNew");
		menuDetail.setMenuTitle("Add New Mortgage");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/MortgageShowUpdateFormAction.do?operation=inquire&mortgageShowParm=add");
		menuDetail.setMenuOrder(4);
		menuDetail.setMenuSubOrder(3);
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Mortgages");
		menuDetail.setMenuName("VCDMortgages");
		menuDetail.setMenuTitle("View-Chg-Del");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/MortgageListAction.do?&mortgageStatus=open&operation=inquire&investorID=" + investorID);
		menuDetail.setMenuOrder(4);
		menuDetail.setMenuSubOrder(2);
		mList.add(menuDetail);			
	
		//now specific to mortgages for this investor
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblmortgage WHERE iinvestorId = " + investorID);
					
		log.debug("before inquiring for Mortgages in menu build process...");
				
		List<Tblmortgage> rsMortgages = new ArrayList();		
		
		log.debug("looping through recordset to build Mortgage menu items");
				
		for (int i = 0; i < rsMortgages.size(); i++)
		{
			Tblmortgage mtg = rsMortgages.get(i);
						
			menuDetail = new Menu();
			menuDetail.setMenuParentName("Mortgages");
			menuDetail.setMenuName("Mortgage" + mtg.getImortgageId().toString());
			menuDetail.setMenuTitle(mtg.getSdescription());
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/MortgageShowUpdateFormAction.do?operation=inquire&mortgageShowParm=update&mortgageID=" + mtg.getImortgageId().toString());
			menuDetail.setMenuOrder(4);
			menuDetail.setMenuSubOrder(i+100);
			mList.add(menuDetail);			
    	
			menuDetail = new Menu();
			menuDetail.setMenuParentName("Mortgage" + mtg.getImortgageId().toString());
			menuDetail.setMenuName("Mortgage" + mtg.getImortgageId().toString() + "ViewChgDelPayments");
			menuDetail.setMenuTitle("View-Chg-Del Payments");
			menuDetail.setMenuLocation(CONTEXT_ROOT + "/MortgagePaymentListAction.do?&operation=inquire&mortgageID=" + mtg.getImortgageId().toString());
			menuDetail.setMenuOrder(4);
			menuDetail.setMenuSubOrder(i+100);
			mList.add(menuDetail);
			
			if (mtg.getDmortgageEndDate() == null)
			{
				menuDetail = new Menu();
				menuDetail.setMenuParentName("Mortgages");
				menuDetail.setMenuName("Mortgage" + mtg.getImortgageId().toString() + "AddPayment");
				menuDetail.setMenuTitle("Add " + mtg.getSdescription() + " Pmt");
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/MortgagePaymentShowUpdateFormAction.do?operation=inquire&mortgagePaymentShowParm=add&mortgageID=" + mtg.getImortgageId().toString());
				menuDetail.setMenuOrder(4);
				menuDetail.setMenuSubOrder(1);
				mList.add(menuDetail);	
			}	
   
		}
		
		return mList;
	}	
	
	private List<Menu> menuStrutsCreateTaxes()
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Taxes");
		menuDetail.setMenuTitle("Taxes");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(0);
		mList.add(menuDetail);
		
		//submenu items
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxTablesClone");
		menuDetail.setMenuTitle("Clone Tax Tables");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxTableCloneAction.do?operation=add");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(1);
		mList.add(menuDetail);		
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxTablesViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Tax Formulas");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxTableListAction.do?operation=inquire");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(2);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxGroupAdd");
		menuDetail.setMenuTitle("Add New Tax Group");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxGroupShowUpdateFormAction.do?operation=inquire&taxGroupShowParm=add");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(3);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxGroupViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Tax Groups");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxGroupListAction.do?operation=inquire");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(4);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxGroupYearlyAmtAdd");
		menuDetail.setMenuTitle("Add New Tax Grp Year");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxGroupYearShowUpdateFormAction.do?operation=inquire&tgyShowParm=add");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(5);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxGroupYearlyAmtViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Tax Grp Year");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxGroupYearListAction.do?operation=inquire");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(6);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxPaymentsAdd");
		menuDetail.setMenuTitle("Add New Tax Payment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxPaymentShowUpdateFormAction.do?operation=inquire&taxPaymentShowParm=add");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(7);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Taxes");
		menuDetail.setMenuName("TaxPaymentsViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Tax Payment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TaxPaymentListAction.do?operation=inquire");
		menuDetail.setMenuOrder(5);
		menuDetail.setMenuSubOrder(8);
		mList.add(menuDetail);
	
		return mList;
	}	
	
	private List<Menu> menuStrutsCreateMisc(Investor investor)
	{
		List<Menu> mList = new ArrayList<Menu>();
		
		Menu menuDetail = new Menu();
		
		//top-level menu
		menuDetail.setMenuName("Miscellaneous");
		menuDetail.setMenuTitle("Miscellaneous");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(0);		
		mList.add(menuDetail);
		
		//submenus
	
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("InvestmentsByInv");
		menuDetail.setMenuTitle("Owned by " + investor.getTaxGroupName());
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentsOwnedShowFormAction.do?operation=inquire&investmentShowParm=inquire&investorID=" + investorID);
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(1);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("InvestmentsViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Investments");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentListAction.do?operation=inquire");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(2);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("InvestmentsAddNew");
		menuDetail.setMenuTitle("Add New Investment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentShowUpdateFormAction.do?operation=inquire&investmentShowParm=add");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(3);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("CategoriesViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del WD Cat");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/WDCategoryListAction.do?operation=inquire&investorID=" + investorID);
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(4);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("CategoriesAddNew");
		menuDetail.setMenuTitle("Add New WD Cat");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/WDCategoryShowUpdateFormAction.do?operation=inquire&wdCategoryShowParm=add");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(5);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("AssetClassViewChgDel");
		menuDetail.setMenuTitle("View-Chg-Del Asset Cls");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/AssetClassListAction.do?operation=inquire");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(6);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("AssetClassAddNew");
		menuDetail.setMenuTitle("Add New Asset Cls");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/AssetClassShowUpdateFormAction.do?operation=inquire&assetClassShowParm=add");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(7);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuParentName("Miscellaneous");
		menuDetail.setMenuName("SearchTrx");
		menuDetail.setMenuTitle("Search Trx Descriptions");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TrxShowSearchFormAction.do?operation=inquire");
		menuDetail.setMenuOrder(6);
		menuDetail.setMenuSubOrder(8);
		mList.add(menuDetail);
			
		return mList;
	}	
	

}
