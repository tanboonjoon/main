package guitests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.BlockCommand;
import seedu.address.testutil.TestTask;

// @@author A0135768R
public class BlockCommandTest extends TaskForceGuiTest {

    @Test
    public void blockCommand_validInputs_Success() {
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

        assertBlockSuccess(command, blocks);
    }

    @Test
    public void blockCommand_DuplicateDates_DatesNotDistinctMessage() {
        commandBox.runCommand("clear");

        TestTask[] blocks = new TestTask[3];
        String name = "meeting with boss";

        List<LocalDateTime> startDates = new ArrayList<>();
        List<LocalDateTime> endDates = new ArrayList<>();

        startDates.add(DateUtil.parseStringIntoDateTime("today 4pm").get());
        startDates.add(DateUtil.parseStringIntoDateTime("tomorrow 3pm").get());
        startDates.add(DateUtil.parseStringIntoDateTime("today 4pm").get());

        endDates.add(DateUtil.parseStringIntoDateTime("today 8pm").get());
        endDates.add(DateUtil.parseStringIntoDateTime("tomorrow 5pm").get());
        endDates.add(DateUtil.parseStringIntoDateTime("next thursday 10pm").get());

        String command = buildBlockCommand(blocks, name, startDates, endDates);

        commandBox.runCommand(command);
        assertResultMessage(BlockCommand.DATES_NOT_DISTINCT_MESSAGE);

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

    private void assertBlockSuccess(String command, final TestTask[] list) {
        commandBox.runCommand(command);
        commandBox.runCommand("list");

        assertTrue(eventListPanel.isListMatching(list));
    }

}
