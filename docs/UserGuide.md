# User Guide

* [Overview](#overview)
* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

## Overview

1. TaskForce allows a user to manage his tasks through a simple
command-line interface (CLI)-based application.
2. It allows for 3 main kinds of tasks:  
   * Reminders - a task with no start nor end date  
   * Deadline - a task that ends at a specific time  
   * Event - an event has both a start and end time
3. One can also block out time from his calendar through this app, through the
implementation of blocks - events with no name (placeholders).
4. This app is built on Java, and runs on any Desktop.

## Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
   > Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.

1. Download the latest `taskforce.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for your TaskForce app.
3. Double-click the file to start the app. The GUI should appear in a few seconds.
   > <img src="images/Ui.png" width="600">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`add`**` wash the toilet` adds a reminder to wash the toilet to the task list.
   * **`search`**` d\0` searches the task list for all tasks happening today.
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>


## Features

> **Command Format**
> * Words in `UPPER_CASE` are the parameters.
> * Items in `SQUARE_BRACKETS` are optional.
> * Items with `...` after them can have multiple instances.
> * The order of parameters is fixed.

#### Viewing help : `help`
Displays information on how to use commands.  
Format: `help [COMMAND]`

> If a `COMMAND` is given, help is displayed for that command only.  
> If no `COMMAND` is given, help is displayed for all commands available.   
> Help is not shown if you enter an incorrect command e.g. `abcd`

#### Adding a task: `add`
Adds a task to the task list.  
Format:  
Reminder: `add TASKNAME [t/TAG]...`  
Deadline: `add TASKNAME [e/END_DATE] [t/TAG]...`  
Event: `add TASKNAME [s/START_DATE] [e/END_DATE] [t/TAG]...`  

> Persons can have any number of tags (including 0)  
> Date format is [DDMMYY] [HHMM] (24 Hour format)  
> If no date is specified, it is taken as today/tomorrow by default (depending on whether
the time has passed at present today)  
> If no time is specified, it is taken as whole day (start 0000, end 2359) by default

Examples:
* `add shine shoes t/personal`
* `add john's graduation ceremony s/1900 e/2100`

#### Searching for (a) specific task(s): `search`
Finds persons whose names contain any of the given keywords.<br>
Format: `find METHOD DETAILS `

Method | Explanation | Example
-------- | :-------- | :---------
`d/` | List all events a number of days after today | `search d/ -1` (yesterday)
`w/` | List all events in a week, after current week | `search w/ 0` (current week)
`e/` | List all events with word appearing in name | `search e/ ceremony`
`d/` | List all deadlines with word appearing in name | `search d/ homework`
`r/` | List all reminders with word appearing in name | `search r/ shine`
`a/` | List all events with word appearing in name | `search a/ shoes`


> * The search is case sensitive. e.g `hans` will not match `Hans`
> * The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
> * Only the name is searched.
> * Only full words will be matched e.g. `Han` will not match `Hans`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Hans` will match `Hans Bo`

Examples:
* `list`<br>
  `delete 2`<br>
  Deletes the 2nd person in the address book.
* `find Betsy`<br>
  `delete 1`<br>
  Deletes the 1st person in the results of the `find` command.

#### Deleting a person : `delete`
Deletes the specified task from the task list. Irreversible.  
Format: `delete INDEX`

> Deletes the person at the specified `INDEX`.
  The index refers to the index number shown in the most recent listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

#### Editing a task: `edit`  
Edits a task in the task list.  
Format: `edit INDEX [NAME] [s/START_DATE] [e/END_DATE] [t/TAG]`   

> Follows index format of delete - The index refers to the index number shown in the most recent listing.  
> The index **must be a positive integer** 1, 2, 3, ...  

> You can modify a reminder into a deadline/event by adding start & end dates:  
> * `edit INDEX s/1700 e/1900`    

> You can modify an event into a deadline by using `edit INDEX s/` (leaving empty)  
> You can modify an event into a block by using `edit INDEX n/`  
> Basically, it allows you to morph events as long as they satisfy the structure

#### Clearing all entries : `clear`
Clears all entries from the task list.<br>
Format: `clear`  

#### Exiting the program : `exit`
Exits the program.<br>
Format: `exit`  

#### Saving the data
TaskForce saves data in the hard disk automatically after any command that changes the data.  
There is no need to save manually.

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with
       the file that contains the data of your previous TaskForce folder.

## Command Summary

Command | Format  
-------- | :--------
Add | `add NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]...`
Clear | `clear`
Delete | `delete INDEX`
Search | `find KEYWORD [MORE_KEYWORDS]`
Help | `help`
Select | `select INDEX`
