//@@author emer7
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.ReviewCommand;

public class ReviewCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE);

    private ReviewCommandParser parser = new ReviewCommandParser();

    @Test
    public void parse_indexInvalid_failure() {
        assertParseFailure(parser, "a", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_indexZeroOrLess_failure() {
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-1", MESSAGE_INVALID_FORMAT);
    }
}
