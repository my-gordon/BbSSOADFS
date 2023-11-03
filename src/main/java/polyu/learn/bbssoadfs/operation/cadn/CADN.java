package polyu.learn.bbssoadfs.operation.cadn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_CADN")
public class CADN {
	@Id
	@Column(name="pkid")
	private Long pkId;
	
	@Column(name="net_id")
	private String netId;
	
	@Column(name="student_no")
	private String studentNo;

	@Column(name="staff_no")
	private String staffNo;
	
	@Column(name="prog_code")
	private String progCode;
	
	@Column(name="sub_code")
	private String subCode;
	
	@Column(name="study_mode")
	private String studyMode;
	
	@Column(name="student_study_year")
	private String studentStudyYear;
	
	@Column(name="dept")
	private String dept;
	
	@Column(name="puuserfullname")
	private String puUserFullName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="employeetype")
	private String employeeType;
	
	@Column(name="post_cat")
	private String postCat;
	
	@Column(name="status")
	private String status;
	
	@Column(name="date")
	private Date date;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getProgCode() {
		return progCode;
	}

	public void setProgCode(String progCode) {
		this.progCode = progCode;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getStudyMode() {
		return studyMode;
	}

	public void setStudyMode(String studyMode) {
		this.studyMode = studyMode;
	}

	public String getStudentStudyYear() {
		return studentStudyYear;
	}

	public void setStudentStudyYear(String studentStudyYear) {
		this.studentStudyYear = studentStudyYear;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPuUserFullName() {
		return puUserFullName;
	}

	public void setPuUserFullName(String puUserFullName) {
		this.puUserFullName = puUserFullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getPostCat() {
		return postCat;
	}

	public void setPostCat(String postCat) {
		this.postCat = postCat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString(){
		return "pkid:"+getPkId() + ", net_id:" + getNetId() + ", student_no:" + getStudentNo() + ", staff_no:" + getStaffNo()  + 
				", prog_code:" + getProgCode() + ", sub_code:" + getSubCode() + ", study_mode:" + getStudyMode() + 
				", student_study_year:" +  getStudentStudyYear() + ", dept:" + getDept() + ", puuserfullname:" + getPuUserFullName() +
				", email:" + getEmail() + ", employeetype:" + getEmployeeType() + ", post_cat:" + getPostCat() + ", status:" + getStatus() + 
				", date:" + getDate();
	}
	
}
