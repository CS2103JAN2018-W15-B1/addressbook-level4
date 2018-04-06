# crizyli
###### \main\java\seedu\address\commons\events\logic\AddressBookUnlockedEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * Indicates the request to toggle Notification Center
 */
public class AddressBookUnlockedEvent extends BaseEvent {
    public AddressBookUnlockedEvent() {
        super();
    }

    @Override
    public String toString() {
        return "AddressBook unlocked!";
    }
}
```
###### \main\java\seedu\address\commons\events\model\AddressBookPasswordChangedEvent.java
``` java
import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Indicates password changed
 * */
public class AddressBookPasswordChangedEvent extends BaseEvent {

    public final String password;

    public final ReadOnlyAddressBook data;

    public AddressBookPasswordChangedEvent(String password, ReadOnlyAddressBook data) {
        this.password = password;
        this.data = data;
    }

    public String toString() {
        return "number of persons " + data.getPersonList().size() + ", number of tags " + data.getTagList().size();
    }
}
```
###### \main\java\seedu\address\commons\events\ui\HideBrowserPanelEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * Represents an event to free resources in Browser Panel
 */
public class HideBrowserPanelEvent extends BaseEvent {

    public HideBrowserPanelEvent() { }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \main\java\seedu\address\commons\events\ui\ShowMyCalendarEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view my calendar page.
 */
public class ShowMyCalendarEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \main\java\seedu\address\commons\events\ui\ShowTodoListDisplayContentEvent.java
``` java
import java.util.ArrayList;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.listevent.ListEvent;

/**
 * Indicates a request to load calendar events to to do list window.
 */
public class ShowTodoListDisplayContentEvent extends BaseEvent {

    private final ArrayList<ListEvent> eventList;

    public ShowTodoListDisplayContentEvent(ArrayList<ListEvent> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ArrayList<ListEvent> getListEvent() {
        return this.eventList;
    }
}
```
###### \main\java\seedu\address\commons\events\ui\ShowTodoListEvent.java
``` java
import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.TodoListWindow;

/**
 * An event requesting to show to do list window.
 */
public class ShowTodoListEvent extends BaseEvent {

    private TodoListWindow todoListWindow;

    public ShowTodoListEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public TodoListWindow getTodoListWindow() {
        return this.todoListWindow;
    }

}
```
###### \main\java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * Deletes an event with specified title in a person's calendar.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "deleteEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Delete an event specified by title of the person identified by the index number used "
            + "in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "TITLE (event tilte)"
            + "Example: " + COMMAND_WORD + " 1 test event";

    public static final String MESSAGE_SUCCESS = "Event deleted!";
    public static final String MESSAGE_NO_SUCH_EVENT = "There is no such event!";
    public static final String MESSAGE_FAILURE = "Unable to delete event, please try again later.";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    /** Application name. */
    private static final String APPLICATION_NAME = "Employees Tracker";

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private final Index targetIndex;
    private final String title;

    public DeleteEventCommand(Index index, String title) {
        this.targetIndex = index;
        this.title = title;
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                TestAddEventCommand.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();
        Person targetPerson = lastShownList.get(targetIndex.getZeroBased());

        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
                null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String calendarId = targetPerson.getCalendarId();
        String pageToken = null;
        String eventId = null;
        do {
            Events events = null;
            try {
                events = service.events().list(calendarId).setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Event> items = events.getItems();
            for (Event event : items) {
                if (event.getSummary().compareTo(title) == 0) {
                    eventId = event.getId();
                    break;
                }
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        if (eventId != null) {
            try {
                service.events().delete(calendarId, eventId).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return new CommandResult(MESSAGE_NO_SUCH_EVENT);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    public Index getTargetIndex() {
        return this.targetIndex;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEventCommand) other).getTargetIndex())
                && this.title.equals(((DeleteEventCommand) other).getTitle())); // state check
    }
}
```
###### \main\java\seedu\address\logic\commands\LockCommand.java
``` java
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.HideBrowserPanelEvent;
import seedu.address.logic.LogicManager;
import seedu.address.model.person.HideAllPersonPredicate;

/**
 * Locks the app with a password
 * */
public class LockCommand extends Command {

    public static final String COMMAND_WORD = "lock";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lock the address book. ";

    public static final String MESSAGE_SUCCESS = "Address book has been locked!";

    private final HideAllPersonPredicate predicate = new HideAllPersonPredicate();

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        EventsCenter.getInstance().post(new HideBrowserPanelEvent());
        LogicManager.lock();

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \main\java\seedu\address\logic\commands\MyCalendarCommand.java
``` java
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowMyCalendarEvent;

/**
 * Show my own calendar
 * */
public class MyCalendarCommand extends Command {

