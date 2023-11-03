package polyu.learn.bbssoadfs.operation.cadn;

import polyu.learn.bbssoadfs.operation.sis.annotation.SISColumn;
import polyu.learn.bbssoadfs.operation.sis.annotation.SISWrapper;

@SISWrapper
public class CADNSISChangeWrapper {
	private ADFSLogDelete adfslogdelete;

	private final String AVAILABLE_IND = "N";
	private final String POLYU_DSK = "PolyU_ADFS";
	private final String NEW_POLYU_DSK = "PolyU_ADFS_deleted";
	
	
	public CADNSISChangeWrapper(ADFSLogDelete adfslogdelete) {
		this.adfslogdelete = adfslogdelete;
	}

	@SISColumn(name = "EXTERNAL_PERSON_KEY")
	public String getExternalPersonKey() {
		return adfslogdelete.getNetId();
	}

	@SISColumn(name = "USER_ID")
	public String getUserId() {
		return adfslogdelete.getNetId();
	}

	@SISColumn(name = "AVAILABLE_IND")
	public String getAvailableInd() {
		return AVAILABLE_IND;
	}

	@SISColumn(name = "DATA_SOURCE_KEY")
	public String getDataSourceKey() {
		return POLYU_DSK;
	}

	@SISColumn(name = "NEW_DATA_SOURCE_KEY")
	public String getNewDataSourceKey() {
		return NEW_POLYU_DSK;
	}
	

	public String toString() {
		return adfslogdelete.toString();
	}

}
