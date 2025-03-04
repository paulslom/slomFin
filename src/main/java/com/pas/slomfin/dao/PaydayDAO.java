package com.pas.slomfin.dao;

import com.pas.beans.Payday;
import com.pas.dynamodb.DynamoClients;
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

public class PaydayDAO implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PaydayDAO.class);

    private Map<Integer, Payday> fullPaydaysMap = new HashMap<>();
    private List<Payday> fullPaydaysList = new ArrayList<>();

    private static DynamoDbTable<Payday> paydaysTable;
    private static final String AWS_TABLE_NAME = "slomFinPayday";

    public PaydayDAO(DynamoClients dynamoClients2)
    {
        try
        {
            paydaysTable = dynamoClients2.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(Payday.class));
        }
        catch (final Exception ex)
        {
            logger.error("Got exception while initializing PaydaysDAO. Ex = " + ex.getMessage(), ex);
        }
    }

    public Integer addPayday(Payday payday) throws Exception
    {
        Payday payday2 = dynamoUpsert(payday);

        payday.setPaydayID(payday2.getPaydayID());

        logger.info("LoggedDBOperation: function-add; table:payday; rows:1");

        refreshListsAndMaps("add", payday);

        logger.info("addPayday complete");

        return payday2.getPaydayID(); //this is the key that was just added
    }
    
    public void deletePayday(Payday payday) throws Exception
    {
    	Key key = Key.builder().partitionValue(payday.getPaydayID()).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		paydaysTable.deleteItem(deleteItemEnhancedRequest);

        logger.info("LoggedDBOperation: function-delete; table:payday; rows:1");

        refreshListsAndMaps("delete", payday);

        logger.info("deletePayday complete");
    }    

    private Payday dynamoUpsert(Payday payday) throws Exception
    {
        if (payday.getPaydayID() == null)
        {
            payday.setPaydayID(determineNextPaydayId());
        }
       
        PutItemEnhancedRequest<Payday> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Payday.class).item(payday).build();
        paydaysTable.putItem(putItemEnhancedRequest);

        return payday;
    }

    private Integer determineNextPaydayId() 
	{
		int nextID = 0;
		
		List<Integer> ids = this.getFullPaydaysMap().keySet().stream().toList();
		int max = Collections.max(ids);
        nextID = max + 1;
        
		return nextID;
	}
    
    public void updatePayday(Payday payday)  throws Exception
    {
        dynamoUpsert(payday);

        logger.info("LoggedDBOperation: function-update; table:payday; rows:1");

        refreshListsAndMaps("update", payday);

        logger.debug("update payday table complete");
    }

    public void readPaydaysFromDB()
    {
        for (Payday payday : paydaysTable.scan().items())
        {
            this.getFullPaydaysList().add(payday);
        }

        logger.info("LoggedDBOperation: function-inquiry; table:payday; rows:{}", this.getFullPaydaysList().size());

        this.setFullPaydaysMap(this.getFullPaydaysList().stream().collect(Collectors.toMap(Payday::getPaydayID, ply -> ply)));

        Collections.sort(this.getFullPaydaysList(), new Comparator<Payday>()
        {
            public int compare(Payday o1, Payday o2)
            {
                return o1.getDefaultDay().compareTo(o2.getDefaultDay());
            }
        });
    }

    private void refreshListsAndMaps(String function, Payday payday)
    {
        if (function.equalsIgnoreCase("delete"))
        {
            this.getFullPaydaysMap().remove(payday.getPaydayID());
        }
        else if (function.equalsIgnoreCase("add"))
        {
            this.getFullPaydaysMap().put(payday.getPaydayID(), payday);
        }
        else if (function.equalsIgnoreCase("update"))
        {
            this.getFullPaydaysMap().remove(payday.getPaydayID());
            this.getFullPaydaysMap().put(payday.getPaydayID(), payday);
        }

        this.getFullPaydaysList().clear();
        Collection<Payday> values = this.getFullPaydaysMap().values();
        this.setFullPaydaysList(new ArrayList<>(values));

        this.getFullPaydaysList().sort(new Comparator<Payday>() {
            public int compare(Payday o1, Payday o2) {
                return o1.getPaydayID().compareTo(o2.getPaydayID());
            }
        });

    }

    public List<Payday> getFullPaydaysList()
    {
        return fullPaydaysList;
    }

    public void setFullPaydaysList(List<Payday> fullPaydaysList)
    {
        this.fullPaydaysList = fullPaydaysList;
    }

    public Map<Integer, Payday> getFullPaydaysMap() {
        return fullPaydaysMap;
    }

    public void setFullPaydaysMap(Map<Integer, Payday> fullPaydaysMap) {
        this.fullPaydaysMap = fullPaydaysMap;
    }

    public Payday getPaydayByPaydayID(int paydayId)
    {
        return this.getFullPaydaysMap().get(paydayId);
    }


}