    public static final String COMMAND_WORD = "myCalendar";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": show my own calendar. ";

    public static final String MESSAGE_SUCCESS = "Your calendar is loaded.";

    @Override
    public CommandResult execute() {

        EventsCenter.getInstance().post(new ShowMyCalendarEvent());

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \main\java\seedu\address\logic\commands\SetPasswordCommand.java
``` java
import seedu.address.logic.LogicManager;

/**
 * Set the application password
 * */
public class SetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "setPassword";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Set new application password, and old "
            + "password requeired."
            + "Parameters: "
            + "oldPassword" + " newPassword ";

    public static final String MESSAGE_SUCCESS = "New password has been set!";

    public static final String MESSAGE_INCORRECT_OLDPASSWORD = "Incorrect old password!";

    private String oldPassword;

    private String newPassword;

    public SetPasswordCommand(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public CommandResult execute() {
        if (this.oldPassword.compareTo(LogicManager.getPassword()) == 0) {
            LogicManager.setPassword(this.newPassword);
            model.setPassword(this.newPassword);
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_INCORRECT_OLDPASSWORD);
        }
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetPasswordCommand // instanceof handles nulls
                && this.oldPassword.equals(((SetPasswordCommand) other).getOldPassword())
                && this.newPassword.equals(((SetPasswordCommand) other).getNewPassword())); // state check
    }
}
```
###### \main\java\seedu\address\logic\commands\TestAddEventCommand.java
``` java
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CreateNewCalendar;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.exceptions.DuplicateTimetableEntryException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Adds an event to a person.
 */
public class TestAddEventCommand extends Command {

    public static final String COMMAND_WORD = "addEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add an event to the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + PREFIX_TITLE + "TITLE "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_STARTTIME + "STARTTIME "
            + PREFIX_ENDTIME + "ENDTIME "
            + "Example: " + COMMAND_WORD + " 1 title/test event loca/NUS, Singapore stime/2017-03-19T08:00:00"
            + " etime/2017-03-19T10:00:00 descrip/this is a test event";

    public static final String MESSAGE_SUCCESS = "Event added!";
    public static final String MESSAGE_FAILURE = "Unable to add event, please try again later.";



    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    /** Application name. */
    private static final String APPLICATION_NAME = "Employees Tracker";

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private final Index targetIndex;
    private final String title;
    private final String location;
    private final String startTime;
    private final String endTime;
    private final String description;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public TestAddEventCommand(Index index, String title, String location, String startTime,
                               String endTime, String description) {
        this.title = title;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.targetIndex = index;
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                TestAddEventCommand.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    @Override
    public CommandResult execute() throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToAddEvent = lastShownList.get(targetIndex.getZeroBased());

        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
                null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Event event = new Event()
                .setSummary(title)
                .setLocation(location)
                .setDescription(description);

        String startTimeFormat = startTime + "+08:00";
        DateTime startDateTime = new DateTime(startTimeFormat);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Singapore");
        event.setStart(start);

        String endTimeFormat = endTime + "+08:00";
        DateTime endDateTime = new DateTime(endTimeFormat);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Singapore");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        //event.setRecurrence(Arrays.asList(recurrence));

        /*EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("jjjsss@example.com"),
                new EventAttendee().setEmail("dzzzssss@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);*/

        String calendarId = personToAddEvent.getCalendarId();
        Logger logger = LogsCenter.getLogger(TestAddEventCommand.class);
        if (calendarId == null || calendarId.equals("")) {
            logger.info("calendarId null, attempting to create calendar");
            try {
                calendarId = CreateNewCalendar.execute(personToAddEvent.getName().fullName);
                logger.info("calendar created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("unable to create calendar");
                return new CommandResult(MESSAGE_FAILURE);
            }
            Person newWithCalendar = new Person(personToAddEvent.getName(),
                    personToAddEvent.getPhone(),
                    personToAddEvent.getEmail(),
                    personToAddEvent.getAddress(),
                    personToAddEvent.getTags(),
                    calendarId);
            //retain the oldId
            newWithCalendar.setId(personToAddEvent.getId());

            try {
                model.updatePerson(personToAddEvent, newWithCalendar);
            } catch (PersonNotFoundException e) {
                logger.info("Unable to find original person in model manager");
                return new CommandResult(MESSAGE_FAILURE);
            } catch (DuplicatePersonException e) {
                logger.info("newly created person (with calendarId) is same as original person");
                return new CommandResult(MESSAGE_FAILURE);
            }
        }

        try {
            event = service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("failed to add event to calendarId");
            return new CommandResult(MESSAGE_FAILURE);
        }

        Notification notification = new Notification(title, calendarId, event.getId(), event.getEnd().toString(),
                model.getPerson(targetIndex.getZeroBased()).getId().toString());
        try {
            model.addNotification(notification);
        } catch (DuplicateTimetableEntryException e) {
            throw new CommandException("Duplicated event");
        }
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TestAddEventCommand // instanceof handles nulls
                && targetIndex.equals(((TestAddEventCommand) other).targetIndex)
                && title.equals(((TestAddEventCommand) other).title)
                && location.equals(((TestAddEventCommand) other).location)
                && startTime.equals(((TestAddEventCommand) other).startTime)
                && endTime.equals(((TestAddEventCommand) other).endTime)
                && description.equals(((TestAddEventCommand) other).description));
    }
}
```
###### \main\java\seedu\address\logic\commands\TodoListCommand.java
``` java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowTodoListDisplayContentEvent;
import seedu.address.commons.events.ui.ShowTodoListEvent;
import seedu.address.model.listevent.ListEvent;

/**
 * Show to do list window.
 */
public class TodoListCommand extends Command {

    public static final String COMMAND_WORD = "todoList";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show the To Do List in a seperate window."
            + "\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "To do list window is loaded.";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    /** Application name. */
    private static final String APPLICATION_NAME = "Employees Tracker";

    private ArrayList<ListEvent> eventList;

    public TodoListCommand() {
        eventList = new ArrayList<>();
    }

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                TestAddEventCommand.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public CommandResult execute() {

        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
                null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String calendarId = "primary";
        String pageToken = null;
        do {
            Events events = null;
            try {
                events = service.events().list(calendarId).setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Event> items = events.getItems();
            for (Event event : items) {
                eventList.add(new ListEvent(event.getSummary(), event.getLocation(), event.getEnd().getDateTime()));
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        EventsCenter.getInstance().post(new ShowTodoListEvent());
        EventsCenter.getInstance().post(new ShowTodoListDisplayContentEvent(eventList));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \main\java\seedu\address\logic\commands\UnlockCommand.java
``` java
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.AddressBookUnlockedEvent;
import seedu.address.logic.LogicManager;

/**
 * Unlocks the addressbook
 */
public class UnlockCommand extends Command {

    public static final String COMMAND_WORD = "unlock";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unlock the address book. ";

    public static final String MESSAGE_SUCCESS = "Address book has been unlocked!";

    public static final String MESSAGE_INCORRECT_PASSWORD = "Incorrect unlock password!";

    private String password;

    public UnlockCommand(String keyword) {
        this.password = keyword;
    }

    @Override
    public CommandResult execute() {
        if (!LogicManager.isLocked()) {
            return new CommandResult("Employees Tracker is already unlocked!");
        }

        if (this.password.compareTo(LogicManager.getPassword()) == 0) {
            LogicManager.unLock();
            EventsCenter.getInstance().post(new AddressBookUnlockedEvent());
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_INCORRECT_PASSWORD);
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnlockCommand // instanceof handles nulls
                && this.password.equals(((UnlockCommand) other).getPassword())); // state check
    }
}
```
###### \main\java\seedu\address\logic\CreateNewCalendar.java
``` java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;

/**
 * Create a calendar for a person.
 */
public class CreateNewCalendar {


