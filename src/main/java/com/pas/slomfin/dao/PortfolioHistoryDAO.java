package com.pas.slomfin.dao;

import com.pas.beans.PortfolioHistory;
import com.pas.dynamodb.DynamoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class PortfolioHistoryDAO implements Serializable 
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PortfolioHistoryDAO.class);

    private Map<Integer, PortfolioHistory> fullPortfolioHistoryMap = new HashMap<>();
    private List<PortfolioHistory> fullPortfolioHistoryList = new ArrayList<>();

    private static DynamoDbTable<PortfolioHistory> portfolioHistoryTable;
    private static final String AWS_TABLE_NAME = "slomFinPortfolioHistory";

    public PortfolioHistoryDAO(DynamoClients dynamoClients2)
    {
        try
        {
            portfolioHistoryTable = dynamoClients2.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(PortfolioHistory.class));
        }
        catch (final Exception ex)
        {
            logger.error("Got exception while initializing PortfolioHistoryDAO. Ex = " + ex.getMessage(), ex);
        }
    }

    public Integer addPortfolioHistory(PortfolioHistory portfolioHistory) throws Exception
    {
        PortfolioHistory portfolioHistory2 = dynamoUpsert(portfolioHistory);

        portfolioHistory.setiPortfolioHistoryID(portfolioHistory2.getiPortfolioHistoryID());

        logger.info("LoggedDBOperation: function-add; table:portfolioHistory; rows:1");

        refreshListsAndMaps("add", portfolioHistory);

        logger.info("addPortfolioHistory complete");

        return portfolioHistory2.getiPortfolioHistoryID(); //this is the key that was just added
    }

    private PortfolioHistory dynamoUpsert(PortfolioHistory portfolioHistory) throws Exception
    {
        PortfolioHistory dynamoPortfolioHistory = new PortfolioHistory();

        if (portfolioHistory.getiPortfolioHistoryID() == null)
        {
            Integer currentMaxPortfolioHistoryID = 0;
            for (int i = 0; i < this.getFullPortfolioHistoryList().size(); i++)
            {
                PortfolioHistory portfolioHistory2 = this.getFullPortfolioHistoryList().get(i);
                currentMaxPortfolioHistoryID = portfolioHistory2.getiPortfolioHistoryID();
            }
            dynamoPortfolioHistory.setiPortfolioHistoryID(currentMaxPortfolioHistoryID + 1);
        }
        else
        {
            dynamoPortfolioHistory.setiPortfolioHistoryID(portfolioHistory.getiPortfolioHistoryID());
        }

        PutItemEnhancedRequest<PortfolioHistory> putItemEnhancedRequest = PutItemEnhancedRequest.builder(PortfolioHistory.class).item(dynamoPortfolioHistory).build();
        portfolioHistoryTable.putItem(putItemEnhancedRequest);

        return dynamoPortfolioHistory;
    }

    public void updatePortfolioHistory(PortfolioHistory portfolioHistory)  throws Exception
    {
        dynamoUpsert(portfolioHistory);

        logger.info("LoggedDBOperation: function-update; table:portfolioHistory; rows:1");

        refreshListsAndMaps("update", portfolioHistory);

        logger.debug("update portfolioHistory table complete");
    }

    public void readPortfolioHistoryFromDB()
    {
        for (PortfolioHistory portfolioHistory : portfolioHistoryTable.scan().items())
        {
            this.getFullPortfolioHistoryList().add(portfolioHistory);
        }

        logger.info("LoggedDBOperation: function-inquiry; table:portfolioHistory; rows:{}", this.getFullPortfolioHistoryList().size());

        this.setFullPortfolioHistoryMap(this.getFullPortfolioHistoryList().stream().collect(Collectors.toMap(PortfolioHistory::getiPortfolioHistoryID, ply -> ply)));

        this.getFullPortfolioHistoryList().sort(new Comparator<PortfolioHistory>() {
            public int compare(PortfolioHistory o1, PortfolioHistory o2) {
                return o1.getHistoryDate().compareTo(o2.getHistoryDate());
            }
        });
    }

    private void refreshListsAndMaps(String function, PortfolioHistory portfolioHistory)
    {
        if (function.equalsIgnoreCase("delete"))
        {
            this.getFullPortfolioHistoryMap().remove(portfolioHistory.getiPortfolioHistoryID());
        }
        else if (function.equalsIgnoreCase("add"))
        {
            this.getFullPortfolioHistoryMap().put(portfolioHistory.getiPortfolioHistoryID(), portfolioHistory);
        }
        else if (function.equalsIgnoreCase("update"))
        {
            this.getFullPortfolioHistoryMap().remove(portfolioHistory.getiPortfolioHistoryID());
            this.getFullPortfolioHistoryMap().put(portfolioHistory.getiPortfolioHistoryID(), portfolioHistory);
        }

        this.getFullPortfolioHistoryList().clear();
        Collection<PortfolioHistory> values = this.getFullPortfolioHistoryMap().values();
        this.setFullPortfolioHistoryList(new ArrayList<>(values));

        this.getFullPortfolioHistoryList().sort(new Comparator<PortfolioHistory>() {
            public int compare(PortfolioHistory o1, PortfolioHistory o2) {
                return o1.getiPortfolioHistoryID().compareTo(o2.getiPortfolioHistoryID());
            }
        });

    }

    public List<PortfolioHistory> getFullPortfolioHistoryList()
    {
        return fullPortfolioHistoryList;
    }

    public void setFullPortfolioHistoryList(List<PortfolioHistory> fullPortfolioHistoryList)
    {
        this.fullPortfolioHistoryList = fullPortfolioHistoryList;
    }

    public Map<Integer, PortfolioHistory> getFullPortfolioHistoryMap() {
        return fullPortfolioHistoryMap;
    }

    public void setFullPortfolioHistoryMap(Map<Integer, PortfolioHistory> fullPortfolioHistoryMap) {
        this.fullPortfolioHistoryMap = fullPortfolioHistoryMap;
    }

    public PortfolioHistory getPortfolioHistoryByPortfolioHistoryID(int portfolioHistoryId)
    {
        return this.getFullPortfolioHistoryMap().get(portfolioHistoryId);
    }
}
