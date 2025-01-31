package com.pas.beans;

import java.io.Serializable;

import com.pas.dynamodb.DynamoTransaction;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.PortfolioHistoryDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;
import com.pas.slomfin.dao.TransactionDAO;
import com.pas.slomfin.dao.AccountDAO;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("pc_SlomFinMain")
@SessionScoped
public class SlomFinMain implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SlomFinMain.class);	
		
	private final double id = Math.random();	
	
	public static String GREEN_STYLECLASS_MENU = "menuGreen";
	public static String RED_STYLECLASS_MENU = "menuRed";
	
	public static String GREEN_STYLECLASS = "resultGreen";
	public static String RED_STYLECLASS = "resultRed";
	public static String YELLOW_STYLECLASS = "resultYellow";
	
	private String siteTitle;	
	
	private String transactionAcidSetting;
	
	private TransactionDAO transactionDAO;
	private InvestmentDAO investmentDAO;
	private AccountDAO accountDAO;
	private PortfolioHistoryDAO portfolioHistoryDAO;
	
	@PostConstruct
	public void init() 
	{
		logger.info("Entering SlomFinMain init method.  Should only be here ONE time");
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
		transactionDAO.readTransactionsWithin2YearsFromDB();
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

	private void loadPortfolioHistory(DynamoClients dynamoClients) throws Exception {

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

	public void setRenderTransactionViewAddUpdateDelete()
	{
	}
}
