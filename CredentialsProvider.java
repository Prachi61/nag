package aws;

import java.io.IOException;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.auth.AWSCredentials;

public class CredentialsProvider {

	public static volatile AWSCredentials credentials = null;

	public static AWSCredentials getCredentails(String accessKey, String secretKey) throws IOException {

		if(credentials == null) 
			credentials = new BasicAWSCredentials(accessKey, secretKey);

		return credentials;
	}
}
