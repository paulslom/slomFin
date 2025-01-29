package com.pas.slomfin.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.PortfolioHistory;
import com.pas.beans.Account;
import com.pas.dynamodb.DynamoClients;
import com.pas.util.TeamComparator;

import jakarta.faces.model.SelectItem;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

public class AccountDAO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(AccountDAO.class);
		
	private Map<Integer,Account> fullNflTeamsMap = new HashMap<>(); 
	private List<Account> fullNflTeamList = new ArrayList<>();
	
	private List<SelectItem> teamsDropdownListCurrentSeason = new ArrayList<>();
	private Map<Integer,List<Account>> teamsMapBySeason = new HashMap<>(); 	
	private List<Account> teamsListCurrentSeason = new ArrayList<>();	

	private static DynamoClients dynamoClients;
	private static DynamoDbTable<Account> nflTeamsTable;
	private static final String AWS_TABLE_NAME = "nflteams";
		
	public AccountDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       nflTeamsTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(Account.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing NflTeamsDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public Integer addNflTeam(Account nflteam) throws Exception
	{
		Account nflTeam2 = dynamoUpsert(nflteam);		
		 
		nflteam.setiTeamID(nflTeam2.getiTeamID());
		
		logger.info("LoggedDBOperation: function-add; table:nflteam; rows:1");
		
		refreshListsAndMaps("add", nflteam);	
				
		logger.info("addNflTeam complete");		
		
		return nflTeam2.getiTeamID(); //this is the key that was just added
	}
	
	private Account dynamoUpsert(Account nflteam) throws Exception 
	{
		Account dynamoNflTeam = new Account();
        
		if (nflteam.getiTeamID() == null)
		{
			Integer currentMaxTeamID = 0;
			for (int i = 0; i < this.getFullNflTeamList().size(); i++) 
			{
				Account nflTeam = this.getFullNflTeamList().get(i);
				currentMaxTeamID = nflTeam.getiTeamID();
			}
			dynamoNflTeam.setiTeamID(currentMaxTeamID + 1);
		}
		else
		{
			dynamoNflTeam.setiTeamID(nflteam.getiTeamID());
		}
				
		PutItemEnhancedRequest<Account> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Account.class).item(dynamoNflTeam).build();
		nflTeamsTable.putItem(putItemEnhancedRequest);
			
		return dynamoNflTeam;
	}

	public void updateNflTeam(Account nflteam)  throws Exception
	{
		dynamoUpsert(nflteam);		
			
		logger.info("LoggedDBOperation: function-update; table:nflteam; rows:1");
		
		refreshListsAndMaps("update", nflteam);	
		
		logger.debug("update nflteam table complete");		
	}
	
	public void readNflTeamsFromDB(List<PortfolioHistory> nflSeasonsList) 
    {
		Iterator<Account> results = nflTeamsTable.scan().items().iterator();
		
		while (results.hasNext()) 
        {
			Account nflTeam = results.next();
			nflTeam.setFullTeamName();
            this.getFullNflTeamList().add(nflTeam);            
        }
		
		logger.info("LoggedDBOperation: function-inquiry; table:nflteam; rows:" + this.getFullNflTeamList().size());
		
		this.setFullNflTeamsMap(this.getFullNflTeamList().stream().collect(Collectors.toMap(Account::getiTeamID, ply -> ply)));
		
		Collections.sort(this.getFullNflTeamList(), new Comparator<Account>() 
		{
		   public int compare(Account o1, Account o2) 
		   {
		      return o1.getiTeamID().compareTo(o2.getiTeamID());
		   }
		});		

		int maxSeasonId = setAllSeasonsTeams(nflSeasonsList);
		
		//establish this season's teams.  Default to max season id
		setThisSeasonsTeams(maxSeasonId);
	}
	
	private int setAllSeasonsTeams(List<PortfolioHistory> nflSeasonsList) 
	{
		int maxSeasonID = 0;
		
		for (int i = 0; i < nflSeasonsList.size(); i++) 
		{			
			PortfolioHistory nflseason = nflSeasonsList.get(i);
			Integer iSeasonID = nflseason.getiSeasonID();
			
			if (iSeasonID > maxSeasonID)
			{
				maxSeasonID = iSeasonID;
			}
			
			List<Account> nflTeamList = new ArrayList<>();
			
			for (int j = 0; j < this.getFullNflTeamList().size(); j++) 
			{			
				Account nflteam = this.getFullNflTeamList().get(j);
				boolean addThisTeam = false;
				
				if (iSeasonID >= nflteam.getiFirstSeasonIDAsTeam()
				&&  nflteam.getiLastSeasonIDAsTeam() == 0)
				{
					addThisTeam = true;
				}
				else if (iSeasonID >= nflteam.getiFirstSeasonIDAsTeam()
				&&  iSeasonID <= nflteam.getiLastSeasonIDAsTeam())
				{
					addThisTeam = true;								
				}
				
				if (addThisTeam)
				{
					nflTeamList.add(nflteam);		
				}
			}
			
			this.getTeamsMapBySeason().put(iSeasonID, nflTeamList);			
		}		
		
		return maxSeasonID;
	}
	
	public void setThisSeasonsTeams(Integer seasonID)
	{
		this.getTeamsListCurrentSeason().clear();
		this.getTeamsDropdownListCurrentSeason().clear();
		
		this.setTeamsListCurrentSeason(this.getTeamsMapBySeason().get(seasonID));
		
		for (int j = 0; j < this.getTeamsListCurrentSeason().size(); j++) 
		{			
			Account nflteam = this.getTeamsListCurrentSeason().get(j);
			SelectItem si = new SelectItem();
			si.setLabel(nflteam.getFullTeamName());
			si.setValue(nflteam.getiTeamID());
			this.getTeamsDropdownListCurrentSeason().add(si);
        }
		
		Collections.sort(this.getTeamsDropdownListCurrentSeason(), new TeamComparator());
		Collections.sort(this.getTeamsListCurrentSeason(), new TeamComparatorByDivision());
	}
	
	
	private void refreshListsAndMaps(String function, Account nflteam)
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullNflTeamsMap().remove(nflteam.getiTeamID());		
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullNflTeamsMap().put(nflteam.getiTeamID(), nflteam);		
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullNflTeamsMap().remove(nflteam.getiTeamID());		
			this.getFullNflTeamsMap().put(nflteam.getiTeamID(), nflteam);	
		}
		
		this.getFullNflTeamList().clear();
		Collection<Account> values = this.getFullNflTeamsMap().values();
		this.setFullNflTeamList(new ArrayList<>(values));
		
		Collections.sort(this.getFullNflTeamList(), new Comparator<Account>() 
		{
		   public int compare(Account o1, Account o2) 
		   {
		      return o1.getiTeamID().compareTo(o2.getiTeamID());
		   }
		});
				
	}
	
	public List<Account> getFullNflTeamList() 
	{
		return fullNflTeamList;
	}

	public void setFullNflTeamList(List<Account> fullNflTeamList) 
	{
		this.fullNflTeamList = fullNflTeamList;
	}

	public Map<Integer, Account> getFullNflTeamsMap() {
		return fullNflTeamsMap;
	}

	public void setFullNflTeamsMap(Map<Integer, Account> fullNflTeamsMap) {
		this.fullNflTeamsMap = fullNflTeamsMap;
	}

	public Account getTeamByTeamID(int teamId) 
	{
		return this.getFullNflTeamsMap().get(teamId);
	}

	public List<Account> getTeamsListCurrentSeason() {
		return teamsListCurrentSeason;
	}

	public void setTeamsListCurrentSeason(List<Account> teamsListCurrentSeason) {
		this.teamsListCurrentSeason = teamsListCurrentSeason;
	}

	public List<SelectItem> getTeamsDropdownListCurrentSeason() {
		return teamsDropdownListCurrentSeason;
	}

	public void setTeamsDropdownListCurrentSeason(List<SelectItem> teamsDropdownListCurrentSeason) {
		this.teamsDropdownListCurrentSeason = teamsDropdownListCurrentSeason;
	}

	public Map<Integer, List<Account>> getTeamsMapBySeason() {
		return teamsMapBySeason;
	}

	public void setTeamsMapBySeason(Map<Integer, List<Account>> teamsMapBySeason) {
		this.teamsMapBySeason = teamsMapBySeason;
	}

	public List<Account> getAfcTeamsList() 
	{
		//teams list current season is already sorted by division
		List<Account> afcTeamsList = new ArrayList<>();
		for (int i = 0; i < this.getTeamsListCurrentSeason().size(); i++) 
		{
			Account nflteam = this.getTeamsListCurrentSeason().get(i);
			if (nflteam.getvConferenceName().equalsIgnoreCase("AFC"))
			{
				afcTeamsList.add(nflteam);
			}
		} 
		return afcTeamsList;
	}

	public List<Account> getNfcTeamsList() 
	{
		//teams list current season is already sorted by division
		List<Account> nfcTeamsList = new ArrayList<>();
		for (int i = 0; i < this.getTeamsListCurrentSeason().size(); i++) 
		{
			Account nflteam = this.getTeamsListCurrentSeason().get(i);
			if (nflteam.getvConferenceName().equalsIgnoreCase("NFC"))
			{
				nfcTeamsList.add(nflteam);
			}
		} 
		return nfcTeamsList;
	}

}
