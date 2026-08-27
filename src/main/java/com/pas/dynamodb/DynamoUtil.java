package com.pas.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Pure Java utility for initializing DynamoDB clients.
 * No Spring dependencies. Safe for JSF startup.
 */
public class DynamoUtil {

	private static final Logger logger = LogManager.getLogger(DynamoUtil.class);

	// Hard‑coded AWS settings since JSF cannot load application.yml
	private static final String AWS_PROFILE = "PaulsAmazon";
	private static final String AWS_REGION  = "us-east-1";

	private static DynamoClients dynamoClients;

	public static synchronized DynamoClients getDynamoClients() {

		if (dynamoClients != null) {
			return dynamoClients;
		}

		logger.info("Initializing DynamoDB client using profile: {}", AWS_PROFILE);

		// Build credentials provider using the fixed profile
		ProfileCredentialsProvider provider =
				ProfileCredentialsProvider.create(AWS_PROFILE);

		// Build the DynamoDB client
		DynamoDbClient ddbClient = DynamoDbClient.builder()
				.credentialsProvider(provider)
				.region(Region.of(AWS_REGION))
				.build();

		// Build the enhanced client
		DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
				.dynamoDbClient(ddbClient)
				.build();

		// Store both in your wrapper class
		dynamoClients = new DynamoClients();
		dynamoClients.setDdbClient(ddbClient);
		dynamoClients.setDynamoDbEnhancedClient(enhancedClient);

		logger.info("DynamoDB client successfully initialized.");

		return dynamoClients;
	}
}
