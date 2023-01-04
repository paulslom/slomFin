package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbldropdown;
import com.pas.dbobjects.TbldropdownRowMapper;
import com.pas.exception.DAOException;
import com.pas.util.Utils;
import com.pas.valueObject.DropDownBean;

public class DropdownDAO extends BaseDBDAO 
{
    private static final DropdownDAO currentInstance = new DropdownDAO();

    private DropdownDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return IVRMenuOptionCodeDAO
     */
    public static DropdownDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	@SuppressWarnings("unchecked")
	public List<Map<String, List<DropDownBean>>> inquire(Object ddInfo) throws DAOException 
	{
		String methodName = "inquire :: ";
        log.debug(methodName + "In");
          
        List<Map<String, List<DropDownBean>>> listOfDropdowns = new ArrayList<Map<String, List<DropDownBean>>>();
       	
 		StringBuffer queryString = new StringBuffer();
 		queryString.append("select * from Tbldropdown ");
 		queryString.append("ORDER BY sDDDescription");
 		
 		List<Tbldropdown> dropdownAll = this.getJdbcTemplate().query(queryString.toString(), new TbldropdownRowMapper());		
     
        Map<String, List<DropDownBean>> mapDropdownAll = new HashMap<String, List<DropDownBean>>(); // holds all drop downs 
            
        for (int i = 0; i < dropdownAll.size(); i++)
		{
        	Tbldropdown ddEntry = (Tbldropdown)dropdownAll.get(i);
        	
		    StringBuffer sbufOne = new StringBuffer();
            
		    String tblName = ddEntry.getSddtableName();
			sbufOne.append(" SELECT ");
	        sbufOne.append(ddEntry.getSddvalueColName() + " as id, ");
	        sbufOne.append(ddEntry.getSdddescColName() + " as description");
	        sbufOne.append(" FROM " + tblName);
			            
            if (ddEntry.getSddwhereText() != null)
            {
            	sbufOne.append(" WHERE " + ddEntry.getSddwhereText());
            }
            
            sbufOne.append(" ORDER BY " + ddEntry.getSddsort()); 
            
            String queryOne = sbufOne.toString();
            
            log.debug("executing query: " + queryOne);
            
            List<DropDownBean> dropdownOne = this.getJdbcTemplate().query(sbufOne.toString(), new BeanPropertyRowMapper<DropDownBean>(DropDownBean.class));			
             
            List<DropDownBean> ddList = new ArrayList<DropDownBean>(); // holds all elements of a single drop down
            
            for (int j = 0; j < dropdownOne.size(); j++)
			{
            	DropDownBean ddOne = dropdownOne.get(j);
            	ddList.add(ddOne);  
                mapDropdownAll.put(ddEntry.getSdddescription().toUpperCase(), ddList);                                	
			}   
            
            listOfDropdowns.add(mapDropdownAll);        
		}
              
        log.debug(methodName + "out");
        return listOfDropdowns;
	}	

}
