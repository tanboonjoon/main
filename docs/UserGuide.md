# User Guide

* [Overview](#overview)
* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

## Overview

1. TaskForce allows you to manage his tasks through a simple
command-line interface (CLI)-based application.
2. It allows for 3 main kinds of tasks:  
   * Reminders - a task with no start nor end date  
   * Deadline - a task that ends at a specific time  
   * Event - an event has both a start and end time
3. You can also block out time from your calendar through this app, through the
implementation of blocks - events with no name (placeholders).
4. This app is built on Java, and runs on any Desktop.

## Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
   > Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.

1. Download the latest `taskforce.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for your TaskForce app.
3. Double-click the file to start the app. The GUI should appear in a few seconds.
   > <img src="images/taskforce_gui_3.PNG" width="600">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`add`**` wash the toilet ` adds a reminder to wash the toilet to the task list.
   * **`search`**` d/0` searches the task list for all tasks happening today.
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>


## Features

> **Command Format**
> * Words in `UPPER_CASE` are the parameters.
> * Items in `[SQUARE_BRACKETS]` are optional.
> * Items with `...` after them can have multiple instances.
> * The order of parameters are not fixed.

#### Viewing help : `help`
Displays information on how to use commands.  
Format: `help [COMMAND]`

> - If a `COMMAND` is given, help is displayed for that command only.  
> - If no `COMMAND` is given, help is displayed for all commands available.   
> - Help is not shown if you enter an incorrect command e.g. `help abcd`

#### Adding a task: `add`
Adds a task to the task list.  
Format:  
Reminder: `add TASKNAME  [d/DESCRIPTION] [t/TAG]...`  
Deadline: `add TASKNAME  [d/DESCRIPTION] [et/END_DATE] [t/TAG]...`  
Event: `add TASKNAME  [d/DESCRIPTION]  [st/START_DATE] [et/END_DATE] [t/TAG]...`  

> - Tasks can have any number of tags (including 0)  
> - Date format is MM-DD-YYYY HHMM (24 hour Format) e.g. `st/ 10-22-2016 1500`
> 	- The command also supports natural language dates such as `today 6pm`
> 	- See the section [On Entering Dates](#On-Entering-Dates) for more details
> - If no time is specified, the time will be assumed to be the time right now.
> - If no start date is specified, it is assumed to be today.
> - If start date/time is specified but end date/time is not specified, the end date/time will be the same day on 2359.

Examples:
* `add housework d/to get pocket money t/important`<br>
* `report d/school report et/130116 2200 t/important`<br>
  Add the task into the ToDoList using `add` command.

#### Blocking out time: `block`
Blocks out time for a potential event, or to indicate unavailability to others.  
This command can block multiple timeslots at once, all for one specific event.  
If multiple timeslots were blocked, when one timeslot is confirmed, all other timeslots are released (further explained in the <kbd>confirm</kbd> command)      

Format: `block NAME st/START_DATE et/END_DATE [st/START_DATE et/END_DATE]...`

> - Blocked out time is only blocked and cannot be tagged.  
> - Each st/ and et/ is a pair, and you can have unlimited pairs
> - Date format is MM-DD-YYYY HHMM (24 hour Format) e.g. `st/ 10-22-2016 1500`
> 	- The command also supports natural language dates such as `today 6pm`
> 	- See the section [On Entering Dates](#On-Entering-Dates) for more details
> - If no start time is specified, the time will be assumed to be the time right now.
> - If no start date is specified, it is assumed to be today.
> - If no end date/time is specified, the end date/time will be the same day on 2359.
> - You must have the `st/` & `et/` tag even if you use default for both date and time

Examples:
* `block meeting with boss st/1400 et/1600 st/tommorrow 1400 et/1600`
* `block study period st/1300 et/1800 st/tomorrow 2000 et/`

#### Confirming previously blocked time: `confirm`  
Confirms a blocked out time and converts it into an event  
Deletes all other blocked timeslots for the same event

Format: `confirm INDEX st/STARTTIME et/ENDTIME`
> - To use this function, you must first list the desired timelot, by either going to the right date to view it, or finding it through keywords
> - Following which, you can use this command the confirm the desired slot you would like.
> - All other times associated to the previously blocked out event will be released, even if they are not in the current view.

Examples:
* `confirm 5 st/6pm et/8pm`

#### Searching for (a) specific task(s): `find`
Finds tasks of a specific time, or whose names contain any of the given keywords.  
Format: `find METHOD/ KEYWORDS`
KEYWORDS for TYPE 'name/' 'desc/' 'tag/' is a word that is contain/part of a task name/description/tag
KEYWORDS for TYPE 'day' and 'week' is a integer number.

Method | Explanation | Example
-------- | :-------- | :---------
`day/` | List all events/deadline a number of days after today | `find day/ -1` (yesterday)
`week/` | List all events/deadline in a week, after current week | `find week/ 0` (current week)
`name/` | List all tasks with taskName containing the keywords | `find name/ shoes`
`tag/` | List all tasks with taskDescription containing the keywords | `find name/ shoes`
`desc/` | List all tasks with the tags of task containing the keywords | `find name/ shoes`


> * The search is not case sensitive. e.g `hans` will match `Hans`
> * The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
> * Only the name is searched.
> * Only full words will be matched e.g. `Han` will not match `Hans`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Hans` will match `Hans Bo`


#### Deleting a task : `delete`
Deletes the specified task from the task list. Irreversible.  
Format: `delete INDEX[, INDEX,...]`

> - Delete the task at the specified `INDEX`.
> - To delete more than one task, seperate the tasks indexes with commas.
> - The index refers to the index number shown in the most recent listing.<br>
> - The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `find all/ Meeting`<br>
  `delete 1`<br>
  Deletes the 1st task in the results of the `find` command.

#### Editing a task: `edit`  
Edits a task in the task list.  
Format: `edit INDEX [NAME] [d/DESCRIPTION] [st/START_DATE] [et/END_DATE] [t/TAGS]`   

> - Follows index format of delete - The index refers to the index number shown in the most recent listing.
> - Only enter in the details you want to edit. Details not specified in this command will not be changed.  
> - The index **must be a positive integer** 1, 2, 3, ...  
> - You can modify a reminder into a deadline/event by adding start & end dates:  
> 	 * `edit INDEX st/1700 et/1900`    
> - For tags, the edit command follows the following rules:
> 	- If the task does not have a tag specified in the edit command, the edit command shall add that tag to the task.
> 	- If the task does have a tag specified in the edit command, the edit command will remove that tag from the task.
> 	- If the task posesses some tags not specified in the edit command, they will be left unchanged by this command.

Examples:
* `edit 1 schoolwork d/change deadline et/220506 2200`
* `edit 4 dinner d/change location t/important`

#### Finding free time in a specific day: `freetime`  
Gives you all the free time blocks in a specific day
Format: `freetime [day/DAYS_FROM_TODAY]`  
> - By default, freetime gives you today's free time  
> - You can adjust days by using the day/ option  
> - For example, for yesterday's free time, `freetime day/-1`  
> - DAYS_FROM_TODAY **must be an integer**

#### Undo the previous command : `undo`
Undo the last command that was successfully executed. <br>
Format: `undo`

#### Changing FileStorage location : `cd`
Changing the saveData into another location <br>
Format: `cd FILEPATH\FILENAME.xml`
Examples:
* `cd C:\Users\Boon\Desktop\newName.xml`
* `cd C:\Users\Boon\newSaveName.xml`

#### Clearing all entries : `clear`
Clears **ALL** entries from the task list. This command **CANNOT** be undone! <br>
Format: `clear`  

#### Exiting the program : `exit`
Exits the program.<br>
Format: `exit`  

#### Saving the data
TaskForce saves data in the hard disk automatically after any command that changes the data.  
There is no need to save manually.

## On Entering Dates

TaskForce supports flexible date inputs and thus allows many natural variations of dates. The following are three broad categories of dates supported by TaskForce

### Formal Dates
> Format Dates follow the format MM-DD-YYYY HHMM <br>
> 	* 03-15-2016 1500

### Relaxed Dates
> Relaxed dates are dates that expressed months in words instead of numbers. If the year is not provided, it is assumed to be this year
> 	* Oct 20 2016
> 	* 16 Aug

### Relative dates
> Relative dates are the most natural variation of the three and supports inputs that is relative to today <br>
> 	* today 5pm
> 	* next thursday 3pm
> 	* tomorrow 9am

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with
       the file that contains the data of your previous TaskForce folder.

## Command Summary

Command | Format  
-------- | :--------
Add | `add EVENT [d/DESCRIPTION] [t/TAG] [st/START_DATE] [et/END_DATE] [t/TAG]...`
Block | `block NAME s/START_DATE e/END_DATE`
Confirm | `confirm NAME s/START_DATE e/END_DATE`
Clear | `clear`
Delete | `delete INDEX`
Edit | `edit INDEX [NAME] [s/START_DATE] [e/END_DATE] ...`
Freetime | `freetime [day/DAYS_FROM_TODAY]`
Find | `find METHOD/ KEYWORD [MORE_KEYWORDS_FOR_ALL_METHOD]`
cd   | `cd FILEPATH/FILENAME.xml`
Undo | `undo`
Help | `help`
Exit | `exit`
