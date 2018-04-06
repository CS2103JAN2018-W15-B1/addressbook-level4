//@@author emer7
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.PersonEditedEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.review.Review;

/**
 * Updates the review of an existing person in the address book.
 */
public class ReviewCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "review";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Review the person identified "
            + "by the index number used in the last person listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "A separate pop-up dialog will appear to request for the review.";

    public static final String MESSAGE_REVIEW_PERSON_SUCCESS = "Reviewed Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Both INDEX and REVIEW must be provided.";
    public static final String MESSAGE_DUPLICATE_REVIEW = "Review must be different.";

    private final Index index;
    private final EditCommand.EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public ReviewCommand(Index index, EditCommand.EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditCommand.EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_REVIEW);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_REVIEW_PERSON_SUCCESS, editedPerson));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        EventsCenter.getInstance().post(new PersonEditedEvent(editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit,
                                             EditCommand.EditPersonDescriptor editPersonDescriptor
    ) throws CommandException {
        assert personToEdit != null;
        assert editPersonDescriptor.getReviews().isPresent();

        Set<Review> oldReviews = personToEdit.getReviews();
        Set<Review> newReviews = editPersonDescriptor.getReviews().get();
        HashSet<Review> updatedReviews = new HashSet<Review>();

        Review newReview = newReviews.iterator().next();
        String newReviewer = newReview.reviewer;
        String newValue = newReview.value;
        Iterator<Review> iterator = oldReviews.iterator();
        while (iterator.hasNext()) {
            Review oldReview = iterator.next();
            String oldReviewer = oldReview.reviewer;
            String oldValue = oldReview.value;
            if (oldReviewer.equals(newReviewer) && oldValue.equals(newValue)) {
                throw new CommandException(MESSAGE_DUPLICATE_REVIEW);
            } else if (oldReviewer.equals(newReviewer)) {
            } else {
                updatedReviews.add(oldReview);
            }
        }
        updatedReviews.add(newReview);

        Person toReturn = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), personToEdit.getCalendarId());
        toReturn.setRating(personToEdit.getRating());
        toReturn.setReviews(updatedReviews);
        toReturn.setId(personToEdit.getId());
        toReturn.setPhotoName(personToEdit.getPhotoName());
        return toReturn;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ReviewCommand)) {
            return false;
        }

        // state check
        ReviewCommand e = (ReviewCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
}
