package seedu.address.commons.events.ui;
//@@author crizyli
import seedu.address.commons.events.BaseEvent;


/**
 * Represents an event to free resources in Browser Panel
 */
public class HideDetailPanelEvent extends BaseEvent {

    public HideDetailPanelEvent() { }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
