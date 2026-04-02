package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.model.person.Name;

public class DeleteContactCommandParserTest {
    private static final String PROFILE_MUTUALLY_EXCLUSIVE_ERROR =
            "Please do not provide a name prefix (n/) when "
                    + "targeting your own profile with 'me'.";

    private final DeleteContactCommandParser parser = new DeleteContactCommandParser();
    @Test
    public void parse_validIndex_returnsDeleteContactCommand() {
        assertParseSuccess(parser, "1", new DeleteContactCommand(INDEX_FIRST_PERSON, null, false));
    }

    @Test
    public void parse_validName_returnsDeleteContactCommand() {
        assertParseSuccess(parser, " n/Alice", new DeleteContactCommand(null, new Name("Alice"), false));
    }

    @Test
    public void parse_validUserProfile_returnsDeleteContactCommand() {
        // "me" preamble maps to user profile flag = true
        assertParseSuccess(parser, "me", new DeleteContactCommand(null, null, true));

        // Ensure whitespace and case-insensitivity don't break it
        assertParseSuccess(parser, "   ME   ", new DeleteContactCommand(null, null, true));
    }

    @Test
    public void parse_missingBothIndexAndName_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteContactCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Triggers the mutually exclusive error by providing both an index and a name
        assertParseFailure(parser, "1 n/Alice",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_profileWithNamePrefix_throwsParseException() {
        // Triggers the mutually exclusive error by providing "me" and a name prefix
        assertParseFailure(parser, "me n/Alice",
                PROFILE_MUTUALLY_EXCLUSIVE_ERROR);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " n/Alice n/Bob",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }
}
