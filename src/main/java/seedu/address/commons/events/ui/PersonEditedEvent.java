//@@author emer7
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.Person;

/**
 * Represents a selection change in the Person List Panel
 */
public class PersonEditedEvent extends BaseEvent {


    private final Person newPerson;

    public PersonEditedEvent(Person newPerson) {
        this.newPerson = newPerson;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Person getNewPerson() {
        return newPerson;
    }
}
