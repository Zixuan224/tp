package seedu.address.model.util;

import java.util.HashSet;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class GeneratePlaceholder {
    /**
     * The single, static placeholder instance used whenever the User Profile is missing.
     */
    public static final Person PLACEHOLDER_PROFILE = new Person(
            new Name("PLACEHOLDER"),
            new HashSet<>(),
            true
    );

    public static ReadOnlyAddressBook getPlaceHolderddressBook() {
        AddressBook placeHolderAb = new AddressBook();
        placeHolderAb.addPerson(PLACEHOLDER_PROFILE);
        return placeHolderAb;
    }

}