    /** Application name. */
    private static final String APPLICATION_NAME = "Employees Tracker";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** set scope to both read and write.
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                CreateNewCalendar.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Create a new calendar for person with personName.
     *
     */
    public static String execute(String personName) throws IOException {
        // Build a new authorized API client service.
        com.google.api.services.calendar.Calendar service =
                null;

        service = getCalendarService();

        // Create a new calendar
        com.google.api.services.calendar.model.Calendar calendar = new Calendar();
        calendar.setSummary(personName);
        calendar.setTimeZone("Asia/Singapore");


        // Insert the new calendar
        String calendarId = "primary";

        try {
            Calendar createdCalendar = service.calendars().insert(calendar).execute();
            calendarId = createdCalendar.getId();
            System.out.println(calendarId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calendarId;
    }

}
```
###### \main\java\seedu\address\logic\DeleteCalendar.java
``` java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

/**
 * Delete calendar of a person.
 */
public class DeleteCalendar {
    /** Application name. */
    private static final String APPLICATION_NAME = "Employees Tracker";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** set scope to both read and write.
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                CreateNewCalendar.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Delete a calendar specified by calendarId.
     *
     */
    public static void execute(String calendarId) throws IOException {
        // Build a new authorized API client service.
        com.google.api.services.calendar.Calendar service =
                null;

        service = getCalendarService();

        // Delete the calendar.
        service.calendars().delete(calendarId).execute();

    }

}
```
###### \main\java\seedu\address\logic\parser\DeleteEventCommandParser.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Scanner;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for the `DeleteEventCommand'
 */
public class DeleteEventCommandParser implements Parser<DeleteEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetPasswordCommand
     * and returns an SetPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        Scanner sc = new Scanner(trimmedArgs);
        if (!sc.hasNextInt()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(((Integer) sc.nextInt()).toString());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }

        if (!sc.hasNext()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }
        String tilte = sc.next();

        return new DeleteEventCommand(index, tilte);
    }
}
```
###### \main\java\seedu\address\logic\parser\LockCommandParser.java
``` java
import seedu.address.logic.commands.LockCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parse arguments for LockCommand
 */
public class LockCommandParser implements Parser<LockCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LockCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();


