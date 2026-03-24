package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListGameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ListGameCommand object.
 */
public class ListGameCommandParser implements Parser<ListGameCommand> {

    @Override
    public ListGameCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equals("0");
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, ListGameCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please provide either an index OR a name, not both.");
        }

        Index index = (!useUserProfile && !preamble.isEmpty()) ? ParserUtil.parseIndex(preamble) : null;
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        return new ListGameCommand(index, name, useUserProfile);
    }
}
