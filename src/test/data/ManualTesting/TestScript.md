Testcase ID: TC001 <br>
Title : Loading Sample test data <br>
Description : Tester should be able to load the sample data successfully <br>
Assumption : Tester has yet to open the app once <br>

Steps no. | Steps | ExpectedResult
--- | :---------------- | :----------------
1 | create a folder call 'taskForceTest' on desktop | a folder called 'taskForceTest' should be created
2  | download the jar file and place it inside 'taskForceTest' folder | the folder should contain the downloaded jar file
3 | open the jar file | TaskForce window should appear. config, perferences and log file should be generated in the same folder
4 | close the app | TaskForce app should be closed
5 | Transfer the forDemoUse.xml in  'ManualTesting' folder and overwrite the forDemoUse file in 'taskForceTest' | the forDemoUse.xml in 'taskForceTest' should be overwritten by the one in 'ManualTeting folder'
6 | open the app again | TaskForce should open with some tasks shown on the list


TestCase ID: TC002 <br>
Title : Adding a task, deadline, event <br>
Description : Tester should be able to add a task event and deadline today <br>
Format : <br>
`add TASKNAME [d/DESCRIPTION] [t/TAG...t/TAG...]` <br> 
`add DEADLINE_NAME et/MM-DD-YYYY HHMM [d/DESCRIPTION] [t/TAG...t/TAG...]` <br>
`add EVENT_NAME st/MM-DD-YYYY HHMM et/MM-DD-YYYY HHMM [d/DESCRIPTION] [t/TAG...t/TAG...]` <br>
Pre-condition : TaskForce Must be opened <br>

Steps no. | Steps | Test Data |  ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Go to the 'Enter Command Here..' box | | User should be able to type anything here
2 | Write/ Paste the Test Data into the box And press enter|  add testing a program now d/testing taskforce t/test | The added task should be added and highlighted under 'Reminders & Deadline'
3 | Repeat step 2 but with a different set of test data |  add this is a deadline et/11-11-2016 1000 | the added event should be added and highlighted under 'Reminders & Deadline'
4 | Repeat step 2 one more time but adding a Event this time | add test cs2103 product st/11-11-2016 1000 et/11-11-2016 1100 | the added event should be added and highlighted under "Event"
