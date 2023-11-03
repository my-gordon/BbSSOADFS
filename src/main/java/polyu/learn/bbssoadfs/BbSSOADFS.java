package polyu.learn.bbssoadfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.OperationHandlerContext;
import polyu.learn.bbssoadfs.util.LoggerUtils;

public class BbSSOADFS {

	private static final Logger LOGGER = LoggerFactory.getLogger(BbSSOADFS.class);

	public static void main(String[] args) {

		LoggerUtils.configLog4j();

		if (args.length == 0) {
			LOGGER.error("Please input an operation.");
			return;
		}

		String operationInsert = args[0];
		String operationPost = args[1];
		String operationPostSecInstRole = args[2];
		String operationUpdate = args[3];
		
		LOGGER.info("====================================Operation {} started====================================",
				operationInsert);
		try {
			(new OperationHandlerContext(operationInsert)).run();
		} catch (Exception ex) {
			LOGGER.error("Error occurred when performing {} operation", operationInsert, ex);
		}

		LOGGER.info("====================================Operation {} ended====================================",
				operationInsert);

		LOGGER.info("====================================Operation {} started====================================",
				operationPost);
		try {
			(new OperationHandlerContext(operationPost)).check();
		} catch (Exception ex) {
			LOGGER.error("Error occurred when posting {} operation to SIS", operationPost, ex);
		}

		LOGGER.info("====================================Operation {} ended====================================",
				operationPost);
	
		LOGGER.info("====================================Operation {} started====================================",
				operationPostSecInstRole);
		try {
			(new OperationHandlerContext(operationPostSecInstRole)).check();
		} catch (Exception ex) {
			LOGGER.error("Error occurred when posting {} operation to SIS", operationPostSecInstRole, ex);
		}

		LOGGER.info("====================================Operation {} ended====================================",
				operationPostSecInstRole);
		
		
		LOGGER.info("====================================Operation {} started====================================",
				operationUpdate);
		try {
			(new OperationHandlerContext(operationUpdate)).update();
		} catch (Exception ex) {
			LOGGER.error("Error occurred when updating {} operation to SIS", operationUpdate, ex);
		}

		LOGGER.info("====================================Operation {} ended====================================",
				operationUpdate);
	}

}
