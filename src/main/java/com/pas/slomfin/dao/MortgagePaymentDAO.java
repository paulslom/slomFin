package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblmortgagehistory;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.MortgagePaymentSelection;
import com.pas.util.MortgageAmortizer;
import com.pas.util.Utils;

/**
 * Title: 		MortgagePaymentDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class MortgagePaymentDAO extends BaseDBDAO
{	
    private static final MortgagePaymentDAO currentInstance = new MortgagePaymentDAO();

    private MortgagePaymentDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return MortgagePaymentDAO
     */
    public static MortgagePaymentDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
    	return currentInstance;
    }
    
	public List<Tblmortgagehistory> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblmortgagehistory mortgagePmt = (Tblmortgagehistory)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings("unchecked")
	public List<Tblmortgagehistory> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblmortgagehistory ");
		
		if (Info instanceof MortgagePaymentSelection)
		{
			MortgagePaymentSelection mortgagePaymentSelection = new MortgagePaymentSelection();
			mortgagePaymentSelection = (MortgagePaymentSelection)Info;
		
			if (mortgagePaymentSelection.getMortgagePaymentID() != null
			&&	mortgagePaymentSelection.getMortgagePaymentID()	== -1) //signal to go get next Payment for an add
			{
				return MortgageAmortizer.getNextMortgagePayment(mortgagePaymentSelection.getMortgageID());
			}
			
			sbuf.append(" where tblmortgage.imortgageId = ");
			sbuf.append(mortgagePaymentSelection.getMortgageID().toString());
			String fromDate = mortgagePaymentSelection.getFromDate();
			String toDate = mortgagePaymentSelection.getToDate();
			if (fromDate != null && toDate != null)
			{
				sbuf.append(" and dpaymentDate between '");
				sbuf.append(fromDate);
				sbuf.append("' and '");
				sbuf.append(toDate);
				sbuf.append("'");						
			}
			
			sbuf.append(" order by dpaymentDate desc");
			
		}
		else //must be an instance of an integer
		{
			Integer mortgagePmtID = (Integer)Info;
			sbuf.append(" where imortgageHistoryId = ");
			sbuf.append(mortgagePmtID.toString());
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblmortgagehistory> mortgagePaymentList = new ArrayList();		
							
		log.debug("final list size is = " + mortgagePaymentList.size());
		log.debug(methodName + "out");
		return mortgagePaymentList;	
	}
	
	public List<Tblmortgagehistory> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblmortgagehistory mortgagePmt = (Tblmortgagehistory)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblmortgagehistory> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		Tblmortgagehistory mortgagePmt = (Tblmortgagehistory)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
