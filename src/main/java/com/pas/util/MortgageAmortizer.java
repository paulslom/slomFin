package com.pas.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.dbobjects.Tblmortgage;
import com.pas.dbobjects.Tblmortgagehistory;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.MortgageDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.MortgageAmortization;

public class MortgageAmortizer
{
	protected static Logger log = LogManager.getLogger(DateUtil.class);	
	
	@SuppressWarnings("unchecked")
	public static List<MortgageAmortization> getFullMortgageAmortization(Integer mortgageID) 
							throws DAOException
	{
		Tblmortgage mortgage = new Tblmortgage();
		
		//first, retrieve this mortgage.
		
		MortgageDAO mortgageDAOReference; 
		
		try
		{
			mortgageDAOReference = (MortgageDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGE_DAO);
			List<Tblmortgage> mortgageList = mortgageDAOReference.inquire(mortgageID);
			if (mortgageList.size() == 0)
			{
				log.debug("unable to retrieve mortgage");
				return null;
			}
			mortgage = mortgageList.get(0);
				
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new DAOException(e1);
		}
		
		//now that you have the mortgage, calculate some variables we'll need later
		
		int lengthOfLoanInMonths = mortgage.getItermInYears() * 12;
		log.debug("Length of Loan in Months is = " + lengthOfLoanInMonths);
		
		log.debug("Yearly interest rate is = " + mortgage.getDinterestRate());
		
		double extraPrincipal = mortgage.getMextraPrincipal().doubleValue();
		log.debug("Extra principal is = " + extraPrincipal);
		
		double monthlyInterestRate = mortgage.getDinterestRate().doubleValue() / 12;
		log.debug("Monthly interest rate is = " + monthlyInterestRate);
		
		double originalLoanAmount = mortgage.getMoriginalLoanAmount().doubleValue();
		log.debug("Original Loan amount is = " + originalLoanAmount);
		
		double monthlyPaymentAmount = (originalLoanAmount*monthlyInterestRate) / (1 - 1 /Math.pow((1 + monthlyInterestRate), lengthOfLoanInMonths));
		monthlyPaymentAmount = PASUtil.getRoundedUpValue(monthlyPaymentAmount);
		
		if (mortgage.getImortgageId() == 4) //special logic for Bank Of America
		{
			monthlyPaymentAmount = monthlyPaymentAmount + .01;
		}
		log.debug("monthly payment amount is = " + monthlyPaymentAmount);
				
		log.debug("now retrieving actual payments already made on this mortgage..");
		
		//Go get payments already made
		
		StringBuffer sbufMortPayments = new StringBuffer();
		sbufMortPayments.append("select * from Tblmortgagehistory ");
		sbufMortPayments.append(" where tblmortgage.imortgageId = ");		
		sbufMortPayments.append(mortgageID.toString());
		sbufMortPayments.append(" and dpaymentDate >= tblmortgage.dmortgageStartDate");
		sbufMortPayments.append(" order by dpaymentDate");
		
		log.debug("retrieving mortgage payments for mortgage ID = " + mortgageID);
		
		List<Tblmortgagehistory> mortgagePaymentList = new ArrayList();		
		
		if (mortgagePaymentList.size() == 0) //may be ok, but not likely.
		{
			log.debug("no mortgage payments already on file ");			
		}
		
		Double mortgageBalance = new Double(mortgage.getMoriginalLoanAmount().doubleValue());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//Insert these payments into a list of MortgageAmortization objects
		List<MortgageAmortization> mortgageAmortizationList = new ArrayList();
		Date lastPaymentDate = null;
		int paymentCounter = 0;
		
		for (int i=0; i < mortgagePaymentList.size(); i++)
		{
			MortgageAmortization mortAmort = new MortgageAmortization();
			Tblmortgagehistory mortPmt = mortgagePaymentList.get(i);
			
			//only if interest was paid do we advance the payment number.
			if (mortPmt.getMinterestPaid().compareTo(new BigDecimal(0.0)) == 1)
				paymentCounter++;
			
			mortAmort.setMortgagePaymentNumber(paymentCounter);
			
			log.debug("Payment number = " + mortAmort.getMortgagePaymentNumber());
			
			mortAmort.setMortgagePaymentDate(new Date(mortPmt.getDpaymentDate().getTime()));
			mortAmort.setMortgagePaymentYear(mortPmt.getDpaymentDate());
			lastPaymentDate = new Date(mortPmt.getDpaymentDate().getTime());
			
			log.debug("Payment date = " + sdf.format(mortAmort.getMortgagePaymentDate()));
			
			Calendar tempCal = Calendar.getInstance();
			tempCal.setTimeInMillis(mortPmt.getDpaymentDate().getTime()); 
					
			//only if interest was paid do we need payment from and to dates
			if (mortPmt.getMinterestPaid().compareTo(new BigDecimal(0.0)) == 1)
			{	
				Date fromDate = DateUtil.getPriorMonthFirstDay(tempCal);
				Date toDate = DateUtil.getPriorMonthLastDay(tempCal);
				
				mortAmort.setMortgagePaymentFromDate(fromDate);
				mortAmort.setMortgagePaymentToDate(toDate);
				
				log.debug("Payment From Date = " + sdf.format(fromDate));
				log.debug("Payment To Date = " + sdf.format(toDate));
			}
			
			mortAmort.setExtraPrincipalPaid(new BigDecimal(0.0));
			
			mortAmort.setPrincipalPaid(mortPmt.getMprincipalPaid());
			log.debug("Principal paid = " + mortAmort.getPrincipalPaid());
			
			mortAmort.setInterestPaid(mortPmt.getMinterestPaid());
			log.debug("Interest paid = " + mortAmort.getInterestPaid());
			
			mortgageBalance = mortgageBalance - mortPmt.getMprincipalPaid().doubleValue();
			mortgageBalance = PASUtil.getRoundedUpValue(mortgageBalance);
			log.debug("New balance = " + mortgageBalance);
			
			mortAmort.setMortgageBalance(new BigDecimal(mortgageBalance.toString()));
			
			mortAmort.setActualPayment(true);
			
			mortgageAmortizationList.add(mortAmort);			
			
		}
		
		//Now generate payments that will be made in the future
		
		Date nextDate = new Date(lastPaymentDate.getTime());
		
		while (mortgageBalance > 0)
		{
			MortgageAmortization mortAmort = new MortgageAmortization();			
			
			mortAmort.setMortgagePaymentNumber(++paymentCounter);
			log.debug("Payment number = " + mortAmort.getMortgagePaymentNumber());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(nextDate);
			nextDate = DateUtil.getNextMonthFirstDay(cal);
			
			mortAmort.setMortgagePaymentDate(nextDate);
			mortAmort.setMortgagePaymentYear(nextDate);
			
			log.debug("Payment date = " + sdf.format(mortAmort.getMortgagePaymentDate()));
			
			Calendar tempCal = Calendar.getInstance();
			tempCal.setTimeInMillis(mortAmort.getMortgagePaymentDate().getTime()); 
					
			if (tempCal.get(Calendar.DAY_OF_MONTH) == 1) //only set from-to dates if payment was on the first
			{	
				Date fromDate = DateUtil.getPriorMonthFirstDay(tempCal);
				Date toDate = DateUtil.getPriorMonthLastDay(tempCal);
				
				mortAmort.setMortgagePaymentFromDate(fromDate);
				mortAmort.setMortgagePaymentToDate(toDate);
				
				log.debug("Payment From Date = " + sdf.format(fromDate));
				log.debug("Payment To Date = " + sdf.format(toDate));
			}
			
			mortAmort.setExtraPrincipalPaid(new BigDecimal(extraPrincipal));
			log.debug("Extra Principal paid = " + mortAmort.getExtraPrincipalPaid());
		
			double currentInterest = mortgageBalance * monthlyInterestRate;
			currentInterest = PASUtil.getRoundedUpValue(currentInterest);
			mortAmort.setInterestPaid(new BigDecimal(currentInterest));
			log.debug("Interest paid = " + mortAmort.getInterestPaid());
			
			double currentPrincipal = monthlyPaymentAmount - currentInterest;
			mortAmort.setPrincipalPaid(new BigDecimal(currentPrincipal));
			log.debug("Principal paid = " + mortAmort.getPrincipalPaid());			
			
			mortgageBalance = mortgageBalance - extraPrincipal;
			mortgageBalance = mortgageBalance - currentPrincipal;			
			mortgageBalance = PASUtil.getRoundedUpValue(mortgageBalance);
			log.debug("New balance = " + mortgageBalance);
			
			mortAmort.setMortgageBalance(new BigDecimal(mortgageBalance.toString()));
			
			mortAmort.setActualPayment(false);
			
			mortgageAmortizationList.add(mortAmort);			
			
		}
		
		return mortgageAmortizationList;
	}

