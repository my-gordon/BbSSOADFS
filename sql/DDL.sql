CREATE TABLE SIS_INTGR_LOG (
	LOG_ID INT NOT NULL PRIMARY KEY,
	LOG_DATETIME DATETIME,
	OPERATION NVARCHAR(MAX),
	STATUS NVARCHAR(MAX),
	SIS_CLIENT NVARCHAR(MAX),
	FEEDFILE_NAME NVARCHAR(MAX),
	FEEDFILE_NUM_OF_LINE INT,
	REF_CODE NVARCHAR(MAX),
	HTTP_RESPONSE NVARCHAR(MAX),
	ERROR_MSG NVARCHAR(MAX),
	DATASETSTATUS_RAW NVARCHAR(MAX),
	DATASETSTATUS_COMPLETED_COUNT INT,
	DATASETSTATUS_DATA_INTEGRATION_ID NVARCHAR(MAX),
	DATASETSTATUS_DATA_SET_UID NVARCHAR(MAX),
	DATASETSTATUS_ERROR_COUNT INT,
	DATASETSTATUS_LAST_ENTRY_DATE NVARCHAR(MAX),
	DATASETSTATUS_QUEUED_COUNT INT,
	DATASETSTATUS_START_DATE NVARCHAR(MAX),
	DATASETSTATUS_WARNING_COUNT INT
);

CREATE VIEW SIS_INSERT_COURSE (COURSE_ID,EXTERNAL_COURSE_KEY,COURSE_NAME,AVAILABLE_IND,DATA_SOURCE_KEY,TEMPLATE_COURSE_KEY, TERM_KEY)
AS SELECT course_id, course_id, course_name, 'Y', 'SAO_POSS', 'template2015api', '' FROM SAO_POSS_COURSE

CREATE VIEW SIS_INSERT_MEMBERSHIP(EXTERNAL_COURSE_KEY,EXTERNAL_PERSON_KEY,ROLE,DATA_SOURCE_KEY)
AS 
SELECT     'EDC_Bb101', net_id, 'Student', 'University'
FROM         dbo.CADN AS C
WHERE     (employeetype = 'T') AND (NOT EXISTS
                          (SELECT     1
                            FROM          dbo.LMS_COURSE_USER_CLOUD
                            WHERE      (course_id = 'EDC_Bb101') AND (user_id = C.net_id)))
UNION ALL
SELECT     lms_cid, lower(student_no), 'Student', 'University'
FROM         LMS_STUDENT_NEED_ENROLL_OLTUT  WHERE  (NOT EXISTS
                          (SELECT     1
                            FROM          dbo.LMS_COURSE_USER_CLOUD
                            WHERE      (course_id = lms_cid) AND (user_id = lower(student_no))))
UNION ALL
SELECT course_id, lower(user_id), CASE course_role WHEN '0' THEN 'Student' ELSE course_role END, 'SAO_POSS_E' FROM SAO_POSS_USER AS S WHERE  (NOT EXISTS
                          (SELECT     1
                            FROM          dbo.LMS_COURSE_USER_CLOUD
                            WHERE      (course_id = S.course_id) AND (user_id = lower(S.user_id))))

                            
                            
                            
CREATE VIEW SIS_INSERT_ORGANIZATION_MEMBERSHIP(EXTERNAL_ORGANIZATION_KEY,EXTERNAL_PERSON_KEY,ROLE,DATA_SOURCE_KEY)
AS                            
SELECT     'EDC008', net_id, 'Student', 'University'
FROM         dbo.CADN AS C
WHERE     (employeetype = 'T') AND (NOT EXISTS
                          (SELECT     user_id
                            FROM          dbo.LMS_COURSE_USER_CLOUD
                            WHERE      (course_id = 'EDC008') AND (C.net_id = user_id)))

                            


CREATE TABLE SIS_TTS_INSERT_MEMBERSHIP (
	EXTERNAL_COURSE_KEY NVARCHAR(MAX),
	EXTERNAL_PERSON_KEY NVARCHAR(MAX),
	ROLE NVARCHAR(MAX)
);


CREATE TABLE SIS_TTS_UPDATE_MEMBERSHIP (
	EXTERNAL_COURSE_KEY NVARCHAR(MAX),
	EXTERNAL_PERSON_KEY NVARCHAR(MAX),
	AVAILABLE_IND NVARCHAR(MAX),
	DATA_SOURCE_KEY NVARCHAR(MAX)
);
