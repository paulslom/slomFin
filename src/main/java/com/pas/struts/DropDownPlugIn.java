package com.pas.struts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.pas.business.BusinessComposite;
import com.pas.slomfin.business.ISlomFinBusiness;
import com.pas.slomfin.business.SlomFinBusinessFactory;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Menu;
import com.pas.valueObject.DropDownBean;
import com.pas.valueObject.IValueObject;

/**
 * Title: DropDownPlugIn
 * Project: SlomFin
 * 
 * Description:  This is a Struts plug-in that is responsible for loading 
 * the static dropdowns into the ServletContext. It is executed once at 
 * application start-up
 * 
 * @version 
 */
public class DropDownPlugIn implements PlugIn
{
    // handle to logger class to log messages
    protected Logger log = LogManager.getLogger(this.getClass());

    public DropDownPlugIn() 
    {
        super();        
    }

    /**
     * This method is invoked by controller while starting the app server
     *
     * @param config holds configuration details for a module
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	public void init(ActionServlet servlet, ModuleConfig config) throws ServletException
    {
        String methodName = " DropDownPlugIn init :: ";
        log.debug(methodName + "in");

        try
        {
        	ISlomFinBusiness ddBusiness = SlomFinBusinessFactory.getBusinessInstance(ISlomFinAppConstants.DROPDOWN_BUSINESS);
        	
        	IValueObject valObj = null;
        
        	BusinessComposite bc = ddBusiness.inquire(valObj);

        	if (bc.hasValidationMessages())
        	{
        		log.error("Error : There are validation errors in DropDown method");               
        		throw new ServletException("Error : There are validation errors in DropDownPlugIn init method");
        	}
        	
        	List<Map<String, List<DropDownBean>>> l = bc.getValueObjectList();
        	ArrayList m = new ArrayList();
        	if (l != null && l.size() > 0)
        	{
        		m = (ArrayList) l.get(0);
        	}
        	
        	Map dropDownsMap = null;
        	if (m.size() > 0)
        	{
        		dropDownsMap = (HashMap) m.get(0);
        	}
			
        	ServletContext servletContext = servlet.getServletContext();
        		
        	//iterate over all elements of map & add the individual dropdowns
            
        	if (dropDownsMap == null)
        	{
        		log.error("Error : There are validation errors in DropDown method..nothing loaded into dropdownsmap!!");               
        		throw new ServletException("Error : There are validation errors in DropDownPlugIn init method");
        	}
        	
        	Iterator dropDownLookupNames = dropDownsMap.keySet().iterator();
						
        	while (dropDownLookupNames.hasNext())
        	{
        		String dropDownLookupName = (String) dropDownLookupNames.next();
        		Collection dropDownLookupValues = (Collection) dropDownsMap.get(dropDownLookupName);
        		servletContext.setAttribute(dropDownLookupName.trim(), dropDownLookupValues);
        		log.debug("loaded dropdown " + dropDownLookupName.trim());
        		
        		//special case - lots of uses for investment id of "CASH" so place this in cache
        		if (dropDownLookupName.trim().equalsIgnoreCase(ISlomFinAppConstants.DROPDOWN_INVESTMENTS))
        		{	
        			Iterator<DropDownBean> iterDD = dropDownLookupValues.iterator(); 
        			while (iterDD.hasNext())
        			{
        				DropDownBean ddBean = new DropDownBean();
        				ddBean = iterDD.next();
    	                if (ddBean.getDescription().equalsIgnoreCase(ISlomFinAppConstants.INVESTMENT_CASH))
    	                {	
    	                  servletContext.setAttribute(ISlomFinAppConstants.CASHINVESTMENTID, ddBean.getId());
    	                  break;
    	                }
        			}	
        		}
        	}
        	
        	//dropdown for the last 5 years
    		Calendar now = Calendar.getInstance();
    		int nowYear = now.get(Calendar.YEAR);
    		List<DropDownBean> last5YearsList = new ArrayList<DropDownBean>(); 
    		
    		for (int i=nowYear; i>nowYear-5; i--)
    		{			  
    			DropDownBean ddBean = new DropDownBean();    	
    			ddBean.setId(String.valueOf(i));
    			ddBean.setDescription(String.valueOf(i));
    			last5YearsList.add(ddBean);
    		} 
    		
    		servletContext.setAttribute(ISlomFinAppConstants.DROPDOWN_LAST5YEARS, last5YearsList);
    		log.debug("loaded dropdown " + ISlomFinAppConstants.DROPDOWN_LAST5YEARS);
        }
        catch (Exception e)
        {
        	log.error("DropDownPlugin", e);        	
        	throw new ServletException("Error : There are validation errors in DropDownPlugIn");
        }

        log.debug("All static drop downs are loaded into context");
                
        log.debug(methodName + "out");
    }

    /**
     * This gets invoked by controller servlet before shut down of the app server
     */
    public void destroy() {

    }

}