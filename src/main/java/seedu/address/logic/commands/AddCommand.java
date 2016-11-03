package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import javafx.util.Pair;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Adds a task to the taskForce list.
 */
public class AddCommand extends Command {

    private static final int RECURRENCE_ALTERNATE_INCREMENT_STEP = 2;
    private static final int RECURRENCE_INCREMENT_STEP = 1;
    private static final int MIN_NUMBER_OF_RECURRENCE = 1;
    private static final int MAX_NUMBER_OF_RECURRENCE = 20;
    public static final String[] COMMAND_WORD = { "add", "schedule", "remind" };

    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0];

    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD + ": Adds a new task to the Task Force. \n"
            + "Format : Task : TASKNAME [d/DESCIPRTION] [t/TAG...]\n"
            + "Deadline : TASKNAME [d/DESCIPRTION] et/END_DATE [t/TAG...]\n"
            + "Recurring Deadline : TASKNAME [d/DESCRIPTION] et/END_DATE [t/TAG...] recurring/FREQUENCY (daily, weekly..., alternate day, alternate week) repeat/REPETITION (between 1 - 20)\n"
            + "Event : EVENTNAME [d/DESCRIPTION] st/START_DATE et/END_DATE [t/TAG...]\n"
            + "Recurring Event : EVENTNAME [d/DESCRIPTION] st/START_DATE et/END_DATE recurring/FREQUENCY (daily, weekly..., alternate day, alternate week...) repeat/REPETITION (between 1 - 20) [t/TAG...]\n"

            + "Example: " + DEFAULT_COMMAND_WORD + " Homework d/CS2103 hw t/veryImportant t/urgent"
            + " Finish report d/physic lab report et/130116 2200 t/important"
            + " Weekly meeting d/progress update st/thursday 3pm et/thursday 4pm recurring/weekly repeat/20";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the ToDo list!";
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please make sure you follow the correct add format";
    public static final String INVALID_END_DATE_MESSAGE = "Please make sure your end date is later than start date";
    public static final String MISSING_NUMBER_OF_RECURRENCE_MESSAGE = "Please indicate the number of recurring by using 'repeat/NUMBER (between 1 - 20)'";
    public static final String REPEAT_ARGUMENT_MESSAGE = "repeat argument must be positive integer between 1 and 20.";
    public static final String WRONG_RECURRING_ARGUMENTS_MESSAGE = "Wrong usage of recurring argument. There are 8 options: daily, weekly, monthly, yearly, alternate day, alternate week, alternate month and alternate year.";

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<String> tagNames;
    private String recurringFrequency;
    private int repeat;
    private int id;
    private List<Task> taskList = new ArrayList<>();

    // @@author A0135768R
    /**
     * 
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public AddCommand(String name, String description, String startDate, String endDate, Set<String> tags,
            String recurring, String repeat) throws IllegalValueException {

        setRecurrenceAttributes(recurring, repeat);

        if (startDate == null && endDate == null) {

            setNewTaskWithDetails(name, description, tags);

        } else if (startDate == null && endDate != null) {

            Optional<Pair<LocalDateTime, LocalDateTime>> datePair = DateUtil.determineStartAndEndDateTime(null,
                    endDate);
            setNewTaskWithDetails(name, description, datePair.get().getValue(), tags);

        } else if (startDate != null) {

            Pair<LocalDateTime, LocalDateTime> datePair = DateUtil.determineStartAndEndDateTime(startDate, endDate)
                    .orElseThrow(() -> new IllegalValueException(INVALID_END_DATE_MESSAGE));

            setNewTaskWithDetails(name, description, datePair.getKey(), datePair.getValue(), tags);

        } else {
            throw new IllegalValueException(INVALID_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        this.id = model.getNextTaskId();

        if (recurringFrequency != null && repeat == 0) {
            return new CommandResult(MISSING_NUMBER_OF_RECURRENCE_MESSAGE);
        }

        try {

            if (recurringFrequency != null && repeat >= MIN_NUMBER_OF_RECURRENCE) {
                this.createRecurringEvent(model, recurringFrequency, repeat);

            } else {
                this.taskList.add(getNewTask(model));
            }

        } catch (IllegalValueException e) {
            return new CommandResult(e.getMessage());
        }

        try {

            for (Task task : taskList) {
                model.addTask(task);
            }

            return new CommandResult(getAddCommandSuccessMessage(taskList), true);

        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
    }

    private void setNewTaskWithDetails(String name, String description, LocalDateTime startDate, LocalDateTime endDate,
            Set<String> tags) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tagNames = tags;
    }

    private void setNewTaskWithDetails(String name, String description, LocalDateTime endDate, Set<String> tags) {
        setNewTaskWithDetails(name, description, null, endDate, tags);
    }

    private void setNewTaskWithDetails(String name, String description, Set<String> tags) {
        setNewTaskWithDetails(name, description, null, null, tags);
    }

    private Task getNewTask(Model model) throws IllegalValueException {

        UniqueTagList tagList = getTagList(tagNames);

        if (startDate == null && endDate != null) {
            return new Deadline(id, name, description, endDate, tagList);
        }

        if (startDate != null && endDate != null) {
            return new Event(id, name, description, startDate, endDate, tagList);
        }

        return new Task(id, name, description, tagList);
    }

    private String getAddCommandSuccessMessage(List<Task> tasks) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(MESSAGE_SUCCESS, taskList.get(0)));

        if (tasks.size() > 1) {
            sb.append("Task repeats " + recurringFrequency);
        }

        for (Task task : tasks) {

            if (!(task instanceof Event)) {
                continue;
            }

            Event event = (Event) task;
            Optional<Event> conflict = DateUtil.checkForConflictingEvents(model, event);

            if (conflict.isPresent()) {
                sb.append("\n");
                sb.append(Messages.CONFLICTING_EVENTS_DETECTED + " The event is:" + conflict.get().getName());
                break;
            }
        }

        return sb.toString();
    }

    private UniqueTagList getTagList(Iterable<String> tagNames) throws IllegalValueException {
        Set<Tag> tagSet = Sets.newHashSet();

        for (String name : tagNames) {
            tagSet.add(model.getTagRegistry().getTagFromString(name, true));
        }

        return new UniqueTagList(tagSet);
    }

    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(ImmutableList.copyOf(taskList),
                Collections.emptyList());
    }

    // @@author A0140037W

    private void createRecurringEvent(Model model, String recurring, int repeat) throws IllegalValueException {
        if (repeat >= MIN_NUMBER_OF_RECURRENCE) {
            this.taskList.add(getNewTask(model));
            this.startDate = this.parseFrequency(startDate, recurring);
            this.endDate = this.parseFrequency(endDate, recurring);
            this.createRecurringEvent(model, recurring, repeat - 1);
        }

    }

    private LocalDateTime parseFrequency(LocalDateTime date, String recurring) throws IllegalValueException {
        if (date != null) {
            switch (recurring.trim().toLowerCase()) {
            case "daily":
                return date.plusDays(RECURRENCE_INCREMENT_STEP);
            case "weekly":
                return date.plusWeeks(RECURRENCE_INCREMENT_STEP);
            case "monthly":
                return date.plusMonths(RECURRENCE_INCREMENT_STEP);
            case "yearly":
                return date.plusYears(RECURRENCE_INCREMENT_STEP);
            case "alternate day":
                return date.plusDays(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "fortnightly":
                return date.plusWeeks(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "biweekly":
                return date.plusWeeks(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "alternate month":
                return date.plusMonths(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "bimonthly":
                return date.plusMonths(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "alternate year":
                return date.plusYears(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "biyearly":
                return date.plusYears(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            default:
                throw new IllegalValueException(WRONG_RECURRING_ARGUMENTS_MESSAGE);
            }
        } else {
            return date;
        }

    }

    private void setRecurrenceAttributes(String recurring, String repeat) throws IllegalValueException {
        if (recurring != null && repeat == null) {
            throw new IllegalValueException(MISSING_NUMBER_OF_RECURRENCE_MESSAGE);
        } else if (recurring == null && repeat != null) {
            throw new IllegalValueException(MESSAGE_USAGE);
        }

        this.recurringFrequency = recurring;

        setRepeat(repeat);
    }

    private void setRepeat(String repeat) throws IllegalValueException {
        if (repeat != null) {
            if (StringUtil.isParsable(repeat) && StringUtil.isUnsignedInteger(repeat)) {
                int temp = Integer.parseInt(repeat);
                if (temp <= MAX_NUMBER_OF_RECURRENCE && temp >= MIN_NUMBER_OF_RECURRENCE) {
                    this.repeat = Integer.parseInt(repeat);
                } else {
                    throw new IllegalValueException(REPEAT_ARGUMENT_MESSAGE);
                }
            } else {
                throw new IllegalValueException(REPEAT_ARGUMENT_MESSAGE);
            }
        }
    }
}
