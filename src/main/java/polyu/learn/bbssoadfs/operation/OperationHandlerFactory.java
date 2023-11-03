package polyu.learn.bbssoadfs.operation;


import polyu.learn.bbssoadfs.operation.cadn.CADNChangePersonConfigProvider;
import polyu.learn.bbssoadfs.operation.cadn.CADNUpdatePersonConfigProvider;
import polyu.learn.bbssoadfs.operation.cadn.CADNUpdateSecInstRoleConfigProvider;
import polyu.learn.bbssoadfs.operation.sis.SISFileEndpointOperationHandler;
import polyu.learn.bbssoadfs.operation.sis.SISIntgrLogFactory;
import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSInsertUserConfigProvider;

public class OperationHandlerFactory {
	
	
	public static OperationHandler getOperationHandler(String operation) {
		SISIntgrLogFactory.getInstance().setOperation(operation);
		OperationType operationType = OperationType.valueOf(operation);
		switch (operationType) {
		case InsertSSOADFS:// Insert SSO_ADFS data
			return new SISFileEndpointOperationHandler(new ADFSInsertUserConfigProvider());
		case PostSSOADFS://Post SSO_ADFS data to SIS
			return new SISFileEndpointOperationHandler(new CADNUpdatePersonConfigProvider());
		case PostSSOADFSSecInstRole://Post SSO_ADFS data to SIS 
			return new SISFileEndpointOperationHandler(new CADNUpdateSecInstRoleConfigProvider());
		case UpdateSSOADFSLOG://Update SSO_ADFS data to SIS
			return new SISFileEndpointOperationHandler(new CADNChangePersonConfigProvider());
		default:
			throw new IllegalArgumentException("Could not find operation type :" + operationType);
		}
	}
	
	
	
}