	@SuppressWarnings("unchecked")
	public static List<Tblmortgagehistory> getNextMortgagePayment(Integer mortgageID) throws DAOException 
	{
		final String methodName = "getNextMortgagePayment::";
		log.debug(methodName + "in");		
		
		
		List<MortgageAmortization> paymentList = getFullMortgageAmortization(mortgageID);
		
		//loop through the list and take the first payment that is not an actual payment.
		
		Tblmortgagehistory tblmortgagehistory = new Tblmortgagehistory();
		
		for (int i = 0; i < paymentList.size(); i++)
		{
			MortgageAmortization mtgPmt = paymentList.get(i);
			
			if (!mtgPmt.isActualPayment())
			{
				tblmortgagehistory.setDpaymentDate(mtgPmt.getMortgagePaymentDate());				
				tblmortgagehistory.setMinterestPaid(mtgPmt.getInterestPaid());
				
				//now need to go get the other fields pertinent to the mortgage itself.
				
				Tblmortgage mortgage = new Tblmortgage();
				
				MortgageDAO mortgageDAOReference; 
				
				try
				{
					mortgageDAOReference = (MortgageDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGE_DAO);
					List<Tblmortgage> mortgageList = mortgageDAOReference.inquire(mortgageID);
					if (mortgageList.size() == 0)
					{
						log.debug("unable to retrieve mortgage");
						return null;
					}
					mortgage = mortgageList.get(0);
					tblmortgagehistory.setTblmortgage(mortgage);
						
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new DAOException(e1);
				}
				tblmortgagehistory.setMpmipaid(mortgage.getMpmi());		
				tblmortgagehistory.setMpropertyTaxesPaid(mortgage.getMpropertyTaxes());
				tblmortgagehistory.setMhomeownersInsPaid(mortgage.getMhomeOwnersIns());
				
				tblmortgagehistory.setMprincipalPaid(mtgPmt.getPrincipalPaid().add(mortgage.getMextraPrincipal()));
				
				break;
			}
			
		}
		
		List<Tblmortgagehistory> mhList = new ArrayList();
		mhList.add(tblmortgagehistory);
		log.debug(methodName + "out");
		
		return mhList;	
	}
	
	public static void main(String[] args)
	{
		try
        {
            // Create the SessionFactory from hibernate.cfg.xml
		
    	    List<Tblmortgagehistory> mhList = getNextMortgagePayment(4); //Mortgage 4 is Bank of America
    	    Tblmortgagehistory mh = mhList.get(0);
    	    log.debug("Next mortgage payment is as follows:");
    	    log.debug("Payment Date = " + mh.getDpaymentDate());
    	    log.debug("Principal = " + mh.getMprincipalPaid());
    	    log.debug("Interest = " + mh.getMinterestPaid());
    	    log.debug("Homeowners ins = " + mh.getMhomeownersInsPaid());
    	    log.debug("Property Taxes = " + mh.getMpropertyTaxesPaid());
    	    log.debug("PMI = " + mh.getMpmipaid());
        }
        catch (Throwable ex)
        {            
            System.err.println("Initial SessionFactory creation failed. " + ex);
        }
		
	}

}
