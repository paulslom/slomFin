package com.pas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {
    "com.pas.spring",
    "com.pas.services"
})

public class Application extends SpringBootServletInitializer 
{
	private static Logger logger = LogManager.getLogger(Application.class);	
	public static void main(String[] args) 
	{
		logger.info("about to kick off Slom Fin Spring boot application");
		System.setProperty(org.apache.tomcat.util.scan.Constants.SKIP_JARS_PROPERTY,"*.jar");
		SpringApplication.run(Application.class, args);
		logger.info("Spring boot application started");
	}
	
}
