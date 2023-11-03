package polyu.learn.bbssoadfs.operation.sis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;
import polyu.learn.bbssoadfs.operation.OperationHandler;

public abstract class SISEndpointOperationHandler  implements OperationHandler, Callbackable{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Override
	public void run() throws Exception {
		LOGGER.info("queering endpoint");
		queryEndpoint();
	}

	protected void queryEndpoint() throws SISException{
		SISClientContext sisClientContext = getSISClientContext();
		SISClient sisClient = SISClientFactory.getSISClient(sisClientContext, this);
		sisClient.query();
	}
	
	protected abstract SISClientContext getSISClientContext() throws SISException;

}
