package com.pas.slomfin.constants;


public interface ISlomFinAppConstants
{
    //Action Forwards
    public static final String AF_ADDANOTHER="addAnother";
    public static final String AF_RPTTRX="fwdRptTrx";
    public static final String AF_RPTMTGPMT="fwdRptMtgPmt";
    public static final String AF_SUCCESSAFTERADD="successAfterAdd";

	//Business references
	public static final String DROPDOWN_BUSINESS = "DropdownBusiness";
	public static final String DBNAME_BUSINESS = "DBNameBusiness";
	
	//Button text
    public static final String BUTTON_ADDCASHSPENT = "Add Cash Transaction";
    public static final String BUTTON_ADDTHENADDANOTHERCASHSPENT = "Add then Add another Cash Transaction";
    public static final String BUTTON_RESUBMIT = "Re-submit";
    public static final String BUTTON_SUBMIT = "Submit";
    public static final String BUTTON_CANCELADD = "Cancel Add";
    public static final String BUTTON_CANCELUPDATE = "Cancel Update";
    public static final String BUTTON_CANCELDELETE = "Cancel Delete";
    public static final String BUTTON_RETURN = "Return";
    public static final String BUTTON_DELETE = "Delete";
    
	// Cache related constants
	public static final String SFINVESTOR="SFInvestor";
	public static final String CASHINVESTMENTID="CashInvestmentID";
	public static final String CURRENTDBNAME="CurrentDBName";
	public static final String TRXSELECTION="TransactionSelection";
	public static final String ACCTSELECTION="AccountSelection";
	public static final String MTGPMTSELECTION="MortgagePaymentSelection";
	public static final String TAXGRPYEARSELECTION="TaxGroupYearSelection";
	public static final String TAXPMTSELECTION="TaxPaymentSelection";
	public static final String GOTODBIND="GoToDBInd";
	
	//DAO references
	public static final String ACCOUNT_DAO = "AccountDAO";
	public static final String ACCOUNTTYPE_TRXTYPE_DAO = "AccountTypeTrxTypeDAO";
	public static final String ACCOUNTTYPE_INVTYPE_DAO = "AccountTypeInvTypeDAO";
	public static final String ACCOUNTTYPE_DAO = "AccountTypeDAO";
	public static final String ASSETCLASS_DAO = "AssetClassDAO";
	public static final String BROKER_DAO = "BrokerDAO";
	public static final String CAPITAL_GAINS_DAO = "CapitalGainsDAO";
	public static final String CASHDEPOSITTYPE_DAO = "CashDepositTypeDAO";
	public static final String DROPDOWN_DAO = "DropdownDAO";
	public static final String FEDERALTAXES_DAO = "FederalTaxesDAO";
	public static final String GOALS_DAO = "GoalsDAO";
	public static final String INVESTMENT_DAO = "InvestmentDAO";
	public static final String INVESTMENTTYPE_DAO = "InvestmentTypeDAO";
	public static final String INVESTOR_DAO = "InvestorDAO";
	public static final String JOB_DAO = "JobDAO";
	public static final String LOGIN_DAO = "LoginDAO";
	public static final String MENU_DAO = "MenuDAO";
	public static final String MENU2_DAO = "Menu2DAO";
	public static final String MISC_DAO = "MiscDAO";
	public static final String MORTGAGE_DAO = "MortgageDAO";
	public static final String MORTGAGEPAYMENT_DAO = "MortgagePaymentDAO";
	public static final String OPTIONTYPE_DAO = "OptionTypeDAO";
	public static final String PAYCHECKHISTORY_DAO = "PaycheckHistoryDAO";
	public static final String PAYCHECKOUTFLOW_DAO = "PaycheckOutflowDAO";
	public static final String PAYCHECKTYPE_DAO = "PaycheckTypeDAO";
	public static final String PORTFOLIO_DAO = "PortfolioDAO";	
	public static final String TAXTABLE_DAO = "TaxTableDAO";
	public static final String TAXFORMULATYPE_DAO = "TaxFormulaTypeDAO";
	public static final String TAXGROUP_DAO = "TaxGroupDAO";
	public static final String TAXGROUPYEAR_DAO = "TaxGroupYearDAO";
	public static final String TAXPAYMENT_DAO = "TaxPaymentDAO";
	public static final String TAXPAYMENTTYPE_DAO = "TaxPaymentTypeDAO";
	public static final String TRANSACTION_DAO = "TransactionDAO";
	public static final String TRANSACTIONTYPE_DAO = "TransactionTypeDAO";
	public static final String UNDERLYINGSTOCKS_DAO = "UnderlyingStocksDAO";
	public static final String WDCATEGORY_DAO = "WDCategoryDAO";
	
