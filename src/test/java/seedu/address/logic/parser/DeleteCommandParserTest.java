package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.model.person.Name;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteContactCommand code.
 */
public class DeleteCommandParserTest {

    private DeleteContactCommandParser parser = new DeleteContactCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteContactCommand() {
        assertParseSuccess(parser, " n/Alice Pauline", new DeleteContactCommand(new Name("Alice Pauline")));
    }

    @Test
    public void parse_missingNamePrefix_throwsParseException() {
        assertParseFailure(parser, "Alice Pauline",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteContactCommand.MESSAGE_USAGE));
    }
}
