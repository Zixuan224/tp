package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsNonEmptyArray() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertNotNull(persons);
        assertTrue(persons.length > 0);
    }

    @Test
    public void getSamplePersons_allPersonsHaveNames() {
        for (Person person : SampleDataUtil.getSamplePersons()) {
            assertNotNull(person.getName());
        }
    }

    @Test
    public void getSampleAddressBook_containsSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
        assertEquals(SampleDataUtil.getSamplePersons().length,
                addressBook.getPersonList().size());
    }
}
