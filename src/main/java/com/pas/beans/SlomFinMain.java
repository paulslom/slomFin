package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.pas.util.Utils;

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
		    
            String targetURL = Utils.getContextRoot() + "/transactionList.xhtml";
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
		    
		    String targetURL = Utils.getContextRoot() + "/accountAddUpdate.xhtml";
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
	
	public void valueChgTrxType(AjaxBehaviorEvent event) 
	{
		logger.info(Utils.getLoggedInUserName() + " picked a new trx type on trx add/update form");
		
		try
		{
			SelectOneMenu selectonemenu = (SelectOneMenu)event.getSource();		
			Integer selectedOption = (Integer)selectonemenu.getValue();			
			String selectedTrxTypeDesc = SlomFinUtil.trxTypesMap.get(selectedOption);			
			enableTrxUpdateFields(selectedTrxTypeDesc);			
		}
		catch (Exception e)
		{
			logger.error("Exception in valueChgTrxType: " +e.getMessage(),e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in valueChgTrxType: " + e.getMessage(),null);
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
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strBuy))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCashDeposit))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCashDividend))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strCheckWithdrawal))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strExerciseOption))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strExpireOption))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strFee))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strInterestEarned))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strLoan))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strMarginInterest))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strReinvest))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strSell))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strSplit))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferIn))
		{
				
		}
		else if (selectedTrxTypeDesc.equalsIgnoreCase(SlomFinUtil.strTransferOut))
		{
				
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
		    	dynamoTrx.setPostedDateJava(Utils.getTwoDaysFromNowDate());
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
		    }
		    
		    setRenderTransactionViewAddUpdateDelete(); 
		    
		    String targetURL = Utils.getContextRoot() + "/transactionAddUpdate.xhtml";
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
					Account acct = this.getAccountByAccountID(this.getSelectedTransaction().getAccountID());
					
					String accountType = SlomFinUtil.accountTypesMap.get(acct.getiAccountTypeID());
			    	if (accountType != null)
			    	{
			       		if (accountType.equalsIgnoreCase(SlomFinUtil.strChecking)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strRealEstate)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strCreditCard)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strCash)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strPension)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strMoneyMarketNoChk)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strMoneyMarketwChk)
			    		||  accountType.equalsIgnoreCase(SlomFinUtil.strMortgage))
			    		{
			    			this.getSelectedTransaction().setInvestmentID(getCashInvestmentID());
			    			this.getSelectedTransaction().setInvestmentDescription("Cash");
				    	}
			    	}
			    	
				}
				
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
}