        return new LockCommand();
    }
}
```
###### \main\java\seedu\address\logic\parser\SetPasswordCommandParser.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Scanner;

import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for the SetPasswordCommand'
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetPasswordCommand
     * and returns an SetPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetPasswordCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        Scanner sc = new Scanner(trimmedArgs);
        if (!sc.hasNext()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }
        String oldPsw = sc.next();
        if (!sc.hasNext()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }
        String newPsw = sc.next();

        return new SetPasswordCommand(oldPsw, newPsw);
    }
}
```
###### \main\java\seedu\address\logic\parser\TestAddEventCommandParser.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Scanner;
import java.util.stream.Stream;

import com.google.api.client.util.DateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.TestAddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TestAddEventCommand object
 */
public class TestAddEventCommandParser implements Parser<TestAddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the TestAddEventCommand
     * and returns a TestAddEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TestAddEventCommand parse(String args) throws ParseException {

        requireNonNull(args);
        Scanner sc = new Scanner(args);
        if (!sc.hasNextInt()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(((Integer) sc.nextInt()).toString());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE));
        }

        String temp = args.trim();
        int i;
        for (i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == 32) {
                break;
            }
        }
        String behindArgs = temp.substring(i);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(behindArgs, PREFIX_TITLE, PREFIX_LOCATION, PREFIX_STARTTIME,
                        PREFIX_ENDTIME, PREFIX_DESCCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_TITLE, PREFIX_LOCATION, PREFIX_STARTTIME, PREFIX_ENDTIME,
                PREFIX_DESCCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE));
        }


        String title = argMultimap.getValue(PREFIX_TITLE).get();
        String location = argMultimap.getValue(PREFIX_LOCATION).get();
        String stime = argMultimap.getValue(PREFIX_STARTTIME).get();
        String etime = argMultimap.getValue(PREFIX_ENDTIME).get();
        try {
            DateTime.parseRfc3339(stime);
        } catch (NumberFormatException n) {
            throw new ParseException("Invalid date/time format: " + stime);
        }

        try {
            DateTime.parseRfc3339(etime);
        } catch (NumberFormatException n) {
            throw new ParseException("Invalid date/time format: " + etime);
        }

        String decription = argMultimap.getValue(PREFIX_DESCCRIPTION).get();

        return new TestAddEventCommand(index, title, location, stime, etime, decription);

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \main\java\seedu\address\logic\parser\UnlockCommandParser.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UnlockCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for the UnlockCommand'
 */
public class UnlockCommandParser implements Parser<UnlockCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnlockCommand
     * and returns an UnlockCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnlockCommand parse(String args) throws ParseException {
        requireNonNull(args);
        if (args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlockCommand.MESSAGE_USAGE));
        }
        String trimmedArgs = args.trim();

        return new UnlockCommand(trimmedArgs);
    }
}
```
###### \main\java\seedu\address\model\listevent\ListEvent.java
``` java
import com.google.api.client.util.DateTime;

