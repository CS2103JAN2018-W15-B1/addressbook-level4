package seedu.address.logic.commands;

//import static org.junit.Assert.assertEquals;
//import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Before;
//import org.junit.Test;
//import seedu.address.logic.CommandHistory;
//import seedu.address.logic.UndoRedoStack;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class TestAddEventCommandTest {
    private final Person testPerson = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("85355255")
            .withTags("friends")
            .withCalendarId("ck6s71ditb731dfepeporbnfb0@group.calendar.google.com")
            .build();

    private Model model;

    @Before
    public void setUp() {
        AddressBook ab = new AddressBook();
        try {
            ab.addPerson(testPerson);
        } catch (DuplicatePersonException e) {
            e.printStackTrace();
        }
        model = new ModelManager(ab, new UserPrefs());
    }

    /*@Test
    public void execute_addEvent_success() throws Exception {
        TestAddEventCommand command = new TestAddEventCommand(INDEX_FIRST_PERSON, "Test Event",
                "NUS", "2018-05-01T12:00:00", "2018-05-01T12:30:00",
                "Test add event command");
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = TestAddEventCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }*/
}
