package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.pas.pojo.AccountPosition;
import com.pas.pojo.CapitalGain;
import com.pas.slomfin.constants.AppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.PortfolioHistoryDAO;
import com.pas.slomfin.dao.TransactionDAO;
import com.pas.slomfin.dao.PaydayDAO;

import com.pas.util.SlomFinUtil;
import com.pas.util.SortSelectItemList;
import com.pas.util.TransactionTypeComparator;
import com.pas.util.CapitalGainComparator;
import com.pas.util.InvestmentComparator;
import com.pas.util.RetrieveStockQuotesService;

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
	
	private String trxSearchTerm;
	
	private String investmentAcidSetting;
	private Investment selectedInvestment;
	private boolean renderInvestmentUpdateFields;
	
	private String localePattern;
	
	private String transactionAcidSetting;
	private DynamoTransaction selectedTransaction;
	
	private String accountAcidSetting;
	private Account selectedAccount;
	private boolean renderAccountUpdateFields = false;
	
	private String paydayAcidSetting;
	private Payday selectedPayday;
	
	private Integer selectedInvestmentID;
	private Integer selectedAccountID;
	
	private boolean renderTransactionUpdateFields = false;
	private boolean renderTransactionId = false;	
	
	private boolean renderTrxAddAnother = false;
	private boolean renderTrxPostedDate = false;
	private boolean renderTrxAmount = true;
	private boolean renderTrxUnits = false;
	private boolean renderTrxPrice = false;
	private boolean renderTrxWDCategoriesList = false;
	private boolean renderTrxCheckNumber = false;
	private boolean renderTrxDividendTaxableYear = false;
	private boolean renderTrxCashDepositType = false;
	private boolean renderTrxXferAccount = false;
	private boolean generateCorrespondingXfer = false;
	private boolean renderTrxCashDescription = true;
	private boolean renderTrxLastOfBillingCycle = false;
	private boolean renderTrxInvestmentType = false;
	private boolean renderTrxInvestment = false;	
	private boolean renderOwnedInvestmentsCheckbox = false;
	
	private boolean ownedInvestments = false;
	
	private List<SelectItem> accountDropdownList = new ArrayList<>();
	private List<SelectItem> trxTypeDropdownList = new ArrayList<>();
	private List<SelectItem> investmentTypeDropdownList = new ArrayList<>();
	private List<SelectItem> investmentDropdownList = new ArrayList<>();
	private List<SelectItem> wdCategoriesDropdownList = new ArrayList<>();
	private List<SelectItem> xferAccountsDropdownList = new ArrayList<>();
	private List<SelectItem> accountTypesDropdownList = new ArrayList<>();
	private List<SelectItem> assetClassesDropdownList = new ArrayList<>();
	
	private String operation;
	
	private TransactionDAO transactionDAO;
	private InvestmentDAO investmentDAO;
	private AccountDAO accountDAO;
	private PortfolioHistoryDAO portfolioHistoryDAO;
	private PaydayDAO paydayDAO;
	
	private List<DynamoTransaction> trxList = new ArrayList<>();
	private List<Investment> investmentsList = new ArrayList<>();	
	private List<Payday> paydayList = new ArrayList<>();
	private List<Investment> reportUnitsOwnedList = new ArrayList<>();
	private List<PortfolioHistory> portfolioHistoryList = new ArrayList<>();
	private List<Account> activeAccountsList = new ArrayList<>();
	private List<AccountPosition> accountPositionsList = new ArrayList<>();
	
	private BigDecimal portfolioValue;
	private BigDecimal taxableValue;
	private BigDecimal retirementValue;
	private BigDecimal unitsTotal;
	
	private List<String> reportYearsList = new ArrayList<>();
	private List<String> reportDividendYearsList = new ArrayList<>();
	private List<DynamoTransaction> dividendTransactionsList = new ArrayList<>();
	private String reportDividendsTitle;
	private BigDecimal dividendsTotal;
	
	private List<CapitalGain> capitalGainsTransactionsList = new ArrayList<>();
	private String reportCapitalGainsTitle;
	private BigDecimal capitalGainsTotal = new BigDecimal(0.0);
	private String capitalGainsTotalStyleClass;
	
	private Integer citiDoubleCashAccountID;
	private Integer checkingAccountID;
	
	private String trxListTitle;
	
	public void onStart(@Observes @Initialized(ApplicationScoped.class) Object pointless) 
	{
		logger.info("Entering SlomFinMain onStart method.  Should only be here ONE time");
		logger.info("SlomFinMain id is: " + this.getId());
		this.setSiteTitle("Slomkowski Financial");
		
		this.setAccountTypesDropdownList(SlomFinUtil.getAccountTypesDropdownList());
		this.setAssetClassesDropdownList(SlomFinUtil.getAssetClassesDropdownList());
		this.setReportYearsList(SlomFinUtil.getRecentYearsList(false));
		this.setReportDividendYearsList(SlomFinUtil.getRecentYearsList(true));
		
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
				loadPaydays(dynamoClients);
				
				this.setCheckingAccountID(accountDAO.getCheckingAccountID());
				this.setCitiDoubleCashAccountID(accountDAO.getCitiDoubleCashAccountID());
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
	
	private void loadPaydays(DynamoClients dynamoClients) throws Exception 
	{
		logger.info("entering loadPaydays");
		paydayDAO = new PaydayDAO(dynamoClients);
		paydayDAO.readPaydaysFromDB();
		logger.info("Paydays read in. List size = " + portfolioHistoryDAO.getFullPortfolioHistoryList().size());
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
		
		Account acct = accountDAO.getAccountByAccountID(accountID);
		
		if (acct.getbClosed())
		{
			this.setTrxList(new ArrayList<>(transactionDAO.getFullTransactionsMapByAccountID().get(accountID)));
		}
		else
	    {
			this.setTrxList(new ArrayList<>(transactionDAO.getLast2YearsTransactionsMapByAccountID().get(accountID)));
	    }
	    
	    if (this.getTrxList() != null && this.getTrxList().size() > 0)
    	{
	    	DynamoTransaction trx = this.getTrxList().get(0);
    		logger.info("account Name:  " + trx.getAccountName() + " selected to show trx from menu");
    		this.setTrxListTitle("Transaction List for Account " + trx.getAccountName());
    	}
	}
		
	private void refreshTrxList(String searchTerm)
	{
		this.getTrxList().clear();		
		this.setTrxList(transactionDAO.searchTransactions(searchTerm));
	}
	
	private void refreshTrxList(Integer selectedInvestmentID, Integer selectedAccountID) 
	{		
		this.getTrxList().clear();
		
		List<DynamoTransaction> trxListByAccount = new ArrayList<>(transactionDAO.getFullTransactionsMapByAccountID().get(selectedAccountID));
		
		for (int i = 0; i < trxListByAccount.size(); i++)
		{
			DynamoTransaction trx = trxListByAccount.get(i);
			
			/*
			if (trx.getInvestmentID().intValue() == 157)
			{
				logger.info("investment id is 157");
			}
			*/
			
			if (trx.getInvestmentID().intValue() == selectedInvestmentID.intValue())
			{
				this.getTrxList().add(trx);
			}
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
	
	public String searchTrxDescriptions() 
	{
		try 
        {		    
		    logger.info("search requested for: " + this.getTrxSearchTerm());
		    
		    refreshTrxList(this.getTrxSearchTerm());
		    
            logger.info("successfully searched.  Found " + this.getTrxList().size() + " items");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
		return "/transactionList.xhtml";
	}  	
	
	public void accountAdd(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    
		    logger.info("Open New Account selected from menu");

		    this.setSelectedAccount(new Account());
		    this.setRenderAccountUpdateFields(true);
		    
		    setAccountAcidSetting("Add");
		    
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
	
	public void accountUpdate(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String accountid = ec.getRequestParameterMap().get("accountid");		    
		    logger.info("account ID " + accountid + " selected for update from menu");

		    this.setSelectedAccount(accountDAO.getAccountByAccountID(Integer.parseInt(accountid)));
		    this.setRenderAccountUpdateFields(true);
		    setAccountAcidSetting("Update");
		    
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
		
	public void reportAccountPositions(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Account Positions report selected from menu");
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    

		    boolean doSubtotals = true;
		    calculateAccountPositions(doSubtotals);	    
			
            String targetURL = SlomFinUtil.getContextRoot() + "/reportAccountPositions.xhtml";
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
	
	private void calculateAccountPositions(boolean doSubtotals) 
	{
		this.setActiveAccountsList(new ArrayList<>(this.getActiveTaxableAccountsList()));
		this.getActiveAccountsList().addAll(this.getActiveRetirementAccountsList());
		
		Map<Integer, List<Investment>> activeAccountsMap = SlomFinUtil.getActiveAccountValues(this.getActiveAccountsList(), transactionDAO.getFullTransactionsList(), investmentDAO.getFullInvestmentsMap(), getCashInvestmentID()); 
		
		this.getAccountPositionsList().clear();
		this.setPortfolioValue(new BigDecimal(0.0));				
		this.setTaxableValue(new BigDecimal(0.0));		
		this.setRetirementValue(new BigDecimal(0.0));		
		
		for (Integer accountID : activeAccountsMap.keySet()) 
		{			    
			Account acct = accountDAO.getAccountByAccountID(accountID);					
					
			List<Investment> investmentList = activeAccountsMap.get(accountID);
            
			BigDecimal tempTotal = new BigDecimal(0.0);
			BigDecimal accountSubTotal = new BigDecimal(0.0);
			
			for (int i = 0; i < investmentList.size(); i++) 
			{
				Investment inv = investmentList.get(i);
				
				AccountPosition acctPos = new AccountPosition();
				
				acctPos.setAccountID(accountID);
				
				if (doSubtotals)
				{
					acctPos.setAccountName(acct.getsAccountName());					   
				}
				else
				{
					if (i == 0)
					{
						acctPos.setAccountName(acct.getsAccountName());				
					}
					else
					{
						acctPos.setAccountName("");
					}
				}
				
				acctPos.setInvestmentID(inv.getiInvestmentID());
				acctPos.setInvestmentName(inv.getDescription());
				acctPos.setInvestmentPrice(inv.getCurrentPrice());
				acctPos.setUnitsOwned(inv.getUnitsOwned());
				acctPos.setTaxableAccount(acct.getbTaxableInd());
				
				if (inv.getiInvestmentID() == getCashInvestmentID())
				{					
					tempTotal = inv.getCurrentValue();			
				}
				else //units owned; need to multiply current price to get current value
				{
					tempTotal = inv.getUnitsOwned().multiply(inv.getCurrentPrice()); 
				}
				
				accountSubTotal = accountSubTotal.add(tempTotal);
				
				acctPos.setPositionValue(tempTotal);
				
				this.getAccountPositionsList().add(acctPos);
			}
			
			//this is to subtotal by account
			if (doSubtotals)
			{
				AccountPosition acctPos1 = new AccountPosition();
				this.getAccountPositionsList().add(acctPos1);
				
				AccountPosition acctPos2 = new AccountPosition();
				acctPos2.setAccountName(acct.getsAccountName());
				acctPos2.setInvestmentName("Total Account Value");
				acctPos2.setPositionValue(accountSubTotal);
				this.getAccountPositionsList().add(acctPos2);
				
				AccountPosition acctPos3 = new AccountPosition();
				this.getAccountPositionsList().add(acctPos3);	
			}
						
			portfolioValue = portfolioValue.add(accountSubTotal);
			
			if (acct.getbTaxableInd())
			{
				taxableValue = taxableValue.add(accountSubTotal);	
			}
			else
			{
				retirementValue = retirementValue.add(accountSubTotal);	
			}
        }
		
	}

	public void reportAccountSummary(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Account Summary report selected from menu");
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    

		    this.setActiveAccountsList(new ArrayList<>(this.getActiveTaxableAccountsList()));
			this.getActiveAccountsList().addAll(this.getActiveRetirementAccountsList());
			
			Map<Integer, List<Investment>> activeAccountsMap = SlomFinUtil.getActiveAccountValues(this.getActiveAccountsList(), transactionDAO.getFullTransactionsList(), investmentDAO.getFullInvestmentsMap(), getCashInvestmentID()); 
			
			this.getActiveAccountsList().clear();
			this.setPortfolioValue(new BigDecimal(0.0));
			
			for (Integer accountID : activeAccountsMap.keySet()) 
			{
				Account acct = accountDAO.getAccountByAccountID(accountID);
				
				List<Investment> investmentList = activeAccountsMap.get(accountID);
	            
				BigDecimal tempTotal = new BigDecimal(0.0);
				
				for (int i = 0; i < investmentList.size(); i++) 
				{
					Investment inv = investmentList.get(i);
					tempTotal = tempTotal.add(inv.getCurrentValue());
				}
				
				acct.setCurrentAccountValue(tempTotal);
				
				portfolioValue = portfolioValue.add(tempTotal);
				
				this.getActiveAccountsList().add(acct);
	        }
			
            String targetURL = SlomFinUtil.getContextRoot() + "/reportAccountSummary.xhtml";
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
	
	public void reportCapitalGains(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Capital gains report selected from menu");
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String yearSelection = ec.getRequestParameterMap().get("yearSel");		    
		    logger.info("Capital gains report selection: " + yearSelection);

		    Integer taxYear = Integer.parseInt(yearSelection);
		    
			this.setReportCapitalGainsTitle("Capital Gains: " + yearSelection);
			
			this.setCapitalGainsTransactionsList(getCapitalGains(taxYear));
			
			Collections.sort(this.getCapitalGainsTransactionsList(), new CapitalGainComparator());
			
            String targetURL = SlomFinUtil.getContextRoot() + "/reportCapitalGains.xhtml";
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
	
	public void reportCostBasis(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Cost Basis report selected from menu");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}
	
	public void reportDividends(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Dividends report selected from menu");
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String yearSelection = ec.getRequestParameterMap().get("yearSel");		    
		    logger.info("Dividend report selection: " + yearSelection);

		    Integer taxYear;
		    boolean taxableInd = false;
		    
		    if (yearSelection.contains(" "))
		    {
		    	String[] returnArray = yearSelection.split(" ");
		    	
		    	String strYear = returnArray[0];
		    	taxYear = Integer.parseInt(strYear);
		    	
				String taxableChoice = returnArray[1];
				if (taxableChoice.equalsIgnoreCase("Taxable"))
				{
					taxableInd = true;
				}
		    }
		    else
		    {
		    	taxYear = Integer.parseInt(yearSelection);
		    }
			this.setReportDividendsTitle("Dividends: " + yearSelection);
			
			this.setDividendTransactionsList(getDividends(taxYear, taxableInd));
			
            String targetURL = SlomFinUtil.getContextRoot() + "/reportDividends.xhtml";
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
	
	private List<DynamoTransaction> getDividends(Integer taxYear, boolean taxableInd) throws Exception 
	{
		List<DynamoTransaction> returnList = new ArrayList<>();
		
		this.setDividendsTotal(new BigDecimal(0.0));
		
		for (int i = 0; i < transactionDAO.getFullTransactionsList().size(); i++) 
		{
			DynamoTransaction trx = transactionDAO.getFullTransactionsList().get(i);
			
			if (trx.getTransactionTypeID() != null 
			&& (trx.getTransactionTypeID() == SlomFinUtil.CashDividend || trx.getTransactionTypeID() == SlomFinUtil.Reinvest))
			{	
				logger.debug("dividend trx id: " + trx.getTransactionID());
				
				if (trx.getDividendTaxableYear() != null && trx.getDividendTaxableYear().intValue() == taxYear.intValue())
				{
					if (taxableInd)
					{
						Account acct = accountDAO.getAccountByAccountID(trx.getAccountID());
						if (acct.getbTaxableInd())
						{
							returnList.add(trx);
							this.setDividendsTotal(this.getDividendsTotal().add(trx.getCostProceeds()));
						}
					}
					else //we want em all so just add 
					{
						returnList.add(trx);
						this.setDividendsTotal(this.getDividendsTotal().add(trx.getCostProceeds()));
					}
				}
				
			}
		}
		return returnList;
	}
	
	private List<CapitalGain> getCapitalGains(Integer taxYear) throws Exception 
	{
		this.setCapitalGainsTotal(new BigDecimal(0.0));			
		
		//establish sales transactions
		List<CapitalGain> capitalGainsTrxList = new ArrayList<>();
		List<CapitalGain> capitalGainsTrxListAdditions = new ArrayList<>();
		
		for (int i = 0; i < transactionDAO.getFullTransactionsList().size(); i++) 
		{
			DynamoTransaction trx = transactionDAO.getFullTransactionsList().get(i);
			
			Account acct = accountDAO.getFullAccountsMap().get(trx.getAccountID());
			
			if (trx.getTransactionTypeID() != null 
			&& (trx.getTransactionTypeID() == SlomFinUtil.Sell)
			&&  acct.getbTaxableInd())
			{	
				logger.debug("capital gain trx id: " + trx.getTransactionID());
				Calendar cal = Calendar.getInstance();
				cal.setTime(trx.getPostedDateJava());
				int trxyear = cal.get(Calendar.YEAR);
				
				if (trxyear == taxYear.intValue())
				{
					CapitalGain cg = new CapitalGain();
					
					cg.setSaleTransactionID(trx.getTransactionID());
					cg.setAccountID(trx.getAccountID());
					cg.setAccountName(trx.getAccountName());
					cg.setSalePrice(trx.getPrice());
					cg.setInvestmentID(trx.getInvestmentID());
					
					Investment inv = investmentDAO.getFullInvestmentsMap().get(trx.getInvestmentID());
					cg.setSaleInvestmentTypeID(inv.getiInvestmentTypeID());
					
					cg.setInvestmentName(trx.getInvestmentDescription());
					cg.setSaleDate(trx.getPostedDateJava());
					cg.setUnitsSold(trx.getUnits());
					cg.setSaleProceeds(trx.getCostProceeds());
					
					capitalGainsTrxList.add(cg);							
				}
				
			}
		}
		
		for (int i = 0; i < capitalGainsTrxList.size(); i++) 
		{			
			CapitalGain cg = capitalGainsTrxList.get(i);
			
			BigDecimal saleUnits = new BigDecimal(cg.getUnitsSold().toString());
			BigDecimal saleProceeds = new BigDecimal(cg.getSaleProceeds().toString());
			//BigDecimal salePrice = new BigDecimal(cg.getSalePrice().toString());
			
			logger.info("working on capital gain for trx id: " + cg.getSaleTransactionID() + " investment: " + cg.getInvestmentName() + " sale date: " + cg.getSaleDate());
			
			//for this sale, get all the transactions that have this investment id.  Must be from taxable accounts
			List<DynamoTransaction> investmentTransactionsList = transactionDAO.getAllTrxByInvestmentID(cg.getInvestmentID());
			
			//Remove any trx that are from retirement accounts; capital gains don't matter on these.
			List<DynamoTransaction> removalTrxList = new ArrayList<>();
			
			for (int j = 0; j < investmentTransactionsList.size(); j++) 
			{
				DynamoTransaction dt = investmentTransactionsList.get(j);
				Account acct = accountDAO.getAccountByAccountID(dt.getAccountID());
				if (!acct.getbTaxableInd())
				{
					removalTrxList.add(dt);
				}
			}
			
			investmentTransactionsList.removeAll(removalTrxList);			
			
		    //need to remove all transactions from investmenttransactionsList that have already been accounted for either in prior years or this year; 
			
		    //First task - identify the sales and units associated
		
			BigDecimal totalUnitsFromSales = new BigDecimal(0.0);
			totalUnitsFromSales = totalUnitsFromSales.setScale(4, java.math.RoundingMode.HALF_UP); 
			
			List<DynamoTransaction> salesToRemoveList = new ArrayList<>();
			
			logger.debug("removing Sales trx and tallying units from those sales.  Also removing cash dividends as those don't have units");
			
			for (int j = 0; j < investmentTransactionsList.size(); j++)
			{
				DynamoTransaction invTrx = investmentTransactionsList.get(j);
			
				Account acct = accountDAO.getAccountByAccountID(invTrx.getAccountID());
			
				if (invTrx.getTransactionTypeID() != null 
				&& (invTrx.getTransactionTypeID() == SlomFinUtil.CashDividend)
				&&  acct.getbTaxableInd())
				{
					salesToRemoveList.add(invTrx);
				}	
				
				if (invTrx.getTransactionTypeID() != null 
				&& (invTrx.getTransactionTypeID() == SlomFinUtil.Sell)
				&&  acct.getbTaxableInd())
				{
					if (invTrx.getPostedDateJava().getTime() < cg.getSaleDate().getTime())
				    {
					   totalUnitsFromSales = totalUnitsFromSales.add(invTrx.getUnits());
					   salesToRemoveList.add(invTrx);
					   logger.debug("found a sale to remove");
					   logger.debug("Trx number = " + (j+1));
					   logger.debug("Trx Date = " + invTrx.getPostedDateJava());
					   logger.debug("Trx Type = " + invTrx.getTransactionTypeDescription());
					   logger.debug("Units = " + invTrx.getUnits());
				    }
				}	
			}
		
			investmentTransactionsList.removeAll(salesToRemoveList);
			logger.info("total transactions for " + cg.getInvestmentName() + " after removing sales trx = " + investmentTransactionsList.size());
		
			if (totalUnitsFromSales.compareTo(new BigDecimal(0.0)) > 0) //if nothing to remove, skip this part
			{	
				BigDecimal unitsRemovalTally = new BigDecimal(0.0);		
				unitsRemovalTally = unitsRemovalTally.setScale(4, java.math.RoundingMode.HALF_UP); 
			
				List<DynamoTransaction> trxToRemoveList = new ArrayList<>();
				
				logger.debug("removing buy and reinvest trx");
				logger.debug("need to find " + totalUnitsFromSales + " units to remove");
				
				BigDecimal unitsAdjustment = new BigDecimal(0.0);
				BigDecimal newUnits =  new BigDecimal(0.0);
				
				for (int j = 0; j < investmentTransactionsList.size(); j++)
				{
					DynamoTransaction purchTrx = investmentTransactionsList.get(j);
					
					unitsRemovalTally = unitsRemovalTally.add(purchTrx.getUnits());
					
					logger.debug("Trx number = " + (j+1));
					logger.debug("Trx Date = " + purchTrx.getPostedDateJava());
					logger.debug("Trx Type = " + purchTrx.getTransactionTypeDescription());
					logger.debug("Units = " + purchTrx.getUnits());
					logger.debug("Units removal tally = " + unitsRemovalTally);	
					
					if (unitsRemovalTally.compareTo(totalUnitsFromSales) < 0) //simple removal, haven't reached total
					{
						trxToRemoveList.add(purchTrx);
					}
					else if (unitsRemovalTally.compareTo(totalUnitsFromSales) == 0) //remove and leave loop; reached total
					{
						trxToRemoveList.add(purchTrx);
						break;
					}
					else //adjust the list item 
					{
						unitsAdjustment = unitsAdjustment.subtract(totalUnitsFromSales);
						newUnits = unitsRemovalTally.subtract(totalUnitsFromSales);
						
						purchTrx.setUnits(newUnits);
						purchTrx.setCostProceeds(newUnits.multiply(purchTrx.getPrice()));
						
						investmentTransactionsList.set(j, purchTrx);
						
						logger.debug("new Units = " + purchTrx.getUnits());
						logger.debug("new Cost/Proceeds = " + purchTrx.getCostProceeds());				
						
						break;
					}		
					
				}
				
				if (trxToRemoveList.size() > 0)
				{
					investmentTransactionsList.removeAll(trxToRemoveList);
					logger.debug("total transactions for " + cg.getInvestmentName() + " after removing buy and reinvest trx = " + investmentTransactionsList.size());
				}
			}
			
			
			//finally execute loop to determine cap gains/losses
			BigDecimal unitsTally = new BigDecimal(0.0);
			BigDecimal costBasisTally = new BigDecimal(0.0);
			BigDecimal gainOrLoss = new BigDecimal(0.0);
			String holdingPeriod = "";
			
			long holdingPeriodDiffInDays = 0;
			
			for (int j = 0; j < investmentTransactionsList.size(); j++)
			{
				DynamoTransaction cgTrx = investmentTransactionsList.get(j);
								
				unitsTally = unitsTally.add(cgTrx.getUnits());
				
				holdingPeriodDiffInDays = SlomFinUtil.getDaysDifference(cg.getSaleDate(), cgTrx.getPostedDateJava());
				
				BigDecimal tempCostProceeds = new BigDecimal(0.0);
				
				if (cgTrx.getCostProceeds() != null)
				{
					tempCostProceeds = cgTrx.getCostProceeds();
				}
				
				String oldHoldingPeriod = holdingPeriod;
				holdingPeriod = SlomFinUtil.setHoldingPeriod(oldHoldingPeriod,holdingPeriodDiffInDays);
								
				if (unitsTally.compareTo(cg.getUnitsSold()) < 0) //haven't reached total
				{
					if (oldHoldingPeriod.contains(AppConstants.LONG_TERM) && holdingPeriod.contains(AppConstants.SHORT_TERM))
					{
						//This means you have to record the long-term row now, the short term one later.
						
						BigDecimal tempUnitsTally = unitsTally.subtract(cgTrx.getUnits()); 
						BigDecimal itemizedSaleProceeds = tempUnitsTally.multiply(cg.getSalePrice());
						gainOrLoss = itemizedSaleProceeds.subtract(costBasisTally);
						
						CapitalGain cg2 = new CapitalGain();
						
						cg2.setSaleTransactionID(cg.getSaleTransactionID());
						cg2.setAccountID(cg.getAccountID());
						cg2.setAccountName(cg.getAccountName());
						cg2.setSalePrice(cg.getSalePrice());
						cg2.setInvestmentID(cg.getInvestmentID());
						cg2.setSaleInvestmentTypeID(cg.getSaleInvestmentTypeID());
						cg2.setInvestmentName(cg.getInvestmentName());
						cg2.setSaleDate(cg.getSaleDate());
						cg2.setUnitsSold(tempUnitsTally);
						cg2.setSaleProceeds(itemizedSaleProceeds);
						cg2.setGainOrLoss(gainOrLoss);
						cg2.setHoldingPeriod(oldHoldingPeriod);
						cg2.setCostBasis(costBasisTally);
						
						if (gainOrLoss.compareTo(BigDecimal.ZERO) > 0)
						{
							cg2.setCgStyleClass(SlomFinUtil.GREEN_STYLECLASS);						
						}
						else if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0)
						{
							cg2.setCgStyleClass(SlomFinUtil.RED_STYLECLASS);						
						}
					
						capitalGainsTrxListAdditions.add(cg2);		
						
						capitalGainsTotal = capitalGainsTotal.add(gainOrLoss);		
						
						//reset these now for short-term row later..
						costBasisTally = tempCostProceeds; 
						unitsTally = cgTrx.getUnits();
						saleUnits = saleUnits.subtract(tempUnitsTally);
						saleProceeds = saleProceeds.subtract(itemizedSaleProceeds); 										
					}
					else //not transitioning, just keep going...
					{
						costBasisTally = costBasisTally.add(tempCostProceeds);
					}
					continue;
				}				
				else if (unitsTally.compareTo(cg.getUnitsSold()) == 0) //reached total exactly
				{				
					costBasisTally = costBasisTally.add(tempCostProceeds);
					gainOrLoss = saleProceeds.subtract(costBasisTally);
					
					cg.setGainOrLoss(gainOrLoss);
					cg.setHoldingPeriod(holdingPeriod);
					cg.setCostBasis(costBasisTally);
					
					if (gainOrLoss.compareTo(BigDecimal.ZERO) > 0)
					{
						cg.setCgStyleClass(SlomFinUtil.GREEN_STYLECLASS);						
					}
					else if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0)
					{
						cg.setCgStyleClass(SlomFinUtil.RED_STYLECLASS);						
					}
					
					capitalGainsTotal = capitalGainsTotal.add(gainOrLoss);				
				}
				else //you're over...take the part of the trx you need....
				{
					//how much did we need?
					unitsTally = unitsTally.subtract(cgTrx.getUnits()); //reset now that we know we're over
					BigDecimal neededUnits = saleUnits.subtract(unitsTally);
					BigDecimal partialCostBasis = neededUnits.multiply(cgTrx.getPrice());
					costBasisTally = costBasisTally.add(partialCostBasis);
					gainOrLoss = saleProceeds.subtract(costBasisTally);
					
					cg.setGainOrLoss(gainOrLoss);
					
					if (gainOrLoss.compareTo(BigDecimal.ZERO) > 0)
					{
						cg.setCgStyleClass(SlomFinUtil.GREEN_STYLECLASS);						
					}
					else if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0)
					{
						cg.setCgStyleClass(SlomFinUtil.RED_STYLECLASS);						
					}
					
					cg.setHoldingPeriod(holdingPeriod);
					cg.setCostBasis(costBasisTally);
					
					cg.setSaleProceeds(saleProceeds);
					cg.setUnitsSold(saleUnits);
					
					capitalGainsTotal = capitalGainsTotal.add(gainOrLoss);			
				}
				
				//jump out on real estate trx >= 2 years - no cap gains on those
				if (cg.getSaleInvestmentTypeID() == SlomFinUtil.REALESTATE
				&&  holdingPeriodDiffInDays >= AppConstants.CAPITAL_GAIN_LONG_TERM_REALESTATE)
				{	
					break;
				}
						
				break;
			}	        
		}
		
		if (capitalGainsTotal.compareTo(BigDecimal.ZERO) > 0)
		{
			this.setCapitalGainsTotalStyleClass(SlomFinUtil.GREEN_STYLECLASS);						
		}
		else if (capitalGainsTotal.compareTo(BigDecimal.ZERO) < 0)
		{
			this.setCapitalGainsTotalStyleClass(SlomFinUtil.RED_STYLECLASS);					
		}
		
		capitalGainsTrxList.addAll(capitalGainsTrxListAdditions);
		
		return capitalGainsTrxList;
	}
	
	public void reportGoals(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Goals report selected from menu");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}
	
	public void reportPortfolioHistory(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Portfolio History report selected from menu");
		    
		    portfolioHistoryDAO.sortFullPhListDateAsc();
		    
		    BigDecimal yearlyGainLossSubTotal = new BigDecimal(0.0);
			BigDecimal firstYearlyAmount = new BigDecimal(0.0);
			BigDecimal percentGainLoss = new BigDecimal(0.0);
			BigDecimal oneHundred = new BigDecimal(100.00);	
			
			oneHundred = oneHundred.setScale(4, java.math.RoundingMode.HALF_UP); 
			
			int previousYear = 1900;
			int phYear = 0;
			int i;
			
			for (i = 0; i < this.getPortfolioHistoryList().size(); i++) 
		    {
				PortfolioHistory ph = this.getPortfolioHistoryList().get(i);
				
				Calendar tempCal = Calendar.getInstance();
				
				tempCal.setTimeInMillis(ph.getHistoryDateJava().getTime());
								
				phYear = tempCal.get(Calendar.YEAR);
				
				if (phYear != previousYear) //set list items then reset total
				{
					if (i != 0) //don't want to do anything first time through
					{
						PortfolioHistory ph2 = this.getPortfolioHistoryList().get(i-1); //This is the previous entry
						yearlyGainLossSubTotal = yearlyGainLossSubTotal.add(ph2.getTotalValue().subtract(firstYearlyAmount));
						ph2.setThisYearsDollars(yearlyGainLossSubTotal);						
						percentGainLoss = percentGainLoss.multiply(yearlyGainLossSubTotal.divide(firstYearlyAmount,4,java.math.RoundingMode.HALF_UP));
						ph2.setThisYearsPercent(percentGainLoss);
						String pctStr = percentGainLoss.toPlainString();
						pctStr = pctStr.substring(0, 6) + "%";
						ph2.setThisYearsPercentDisplay(pctStr);
						ph2.setRenderThisYearsStuff(true);
						this.getPortfolioHistoryList().set(i-1, ph2);
					}
					
					firstYearlyAmount = firstYearlyAmount.subtract(firstYearlyAmount);
					firstYearlyAmount = firstYearlyAmount.add(ph.getTotalValue());
					yearlyGainLossSubTotal = yearlyGainLossSubTotal.subtract(yearlyGainLossSubTotal);
					percentGainLoss = percentGainLoss.subtract(percentGainLoss);
					percentGainLoss = percentGainLoss.add(oneHundred);
					previousYear = phYear;					
				}												
			}
			
			//this is for the last item in the list
			
			PortfolioHistory ph2 = this.getPortfolioHistoryList().get(i-1); //This is the previous entry
			yearlyGainLossSubTotal = yearlyGainLossSubTotal.add(ph2.getTotalValue().subtract(firstYearlyAmount));
			ph2.setThisYearsDollars(yearlyGainLossSubTotal);						
			percentGainLoss = percentGainLoss.multiply(yearlyGainLossSubTotal.divide(firstYearlyAmount,4,java.math.RoundingMode.HALF_UP));
			ph2.setThisYearsPercent(percentGainLoss);
			String pctStr = percentGainLoss.toPlainString();
			pctStr = pctStr.substring(0, 6) + "%";
			ph2.setThisYearsPercentDisplay(pctStr);
			ph2.setRenderThisYearsStuff(true);
			this.getPortfolioHistoryList().set(i-1, ph2);
					
		    portfolioHistoryDAO.sortFullPhListDateDesc();
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/reportPortfolioHistory.xhtml";
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
	
	public void reportPortfolioByAssetClass(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Portfolio By Asset Class report selected from menu");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}
	
	public void reportPortfolioSummary(ActionEvent event) 
	{
		//need to do total taxable, and total retirement, as well as do not list the account name in every row, only the first one
		try 
        {		    
		    logger.info("Portfolio Summary report selected from menu");
		 	    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    

		    boolean doSubtotals = false;
		    calculateAccountPositions(doSubtotals);	    
			
            String targetURL = SlomFinUtil.getContextRoot() + "/reportPortfolioSummary.xhtml";
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
	
	public void reportTrxByInvestment(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Trx By Investment report selected from menu");
		    
		    this.getInvestmentDropdownList().clear();
		    this.setInvestmentDropdownList(investmentDAO.getAllInvestmentsDropdown());
		    
		    this.getAccountDropdownList().clear();
		    this.setAccountDropdownList(accountDAO.getAllAccountsDropdown());
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/transactionByInvestmentSelection.xhtml";
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
	
	public void reportUnitsOwned(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("units owned report selected from menu");
		    
		    this.getReportUnitsOwnedList().clear();
		    
		    Map<Integer, BigDecimal> unitsOwnedMap = SlomFinUtil.getUnitsOwned(transactionDAO.getFullTransactionsList());
		    
		    for (Integer key : unitsOwnedMap.keySet()) 
			{
	            BigDecimal totalUnitsOwned = unitsOwnedMap.get(key);
	            
	            if (totalUnitsOwned != null
	        	&&  totalUnitsOwned.compareTo(BigDecimal.ZERO) != 0)
	            {
	            	Investment inv = investmentDAO.getInvestmentByInvestmentID(key);
	            	inv.setUnitsOwned(totalUnitsOwned);
	            	this.getReportUnitsOwnedList().add(inv);	            	
	            }
	        }
		    
		    Collections.sort(this.getReportUnitsOwnedList(), new InvestmentComparator());
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/reportUnitsOwned.xhtml";
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
	
	public void reportWdCategories(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("WD Categories report selected from menu");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}
	
	public void updateSecurityPrices(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("update Security prices selected from menu");
		    
		    this.getReportUnitsOwnedList().clear();
		    
		    Map<Integer, BigDecimal> unitsOwnedMap = SlomFinUtil.getUnitsOwned(transactionDAO.getFullTransactionsList());
		    
		    for (Integer key : unitsOwnedMap.keySet()) 
			{
	            BigDecimal totalUnitsOwned = unitsOwnedMap.get(key);
	            
	            if (totalUnitsOwned != null
	        	&&  totalUnitsOwned.compareTo(BigDecimal.ZERO) != 0)
	            {
	            	Investment inv = investmentDAO.getInvestmentByInvestmentID(key);
	            	inv.setUnitsOwned(totalUnitsOwned);
	            	this.getReportUnitsOwnedList().add(inv);	            	
	            }
	        }
		    
		    Collections.sort(this.getReportUnitsOwnedList(), new InvestmentComparator());
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/updateSecurityPrices.xhtml";
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
	
	public BigDecimal getTotalCashBalanceAllAccounts()
	{
		BigDecimal balance = new BigDecimal(0.0);
		
		for (int i = 0; i < transactionDAO.getFullTransactionsList().size(); i++) 
		{
			DynamoTransaction trx = transactionDAO.getFullTransactionsList().get(i);
			Account acct = accountDAO.getAccountByAccountID(trx.getAccountID());
				
			if (!acct.getbClosed())
			{
				if (trx.getCostProceeds() != null)
				{
					balance = SlomFinUtil.transactAnAmount(trx, balance, "amount");						
				}
			}
		}
		return balance;
	}
	
	public String saveInvestmentPrices()
	{
		BigDecimal portfolioHistoryBalance = new BigDecimal(0.0);
		
		try 
		{
			for (int i = 0; i < this.getReportUnitsOwnedList().size(); i++) 
			{
				Investment inv = this.getReportUnitsOwnedList().get(i);
				investmentDAO.updateInvestment(inv);	
				BigDecimal investmentValue = inv.getCurrentPrice().multiply(inv.getUnitsOwned());
				portfolioHistoryBalance = portfolioHistoryBalance.add(investmentValue);
				logger.info("calcing portfolio history... investment: " + inv.getDescription() + " value: " + investmentValue + " new porthistory total: " + portfolioHistoryBalance);
			}
			
			BigDecimal cashBalances = getTotalCashBalanceAllAccounts();			
			portfolioHistoryBalance = portfolioHistoryBalance.add(cashBalances);
			logger.info("calcing portfolio history... cash balances in all accounts: " + cashBalances + " new porthistory total: " + portfolioHistoryBalance);
			
			PortfolioHistory ph = new PortfolioHistory();
			ph.setHistoryDate(DateToStringConverter.convertDateToDynamoStringFormat(new Date()));
			ph.setTotalValue(portfolioHistoryBalance);
			portfolioHistoryDAO.addPortfolioHistory(ph);
			
			portfolioHistoryDAO.sortFullPhListDateDesc();
			
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Investment prices successfully updated", "Investment prices successfully updated");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		} 
		catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }			
		
		return "/reportPortfolioHistory.xhtml";
	}
	
	public String getStockQuotes()
	{
		RetrieveStockQuotesService rqs = new RetrieveStockQuotesService();
		
		try 
		{
			String rqsReturn = rqs.getMarketCloseDate();
			
			String[] rqsReturnArray = rqsReturn.split("~");
			String strAttempts = rqsReturnArray[0];
			int stocksCounter = Integer.parseInt(strAttempts);
			String marketCloseDate = rqsReturnArray[1];
			
			for (int i = 0; i < this.getReportUnitsOwnedList().size(); i++) 
			{
				Investment inv = this.getReportUnitsOwnedList().get(i);
				
				if (inv.getInvestmentTypeDescription().equalsIgnoreCase("Stock"))
				{
					stocksCounter++;
					
					logger.info("quoting stock: " + inv.getTickerSymbol() + " - count = " + stocksCounter);
					BigDecimal stockprice = rqs.getStockQuote(inv.getTickerSymbol(), marketCloseDate);
					inv.setCurrentPrice(stockprice);
					
					//can only do 5 api calls per minute (under their free plan) so need to sleep for a bit before resuming this...
					if (stocksCounter % 5 == 0)
					{
						TimeUnit.SECONDS.sleep(70);					
					}
				}
			}
			
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock quotes successful", "Stock quotes successful");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		} 
		catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }			
		
		return "";
	}
	
	public void showInvestmentsList(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("investments List selected from menu");
		    
		    this.setInvestmentsList(investmentDAO.getFullInvestmentsList());
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
            String targetURL = SlomFinUtil.getContextRoot() + "/investmentList.xhtml";
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
	
	public List<String> autoCompleteTrxDescription(String searchTerm) 
	{
        List<String> returnList = new ArrayList<>();
        List<DynamoTransaction> searchList = transactionDAO.searchTransactions(searchTerm);
        
        Map<String, String> tempMap = new HashMap<>();
        for (int i = 0; i < searchList.size(); i++) 
        {
        	DynamoTransaction trx = searchList.get(i);
        	if (!tempMap.containsKey(trx.getTransactionDescription()))
			{
        		tempMap.put(trx.getTransactionDescription(), searchTerm);
        		returnList.add(trx.getTransactionDescription());
			}
		}
        
        Collections.sort(returnList);
        
        return returnList;
    }

	public void addPaycheckShowForm(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Add new paycheck selected from menu");
		    
		    this.setPaydayList(paydayDAO.getFullPaydaysList());
		    
		    for (int i = 0; i < this.getPaydayList().size(); i++) 
		    {
				Payday pd = this.getPaydayList().get(i);

				Calendar cal = Calendar.getInstance();
				
				if (pd.getDefaultDay() == 0) //if not on a specific day (indicated by zero) then assume you always want it.
				{
					pd.setPdRowStyleClass(SlomFinUtil.GREEN_STYLECLASS);
					pd.setProcessInd(true);
					Date tempDate = cal.getTime();
					pd.setPaydayTrxDate(tempDate);
				}
				else //specific date requested
				{
					pd.setPdRowStyleClass(SlomFinUtil.RED_STYLECLASS);
					pd.setProcessInd(false);
					
					if (pd.getNextMonthInd())
					{
						cal.add(Calendar.MONTH, 1);
						cal.set(Calendar.DAY_OF_MONTH, pd.getDefaultDay());
					    Date tempDate = cal.getTime();
						pd.setPaydayTrxDate(tempDate);
					}
					else
					{
						cal.set(Calendar.DAY_OF_MONTH, pd.getDefaultDay());
					    Date tempDate = cal.getTime();
						pd.setPaydayTrxDate(tempDate);
					}
				}
				
				if (pd.getXferAccountID() != null && pd.getXferAccountID() > 0)
				{
					Account xferAccount = accountDAO.getAccountByAccountID(pd.getXferAccountID());
					pd.setXferAccountName(xferAccount.getsAccountName());
				}
			}
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
            String targetURL = SlomFinUtil.getContextRoot() + "/addPaycheck.xhtml";
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
	
	public String addPaycheck()
	{
		logger.info("entering addPaycheck");
		
		boolean somethingFailed = false;
		
		try
		{	
			transactionAcidSetting = "Add";
			this.setGenerateCorrespondingXfer(true);
			
			for (int i = 0; i < this.getPaydayList().size(); i++) 
			{
				Payday pd = this.getPaydayList().get(i);
				if (pd.getProcessInd())
				{
					logger.info("processing selected for transaction: " + pd.getPaydayDescription() + " for amount: " + pd.getDefaultAmount());
					DynamoTransaction dynamoTrx = new DynamoTransaction();
					dynamoTrx.setAccountID(pd.getAccountID());
			    	Account acct = getAccountByAccountID(dynamoTrx.getAccountID());
			    	dynamoTrx.setEntryDateJava(new Date());
			    	dynamoTrx.setPostedDateJava(pd.getPaydayTrxDate());
			    	dynamoTrx.setAccountName(acct.getsAccountName());
			    	dynamoTrx.setTransactionTypeID(pd.getTransactionTypeID());
			    	dynamoTrx.setTransactionTypeDescription(SlomFinUtil.trxTypesMap.get(dynamoTrx.getTransactionTypeID()));		
			    	dynamoTrx.setCostProceeds(pd.getDefaultAmount());
			    	dynamoTrx.setTransactionDescription(pd.getPaydayDescription());
			    	
			    	if (dynamoTrx.getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strTransferOut))
					{
			    		dynamoTrx.setTransferAccountID(pd.getXferAccountID());
					}
			    	
			    	this.setSelectedTransaction(dynamoTrx);	
			    	
			    	String result = prepTrxAndUpsertDatabase();
					
			    	if (result.equalsIgnoreCase("fail"))
			    	{
			    		somethingFailed = true;
			    	}
			    	
				}
				
			}
						
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Paycheck transactions successfully added", "Paycheck transactions successfully added");
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	
		}
		catch (Exception e)
		{
        	logger.error("addPaycheck errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
				
		if (somethingFailed)
		{
			return "";	
		}
		else
		{
			refreshTrxList(accountDAO.getCheckingAccountID());
			return "/transactionList.xhtml";
		}
	}
	
	public void showPaydayList(ActionEvent event) 
	{
		try 
        {		    
		    logger.info("Payday List selected from menu");
		    
		    this.setPaydayList(paydayDAO.getFullPaydaysList());
		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
            String targetURL = SlomFinUtil.getContextRoot() + "/paydayList.xhtml";
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
	
	public void paydayRowChecked(AjaxBehaviorEvent event) 
	{
		logger.info("payday row was either checked or unchecked");
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
				this.setInvestmentDropdownList(setOwnedInvestmentsDropdown(this.getSelectedTransaction().getInvestmentTypeID()));				
            }
			else
			{
				logger.info("user un-checked that they want only owned investments in the dropdown");
				this.setInvestmentDropdownList(investmentDAO.getInvestmentsByInvestmentTypeID(this.getSelectedTransaction().getInvestmentTypeID()));
			}
			
			SortSelectItemList.sortSelectItemList(this.getInvestmentDropdownList(), "label", true);
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
		Map<Integer, BigDecimal> unitsOwnedForAccountMap = SlomFinUtil.getUnitsOwned(accountTrxList); 
		
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
			
			if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferIn)
			||  selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferOut))
			{
				this.setXferAccountsDropdownList(accountDAO.getXferAccountsDropdown(this.getSelectedTransaction().getAccountID()));
			}
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
			
			if (selectedInvTypeDesc.equalsIgnoreCase("Cash"))
			{
				setRenderTrxInvestment(false);
				setRenderTrxUnits(false);
				setRenderTrxAmount(true);
			}
			else //not cash
			{
				setRenderTrxInvestment(true);
				setRenderTrxUnits(true);
				
				if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strCashDividend))
				{
					setRenderTrxInvestment(true);
					setRenderTrxUnits(false);
					setRenderTrxAmount(true);
				}
				else if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strBuy)
				||	this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strReinvest)
				||	this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strSell))
				{
					setRenderTrxAmount(true);
				}
				else
				{
					setRenderTrxAmount(false);
				}
				
				if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strBuy))
				{
					this.setInvestmentDropdownList(investmentDAO.getInvestmentsByInvestmentTypeID(this.getSelectedTransaction().getInvestmentTypeID()));
				}
				else
				{
					this.setInvestmentDropdownList(setOwnedInvestmentsDropdown(this.getSelectedTransaction().getInvestmentTypeID()));		
				}		
			}
			
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(false);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(false);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(true);
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
			setRenderTrxPostedDate(false);
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
			setRenderTrxPostedDate(false);
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
			setRenderTrxPostedDate(false);
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
			setGenerateCorrespondingXfer(false);
			setRenderTrxPostedDate(false);
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
			setRenderTrxInvestment(false);
			setRenderOwnedInvestmentsCheckbox(false);
			setGenerateCorrespondingXfer(true);
			setRenderTrxPostedDate(false);
		}
		
	}

	public String returnToMain()
	{
		return "/main.xhtml";
	}
	
	public String returnToTrxList()
	{
		return "/transactionList.xhtml";
	}
	
	public String returnToPaydayList()
	{
		this.setPaydayList(paydayDAO.getFullPaydaysList());
		return "/paydayList.xhtml";
	}
	
	public String returnToInvestmentsList()
	{
		this.setInvestmentsList(investmentDAO.getFullInvestmentsList());
		return "/investmentList.xhtml";
	}
	
	public String selectInvestmentAcid()
	{		
		try 
        {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    String acid = ec.getRequestParameterMap().get("operation");
		    String investmentId = ec.getRequestParameterMap().get("InvestmentId");
		    
		    this.setInvestmentAcidSetting(acid);
		    
		    logger.info("Investment operation setup for add-change-inquire-delete.  Function is: " + acid);
		     
		    if (acid.equalsIgnoreCase("add"))
		    {
		    	Investment investment = new Investment();
		    	this.setSelectedInvestment(investment);	
		    }
		    else //go get the existing 
		    {
		    	this.setSelectedInvestment(this.getInvestmentByInvestmentID(Integer.parseInt(investmentId)));
		    }
		 	
		    if (acid.equalsIgnoreCase("view")
		    ||  acid.equalsIgnoreCase("delete"))
		    {
				this.getSelectedInvestment().setInvestmentTypeDescription(SlomFinUtil.invTypesMap.get(this.getSelectedInvestment().getiInvestmentTypeID()));
				this.getSelectedInvestment().setAssetClass(SlomFinUtil.assetClassesMap.get(this.getSelectedInvestment().getAssetClassID()));		
	
		    	this.setRenderInvestmentUpdateFields(false);
		    }
		    else
		    {
		    	this.getInvestmentTypeDropdownList().clear();
			    this.setInvestmentTypeDropdownList(SlomFinUtil.getInvestmentTypesDropdownList());
			    	
			    this.getAssetClassesDropdownList().clear();
			    this.setAssetClassesDropdownList(SlomFinUtil.getAssetClassesDropdownList());
			 
		    	this.setRenderInvestmentUpdateFields(true);
		    }
		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/investmentAddUpdate.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL + " with operation: " + acid);
					    
        } 
        catch (Exception e) 
        {
        	logger.error("selectInvestmentAcid errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
		
		return "";		
	}	
	
	public void investmentAdd(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    
		    logger.info("Add New Investment selected from menu");

		    this.setSelectedInvestment(new Investment());
		    this.setRenderInvestmentUpdateFields(true);
		    setInvestmentAcidSetting("Add");
		    
		    this.getInvestmentTypeDropdownList().clear();
	    	this.setInvestmentTypeDropdownList(SlomFinUtil.getInvestmentTypesDropdownList());
	    	
	    	this.getAssetClassesDropdownList().clear();
	    	this.setAssetClassesDropdownList(SlomFinUtil.getAssetClassesDropdownList());
	    	
            String targetURL = SlomFinUtil.getContextRoot() + "/investmentAddUpdate.xhtml";
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
		    
		    this.setRenderTrxAddAnother(false);
		    
		    if (acid.equalsIgnoreCase("add"))
		    {
		    	DynamoTransaction dynamoTrx = new DynamoTransaction();
		    	dynamoTrx.setAccountID(Integer.parseInt(accountId));
		    	Account acct = getAccountByAccountID(dynamoTrx.getAccountID());
		    	dynamoTrx.setEntryDateJava(new Date());
		    	dynamoTrx.setPostedDateJava(SlomFinUtil.getTwoDaysFromNowDate());
		    	dynamoTrx.setAccountName(acct.getsAccountName());
		    	
		    	String accountType = SlomFinUtil.accountTypesMap.get(acct.getiAccountTypeID());
		    	
		    	if (accountDAO.getCitiDoubleCashAccountID() == Integer.parseInt(accountId))
		    	{
		    		this.setRenderTrxAddAnother(true);
		    	}
		    	
		    	if (accountType != null 
		    	&& (accountType.equalsIgnoreCase(SlomFinUtil.strCreditCard) || accountType.equalsIgnoreCase(SlomFinUtil.strChecking)))
		    	{
		    		dynamoTrx.setTransactionTypeID(SlomFinUtil.CashWithdrawal);
		    		dynamoTrx.setTransactionTypeDescription(SlomFinUtil.strCashWithdrawal);		    			
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
		    	this.getTrxTypeDropdownList().clear();
		    	this.setTrxTypeDropdownList(SlomFinUtil.getValidTrxTypesForAccountTypes(acct.getiAccountTypeID()));
		    	
		    	Collections.sort(this.getTrxTypeDropdownList(), new TransactionTypeComparator());
		    	
		    	this.getInvestmentTypeDropdownList().clear();
		    	this.setInvestmentTypeDropdownList(SlomFinUtil.getValidInvTypesForAccountTypes(acct.getiAccountTypeID()));
		    	
		    	if (this.getSelectedTransaction().getTransactionTypeDescription() != null)
		    	{
		    		enableTrxUpdateFields(this.getSelectedTransaction().getTransactionTypeDescription());	
		    	}
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
	
	public String selectPaydayAcid()
	{		
		try 
        {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    String acid = ec.getRequestParameterMap().get("operation");
		    String paydayId = ec.getRequestParameterMap().get("paydayId");
		    
		    this.setPaydayAcidSetting(acid);
		    
		    logger.info("Payday operation setup for add-change-inquire-delete.  Function is: " + acid);
		     
		    if (acid.equalsIgnoreCase("add"))
		    {
		    	Payday pd = new Payday();
		    	this.setSelectedPayday(pd);	
		    }
		    else //go get the existing trx
		    {
		    	this.setSelectedPayday(this.getPaydayByPaydayID(Integer.parseInt(paydayId)));
		    }
		    
		    this.getTrxTypeDropdownList().clear();
	    	this.setTrxTypeDropdownList(SlomFinUtil.getAllTrxTypesTypesDropdownList());	    	
	    	Collections.sort(this.getTrxTypeDropdownList(), new TransactionTypeComparator());
	    	
		    this.setAccountDropdownList(accountDAO.getXferAccountsDropdown(0));
		    this.setXferAccountsDropdownList(accountDAO.getXferAccountsDropdown(0));
		    
		    String targetURL = SlomFinUtil.getContextRoot() + "/paydayAddUpdate.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL + " with operation: " + acid);
					    
        } 
        catch (Exception e) 
        {
        	logger.error("selectPaydayAcid errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
		
		return "";		
	}	
	
	private Payday getPaydayByPaydayID(int paydayID) 
	{
		return paydayDAO.getPaydayByPaydayID(paydayID);
	}

	private Account getAccountByAccountID(Integer accountID) 
	{
		return accountDAO.getAccountByAccountID(accountID);
	}
	
	private Investment getInvestmentByInvestmentID(Integer investmentID) 
	{
		return investmentDAO.getInvestmentByInvestmentID(investmentID);
	}

	public String addChangeDelInvestment()
	{
		logger.info("entering addChangeDelInvestment for operation: " + investmentAcidSetting);
		
		String result = prepInvestmentAndUpsertDatabase();
		
		if (result.equalsIgnoreCase("success"))
		{
			this.setInvestmentsList(investmentDAO.getFullInvestmentsList());
			return "/investmentList.xhtml";	
		}
		else
		{
			return "";
		}
	}	
	
	private String prepInvestmentAndUpsertDatabase() 
	{
		String returnString = "";
	
		try
		{
			if (!investmentAcidSetting.equalsIgnoreCase("Delete"))
			{
		
				if (this.getSelectedInvestment().getiInvestmentTypeID() == -1)
				{
					FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Investment Type not selected", "Investment Type not selected");
				 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
				 	return "";
				}
				
				this.getSelectedInvestment().setInvestmentTypeDescription(SlomFinUtil.invTypesMap.get(this.getSelectedInvestment().getiInvestmentTypeID()));
				this.getSelectedInvestment().setAssetClass(SlomFinUtil.assetClassesMap.get(this.getSelectedInvestment().getAssetClassID()));		
			}				
				
			if (investmentAcidSetting.equalsIgnoreCase("Add"))
			{
				investmentDAO.addInvestment(this.getSelectedInvestment());
			}
			else if (investmentAcidSetting.equalsIgnoreCase("Update"))
			{
				investmentDAO.updateInvestment(this.getSelectedInvestment());
			}
			else if (investmentAcidSetting.equalsIgnoreCase("Delete"))
			{
				investmentDAO.deleteInvestment(this.getSelectedInvestment());
			}
				
			returnString = "success";
		}
		catch (Exception e)
		{
			logger.error("upsert transaction(s) errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	returnString = "fail";
		}

		return returnString;		
	}
	
	public String addChangeDelTransaction()
	{
		logger.info("entering addChangeDelTransaction for operation: " + transactionAcidSetting);
		
		String result = prepTrxAndUpsertDatabase();
		
		if (result.equalsIgnoreCase("success"))
		{
			return "/transactionList.xhtml";	
		}
		else
		{
			return "";
		}
	}	
	
	public String addThenAddAnother()
	{
		logger.info("entering addThenAddAnother for operation: " + transactionAcidSetting);
		
		String result = prepTrxAndUpsertDatabase();
		
		if (result.equalsIgnoreCase("success"))
		{
			return selectTransactionAcid();
		}
		else
		{
			return "";
		}
	}	
	
	private String prepTrxAndUpsertDatabase() 
	{
		String returnString = "";
	
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
				
				Calendar calNow = Calendar.getInstance();
				Calendar calEntered = Calendar.getInstance();
					
				calEntered.setTime(this.getSelectedTransaction().getPostedDateJava());
				calEntered.set(Calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR_OF_DAY));
				calEntered.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE));
				calEntered.set(Calendar.SECOND, calNow.get(Calendar.SECOND));
					
				this.getSelectedTransaction().setTransactionPostedDate(DateToStringConverter.convertDateToDynamoStringFormat(calEntered.getTime()));
								
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
				}
				
				if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strTransferOut))
				{					
					if (this.isGenerateCorrespondingXfer())
					{
						DynamoTransaction correspondingTrx = new DynamoTransaction(this.getSelectedTransaction());
						correspondingTrx.setAccountID(this.getSelectedTransaction().getTransferAccountID());
						Account xferAcct = accountDAO.getAccountByAccountID(this.getSelectedTransaction().getTransferAccountID());
						correspondingTrx.setAccountName(xferAcct.getsAccountName());
						correspondingTrx.setTransactionTypeID(SlomFinUtil.TransferIn);
						correspondingTrx.setTransactionTypeDescription(SlomFinUtil.strTransferIn);
						correspondingTrx.setTrxTypPositiveInd(true);
						transactionDAO.addTransaction(correspondingTrx);
					}															
				}
				
				if (this.getSelectedTransaction().getTransactionTypeDescription().equalsIgnoreCase(SlomFinUtil.strTransferIn))
				{
					if (this.isGenerateCorrespondingXfer())
					{
						DynamoTransaction correspondingTrx = new DynamoTransaction(this.getSelectedTransaction());
						correspondingTrx.setAccountID(this.getSelectedTransaction().getTransferAccountID());
						Account xferAcct = accountDAO.getAccountByAccountID(this.getSelectedTransaction().getTransferAccountID());
						correspondingTrx.setAccountName(xferAcct.getsAccountName());
						correspondingTrx.setTransactionTypeID(SlomFinUtil.TransferOut);
						correspondingTrx.setTransactionTypeDescription(SlomFinUtil.strTransferOut);
						correspondingTrx.setTrxTypPositiveInd(false);
						transactionDAO.addTransaction(correspondingTrx);
					}					
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
			
			refreshTrxList(this.getSelectedTransaction().getAccountID());
			
			returnString = "success";
		}
		catch (Exception e)
		{
			logger.error("upsert transaction(s) errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	returnString = "fail";
		}

		return returnString;		
	}

	public String paydayDelete()
	{
		try
		{
			paydayDAO.deletePayday(this.getSelectedPayday());			
		}
		catch (Exception e)
		{
        	logger.error("paydayDelete errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
		
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Payday successfully deleted", "Payday successfully deleted");
	 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		
		return "";	
	}
	
	public String addChangeDelAccount()
	{
		logger.info("entering addChangeDelAccount for operation: " + accountAcidSetting);
		
		try
		{
			if (!accountAcidSetting.equalsIgnoreCase("Delete"))
			{
				if (this.getSelectedAccount().getiAccountTypeID() == -1)
				{
					FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account Type not selected", "Account Type not selected");
				 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
				 	return "";
				}
				else
				{
					this.getSelectedAccount().setsAccountType(SlomFinUtil.getAccountTypesMap().get(this.getSelectedAccount().getiAccountTypeID()));
				}
			}				
			
			if (accountAcidSetting.equalsIgnoreCase("Add"))
			{
				accountDAO.addAccount(this.getSelectedAccount());
			}
			else if (accountAcidSetting.equalsIgnoreCase("Update"))
			{
				accountDAO.updateAccount(this.getSelectedAccount());
			}
			else if (accountAcidSetting.equalsIgnoreCase("Delete"))
			{
				accountDAO.deleteAccount(this.getSelectedAccount());
			}
		}
		catch (Exception e)
		{
        	logger.error("addChangeDelAccount errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
		
		if (accountAcidSetting.equalsIgnoreCase("Add"))
		{
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account successfully added", "Account successfully added");
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		}
		else if (accountAcidSetting.equalsIgnoreCase("Update"))
		{
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account successfully updated", "Account successfully updated");
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		}
				
		return "";	
	}
	
	public String trxByInvestmentSelection()
	{
		try 
        {		    
		    logger.info("finding transactions by investment and account selection");
		    
		    refreshTrxList(this.getSelectedInvestmentID(), this.getSelectedAccountID());
		    
		    this.setUnitsTotal(new BigDecimal(0.0));
		    
		    for (int i = 0; i < this.getTrxList().size(); i++) 
		    {
				DynamoTransaction trx = this.getTrxList().get(i);
				
				if (trx.getUnits() != null)
				{
					if (trx.getTransactionTypeDescription().equalsIgnoreCase("Buy")
					|| 	trx.getTransactionTypeDescription().equalsIgnoreCase("Reinvest")
					||	trx.getTransactionTypeDescription().equalsIgnoreCase("Split"))
					{	
						trx.setUnitsStyleClass(SlomFinUtil.GREEN_STYLECLASS);
						trx.setDisplayUnits(trx.getUnits());
					}
					else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Sell"))
					{
						trx.setUnitsStyleClass(SlomFinUtil.RED_STYLECLASS);
						trx.setDisplayUnits(trx.getUnits().multiply(new BigDecimal(-1.0)));
					}
					
				}
				
				if (trx.getDisplayUnits() != null)
				{
					unitsTotal = unitsTotal.add(trx.getDisplayUnits());
				}
			}
		    
            logger.info("successfully found " + this.getTrxList().size() + " items");
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
		
		return "/transactionList.xhtml";
	}	
	
	public String addChangePayday()
	{
		logger.info("entering addChangePayday for operation: " + paydayAcidSetting);
		
		try
		{
			if (this.getSelectedPayday().getTransactionTypeID() == -1)
			{
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "trx Type not selected", "trx Type not selected");
			 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
			 	return "";
			}
			else
			{
				this.getSelectedPayday().setTrxTypeDescription(SlomFinUtil.getTrxTypesMap().get(this.getSelectedPayday().getTransactionTypeID()));
			}
			
			if (this.getSelectedPayday().getAccountID() == -1)
			{
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account not selected", "Account not selected");
			 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
			 	return "";
			}
			else
			{
				Account acct = accountDAO.getAccountByAccountID(this.getSelectedPayday().getAccountID());
				this.getSelectedPayday().setAccountName(acct.getsAccountName());
			}
		   
			if (this.getSelectedPayday().getXferAccountID() != -1)
			{
				Account acct = accountDAO.getAccountByAccountID(this.getSelectedPayday().getXferAccountID());
				this.getSelectedPayday().setAccountName(acct.getsAccountName());
			}
					    
			if (paydayAcidSetting.equalsIgnoreCase("Add"))
			{
				paydayDAO.addPayday(this.getSelectedPayday());
			}
			else if (paydayAcidSetting.equalsIgnoreCase("Update"))
			{
				paydayDAO.updatePayday(this.getSelectedPayday());
			}
			
		}
		catch (Exception e)
		{
        	logger.error("addChangePayday errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
		
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Payday successfully added/changed", "Account successfully added/changed");
	 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		
		return "";	
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

	public List<SelectItem> getWdCategoriesDropdownList() 
	{
		setWdCategoriesDropdownList(transactionDAO.getWdCategoryDropdownList());
		return wdCategoriesDropdownList;
	}

	public void setWdCategoriesDropdownList(List<SelectItem> wdCategoriesDropdownList) {
		this.wdCategoriesDropdownList = wdCategoriesDropdownList;
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

	public boolean isOwnedInvestments() {
		return ownedInvestments;
	}

	public void setOwnedInvestments(boolean ownedInvestments) {
		this.ownedInvestments = ownedInvestments;
	}
	
	public boolean isRenderOwnedInvestmentsCheckbox() {
		return renderOwnedInvestmentsCheckbox;
	}

	public void setRenderOwnedInvestmentsCheckbox(boolean renderOwnedInvestmentsCheckbox) {
		this.renderOwnedInvestmentsCheckbox = renderOwnedInvestmentsCheckbox;
	}

	public boolean isGenerateCorrespondingXfer() {
		return generateCorrespondingXfer;
	}

	public void setGenerateCorrespondingXfer(boolean generateCorrespondingXfer) {
		this.generateCorrespondingXfer = generateCorrespondingXfer;
	}

	public List<SelectItem> getTrxTypeDropdownList() {
		return trxTypeDropdownList;
	}

	public void setTrxTypeDropdownList(List<SelectItem> trxTypeDropdownList) {
		this.trxTypeDropdownList = trxTypeDropdownList;
	}

	public List<SelectItem> getInvestmentTypeDropdownList() {
		return investmentTypeDropdownList;
	}

	public void setInvestmentTypeDropdownList(List<SelectItem> investmentTypeDropdownList) {
		this.investmentTypeDropdownList = investmentTypeDropdownList;
	}

	public List<SelectItem> getInvestmentDropdownList() {
		return investmentDropdownList;
	}

	public void setInvestmentDropdownList(List<SelectItem> investmentDropdownList) {
		this.investmentDropdownList = investmentDropdownList;
	}

	public List<SelectItem> getXferAccountsDropdownList() {
		return xferAccountsDropdownList;
	}

	public void setXferAccountsDropdownList(List<SelectItem> xferAccountsDropdownList) {
		this.xferAccountsDropdownList = xferAccountsDropdownList;
	}

	public List<Investment> getInvestmentsList() {
		return investmentsList;
	}

	public void setInvestmentsList(List<Investment> investmentsList) {
		this.investmentsList = investmentsList;
	}

	public boolean isRenderTrxPostedDate() {
		return renderTrxPostedDate;
	}

	public void setRenderTrxPostedDate(boolean renderTrxPostedDate) {
		this.renderTrxPostedDate = renderTrxPostedDate;
	}

	public String getAccountAcidSetting() {
		return accountAcidSetting;
	}

	public void setAccountAcidSetting(String accountAcidSetting) {
		this.accountAcidSetting = accountAcidSetting;
	}

	public Account getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public boolean isRenderAccountUpdateFields() {
		return renderAccountUpdateFields;
	}

	public void setRenderAccountUpdateFields(boolean renderAccountUpdateFields) {
		this.renderAccountUpdateFields = renderAccountUpdateFields;
	}

	public List<SelectItem> getAccountTypesDropdownList() {
		return accountTypesDropdownList;
	}

	public void setAccountTypesDropdownList(List<SelectItem> accountTypesDropdownList) {
		this.accountTypesDropdownList = accountTypesDropdownList;
	}

	public String getTrxSearchTerm() {
		return trxSearchTerm;
	}

	public void setTrxSearchTerm(String trxSearchTerm) {
		this.trxSearchTerm = trxSearchTerm;
	}

	public List<Payday> getPaydayList() {
		return paydayList;
	}

	public void setPaydayList(List<Payday> paydayList) {
		this.paydayList = paydayList;
	}

	public Integer getCitiDoubleCashAccountID() {
		return citiDoubleCashAccountID;
	}

	public void setCitiDoubleCashAccountID(Integer citiDoubleCashAccountID) {
		this.citiDoubleCashAccountID = citiDoubleCashAccountID;
	}

	public Integer getCheckingAccountID() {
		return checkingAccountID;
	}

	public void setCheckingAccountID(Integer checkingAccountID) {
		this.checkingAccountID = checkingAccountID;
	}

	public String getPaydayAcidSetting() {
		return paydayAcidSetting;
	}

	public void setPaydayAcidSetting(String paydayAcidSetting) {
		this.paydayAcidSetting = paydayAcidSetting;
	}

	public Payday getSelectedPayday() {
		return selectedPayday;
	}

	public void setSelectedPayday(Payday selectedPayday) {
		this.selectedPayday = selectedPayday;
	}

	public List<SelectItem> getAccountDropdownList() {
		return accountDropdownList;
	}

	public void setAccountDropdownList(List<SelectItem> accountDropdownList) {
		this.accountDropdownList = accountDropdownList;
	}

	public List<Investment> getReportUnitsOwnedList() {
		return reportUnitsOwnedList;
	}

	public void setReportUnitsOwnedList(List<Investment> reportUnitsOwnedList) {
		this.reportUnitsOwnedList = reportUnitsOwnedList;
	}

	public String getInvestmentAcidSetting() {
		return investmentAcidSetting;
	}

	public void setInvestmentAcidSetting(String investmentAcidSetting) {
		this.investmentAcidSetting = investmentAcidSetting;
	}

	public Investment getSelectedInvestment() {
		return selectedInvestment;
	}

	public void setSelectedInvestment(Investment selectedInvestment) {
		this.selectedInvestment = selectedInvestment;
	}

	public List<SelectItem> getAssetClassesDropdownList() {
		return assetClassesDropdownList;
	}

	public void setAssetClassesDropdownList(List<SelectItem> assetClassesDropdownList) {
		this.assetClassesDropdownList = assetClassesDropdownList;
	}

	public boolean isRenderInvestmentUpdateFields() {
		return renderInvestmentUpdateFields;
	}

	public void setRenderInvestmentUpdateFields(boolean renderInvestmentUpdateFields) {
		this.renderInvestmentUpdateFields = renderInvestmentUpdateFields;
	}

	public List<PortfolioHistory> getPortfolioHistoryList() 
	{
		this.setPortfolioHistoryList(portfolioHistoryDAO.getFullPortfolioHistoryList());
		return portfolioHistoryList;
	}

	public void setPortfolioHistoryList(List<PortfolioHistory> portfolioHistoryList) {
		this.portfolioHistoryList = portfolioHistoryList;
	}

	public List<DynamoTransaction> getDividendTransactionsList() {
		return dividendTransactionsList;
	}

	public void setDividendTransactionsList(List<DynamoTransaction> dividendTransactionsList) {
		this.dividendTransactionsList = dividendTransactionsList;
	}

	public String getReportDividendsTitle() {
		return reportDividendsTitle;
	}

	public void setReportDividendsTitle(String reportDividendsTitle) {
		this.reportDividendsTitle = reportDividendsTitle;
	}

	public BigDecimal getDividendsTotal() {
		return dividendsTotal;
	}

	public void setDividendsTotal(BigDecimal dividendsTotal) {
		this.dividendsTotal = dividendsTotal;
	}

	public List<Account> getActiveAccountsList() {
		return activeAccountsList;
	}

	public void setActiveAccountsList(List<Account> activeAccountsList) {
		this.activeAccountsList = activeAccountsList;
	}

	public BigDecimal getPortfolioValue() {
		return portfolioValue;
	}

	public void setPortfolioValue(BigDecimal portfolioValue) {
		this.portfolioValue = portfolioValue;
	}

	public List<AccountPosition> getAccountPositionsList() {
		return accountPositionsList;
	}

	public void setAccountPositionsList(List<AccountPosition> accountPositionsList) {
		this.accountPositionsList = accountPositionsList;
	}

	public Integer getSelectedInvestmentID() {
		return selectedInvestmentID;
	}

	public void setSelectedInvestmentID(Integer selectedInvestmentID) {
		this.selectedInvestmentID = selectedInvestmentID;
	}

	public Integer getSelectedAccountID() {
		return selectedAccountID;
	}

	public void setSelectedAccountID(Integer selectedAccountID) {
		this.selectedAccountID = selectedAccountID;
	}

	public BigDecimal getUnitsTotal() {
		return unitsTotal;
	}

	public void setUnitsTotal(BigDecimal unitsTotal) {
		this.unitsTotal = unitsTotal;
	}

	public List<String> getReportYearsList() {
		return reportYearsList;
	}

	public void setReportYearsList(List<String> reportYearsList) {
		this.reportYearsList = reportYearsList;
	}
	public String getReportCapitalGainsTitle() {
		return reportCapitalGainsTitle;
	}

	public void setReportCapitalGainsTitle(String reportCapitalGainsTitle) {
		this.reportCapitalGainsTitle = reportCapitalGainsTitle;
	}

	public BigDecimal getCapitalGainsTotal() {
		return capitalGainsTotal;
	}

	public void setCapitalGainsTotal(BigDecimal capitalGainsTotal) {
		this.capitalGainsTotal = capitalGainsTotal;
	}

	public List<String> getReportDividendYearsList() {
		return reportDividendYearsList;
	}

	public void setReportDividendYearsList(List<String> reportDividendYearsList) {
		this.reportDividendYearsList = reportDividendYearsList;
	}

	public List<CapitalGain> getCapitalGainsTransactionsList() {
		return capitalGainsTransactionsList;
	}

	public void setCapitalGainsTransactionsList(List<CapitalGain> capitalGainsTransactionsList) {
		this.capitalGainsTransactionsList = capitalGainsTransactionsList;
	}

	public String getCapitalGainsTotalStyleClass() {
		return capitalGainsTotalStyleClass;
	}

	public void setCapitalGainsTotalStyleClass(String capitalGainsTotalStyleClass) {
		this.capitalGainsTotalStyleClass = capitalGainsTotalStyleClass;
	}

	public BigDecimal getTaxableValue() {
		return taxableValue;
	}

	public void setTaxableValue(BigDecimal taxableValue) {
		this.taxableValue = taxableValue;
	}

	public BigDecimal getRetirementValue() {
		return retirementValue;
	}

	public void setRetirementValue(BigDecimal retirementValue) {
		this.retirementValue = retirementValue;
	}

	public String getLocalePattern() 
	{
		this.setLocalePattern("MM-dd-yyyy");
		return localePattern;
	}

	public void setLocalePattern(String localePattern) {
		this.localePattern = localePattern;
	}

	public boolean isRenderTrxAddAnother() {
		return renderTrxAddAnother;
	}

	public void setRenderTrxAddAnother(boolean renderTrxAddAnother) {
		this.renderTrxAddAnother = renderTrxAddAnother;
	}

}
