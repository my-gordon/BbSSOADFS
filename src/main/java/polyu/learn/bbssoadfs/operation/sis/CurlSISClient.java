package polyu.learn.bbssoadfs.operation.sis;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;

public class CurlSISClient extends SISClient {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final String NEW_LINE = System.lineSeparator();

	public CurlSISClient(SISClientContext sisClientContext,
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
		try{
			String cmd = "";
			
			cmd = String.format("curl -s -k -H \"Content-Type:text/plain\" -u %s:%s %s", userName, password, endpoint);
			
			if(feedFile != null){
				cmd = String.format("curl -s -k -H \"Content-Type:text/plain\" -u %s:%s --data-binary @%s %s", userName, password, feedFile.getCanonicalPath(), endpoint);
			}
			
			LOGGER.info("Running curl cmd: {}", cmd);
			
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c","cd \"curl\" && " + cmd);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			
			StringBuilder strResponse = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				strResponse.append(line).append(NEW_LINE);
			}
			br.close();
			this.getSISClientCallback().callback(strResponse.toString());
			
		}catch(Exception ex){
			throw new SISException("Error querying the endpoint: " + endpoint,
					ex);
		}

	}



}
