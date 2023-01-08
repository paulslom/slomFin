package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tbljob;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.Menu;

public class Menu2DAO extends BaseDBDAO
{
  private Integer investorID;
  
  private static final String CONTEXT_ROOT = "/slomfin";

  private static final Menu2DAO currentInstance = new Menu2DAO();

    private Menu2DAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return LoginDAO
     */
    public static Menu2DAO getDAOInstance()
    {
        return currentInstance;
    }
        
	@SuppressWarnings("unchecked")
	public List inquire(Object investor) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		log.debug("entering Menu2DAO inquire");
		
		JobDAO jobDAOReference;
		
		try 
		{
			jobDAOReference = (JobDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.JOB_DAO);
		} 
		catch (SystemException e) 
		{
			log.error("error retrieving portfolios in Menu2DAO", e);
			throw new DAOException(e);
		}	
		
		log.debug(methodName + "before inquiring for Menu. Investor ID parameter is = " + (((Investor)investor).getInvestorID()));
		investorID = Integer.valueOf (((Investor)investor).getInvestorID());
		
 		List<Menu> reportsList = menuStrutsCreateReports();
 		List<Menu> workList = menuStrutsCreateWork(jobDAOReference);
  		List<Menu> miscList = menuStrutsCreateMisc((Investor)investor);
 		
  		List<Map> menuList = new ArrayList<Map>();
  		
  		Map<String, List<Menu>> menuMap = new HashMap<>();
  		menuMap.put("Reports", reportsList);
  		menuMap.put("Work", workList);
  		menuMap.put("Misc", miscList);
  		
  		menuList.add(menuMap);
		
		log.debug(methodName + "out");
		
		return menuList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateReports() throws DAOException
	{
		List<Menu> mList = new ArrayList<Menu>();
				
		Menu menuDetail = new Menu();
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Trx By Investment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentChoiceShowFormAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Capital Gains");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportCapitalGainsSelectionAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Dividends");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportDividendsSelectionAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Withdrawal Categories");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportWDCategoriesSelectionAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Portfolio Summary");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioSummaryAction.do?&operation=inquire&investorID=" + investorID);		
		mList.add(menuDetail);	
				
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Cost Basis");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportCostBasisAction.do?operation=inquire");		
		mList.add(menuDetail);	
				
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Goals");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportGoalsShowSelectionFormAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Portfolio History");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioHistoryAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Portfolio By Asset Class");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportPortfolioByAssetClassAction.do?operation=inquire");		
		mList.add(menuDetail);	
				
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Units Owned");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/ReportUnitsOwnedAction.do?operation=inquire");		
		mList.add(menuDetail);	
		
		return mList;
	}	
	
	@SuppressWarnings("unchecked")
	private List<Menu> menuStrutsCreateWork(JobDAO jobDAOReference) throws DAOException
	{
		List<Menu> mList = new ArrayList<Menu>();
				
		Menu menuDetail = new Menu();
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Paycheck Outflow View-Chg-Del");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckOutflowListAction.do?operation=inquire&paycheckOutflowShowParm=inquire");
		mList.add(menuDetail);	
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Paycheck Outflow Add");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckOutflowShowUpdateFormAction.do?operation=inquire&paycheckOutflowShowParm=add");
		mList.add(menuDetail);	
				
		log.debug("before inquiring for Jobs in menu build process...");
		
		Tblinvestor tblinvestor = new Tblinvestor();
		tblinvestor.setIinvestorId(investorID);
		List<Tbljob> rsJobs = jobDAOReference.inquire(tblinvestor);
		
		log.debug("looping through recordset to build Job menu items");
				
		for (int i = 0; i < rsJobs.size(); i++)
		{
			Tbljob job = rsJobs.get(i);
			
			if (job.getDjobEndDate() == null) //only do current jobs
			{	
				menuDetail = new Menu();
				menuDetail.setMenuTitle("Add Paycheck " + job.getSemployer().substring(0,6));
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckAddShowFormAction1.do?operation=inquire&jobID=" + job.getIjobId().toString());
				mList.add(menuDetail);
				
				menuDetail = new Menu();
				menuDetail.setMenuTitle("Pchk Hist View-Chg-Del " + job.getSemployer().substring(0,3));
				menuDetail.setMenuLocation(CONTEXT_ROOT + "/PaycheckHistoryListAction.do?&operation=inquire&paycheckHistoryShowParm=inquire&ijobID=" +  job.getIjobId().toString());
				mList.add(menuDetail);
			}
		         
		}
		
		return mList;
	}
		
	private List<Menu> menuStrutsCreateMisc(Investor investor)
	{
		List<Menu> mList = new ArrayList<Menu>();
				
		Menu menuDetail = new Menu();
		menuDetail.setMenuTitle("Owned by " + investor.getTaxGroupName());
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentsOwnedShowFormAction.do?operation=inquire&investmentShowParm=inquire&investorID=" + investorID);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("View-Chg-Del Investments");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentListAction.do?operation=inquire");
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Add New Investment");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/InvestmentShowUpdateFormAction.do?operation=inquire&investmentShowParm=add");
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("View-Chg-Del WD Cat");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/WDCategoryListAction.do?operation=inquire&investorID=" + investorID);
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Add New WD Cat");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/WDCategoryShowUpdateFormAction.do?operation=inquire&wdCategoryShowParm=add");
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("View-Chg-Del Asset Cls");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/AssetClassListAction.do?operation=inquire");
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Add New Asset Cls");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/AssetClassShowUpdateFormAction.do?operation=inquire&assetClassShowParm=add");
		mList.add(menuDetail);
		
		menuDetail = new Menu();
		menuDetail.setMenuTitle("Search Trx Descriptions");
		menuDetail.setMenuLocation(CONTEXT_ROOT + "/TrxShowSearchFormAction.do?operation=inquire");
		mList.add(menuDetail);
			
		return mList;
	}	
	

}
