package seedu.address.logic.commands;
//@@author crizyli
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.logic.FileChoosedEvent;
import seedu.address.commons.events.ui.ShowFileChooserEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Photo;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Adds a photo to an employee.
 */
public class AddPhotoCommand extends Command {

    public static final String COMMAND_WORD = "addPhoto";

    public static final String IMAGE_FOLDER = "\\src\\main\\resources\\images\\";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a photo to an employee.\n"
            + "Choose a photo in the file chooser"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "New photo added!";

    private final Index targetIndex;
    private String path;

    /**
     * Creates an AddPhotoCommand to add the specified {@code Photo}
     */
    public AddPhotoCommand(Index index) {
        this.targetIndex = index;
        registerAsAnEventHandler(this);
    }

    @Override
    public CommandResult execute() throws CommandException {

        EventsCenter.getInstance().post(new ShowFileChooserEvent());

        List<Person> lastShownList = model.getFilteredPersonList();
        Person targetPerson = lastShownList.get(targetIndex.getZeroBased());
        String photoNameWithExtension = path.substring(path.lastIndexOf("\\") + 1);

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        String src = path;
        String dest = s + IMAGE_FOLDER + photoNameWithExtension;

        byte[] buffer = new byte[1024];
        try {
            FileInputStream fis = new FileInputStream(src);
            BufferedInputStream bis = new BufferedInputStream(fis);


            FileOutputStream fos = new FileOutputStream(dest);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }

            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Photo newPhoto = new Photo(photoNameWithExtension);
        targetPerson.setPhotoName(newPhoto.getName());
        Person editedPerson = new Person(targetPerson.getName(), targetPerson.getPhone(), targetPerson.getEmail(),
                targetPerson.getAddress(), targetPerson.getTags(), targetPerson.getCalendarId());
        editedPerson.setPhotoName(newPhoto.getName());

        try {
            model.updatePerson(targetPerson, editedPerson);
        } catch (DuplicatePersonException e) {
            e.printStackTrace();
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddPhotoCommand // instanceof handles nulls
                && targetIndex.equals(((AddPhotoCommand) other).targetIndex)
                && path.equals(((AddPhotoCommand) other).path));
    }

    protected void registerAsAnEventHandler(Object handler) {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Subscribe
    private void handleFileChoosedEvent(FileChoosedEvent event) {
        this.path = event.getFilePath();
    }
}
