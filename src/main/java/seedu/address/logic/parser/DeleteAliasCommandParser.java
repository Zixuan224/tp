package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteAliasCommand object.
 */
public class DeleteAliasCommandParser implements Parser<DeleteAliasCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAliasCommand
     * and returns a DeleteAliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);

        // 1. Game and Alias prefixes are always strictly required
        if (!arePrefixesPresent(argMultimap, PREFIX_GAME, PREFIX_ALIAS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteAliasCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equalsIgnoreCase("me"); // Now checks for 'me'
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, DeleteAliasCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please do not provide a name prefix (n/) "
                    + "when targeting your own profile with 'me'.");
        }

        Index index = (!useUserProfile && !preamble.isEmpty()) ? ParserUtil.parseIndex(preamble) : null;
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        Game game = ParserUtil.parseGame(argMultimap.getValue(PREFIX_GAME).get());
        Alias alias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS).get());

        return new DeleteAliasCommand(index, name, game, alias, useUserProfile);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
