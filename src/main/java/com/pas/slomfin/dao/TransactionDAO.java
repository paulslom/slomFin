package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblTransactionRowMapper;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblcashdeposittype;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tbltransaction;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.dbobjects.Tblunderlyingstocks;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.BudgetSelection;
import com.pas.slomfin.valueObject.PaycheckOutflowAdd;
import com.pas.slomfin.valueObject.TransactionList;
import com.pas.slomfin.valueObject.TransactionSearch;
import com.pas.slomfin.valueObject.TransactionSelection;
import com.pas.util.PASUtil;
import com.pas.util.Utils;

/**
 * Title: 		TransactionDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction DAO extends BaseDBDAO. Implements the data access to the tblTransaction table
 * Copyright: 	Copyright (c) 2006
 */
public class TransactionDAO extends BaseDBDAO
{
    private static final TransactionDAO currentInstance = new TransactionDAO();

    private TransactionDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TransactionDAO
     */
    public static TransactionDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltransaction> add(Object info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TransactionDAO add");		
				
		if (info instanceof Tbltransaction)
		{
		    Tbltransaction trx = (Tbltransaction)info;
		    Calendar now = Calendar.getInstance();			
		 	java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTimeInMillis());
			trx.setDtranEntryDate(timestamp);
			trx.setDtranChangeDate(timestamp);
			
			doTrxInsert(trx);			
		
			if (!trx.isSpawnedXfer())
			{
				if (trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
				||	trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
				{
					//remove this object from Hibernate's awareness so we can play with it...
					trx.setItransactionId(null);
					
					try
					{
						addSpawnedXfer(trx);
					}
					catch (SystemException e)
					{
						log.error("SystemException encountered: " +e.getMessage());
						e.printStackTrace();
						throw new DAOException(e);
					}
				}
			}			
			
			if (trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION))
			{				
				//remove this object from Hibernate's awareness so we can play with it...
				trx.setItransactionId(null);
				
				try
				{
					addSpawnedExerciseOption(trx);
				}
				catch (SystemException e)
				{
					log.error("SystemException encountered: " +e.getMessage());
					e.printStackTrace();
					throw new DAOException(e);
				}
			}
			
		}
		else //must be a list - this list will contain paycheckOutflowAdd objects
		{
		    TransactionList tList = (TransactionList)info;
		    ArrayList<PaycheckOutflowAdd> pcoaList = (ArrayList<PaycheckOutflowAdd>)tList.getTransactionList();
		    
		    for (int i=0; i<pcoaList.size(); i++)
			{				
				PaycheckOutflowAdd paycheckOutflowAdd = (PaycheckOutflowAdd)pcoaList.get(i);			
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss.SSS");
				java.util.Date trxdate = new java.util.Date();
				try
				{
					trxdate = sdf.parse(paycheckOutflowAdd.getPcoAddDate() + " 00:00:00.000");
				}
				catch (ParseException e1)
				{
					log.error(methodName + "TransactionDAO DAO Exception" + e1.getMessage());
					e1.printStackTrace();
					throw new DAOException(e1);			
				}
				
				Tbltransaction trx = new Tbltransaction();
				
				trx.setDtransactionDate(trxdate);
				trx.setDtranPostedDate(trxdate); //just default posted date to actual trx date on paycheck outflow adds
				trx.setMcostProceeds(new BigDecimal(paycheckOutflowAdd.getAmount()));
					
				if (paycheckOutflowAdd.getCheckNo() != null)
				{
					trx.setIcheckNo(Integer.valueOf(paycheckOutflowAdd.getCheckNo()));
				}
				
				trx.setSdescription(paycheckOutflowAdd.getDescription());
				
				AccountDAO accountDAOReference;
				InvestmentDAO investmentDAOReference;
				TransactionTypeDAO trxTypeDAOReference;
				WDCategoryDAO wdCategoryDAOReference;
				CashDepositTypeDAO cdTypeDAOReference;
				
				try
				{
					accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
					investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
					trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
					wdCategoryDAOReference = (WDCategoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.WDCATEGORY_DAO);
					cdTypeDAOReference = (CashDepositTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.CASHDEPOSITTYPE_DAO);
					
					List<Tblaccount> acctList = accountDAOReference.inquire(new Integer(paycheckOutflowAdd.getAccountID()));
					trx.setTblaccount(acctList.get(0));
					trx.setIaccountID(trx.getTblaccount().getIaccountId());
					
					List<Tblinvestment> investmentList = investmentDAOReference.inquire(new Integer(paycheckOutflowAdd.getInvestmentID()));
					trx.setTblinvestment(investmentList.get(0));
					trx.setIinvestmentID(trx.getTblinvestment().getIinvestmentId());
					
					List<Tbltransactiontype> trxTypeList = trxTypeDAOReference.inquire(new Integer(paycheckOutflowAdd.getTransactionTypeID()));
					trx.setTbltransactiontype(trxTypeList.get(0));
					trx.setiTransactionTypeID(trx.getTbltransactiontype().getItransactionTypeId());
					
					if (paycheckOutflowAdd.getXferAccountID() != null)
					{	
						List<Tblaccount> xferAcctList = accountDAOReference.inquire(new Integer(paycheckOutflowAdd.getXferAccountID()));
						Tblaccount xferAcct = xferAcctList.get(0);
						trx.setXferAccountID(xferAcct.getIaccountId());
					}
					
					if (paycheckOutflowAdd.getWdCategoryID() != null)
					{	
						List<Tblwdcategory> wdcategoryList = wdCategoryDAOReference.inquire(new Integer(paycheckOutflowAdd.getWdCategoryID()));
						trx.setTblwdcategory(wdcategoryList.get(0));
						trx.setiWDCategoryID(trx.getTblwdcategory().getIwdcategoryId());
					}
					
					if (paycheckOutflowAdd.getCashDepositTypeID() != null)
					{	
						List<Tblcashdeposittype> cdTypeList = cdTypeDAOReference.inquire(new Integer(paycheckOutflowAdd.getCashDepositTypeID()));
						trx.setTblcashdeposittype(cdTypeList.get(0));
						trx.setiCashDepositTypeID(trx.getTblcashdeposittype().getIcashDepositTypeId());
					}
						
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new DAOException(e1);
				}
				
				trx.setMeffectiveAmount(PASUtil.getEffectiveAmount(trx.getMcostProceeds(),trx.getTbltransactiontype().getSdescription()));
					
				//always add these fields
				Calendar now = Calendar.getInstance();			
			 	java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTimeInMillis());
				trx.setDtranEntryDate(timestamp);
				trx.setDtranChangeDate(timestamp);
			    
				//insert the row
				doTrxInsert(trx);		
				
				if (!trx.isSpawnedXfer())
				{
					if (trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
					||	trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
					{
						//remove this object from Hibernate's awareness so we can play with it...
						trx.setItransactionId(null);
						
						try
						{
							addSpawnedXfer(trx);
						}
						catch (SystemException e)
						{
							log.error("SystemException encountered: " +e.getMessage());
							e.printStackTrace();
							throw new DAOException(e);
						}
					}
				}	
				//remove this object from Hibernate's awareness so we can do another one...								
			}
		}
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	private void doTrxInsert(Tbltransaction trx) 
	{
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tbltransaction (");
		sbuf.append("iAccountID, iInvestmentID, iTransactionTypeID, dTransactionDate, decUnits, mPrice, mCostProceeds,");
		sbuf.append("dTranEntryDate, dTranPostedDate, dTranChangeDate, iDividendTaxableYear, mEffectiveAmount,");
		sbuf.append("iOptionTypeID, mStrikePrice, dExpirationDate, bOpeningTrxInd, iCheckNo, sDescription, iWDCategoryID, iCashDepositTypeID, bFinalTrxOfBillingCycle");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");	
		
		log.info("about to run insert on tbltransaction.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {trx.getIaccountID(), trx.getIinvestmentID(), trx.getiTransactionTypeID(),
				trx.getDtransactionDate(), trx.getDecUnits(), trx.getMprice(), trx.getMcostProceeds(), trx.getDtranEntryDate(), trx.getDtranPostedDate(),
				trx.getDtranChangeDate(), trx.getIdividendTaxableYear(), trx.getMeffectiveAmount(), trx.getiOptionTypeID(), trx.getMstrikePrice(),
				trx.getDexpirationDate(), trx.isBopeningTrxInd(), trx.getIcheckNo(), trx.getSdescription(),trx.getiWDCategoryID(), 
				trx.getiCashDepositTypeID(), trx.isBfinalTrxOfBillingCycle()});			
	}
	
