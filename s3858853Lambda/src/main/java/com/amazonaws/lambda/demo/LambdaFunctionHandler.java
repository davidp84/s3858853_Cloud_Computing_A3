package com.amazonaws.lambda.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	private DynamoDB dynamoDb;
	private String DYNAMO_DB_TABLE_NAME = "breakdown";
	private Regions REGION = Regions.US_EAST_1;

    @Override
	public String handleRequest(Object input, Context context) {
		this.initDynamoDbClient();

		return retrieveData();
	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}

	private String retrieveData() {
		Table table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);

		ScanSpec scanSpec = new ScanSpec().withProjectionExpression("#un, event, #de, #ct, latitude, longitude")
				.withFilterExpression("#ct = :current")
				.withNameMap(new NameMap().with("#un", "user-name").with("#ct", "current").with("#de", "dateTime"))
				.withValueMap(new ValueMap().withBoolean(":current", true));

		Map<String, String> info = new HashMap<String, String>();

		JSONObject jo = new JSONObject();

		try {
			ItemCollection<ScanOutcome> items = table.scan(scanSpec);

			Iterator<Item> iter = items.iterator();
			int i = 0;

			while (iter.hasNext()) {
				Item item = iter.next();
				info.put("userName", item.getString("user-name"));
				info.put("event", item.getString("event").toUpperCase());
				info.put("dateTime", item.getString("dateTime"));
				info.put("latitude", String.valueOf(item.getDouble("latitude")));
				info.put("longitude", String.valueOf(item.getDouble("longitude")));
				jo.put(String.valueOf(i), info);
				i++;
			}

		} catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}

		return jo.toString();
	}

}
