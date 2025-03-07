package com.pas.slomfin.dao;

import com.pas.beans.Investment;
import com.pas.dynamodb.DynamoClients;

import jakarta.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class InvestmentDAO implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(InvestmentDAO.class);

    private Map<Integer, Investment> fullInvestmentsMap = new HashMap<>();
    private List<Investment> fullInvestmentsList = new ArrayList<>();
    
    private Map<Integer,List<Investment>> investmentsMapByInvestmentTypeID = new HashMap<>();
    
    private Integer cashInvestmentId;
    
    private static DynamoDbTable<Investment> investmentsTable;
    private static final String AWS_TABLE_NAME = "slomFinInvestments";

    public InvestmentDAO(DynamoClients dynamoClients2)
    {
        try
        {
            investmentsTable = dynamoClients2.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(Investment.class));
        }
        catch (final Exception ex)
        {
            logger.error("Got exception while initializing InvestmentsDAO. Ex = " + ex.getMessage(), ex);
        }
    }

    public Integer addInvestment(Investment investment) throws Exception
    {
        Investment investment2 = dynamoUpsert(investment);

        investment.setiInvestmentID(investment2.getiInvestmentID());

        logger.info("LoggedDBOperation: function-add; table:investment; rows:1");

        refreshListsAndMaps("add", investment);

        logger.info("addInvestment complete.  We just added investment id: " + investment2.getiInvestmentID());

        return investment2.getiInvestmentID(); //this is the key that was just added
    }

    private Integer determineNextInvestmentId() 
	{
		int nextID = 0;
		
		List<Integer> ids = this.getFullInvestmentsMap().keySet().stream().toList();
		int max = Collections.max(ids);
        nextID = max + 1;
        
		return nextID;
	}
    
    private Investment dynamoUpsert(Investment investment) throws Exception
    {
        if (investment.getiInvestmentID() == null)
        {            
            investment.setiInvestmentID(determineNextInvestmentId());
        }
      
        PutItemEnhancedRequest<Investment> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Investment.class).item(investment).build();
        investmentsTable.putItem(putItemEnhancedRequest);

        return investment;
    }

    public void updateInvestment(Investment investment)  throws Exception
    {
        dynamoUpsert(investment);

        logger.info("LoggedDBOperation: function-update; table:investment; rows:1");

        refreshListsAndMaps("update", investment);

        logger.debug("update investment table complete");
    }

    public void deleteInvestment(Investment investment) 
	{
		Key key = Key.builder().partitionValue(investment.getiInvestmentID()).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		investmentsTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:investment; rows:1");
		
		refreshListsAndMaps("delete", investment);		
		
		logger.info("delete investment complete");	
	}
    
    public void readInvestmentsFromDB()
    {
    	Iterator<Investment> results = investmentsTable.scan().items().iterator();
    	
    	while (results.hasNext()) 
        {
			Investment investment = results.next();
			this.getFullInvestmentsList().add(investment); 
        }     

        logger.info("LoggedDBOperation: function-inquiry; table:investment; rows:{}", this.getFullInvestmentsList().size());

        for (int i = 0; i < this.getFullInvestmentsList().size(); i++)
        {
			Investment investment = this.getFullInvestmentsList().get(i);
			//logger.info("About to store investment ID in map: " + investment.getiInvestmentID() + " which is: " + investment.getDescription());
			this.getFullInvestmentsMap().put(investment.getiInvestmentID(), investment);
			
			if (investment.getTickerSymbol() != null && investment.getTickerSymbol().equalsIgnoreCase("CASH"))
			{
				this.setCashInvestmentId(investment.getiInvestmentID());
			}
			
			
			
		}
        this.setFullInvestmentsMap(this.getFullInvestmentsList().stream().collect(Collectors.toMap(Investment::getiInvestmentID, ply -> ply)));

        this.getFullInvestmentsList().sort(new Comparator<Investment>() {
            public int compare(Investment o1, Investment o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });
        
        for (int i = 0; i < this.getFullInvestmentsList().size(); i++)
        {
			Investment investment = this.getFullInvestmentsList().get(i);
			
	        if (this.getInvestmentsMapByInvestmentTypeID().containsKey(investment.getiInvestmentTypeID()))
			{
				List<Investment> tempList = this.getInvestmentsMapByInvestmentTypeID().get(investment.getiInvestmentTypeID());
				tempList.add(investment);
				this.getInvestmentsMapByInvestmentTypeID().replace(investment.getiInvestmentTypeID(), tempList);
			}
			else
			{
				List<Investment> tempList = new ArrayList<>();
				tempList.add(investment);
				this.getInvestmentsMapByInvestmentTypeID().put(investment.getiInvestmentTypeID(), tempList);
			}
        }
        
    }

    private void refreshListsAndMaps(String function, Investment investment)
    {
        if (function.equalsIgnoreCase("delete"))
        {
            this.getFullInvestmentsMap().remove(investment.getiInvestmentID());
        }
        else if (function.equalsIgnoreCase("add"))
        {
            this.getFullInvestmentsMap().put(investment.getiInvestmentID(), investment);
        }
        else if (function.equalsIgnoreCase("update"))
        {
            this.getFullInvestmentsMap().remove(investment.getiInvestmentID());
            this.getFullInvestmentsMap().put(investment.getiInvestmentID(), investment);
        }

        this.getFullInvestmentsList().clear();
        Collection<Investment> values = this.getFullInvestmentsMap().values();
        this.setFullInvestmentsList(new ArrayList<>(values));

        this.getFullInvestmentsList().sort(new Comparator<Investment>() {
            public int compare(Investment o1, Investment o2) {
                return o1.getiInvestmentID().compareTo(o2.getiInvestmentID());
            }
        });

    }

    public List<Investment> getFullInvestmentsList()
    {
        return fullInvestmentsList;
    }

    public void setFullInvestmentsList(List<Investment> fullInvestmentsList)
    {
        this.fullInvestmentsList = fullInvestmentsList;
    }

    public Map<Integer, Investment> getFullInvestmentsMap() {
        return fullInvestmentsMap;
    }

    public void setFullInvestmentsMap(Map<Integer, Investment> fullInvestmentsMap) {
        this.fullInvestmentsMap = fullInvestmentsMap;
    }

    public Investment getInvestmentByInvestmentID(Integer investmentId)
    {
        return this.getFullInvestmentsMap().get(investmentId);
    }

    public List<SelectItem> getInvestmentsByInvestmentTypeID(int investmentTypeId)
    {
    	List<Investment> invList = this.getInvestmentsMapByInvestmentTypeID().get(investmentTypeId);
    	List<SelectItem> returnList = new ArrayList<>();
    	
    	SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		returnList.add(si1);
		
    	for (int i = 0; i < invList.size(); i++) 
    	{
    		Investment inv = invList.get(i);
    		SelectItem si = new SelectItem();
			si.setValue(inv.getiInvestmentID());
			si.setLabel(inv.getDescription());
			returnList.add(si);
		}
    	
        return returnList;
    }
    
	public Integer getCashInvestmentId() {
		return cashInvestmentId;
	}

	public void setCashInvestmentId(Integer cashInvestmentId) {
		this.cashInvestmentId = cashInvestmentId;
	}

	public Map<Integer, List<Investment>> getInvestmentsMapByInvestmentTypeID() {
		return investmentsMapByInvestmentTypeID;
	}

	public void setInvestmentsMapByInvestmentTypeID(Map<Integer, List<Investment>> investmentsMapByInvestmentTypeID) {
		this.investmentsMapByInvestmentTypeID = investmentsMapByInvestmentTypeID;
	}

	
}
