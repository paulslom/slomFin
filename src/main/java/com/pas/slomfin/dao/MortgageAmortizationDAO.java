package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.MortgageAmortization;
import com.pas.slomfin.valueObject.MortgagePaymentSelection;
import com.pas.util.MortgageAmortizer;
import com.pas.util.Utils;

/**
 * Title: 		MortgagePaymentDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class MortgageAmortizationDAO extends BaseDBDAO
{
    private static final MortgageAmortizationDAO currentInstance = new MortgageAmortizationDAO();

    private MortgageAmortizationDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return MortgagePaymentDAO
     */
    public static MortgageAmortizationDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }    
	
	@SuppressWarnings("unchecked")
	public List<MortgageAmortization> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		MortgagePaymentSelection mortgagePaymentSelection = (MortgagePaymentSelection)Info;
	
		Integer mortID = mortgagePaymentSelection.getMortgageID();
		
		List<MortgageAmortization> mortgagePaymentList = MortgageAmortizer.getFullMortgageAmortization(mortID);
		
		log.debug("final list size is = " + mortgagePaymentList.size());
		log.debug(methodName + "out");
		return mortgagePaymentList;	
	}

}
