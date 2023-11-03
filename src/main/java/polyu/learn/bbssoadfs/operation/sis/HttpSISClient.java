package polyu.learn.bbssoadfs.operation.sis;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;

public class HttpSISClient extends SISClient {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final String NEW_LINE = System.lineSeparator();

	public HttpSISClient(SISClientContext sisClientContext,
			Callbackable sisClientCallback) {
		super(sisClientContext, sisClientCallback);
	}

	@Override
	public void query() throws SISException {
		SISClientContext sisClientContext = getSISClientContext();
		String endpoint = sisClientContext.getEndpoint();
		String userName = sisClientContext.getUsername();
		String password = sisClientContext.getPassword();
		File feedFile = sisClientContext.getFeedFile();

		try {
			long startTime = System.currentTimeMillis();
			HttpURLConnection connection = (HttpURLConnection) new URL(endpoint)
					.openConnection();
			connection.setDoOutput(true);
			String basicAuthString = new String(userName + ":" + password);
			String encoded = Base64.encodeBase64String(basicAuthString
					.getBytes());
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			connection.setRequestProperty("Content-Type", "text/plain");

			if (feedFile != null) {
				DataOutputStream request = new DataOutputStream(
						connection.getOutputStream());
				String feedFileStr = FileUtils.readFileToString(feedFile);
				request.writeBytes(feedFileStr);
				request.flush();
				request.close();
			}

			InputStream responseStream = new BufferedInputStream(
					connection.getInputStream());

			BufferedReader br = new BufferedReader(
					new InputStreamReader(responseStream));
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append(NEW_LINE);
			}
			br.close();

			String response = stringBuilder.toString();
			responseStream.close();
			int responseCode = connection.getResponseCode();
			connection.disconnect();
			long elapsedTime = System.currentTimeMillis() - startTime;
			LOGGER.info("SISClient Time Elapsed: {}ms", elapsedTime);
			LOGGER.info("Http Response Code: {}", responseCode);

			if (responseCode != 200) {
				throw new SISException("The http response code is NOT 200.");
			}

			this.getSISClientCallback().callback(response);
		} catch (Exception ex) {
			throw new SISException("Could not query SIS endpoint: " + endpoint,
					ex);
		}

	}

}
