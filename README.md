# BbSSOADFS

This is a of open source code (Java) that demonstrate how Blackboard(Bb) integrated with Single Sign-On (SSO) and Active Directory Federation Service (ADFS).

========================================================================================================================

-- **********************************************************
-- ***Platform: Eclipse Jee Oxygen ***
-- Building Block/Java file Name: BbSSOADFS
-- Project Name: BbSSOADFS
-- Changed Number: 2
-- Modified Date: 2018-05-18
-- Remarks: 
--          1. Import the data from feed file(.CSV) which locates in folder “~/BbSSOADFS/feedfile” into MSSQL database and then write logs into the log table of MSSQL database and move the feed file into folder       
            “~/feedfile/completed” and rename it.
--          2. By executing the 4 main functions are manipulate these data from MSSQL database and get the delta data to download as CADNUpdatePerson.txt, CADNInsertSecInstRole.txt and CADNChangePerson.txt respectively 
               and save them in folder “~/feedfile/processing” with using the CURL to execute the action (INSERT/UPDATE/DELETE) for LMS Blackboard Learn SaaS.
--          3. Write logs into the table of MSSQL database and move the feed file to folder “~/feedfile/completed” and rename it as backup files.
--          4. Last but not least, notify the administrators of LMS Blackboard Learn SaaS by sending Email when there are having action (Add/Update/Delete) triggered by scheduled job in a specific server and each of the 
               action executed must be written into the log table.
--          5. Export as Jar file to be run in Task Scheduler.

-- **********************************************************

========================================================================================================================
