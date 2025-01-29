package com.pas.slomfin.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.dynamodb.DateToStringConverter;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoTransaction;
import com.pas.util.Utils;

import jakarta.faces.model.SelectItem;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TransactionDAO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(TransactionDAO.class);
	
	private Map<Integer,DynamoTransaction> fullNflGamesMap = new HashMap<>(); 
	private Map<Integer,List<DynamoTransaction>> gamesMapBySeason = new HashMap<>(); 
	private Map<Integer,String> gameTypesMap = new HashMap<>();
	private Map<Integer,Integer> weeksMapByWeekNumber = new HashMap<>();
	private Map<String,Map<Integer,String>> teamRegularSeasonGamesMap = new HashMap<>(); 
	
	private List<DynamoTransaction> fullNflGameList = new ArrayList<>();
	private List<DynamoTransaction> seasonGamesList = new ArrayList<>();	
	private List<DynamoTransaction> playoffGamesList = new ArrayList<>();
	private List<SelectItem> gameTypesList = new ArrayList<>();
	
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<DynamoTransaction> transactionsTable;
	private static final String AWS_TABLE_NAME = "slomFinTransactions";
	
	private int maxRegularSeasonWeekID = 0;
	private int maxRegularSeasonWeekNumber = 0;
	
	private Schedule scheduleTitleRow = new Schedule();
	
	//private static String HTML_CRLF = "<br>";
	//private static String PDF_CRLF = "\r\n\r\n";
		
	public TransactionDAO(DynamoClients dynamoClients2) 
	{		
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       nflGamesTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(DynamoTransaction.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing NflGamesDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public Integer addNflGame(DynamoTransaction dynamoNflGame) throws Exception
	{
		DynamoTransaction nflGame2 = dynamoUpsert(dynamoNflGame);		
		 
		dynamoNflGame.setIgameId(nflGame2.getIgameId());
		
		logger.info("LoggedDBOperation: function-add; table:nflgame; rows:1");
	
		refreshListsAndMaps("add", dynamoNflGame);	
					
		logger.info("addNflGame complete. added gameID: " + nflGame2.getIgameId());		
		
		return nflGame2.getIgameId(); //this is the key that was just added
	}
	
	public DynamoTransaction getGameByGameID(Integer gameId)
	{
		return this.getFullNflGamesMap().get(gameId);
	}
	
	private DynamoTransaction dynamoUpsert(DynamoTransaction dynamoNflGame) throws Exception 
	{
		if (dynamoNflGame.getIgameId() == null)
		{
			Integer nextGameID = determineNextGameId();
			dynamoNflGame.setIgameId(nextGameID);
		}
						
		PutItemEnhancedRequest<DynamoTransaction> putItemEnhancedRequest = PutItemEnhancedRequest.builder(DynamoTransaction.class).item(dynamoNflGame).build();
		nflGamesTable.putItem(putItemEnhancedRequest);
			
		return dynamoNflGame;
	}

	private Integer determineNextGameId() 
	{
		Integer nextGameID = 0;
		
		List<Integer> gameIds = this.getFullNflGamesMap().keySet().stream().collect(Collectors.toList());
		int max = Collections.max(gameIds);
        nextGameID = max + 1;
        
		return nextGameID;
	}

	public void updateNflGame(DynamoTransaction dynamoNflgame)  throws Exception
	{
		dynamoUpsert(dynamoNflgame);		
			
		logger.info("LoggedDBOperation: function-update; table:nflgame; rows:1");
		
		refreshListsAndMaps("update", dynamoNflgame);	
		
		logger.debug("update nflgame table complete");		
	}
	
	public void deleteNflGame(DynamoTransaction nflgame) throws Exception 
	{
		Key key = Key.builder().partitionValue(nflgame.getIgameId()).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		nflGamesTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:nflgame; rows:1");
		
		refreshListsAndMaps("delete", nflgame);		
		
		logger.info(" deleteGame complete");	
	}
	
	public void readTransactionsWithin2YearsFromDB() throws Exception 
    {
		logger.info("entering readTransactionsWithin2YearsFromDB");
		
		Map<String, Course> coursesMap = new HashMap<>();
		if (golfmain == null || golfmain.getCourseDAO() == null) //if golfmain jsf bean unavailable... so just redo the gamedao read
		{
			DynamoClients dynamoClients = DynamoUtil.getDynamoClients();				
			CourseDAO courseDAO = new CourseDAO(dynamoClients);		
			courseDAO.readCoursesFromDB(defaultGroup);
			coursesMap = courseDAO.getCourseSelections().stream().collect(Collectors.toMap(Course::getCourseID, course -> course));
		}
		else
		{
			coursesMap = golfmain.getCoursesMap();
		}
		
		String twoYearsAgo = Utils.getTwoYearsAgoDate();
		
		logger.info("looking for transactions newer than: " + twoYearsAgo);
		
		Map<String, AttributeValue> av = Map.of(":min_value", AttributeValue.fromS(twoYearsAgo));
		
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .filterExpression(Expression.builder()
                        .expression("transactionPostedDate >= :min_value")
                        .expressionValues(av)
                        .build())
                .build();
		
		Iterator<DynamoGame> results = gamesTable.scan(request).items().iterator();
	  	
		int gameCount = 0;
		while (results.hasNext()) 
        {
			gameCount++;
			logger.info("iterating game " + gameCount);
			DynamoGame dynamoGame = results.next();
          	
			Game game = new Game(golfmain);

			game.setGameID(dynamoGame.getGameID());
			game.setOldGameID(dynamoGame.getOldGameID());		
			
			String gameDate = dynamoGame.getGameDate();
			DateToStringConverter dsc = new DateToStringConverter();
			Date dGameDate = dsc.unconvert(gameDate);
			game.setGameDate(dGameDate);
			
			game.setCourseID(dynamoGame.getCourseID());				
			
			if (coursesMap != null && coursesMap.containsKey(game.getCourseID()))
			{
				Course course = coursesMap.get(game.getCourseID());
				game.setCourse(course);
				game.setCourseName(course.getCourseName());
			}
			
			game.setFieldSize(dynamoGame.getFieldSize());
			game.setTotalPlayers(dynamoGame.getTotalPlayers());
			game.setTotalTeams(dynamoGame.getTotalTeams());
			game.setSkinsPot(dynamoGame.getSkinsPot());
			game.setTeamPot(dynamoGame.getTeamPot());
			game.setGameFee(dynamoGame.getGameFee());
			game.setBetAmount(dynamoGame.getBetAmount());
			game.setHowManyBalls(dynamoGame.getHowManyBalls());
			game.setPurseAmount(dynamoGame.getPurseAmount());
			game.setEachBallWorth(dynamoGame.getEachBallWorth());
			game.setIndividualGrossPrize(dynamoGame.getIndividualGrossPrize());
			game.setIndividualNetPrize(dynamoGame.getIndividualNetPrize());
			game.setPlayTheBallMethod(dynamoGame.getPlayTheBallMethod());
			game.setGameClosedForSignups(dynamoGame.isGameClosedForSignups());
			game.setGameNoteForEmail(dynamoGame.getGameNoteForEmail());	
			
            this.getFullGameList().add(game);			
        }
		
		Collections.sort(this.getFullGameList(), new Comparator<Game>() 
		{
		   public int compare(Game o1, Game o2) 
		   {
		      return o1.getGameDate().compareTo(o2.getGameDate());
		   }
		});
		
		logger.info("LoggedDBOperation: function-inquiry; table:game; rows:" + this.getFullGameList().size());
	}
	
	public void readNflGamesFromDB(int seasonID) 
    {
		Iterator<DynamoTransaction> results = nflGamesTable.scan().items().iterator();
		
		while (results.hasNext()) 
        {
			DynamoTransaction dynamoNflGame = results.next();			
            this.getFullNflGameList().add(dynamoNflGame);            
        }
		
		logger.info("LoggedDBOperation: function-inquiry; table:nflgame; rows:" + this.getFullNflGameList().size());
		
		this.setFullNflGamesMap(this.getFullNflGameList().stream().collect(Collectors.toMap(DynamoTransaction::getIgameId, ply -> ply)));
		
		Collections.sort(this.getFullNflGameList(), new GameComparator());
		
		for (int i = 0; i < this.getFullNflGameList().size(); i++) 
		{			
			DynamoTransaction nflgame = this.getFullNflGameList().get(i);
			
			logger.debug("looping game number: " + i + " for seasonid: " + nflgame.getiSeasonId());
			
			if (!this.getGameTypesMap().containsKey(nflgame.getIgameTypeId()))
			{
				this.getGameTypesMap().put(nflgame.getIgameTypeId(), nflgame.getSgameTypeDesc());
			}
			
			if (!this.getWeeksMapByWeekNumber().containsKey(nflgame.getIweekNumber()))
			{
				this.getWeeksMapByWeekNumber().put(nflgame.getIweekNumber(), nflgame.getIweekId());
			}
			
			if (this.getGamesMapBySeason().containsKey(nflgame.getiSeasonId()))
			{
				List<DynamoTransaction> tempGameList = this.getGamesMapBySeason().get(nflgame.getiSeasonId());
				tempGameList.add(nflgame);
				this.getGamesMapBySeason().replace(nflgame.getiSeasonId(), tempGameList);
			}
			else
			{
				List<DynamoTransaction> tempGameList = new ArrayList<>();
				tempGameList.add(nflgame);
				this.getGamesMapBySeason().put(nflgame.getiSeasonId(), tempGameList);				
			}
		}
		
		//establish this season's games.  Use default season id, supplied from NflMain
		this.setSeasonGamesList(this.getGamesMapBySeason().get(seasonID));
		
		//establish this season's games by team
		this.setTeamGamesMap(seasonID);
		
		//establish schedule title row
		this.establishScheduleTitleRow(seasonID);
		
		//establish this season's game types.
		for (Integer key : this.getGameTypesMap().keySet()) 
		{
			SelectItem si = new SelectItem();
			si.setLabel(this.getGameTypesMap().get(key));
			si.setValue(key);
			this.getGameTypesList().add(si);
        }
		
		setMaxRegularSeasonWeek();
	}


	public void establishScheduleTitleRow(Integer getiSeasonID) 
	{	
		Map<Integer,String> tempWeeksMap = new HashMap<>();
		
		for (int i = 0; i < this.getSeasonGamesList().size(); i++) 
		{
			DynamoTransaction game = this.getSeasonGamesList().get(i);
			
			if (!game.getSgameTypeDesc().equalsIgnoreCase("Regular Season"))
			{
				continue;
			}
			else if (tempWeeksMap.containsKey(game.getIweekNumber()))
			{
				continue;
			}
			else if (game.getGameDayOfWeek().equalsIgnoreCase("Sun"))
			{
				String dtstr = DateToStringConverter.convertToScheduleFormat(game.getDgameDateTime());
				String opponentName = "";
				if (game.getIweekNumber() > 9)
				{
					//opponentName = "Wk " + game.getIweekNumber() + HTML_CRLF + dtstr;
					opponentName = "Wk " + game.getIweekNumber() + " " + dtstr;
				}
				else
				{
					opponentName = "Wk " + game.getIweekNumber() + " " + dtstr;
				}
				tempWeeksMap.put(game.getIweekNumber(),opponentName);				
			}
		}
		
		Schedule tempSchedule = new Schedule();
		tempSchedule.setTeam("");
		
		List<String> opponentsList = tempWeeksMap.values().stream().collect(Collectors.toList());
		
		tempSchedule.setOpponentsList(opponentsList);
		this.setScheduleTitleRow(tempSchedule);
		
	}
	
	public void setTeamGamesMap(Integer getiSeasonID) 
	{
		this.getTeamRegularSeasonGamesMap().clear();
		
		for (int i = 0; i < this.getSeasonGamesList().size(); i++) 
		{
			DynamoTransaction game = this.getSeasonGamesList().get(i);
			
			if (!game.getSgameTypeDesc().equalsIgnoreCase("Regular Season"))
			{
				continue;
			}
			
			if (this.getTeamRegularSeasonGamesMap().containsKey(game.getCawayteamCityAbbr()))
			{
				Map<Integer,String> tempMap = new HashMap<>(this.getTeamRegularSeasonGamesMap().get(game.getCawayteamCityAbbr()));
				tempMap.put(game.getIweekNumber(), "@" + game.getChometeamCityAbbr().toLowerCase() + game.getGameTimeOnly().substring(1, 2));
				this.getTeamRegularSeasonGamesMap().replace(game.getCawayteamCityAbbr(),tempMap);
			}
			else
			{
				Map<Integer,String> tempMap = new HashMap<>();
				tempMap.put(game.getIweekNumber(), "@" + game.getChometeamCityAbbr().toLowerCase() + game.getGameTimeOnly().substring(1, 2));
				this.getTeamRegularSeasonGamesMap().put(game.getCawayteamCityAbbr(),tempMap);
			}
			
			if (this.getTeamRegularSeasonGamesMap().containsKey(game.getChometeamCityAbbr()))
			{
				Map<Integer,String> tempMap = new HashMap<>(this.getTeamRegularSeasonGamesMap().get(game.getChometeamCityAbbr()));
				tempMap.put(game.getIweekNumber(), game.getCawayteamCityAbbr().toUpperCase() + game.getGameTimeOnly().substring(1, 2));
				this.getTeamRegularSeasonGamesMap().replace(game.getChometeamCityAbbr(),tempMap);
			}
			else
			{
				Map<Integer,String> tempMap = new HashMap<>();
				tempMap.put(game.getIweekNumber(), game.getCawayteamCityAbbr().toUpperCase() + game.getGameTimeOnly().substring(1, 2));
				this.getTeamRegularSeasonGamesMap().put(game.getChometeamCityAbbr(),tempMap);
			}
		}
		
		establishByeWeeks();
		
		logger.info("done setting team regular season games map");
	}
		
	private void establishByeWeeks() 
	{
		Map<String, Integer> byeWeeksMap = new HashMap<>();
		
		for (var team : this.getTeamRegularSeasonGamesMap().entrySet()) 
		{
		    Map<Integer, String> opponentsMap = team.getValue();
		    
		    int lastWeekNum = 1;
		    for (var opponentsEntry : opponentsMap.entrySet()) 
			{
			    if (opponentsEntry.getKey() - lastWeekNum > 1)
			    {
			    	byeWeeksMap.put(team.getKey(), Integer.valueOf(lastWeekNum + 1));
			    }
			    lastWeekNum = opponentsEntry.getKey();
			}
		}
		
		for (var byeWeeksEntry : byeWeeksMap.entrySet()) 
		{
		    logger.debug("team: " + byeWeeksEntry.getKey() + " bye week: " + byeWeeksEntry.getValue());	
		    
		    Map<Integer,String> tempMap = new HashMap<>(this.getTeamRegularSeasonGamesMap().get(byeWeeksEntry.getKey()));
			tempMap.put(byeWeeksEntry.getValue(), "*BYE*");
			this.getTeamRegularSeasonGamesMap().replace(byeWeeksEntry.getKey(),tempMap);
		}
		
	}

	public void setMaxRegularSeasonWeek()
	{
		int maxweekid = 0;
		int maxweeknumber = 0;
		
		if (this.getSeasonGamesList() != null)
		{
			for (int i = 0; i < this.getSeasonGamesList().size(); i++) 
			{
				DynamoTransaction nflgame = this.getSeasonGamesList().get(i);
				if (nflgame.getSgameTypeDesc().equalsIgnoreCase("Regular Season") && nflgame.getIweekId() > maxweekid)
				{
					maxweekid = nflgame.getIweekId();
					maxweeknumber = nflgame.getIweekNumber();
				}
			}			
		}
		
		this.setMaxRegularSeasonWeekID(maxweekid);
		this.setMaxRegularSeasonWeekNumber(maxweeknumber);
	}
	
	public List<DynamoTransaction> getGameScoresList(String byTeamOrWeek, Integer weekNumberOrTeamID) 
	{
		List<DynamoTransaction> returnList = new ArrayList<>();
		
		int tabCount = 1;
		
		for (int i = 0; i < this.getSeasonGamesList().size(); i++) 
		{
			DynamoTransaction game = this.getSeasonGamesList().get(i);
			
			if (byTeamOrWeek.equalsIgnoreCase("byWeek"))
			{
				if (game.getIweekNumber() == weekNumberOrTeamID)
				{
					game.setTabIndexAwayTeam(tabCount++);
					game.setTabIndexHomeTeam(tabCount++);
					returnList.add(game);
				}
			}
			else if (byTeamOrWeek.equalsIgnoreCase("byTeam"))
			{
				if (game.getIhomeTeamID() == weekNumberOrTeamID
				||  game.getIawayTeamID() == weekNumberOrTeamID)
				{
					game.setTabIndexAwayTeam(tabCount++);
					game.setTabIndexHomeTeam(tabCount++);
					returnList.add(game);
				}
			}
			
		} 
		
		return returnList;
	}
		
	private void refreshListsAndMaps(String function, DynamoTransaction dynamoNflgame)
	{
		Integer seasonId = dynamoNflgame.getiSeasonId();
		
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullNflGamesMap().remove(dynamoNflgame.getIgameId());	
			
			List<DynamoTransaction> found = new ArrayList<DynamoTransaction>();
			List<DynamoTransaction> seasonGamesList = this.getGamesMapBySeason().get(seasonId);
			List<DynamoTransaction> newList = this.getGamesMapBySeason().get(seasonId);
			for (int i = 0; i < seasonGamesList.size(); i++) 
			{
				DynamoTransaction game = seasonGamesList.get(i);
				if (game.getIgameId() == dynamoNflgame.getIgameId())
				{
					found.add(game);
					break;
				}
			}
			newList.removeAll(found);
			this.getGamesMapBySeason().replace(seasonId, newList);	
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullNflGamesMap().put(dynamoNflgame.getIgameId(), dynamoNflgame);
			
			//First game of an import new season's games won't have this yet, add it
			if (!this.getGamesMapBySeason().containsKey(seasonId))
			{
				this.getGamesMapBySeason().put(seasonId, new ArrayList<DynamoTransaction>());
			}
			ArrayList<DynamoTransaction> newList = new ArrayList<>(this.getGamesMapBySeason().get(seasonId));
			newList.add(dynamoNflgame);
			this.getGamesMapBySeason().replace(seasonId, newList);			
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullNflGamesMap().remove(dynamoNflgame.getIgameId());		
			this.getFullNflGamesMap().put(dynamoNflgame.getIgameId(), dynamoNflgame);	
			List<DynamoTransaction> newList = new ArrayList<>(this.getGamesMapBySeason().get(seasonId));
			
			List<DynamoTransaction> found = new ArrayList<DynamoTransaction>();
			List<DynamoTransaction> seasonGamesList = this.getGamesMapBySeason().get(seasonId);
			
			for (int i = 0; i < seasonGamesList.size(); i++) 
			{
				DynamoTransaction game = seasonGamesList.get(i);
				if (game.getIgameId() == dynamoNflgame.getIgameId())
				{
					found.add(game);
					break;
				}
			}
			newList.removeAll(found);
			newList.add(dynamoNflgame);
			this.getGamesMapBySeason().replace(seasonId, newList);	
		}
		
		this.getFullNflGameList().clear();
		Collection<DynamoTransaction> values = this.getFullNflGamesMap().values();
		this.setFullNflGameList(new ArrayList<>(values));		
		Collections.sort(this.getFullNflGameList(), new GameComparator());	
		
		this.setSeasonGamesList(this.getGamesMapBySeason().get(seasonId));        
		Collections.sort(this.getSeasonGamesList(), new GameComparator());			
	}
	
	public List<DynamoTransaction> getFullNflGameList() 
	{
		return fullNflGameList;
	}

	public void setFullNflGameList(List<DynamoTransaction> fullNflGameList) 
	{
		this.fullNflGameList = fullNflGameList;
	}

	public Map<Integer, DynamoTransaction> getFullNflGamesMap() {
		return fullNflGamesMap;
	}

	public void setFullNflGamesMap(Map<Integer, DynamoTransaction> fullNflGamesMap) {
		this.fullNflGamesMap = fullNflGamesMap;
	}

	public Map<Integer, List<DynamoTransaction>> getGamesMapBySeason() {
		return gamesMapBySeason;
	}

	public void setGamesMapBySeason(Map<Integer, List<DynamoTransaction>> gamesMapBySeason) {
		this.gamesMapBySeason = gamesMapBySeason;
	}

	public List<DynamoTransaction> getSeasonGamesList() {
		return seasonGamesList;
	}

	public void setSeasonGamesList(List<DynamoTransaction> seasonGamesList) {
		this.seasonGamesList = seasonGamesList;
	}

	public List<SelectItem> getGameTypesList() {
		return gameTypesList;
	}

	public void setGameTypesList(List<SelectItem> gameTypesList) {
		this.gameTypesList = gameTypesList;
	}

	public Map<Integer, String> getGameTypesMap() {
		return gameTypesMap;
	}

	public void setGameTypesMap(Map<Integer, String> gameTypesMap) {
		this.gameTypesMap = gameTypesMap;
	}

	public String getGameTypeDescriptionByGameTypeId(int gameTypeId) 
	{
		return this.getGameTypesMap().get(gameTypeId);
	}

	public Map<Integer, Integer> getWeeksMapByWeekNumber() {
		return weeksMapByWeekNumber;
	}

	public void setWeeksMapByWeekNumber(Map<Integer, Integer> weeksMapByWeekNumber) {
		this.weeksMapByWeekNumber = weeksMapByWeekNumber;
	}

	public Integer getWeekIdByWeekNumber(int weekNumber) 
	{
		return this.getWeeksMapByWeekNumber().get(weekNumber);
	}

	public int getMaxRegularSeasonWeekID() {
		return maxRegularSeasonWeekID;
	}

	public void setMaxRegularSeasonWeekID(int maxRegularSeasonWeekID) {
		this.maxRegularSeasonWeekID = maxRegularSeasonWeekID;
	}

	public int getMaxRegularSeasonWeekNumber() {
		return maxRegularSeasonWeekNumber;
	}

	public void setMaxRegularSeasonWeekNumber(int maxRegularSeasonWeekNumber) {
		this.maxRegularSeasonWeekNumber = maxRegularSeasonWeekNumber;
	}

	public List<DynamoTransaction> getPlayoffGamesList() 
	{
		playoffGamesList.clear();
		
		for (int i = 0; i < this.getSeasonGamesList().size(); i++) 
		{
			DynamoTransaction nflgame = this.getSeasonGamesList().get(i);
			if (nflgame.getSgameTypeDesc().equalsIgnoreCase("Regular Season"))
			{
				continue;
			}
			else
			{
				playoffGamesList.add(nflgame);
			}
		}
		return playoffGamesList;
	}

	public void setPlayoffGamesList(List<DynamoTransaction> playoffGamesList) {
		this.playoffGamesList = playoffGamesList;
	}

	public Schedule getScheduleTitleRow() 
	{
		return scheduleTitleRow;
	}
	
	public void setScheduleTitleRow(Schedule scheduleTitleRow) {
		this.scheduleTitleRow = scheduleTitleRow;
	}

	public Map<String, Map<Integer, String>> getTeamRegularSeasonGamesMap() {
		return teamRegularSeasonGamesMap;
	}

	public void setTeamRegularSeasonGamesMap(Map<String, Map<Integer, String>> teamRegularSeasonGamesMap) {
		this.teamRegularSeasonGamesMap = teamRegularSeasonGamesMap;
	}

}
