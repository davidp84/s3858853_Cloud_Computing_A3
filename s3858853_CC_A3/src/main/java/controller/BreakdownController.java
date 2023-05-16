package controller;

import java.util.HashMap;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import model.BreakdownEvent;

public class BreakdownController {

	// Receives event and uploads to database
	public void createEvent(BreakdownEvent breakdown) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("breakdown");

		try {
			table.putItem(new Item()
					.withPrimaryKey("user-name", breakdown.getUserName(), "dateTime", breakdown.getDateTimeUTC())
					.withBoolean("current", breakdown.isCurrent()).withString("event", breakdown.getEventString())
					.withNumber("latitude", breakdown.getLatitude()).withNumber("longitude", breakdown.getLongitude()));
			System.out.println("PutItem succeeded: " + " " + breakdown.getEventString());
		} catch (Exception e) {
			System.err.println("Unable to register event!");
			System.err.println(e.getMessage());
		}

	}

	// Receives keys and sets the 'current' status from true to false.
	public void removeEvent(String user, String event) throws Exception {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("breakdown");

		HashMap<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("#ct", "current");

		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(":current", false);

		System.out.println("" + user + " - " + event);
		UpdateItemSpec updateItemSpec = new UpdateItemSpec()
				.withPrimaryKey(new PrimaryKey("user-name", user, "dateTime", event))
				.withUpdateExpression("set #ct = :current").withNameMap(nameMap).withValueMap(valueMap)
				.withReturnValues(ReturnValue.UPDATED_NEW);

		try {
			System.out.println("Attempting a conditional delete...");
			table.updateItem(updateItemSpec);
			System.out.println("DeleteItem succeeded");
		} catch (Exception e) {
			System.err.println("Unable to register event!");
			System.err.println(e.getMessage());
		}
	}
}