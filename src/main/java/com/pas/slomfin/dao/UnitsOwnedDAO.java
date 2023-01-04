package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Dividend;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.slomfin.valueObject.UnitsOwned;
import com.pas.util.Utils;

/**
 * Title: 		UnitsOwnedDAO
 * Project: 	Slomkowski Financial Application
 * Description: UnitsOwned DAO extends BaseDBDAO. Implements the data access to the tblPortfolio table
 * Copyright: 	Copyright (c) 2006
 */
public class UnitsOwnedDAO extends BaseDBDAO
{
	private static final UnitsOwnedDAO currentInstance = new UnitsOwnedDAO();

    private UnitsOwnedDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return PortfolioDAO
     */
    public static UnitsOwnedDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
    @SuppressWarnings("unchecked")
	public List inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		List<UnitsOwned> unitsOwnedList = new ArrayList<UnitsOwned>();
				
		IDObject idObject = (IDObject)Info;	
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append(" select invm.sdescription as investmentDescription,"); 
		sbuf.append("    SUM(CASE trxtyp.bpositiveInd");  
		sbuf.append("          WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) as unitsOwned"); 
		sbuf.append("   from Tbltransaction trx"); 
		sbuf.append("  INNER JOIN Tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid"); 
		sbuf.append("  INNER JOIN Tblaccount acct on trx.iaccountid = acct.iaccountid"); 
		sbuf.append("  INNER JOIN tblaccounttype acctyp on acct.iaccounttypeid = acctyp.iAccountTypeID");
		sbuf.append(" INNER JOIN Tblportfolio portf on acct.iportfolioid = portf.iportfolioid"); 
		sbuf.append(" INNER JOIN Tblinvestor invstr on portf.iinvestorid = invstr.iinvestorid"); 
		sbuf.append(" INNER JOIN Tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid"); 
		sbuf.append("  INNER JOIN tblinvestmenttype invmtyp on invm.iInvestmentTypeID = invmtyp.iInvestmentTypeID");
		sbuf.append("  where invstr.itaxGroupId = ");
		sbuf.append("   (select inv.itaxGroupId"); 
		sbuf.append("      from Tblinvestor inv"); 		
		sbuf.append("    where inv.iinvestorId = ");
		sbuf.append(idObject.getId()); //this is the investor id
        sbuf.append(")");	
		sbuf.append("   and invmtyp.sdescription <> 'Real Estate'"); 
		sbuf.append("   and acctyp.saccountType <> '529 Plan'"); 
		sbuf.append("   group by invm.sdescription");  
		sbuf.append("  having SUM(CASE trxtyp.bpositiveInd"); 
		sbuf.append("     WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) <> 0");
		
		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					UnitsOwned uo = new UnitsOwned();
					uo.setInvestmentDescription(rs.getString(1));
					uo.setUnitsOwned(rs.getBigDecimal(2));
					unitsOwnedList.add(uo);
				}
				return tempList;
		    }
		});		
										
		return unitsOwnedList;	
							
	}
		
}
