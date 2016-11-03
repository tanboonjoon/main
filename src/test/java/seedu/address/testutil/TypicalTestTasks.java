package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskForce;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestTasks() {
        try {
            alice = new TaskBuilder().withName("Alice Pauline").withDescription("123, Jurong West Ave 6, #08111")
                    .withTags("friends").build();
            benson = new TaskBuilder().withName("Benson Meier").withDescription("311, Clementi Ave 2, #0225")
                    .withTags("owesMoney", "friends").build();
            carl = new TaskBuilder().withName("Carl Kurz").withDescription("wall street").build();
            daniel = new TaskBuilder().withName("Daniel Meier").withDescription("10th street").build();
            elle = new TaskBuilder().withName("Elle Meyer").withDescription("michegan ave").build();
            fiona = new TaskBuilder().withName("Fiona Kunz").withDescription("little tokyo").build();
            george = new TaskBuilder().withName("George Best").withDescription("4th street").build();

            // Manually added
            hoon = new TaskBuilder().withName("Hoon Meier").withDescription("little india").build();
            ida = new TaskBuilder().withName("Ida Mueller").withDescription("chicago ave").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadTaskForceWithSampleData(TaskForce ab) {

        try {
            ab.addTask(new Task(alice));
            ab.addTask(new Task(benson));
            ab.addTask(new Task(carl));
            ab.addTask(new Task(daniel));
            ab.addTask(new Task(elle));
            ab.addTask(new Task(fiona));
            ab.addTask(new Task(george));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[] { alice, benson, carl, daniel, elle, fiona, george };
    }

    public TaskForce getTypicalTaskForce() {
        TaskForce ab = new TaskForce();
        loadTaskForceWithSampleData(ab);
        return ab;
    }
}
