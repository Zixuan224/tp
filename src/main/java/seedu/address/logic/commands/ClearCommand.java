package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Optional<Person> userProfile = model.getUserProfile();
        model.setAddressBook(new AddressBook());
        userProfile.ifPresent(model::addPerson);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
