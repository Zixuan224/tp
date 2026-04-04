package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddGameCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

public class AddGameCommandParserTest {

    private static final String PROFILE_MUTUALLY_EXCLUSIVE_ERROR =
            "Please do not provide a name prefix (n/) when targeting your own profile with 'me'.";

    private AddGameCommandParser parser = new AddGameCommandParser();
    private final Game validGame = new Game("Minecraft");
    private final Name validName = new Name("Zi Xuan");


    @Test
    public void parse_validArgsByIndex_returnsAddGameCommand() {
        // Simulates: game add 1 g/Minecraft
        assertParseSuccess(parser, " 1 g/Minecraft",
                new AddGameCommand(INDEX_FIRST_PERSON, null, validGame, false));
    }

    @Test
    public void parse_validArgsByName_returnsAddGameCommand() {
        // Simulates: game add n/Zi Xuan g/Minecraft
        assertParseSuccess(parser, " n/Zi Xuan g/Minecraft",
                new AddGameCommand(null, validName, validGame, false));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Simulates the mutually exclusive error: game add 1 n/Zi Xuan g/Minecraft
        assertParseFailure(parser, " 1 n/Zi Xuan g/Minecraft",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_missingGame_throwsParseException() {
        // Missing the game prefix completely
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " 1", expectedMessage);
        assertParseFailure(parser, " n/Zi Xuan", expectedMessage);
    }

    @Test
    public void parse_validUserProfile_returnsAddGameCommand() {
        // Simulates: game add me g/Minecraft (user profile)
        assertParseSuccess(parser, " me g/Minecraft",
                new AddGameCommand(null, null, validGame, true));

        // Ensure whitespace and case-insensitivity don't break it
        assertParseSuccess(parser, "   ME   g/Minecraft",
                new AddGameCommand(null, null, validGame, true));
    }

    @Test
    public void parse_profileWithNamePrefix_throwsParseException() {
        // "me" and name prefix together is invalid
        assertParseFailure(parser, " me n/Zi Xuan g/Minecraft",
                PROFILE_MUTUALLY_EXCLUSIVE_ERROR);
    }

    @Test
    public void parse_invalidGameName_throwsParseException() {
        // Empty game name is invalid
        assertParseFailure(parser, " 1 g/  ", seedu.address.model.game.Game.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        // Covers empty and whitespace-only strings
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE));
    }
}
