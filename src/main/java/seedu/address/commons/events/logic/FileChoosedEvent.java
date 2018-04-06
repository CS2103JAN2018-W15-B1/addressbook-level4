package seedu.address.commons.events.logic;
//@@author crizyli
import seedu.address.commons.events.BaseEvent;

/**
 * An event to inform AddPhotoCommand the photo is chosen.
 */
public class FileChoosedEvent extends BaseEvent {

    private final String filePath;

    public FileChoosedEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getFilePath() {
        return filePath;
    }
}
