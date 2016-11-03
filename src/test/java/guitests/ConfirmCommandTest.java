package guitests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.testutil.TestTask;

//@@author A0135768R
public class ConfirmCommandTest extends TaskForceGuiTest {

    @Test
    public void confirmCommand_validInputs_success() {
        setupConfirmTest();

        TestTask[] list = new TestTask[1];

        list[0] = new TestTask();
        list[0].setName("meeting with boss");
        list[0].setStartDate(DateUtil.parseStringIntoDateTime("today 4pm").get());
        list[0].setEndDate(DateUtil.parseStringIntoDateTime("today 9pm").get());

        assertConfirmSuccess("confirm 1 st/today 4pm et/today 9pm", list);

    }

    @Test
    public void confirmCommand_invalidInputs_errorMsg() {
        setupConfirmTest();

        commandBox.runCommand("confirm dsadsa st/323232");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));

        commandBox.runCommand("confirm st/323232");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));

        commandBox.runCommand("confirm 1 st/323232");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));

        commandBox.runCommand("confirm 1 st/'' ");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));

        commandBox.runCommand("confirm 1 st/'' et/@!@@@@!@#@# ");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));

        commandBox.runCommand("confirm 1");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
    }

    @Test
    public void confirmCommand_notBlock_errorMsg() {
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand(currentList[0].getAddCommand());

        commandBox.runCommand("confirm 1 st/today 5pm et/6pm");
        assertResultMessage(ConfirmCommand.MESSAGE_ONLY_BLOCKS);

        commandBox.runCommand("confirm 100 st/5pm et/6pm");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

    }

    private void setupConfirmTest() {

        commandBox.runCommand("clear");

        TestTask[] blocks = new TestTask[3];
        String name = "meeting with boss";

        List<LocalDateTime> startDates = new ArrayList<>();
        List<LocalDateTime> endDates = new ArrayList<>();

        startDates.add(DateUtil.parseStringIntoDateTime("today 4pm").get());
        startDates.add(DateUtil.parseStringIntoDateTime("tomorrow 3pm").get());
        startDates.add(DateUtil.parseStringIntoDateTime("next thursday 1pm").get());

        endDates.add(DateUtil.parseStringIntoDateTime("today 8pm").get());
        endDates.add(DateUtil.parseStringIntoDateTime("tomorrow 5pm").get());
        endDates.add(DateUtil.parseStringIntoDateTime("next thursday 10pm").get());

        String command = buildBlockCommand(blocks, name, startDates, endDates);
        commandBox.runCommand(command);

    }

    private String buildBlockCommand(TestTask[] blocks, String name, List<LocalDateTime> startDates,
            List<LocalDateTime> endDates) {
        StringBuilder sb = new StringBuilder();

        sb.append("block " + name + " ");

        for (int i = 0; i < blocks.length; i++) {
            TestTask task = new TestTask();

            task.setName(name);
            task.setStartDate(startDates.get(i));
            task.setEndDate(endDates.get(i));

            sb.append("st/" + startDates.get(i) + " et/" + endDates.get(i));
            sb.append(" ");

            blocks[i] = task;
        }

        return sb.toString();
    }

    private void assertConfirmSuccess(String command, final TestTask[] list) {
        commandBox.runCommand(command);

        assertTrue(eventListPanel.isListMatching(list));
    }

}
