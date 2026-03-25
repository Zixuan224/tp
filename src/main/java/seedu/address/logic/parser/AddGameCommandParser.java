package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddGameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new AddGameCommand object.
 */
public class AddGameCommandParser implements Parser<AddGameCommand> {

    @Override
    public AddGameCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME);

        if (argMultimap.getValue(PREFIX_GAME).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE));
        }

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equals("0");
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, AddGameCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please provide either an index OR a name, not both.");
        }

        Index index = (!useUserProfile && !preamble.isEmpty()) ? ParserUtil.parseIndex(preamble) : null;
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        String gameString = argMultimap.getValue(PREFIX_GAME).get();
        if (!Game.isValidGameName(gameString)) {
            throw new ParseException(Game.MESSAGE_CONSTRAINTS);
        }
        Game game = new Game(gameString);

        return new AddGameCommand(index, name, game, useUserProfile);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
