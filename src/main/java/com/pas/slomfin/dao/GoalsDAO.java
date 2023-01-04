package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Goals;
import com.pas.slomfin.valueObject.GoalsSelection;
import com.pas.slomfin.valueObject.PortfolioSummary;
import com.pas.util.Utils;

/**
 * Title: 		GoalsDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class GoalsDAO extends BaseDBDAO
{
    private static final GoalsDAO currentInstance = new GoalsDAO();

    private GoalsDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return GoalsDAO
     */
    public static GoalsDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	public List<Goals> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		List<Goals> GoalsList = new ArrayList<Goals>();
			
		GoalsSelection goalsSelection = (GoalsSelection)Info;
		
		Integer investorID = goalsSelection.getGoalsInvestorID();		
		Double rateOfReturn = goalsSelection.getGoalsRateOfReturn().doubleValue() * .01;
		Integer projectionYears = goalsSelection.getGoalsProjectionYears();				

		PortfolioDAO portfolioDAOReference;
		List<PortfolioSummary> portfolioSummaryList = null;
		
		try
		{
			portfolioDAOReference = (PortfolioDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PORTFOLIO_DAO);
			portfolioSummaryList = portfolioDAOReference.portfolioSummaryBuild(investorID.toString());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new DAOException(e1);
		}
				
		log.debug("Size of portfoliosummaryList retrieved is: " + portfolioSummaryList.size());
		
		//first thing we need to do is pare this list back so that it only contains Sums by account
		
		List<PortfolioSummary> portfolioSummaryListPared = portfolioSummarySumsByAccount(portfolioSummaryList);
		
		//Now that we have sums by account, need to create goals objects from these.
		
		log.debug("Creating projections: rate of Return is = " + rateOfReturn);
		log.debug("Creating projections: projection years = " + projectionYears);
		
		for (int i = 0; i < portfolioSummaryListPared.size(); i++)
		{
			PortfolioSummary portSummPared = portfolioSummaryListPared.get(i);
			
			//First, take care of how much this account will be worth.
			Goals goalsDetail = new Goals();	
			
			Double rateToUse = rateOfReturn;
			if (portSummPared.getAccountType().equalsIgnoreCase((ISlomFinAppConstants.ACCOUNTTYPE_PENSION)))
				rateToUse = rateToUse * .5; //divide in 2 if pension - these are fixed investments
			
			goalsDetail.setAccountName(portSummPared.getAccountName());			
			goalsDetail.setPortfolioName(portSummPared.getPortfolioName());	
			
			Double futureAccountValue = portSummPared.getCurrentValue().doubleValue() * Math.pow( 1.0 + rateToUse, projectionYears.doubleValue() );
			
			goalsDetail.setAccountValue(new BigDecimal(futureAccountValue));
			
			GoalsList.add(goalsDetail);
			
			//Next, we may need to project new money added to this account
			
			Double newMoney = portSummPared.getNewMoney().doubleValue();
			
			if (newMoney > 0)
			{	
				Goals goalsDetail2 = new Goals();	
								
				goalsDetail2.setAccountName(portSummPared.getAccountName() + " - New Money");			
				goalsDetail2.setPortfolioName(portSummPared.getPortfolioName());	
				
				Double futureAccountValueNewMoney = newMoney;
				
				for (int j = 1; j <= projectionYears; j++)
				{
					if (j > 1) //first time through, just use the value
					{
						futureAccountValueNewMoney = futureAccountValueNewMoney * Math.pow( 1.0 + rateToUse,1);						
						futureAccountValueNewMoney = futureAccountValueNewMoney + newMoney;
						log.debug("In year " + j + " the value is = " + futureAccountValueNewMoney);
					}
				}
				goalsDetail2.setAccountValue(new BigDecimal(futureAccountValueNewMoney));
				
				GoalsList.add(goalsDetail2);
			}
			
		}
			
		log.debug("final list size is = " + GoalsList.size());
		log.debug(methodName + "out");
		return GoalsList;	
	}
	
	public List<PortfolioSummary> portfolioSummarySumsByAccount(List<PortfolioSummary> portfolioSummaryList)
	{
		//Only take account types that are "retirement investing" types.
		//With money market accounts that are associated with brokerages, add these amounts in.
		
		String lastAcctName = "@#%!@^";
		String lastPortfolioName = "#%^";
		String lastAccountType = "$@@%";
		String lastAssetClass = "@#%@";
		Double lastNewMoney = 0.0;
		
		Double acctTotal = new Double(0.0);
		
		List<PortfolioSummary> portfolioSummaryListPared = new ArrayList<PortfolioSummary>();
				
		//the input list comes ordered by Portfolio Name and account Name
		
		for (int i = 0; i < portfolioSummaryList.size(); i++)
		{
			PortfolioSummary portSumm = portfolioSummaryList.get(i);
						
			String acctName = portSumm.getAccountName();
			
			if (!lastAcctName.equalsIgnoreCase("@#%!@^")) //do nothing if first time through
			{
			   if (!acctName.equalsIgnoreCase(lastAcctName)) //different account name
			   {
				   if (acctTotal > 0)
			   	   {
			    	  if (acctName.substring(0, 3).equalsIgnoreCase(lastAcctName.substring(0, 3))
			    	  &&  (acctName.endsWith("Money Mkt") || acctName.endsWith("MnyMkt")))
			          {
			    		  log.debug("Not writing portSummAdd - account name is " + acctName);
			          } 
			    	  else //we need to write this out
			    	  {	  
			    		  PortfolioSummary portSummAdd = new PortfolioSummary();
			    		  portSummAdd.setAccountName(lastAcctName);
			    		  portSummAdd.setPortfolioName(lastPortfolioName);
			    		  portSummAdd.setCurrentValue(new BigDecimal(acctTotal));
			    		  portSummAdd.setAccountType(lastAccountType);
			    		  portSummAdd.setAssetClass(lastAssetClass);
			    		  portSummAdd.setNewMoney(new BigDecimal(lastNewMoney));
			    		  portfolioSummaryListPared.add(portSummAdd);
			    		  log.debug("Writing Portfolio = " + lastPortfolioName + " Account = " + lastAcctName + " Value = " + acctTotal);
			    		  acctTotal = 0.0; //reset for next account
			          }
			     	  
			   	   }
				   //reset last new money
				   lastNewMoney = 0.0;
			   }
			}
			
			String acctType = portSumm.getAccountType();
			
			if (acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TAXABLEBROKERAGE)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTHIRA)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTH401K)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_401K)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_PENSION)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TRADIRA))
			{
				acctTotal = acctTotal + portSumm.getCurrentValue().doubleValue();
				lastAcctName = portSumm.getAccountName();
				lastAccountType = acctType;
				lastAssetClass = portSumm.getAssetClass();
				if (portSumm.getNewMoney() == null || portSumm.getNewMoney().compareTo(new BigDecimal(0.0)) == 0)
				{
					if (lastNewMoney > 0) //this could be the cash account for a brokerage, ignore it
					{
						//do nothing
					}
					else
					{
						lastNewMoney = 0.0;
					}
				}
				else
				{
					lastNewMoney = portSumm.getNewMoney().doubleValue();
				}
			}
			else //need to check if this is the money market account associated with the brokerage account
				if (acctName.substring(0, 3).equalsIgnoreCase(lastAcctName.substring(0, 3))
				&&  (acctName.endsWith("Money Mkt") || acctName.endsWith("MnyMkt")))
					acctTotal = acctTotal + portSumm.getCurrentValue().doubleValue();
				else
				{	
					lastAcctName = portSumm.getAccountName();
					lastAccountType = acctType;
					lastAssetClass = portSumm.getAssetClass();
					if (portSumm.getNewMoney() == null)
						lastNewMoney = 0.0;
					else
					    lastNewMoney = portSumm.getNewMoney().doubleValue();
				}	
			lastPortfolioName = portSumm.getPortfolioName();				
						
		}
		
		if (acctTotal > 0) //need to capture the last one in the list
		{
			PortfolioSummary portSummAdd = new PortfolioSummary();
   		  	portSummAdd.setAccountName(lastAcctName);
   		  	portSummAdd.setPortfolioName(lastPortfolioName);
   		  	portSummAdd.setCurrentValue(new BigDecimal(acctTotal));
   		  	portSummAdd.setNewMoney(new BigDecimal(lastNewMoney));
   		  	portSummAdd.setAccountType(lastAccountType);
   		    portSummAdd.setAssetClass(lastAssetClass);
   		  	portfolioSummaryListPared.add(portSummAdd);
   		  	log.debug("Writing Portfolio = " + lastPortfolioName + " Account = " + lastAcctName + " Value = " + acctTotal);
 		}
		
		return portfolioSummaryListPared;
		
	}

}
