package com.pas.slomfin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinancialUtilities 
{
	protected static Logger log = LogManager.getLogger(FinancialUtilities.class);  

		
	public FinancialUtilities() 
	{		
	}
	
	public String dividendsQuery(Integer requestedYear, boolean taxableOnly, Integer taxGroupID)
	{
		String returnQuery = "";
		
		StringBuffer sbuf = new StringBuffer();
			
		sbuf.append(" select acc.IaccountId, acc.SaccountNameAbbr, trx.DtranPostedDate, trx.IdividendTaxableYear, invm.sdescription as invDesc,");					
		sbuf.append(" trx.IinvestmentId, trx.McostProceeds, portf.SportfolioName, portf.bTaxableInd, trx.decUnits");
		sbuf.append(" from Tbltransaction trx"); 
		sbuf.append(" INNER JOIN Tblaccount acc on trx.iaccountid = acc.iaccountid"); 
		sbuf.append(" INNER JOIN Tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid");
		sbuf.append(" INNER JOIN TblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append(" INNER JOIN Tblinvestor invstr on portf.iinvestorid = invstr.iinvestorid");
		sbuf.append(" INNER JOIN TbltransactionType trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");	
		sbuf.append(" where trxtyp.sdescription IN ('Cash Dividend', 'Reinvest')");
				
		if (requestedYear != null && requestedYear > 0) // means a specific year picked
		{	
			sbuf.append(" and trx.idividendTaxableYear = ");			
			sbuf.append(requestedYear);
		}
		
		if (taxableOnly)
		{	
			sbuf.append(" and portf.btaxableInd = true");
		}
		
		sbuf.append(" and invstr.itaxGroupId = ");
		sbuf.append(taxGroupID);
		sbuf.append(" order by trx.dtranposteddate");
		
		returnQuery = sbuf.toString();
		
		return returnQuery;
	}
	
}
