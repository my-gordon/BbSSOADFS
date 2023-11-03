package polyu.learn.bbssoadfs.operation.sis;

import java.io.File;
import java.util.List;

import polyu.learn.bbssoadfs.operation.cadn.ADFSLog;
import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSUser;

public interface SISFileEndpointConfigProvider {
	// get the feed file
	public abstract File getFeedFile() throws SISException;

	// get the sis client context
	public abstract SISClientContext getSISClientContext() throws SISException;

	// get the ADFS User file @2018-05-18
	public abstract List<ADFSUser> insertADFSUserFromCSVFeedFile() throws SISException;

	// delete the ADFS User @2018-05-18
	public abstract void deleteADFSUser() throws SISException;
	
	// retrieve ADFS User From Table @2018-05-18
	public abstract List<ADFSUser> retrieveADFSUserFromTable() throws SISException;
	
	// write to ADFS Log table @2018-05-18
	public abstract void writeOrUpdateADFSLog() throws SISException;
	
	// check and delete ADFS Log table @2018-05-18
    public abstract void CheckADFSLog() throws SISException;
		
}
