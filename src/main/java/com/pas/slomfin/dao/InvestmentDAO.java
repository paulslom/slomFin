package com.pas.slomfin.dao;

import com.pas.beans.Investment;
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

public class InvestmentDAO implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(InvestmentDAO.class);

    private Map<Integer, Investment> fullInvestmentsMap = new HashMap<>();
    private List<Investment> fullInvestmentsList = new ArrayList<>();

    private static DynamoDbTable<Investment> investmentsTable;
    private static final String AWS_TABLE_NAME = "SlomFinInvestments";

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

        investment.setInvestmentID(investment2.getInvestmentID());

        logger.info("LoggedDBOperation: function-add; table:investment; rows:1");

        refreshListsAndMaps("add", investment);

        logger.info("addInvestment complete");

        return investment2.getInvestmentID(); //this is the key that was just added
    }

    private Investment dynamoUpsert(Investment investment) throws Exception
    {
        Investment dynamoInvestment = new Investment();

        if (investment.getInvestmentID() == null)
        {
            Integer currentMaxInvestmentID = 0;
            for (int i = 0; i < this.getFullInvestmentsList().size(); i++)
            {
                Investment investment2 = this.getFullInvestmentsList().get(i);
                currentMaxInvestmentID = investment2.getInvestmentID();
            }
            dynamoInvestment.setInvestmentID(currentMaxInvestmentID + 1);
        }
        else
        {
            dynamoInvestment.setInvestmentID(investment.getInvestmentID());
        }

        PutItemEnhancedRequest<Investment> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Investment.class).item(dynamoInvestment).build();
        investmentsTable.putItem(putItemEnhancedRequest);

        return dynamoInvestment;
    }

    public void updateInvestment(Investment investment)  throws Exception
    {
        dynamoUpsert(investment);

        logger.info("LoggedDBOperation: function-update; table:investment; rows:1");

        refreshListsAndMaps("update", investment);

        logger.debug("update investment table complete");
    }

    public void readInvestmentsFromDB()
    {
        for (Investment investment : investmentsTable.scan().items())
        {
            this.getFullInvestmentsList().add(investment);
        }

        logger.info("LoggedDBOperation: function-inquiry; table:investment; rows:{}", this.getFullInvestmentsList().size());

        this.setFullInvestmentsMap(this.getFullInvestmentsList().stream().collect(Collectors.toMap(Investment::getInvestmentID, ply -> ply)));

        this.getFullInvestmentsList().sort(new Comparator<Investment>() {
            public int compare(Investment o1, Investment o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });
    }

    private void refreshListsAndMaps(String function, Investment investment)
    {
        if (function.equalsIgnoreCase("delete"))
        {
            this.getFullInvestmentsMap().remove(investment.getInvestmentID());
        }
        else if (function.equalsIgnoreCase("add"))
        {
            this.getFullInvestmentsMap().put(investment.getInvestmentID(), investment);
        }
        else if (function.equalsIgnoreCase("update"))
        {
            this.getFullInvestmentsMap().remove(investment.getInvestmentID());
            this.getFullInvestmentsMap().put(investment.getInvestmentID(), investment);
        }

        this.getFullInvestmentsList().clear();
        Collection<Investment> values = this.getFullInvestmentsMap().values();
        this.setFullInvestmentsList(new ArrayList<>(values));

        this.getFullInvestmentsList().sort(new Comparator<Investment>() {
            public int compare(Investment o1, Investment o2) {
                return o1.getInvestmentID().compareTo(o2.getInvestmentID());
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

    public Investment getInvestmentByInvestmentID(int investmentId)
    {
        return this.getFullInvestmentsMap().get(investmentId);
    }
}
