package com.pas.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.faces.model.SelectItem;

public class SlomFinUtil
{
	static Logger log = LogManager.getLogger(SlomFinUtil.class);

	public static Map<Integer, List<Integer>> invTypeTrxTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeInvTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeTrxTypeMap = new HashMap<>();
	
	public static Map<Integer, String> accountTypesMap = new HashMap<>();	
	public static Map<Integer, String> trxTypesMap = new HashMap<>();
	public static Map<Integer, String> invTypesMap = new HashMap<>();
	
	public static Map<Integer, List<SelectItem>> acctTypeTrxTypeDropdownsMap = new HashMap<>();
	
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
		
		for (int i = 0; i < validTrxTypes.size(); i++)
		{
			SelectItem si = new SelectItem();
			si.setValue(validTrxTypes.get(i));
			si.setLabel(trxTypesMap.get(si.getValue()));
			returnList.add(si);
		}
		
		return returnList;
	}
	
}