/**
 * Represents a event that is to be loaded in to do list window.
 */
public class ListEvent {

    private String title;
    private String location;
    private DateTime endTime;

    public ListEvent(String title, String location, DateTime endTime) {
        this.setTitle(title);
        this.setLocation(location);
        this.setEndTime(endTime);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Event: " + title + "  Location: " + location + "   End at: " + endTime.toString();
    }
}
```
###### \main\java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void setPassword(String password) {
        addressBook.setPassword(password);
        indicatePasswordChangedEvent(password);
    }

    public String getPassword() {
        return addressBook.getPassword();
    }

```
###### \main\java\seedu\address\model\person\HideAllPersonPredicate.java
``` java
import java.util.function.Predicate;

/**
* For hiding persons during lock
*/
public class HideAllPersonPredicate implements Predicate<Person> {

    public HideAllPersonPredicate() {}

    @Override
    public boolean test(Person person) {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

}
```
###### \main\java\seedu\address\storage\StorageManager.java
``` java
    @Override
    @Subscribe
    public void handleAddressBookPasswordChangedEvent(AddressBookPasswordChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
```
###### \main\java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleHideBrowserPanelEvent(HideBrowserPanelEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        name.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        rating.setText("");
        review.setText("");
        tags.getChildren().clear();
        loadDefaultPage();
    }

```
###### \main\java\seedu\address\ui\CalendarBrowser.java
``` java
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * A pop-up browser window of the user's own calendar
 */
class CalendarBrowser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public CalendarBrowser() {
        webEngine.load("https://calendar.google.com");
        getChildren().add(browser);
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }
}
```
###### \main\java\seedu\address\ui\MyCalendarView.java
``` java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * View the pop-uped browser window of user's calendar.
 */
public class MyCalendarView extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) {
        stage.setTitle("My Calendar");
        CalendarBrowser calendarBrowser = new CalendarBrowser();
        scene = new Scene(calendarBrowser, calendarBrowser.computePrefWidth(750),
                calendarBrowser.computePrefHeight(600));
        stage.setScene(scene);
        stage.show();
    }
}
```
###### \main\java\seedu\address\ui\TodoListWindow.java
``` java
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ShowTodoListDisplayContentEvent;
import seedu.address.model.listevent.ListEvent;

/**
 * Controller for to do list window
 */
public class TodoListWindow {

    private static final Logger logger = LogsCenter.getLogger(TodoListWindow.class);
    private static final String FXML = "TodoListWindow.fxml";

    private ObservableList<ListEvent> list = FXCollections.observableArrayList();

    @FXML
    private ListView<ListEvent> eventList;

    /**
     * Creates a new TodoListWindow.
     */
    public TodoListWindow() {
        registerAsAnEventHandler(this);
    }

    public void show() {
        logger.fine("Showing to do list.");
    }