	//Dropdowns
	//First the "Base" dropdowns.
	public static final String DROPDOWN_ACCOUNTS = "ACC";
	public static final String DROPDOWN_ACCOUNTTYPES = "ACCTYP";
	public static final String DROPDOWN_ASSETCLASSES = "ASSCL";
	public static final String DROPDOWN_BROKERS = "BRK";
	public static final String DROPDOWN_CASHDEPOSITTYPES = "CDEPTYP";
	public static final String DROPDOWN_COMMISSIONS = "COMM";
	public static final String DROPDOWN_INVESTMENTS = "INVM";
	public static final String DROPDOWN_INVESTMENTTYPES = "INVTYP";
	public static final String DROPDOWN_INVESTORS = "INVS";
	public static final String DROPDOWN_JOBS = "JOB";
	public static final String DROPDOWN_MENUS = "MENU";
	public static final String DROPDOWN_MORTGAGES = "MORT";
	public static final String DROPDOWN_OPTIONCOMMISSIONS = "OPTCOM";
	public static final String DROPDOWN_OPTIONTYPES = "OPTTYP";
	public static final String DROPDOWN_PAYCHECKTYPES = "PCHKTYP";
	public static final String DROPDOWN_PAYDAYDEFAULTS = "PDDEF";
	public static final String DROPDOWN_PORTFOLIOS = "PORT";
	public static final String DROPDOWN_TAXFORMULAS = "TXFORM";
	public static final String DROPDOWN_TAXFORMULATYPES = "TXFORMTYP";
	public static final String DROPDOWN_TAXGROUPS = "TXGRP";
	public static final String DROPDOWN_TAXPAYMENTTYPES = "TXPMTTYP";
	public static final String DROPDOWN_TRANSACTIONTYPES = "TRXTYP";
	public static final String DROPDOWN_WDCATEGORIES = "WDCAT";
	
	//Derived dropdowns
	public static final String DROPDOWN_ACCOUNTS_TAXABLE = "ACC_TXBL0";
	public static final String DROPDOWN_INVESTMENTS_BYINVTYP = "INVM_INVTYP";
	public static final String DROPDOWN_JOBS_BYINVESTOR = "JOB_INVS";
	public static final String DROPDOWN_PORTFOLIOS_BYINVESTOR = "PORT_INVS";	
	public static final String DROPDOWN_WDCATEGORIES_BYINVESTOR = "WDCAT_INVS";
	public static final String DROPDOWN_LAST5YEARS = "LAST5YEARS";
		
	//Dropdown names of session vars	
	public static final String SESSION_DD_ACCOUNTS = "Accounts";
	public static final String SESSION_DD_ACCOUNTTYPES = "AccountTypes";
	public static final String SESSION_DD_ASSETCLASSES = "AssetClasses";
	public static final String SESSION_DD_BROKERS = "Brokers";
	public static final String SESSION_DD_CASHDEPOSITTYPES = "CashDepositTypes";
	public static final String SESSION_DD_INVESTMENTS = "Investments";
	public static final String SESSION_DD_INVESTMENTTYPES = "InvestmentTypes";
	public static final String SESSION_DD_JOBS = "Jobs";
	public static final String SESSION_DD_MORTGAGES = "Mortgages";
	public static final String SESSION_DD_MTGACCOUNTS = "mortgageAccounts";
	public static final String SESSION_DD_OPTIONTYPES = "OptionTypes";
	public static final String SESSION_DD_PAYCHECKTYPES = "PaycheckTypes";
	public static final String SESSION_DD_PORTFOLIOS = "Portfolios";
	public static final String SESSION_DD_TAXGROUPS = "TaxGroups";
	public static final String SESSION_DD_TAXPAYMENTTYPES = "TaxPaymentTypes";
	public static final String SESSION_DD_TRANSACTIONTYPES = "TrxTypes";
	public static final String SESSION_DD_UNDERLYINGSTOCKS = "UnderlyingStocks";
	public static final String SESSION_DD_WDCATEGORIES = "WDCategories";
	public static final String SESSION_DD_XFERACCOUNTS = "XferAccounts";
	public static final String SESSION_DD_INVESTMENTCHOICEACCOUNTS = "InvestmentChoiceAccounts";
		
	//Misc dropdown items
	public static final String INVESTMENT_CASH = "Cash";
	public static final String CASHDEPTYPE_MORTGAGE_PRINCIPAL = "MtgPrincipal";
		
    //Investment Types
    public static final String INVTYP_CASH = "Cash";
    public static final String INVTYP_OPTION = "Option";
    public static final String INVTYP_STOCK = "Stock";
    public static final String INVTYP_REALESTATE = "Real Estate";
    public static final String INVTYP_CRYPTOCURRENCY = "CryptoCurrency";
    
