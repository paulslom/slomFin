package com.pas.slomfin.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.LoginForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.util.Base64Extensions;
import com.pas.util.SessionTimeOutMgr;
import com.pas.valueObject.User;

/**
 * Title: 		LoginAction
 * Description: Verifies User Login to the Portfolio database
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class LoginAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside LoginAction pre - process");
		
		LoginForm loginForm = (LoginForm) form;
		
		String userId = loginForm.getUserId();
		String password = loginForm.getPassword();
		HttpSession httpSession = req.getSession();
		
		User user = new User();
		user.setUserId(userId);
		user.setSessionId(httpSession.getId());
		Base64Extensions encrypt = new Base64Extensions ();
		String encryptedPassword = encrypt.encryptBase64(password);
		user.setPassword(encryptedPassword);
		log.debug("user id : " + userId + " entered onto form" );
		log.debug("Encrypted password is " + encryptedPassword);
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		cache.setUser(httpSession, user);
		
		cache.setObject("RequestObject", user, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		
		// binds SessionTimeOutMgr object to session
		if (httpSession.getAttribute(IAppConstants.SESSION_TIMEOUT_MANAGER) == null)
		{
			httpSession.setAttribute(IAppConstants.SESSION_TIMEOUT_MANAGER, new SessionTimeOutMgr());
		}		
		
		return true;
    }
    
    public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside LoginAction postprocessAction");
	
		ActionMessages messages = ac.getMessages();

		if (messages == null)
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		else
			if (!messages.isEmpty())
				ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
			else
				ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		String db = (String) req.getSession().getServletContext().getAttribute(ISlomFinAppConstants.CURRENTDBNAME);
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_CURRENTDB, db);
		
		log.debug("exiting LoginAction postprocessAction");
		
		
	}
}
