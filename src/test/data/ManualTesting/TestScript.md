Testcase ID: TC000 <br>
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

Testcase ID: TC001 <br>
Title : Opening the help menu <br>
Description : Tester should be able to open the help menu <br>
Precondition : TaskForce app is opened 


Steps no. | Steps | Test Data |  ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Go to the 'Enter Command Here..' box | | User should be able to type anything here
2 | Enter the help command in the box and press enter | help | a Help window should pop up
3 | close the help window by pressing esc | | The help window should close
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

TestCase ID: TC003 <br>
Title : The Flexibility of adding a task,event ,deadline <br>
Description : Tester should be able to add a task without following a rigid format and natural date language <br>
Assumption : TC002 has been tested and Tester are familiar with the format of adding atask <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Add a floating task using the test data | add very flexible t/flexible d/testing the flexibility t/test | the task should be added and highlighted under 'Reminders & Deadline'.
2 | Add a deadline | add cs2103 report d/very important et/today 10pm t/schoolwork | the dealdine should be added and highlighted under 'Reminders & Deadline'
3 | Add a event | add dinner with professor et/today 11pm d/learn from prof st/today 9pm t/dinner | the event should be added and highlighted under 'Reminders & Deadline'.

TestCase ID : TC004 <br>
Title : Searching for task <br>
Format : <br>
`find name/KEYWORDS [mark/true]` <br> 
`find desc/KEYWORDS [mark/true]` <br>
`find tag/KEYWORDS [mark/true]` <br>
`find day/WHOLE_NUMBER [mark/true]` <br>
`find week/WHOLE_NUMBER [mark/true]` <br>
`find type/all` <br>
`find type/overdue` <br>
`find type/mark` <br>
Description : Tester should be able to search task using keywords. <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Enter the find by name command | find name/cs | the list should be updated with all task that contain CS in the name 
2 | Enter the find by name command including marked task | find name/cs mark/true | the list should be updated with all task that contain CS in the name and task that are crossed out with CS in their name
3 | Enter the find by desc command | find desc/presentation | the list should only show tasks with 'presentation in the description
4 | Enter the find by tag command | find tag/important | the list should only show tasks that has a 'important' tagged to it
5 | Enter find by day command | find day/0 | the list should show all deadline that end today, event that start/end or is ongoing this week and all reminders
6 | Enter find by week command | find week/0 | the list should show all deadline that end this week, event that start/end or is ongoing today and all reminders
7 | Enter find by type/all command | find type/all | the list should show everything in the save data, the last index of event should be at least 9x
8 | Enter find by type/overdue command | find type/overdue | the list should deadline that are tagged with a red circle which mean they are overdue
9 | Enter find by type/mark command | find type/mark | the list should show only tasks that are crossed out/ marked down

TestCase ID : TC005 <br>
Title : Deleting (a) tasks <br>
Format: <br>
`delete index [,index2, index3...]` <br> 
Description : Tester should be able to delete away task
Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Default the list to show everything using find command | find type/all | the list should show everything stored in the save data
2 | Delete the first task that is index 1 | delete 1 | the task should be gone from the list
3 | Delete the mutiple tasks found in the list | delete 1,3,2,6 | the 4 tasks should be deleted and dissapear from the list