    //Option Types
    public static final String OPTIONTYPE_PUT = "Put";
    public static final String OPTIONTYPE_CALL = "Call";
	
	//Menu constants
	public static final String REPORTS_MENU_LIST="ReportsMenuList";
	public static final String WORK_MENU_LIST="WorkMenuList";
	public static final String MISC_MENU_LIST="MiscMenuList";
	
	//Misc Constants       
    public static final Integer DROPDOWN_NOT_SELECTED = 0;
    public static final String DEFAULT_INVESTOR = "defaultInvestorID";
    public static final String DIRECT_DEPOSIT = "Direct Deposit";
    public static final String DEFERRAL_401K = "Employee contribution";
    public static final String ROTH_401K = "401k contribution";
    public static final String MATCHING_401K = "Employer Match";
    public static final String STOCK_INVTYPE_ID = "1";
    public static final String PORTFOLIOSUMMARYREQUEST = "PortSumm";
    public static final String PORTFOLIOSUMMARYBYASSETCLASSREQUEST = "PortSummAss";
        
    //Tax constants
    public static final Double CAPITAL_LOSS_LIMIT = new Double(3000.00);
    public static final Double CHILD_CREDIT_LOW_THRESHHOLD = new Double(110000.00);
    public static final Double CHILD_CREDIT_HIGH_THRESHHOLD = new Double(130000.00);
	public static final int CAPITAL_GAIN_LONG_TERM_STOCKS = 365;
	public static final int CAPITAL_GAIN_LONG_TERM_REALESTATE = 730;
	
	//Session vars
    public static final String SESSION_CURRENTDB = "CurrentDB";
    public static final String SESSION_ACCOUNTLIST = "AccountList";
    public static final String SESSION_ASSETCLASSLIST = "AssetClassList";
    public static final String SESSION_INVESTMENTLIST = "InvestmentList";
    public static final String SESSION_JOBLIST = "JobList";
	public static final String SESSION_MORTGAGELIST = "MortgageList";
	public static final String SESSION_MORTGAGEAMORTIZATIONLIST = "MortgageAmortizationList";
	public static final String SESSION_MORTGAGEPAYMENTLIST = "MortgagePaymentList";
	public static final String SESSION_PAYCHECKHISTORYLIST = "PaycheckHistoryList";
	public static final String SESSION_PAYCHECKOUTFLOWLIST = "PaycheckOutflowList";
	public static final String SESSION_PORTFOLIOLIST = "PortfolioList";
	
	public static final String SESSION_REPORTBUDGETLIST = "ReportBudgetList";
	public static final String SESSION_REPORTCOSTBASISLIST = "ReportCostBasisList";
	public static final String SESSION_REPORTDIVIDENDSLIST = "ReportDividendsList";
	public static final String SESSION_REPORTCAPITALGAINSLIST = "ReportCapitalGainsList";
	
	public static final String SESSION_REPORTNCWAGESLIST = "ReportNCWagesList";
	public static final String SESSION_REPORTNCDIVIDENDSLIST = "ReportNCDividendsList";
	public static final String SESSION_REPORTNCINTERESTLIST = "ReportNCInterestList";
	public static final String SESSION_REPORTNCCAPITALGAINSLIST = "ReportNCCapitalGainsList";
	public static final String SESSION_REPORTNCCREDITSLIST = "ReportNCCreditsList";
	
	public static final String SESSION_REPORTFTXWAGESLIST = "ReportFTXWagesList";
	public static final String SESSION_REPORTFTXDIVIDENDSLIST = "ReportFTXDividendsList";
	public static final String SESSION_REPORTFTXINTERESTLIST = "ReportFTXInterestList";
	public static final String SESSION_REPORTFTXCAPITALGAINSLIST = "ReportFTXCapitalGainsList";
	public static final String SESSION_REPORTFTXMISCINCOMELIST = "ReportFTXMiscIncomeList";
	public static final String SESSION_REPORTFTXDEDUCTIONSLIST = "ReportFTXDeductionsList";
	public static final String SESSION_REPORTFTXCREDITSLIST = "ReportFTXCreditsList";
	public static final String SESSION_REPORTFTXEXEMPTIONSLIST = "ReportFTXExemptionsList";
	public static final String SESSION_REPORTFTXW2LIST = "ReportFTXW2List";
	