	@SuppressWarnings("unchecked")
	private void addSpawnedExerciseOption(Tbltransaction trx) throws DAOException, SystemException
	{
		//first task is to find the the transaction that was the opening trx for this exercise
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltransaction trx");
		sbuf.append(" where trx.bopeningTrxInd = 1");
		sbuf.append(" and trx.tbloptiontype.ioptionTypeId = ");
		sbuf.append(trx.getiOptionTypeID().toString());
		sbuf.append(" and trx.dexpirationDate = '");
		sbuf.append(trx.getDexpirationDate().toString());
		sbuf.append("' and trx.mstrikePrice = ");
		sbuf.append(trx.getMstrikePrice().toString());
		sbuf.append(" and trx.tblinvestment.iinvestmentId = ");
		sbuf.append(trx.getIinvestmentID().toString());
		
		List<Tbltransaction> openingTrxList = new ArrayList();		
		
		if (openingTrxList.size() ==0) //should find exactly one of these, if not, return and log an error.
		{
			log.error("unable to find an opening Option transaction.  I used this query: " + sbuf.toString());
			return;
		}
		
		Tbltransaction openingTrx = openingTrxList.get(0);
						
		String optionType = trx.getTbloptiontype().getSdescription();
		String openingTrxTypeDesc = openingTrx.getTbltransactiontype().getSdescription();
		String trxTypeToGenerate = "";
		
		//derive whether this is a "buy" or "sell" depending on what type of option was exercised
		//Sold put exercised = Buy shares
		//Sold call exercised = Sell shares
		//Bought put exercised = Sell shares
		//Bought call exercised = Buy shares
		
		if (openingTrxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))
			if (optionType.equalsIgnoreCase(ISlomFinAppConstants.OPTIONTYPE_PUT))
				trxTypeToGenerate = ISlomFinAppConstants.TRXTYP_BUY;
			else
				trxTypeToGenerate = ISlomFinAppConstants.TRXTYP_SELL;
		else // opening trx must be a Buy
			if (optionType.equalsIgnoreCase(ISlomFinAppConstants.OPTIONTYPE_PUT))
				trxTypeToGenerate = ISlomFinAppConstants.TRXTYP_SELL;
			else
				trxTypeToGenerate = ISlomFinAppConstants.TRXTYP_BUY;
		
