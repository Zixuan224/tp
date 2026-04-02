package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CopyCommand;
import seedu.address.model.person.Name;

public class CopyCommandParserTest {

    private CopyCommandParser parser = new CopyCommandParser();

    @Test
    public void parse_validIndex_returnsCopyCommand() {
        assertParseSuccess(parser, "1", new CopyCommand(INDEX_FIRST_PERSON, null, false));
    }

    @Test
    public void parse_validName_returnsCopyCommand() {
        Name expectedName = new Name("John Doe");
        assertParseSuccess(parser, " n/John Doe", new CopyCommand(null, expectedName, false));
    }

    @Test
    public void parse_validUserProfile_returnsCopyCommand() {
        // "me" preamble maps to user profile flag = true
        assertParseSuccess(parser, "me", new CopyCommand(null, null, true));

        // Ensure whitespace doesn't break it
        assertParseSuccess(parser, "  me  ", new CopyCommand(null, null, true));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        assertParseFailure(parser, "1 n/John Doe",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothUserProfileAndName_throwsParseException() {
        assertParseFailure(parser, "me n/John Doe",
                "Please do not provide a name prefix (n/) when targeting your own profile with 'me'.");
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " n/John n/Doe",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }
}
