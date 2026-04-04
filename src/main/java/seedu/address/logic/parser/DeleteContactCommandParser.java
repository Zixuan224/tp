package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteContactCommand object
 */
public class DeleteContactCommandParser implements Parser<DeleteContactCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteContactCommand
     * and returns a DeleteContactCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteContactCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equalsIgnoreCase("me"); // Now checks for 'me'
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, DeleteContactCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please do not provide a name prefix (n/)"
                    + " when targeting your own profile with 'me'.");
        }

        if (useUserProfile) {
            return new DeleteContactCommand(null, null, true);
        }

        if (hasNamePrefix) {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            return new DeleteContactCommand(null, name, false);
        }

        Index index = ParserUtil.parseIndex(preamble);
        return new DeleteContactCommand(index, null, false);
    }
}
