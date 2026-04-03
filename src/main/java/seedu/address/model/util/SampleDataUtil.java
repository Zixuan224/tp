package seedu.address.model.util;

import java.util.HashSet;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new HashSet<>()),
            new Person(new Name("Bernice Yu"), new HashSet<>()),
            new Person(new Name("Charlotte Oliveiro"), new HashSet<>()),
            new Person(new Name("David Li"), new HashSet<>()),
            new Person(new Name("Irfan Ibrahim"), new HashSet<>()),
            new Person(new Name("Roy Balakrishnan"), new HashSet<>())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
