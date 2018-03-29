package seedu.address.commons.events.model;

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
