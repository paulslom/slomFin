package com.pas.slomfin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.SystemException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TransactionDAO;
import com.pas.slomfin.valueObject.TransactionSearch;

public class SearchServlet extends HttpServlet
{	
	static Logger log = LogManager.getLogger(SearchServlet.class);
	
	protected void doGet(HttpServletRequest srvReq, HttpServletResponse srvResp)
	                 throws ServletException, IOException
	{		
		PrintWriter out = srvResp.getWriter();
		
		StringBuffer sbuf = new StringBuffer();
		
		//System.out.println("Entered doGet of SearchServlet..");
		log.debug("Entered doGet of SearchServlet");
		
		String reqParm = srvReq.getParameter("searchString");
		
		log.debug("searchString Parameter = " + reqParm);
		
		if (reqParm == null)
		{	
			log.debug("parameter is null...Nothing to search for!!");
			sbuf.append("nothing found");
		}
		else
		{			
			try
			{
				TransactionDAO daoReference = (TransactionDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTION_DAO);
				log.debug("retrieving descriptions for search string = " + reqParm);
				
				//Set up search object to send into DAO inquire
				ICacheManager cache =  CacheManagerFactory.getCacheManager();
				TransactionSearch trxSearch = new TransactionSearch();
				trxSearch.setTrxSearchInvestorID(new Integer(cache.getInvestor(srvReq.getSession()).getInvestorID()));
				trxSearch.setTrxDescriptionText(reqParm);
				
				List<String> trxDescList = daoReference.searchDistinct(trxSearch);
				
				//loop through the list here, and create a String separated by /n
				//this is what the Ajax stuff is expecting.
				
				for (int i = 0; i < trxDescList.size(); i++)
				{					
					sbuf.append(trxDescList.get(i));
					sbuf.append("\n");
				}
			}
			catch (PASSystemException e)
			{
				log.error("PASSystemException caught in SearchServlet - message is " + e.getMessage());
				e.printStackTrace();
				throw new ServletException(e);			
			}
			catch (DAOException e) 
			{
				log.error("DAOException caught in SearchServlet - message is " + e.getMessage());
				e.printStackTrace();
				throw new ServletException(e);			
			}
			catch (SystemException e)
			{
				log.error("SystemException caught in SearchServlet - message is " + e.getMessage());
				e.printStackTrace();
				throw new ServletException(e);			
			}
		}		
		String outputString = sbuf.toString();
		
		out.println(outputString);
		log.debug("outputString = " + outputString);
		log.debug("leaving doGet of SearchServlet");
	}

}
