package com.pas.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pas.dynamodb.DynamoTransaction;
import com.pas.slomfin.constants.IAppConstants;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SlomFinUtil
{
	static Logger log = LogManager.getLogger(SlomFinUtil.class);

	public static String MY_TIME_ZONE = "America/New_York";
	public static String GREEN_STYLECLASS = "resultGreen";
	public static String RED_STYLECLASS = "resultRed";
	public static String BIGREDBOLD_STYLECLASS = "resultRedBigBold";
	public static String YELLOW_STYLECLASS = "resultYellow";
	
	public static Map<Integer, List<Integer>> invTypeTrxTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeInvTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeTrxTypeMap = new HashMap<>();
	
	public static Map<Integer, String> accountTypesMap = new HashMap<>();	
	public static Map<Integer, String> trxTypesMap = new HashMap<>();
	public static Map<Integer, String> invTypesMap = new HashMap<>();
	public static Map<Integer, String> assetClassesMap = new HashMap<>();	
	
	public static Map<Integer, List<SelectItem>> acctTypeTrxTypeDropdownsMap = new HashMap<>();
	
	private static Logger logger = LogManager.getLogger(SlomFinUtil.class);	
	
	//Investment Types
	public static int STOCK = 1;
	public static int OPTION = 2; 
	public static int MUTUALFUND = 3;
	public static int CASH = 5;
	public static int REALESTATE = 6;
	public static int CRYPTO = 7;
	
	//Account Types
	public static int TaxableBrokerage = 1; 
	public static int F401k = 2;	
	public static int RothIRA = 3;
	public static int Savings = 4;
	public static int Checking = 5;
	public static int AccountRealEstate = 6;
	public static int F529Plan = 7; 
	public static int CreditCard = 8;
	public static int HardCash = 9;
	public static int Pension = 10;
	public static int MoneyMarketNoChk = 11;
	public static int MoneyMarketwChk = 12;
	public static int Mortgage = 13;
	public static int TraditionalIRA = 14;
	public static int Roth401k = 15;
	public static int HSA = 16;
	
	//Account Type Descriptions
	public static String strTaxableBrokerage = "Taxable Brokerage"; 
	public static String str401k = "401k";	
	public static String strRothIRA = "Roth IRA";
	public static String strSavings = "Savings";
	public static String strChecking = "Checking";
	public static String strRealEstate = "Real Estate";
	public static String str529 = "529 Plan"; 
	public static String strCreditCard = "Credit Card";
	public static String strCash = "Hard Cash";
	public static String strPension = "Pension";
	public static String strMoneyMarketNoChk = "Money Market NoChk";
	public static String strMoneyMarketwChk = "Money Market wChk";
	public static String strMortgage = "Mortgage";	
	public static String strTraditionalIRA = "Traditional IRA";
	public static String strRoth401k = "Roth 401k";
	public static String strHsa = "HSA";
		
	//Transaction Types
	public static int Buy = 1; 
	public static int CashDividend = 2;	
	public static int ExpireOption = 3;
	public static int Fee = 4;
	public static int CashDeposit = 5;
	public static int InterestEarned = 6;
	public static int CheckWithdrawal = 7; 
	public static int MarginInterest = 8;
	public static int Split = 9;
	public static int Reinvest = 10;
	public static int Sell = 11;
	public static int TransferIn = 12;
	public static int TransferOut = 13;	
	public static int CashWithdrawal = 15;
	public static int ExerciseOption = 16;
	public static int Loan = 17;
	
	//Asset Classes
	public static int EmergingMarketStocks = 1;
	public static int USSmallCapStocks = 2;
	public static int USLargeCapStocks = 3;
	public static int ForeignStocks = 4;
	public static int USRealEstate = 5;
	public static int USOilandGas = 6;
	public static int CorporateBonds = 7;
	public static int ForeignBonds = 8;
	public static int TreasuryBonds = 9;
	public static int MunicipalBonds = 10;
	public static int MoneyMarket = 11;
	public static int TBills = 12;
	public static int Gold = 13;
	public static int Options = 14;
	public static int Cash = 15;
	public static int Energy = 16;
	public static int Technology = 17;
	public static int MidcapStocks = 18;
	public static int Cryptocurrency = 20;

	
	//Transaction Type Descriptions
	public static String strBuy = "Buy"; 
	public static String strCashDividend = "Cash Dividend";	
	public static String strExpireOption = "Expire Option";
	public static String strFee = "Fee";
	public static String strCashDeposit = "Cash Deposit";
	public static String strInterestEarned = "Interest Earned";
	public static String strCheckWithdrawal = "Check Withdrawal"; 
	public static String strMarginInterest = "Margin Interest";
	public static String strSplit = "Split";
	public static String strReinvest = "Reinvest";
	public static String strSell = "Sell";
	public static String strTransferIn = "Transfer In";
	public static String strTransferOut = "Transfer Out";	
	public static String strCashWithdrawal = "Cash Withdrawal";
	public static String strExerciseOption = "Exercise Option";
	public static String strLoan = "Loan";
	
	static
	{
		accountTypesMap.put(TaxableBrokerage, strTaxableBrokerage); 
		accountTypesMap.put(F401k, str401k); 
		accountTypesMap.put(RothIRA, strRothIRA); 
		accountTypesMap.put(Savings, strSavings); 
		accountTypesMap.put(Checking, strChecking); 
		accountTypesMap.put(AccountRealEstate, strRealEstate); 
		accountTypesMap.put(F529Plan, str529); 
		accountTypesMap.put(CreditCard, strCreditCard); 
		accountTypesMap.put(HardCash, strCash); 
		accountTypesMap.put(Pension, strPension); 
		accountTypesMap.put(MoneyMarketNoChk, strMoneyMarketNoChk); 
		accountTypesMap.put(MoneyMarketwChk, strMoneyMarketwChk); 
		accountTypesMap.put(Mortgage, strMortgage); 
		accountTypesMap.put(TraditionalIRA, strTraditionalIRA); 
		accountTypesMap.put(Roth401k, strRoth401k); 
		accountTypesMap.put(HSA, strHsa); 
		
		assetClassesMap.put(EmergingMarketStocks, "Emerging Market Stocks"); 
		assetClassesMap.put(USSmallCapStocks, "US Small Cap Stocks"); 
		assetClassesMap.put(USLargeCapStocks, "US Large Cap Stocks"); 
		assetClassesMap.put(ForeignStocks, "Foreign Stocks"); 
		assetClassesMap.put(USRealEstate, "US Real Estate"); 
		assetClassesMap.put(USOilandGas, "US Oil and Gas"); 
		assetClassesMap.put(CorporateBonds, "Corporate Bonds"); 
		assetClassesMap.put(ForeignBonds, "Foreign Bonds"); 
		assetClassesMap.put(TreasuryBonds, "Treasury Bonds"); 
		assetClassesMap.put(MunicipalBonds, "Municipal Bonds"); 
		assetClassesMap.put(MoneyMarket, "Money Market"); 
		assetClassesMap.put(TBills, "T-Bills"); 
		assetClassesMap.put(Gold, "Gold"); 
		assetClassesMap.put(Options, "Options"); 
		assetClassesMap.put(Cash, "Cash"); 
		assetClassesMap.put(Energy, "Energy"); 
		assetClassesMap.put(Technology, "Technology"); 
		assetClassesMap.put(MidcapStocks, "Midcap Stocks"); 
		assetClassesMap.put(Cryptocurrency, "Cryptocurrency"); 

		trxTypesMap.put(Buy, "Buy"); 
		trxTypesMap.put(CashDividend, "Cash Dividend"); 
		trxTypesMap.put(ExpireOption, "Expire Option"); 
		trxTypesMap.put(Fee, "Fee"); 
		trxTypesMap.put(CashDeposit, "Cash Deposit"); 
		trxTypesMap.put(InterestEarned, "Interest Earned"); 
		trxTypesMap.put(CheckWithdrawal, "Check Withdrawal"); 
		trxTypesMap.put(MarginInterest, "Margin Interest"); 
		trxTypesMap.put(Split, "Split"); 
		trxTypesMap.put(Reinvest, "Reinvest"); 
		trxTypesMap.put(Sell, "Sell"); 
		trxTypesMap.put(TransferIn, "Transfer In"); 
		trxTypesMap.put(TransferOut, "Transfer Out"); 		
		trxTypesMap.put(CashWithdrawal, "Cash Withdrawal"); 
		trxTypesMap.put(ExerciseOption, "Exercise Option"); 
		trxTypesMap.put(Loan, "Loan"); 
		
		invTypesMap.put(STOCK, "Stock"); 
		invTypesMap.put(OPTION, "Option"); 
		invTypesMap.put(MUTUALFUND, "Mutual Fund"); 
		invTypesMap.put(CASH, "Cash"); 
		invTypesMap.put(REALESTATE, "Real Estate"); 
		invTypesMap.put(CRYPTO, "Crypto"); 
				
		invTypeTrxTypeMap.put(STOCK, Arrays.asList(Buy,CashDividend,Split,Reinvest,Sell,TransferIn,TransferOut));
		invTypeTrxTypeMap.put(OPTION, Arrays.asList(Buy,ExpireOption,Split,Sell,TransferIn,TransferOut,ExerciseOption));
		invTypeTrxTypeMap.put(MUTUALFUND, Arrays.asList(Buy,CashDividend,Split,Reinvest,Sell,TransferIn,TransferOut));
		invTypeTrxTypeMap.put(CASH, Arrays.asList(CashDividend,Fee,CashDeposit,InterestEarned,CheckWithdrawal,MarginInterest,TransferIn,TransferOut,CashWithdrawal,Loan));
		invTypeTrxTypeMap.put(REALESTATE, Arrays.asList(Buy,Sell));
		invTypeTrxTypeMap.put(CRYPTO, Arrays.asList(Buy,Sell));
				
		acctTypeInvTypeMap.put(TaxableBrokerage, Arrays.asList(STOCK,OPTION,MUTUALFUND,CASH,CRYPTO));
		acctTypeInvTypeMap.put(F401k, Arrays.asList(STOCK,MUTUALFUND,CASH));
		acctTypeInvTypeMap.put(RothIRA, Arrays.asList(STOCK,OPTION,MUTUALFUND,CASH));
		acctTypeInvTypeMap.put(Savings, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(Checking, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(AccountRealEstate, Arrays.asList(REALESTATE));
		acctTypeInvTypeMap.put(F529Plan, Arrays.asList(MUTUALFUND,CASH));
		acctTypeInvTypeMap.put(CreditCard, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(HardCash, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(Pension, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(MoneyMarketNoChk, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(MoneyMarketwChk, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(Mortgage, Arrays.asList(CASH));
		acctTypeInvTypeMap.put(TraditionalIRA, Arrays.asList(STOCK,OPTION,MUTUALFUND,CASH));
		acctTypeInvTypeMap.put(Roth401k, Arrays.asList(STOCK,MUTUALFUND,CASH));
		acctTypeInvTypeMap.put(HSA, Arrays.asList(STOCK,OPTION,MUTUALFUND,CASH));
		
		//124 of these - still need to do
		acctTypeTrxTypeMap.put(TaxableBrokerage, Arrays.asList(Buy, CashDividend, ExpireOption, Fee, CashDeposit, InterestEarned, MarginInterest, Split, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal, ExerciseOption));
		acctTypeTrxTypeMap.put(F401k, Arrays.asList(Buy, CashDividend, Fee, CashDeposit, InterestEarned, Split, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(RothIRA, Arrays.asList(Buy, CashDividend, ExpireOption, Fee, CashDeposit, InterestEarned, Split, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(Savings, Arrays.asList(Fee, CashDeposit, InterestEarned, Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(Checking, Arrays.asList(Fee, CashDeposit, InterestEarned, Sell,TransferIn, TransferOut, CheckWithdrawal, CashWithdrawal));
		acctTypeTrxTypeMap.put(AccountRealEstate, Arrays.asList(Buy, Sell));
		acctTypeTrxTypeMap.put(F529Plan, Arrays.asList(Buy, CashDividend, Fee, CashDeposit, InterestEarned, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(CreditCard, Arrays.asList(Fee, CashDeposit, TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(HardCash, Arrays.asList(Fee, CashDeposit, TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(Pension, Arrays.asList(CashDeposit, InterestEarned));
		acctTypeTrxTypeMap.put(MoneyMarketNoChk, Arrays.asList(CashDividend, Fee, CashDeposit, TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(MoneyMarketwChk, Arrays.asList(CashDividend, Fee, CashDeposit, TransferIn, TransferOut, CashWithdrawal, CheckWithdrawal));
		acctTypeTrxTypeMap.put(Mortgage, Arrays.asList(CashDeposit, Loan));
		acctTypeTrxTypeMap.put(TraditionalIRA, Arrays.asList(Buy, CashDividend, ExpireOption, Fee, CashDeposit, InterestEarned, Split, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(Roth401k, Arrays.asList(Buy, CashDividend, ExpireOption, Fee, CashDeposit, InterestEarned, Split, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		acctTypeTrxTypeMap.put(HSA, Arrays.asList(Buy, CashDividend, Fee, CashDeposit, InterestEarned, Reinvest,Sell,TransferIn, TransferOut, CashWithdrawal));
		
	}
	
	public static List<SelectItem> getValidTrxTypesForAccountTypes(Integer accountType)
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		List<Integer> validTrxTypes = acctTypeTrxTypeMap.get(accountType);
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (int i = 0; i < validTrxTypes.size(); i++)
		{
			SelectItem si = new SelectItem();
			si.setValue(validTrxTypes.get(i));
			si.setLabel(trxTypesMap.get(si.getValue()));
			returnList.add(si);
		}
		
		return returnList;
	}
	
	public static List<SelectItem> getValidInvTypesForAccountTypes(Integer accountType)
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		List<Integer> validInvTypes = acctTypeInvTypeMap.get(accountType);
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (int i = 0; i < validInvTypes.size(); i++)
		{
			SelectItem si = new SelectItem();
			si.setValue(validInvTypes.get(i));
			si.setLabel(invTypesMap.get(si.getValue()));
			returnList.add(si);
		}
		
		return returnList;
	}
	
	public static String getLastYearsLastDayDate() 
	{
	    Calendar prevYear = Calendar.getInstance();
	    prevYear.add(Calendar.YEAR, -1);
	    String returnDate = prevYear.get(Calendar.YEAR) + "-12-31";
	    return returnDate;
	}
	
	public static String getOneMonthAgoDate() 
	{
	    Calendar calOneMonthAgo = Calendar.getInstance();
	    calOneMonthAgo.add(Calendar.MONTH, -1);
	    Date dateOneMonthAgo = calOneMonthAgo.getTime();
	    Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
	    String returnDate = formatter.format(dateOneMonthAgo);
	    return returnDate;
	}
	
	public static Date getTwoDaysFromNowDate() 
	{
	    Calendar calTwoDaysAway = Calendar.getInstance();
	    calTwoDaysAway.add(Calendar.DATE, 2);
	    Date returnDate = calTwoDaysAway.getTime();
	    return returnDate;
	}
	
	public static Date getTwoYearsAgoDate() 
	{
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -2);
	    Date date = cal.getTime();
	    return date;
	}
		
	public static String getLoggedInUserName()
	{		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		String currentUser = (String) session.getAttribute("currentUser");
		
		currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		logger.info("current logged in user is: " + currentUser);
		
	    return currentUser == null ? null : currentUser.toLowerCase().trim();
	}	

	public static List<SelectItem> getAllTrxTypesTypesDropdownList() 
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (Integer key : trxTypesMap.keySet()) 
		{
        	String description = trxTypesMap.get(key);
            SelectItem si = new SelectItem();
			si.setValue(key);
			si.setLabel(description);
			returnList.add(si);            	
        }
		return returnList;
	}
	
	public static List<SelectItem> getAssetClassesDropdownList() 
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (Integer key : assetClassesMap.keySet()) 
		{
        	String description = assetClassesMap.get(key);
            SelectItem si = new SelectItem();
			si.setValue(key);
			si.setLabel(description);
			returnList.add(si);            	
        }
		return returnList;
	}
	
	public static List<SelectItem> getAccountTypesDropdownList() 
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (Integer key : accountTypesMap.keySet()) 
		{
        	String description = accountTypesMap.get(key);
            SelectItem si = new SelectItem();
			si.setValue(key);
			si.setLabel(description);
			returnList.add(si);            	
        }
		return returnList;
	}
	
	public static List<SelectItem> getInvestmentTypesDropdownList() 
	{
		List<SelectItem> returnList = new ArrayList<>();
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
		for (Integer key : invTypesMap.keySet()) 
		{
        	String description = invTypesMap.get(key);
            SelectItem si = new SelectItem();
			si.setValue(key);
			si.setLabel(description);
			returnList.add(si);            	
        }
		return returnList;
	}
	
	public static String getDayofWeekString(Date date) 
	{
		Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("EEEE", locale);
	    return formatter.format(date);
	}
		
	public static String getEncryptedPassword(String unencryptedPassword)
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPW = passwordEncoder.encode(unencryptedPassword);
		return encryptedPW;
	}
	
	public static List<String> getRecentYearsList(boolean withTaxableInd)
	{
		List<String> returnList = new ArrayList<>();
		
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		
		for (int i=nowYear; i>nowYear-8; i--)
		{
			
			if (withTaxableInd)
			{
				String tempStr1 = String.valueOf(i) + " Taxable Only";
				returnList.add(tempStr1);
				
				String tempStr2 = String.valueOf(i) + " All";
				returnList.add(tempStr2);
			}
			else
			{
				returnList.add(String.valueOf(i));
			}			
		} 
		
		return returnList;
	}
	
	public static String getContextRoot()
	{
		String contextRoot = "";
		try
		{
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = request.getSession();	
	        contextRoot = (String) httpSession.getAttribute(IAppConstants.CONTEXT_ROOT);
		}
		catch (Exception e)
		{			
		}
		
		return contextRoot;
	}
	
	public static Map<Integer, String> sortHashMapByValues(Map<Integer, String> hm) 
	{
	    HashMap<Integer, String> temp = hm.entrySet().stream().sorted((i1, i2)-> 
	            i1.getValue().compareTo(i2.getValue())).collect(
	            Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,
	            (e1, e2) -> e1, LinkedHashMap::new));
	    
	    return temp;
	}

	//Assumes input parameter tempList is already sorted.
	public static List<DynamoTransaction> setAccountBalances(List<DynamoTransaction> tempList, BigDecimal startingBalance) 
	{
		//establish balance
	    BigDecimal currentBalance = startingBalance;
	    
	    for (int i = 0; i < tempList.size(); i++)
	    {
	    	DynamoTransaction trx = tempList.get(i);
	    	
	    	if (trx.getCostProceeds() != null)
	    	{
	    		currentBalance = transactAnAmount(trx, currentBalance, "amount");	    		
	    	}
	    	
	    	trx.setCurrentBalance(currentBalance);
	    	
	    	if (currentBalance.compareTo(BigDecimal.ZERO) > 0)
	    	{
	            trx.setBalanceStyleClass(GREEN_STYLECLASS);
	        }
	    	else if (currentBalance.compareTo(BigDecimal.ZERO) < 0)
	    	{
	            trx.setBalanceStyleClass(RED_STYLECLASS);
	            
	            if (trx.getFinalTrxOfBillingCycle())
	            {
	            	trx.setBalanceStyleClass(BIGREDBOLD_STYLECLASS);
	            }
	        }
	    	
		}
	    	    
		return tempList;
	}
	
	
	//Assumes input parameter list is already sorted.
	public static Map<Integer, BigDecimal> getUnitsOwned(List<DynamoTransaction> transactionsList) 
	{
		Map<Integer, BigDecimal> returnMap = new HashMap<>();
		
		for (int i = 0; i < transactionsList.size(); i++)
	    {
	    	DynamoTransaction trx = transactionsList.get(i);
	    	
	    	if (trx.getUnits() != null
	    	&&  trx.getUnits().compareTo(BigDecimal.ZERO) != 0)
	    	{
	    		if (returnMap.containsKey(trx.getInvestmentID()))
				{
	    			BigDecimal currentTotal = returnMap.get(trx.getInvestmentID());
					BigDecimal newAmount = transactAnAmount(trx, currentTotal, "units");
					returnMap.replace(trx.getInvestmentID(), newAmount);
				}
				else
				{
					BigDecimal newAmount = transactAnAmount(trx, new BigDecimal(0.0), "units");
					returnMap.put(trx.getInvestmentID(), newAmount);
				}
	    		
	    	}
	    		    	
		}
	    	    
		return returnMap;
	}
	
	public static BigDecimal transactAnAmount(DynamoTransaction trx, BigDecimal inputAmount, String amountOrUnits)
	{
		BigDecimal returnAmount = null;
		
		if (trx.getTransactionTypeDescription().equalsIgnoreCase("Buy"))
		{	
			if (amountOrUnits.equalsIgnoreCase("amount"))
    		{
				returnAmount = inputAmount.subtract(trx.getCostProceeds());
    		}
			else
			{
				returnAmount = inputAmount.add(trx.getUnits());
			}
		}
		else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Sell"))
		{
			if (amountOrUnits.equalsIgnoreCase("amount"))
    		{
				returnAmount = inputAmount.add(trx.getCostProceeds());
    		}
			else
			{
				returnAmount = inputAmount.subtract(trx.getUnits());
			}
		}
		else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Reinvest"))
		{
			if (amountOrUnits.equalsIgnoreCase("amount"))
    		{
				returnAmount = inputAmount.add(new BigDecimal(0.0));
			}
			else
			{
				returnAmount = inputAmount.add(trx.getUnits());
			}
		}
		else if (trx.getTrxTypPositiveInd())
    	{		    				
			if (amountOrUnits.equalsIgnoreCase("amount"))
    		{
				if (trx.getCostProceeds() == null)
				{
					returnAmount = inputAmount;
				}
				else
				{
					returnAmount = inputAmount.add(trx.getCostProceeds());
				}
    		}
			else
			{
				returnAmount = inputAmount.add(trx.getUnits());
			}
    	}
    	else
    	{		    		
    		if (amountOrUnits.equalsIgnoreCase("amount"))
    		{
				returnAmount = inputAmount.subtract(trx.getCostProceeds());
    		}
			else
			{
				returnAmount = inputAmount.subtract(trx.getUnits());
			}
    	}
		
		return returnAmount;
		
	}

	public static Map<Integer, String> getAccountTypesMap() {
		return accountTypesMap;
	}

	public static void setAccountTypesMap(Map<Integer, String> accountTypesMap) {
		SlomFinUtil.accountTypesMap = accountTypesMap;
	}

	public static Map<Integer, String> getTrxTypesMap() {
		return trxTypesMap;
	}

	public static void setTrxTypesMap(Map<Integer, String> trxTypesMap) {
		SlomFinUtil.trxTypesMap = trxTypesMap;
	}
	
	
	
}