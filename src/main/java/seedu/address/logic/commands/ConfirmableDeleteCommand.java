package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a delete command that requires user confirmation before executing.
 * Intentionally decoupled from {@code UndoableCommand} to allow future commands
 * that require confirmation but do not support undo.
 */
public interface ConfirmableDeleteCommand {

    /**
     * Performs the actual deletion after the user has confirmed.
     *
     * @throws CommandException if the deletion fails.
     */
    CommandResult performDeletion(Model model) throws CommandException;

    /**
     * Returns the message to display when the user cancels the deletion.
     */
    String getCancelMessage();
}
