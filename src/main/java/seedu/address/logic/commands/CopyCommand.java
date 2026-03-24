package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Copies a contact's addition command to the system clipboard.
 */
public class CopyCommand extends Command {

    public static final String COMMAND_WORD = "copy";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Copies the 'contact add' command for the specified contact to the clipboard.\n"
            + "Parameters: INDEX (must be a positive integer) OR " + PREFIX_NAME + "NAME\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe";

    public static final String MESSAGE_SUCCESS = "Copied contact to clipboard: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name not found";

    private final Index targetIndex;
    private final Name targetName;

    /**
     * @param targetIndex of the person in the filtered person list to copy
     * @param targetName of the person in the filtered person list to copy
     */
    public CopyCommand(Index targetIndex, Name targetName) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToCopy;

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToCopy = lastShownList.get(targetIndex.getZeroBased());
        } else {
            personToCopy = lastShownList.stream()
                    .filter(p -> p.getName().equals(targetName))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
        }

        String copyString = generateCommandString(personToCopy);
        copyToClipboard(copyString);

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToCopy.getName().fullName));
    }

    /**
     * Reconstructs the exact 'contact add' command string needed to recreate the person.
     */
    private String generateCommandString(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(AddContactCommand.COMMAND_WORD).append(" ");
        sb.append(PREFIX_NAME).append(person.getName().fullName).append(" ");

        for (Tag tag : person.getTags()) {
            sb.append(PREFIX_TAG).append(tag.tagName).append(" ");
        }

        for (Game game : person.getGames()) {
            sb.append(PREFIX_GAME).append(game.gameName).append(" ");
            for (Alias alias : game.getAliases()) {
                sb.append(PREFIX_ALIAS).append(alias.value).append(" ");
            }
        }

        return sb.toString().trim();
    }

    /**
     * Saves the generated string to the user's system clipboard.
     */
    private void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CopyCommand otherCopyCommand)) {
            return false;
        }

        boolean indexEquals = (targetIndex == null && otherCopyCommand.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(otherCopyCommand.targetIndex));
        boolean nameEquals = (targetName == null && otherCopyCommand.targetName == null)
                || (targetName != null && targetName.equals(otherCopyCommand.targetName));

        return indexEquals && nameEquals;
    }
}
