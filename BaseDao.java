package dao;

import play.Play;
//import aws.CredentialsProvider;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class BaseDao {
	public static AmazonDynamoDBClient dynamoDB = null;
	public static DynamoDBMapper mapper = null;

	static {
		try {
//			String secretKey = Play.application().configuration().getString("aws.secretKey");
//			String accessKey = Play.application().configuration().getString("aws.accessKey");

			String secretKey = "aB8pHXXTuUF6ERcfNW18EgGBFWat2fOHwYzK1qwv";
			String accessKey = "AKIAJORB43IQ4JYAZMUQ";

			//			AWSCredentials credentials = CredentialsProvider.getCredentails(accessKey, secretKey);
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			dynamoDB = new AmazonDynamoDBClient(credentials);
			dynamoDB.setEndpoint("dynamodb.ap-southeast-1.amazonaws.com");
			mapper = new DynamoDBMapper(dynamoDB);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
