package polyu.learn.bbssoadfs.operation.sis;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;
import polyu.learn.bbssoadfs.util.ConfigUtils;

public class SISClientFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(SISClientFactory.class);

	public static SISClient getSISClient(SISClientContext sisClientContext, Callbackable sisClientCallback)
			throws SISException {
		Properties prop = null;
		try {
			prop = ConfigUtils.getPropertiesByName("config");
		} catch (IOException ex) {
			throw new SISException("Could not get config properties file", ex);
		}
		String sisClient = prop.getProperty("sis.client");

		LOGGER.info("Using SIS Client: {}", sisClient);
		LOGGER.info("SISClientContext - \n{}", sisClientContext);
		SISIntgrLogFactory.getInstance().setSisClient(sisClient);

		if (SISClientType.HttpClient.name().equals(sisClient)) {
			return new HttpSISClient(sisClientContext, sisClientCallback);
		} else if (SISClientType.Curl.name().equals(sisClient)) {
			return new CurlSISClient(sisClientContext, sisClientCallback);
		}
		throw new SISException("Could not find SIS Client Type: " + sisClient);
	}

}