    protected void registerAsAnEventHandler(Object handler) {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Subscribe
    private void handleShowTodoListDisplayContentEvent(ShowTodoListDisplayContentEvent e) {

        ArrayList<ListEvent> allEvents = e.getListEvent();
        for (int i = 0; i < allEvents.size(); i++) {
            list.add(allEvents.get(i));
        }
        eventList.setItems(list);
    }

}
```
###### \main\java\seedu\address\ui\UiManager.java
``` java
    @Subscribe
    private void handleShowTodoListEvent(ShowTodoListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TodoListWindow.fxml"));
            fxmlLoader.setLocation(getFxmlFileUrl("TodoListWindow.fxml"));
            try {
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
    }

```
###### \main\resources\view\TodoListWindow.fxml
``` fxml
<?import javafx.collections.FXCollections?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.layout.VBox?>

<VBox prefHeight="400.0" prefWidth="649.0" xmlns:fx="http://javafx.com/fxml/1" xmlns="http://javafx.com/javafx/8.0.121" fx:controller="seedu.address.ui.TodoListWindow">
<children>
    <ListView fx:id="eventList">
        <items>
            <FXCollections fx:factory="observableArrayList">
            </FXCollections>
        </items>
    </ListView>
</children>
</VBox>
```
###### \test\java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java
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

public class DeleteEventCommandTest {
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
        TestAddEventCommand addEventCommand = new TestAddEventCommand(INDEX_FIRST_PERSON, "Test Event",
                "NUS", "2018-05-01T12:00:00", "2018-05-01T12:30:00",
                "Test add event command");
        addEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        addEventCommand.execute();
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(INDEX_FIRST_PERSON, "Test Event");
        deleteEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = DeleteEventCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = deleteEventCommand.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }*/

}
```
###### \test\java\seedu\address\logic\commands\LockCommandTest.java
``` java
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code LockCommand}.
 */
public class LockCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        LockCommand firstLockCommand = new LockCommand();
        LockCommand secondLockCommand = new LockCommand();

        // same object -> returns true
        assertTrue(firstLockCommand.equals(firstLockCommand));

        // different types -> returns false
        assertFalse(firstLockCommand.equals(1));

        // null -> returns false
        assertFalse(firstLockCommand.equals(null));

    }

    @Test
    public void lockSuccess() {

        LockCommand testLockCommand = new LockCommand();
        testLockCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = LockCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = testLockCommand.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }



}
```
###### \test\java\seedu\address\logic\commands\MyCalendarCommandTest.java
``` java
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code MyCalendarCommand}.
 */
public class MyCalendarCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        MyCalendarCommand myCalendarCommand = new MyCalendarCommand();

        // same object -> returns true
        assertTrue(myCalendarCommand.equals(myCalendarCommand));

        // different types -> returns false
        assertFalse(myCalendarCommand.equals(1));

        // null -> returns false
        assertFalse(myCalendarCommand.equals(null));

    }

    @Test
    public void viewSuccess() {
        MyCalendarCommand testCommand = new MyCalendarCommand();
        testCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = MyCalendarCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = testCommand.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }
}
```
###### \test\java\seedu\address\logic\commands\SetPasswordCommandTest.java
``` java
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.LogicManager;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code SetPasswordCommand}.
 */
public class SetPasswordCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setUp() {
        model.setPassword("admin");
        LogicManager logicManager = new LogicManager(model);
    }

    @Test
    public void equals() {

        SetPasswordCommand firstCommand = new SetPasswordCommand("admin", "qwe");
        SetPasswordCommand secondCommand = new SetPasswordCommand("admin", "123");
        SetPasswordCommand thirdCommand = new SetPasswordCommand("test", "12345");

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        SetPasswordCommand secondCommandcopy = new SetPasswordCommand("admin", "123");
        assertTrue(secondCommand.equals(secondCommandcopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different value -> returns false
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(thirdCommand.equals(secondCommand));
    }

    @Test
    public void setPasswordFail() {
        //incorrect old password entered.
        SetPasswordCommand command = new SetPasswordCommand("123", "qwe");
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = SetPasswordCommand.MESSAGE_INCORRECT_OLDPASSWORD;
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);

    }

    @Test
    public void setPasswordSuccess() {

        SetPasswordCommand command = new SetPasswordCommand("admin", "qwe");
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = SetPasswordCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);

    }
}
```
###### \test\java\seedu\address\logic\commands\TestAddEventCommandTest.java
``` java
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
```
###### \test\java\seedu\address\logic\commands\UnlockCommandTest.java
``` java
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.LogicManager;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code UnlockCommand}.
 */
public class UnlockCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        UnlockCommand firstUnlockCommand = new UnlockCommand("nopassword");
        UnlockCommand secondUnlockCommand = new UnlockCommand("12345");

        // same object -> returns true
        assertTrue(firstUnlockCommand.equals(firstUnlockCommand));

        // same values -> returns true
        UnlockCommand secondUnlockCommandcopy = new UnlockCommand("12345");
        assertTrue(secondUnlockCommand.equals(secondUnlockCommandcopy));

        // different types -> returns false
        assertFalse(firstUnlockCommand.equals(1));

        // null -> returns false
        assertFalse(firstUnlockCommand.equals(null));

        // different value -> returns false
        assertFalse(firstUnlockCommand.equals(secondUnlockCommand));
    }

    @Test
    public void unlockSuccess() {
        model.setPassword("admin");
        LogicManager logicManager = new LogicManager(model);
        LockCommand testLockCommand = new LockCommand();
        testLockCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        testLockCommand.execute();
        UnlockCommand testUnlockCommand = new UnlockCommand("admin");
        testUnlockCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = UnlockCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = testUnlockCommand.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }

    @Test
    public void unlockFail() {
        model.setPassword("qwer");
        LogicManager logicManager = new LogicManager(model);
        LockCommand testLockCommand = new LockCommand();
        testLockCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        testLockCommand.execute();
        UnlockCommand testUnlockCommand = new UnlockCommand("admin");
        testUnlockCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = UnlockCommand.MESSAGE_INCORRECT_PASSWORD;
        CommandResult commandResult = testUnlockCommand.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }

}
```
###### \test\java\seedu\address\logic\parser\DeleteEventCommandParserTest.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.DeleteEventCommand;

/**
 * Test scope: similar to {@code DeleteEventCommandParserTest}.
 * @see DeleteEventCommandParserTest
 */
public class DeleteEventCommandParserTest {

    private DeleteEventCommandParser parser = new DeleteEventCommandParser();

    @Test
    public void parse_invalidArgs() {
        // no agrs provided command
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteEventCommand.MESSAGE_USAGE));

        //only one arg provided
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteEventCommand.MESSAGE_USAGE));

        //illegal value for index
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteEventCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsDeleteEventCommand() {
        assertParseSuccess(parser, " 1 event",
                new DeleteEventCommand(INDEX_FIRST_PERSON, "event"));
    }
}
```
###### \test\java\seedu\address\logic\parser\SetPasswordCommandParserTest.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.SetPasswordCommand;

/**
 * Test scope: similar to {@code SetPasswordCommandParserTest}.
 * @see SetPasswordCommandParserTest
 */
public class SetPasswordCommandParserTest {

    private SetPasswordCommandParser parser = new SetPasswordCommandParser();

    @Test
    public void parse_invalidArgs() {
        // no agrs provided command
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SetPasswordCommand.MESSAGE_USAGE));

        //only old password provided
        assertParseFailure(parser, "qqq", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SetPasswordCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSetPasswordCommand() {
        assertParseSuccess(parser, " 1234 qwer",
                new SetPasswordCommand("1234", "qwer"));
    }
}
```
###### \test\java\seedu\address\logic\parser\TestAddEventCommandParserTest.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_ENDTIME;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_LOCATION;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_STARTTIME;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_TITLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_ENDTIME;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_STARTTIME;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_ENDTIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_LOCATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_STARTTIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TITLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.TestAddEventCommand;

