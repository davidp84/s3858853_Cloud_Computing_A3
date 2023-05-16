package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import model.BreakdownEvent;

public class MainController {
	// Retrieves a list of breakdown events for the logged in user.
	public List<BreakdownEvent> getCurrentEvents(String user) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("breakdown");

		List<BreakdownEvent> events = new ArrayList<BreakdownEvent>();

		ScanSpec scanSpec = new ScanSpec().withProjectionExpression("#un, event, #de, #ct, latitude, longitude")
				.withFilterExpression("#un = :user and #ct = :current")
				.withNameMap(new NameMap().with("#un", "user-name").with("#ct", "current").with("#de", "dateTime"))
				.withValueMap(new ValueMap().withString(":user", user).withBoolean(":current", true));

		try {
			ItemCollection<ScanOutcome> items = table.scan(scanSpec);

			Iterator<Item> iter = items.iterator();
			while (iter.hasNext()) {
				Item item = iter.next();
				BreakdownEvent event = new BreakdownEvent();
				event.setUserName(item.getString(user));
				event.setCurrent(item.getBoolean("current"));
				event.setEvent(model.Event.valueOf(item.getString("event").toUpperCase()));

				event.setDateTimeUTC(item.getString("dateTime"));

				event.setLatitude(item.getDouble("latitude"));
				event.setLongitude(item.getDouble("longitude"));
				events.add(event);
			}

		} catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}

		return events;
	}
}
