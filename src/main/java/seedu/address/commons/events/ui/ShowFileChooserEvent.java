package seedu.address.commons.events.ui;
//@@author crizyli
import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to show the file chooser.
 */
public class ShowFileChooserEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
