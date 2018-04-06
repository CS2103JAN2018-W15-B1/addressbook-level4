package seedu.address.logic.commands;
//@@author crizyli

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;
//import seedu.address.logic.CommandHistory;
//import seedu.address.logic.UndoRedoStack;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code TodoListCommand}.
 */
public class TodoListCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        TodoListCommand todoListCommand = new TodoListCommand();

        // same object -> returns true
        assertTrue(todoListCommand.equals(todoListCommand));

        // different types -> returns false
        assertFalse(todoListCommand.equals(1));

        // null -> returns false
        assertFalse(todoListCommand.equals(null));
    }

    /*@Test
    public void showSuccess() {
        TodoListCommand command = new TodoListCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = TodoListCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }*/
}
