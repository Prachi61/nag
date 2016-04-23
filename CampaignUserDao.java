package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.Play;

import model.dynamo.CampaignUser;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class CampaignUserDao {

	static AmazonDynamoDBClient dynamoDB = null;
	static DynamoDBMapper mapper = null;
	//private static final String secretKey ;
	//private static final String accessKey ;
	public static volatile AWSCredentials credentials = null;
	static{
		try {

			
			String secretKey = Play.application().configuration().getString("aws.secretKey");
			String accessKey = Play.application().configuration().getString("aws.accessKey");
			 
			if(credentials == null) 
						credentials = new BasicAWSCredentials(accessKey, secretKey);
		//		credentials = new BasicAWSCredentials("AKIAJORB43IQ4JYAZMUQ", "aB8pHXXTuUF6ERcfNW18EgGBFWat2fOHwYzK1qwv");

			dynamoDB = new AmazonDynamoDBClient(credentials);
			dynamoDB.setEndpoint("dynamodb.ap-southeast-1.amazonaws.com");
			mapper = new DynamoDBMapper(dynamoDB);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getUsers() {
		List<String> emails = new ArrayList<String>();

		ScanRequest scanRequest = new ScanRequest()
		.withTableName("MailUser")
		.withAttributesToGet("email");

		ScanResult result = dynamoDB.scan(scanRequest);

		for (Map<String, AttributeValue> item : result.getItems()){
			for(Entry<String,AttributeValue> entry: item.entrySet()){
				emails.add(entry.getValue().getS());
			}
		}
		return emails;
	}

	public static CampaignUser getCampaignUser(String email) {

		if(email == null) 
			return null;
		System.out.println(email);
		CampaignUser campaignUser = new CampaignUser();
		campaignUser.setEmail(email);

		System.out.println(campaignUser.getEmail());
		campaignUser = mapper.load(campaignUser);

		return campaignUser;
	}

}
