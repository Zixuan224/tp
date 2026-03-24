package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Name;

public class CopyCommandTest {

    @Test
    public void equals() {
        CopyCommand copyFirstCommand = new CopyCommand(INDEX_FIRST_PERSON, null);
        CopyCommand copySecondCommand = new CopyCommand(INDEX_SECOND_PERSON, null);
        CopyCommand copyNameCommand1 = new CopyCommand(null, new Name("Alice"));
        CopyCommand copyNameCommand2 = new CopyCommand(null, new Name("Bob"));

        // same object -> returns true
        assertTrue(copyFirstCommand.equals(copyFirstCommand));

        // same values -> returns true
        CopyCommand copyFirstCommandCopy = new CopyCommand(INDEX_FIRST_PERSON, null);
        assertTrue(copyFirstCommand.equals(copyFirstCommandCopy));

        // different types -> returns false
        assertFalse(copyFirstCommand.equals(1));

        // null -> returns false
        assertFalse(copyFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(copyFirstCommand.equals(copySecondCommand));

        // name commands
        assertTrue(copyNameCommand1.equals(new CopyCommand(null, new Name("Alice"))));
        assertFalse(copyNameCommand1.equals(copyNameCommand2));

        // mixing name and index -> returns false
        assertFalse(copyFirstCommand.equals(copyNameCommand1));
    }
}
