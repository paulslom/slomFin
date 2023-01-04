/*
 * Created on Mar 8, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pas.slomfin.cache;

import javax.servlet.http.HttpSession;

import com.pas.slomfin.valueObject.AccountSelection;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.TransactionSelection;
import com.pas.valueObject.User;
import com.pas.exception.PASSystemException;

/**
 * @author sganapathy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ICacheManager
{
	public Object getObject(String name, HttpSession session) throws PASSystemException;
	public void setObject(String name, Object valObj, HttpSession session) throws PASSystemException;
	public void removeObject(String name, HttpSession session) throws PASSystemException;
	public void clearCache(HttpSession session) throws PASSystemException;
	public void clearCache(String name,HttpSession session) throws PASSystemException;
	public User getUser(HttpSession session) throws PASSystemException;
	public void setUser(HttpSession session, User user) throws PASSystemException;
	public Investor getInvestor(HttpSession session) throws PASSystemException;
	public void setInvestor(HttpSession session, Investor investor) throws PASSystemException;
	public AccountSelection getAccountSelection(HttpSession session) throws PASSystemException;
	public void setAccountSelection(HttpSession session, AccountSelection accountSelection) throws PASSystemException;
	public TransactionSelection getTransactionSelection(HttpSession session) throws PASSystemException;
	public void setTransactionSelection(HttpSession session, TransactionSelection transactionSelection) throws PASSystemException;
	public void setGoToDBInd(HttpSession session, boolean goToDBInd) throws PASSystemException;
	public boolean getGoToDBInd(HttpSession session) throws PASSystemException;
	
}
