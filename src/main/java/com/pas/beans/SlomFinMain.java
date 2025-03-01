package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.pas.dynamodb.DateToStringConverter;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoTransaction;
import com.pas.dynamodb.DynamoUtil;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.PortfolioHistoryDAO;
import com.pas.slomfin.dao.TransactionDAO;
import com.pas.util.SlomFinUtil;
import com.pas.util.TransactionTypeComparator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;

@Named("pc_SlomFinMain")
@ApplicationScoped
public class SlomFinMain implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SlomFinMain.class);	
		
	private final double id = Math.random();	
	
	public static String GREEN_STYLECLASS_MENU = "menuGreen";
	public static String RED_STYLECLASS_MENU = "menuRed";
	
	private String siteTitle;	
	
	private String transactionAcidSetting;
	private DynamoTransaction selectedTransaction;
	private boolean renderTransactionUpdateFields = false;
	private boolean renderTransactionId = false;
	private List<SelectItem> trxTypeList = new ArrayList<>();
	private List<SelectItem> investmentTypeList = new ArrayList<>();
	private List<SelectItem> investmentList = new ArrayList<>();
	
	private boolean renderTrxAmount = true;
	private boolean renderTrxUnits = false;
	private boolean renderTrxPrice = false;
	private boolean renderTrxWDCategoriesList = false;
	private boolean renderTrxCheckNumber = false;
	private boolean renderTrxDividendTaxableYear = false;
	private boolean renderTrxCashDepositType = false;
	private boolean renderTrxXferAccount = false;
	private boolean renderTrxCashDescription = true;
	private boolean renderTrxLastOfBillingCycle = false;
	private boolean renderTrxInvestmentType = false;
	private boolean renderTrxInvestment = false;	
	private boolean renderOwnedInvestmentsCheckbox = false;
	
	private boolean ownedInvestments = false;
	
	private List<SelectItem> wdCategoriesList = new ArrayList<>();
	
	private String operation;
	
	private TransactionDAO transactionDAO;
	private InvestmentDAO investmentDAO;
	private AccountDAO accountDAO;
	private PortfolioHistoryDAO portfolioHistoryDAO;
	
	private List<DynamoTransaction> trxList = new ArrayList<>();
	private String trxListTitle;
	
	public void onStart(@Observes @Initialized(ApplicationScoped.class) Object pointless) 
	{
		logger.info("Entering SlomFinMain onStart method.  Should only be here ONE time");
		logger.info("SlomFinMain id is: " + this.getId());
		this.setSiteTitle("Slomkowski Financial");
		
		try 
		{
			//this gets populated at app startup, no need to do it again when someone logs in.
			if (accountDAO == null || accountDAO.getFullAccountsList().isEmpty())
			{
				DynamoClients dynamoClients = DynamoUtil.getDynamoClients();				
				
				loadAccounts(dynamoClients);
				loadInvestments(dynamoClients);
				loadPortfolioHistory(dynamoClients);
				loadTransactions(dynamoClients);
			}
		} 
		catch (Exception e) 
		{
			logger.error(e.getMessage(), e);
		}		
	}

	private void loadTransactions(DynamoClients dynamoClients) throws Exception
	{
		logger.info("entering loadTransactions");
		transactionDAO = new TransactionDAO(dynamoClients);
		transactionDAO.readAllTransactionsFromDB();
		logger.info("Transactions read in. List size = " + transactionDAO.getFullTransactionsList().size());
	}

	private void loadInvestments(DynamoClients dynamoClients)
	{
		logger.info("entering loadInvestments");
		investmentDAO = new InvestmentDAO(dynamoClients);
		investmentDAO.readInvestmentsFromDB();
		logger.info("Investments read in. List size = " + investmentDAO.getFullInvestmentsList().size());
	}

	private void loadAccounts(DynamoClients dynamoClients)
	{
		logger.info("entering loadAccounts");
		accountDAO = new AccountDAO(dynamoClients);
		accountDAO.readAccountsFromDB();
		logger.info("Accounts read in. List size = " + accountDAO.getFullAccountsList().size());
	}

	private void loadPortfolioHistory(DynamoClients dynamoClients) throws Exception 
	{
		logger.info("entering loadPortfolioHistory");
		portfolioHistoryDAO = new PortfolioHistoryDAO(dynamoClients);
		portfolioHistoryDAO.readPortfolioHistoryFromDB();
		logger.info("Portfolio History read in. List size = " + portfolioHistoryDAO.getFullPortfolioHistoryList().size());
	}
	
	public String getSignedOnUserName()
	{
		String username = "";
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) 
		{
		   username = ((UserDetails)principal).getUsername();
		} 
		else 
		{
		   username = principal.toString();
		}
		
		if (username != null)
		{
			username = username.toLowerCase();
		}
		return username;
	}
	
	private void refreshTrxList(Integer accountID)
	{
		this.getTrxList().clear();
		
	    this.setTrxList(new ArrayList<>(transactionDAO.getLast2YearsTransactionsMapByAccountID().get(accountID)));
	    
	    if (this.getTrxList() != null && this.getTrxList().size() > 0)
    	{
	    	DynamoTransaction trx = this.getTrxList().get(0);
    		logger.info("account Name:  " + trx.getAccountName() + " selected to show trx from menu");
    		this.setTrxListTitle("Transaction List for Account " + trx.getAccountName());
    	}
	}
	
	public void accountTransactionsSelection(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String accountid = ec.getRequestParameterMap().get("accountid");		    
		    logger.info("account ID " + accountid + " selected to show trx from menu");
		    
		    refreshTrxList(Integer.parseInt(accountid));
		    
            String targetURL = SlomFinUtil.getContextRoot() + "/transactionList.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL);
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}  	
	
	public void accountSelection(ActionEvent event) 
	{
		try 
        {		
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    
		    String accountid = ec.getRequestParameterMap().get("accountid");
		    
		    logger.info("account ID " + accountid + " selected to update account from menu");
		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/accountAddUpdate.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL);
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	} 
	
	public void invOwnedCheck(AjaxBehaviorEvent event) 
	{
		try 
        {
			SelectBooleanCheckbox selectbooleanCheckbox = (SelectBooleanCheckbox)event.getSource();		
			Boolean invOwnedWasChecked = (Boolean)selectbooleanCheckbox.getValue();	
			
			if (invOwnedWasChecked)
            {
				logger.info("user checked that they want only owned investments in the dropdown");
				this.setInvestmentList(setOwnedInvestmentsDropdown(this.getSelectedTransaction().getInvestmentTypeID()));				
            }
			else
			{
				logger.info("user un-checked that they want only owned investments in the dropdown");
				this.setInvestmentList(investmentDAO.getInvestmentsByInvestmentTypeID(this.getSelectedTransaction().getInvestmentTypeID()));
			}
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}  	
	
	private List<SelectItem> setOwnedInvestmentsDropdown(Integer investmentTypeID) 
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		List<DynamoTransaction> accountTrxList = transactionDAO.getFullTransactionsMapByAccountID().get(this.getSelectedTransaction().getAccountID());
		Map<Integer, BigDecimal> unitsOwnedForAccountMap = SlomFinUtil.getUnitsOwnedForAccount(accountTrxList); 
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (Integer key : unitsOwnedForAccountMap.keySet()) 
		{
            BigDecimal totalUnitsOwned = unitsOwnedForAccountMap.get(key);
            
            if (totalUnitsOwned != null
        	&&  totalUnitsOwned.compareTo(BigDecimal.ZERO) != 0)
            {
            	Investment inv = investmentDAO.getInvestmentByInvestmentID(key);
            	
            	if (inv.getiInvestmentTypeID() == investmentTypeID)
            	{
            		String investmentDescription = inv.getDescription();
    	            SelectItem si = new SelectItem();
    				si.setValue(key);
    				si.setLabel(investmentDescription);
    				returnList.add(si);
            	}
            	
            }
        }
		return returnList;
	}

	public void valueChgTrxType(AjaxBehaviorEvent event) 
	{
		logger.info(SlomFinUtil.getLoggedInUserName() + " picked a new trx type on trx add/update form");
		
		try
		{
			SelectOneMenu selectonemenu = (SelectOneMenu)event.getSource();		
			Integer selectedOption = (Integer)selectonemenu.getValue();			
			String selectedTrxTypeDesc = SlomFinUtil.trxTypesMap.get(selectedOption);	
			this.getSelectedTransaction().setTransactionTypeDescription(selectedTrxTypeDesc);
			
			logger.info(selectedTrxTypeDesc + " was picked");
			
			enableTrxUpdateFields(selectedTrxTypeDesc);			
		}
		catch (Exception e)
		{
			logger.error("Exception in valueChgTrxType: " +e.getMessage(),e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in valueChgTrxType: " + e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
		}
	}
	
	public void valueChgInvType(AjaxBehaviorEvent event) 
	{
		logger.info(SlomFinUtil.getLoggedInUserName() + " picked a new investment type on trx add/update form");
		
		try
		{
			SelectOneMenu selectonemenu = (SelectOneMenu)event.getSource();		
			Integer selectedOption = (Integer)selectonemenu.getValue();			
			String selectedInvTypeDesc = SlomFinUtil.invTypesMap.get(selectedOption);
			logger.info(SlomFinUtil.getLoggedInUserName() + " picked investment type " + selectedInvTypeDesc);
			setRenderTrxInvestment(true);
			
			if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strSell)
			||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strReinvest)
			||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCashDividend)
			||	this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strSplit))
			{
				this.setInvestmentList(setOwnedInvestmentsDropdown(this.getSelectedTransaction().getInvestmentTypeID()));		
			}
			else
			{
				this.setInvestmentList(investmentDAO.getInvestmentsByInvestmentTypeID(this.getSelectedTransaction().getInvestmentTypeID()));
			}
		}
		catch (Exception e)
		{
			logger.error("Exception in valueChgInvType: " +e.getMessage(),e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in valueChgInvType: " + e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
		}
	}
	
	private void enableTrxUpdateFields(String selectedTrxTypeDesc) 
	{
		if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCashWithdrawal))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(true);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(true);
			setRenderTrxInvestmentType(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strBuy))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(true);
			setRenderTrxPrice(true);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);	
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(true);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCashDeposit))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(true);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(false);	
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCashDividend))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(true);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCheckWithdrawal))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(true);
			setRenderTrxCheckNumber(true);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strExerciseOption))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strExpireOption))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strFee))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(true);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(true);
			setRenderTrxInvestmentType(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strInterestEarned))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strLoan))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(true);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strMarginInterest))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(false);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(true);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(false);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strReinvest))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(true);
			setRenderTrxPrice(true);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(true);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strSell))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(true);
			setRenderTrxPrice(true);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);	
			setRenderTrxInvestmentType(true);	
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strSplit))
		{
			setRenderTrxAmount(false);
			setRenderTrxUnits(true);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(false);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(true);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferIn))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(true);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(true);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(true);	
			setRenderOwnedInvestmentsCheckbox(false);
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferOut))
		{
			setRenderTrxAmount(true);
			setRenderTrxUnits(true);
			setRenderTrxPrice(false);
			setRenderTrxWDCategoriesList(false);
			setRenderTrxCheckNumber(false);
			setRenderTrxDividendTaxableYear(false);
			setRenderTrxCashDepositType(false);
			setRenderTrxXferAccount(true);
			setRenderTrxCashDescription(false);
			setRenderTrxLastOfBillingCycle(false);
			setRenderTrxInvestmentType(true);
			setRenderTrxInvestment(true);
			setRenderOwnedInvestmentsCheckbox(false);
		}
		
	}

	public String returnToTrxList()
	{
		return "/transactionList.xhtml";
	}
	
	public String selectTransactionAcid()
	{		
		try 
        {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    String acid = ec.getRequestParameterMap().get("operation");
		    String transactionId = ec.getRequestParameterMap().get("transactionId");
		    String accountId = ec.getRequestParameterMap().get("accountId");
		    
		    this.setTransactionAcidSetting(acid);
		    
		    logger.info("Transaction operation setup for add-change-inquire-delete.  Function is: " + acid);
		    logger.info("transaction id selected: " + transactionId);
		    
		    if (acid.equalsIgnoreCase("add"))
		    {
		    	DynamoTransaction dynamoTrx = new DynamoTransaction();
		    	dynamoTrx.setAccountID(Integer.parseInt(accountId));
		    	Account acct = getAccountByAccountID(dynamoTrx.getAccountID());
		    	dynamoTrx.setEntryDateJava(new Date());
		    	dynamoTrx.setPostedDateJava(SlomFinUtil.getTwoDaysFromNowDate());
		    	dynamoTrx.setAccountName(acct.getsAccountName());
		    	
		    	String accountType = SlomFinUtil.accountTypesMap.get(acct.getiAccountTypeID());
		    	if (accountType != null && accountType.equalsIgnoreCase(SlomFinUtil.strCreditCard))
		    	{
		    		dynamoTrx.setTransactionTypeID(SlomFinUtil.CashWithdrawal);
		    		dynamoTrx.setTransactionTypeDescription(SlomFinUtil.strCashWithdrawal);
		    		enableTrxUpdateFields(dynamoTrx.getTransactionTypeDescription());		
		    	}
		    	
		    	this.setSelectedTransaction(dynamoTrx);	    	
		    }
		    else //go get the existing trx
		    {
		    	this.setSelectedTransaction(this.getTransactionByTransactionID(Integer.parseInt(transactionId)));
		    }
		    
		    if (acid.equalsIgnoreCase("add")
            ||	acid.equalsIgnoreCase("update"))
		    {
		    	Account acct = getAccountByAccountID(this.getSelectedTransaction().getAccountID());
		    	this.getTrxTypeList().clear();
		    	this.setTrxTypeList(SlomFinUtil.getValidTrxTypesForAccountTypes(acct.getiAccountTypeID()));
		    	
		    	Collections.sort(this.getTrxTypeList(), new TransactionTypeComparator());
		    	
		    	this.getInvestmentTypeList().clear();
		    	this.setInvestmentTypeList(SlomFinUtil.getValidInvTypesForAccountTypes(acct.getiAccountTypeID()));
		    }
		    
		    setRenderTransactionViewAddUpdateDelete(); 
		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/transactionAddUpdate.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL + " with operation: " + acid);
					    
        } 
        catch (Exception e) 
        {
        	logger.error("selectTransactionAcid errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
		
		return "";		
	}	
	
	private Account getAccountByAccountID(Integer accountID) 
	{
		return accountDAO.getAccountByAccountID(accountID);
	}

	public String addChangeDelTransaction()
	{
		logger.info("entering addChangeDelTransaction for operation: " + transactionAcidSetting);
		
		try
		{
			if (!transactionAcidSetting.equalsIgnoreCase("Delete"))
			{
				if (this.getSelectedTransaction().getTransactionTypeID() == -1)
				{
					FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaction Type not selected", "Transaction Type not selected");
				 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
				 	return "";
				}
				
				if (this.getSelectedTransaction().getWdCategoryID() != null && this.getSelectedTransaction().getWdCategoryID() > 0)
				{
					this.getSelectedTransaction().setWdCategoryDescription(transactionDAO.getWdCategoryMap().get(this.getSelectedTransaction().getWdCategoryID()));
				}
				
				this.getSelectedTransaction().setTransactionDate(DateToStringConverter.convertDateToDynamoStringFormat(this.getSelectedTransaction().getEntryDateJava()));
				this.getSelectedTransaction().setTransactionEntryDate(DateToStringConverter.convertDateToDynamoStringFormat(this.getSelectedTransaction().getEntryDateJava()));
				
				//Leave posted date field alone on an update - it is a sort key and cannot be updated - if modified it'll create a new entry
				if (transactionAcidSetting.equalsIgnoreCase("Add"))
				{
					this.getSelectedTransaction().setTransactionPostedDate(DateToStringConverter.convertDateToDynamoStringFormat(this.getSelectedTransaction().getPostedDateJava()));
				}
				
				if (this.getSelectedTransaction().getInvestmentID() == null || this.getSelectedTransaction().getInvestmentID() == 0)
				{
					this.getSelectedTransaction().setInvestmentID(getCashInvestmentID());			    	
					this.getSelectedTransaction().setInvestmentDescription("Cash");
				}
				else //we have an investment selected
				{
					this.getSelectedTransaction().setInvestmentDescription(investmentDAO.getInvestmentByInvestmentID(this.getSelectedTransaction().getInvestmentID()).getDescription());
				}
				
				this.getSelectedTransaction().setTransactionTypeDescription(SlomFinUtil.trxTypesMap.get(this.getSelectedTransaction().getTransactionTypeID()));		
				
				if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strFee)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCheckWithdrawal)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strMarginInterest)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strSell)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strTransferOut)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCashWithdrawal)
				||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strLoan))
				{
					this.getSelectedTransaction().setTrxTypPositiveInd(false);
					this.getSelectedTransaction().setEffectiveAmount(this.getSelectedTransaction().getCostProceeds().multiply(new BigDecimal(-1.0)));
				}
				else if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strBuy)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCashDividend)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strExpireOption)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCashDeposit)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strInterestEarned)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strSplit)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strReinvest)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strTransferIn)
					||  this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strExerciseOption))
				{
					this.getSelectedTransaction().setTrxTypPositiveInd(true);
					this.getSelectedTransaction().setEffectiveAmount(this.getSelectedTransaction().getCostProceeds());
				}
												
			}
			
			if (transactionAcidSetting.equalsIgnoreCase("Add"))
			{
				transactionDAO.addTransaction(this.getSelectedTransaction());
			}
			else if (transactionAcidSetting.equalsIgnoreCase("Update"))
			{
				transactionDAO.updateTransaction(this.getSelectedTransaction());
			}
			else if (transactionAcidSetting.equalsIgnoreCase("Delete"))
			{
				transactionDAO.deleteTransaction(this.getSelectedTransaction());
			}
		}
		catch (Exception e)
		{
        	logger.error("addChangeDelTransaction errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
		
		refreshTrxList(this.getSelectedTransaction().getAccountID());
		
		return "/transactionList.xhtml";	
	}
	
	public void setRenderTransactionViewAddUpdateDelete() 
	{
		if (transactionAcidSetting == null)
		{
			this.setRenderTransactionUpdateFields(false);
			this.setRenderTransactionId(false);
		}
		else
		{
			if (transactionAcidSetting.equalsIgnoreCase("View"))
			{
				this.setRenderTransactionUpdateFields(false);
				this.setRenderTransactionId(true);
			}
			else if (transactionAcidSetting.equalsIgnoreCase("Add"))
			{
				this.setRenderTransactionUpdateFields(true);
				this.setRenderTransactionId(false);
			}
			else if (transactionAcidSetting.equalsIgnoreCase("Update"))
			{
				this.setRenderTransactionUpdateFields(true);
				this.setRenderTransactionId(true);
			}
			else if (transactionAcidSetting.equalsIgnoreCase("Delete"))
			{
				this.setRenderTransactionUpdateFields(false);
				this.setRenderTransactionId(true);
			}
			else //not sure what to do then...
			{
				this.setRenderTransactionUpdateFields(false);
				this.setRenderTransactionId(false);
			}
		}
		
	}
	public double getId() {
		return id;
	}
		
	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	public TransactionDAO getTransactionDAO() {
		return transactionDAO;
	}

	public void setTransactionDAO(TransactionDAO transactionDAO) {
		this.transactionDAO = transactionDAO;
	}

	public InvestmentDAO getInvestmentDAO() {
		return investmentDAO;
	}

	public void setInvestmentDAO(InvestmentDAO investmentDAO) {
		this.investmentDAO = investmentDAO;
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public PortfolioHistoryDAO getPortfolioHistoryDAO() {
		return portfolioHistoryDAO;
	}

	public void setPortfolioHistoryDAO(PortfolioHistoryDAO portfolioHistoryDAO) {
		this.portfolioHistoryDAO = portfolioHistoryDAO;
	}

    public String getTransactionAcidSetting() {
        return transactionAcidSetting;
    }

    public void setTransactionAcidSetting(String transactionAcidSetting) {
        this.transactionAcidSetting = transactionAcidSetting;
    }

	public DynamoTransaction getTransactionByTransactionID(int trxID)
	{
		return transactionDAO.getTransactionByTransactionID(trxID);
	}

	public List<Account> getActiveTaxableAccountsList()
	{
		return accountDAO.getActiveTaxableAccountsList();
	}

	public List<Account> getActiveRetirementAccountsList()
	{
		return accountDAO.getActiveRetirementAccountsList();
	}

	public List<Account> getClosedAccountsList()
	{
		return accountDAO.getClosedAccountsList();
	}
	
	public Integer getCashInvestmentID()
	{
		return investmentDAO.getCashInvestmentId();
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<DynamoTransaction> getTrxList() {
		return trxList;
	}

	public void setTrxList(List<DynamoTransaction> trxList) {
		this.trxList = trxList;
	}

	public String getTrxListTitle() {
		return trxListTitle;
	}

	public void setTrxListTitle(String trxListTitle) {
		this.trxListTitle = trxListTitle;
	}

	public DynamoTransaction getSelectedTransaction() {
		return selectedTransaction;
	}

	public void setSelectedTransaction(DynamoTransaction selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}

	public boolean isRenderTransactionUpdateFields() {
		return renderTransactionUpdateFields;
	}

	public void setRenderTransactionUpdateFields(boolean renderTransactionUpdateFields) {
		this.renderTransactionUpdateFields = renderTransactionUpdateFields;
	}

	public boolean isRenderTransactionId() {
		return renderTransactionId;
	}

	public void setRenderTransactionId(boolean renderTransactionId) {
		this.renderTransactionId = renderTransactionId;
	}

	public List<SelectItem> getTrxTypeList() {
		return trxTypeList;
	}

	public void setTrxTypeList(List<SelectItem> trxTypeList) {
		this.trxTypeList = trxTypeList;
	}

	public boolean isRenderTrxAmount() {
		return renderTrxAmount;
	}

	public void setRenderTrxAmount(boolean renderTrxAmount) {
		this.renderTrxAmount = renderTrxAmount;
	}

	public boolean isRenderTrxUnits() {
		return renderTrxUnits;
	}

	public void setRenderTrxUnits(boolean renderTrxUnits) {
		this.renderTrxUnits = renderTrxUnits;
	}

	public boolean isRenderTrxPrice() {
		return renderTrxPrice;
	}

	public void setRenderTrxPrice(boolean renderTrxPrice) {
		this.renderTrxPrice = renderTrxPrice;
	}

	public boolean isRenderTrxWDCategoriesList() {
		return renderTrxWDCategoriesList;
	}

	public void setRenderTrxWDCategoriesList(boolean renderTrxWDCategoriesList) {
		this.renderTrxWDCategoriesList = renderTrxWDCategoriesList;
	}

	public boolean isRenderTrxCheckNumber() {
		return renderTrxCheckNumber;
	}

	public void setRenderTrxCheckNumber(boolean renderTrxCheckNumber) {
		this.renderTrxCheckNumber = renderTrxCheckNumber;
	}

	public boolean isRenderTrxDividendTaxableYear() {
		return renderTrxDividendTaxableYear;
	}

	public void setRenderTrxDividendTaxableYear(boolean renderTrxDividendTaxableYear) {
		this.renderTrxDividendTaxableYear = renderTrxDividendTaxableYear;
	}

	public boolean isRenderTrxCashDepositType() {
		return renderTrxCashDepositType;
	}

	public void setRenderTrxCashDepositType(boolean renderTrxCashDepositType) {
		this.renderTrxCashDepositType = renderTrxCashDepositType;
	}

	public boolean isRenderTrxXferAccount() {
		return renderTrxXferAccount;
	}

	public void setRenderTrxXferAccount(boolean renderTrxXferAccount) {
		this.renderTrxXferAccount = renderTrxXferAccount;
	}

	public boolean isRenderTrxCashDescription() {
		return renderTrxCashDescription;
	}

	public void setRenderTrxCashDescription(boolean renderTrxCashDescription) {
		this.renderTrxCashDescription = renderTrxCashDescription;
	}

	public boolean isRenderTrxLastOfBillingCycle() {
		return renderTrxLastOfBillingCycle;
	}

	public void setRenderTrxLastOfBillingCycle(boolean renderTrxLastOfBillingCycle) {
		this.renderTrxLastOfBillingCycle = renderTrxLastOfBillingCycle;
	}

	public List<SelectItem> getWdCategoriesList() 
	{
		setWdCategoriesList(transactionDAO.getWdCategoryDropdownList());
		return wdCategoriesList;
	}

	public void setWdCategoriesList(List<SelectItem> wdCategoriesList) {
		this.wdCategoriesList = wdCategoriesList;
	}

	public boolean isRenderTrxInvestmentType() {
		return renderTrxInvestmentType;
	}

	public void setRenderTrxInvestmentType(boolean renderTrxInvestmentType) {
		this.renderTrxInvestmentType = renderTrxInvestmentType;
	}

	public boolean isRenderTrxInvestment() {
		return renderTrxInvestment;
	}

	public void setRenderTrxInvestment(boolean renderTrxInvestment) {
		this.renderTrxInvestment = renderTrxInvestment;
	}

	public List<SelectItem> getInvestmentTypeList() {
		return investmentTypeList;
	}

	public void setInvestmentTypeList(List<SelectItem> investmentTypeList) {
		this.investmentTypeList = investmentTypeList;
	}

	public boolean isOwnedInvestments() {
		return ownedInvestments;
	}

	public void setOwnedInvestments(boolean ownedInvestments) {
		this.ownedInvestments = ownedInvestments;
	}

	public List<SelectItem> getInvestmentList() {
		return investmentList;
	}

	public void setInvestmentList(List<SelectItem> investmentList) {
		this.investmentList = investmentList;
	}

	public boolean isRenderOwnedInvestmentsCheckbox() {
		return renderOwnedInvestmentsCheckbox;
	}

	public void setRenderOwnedInvestmentsCheckbox(boolean renderOwnedInvestmentsCheckbox) {
		this.renderOwnedInvestmentsCheckbox = renderOwnedInvestmentsCheckbox;
	}

}
