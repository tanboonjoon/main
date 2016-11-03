package seedu.address.logic;

import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.commands.*;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.util.DateUtil;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.model.TaskForce;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;
import seedu.address.storage.StorageManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.*;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    // These are for checking the correctness of the events raised
    private ReadOnlyTaskForce latestSavedTaskForce;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskForceChangedEvent abce) {
        latestSavedTaskForce = new TaskForce(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        String tempTaskForceFile = saveFolder.getRoot().getPath() + "TempTaskForce.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskForceFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskForce = new TaskForce(model.getTaskForce()); // last
                                                                    // saved
                                                                    // assumed
                                                                    // to be up
                                                                    // to date
                                                                    // before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet

        model.getConfigs().setConfigurationOption("enableSudo", true);
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void executeInvalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'address book' and the 'last shown list' are expected to be
     * empty.
     * 
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskForce, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskForce(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's
     * state are as expected:<br>
     * - the internal address book data are same as those in the
     * {@code expectedTaskForce} <br>
     * - the backing list shown by UI matches the {@code shownList} <br>
     * - {@code expectedTaskForce} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage, ReadOnlyTaskForce expectedTodoList,
            List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        // Execute the command
        CommandResult result = logic.execute(inputCommand);
        // Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());
        // //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTodoList, model.getTaskForce());
        assertEquals(expectedTodoList, latestSavedTaskForce);
    }

    @Test
    public void executeUnknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void executeHelp() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void executeExit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    // @Test
    // public void executeClear() throws Exception {
    // TestDataHelper helper = new TestDataHelper();
    // model.addTask(helper.generateTask(1));
    // model.addTask(helper.generateTask(2));
    // model.addTask(helper.generateTask(3));
    //
    // assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new
    // TaskForce(), Collections.emptyList());
    // }

    @Test
    public void executeAddInvalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior("add wrong args wrong args /t", expectedMessage);
        assertCommandBehavior("add wrong args wrong args d/", expectedMessage);
        assertCommandBehavior("add wrong args wrong args d/ t/", expectedMessage);
        assertCommandBehavior("add wrong args wrong args d/         /t", expectedMessage);
        assertCommandBehavior("add d/         /t", expectedMessage);
        assertCommandBehavior("add hi d/hello dl/asd", expectedMessage);
        assertCommandBehavior("add hi d/hello dt/asd", expectedMessage);
    }

    @Test
    public void executeAddInvalidTaskData() throws Exception {
        // NOT APPLICABLE TO THE CURRENT ADD COMMAND
        // assertCommandBehavior(
        // "add []\\[;] p/12345 e/valid@e.mail a/valid, address",
        // Name.MESSAGE_NAME_CONSTRAINTS);
        // assertCommandBehavior(
        // "add Valid Name p/not_numbers e/valid@e.mail a/valid, address",
        // Phone.MESSAGE_PHONE_CONSTRAINTS);
        // assertCommandBehavior(
        // "add Valid Name p/12345 e/notAnEmail a/valid, address",
        // Email.MESSAGE_EMAIL_CONSTRAINTS);
        // assertCommandBehavior(
        // "add Valid Name p/12345 e/valid@e.mail a/valid, address
        // t/invalid_-[.tag", Tag.MESSAGE_TAG_CONSTRAINTS);

    }

    @Test
    public void executeAddOrderDontMatter() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        TaskForce expectedAB = new TaskForce();

        Task john = helper.john();
        expectedAB.addTask(john);

        assertCommandBehavior("add John's Birthday party t/friendsparty d/at his house",
                String.format(AddCommand.MESSAGE_SUCCESS, john), expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void addCommandOptionalArgs() throws Exception {

        TestDataHelper helper = new TestDataHelper();
        TaskForce expectedAB = new TaskForce();

        // Optional arguments
        Task johnny = helper.johnny();
        expectedAB.addTask(johnny);
        assertCommandBehavior("add Johnny's Birthday party d/at his house",
                String.format(AddCommand.MESSAGE_SUCCESS, johnny), expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void executeAddSuccessful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        TaskForce expectedAB = new TaskForce();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedAB, expectedAB.getTaskList());

        Task test_event = helper.testEvent();
        expectedAB.addTask(test_event);

        Task test_deadline = helper.testDeadline();
        expectedAB.addTask(test_deadline);

        Task test_eventWithoutEndDate = helper.testEventWithoutEndDate();

        CommandResult result = logic.execute("add event d/this is a event st/02-13-2016 1300 et/02-13-2016 1310");
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, test_event), result.feedbackToUser);

        CommandResult result2 = logic.execute("add deadline d/this is a deadline et/Aug 13 2016 1600");
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, test_deadline), result2.feedbackToUser);

        CommandResult result3 = logic.execute("add eventWithoutStartTime st/today 9pm");
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, test_eventWithoutEndDate), result3.feedbackToUser);
    }

    @Test
    public void executeListShowsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskForce expectedAB = helper.generateTaskForce(2);
        List<? extends ReadOnlyTask> expectedList = expectedAB.getTaskList();

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandBehavior("list", ListCommand.MESSAGE_SUCCESS, expectedAB, expectedList);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single task in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single task in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandBehavior(commandWord, expectedMessage); // index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); // index
                                                                    // cannot be
                                                                    // 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single task in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single task in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generateTaskList(2);

        // set AB state to 2 tasks
        model.resetData(new TaskForce());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTaskForce(), taskList);
    }

    @Test
    public void executeDeleteInvalidArgsFormatErrorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void executeDeleteIndexNotFoundErrorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void executeDeleteRemovesCorrectTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);

        TaskForce expectedAB = helper.generateTaskForce(threeTasks);
        expectedAB.removeTask(threeTasks.get(1));
        helper.addToModel(model, threeTasks);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threeTasks.get(1).getName()), expectedAB,
                expectedAB.getTaskList());
    }

    /*
     * Comment out until find command is fixed
     * 
     * @Test public void execute_find_invalidArgsFormat() throws Exception {
     * String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
     * FindCommand.MESSAGE_USAGE); assertCommandBehavior("find ",
     * expectedMessage); }
     * 
     * @Test public void execute_find_onlyMatchesFullWordsInNames() throws
     * Exception { TestDataHelper helper = new TestDataHelper(); Task pTarget1 =
     * helper.generateTaskWithName("bla bla KEY bla"); Task pTarget2 =
     * helper.generateTaskWithName("bla KEY bla bceofeia"); Task p1 =
     * helper.generateTaskWithName("KE Y"); Task p2 =
     * helper.generateTaskWithName("KEYKEYKEY sduauo");
     * 
     * List<Task> fourTasks = helper.generateTaskList(p1, pTarget1, p2,
     * pTarget2); TaskForce expectedAB = helper.generateTaskForce(fourTasks);
     * List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2);
     * helper.addToModel(model, fourTasks);
     * 
     * assertCommandBehavior("find KEY",
     * Command.getMessageForTaskListShownSummary(expectedList.size()),
     * expectedAB, expectedList); }
     * 
     * @Test public void execute_find_isNotCaseSensitive() throws Exception {
     * TestDataHelper helper = new TestDataHelper(); Task p1 =
     * helper.generateTaskWithName("bla bla KEY bla"); Task p2 =
     * helper.generateTaskWithName("bla KEY bla bceofeia"); Task p3 =
     * helper.generateTaskWithName("key key"); Task p4 =
     * helper.generateTaskWithName("KEy sduauo");
     * 
     * List<Task> fourTasks = helper.generateTaskList(p3, p1, p4, p2); TaskForce
     * expectedAB = helper.generateTaskForce(fourTasks); List<Task> expectedList
     * = fourTasks; helper.addToModel(model, fourTasks);
     * 
     * assertCommandBehavior("find KEY",
     * Command.getMessageForTaskListShownSummary(expectedList.size()),
     * expectedAB, expectedList); }
     * 
     * 
     * @Test public void execute_find_matchesIfAnyKeywordPresent() throws
     * Exception { TestDataHelper helper = new TestDataHelper(); Task pTarget1 =
     * helper.generateTaskWithName("bla bla KEY bla"); Task pTarget2 =
     * helper.generateTaskWithName("bla rAnDoM bla bceofeia"); Task pTarget3 =
     * helper.generateTaskWithName("key key"); Task p1 =
     * helper.generateTaskWithName("sduauo");
     * 
     * List<Task> fourTasks = helper.generateTaskList(pTarget1, p1, pTarget2,
     * pTarget3); TaskForce expectedAB = helper.generateTaskForce(fourTasks);
     * List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2,
     * pTarget3); helper.addToModel(model, fourTasks);
     * 
     * assertCommandBehavior("find key rAnDoM",
     * Command.getMessageForTaskListShownSummary(expectedList.size()),
     * expectedAB, expectedList); }
     */

    @Test
    public void executeInvalidBlockCommand() throws Exception {
        TaskForce expectedAB = new TaskForce();

        assertCommandBehavior("block st/sadsd et/dasdad",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BlockCommand.MESSAGE_USAGE), expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void executeInvalidDatesBlockCommand() throws Exception {
        TaskForce expectedAB = new TaskForce();

        assertCommandBehavior("block name st/sadsd et/today 5pm", AddCommand.INVALID_END_DATE_MESSAGE, expectedAB,
                expectedAB.getTaskList());
    }

    /**
     * A utility class to generate test data.
     */
    public class TestDataHelper {

        public Task adam() throws Exception {
            String name = "Adam Brown";

            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(0, name, " ", tags);
        }

        public Task john() throws Exception {
            return new Task(0, "John's Birthday party", "at his house", new UniqueTagList(new Tag("friendsparty")));
        }

        public Task johnny() throws Exception {
            return new Task(0, "Johnny's Birthday party", "at his house", new UniqueTagList());
        }

        private Task testDeadline() throws Exception {
            return new Deadline(0, "deadline", "this is a deadline",
                    DateUtil.parseStringIntoDateTime("13 Aug 16 1300").get(), new UniqueTagList());
        }

        private Task testEventWithoutEndDate() throws Exception {
            return new Event(0, "eventWithoutStartTime", "", DateUtil.parseStringIntoDateTime("today 9pm").get(),
                    DateUtil.END_OF_TODAY, new UniqueTagList());
        }

        private Task testEvent() throws Exception {
            LocalDateTime startDate = DateUtil.parseStringIntoDateTime("02-13-2016 1300").get();
            LocalDateTime endDate = DateUtil.parseStringIntoDateTime("02-13-2016 1310").get();
            return new Event(0, "event", "this is a event", startDate, endDate, new UniqueTagList());

        }

        /**
         * Generates a valid task using the given seed. Running this function
         * with the same parameter values guarantees the returned task will have
         * the same state. Each unique seed will generate a unique Task object.
         *
         * @param seed
         *            used to generate the task data field values
         */
        public Task generateTask(int seed) throws Exception {
            return new Task(0, "Task " + seed, "description " + seed,
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        /** Generates the correct add command based on the task given */
        public String generateAddCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getName().toString());
            cmd.append(" d/").append(p.getDescription());

            UniqueTagList tags = p.getTags();
            for (Tag t : tags) {
                cmd.append(" t/").append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an TaskForce with auto-generated tasks.
         */
        public TaskForce generateTaskForce(int numGenerated) throws Exception {
            TaskForce taskForce = new TaskForce();
            addToTaskForce(taskForce, numGenerated);
            return taskForce;
        }

        /**
         * Generates an TaskForce based on the list of Tasks given.
         */
        public TaskForce generateTaskForce(List<Task> tasks) throws Exception {
            TaskForce taskForce = new TaskForce();
            addToTaskForce(taskForce, tasks);
            return taskForce;
        }

        /**
         * Adds auto-generated Task objects to the given TaskForce
         * 
         * @param taskForce
         *            The TaskForce to which the Tasks will be added
         */
        public void addToTaskForce(TaskForce taskForce, int numGenerated) throws Exception {
            addToTaskForce(taskForce, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given TaskForce
         */
        public void addToTaskForce(TaskForce taskForce, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                taskForce.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         * 
         * @param model
         *            The model to which the Tasks will be added
         */
        public void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given model
         */
        public void addToModel(Model model, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Tasks based on the flags.
         */
        public List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        public List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some
         * dummy values.
         */
        public Task generateTaskWithName(String name) throws Exception {
            return new Task(0, name, "description ...", new UniqueTagList(new Tag("tag"))

            );
        }
    }
}
