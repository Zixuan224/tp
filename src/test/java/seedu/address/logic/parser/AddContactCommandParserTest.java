package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ALIAS_DESC_JETT;
import static seedu.address.logic.commands.CommandTestUtil.ALIAS_DESC_STEVE;
import static seedu.address.logic.commands.CommandTestUtil.GAME_DESC_MINECRAFT;
import static seedu.address.logic.commands.CommandTestUtil.GAME_DESC_VALORANT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALIAS_JETT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALIAS_STEVE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GAME_MINECRAFT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GAME_VALORANT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddContactCommand;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddContactCommandParserTest {
    private final AddContactCommandParser parser = new AddContactCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB
                + TAG_DESC_FRIEND, new AddContactCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddContactCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY, new AddContactCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_gamesAndAliases_success() {
        // 1. Game without alias
        Person expectedPerson1 = new PersonBuilder(BOB).withGames(VALID_GAME_MINECRAFT).build();
        assertParseSuccess(parser, NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + GAME_DESC_MINECRAFT,
                new AddContactCommand(expectedPerson1));

        // 2. Game with multiple aliases
        Person expectedPerson2 = new PersonBuilder(BOB)
                .withGameAndAliases(VALID_GAME_VALORANT, VALID_ALIAS_STEVE, VALID_ALIAS_JETT).build();
        assertParseSuccess(parser, NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + GAME_DESC_VALORANT
                        + ALIAS_DESC_STEVE + ALIAS_DESC_JETT, new AddContactCommand(expectedPerson2));

        // 3. Multiple games mapping to their respective aliases chronologically
        Person expectedPerson3 = new PersonBuilder(BOB)
                .withGameAndAliases(VALID_GAME_MINECRAFT, VALID_ALIAS_STEVE)
                .withGameAndAliases(VALID_GAME_VALORANT, VALID_ALIAS_JETT).build();
        assertParseSuccess(parser, NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + GAME_DESC_MINECRAFT
                + ALIAS_DESC_STEVE + GAME_DESC_VALORANT + ALIAS_DESC_JETT, new AddContactCommand(expectedPerson3));
    }

    @Test
    public void parse_aliasWithoutGame_failure() {
        String expectedMessage = "Aliases must be preceded by a game prefix (g/).";
        // Attempting to add an alias before declaring a game
        assertParseFailure(parser, NAME_DESC_BOB + ALIAS_DESC_STEVE + GAME_DESC_MINECRAFT, expectedMessage);
    }
}
