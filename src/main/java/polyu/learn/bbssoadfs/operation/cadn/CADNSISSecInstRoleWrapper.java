package polyu.learn.bbssoadfs.operation.cadn;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import polyu.learn.bbssoadfs.operation.sis.annotation.SISColumn;
import polyu.learn.bbssoadfs.operation.sis.annotation.SISWrapper;

@SISWrapper
public class CADNSISSecInstRoleWrapper {
	private CADN cadn;

	private final String STUDENT_EMAIL_SUFFIX = "@connect.polyu.hk";
	private final String STAFF_EMAIL_SUFFIX = "@polyu.edu.hk";
	private final String PRIMARY_INSTITUTION_ROLE = "STUDENT";
	private final String SECONDARY_INSTITUION_ROLE = "STAFF";
	private final String AVAILABLE_IND = "Y";//New Added @2018-05-18
	private final String POLYU_DSK = "PolyU";//For Secondary Institution Role //New Added @2018-07-11
	private final String NEW_POLYU_DSK = "PolyU";//New Added @2018-05-18
	
	
	public CADNSISSecInstRoleWrapper(CADN cadn) {
		this.cadn = cadn;
	}

	@SISColumn(name = "EXTERNAL_PERSON_KEY")
	public String getExternalPersonKey() {
		return cadn.getNetId();
	}

	@SISColumn(name = "USER_ID")
	public String getUserId() {
		return cadn.getNetId();
	}

	// Alan's Logic on splitting names
	@SISColumn(name = "FIRSTNAME")
	public String getFirstName() {
		String firstName = "";
		String fullName = StringUtils.stripToEmpty(cadn.getPuUserFullName());
		String[] temp = fullName.split(" ");
		for (int x = 0; x < temp.length; x++) {
			if(temp[x].length() == 0){
				continue;
			}else if (temp[x].length() >= 2) {
				char ch = temp[x].charAt(1);
				if (!Character.isUpperCase(ch)) {
					firstName = firstName + " " + temp[x];
				}
			} else {
				char ch = temp[x].charAt(0);
				if (!Character.isUpperCase(ch)) {
					firstName = firstName + " " + temp[x];
				}
			}
		}
		if (firstName.isEmpty()) {
			firstName = getLastName();
		}
		return StringUtils.stripToEmpty(firstName);

	}

	// Alan's Logic on splitting names
	@SISColumn(name = "LASTNAME")
	public String getLastName() {
		String lastName = "";
		String fullName = StringUtils.stripToEmpty(cadn.getPuUserFullName());
		String[] temp = fullName.split(" ");
		for (int x = 0; x < temp.length; x++) {
			if(temp[x].length() == 0){
				continue;
			}else if (temp[x].length() >= 2) {
				char ch = temp[x].charAt(1);
				if (Character.isUpperCase(ch)) {
					lastName = lastName + " " + temp[x];
				}
			} else {
				char ch = temp[x].charAt(0);
				if (Character.isUpperCase(ch)) {
					lastName = lastName + " " + temp[x];
				}
			}
		}
		if (lastName.isEmpty()) {
			lastName = getFirstName();
		}
		return StringUtils.stripToEmpty(lastName);
	}
	
	@SISColumn(name = "PASSWD")
	public String getPassword() {
		return RandomStringUtils.random(8, true, true);
	}

	@SISColumn(name = "EMAIL")
	public String getEmail() {
		 String employeeType = cadn.getEmployeeType();
		 String netId = cadn.getNetId();
		 String email = "";
		 
		 if("O".equals(employeeType)){
			 email = cadn.getEmail();
		 }
		 else if("S".equals(employeeType)){
			 email = netId + STUDENT_EMAIL_SUFFIX;
		 }
		 else if("T".equals(employeeType) || "F".equals(employeeType)){
			 email = netId + STAFF_EMAIL_SUFFIX;
		 }
		 return email;
	}

	@SISColumn(name = "STUDENT_ID")
	public String getStudentId() {
		String employeeType = cadn.getEmployeeType();
		String netId = cadn.getNetId();
		String studentId = "S".equals(employeeType) || "O".equals(employeeType) ? netId : "";
		return studentId;

	}

	@SISColumn(name = "DEPARTMENT")
	public String getDepartment() {
		return cadn.getDept();
	}

	@SISColumn(name = "INSTITUTION_ROLE")
	public String getInstitutionRole() {
		return PRIMARY_INSTITUTION_ROLE;
	}
	
	//New Added @2018-05-18
	@SISColumn(name = "AVAILABLE_IND")
	public String getAvailableInd() {
		return AVAILABLE_IND;
	}

	@SISColumn(name = "DATA_SOURCE_KEY")
	public String getDataSourceKey() {
		return POLYU_DSK;
	}

	//New Added @2018-05-18
	@SISColumn(name = "NEW_DATA_SOURCE_KEY")
	public String getNewDataSourceKey() {
		return NEW_POLYU_DSK;
	}
	
	@SISColumn(name = "ROLE_ID")
	public String getRoleId() {
		return SECONDARY_INSTITUION_ROLE;
	}

	public String toString() {
		return cadn.toString();
	}

}