	public static final String SESSION_REPORTGOALSLIST = "ReportGoalsList";
	public static final String SESSION_REPORTMTGAMORTIZATIONLIST = "ReportMtgAmortizationList";
	public static final String SESSION_REPORTMTGPAYMENTSLIST = "ReportMtgPaymentsList";
	public static final String SESSION_REPORTNCTAXESLIST = "ReportNCTaxesList";
	public static final String SESSION_REPORTPORTBYASSETCLASSLIST = "ReportPortByAssetClassList";
	public static final String SESSION_REPORTPORTFOLIOHISTORYLIST = "ReportPortfolioHistoryList";
	public static final String SESSION_REPORTPORTSUMMARYLIST = "ReportPortSummaryList";
	public static final String SESSION_REPORTTRXLIST = "ReportTrxList";
	public static final String SESSION_REPORTUNITSOWNEDLIST = "ReportUnitsOwnedList";
	public static final String SESSION_REPORTWDCATEGORIESLIST = "ReportWDCategoriesList";
	
	public static final String SESSION_TAXTABLELIST = "TaxTableList";
	public static final String SESSION_TAXGROUPLIST = "TaxGroupList";
	public static final String SESSION_TAXGROUPYEARLIST = "TaxGroupYearList";
	public static final String SESSION_TAXPAYMENTLIST = "TaxPaymentList";
	public static final String SESSION_TRXLIST = "TransactionList";
	public static final String SESSION_TRXCOMMONLIST = "TrxCommonList";
	public static final String SESSION_WDCATEGORYLIST = "WDCategoryList";
	
	public static final String SESSION_CASHSPENTPICTUREFILE = "cashSpentPictureFile";
    
    //Table Names
    public static final String TABLENAME_APPSECURITY = "Tblappsecurity";
    public static final String TABLENAME_INVESTOR = "tblInvestor";
    public static final String TABLENAME_DROPDOWN = "tblDropdown";
    
    //Account Types
    public static final String ACCOUNTTYPE_TAXABLEBROKERAGE ="Taxable Brokerage";
    public static final String ACCOUNTTYPE_401K = "401k";
    public static final String ACCOUNTTYPE_ROTHIRA = "Roth IRA";
    public static final String ACCOUNTTYPE_SAVINGS = "Savings";
    public static final String ACCOUNTTYPE_CHECKING = "Checking";
    public static final String ACCOUNTTYPE_REALESTATE = "Real Estate";
    public static final String ACCOUNTTYPE_529PLAN = "529 Plan";
    public static final String ACCOUNTTYPE_CREDITCARD = "Credit Card";
    public static final String ACCOUNTTYPE_HARDCASH = "Hard Cash";
    public static final String ACCOUNTTYPE_PENSION = "Pension";
    public static final String ACCOUNTTYPE_MONEYMARKETNOCHK = "Money Market NoChk";
    public static final String ACCOUNTTYPE_MONEYMARKETWCHK = "Money Market wChk";
    public static final String ACCOUNTTYPE_MORTGAGE = "Mortgage";
    public static final String ACCOUNTTYPE_TRADIRA = "Traditional IRA";
    public static final String ACCOUNTTYPE_ROTH401K = "Roth 401k";
    
    //Transaction Types
    public static final String TRXTYP_BUY ="Buy";
    public static final String TRXTYP_CASHDEPOSIT = "Cash Deposit";
    public static final String TRXTYP_CASHDIVIDEND = "Cash Dividend";
    public static final String TRXTYP_CASHWITHDRAWAL = "Cash Withdrawal";
    public static final String TRXTYP_CHECKWITHDRAWAL = "Check Withdrawal";
    public static final String TRXTYP_EXERCISEOPTION = "Exercise Option";
    public static final String TRXTYP_EXPIREOPTION = "Expire Option";
    public static final String TRXTYP_FEE = "Fee"; 
    public static final String TRXTYP_INTERESTEARNED = "Interest Earned";    
    public static final String TRXTYP_LOAN = "Loan";
    public static final String TRXTYP_MARGININTEREST = "Margin Interest";
    public static final String TRXTYP_REINVEST = "Reinvest";
    public static final String TRXTYP_SELL = "Sell";
    public static final String TRXTYP_SPLIT = "Split";    
    public static final String TRXTYP_TRANSFERIN = "Transfer In";
    public static final String TRXTYP_TRANSFEROUT = "Transfer Out";      
     
    //Webpage origination codes
	public static final int TRXLIST_ORIGIN_MENU = 1;
	public static final int TRXLIST_ORIGIN_CASHSPENT = 2;
	public static final int TRXLIST_ORIGIN_TRXLIST = 3;
	public static final int TRXLIST_ORIGIN_TRXVIEWCHGDEL = 4;
	public static final int TRXLIST_ORIGIN_TRXADD = 5;
	
    public static final String DBCONFIG_PORTFOLIODB = "Portfolio";
    public static final String DBCONFIG_PORTFOLIO_MYSQL_DB = "PortfolioMySQL";
    
}