		TransactionTypeDAO trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
		List<Tbltransactiontype> trxTypeList =	trxTypeDAOReference.inquire(trxTypeToGenerate);		
	
		if (trxTypeList.size()>0)
		{	
			Tbltransactiontype trxTypeCompanion = trxTypeList.get(0);
			trx.setTbltransactiontype(trxTypeCompanion);
		}
		
		//now figure out what the underlying Stock is...		
		
	    UnderlyingStocksDAO undDAOReference = (UnderlyingStocksDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.UNDERLYINGSTOCKS_DAO);
	    List<Tblunderlyingstocks> undList =	undDAOReference.inquire(trx.getTblinvestment().getIinvestmentId());		
	
		if (undList.size() ==0) //should find exactly one of these, if not, return and log an error.
		{
			log.error("unable to find an opening Option transaction.  I used this query: " + sbuf.toString());
			return;
		}
		
		Tblunderlyingstocks tblunderlyingstocks = undList.get(0);
		BigDecimal optionMultiplier = tblunderlyingstocks.getTblinvestmentByIInvestmentId().getDoptionMultiplier();			
		Integer underlyingID = tblunderlyingstocks.getTblinvestmentByIUnderlyingInvId().getIinvestmentId();
		
		//lets say SFB (SPY options is the ID in tblUnderlyingstocks.
		//this means underlying ID will contain the ID of SPY, the underlying stock.
		//need to go get this investment now so we can re-populate the trx we're saving with that investment.
		
