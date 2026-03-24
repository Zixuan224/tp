package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new CopyCommand object.
 */
public class CopyCommandParser implements Parser<CopyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CopyCommand
     * and returns a CopyCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public CopyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble();
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        // Ensure user didn't enter both an index AND a name, or neither
        ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, CopyCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        Index index = preamble.isEmpty() ? null : ParserUtil.parseIndex(preamble);
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        return new CopyCommand(index, name);
    }
}