public class TestAddEventCommandParserTest {

    private TestAddEventCommandParser parser = new TestAddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        final String expectedTitle = "Test Event";
        final String expectedLocation = "NUS";
        final String expectedStarttime = "2018-05-15T10:00:00";
        final String expectedEndtime = "2018-05-15T12:00:00";
        final String expectedDescription = "A test event.";

        assertParseSuccess(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, new TestAddEventCommand(INDEX_FIRST_PERSON, expectedTitle,
                expectedLocation, expectedStarttime, expectedEndtime, expectedDescription));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, new TestAddEventCommand(INDEX_FIRST_PERSON, expectedTitle,
                expectedLocation, expectedStarttime, expectedEndtime, expectedDescription));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE);

        //missing title prefix
        assertParseFailure(parser, "1" + VALID_EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing location prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + VALID_EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing starttime prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + VALID_EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing endtime prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + VALID_EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing description prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + VALID_EVENT_DESCRIPTION, expectedMessage);

        //all prefix missing
        assertParseFailure(parser, "1" + VALID_EVENT_TITLE + VALID_EVENT_LOCATION + VALID_EVENT_STARTTIME
                + VALID_EVENT_ENDTIME + VALID_EVENT_DESCRIPTION, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid start time
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + INVALID_EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, "Invalid date/time format: " + "2018-04-09T08:00");

        // invalid end time
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + INVALID_EVENT_ENDTIME + EVENT_DESCRIPTION, "Invalid date/time format: " + "2018-04-09T10");

        //non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE));
    }
}
```
###### \test\java\seedu\address\logic\parser\UnlockCommandParserTest.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.UnlockCommand;

/**
 * Test scope: similar to {@code UnlockCommandParserTest}.
 * @see UnlockCommandParserTest
 */
public class UnlockCommandParserTest {

    private UnlockCommandParser parser = new UnlockCommandParser();

    @Test
    public void parse_invalidArgs() {
        // no agrs provided command
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlockCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsLockCommand() {
        // agrs provided command
        assertParseSuccess(parser, " 1234", new UnlockCommand("1234"));
    }
}
```