		InvestmentDAO investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
		List<Tblinvestment> investmentList = investmentDAOReference.inquire(underlyingID);
		if (investmentList.size() == 0) //should find exactly one of these, if not, return and log an error.
		{
			log.error("unable to find the underlying Investment for this Option.  I used this id: " + underlyingID.toString());
			return;
		}
		
		trx.setTblinvestment(investmentList.get(0));			
		
		BigDecimal trxAmt = new BigDecimal(0.0);
		trxAmt = optionMultiplier.multiply(trx.getMstrikePrice());
		trx.setMcostProceeds(trxAmt);
		
		BigDecimal unitsAmt = new BigDecimal(0.0);
		unitsAmt = optionMultiplier.multiply(trx.getDecUnits());
		trx.setDecUnits(unitsAmt);
		
		trx.setMprice(trx.getMstrikePrice());
		
		trx.setDexpirationDate(null);
		trx.setMstrikePrice(null);		
		
		trx.setMeffectiveAmount(PASUtil.getEffectiveAmount(trxAmt,trx.getTbltransactiontype().getSdescription()));
	}
	
	private void addSpawnedXfer(Tbltransaction trx) throws DAOException, SystemException
	{
		//first get details about the account we're transferring to or from
		
		log.info("entering addSpawnedXfer");
		
		AccountDAO acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
		List<Tblaccount> acctList =	acctDAOReference.inquire(trx.getXferAccountID());		
			
		String trxdesc = "";
		String companionTrxDesc = "";
		
		if (trx.getTbltransactiontype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
		{	trxdesc = "From: ";
			companionTrxDesc = ISlomFinAppConstants.TRXTYP_TRANSFERIN;
		}
		else
		{	
			trxdesc = "To: ";
			companionTrxDesc = ISlomFinAppConstants.TRXTYP_TRANSFEROUT;
		}
		
		if (acctList.size() > 0)
		{	
			Tblaccount accToXferFromOrTo = acctList.get(0);
			trxdesc = trxdesc + trx.getTblaccount().getSaccountNameAbbr();
			trx.setTblaccount(accToXferFromOrTo);
			trx.setIaccountID(trx.getTblaccount().getIaccountId());
		}
		
		trx.setSdescription(trxdesc);
		
		//now need to get the companion trx Type
		TransactionTypeDAO trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
		List<Tbltransactiontype> trxTypeList =	trxTypeDAOReference.inquire(companionTrxDesc);		
	
		if (trxTypeList.size()>0)
		{	
			Tbltransactiontype trxTypeCompanion = trxTypeList.get(0);
			trx.setTbltransactiontype(trxTypeCompanion);
			trx.setiTransactionTypeID(trx.getTbltransactiontype().getItransactionTypeId());
		}
		
		trx.setMeffectiveAmount(PASUtil.getEffectiveAmount(trx.getMcostProceeds(),trx.getTbltransactiontype().getSdescription()));
		
		trx.setSpawnedXfer(true);
		
		log.info("about to call transactionDAO add method from addSpawnedXfer");
		
		add(trx);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltransaction> inquire(Object info) throws DAOException 
	{		
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		boolean limitResults = false;
		boolean doBalances = false;
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select trx.*, acc.*, acctyp.*, portf.*, trxtyp.*, inv.*, invtyp.*, opttyp.*, wdcat.*, cdeptyp.*, asscl.*, brk.*, invstr.*, txg.*,");
		sbuf.append(" trx.sDescription as trxDescription, trxtyp.sDescription as trxTypeDescription, ");
		sbuf.append(" inv.sDescription as investmentDescription, invtyp.sDescription as investmentTypeDescription,");
		sbuf.append(" opttyp.sDescription as optTypeDescription");
		sbuf.append("  from tbltransaction trx");
		sbuf.append(" inner join tblaccount acc on trx.iAccountID = acc.iAccountID"); 
		sbuf.append(" inner join tblportfolio portf on acc.iPortfolioID = portf.iPortfolioID");
		sbuf.append(" inner join tblinvestor invstr on portf.iinvestorID = invstr.iinvestorID");
		sbuf.append(" inner join tbltransactiontype trxtyp on trx.itransactiontypeID = trxtyp.itransactiontypeID");
		sbuf.append(" inner join tblinvestment inv on trx.iinvestmentID = inv.iInvestmentID");
		sbuf.append(" inner join tblassetclass asscl on inv.iassetclassid = asscl.iassetclassid");
		sbuf.append(" inner join tblinvestmenttype invtyp on inv.iinvestmenttypeID = invtyp.iInvestmenttypeID");
		sbuf.append(" inner join tblaccounttype acctyp on acc.iaccounttypeid = acctyp.iaccounttypeid");
		sbuf.append(" left join tbltaxgroup txg on invstr.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblbroker brk on acc.ibrokerid = brk.ibrokerid"); 
		sbuf.append(" left join tbloptiontype opttyp on trx.iOptiontypeID = opttyp.iOptiontypeID");
		sbuf.append(" left join tblwdcategory wdcat on trx.iwdCategoryID = wdcat.iwdCategoryID");
		sbuf.append(" left join tblcashdeposittype cdeptyp on trx.icashdeposittypeID = cdeptyp.icashdeposittypeID");
				    
		if (info instanceof Tbltransaction) //looking for a particular row on the database
		{
			Tbltransaction tblt = (Tbltransaction)info;
			String trxID = tblt.getItransactionId().toString();
						
			log.debug("Transaction object provided - will perform a single row retrieval from DB");
			
			sbuf.append(" where trx.itransactionId = " + trxID);
			
			log.debug(methodName + "before inquiring for Transaction. Key value is = " + trxID);
					
		}
		else if (info instanceof TransactionSearch)
		{
			//this is an inquire request for a search on some text possibly by date
			
			log.debug("TransactionSearch object provided - will search for transactions");
						
			TransactionSearch trxSearch = (TransactionSearch)info;
						
			String searchText = "#$@DFGERH"; //first-time trick: this will never be found if the string is null leave this in
			
			if (trxSearch.getTrxDescriptionText() != null)
				searchText = trxSearch.getTrxDescriptionText();
			
			trxSearch.getTrxSearchInvestorID();
			
			//only search on this investor...
			sbuf.append(" where portf.iinvestorId = " + trxSearch.getTrxSearchInvestorID());
			sbuf.append(" and trx.sdescription like '%");
			sbuf.append(searchText);
			sbuf.append("%'");
			
			String fromDate = trxSearch.getFromDate();
			if (fromDate != null)
				if (fromDate.trim().length() >= 0)
				{
					sbuf.append(" and trx.dtransactionDate >= '");
					sbuf.append(fromDate);
					sbuf.append("'");
				}
			
			String toDate = trxSearch.getToDate();
			if (toDate != null)
				if (toDate.trim().length() >= 0)
				{
					sbuf.append(" and trx.dtransactionDate <= '");
					sbuf.append(toDate);
					sbuf.append("'");
				}
			
			sbuf.append(" order by trx.iaccountId asc, trx.dtransactionDate desc");
					
		}
		else if (info instanceof BudgetSelection)
		{
			//this is an inquire request for a list of WDCategory Trx			
							
			BudgetSelection budgetSel = (BudgetSelection)info;
			
			sbuf.append(" where portf.iinvestorId = ");
			sbuf.append(budgetSel.getBudgetInvestorID());
			sbuf.append(" AND YEAR(dtransactionDate) = ");
			sbuf.append(budgetSel.getBudgetYear());
			sbuf.append(" AND wdcat.iwdcategoryId IS NOT NULL");
			sbuf.append(" AND wdcat.iwdcategoryId NOT IN (37, 38)"); //unknown and not tracked
			sbuf.append(" ORDER by wdcat.swdcategoryDescription, dtransactiondate");
			
		}
		else	//user wants a list of Transactions - search criteria in TransactionSelection object		
		{            			
			log.debug("Will perform multiple rows retrieval for Transactions");
				
			TransactionSelection trxSel = (TransactionSelection)info;
			
			if (trxSel.isRecentInd())
			{
				log.debug("trxSel.setRecentInd is true inside TransactionDAO");	
				limitResults = true;
			}				
			else
			{
				log.debug("trxSel.setRecentInd is false inside TransactionDAO");				
			}
			
			boolean whereDone = false;
			
			if (trxSel.getAccountID() != null)
			{
				doBalances = true;
				sbuf.append(" where acc.iaccountId = " + trxSel.getAccountID());
				whereDone = true;
			}
			
			if (trxSel.getInvestmentList() != null)
			{
				if (whereDone)
				{
					sbuf.append(" and inv.iinvestmentId IN (" + trxSel.getInvestmentList() + ")");	
				}
				else
				{	
					sbuf.append(" where inv.iinvestmentId IN (" + trxSel.getInvestmentList() + ")");	
					whereDone = true;
				}
			}
			
			//may also sometimes have date parameters
			if (trxSel.getFromDate() != null)
			{
				if (whereDone)
				{
					sbuf.append(" and trx.dtransactionDate >= '" + trxSel.getFromDate() + "'");
				}
				else
				{	
					sbuf.append(" where trx.dtransactionDate >= '" + trxSel.getFromDate() + "'");
					whereDone = true;
				}
			}
			
			if (trxSel.getToDate() != null)
			{
				if (whereDone)
				{
					sbuf.append(" and trx.dtransactionDate <= '" + trxSel.getToDate() + "'");
				}
				else
				{	
					sbuf.append(" where trx.dtransactionDate <= '" + trxSel.getToDate() + "'");
					whereDone = true;
				}	
			}
			
			sbuf.append(" order by acc.iaccountId asc, trx.dtranPostedDate desc");
			
		}
				
		log.debug("about to run query: " + sbuf.toString());
		
		
		if (limitResults)
		{
			//hQuery.setMaxResults(TRX_MAX_RESULTS);
		}
		
		List<Tbltransaction> transactionList = this.getJdbcTemplate().query(sbuf.toString(), new TblTransactionRowMapper())	; 
			
		if (doBalances)
		{
			AccountDAO acctDAOReference;
			
			try
			{
				acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
				TransactionSelection trxSel = (TransactionSelection)info;
				BigDecimal acctBalance = acctDAOReference.getAccountBalance(trxSel.getAccountID());
				log.debug("account balance retrieved is = " + acctBalance);
				BigDecimal priorAmount = new BigDecimal(0.0);
				
				//now need to update the balances in the list
				//use the acctBalance to derive the rest
				for (int i = 0; i < transactionList.size(); i++)
				{
					Tbltransaction tblt = transactionList.get(i);
					
					if (i>0) //already have acct balance for first row
					   acctBalance = acctBalance.subtract(priorAmount);
					
					tblt.setMacctBalance(acctBalance);
					transactionList.set(i,tblt);
					priorAmount = tblt.getMeffectiveAmount();
					
				}
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new DAOException(e1);
			}
		}
		
		log.debug("final list size is = " + transactionList.size());
		log.debug(methodName + "out");
		return transactionList;	
	}
	
	public List<Tbltransaction> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TransactionDAO update");
							
		Tbltransaction trx = (Tbltransaction)Info;
		
		Calendar now = Calendar.getInstance();			
	 	java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTimeInMillis());
		trx.setDtranChangeDate(timestamp);
		
		//the following are nullable; and if we have an integer value of zero we need to null these out
		
		if (trx.getiOptionTypeID() == 0)
		{
			trx.setiOptionTypeID(null);
		}
		
		if (trx.getiWDCategoryID() == 0)
		{
			trx.setiWDCategoryID(null);
		}
		if (trx.getiCashDepositTypeID() == 0)
		{
			trx.setiCashDepositTypeID(null);
		}
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tbltransaction set iAccountID = ?, iInvestmentID = ?, iTransactionTypeID = ?, dTransactionDate = ?, decUnits = ?, mPrice = ?, mCostProceeds = ?,");
		sbuf.append("dTranEntryDate = ?, dTranPostedDate = ?, dTranChangeDate = ?, iDividendTaxableYear = ?, mEffectiveAmount = ?,");
		sbuf.append("iOptionTypeID = ?, mStrikePrice = ?, dExpirationDate = ?, bOpeningTrxInd = ?, iCheckNo = ?,");
		sbuf.append("sDescription = ?, iWDCategoryID = ?, iCashDepositTypeID = ?, bFinalTrxOfBillingCycle = ?");
		sbuf.append(" where iTransactionID = ?");	
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {trx.getIaccountID(), trx.getIinvestmentID(), trx.getiTransactionTypeID(),
				trx.getDtransactionDate(), trx.getDecUnits(), trx.getMprice(), trx.getMcostProceeds(), trx.getDtranEntryDate(), trx.getDtranPostedDate(),
				trx.getDtranChangeDate(), trx.getIdividendTaxableYear(), trx.getMeffectiveAmount(), trx.getiOptionTypeID(), trx.getMstrikePrice(),
				trx.getDexpirationDate(), trx.isBopeningTrxInd(), trx.getIcheckNo(), trx.getSdescription(),trx.getiWDCategoryID(), 
				trx.getiCashDepositTypeID(), trx.isBfinalTrxOfBillingCycle(), trx.getItransactionId()});	
			
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltransaction> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TransactionDAO delete");
						
		Tbltransaction trx = (Tbltransaction)Info;
		
		Integer trxID = trx.getItransactionId();
		
		log.info("about to delete transaction id: " + trxID);
		
		String deleteStr = "delete from tbltransaction where itransactionid = ?";
		this.getJdbcTemplate().update(deleteStr, trxID);
		
		log.info("successfully deleted transaction id: " + trxID);
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<String> searchDistinct(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");				
			
		//this is an inquire request for a search on some text			
								
		TransactionSearch trxSearch = (TransactionSearch)info;
							
		String searchText = "#$@DFGERH"; //first-time trick: this will never be found if the string is null leave this in
			
		if (trxSearch.getTrxDescriptionText() != null)
		{
			searchText = trxSearch.getTrxDescriptionText();
		}
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select distinct trx.sdescription");
		sbuf.append("  from Tbltransaction trx, Tblaccount acct, Tblportfolio portf");
		sbuf.append(" where trx.iaccountid = acct.iaccountid");
		sbuf.append("   and acct.iPortfolioid = portf.iportfolioid");
		sbuf.append(" and trx.sdescription like '");
		sbuf.append(searchText + "%'");
		sbuf.append(" and portf.iinvestorId = ");
		sbuf.append(trxSearch.getTrxSearchInvestorID().toString());										
		
		List<String> descriptionsList = this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List<String>>() 
		{	   
			@Override
		    public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List<String> tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
			        tempList.add(rs.getString(1));			        
				}
				return tempList;
		    }
		});
						
		log.debug("final list size is = " + descriptionsList.size());
		log.debug(methodName + "out");
		return descriptionsList;	
	}
	
}
