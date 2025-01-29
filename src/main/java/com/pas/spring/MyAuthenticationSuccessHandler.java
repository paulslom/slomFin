package com.pas.spring;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pas.slomfin.constants.INFLAppConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{ 
	private static Logger logger = LogManager.getLogger(MyAuthenticationSuccessHandler.class);
	
	@Autowired
    private SpringBean springBean;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication) throws IOException 
    {
    	logger.info("entering onAuthenticationSuccess method of MyAuthenticationSuccessHandler");
    	
        HttpSession session = request.getSession(true);
        if (session != null) 
        {
            session.setAttribute("currentGolfUser", authentication.getName()); //so we can know who this is later
            if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) 
            {
            	session.setAttribute("currentUserisAdmin", true);
            }
            else
            {
            	session.setAttribute("currentUserisAdmin", false);            	
            }
            
            if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) 
            {
            	session.setAttribute("currentUserisUser", true);
            }
            else
            {
            	session.setAttribute("currentUserisUser", false);
            }
            
            session.setAttribute(INFLAppConstants.CONTEXT_ROOT, springBean.getContextRoot());
            
        }       
        
        String whereTo = springBean.getContextRoot() + "/main.xhtml";
        logger.info("redirecting to: " + whereTo);
		response.sendRedirect(whereTo);
        
    }	
}