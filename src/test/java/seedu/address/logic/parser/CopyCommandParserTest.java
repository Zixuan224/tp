package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CopyCommand;
import seedu.address.model.person.Name;

public class CopyCommandParserTest {

    private CopyCommandParser parser = new CopyCommandParser();

    @Test
    public void parse_validIndex_returnsCopyCommand() {
        assertParseSuccess(parser, "1", new CopyCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void parse_validName_returnsCopyCommand() {
        assertParseSuccess(parser, " n/Amy Bee", new CopyCommand(null, new Name("Amy Bee")));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        assertParseFailure(parser, "1 n/Amy Bee", "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_neitherIndexNorName_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }
}
