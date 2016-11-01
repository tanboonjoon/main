package seedu.address.logic;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;

import javafx.util.Pair;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.parser.ArgumentsParser;
import seedu.address.logic.parser.CommandArgs;

// @@author A0135768R
public class ArgsParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void argsParser_RequiredArgs_exception() throws IncorrectCommandException {

        thrown.expect(IncorrectCommandException.class);

        ArgumentsParser parser = new ArgumentsParser();

        parser.addNoFlagArg(CommandArgs.NAME).addOptionalArg(CommandArgs.DESC).addRequiredArg(CommandArgs.END_DATETIME);

        parser.parse("hello");

        assert (!parser.getArgValue(CommandArgs.DESC).isPresent());

    }

    @Test
    public void argsParser_validInputs_MaintainOrder() throws IncorrectCommandException {
        ArgumentsParser parser = new ArgumentsParser(true);

        List<Pair<LocalDateTime, LocalDateTime>> startEndPairs = Lists.newLinkedList();

        startEndPairs.add(new Pair<LocalDateTime, LocalDateTime>(DateUtil.parseStringIntoDateTime("today 12pm").get(),
                DateUtil.parseStringIntoDateTime("today 2pm").get()));

        startEndPairs
                .add(new Pair<LocalDateTime, LocalDateTime>(DateUtil.parseStringIntoDateTime("tomorrow 12pm").get(),
                        DateUtil.parseStringIntoDateTime("tomorrow 2pm").get()));

        startEndPairs
                .add(new Pair<LocalDateTime, LocalDateTime>(DateUtil.parseStringIntoDateTime("next week 12pm").get(),
                        DateUtil.parseStringIntoDateTime("next week 2pm").get()));

        parser.addOptionalArg(CommandArgs.END_DATETIME).addOptionalArg(CommandArgs.START_DATETIME);

        StringBuilder sb = new StringBuilder();

        for (Pair<LocalDateTime, LocalDateTime> pair : startEndPairs) {
            sb.append("st/" + pair.getKey().toString() + " et/" + pair.getValue().toString() + " ");
        }

        parser.parse(sb.toString());

        List<String> start = parser.getArgValues(CommandArgs.START_DATETIME).get();
        List<String> end = parser.getArgValues(CommandArgs.END_DATETIME).get();

        for (int i = 0; i < start.size(); i++) {
            assertEquals(start.get(i), startEndPairs.get(i).getKey().toString());
            assertEquals(end.get(i), startEndPairs.get(i).getValue().toString());

        }

    }
}
