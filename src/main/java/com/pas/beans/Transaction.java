package com.pas.beans;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.pas.dynamodb.DynamoTransaction;
import com.pas.spring.SpringBean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("pc_Transaction")
@SessionScoped
public class Transaction implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(Transaction.class);	
	
	private DynamoTransaction selectedTransaction;
        
    @Inject SlomFinMain slomFinMain;

	@Autowired SpringBean springBean;

    public String toString()
    {
    	return "transactionid: " + this.getSelectedTransaction().getTransactionID() + 
				" trxdate: " + selectedTransaction.getTransactionPostedDate() + 
				" account: " + selectedTransaction.getAccountName() +
				" cost/Proceeds: " + selectedTransaction.getCostProceeds();
    }
   
    @PostConstruct
    public void init()
    {
    	logger.info("entering postconstruct init method of Transaction");		
    }
    
    public String addChangeDelTransaction() 
	{	 
		logger.info("entering addChangeDelTransaction.  Action will be: " + slomFinMain.getTransactionAcidSetting());
		
		if (!validateTransactionEntry()) //will be true if all good.  If false, we leave
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please enter all required fields",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
			return "";
		}
		
		try
		{
			if (slomFinMain.getTransactionAcidSetting().equalsIgnoreCase("Add"))
			{			
				setAddUpdateFields();			   
				slomFinMain.getTransactionDAO().addTransaction(this.getSelectedTransaction());
			}
			else if (slomFinMain.getTransactionAcidSetting().equalsIgnoreCase("Update"))
			{
				setAddUpdateFields();		
				slomFinMain.getTransactionDAO().updateTransaction(this.getSelectedTransaction());
			}
			else if (slomFinMain.getTransactionAcidSetting().equalsIgnoreCase("Delete"))
			{
				slomFinMain.getTransactionDAO().deleteTransaction(this.getSelectedTransaction());
			}
		}
		catch (Exception e)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
			return "";
		}
	    return "gamesList.xhtml";
	}
  	private void setAddUpdateFields() 
    {
    	
	}

	public void selectTransactionforAcid(ActionEvent event) 
	{
		logger.info("game selected for add-change-inquire-delete");
		
		try 
        {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		    String acid = ec.getRequestParameterMap().get("operation");
		    String gameId = ec.getRequestParameterMap().get("gameId");
		    slomFinMain.setTransactionAcidSetting(acid);
		    logger.info("game id selected: " + gameId);
		    
		    if (slomFinMain.getTransactionAcidSetting().equalsIgnoreCase("add"))
		    {
		    	this.setSelectedTransaction(new DynamoTransaction()); 
		    }
		    else //go get the existing game
		    {
		    	this.setSelectedTransaction(slomFinMain.getTransactionByTransactionID(Integer.parseInt(gameId)));
		    }
		    
		    slomFinMain.setRenderTransactionViewAddUpdateDelete();

			String targetURL = springBean.getContextRoot() + "/gameAddUpdate.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL + " with operation: " + acid);
        } 
        catch (Exception e) 
        {
            logger.error("selectTransactionforAcid exception: " + e.getMessage(), e);
        }
	}
    
	private boolean validateTransactionEntry()
    {
		boolean fieldsValidated = true; //assume true, if anything wrong make it false and get out
		
		if (this.getSelectedTransaction().getTransactionPostedDate() == null || this.getSelectedTransaction().getTransactionPostedDate().trim().length() == 0)
		{
			fieldsValidated = false;
		}
		else //something in the date - make sure it parses in our format
		{
			SimpleDateFormat sdf = new SimpleDateFormat("E yyyy-MM-dd HH:mm a");
	        try 
	        {
	            Date parsedDate = sdf.parse(this.getSelectedTransaction().getTransactionPostedDate());
	            logger.debug("parsed date = " + parsedDate);
	        }
	        catch (Exception e) 
	        {
	        	logger.error("Error parsing date: " + this.getSelectedTransaction().getTransactionPostedDate());
	        	fieldsValidated = false;
	        }
		}
		return fieldsValidated;
	}

	public DynamoTransaction getSelectedTransaction() {
		return selectedTransaction;
	}

	public void setSelectedTransaction(DynamoTransaction selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}
	
}
