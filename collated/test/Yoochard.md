# Yoochard
###### \java\guitests\guihandles\ResultDisplayHandle.java
``` java
    /**
     * Returns the list of style classes present in the result display.
     */
    public List<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }
}
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public void sort(String field) {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\ChangeThemeCommandTest.java
``` java

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_THEME_NAME;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class ChangeThemeCommandTest {

    private UserPrefs userPrefs;
    private Model model;
    private GuiSettings guiSettings;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        userPrefs = new UserPrefs();
        guiSettings = userPrefs.getGuiSettings();
    }

    @Test
    public void executeChangeThemeCommandSuccess() throws Exception {
        ChangeThemeCommand changeThemeCommand = prepareCommand("dark");

        String expectedMessage = String.format(ChangeThemeCommand.MESSAGE_CHANGE_THEME_SUCCESS, "dark");

        UserPrefs expectedUserPrefs = new UserPrefs();
        expectedUserPrefs.setGuiSettings(
                guiSettings.getWindowHeight(),
                guiSettings.getWindowWidth(),
                guiSettings.getWindowCoordinates().x,
                guiSettings.getWindowCoordinates().y,
                guiSettings.getTheme());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), expectedUserPrefs);

        assertCommandSuccess(changeThemeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeChangeThemeCommandInvalidThemeNameFailure() throws Exception {
        ChangeThemeCommand changeThemeCommand = prepareCommand(INVALID_THEME_NAME);

        String expectedMessage = String.format(ChangeThemeCommand.MESSAGE_INVALID_THEME_NAME, INVALID_THEME_NAME);

        assertCommandFailure(changeThemeCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        ChangeThemeCommand themeDarkTheme = new ChangeThemeCommand("dark");
        ChangeThemeCommand themeBrightTheme = new ChangeThemeCommand("bright");

        // same theme -> returns true
        assertTrue(themeDarkTheme.equals(themeDarkTheme));
        assertTrue(themeBrightTheme.equals(themeBrightTheme));

        // invalid types-> returns false
        assertFalse(themeDarkTheme.equals(1));

        // null -> returns false
        assertFalse(themeDarkTheme.equals(null));

        // different themes -> returns false
        assertFalse(themeDarkTheme.equals(themeBrightTheme));
    }

    /**
     * Returns an {@code ChangeThemeCommand} with parameters {@code theme}
     */
    private ChangeThemeCommand prepareCommand(String theme) {
        ChangeThemeCommand changeThemeCommand = new ChangeThemeCommand(theme);
        changeThemeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return changeThemeCommand;
    }

}
```
###### \java\seedu\address\logic\commands\SortCommandTest.java
``` java

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code LockCommand}.
 */
public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        SortCommand firstSortCommand = new SortCommand("rate");
        SortCommand secondSortCommand = new SortCommand("name");

        // same object -> returns true
        assertTrue(firstSortCommand.equals(firstSortCommand));

        // same values -> returns true
        SortCommand secondSortCommandcopy = new SortCommand("name");
        assertTrue(secondSortCommand.equals(secondSortCommandcopy));

        // different types -> returns false
        assertFalse(secondSortCommand.equals(1));

        // null -> returns false
        assertFalse(firstSortCommand.equals(null));

        // different value -> returns false
        assertFalse(firstSortCommand.equals(secondSortCommand));
    }

    @Test
    public void sortSuccess() {
        SortCommand testSortCommand = new SortCommand("rate");
        testSortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = SortCommand.MESSAGE_SORT_EMPLOYEE_SUCCESS;
        try {
            CommandResult commandResult = testSortCommand.execute();
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }



}
```
###### \java\seedu\address\logic\parser\SortCommandParserTest.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.SortCommand;

/**
 * Test scope: To test SortCommandParser.
 * @see DeleteCommandParserTest
 */
public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void no_arguments_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\ui\CommandBoxTest.java
``` java
    /**
     * Runs a command that fails, then verifies that <br>
     *      - the text remains <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForFailedCommand() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertFalse(((NewResultAvailableEvent) eventsCollectorRule.eventsCollector.getMostRecent()).isSuccessful);
        eventsCollectorRule.eventsCollector.reset();
        assertEquals(COMMAND_THAT_FAILS, commandBoxHandle.getInput());
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Runs a command that succeeds, then verifies that <br>
     *      - the text is cleared <br>
     *      - the command box's style is the same as {@code defaultStyleOfCommandBox}.
     */
    private void assertBehaviorForSuccessfulCommand() {
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertTrue(((NewResultAvailableEvent) eventsCollectorRule.eventsCollector.getMostRecent()).isSuccessful);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() > 1);
        eventsCollectorRule.eventsCollector.reset();
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }
```
###### \java\seedu\address\ui\ResultDisplayTest.java
``` java
    private static final NewResultAvailableEvent NEW_RESULT_SUCCESS_EVENT_STUB =
            new NewResultAvailableEvent("success", true);
    private static final NewResultAvailableEvent NEW_RESULT_FAILURE_EVENT_STUB =
            new NewResultAvailableEvent("failure", false);
    private List<String> defaultStyleOfResultDisplay;
    private List<String> errorStyleOfResultDisplay;

    private ResultDisplayHandle resultDisplayHandle;
```
###### \java\seedu\address\ui\ResultDisplayTest.java
``` java
    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(),
                ResultDisplayHandle.RESULT_DISPLAY_ID));

        defaultStyleOfResultDisplay = new ArrayList<>(resultDisplayHandle.getStyleClass());
        defaultStyleOfResultDisplay.remove(ResultDisplay.SUGGESTION_STYLE_CLASS);

        errorStyleOfResultDisplay = new ArrayList<>(defaultStyleOfResultDisplay);
        errorStyleOfResultDisplay.add(ResultDisplay.ERROR_STYLE_CLASS);
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        resultDisplayHandle.getStyleClass().remove(ResultDisplay.SUGGESTION_STYLE_CLASS);
        assertEquals(ResultDisplay.WELCOME_MESSAGE, resultDisplayHandle.getText());
        assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());

        // receiving new results
        assertResultDisplay(NEW_RESULT_SUCCESS_EVENT_STUB);
        assertResultDisplay(NEW_RESULT_FAILURE_EVENT_STUB);
    }

    /**
     * Posts the {@code event} to the {@code EventsCenter}, then verifies that <br>
     *      - the text on the result display matches the {@code event}'s message <br>
     *      - the result display's style is the same as {@code defaultStyleOfResultDisplay} if event is successful,
     *        {@code errorStyleOfResultDisplay} otherwise.
     */
    private void assertResultDisplay(NewResultAvailableEvent event) {
        postNow(event);
        guiRobot.pauseForHuman();

        List<String> expectedStyleClass = event.isSuccessful ? defaultStyleOfResultDisplay : errorStyleOfResultDisplay;
        resultDisplayHandle.getStyleClass().remove(ResultDisplay.SUGGESTION_STYLE_CLASS);

        assertEquals(event.message, resultDisplayHandle.getText());
        assertEquals(expectedStyleClass, resultDisplayHandle.getStyleClass());
    }
}
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    /**
     * Asserts that the command box and result display shows the default style.
     */
    protected void assertCommandBoxAndResultDisplayShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
        assertEquals(defaultStyleOfResultDisplay, getResultDisplay().getStyleClass());
    }

    /**
     * Asserts that the command box and result display shows the error style.
     */
    protected void assertCommandBoxAndResultDisplayShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
        assertEquals(errorStyleOfResultDisplay, getResultDisplay().getStyleClass());
    }

```
