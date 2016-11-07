TestCase ID: TC000 <br>
Title : Loading Sample test data <br>
Description : Tester should be able to load the sample data successfully <br>
Assumption : Tester has yet to open the app once <br>

Steps no. | Steps | Test Data |  ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | create a folder call 'taskForceTest' on desktop |  |a folder called 'taskForceTest' should be created
2  | download the v0.5 jar file and place it inside 'taskForceTest' folder | 	[jar file](https://github.com/CS2103AUG2016-F10-C2/main/releases) |the folder should contain the downloaded jar file
3 | open the jar file | |TaskForce window should appear. config, perferences and log file should be generated in the same folder
4 | close the app | |TaskForce app should be closed
5 | Transfer the forDemoUse.xml in  'ManualTesting' folder and overwrite the forDemoUse file in 'taskForceTest' | |the forDemoUse.xml in 'taskForceTest' should be overwritten by the one in 'ManualTeting folder'
6 | open the app again | | TaskForce should open with some tasks shown on the list

TestCase ID: TC001 <br>
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
Assumption : Tester is testing on 11-11-2016. If not, the event and deadline added will not be reflected in the list because it defaulted to showing deadline and event that start/end today.

Steps no. | Steps | Test Data |  ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Go to the 'Enter Command Here..' box | | User should be able to type anything here
2 | Write/ Paste the Test Data into the box And press enter|  add testing a program now d/testing taskforce t/test | The added task should be added and highlighted under 'Reminders & Deadline'
3 | Repeat step 2 but with a different set of test data |  add this is a deadline et/11-11-2016 1000 | the added event should be added and highlighted under 'Reminders & Deadline'
4 | Repeat step 2 one more time but adding a Event this time | add test cs2103 product st/11-11-2016 1000 et/11-11-2016 1100 | the added event should be added and highlighted under "Event"

TestCase ID: TC003 <br>
Title : The Flexibility of adding a task,event ,deadline , checking for conflict<br>
Description : Tester should be able to add a task without following a rigid format and natural date language and getting informed of conflict event <br>
Assumption : TC002 has been tested and Tester are familiar with the format of adding atask <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Add a floating task using the test data | add very flexible t/flexible d/testing the flexibility t/test | the task should be added and highlighted under 'Reminders & Deadline'.
2 | Add a deadline | add cs2103 report d/very important et/today 10pm t/schoolwork | the dealdine should be added and highlighted under 'Reminders & Deadline'
3 | Add a event | add dinner with professor et/today 11pm d/learn from prof st/today 9pm t/dinner | the event should be added and highlighted under 'Reminders & Deadline'.
3 | Add another event | schedule Gym session et/9pm d/gotta get my money's worth! st/today 7pm t/getMoving | the event should be added and highlighted under 'Reminders & Deadline'. Furthermore, the end date should be on friday even though the user did not provide a date.
There should be a conflict saying the event clashes with cs2102 lab as well

TestCase ID : TC004 <br>
Title : Searching for task <br>
Description : Tester should be able to search task using keywords. <br>
Format : <br>
`find name/KEYWORDS [mark/true]` <br> 
`find desc/KEYWORDS [mark/true]` <br>
`find tag/KEYWORDS [mark/true]` <br>
`find day/WHOLE_NUMBER [mark/true]` <br>
`find week/WHOLE_NUMBER [mark/true]` <br>
`find type/all` <br>
`find type/overdue` <br>
`find type/mark` <br>


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
2 | Take note of the index 1 task and delete that task using delete command | delete 1 | the task should be gone from the list And System should print out the name of the task that is deleted 
3 | Delete the mutiple tasks found in the list | delete 1,3,2,6 | the 4 tasks should be deleted and dissapear from the list and System should print out all the name of the tasks that are deleted

TestCase ID : TC006 <br>
Title : Blocking event < br>
Description : Testing should be able to block mutiple timeslot for a uncomfirmed event <br>
Format :
`block EVENT_NAME st/DATES et/DATES st/DATES et/DATES...` <br>


Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | default the list to only show taskname that contain block | find name/block | the list shown should be empty because no task has block in its' name
2 | enter the block command | block blockMeeting st/today 5pm et/today 6pm st/tomorrow 3pm et/tomorrow 5pm st/today 9pm et/today 11pm | The list should now show three event that is tagged with a white circle


TestCase ID : TC007 <br>
Title : Confirm a block Event < br>
Description : Tester should be able to block mutiple timing for a uncomfirmed event 
Format :
`block EVENT_NAME st/DATES et/DATES st/DATES et/DATES...` <br>
Precondition : TC006 must be completed first <br>
Assumption : The list is still under the effect of 'find name/block' so it should contain only 3 blocked event <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | comfirm the first event aand change the timing | confirm 1 st/today 6:30pm et/7:30pm | the list should only show one event now. And the color of the circle is changed from white to yellow. The remaining block are released/deleted automatically

TestCase ID : TC008 <br>
Title : Recur a Event and Deadline <br>
Description : Tester should be able to add/recurr a deadline and event with just one command <br>
Format : <br>
`add DEADLINE_NAME et/MM-DD-YYYY HHMM [d/DESCRIPTION] [t/TAG...t/TAG...] [recur/TYPE] [r/TIME]` <br>
`add EVENT_NAME st/MM-DD-YYYY HHMM et/MM-DD-YYYY HHMM [d/DESCRIPTION] [t/TAG...t/TAG...] [recur/TYPE] [r/TIME]` <br>
Recur TYPE Format:
> * daily
> * weekly
> * monthly
> * yearly
> * alternate day
> * fortnightly
> * biweekly
> * alternate month
> * bimonthly
> * alternate year
> * biyearly <br>

Precondition : TC002, TC003, TC004 haven been completed
Assumption : Tester are already familiar with add, find command format

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Default the list to show task with recur in its' name | find name/recur | the list should be empty as no task should have recur in its' name
2 | recurr a deadline using the test data | add recurDeadline et/today 6pm recur/weekly r/4 | 4 deadline should be added to the list, each deadline is 1 week after the previous one
3 | recur a event using the test data | add recurEvent st/today 2pm et/today 4pm recur/daily r/3 | 3 event should be shown, event that start today, tomorrow and the next day.


TestCase ID : TC009 <br>
Title : Editing a task <br>
Description : Tester should be able to edit a task succesffully <br>
Precondition : TC007 is completed <br>
Assumption : The list is still under the effect of 'find name/recur' the list show only show tasks with 'recur' in its name <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | edit the first task and change it name | edit 1 testing recurring deadline | the edited task should be highlighted reflecting the new changes
2 | edit the deadline and change it to a event | edit 2 changeToRecurrEvent st/today 6pm et/today 8 pm | the task should move to the Event column and highligted to reflect the new changes 
3 | edit the event and postpone it to another day | postpone 6 st/today 6pm et/today 8pm | the event with changes timing should be highlighted reflecting the new changes 

TestCase ID : TC010 <br>
Title : Undo/Redo a action <br> 
Description : Tester should be able to undo redo a actions related to add, delete and block command <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | default the list to only task with undo in it name | find name/undo | the list should be empty as no task contain undo its' name
2 | add a task containing undo in it name | add task to undo | the list should now have the added task highlighted
3 | undo the add action | undo | the added task should dissapear, making the list empty again
4 | redo the previous action which will be undoing 'undo add action' | redo | the added task should appear and highlighted again.

TestCase ID : TC011 <br>
Title : Checking free time <br>
Description : Tester should be able to check for freetime for the day <br>
Format : <br>
`freetime` will show the default free time for day <br>
`freetime day/WHOLE_NUMBER` <br>

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | type freetime command to search for today freetime | freetime | the list should be updated to show deadline and event that start/due today. a time bar should appeared on the result message panel showing your free time according to the events reflected in the list.
2 | type freetime command to search for tomorrow free time | freetime day/1 | the list should be updated to show deadline and event that start/due tomorrow . a time bar should appeared on the result message panel showing your free time according to the events reflected in the list.

TestCase ID : TC012 <br>
Title : Changing/Check save data location <br>
Description : Tester should be able to check or change the save data location <br>
Format : <br>
`cd` will tell you the current save data location <br>
`cd C:\Users\Boon\newSaveName.xml` will change the location on a window platform
`cd ./path/to/new/location/on/unix/platform/sampleData.xml` will change the location on a unix platform <br>
Description : Tester should be able to check and change the location of the save data location. <br>
Assumption : Tester should be able to provide a valid file path, not doing so will result in a invalid file path message
Tester should provide a file path that do not require system permission (e.g saving inside a C drive C:\newfile.xml )

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | check for the current location of sava data using cd command | cd | the system will display the location of the current save data
2 | change the test data to a new location | refer to format under TC012 | The system should display the location of the new sav data. the new data should be created physically on the new location as well

TestCase ID :TC012 <br>
Title : Modying config file <br>
Description : Tester should be able modify the config file using config command <br>
Format : <br>
`config CONFG_OPTION v/NEW_VALUE`
Config optiop : 
userPrefsFilePath  : Value
activeHoursFrom : 0000 to 2359
activeHoursTo : 0000 to 2359
enableSudo : true or false

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | Change the activeHoursTo to 10pm using config command | config activeHoursTo v/2200 | the system should show a message saying activeHoursTo is successfully set to 2200
2 | Change the activeHoursFrom to 8am using config command | config activeHoursTo v/0800 | the system should show a message saying activeHoursFrom is successfully set to 0800
3 | disable the sudo mode using config command | config enableSudo v/false | the system should show a message saying enableSudo is set to false

TestCase ID :TC013 <br>
Title : Clear the entire save data <br>
Description : Tester should be able to clear the entire save data completely 
Precondition : TC012 completed
Assumption : Tester did not set enableSudo to true during TC012

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | default the list to show everything in the save data | find type/all | the list should show everything stored in the save data
2 | try to clear the command using clear command | clear | the system should show a error message saying SUDO mode needs to be required
3 | enable sudo mode using Config Command | config enableSudo v/true | the system should show enablesudo is successfuly set to true
4 | try to clear the command again usign clear command | clear | a confirmation dialog should pop up now telling user the operation is undoable
5 | press ok to the confirmation dialog | | The list should be empty now. System should show a message saying taskforce data has been cleared

TestCase ID : TC014 <br>
Title : Exit the TaskForce Program <br >
Description : Tester should be able to exit the program successfully

Steps no. | Steps | Test Data | ExpectedResult
--- | :---------------- | :---------------- | :----------------
1 | exit the program using exit command | exit | the taskforce should close automatically


