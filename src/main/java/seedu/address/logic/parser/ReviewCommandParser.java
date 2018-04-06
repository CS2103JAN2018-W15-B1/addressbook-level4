//@@author emer7
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.logic.ReviewInputEvent;
import seedu.address.commons.events.ui.ShowReviewDialogEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ReviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.review.Review;

/**
 * Parses input arguments and creates a new Review object
 */
public class ReviewCommandParser implements Parser<ReviewCommand> {

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private String reviewerInput;
    private String reviewInput;

    /**
     * Parses the given {@code String} of arguments in the context of the ReviewCommand
     * and returns a ReviewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ReviewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        Scanner sc = new Scanner(args);
        if (!sc.hasNextInt()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(((Integer) sc.nextInt()).toString());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE));
        }

        EventsCenter.getInstance().registerHandler(this);
        EventsCenter.getInstance().post(new ShowReviewDialogEvent());

        if (reviewerInput == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE));
        } else if (reviewerInput.isEmpty()) {
            reviewerInput = "-";
        }
        String reviewer = reviewerInput.trim();

        if (reviewInput == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE));
        } else if (reviewInput.isEmpty()) {
            reviewInput = "-";
        }
        String review = reviewInput.trim();

        String combined = reviewer + "\n" + review;
        HashSet<Review> combinedSet = new HashSet<Review>();
        combinedSet.add(new Review(combined));

        EditCommand.EditPersonDescriptor editPersonDescriptor = new EditCommand.EditPersonDescriptor();
        editPersonDescriptor.setReviews(combinedSet);
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(ReviewCommand.MESSAGE_NOT_EDITED);
        }

        return new ReviewCommand(index, editPersonDescriptor);
    }

    @Subscribe
    private void getReviewInput(ReviewInputEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        reviewerInput = event.getReviewerInput();
        reviewInput = event.getReviewInput();
    }
}
