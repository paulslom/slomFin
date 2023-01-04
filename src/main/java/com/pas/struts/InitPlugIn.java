/*
 * Created on Apr 16, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pas.struts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.exception.PASSystemException;
import com.pas.util.IPropertyReader;
import com.pas.util.PropertyReaderFactory;

import com.pas.slomfin.business.SlomFinBusinessFactory;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.action.SlomFinStandardAction;

/**
 * @author DPandey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt; Code and Comments
 */
public class InitPlugIn implements PlugIn {
		
	private String propertyFiles;
		
	private IPropertyReader pr;
	private Logger log = LogManager.getLogger(this.getClass()); 

	
	// This method will be called at the time of application shutdown 
	public void destroy() {
		try{
			pr.clearProperties();
		}catch(PASSystemException e){
			e.logException(log);
		}
		finally{
			pr = null;	
			propertyFiles = null;			
			log = null;
		}
	}
	
	//This method will be called at the time of application startup
	public void init(ActionServlet actionServlet, ModuleConfig config) throws ServletException {
		initializeProperties(actionServlet);
		//register BigDecimalConverter for struts BeanUtils
//		ConvertUtils.register(new BigDecimalConverter(), BigDecimal.class);
		
	}
	private void initializeProperties(ActionServlet actionServlet)
	{
		String method = "initializeProperties::";
		log.debug(method + "in");
		
		try
		{
			ArrayList<String> propertyFileList = getFiles();
			pr = PropertyReaderFactory.getPropertyReader();
			pr.clearProperties();
			
			Iterator<String> itr = propertyFileList.iterator();
			String fileName;
			while (itr.hasNext())
			{
				fileName = itr.next();
				log.debug("file to add: " + fileName);
				pr.addFile(fileName);
			}
			pr.init();		
			initializeFactory();
			log.debug(method + "out");
			
		}
		catch(PASSystemException e)
		{			
			e.logException(log);
			//notifyViaEmail(e.getMessage());
		}
	}
	public String getPropertyFiles() {
		return propertyFiles;
	}
	public void setPropertyFiles(String files) {
		propertyFiles = files;
	}
	
	private ArrayList<String> getFiles()
	{
		ArrayList<String> propertyFileList = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(getPropertyFiles(), "|");
		String nextToken;
		while( st.hasMoreTokens()){
			nextToken = st.nextToken();
			propertyFileList.add(nextToken);
		}
		return propertyFileList;
	} 
	
	private void initializeFactory() throws PASSystemException
	{
		log.debug("Initializing factory classes ....");

		SlomFinDAOFactory.initialize();
		SlomFinBusinessFactory.initialize();
		SlomFinStandardAction.initializeActionSettings();
		
		log.debug("Completed Initializing factory classes");
	}	

}
