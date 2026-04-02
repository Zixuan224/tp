package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code CopyCommand}.
 */
public class CopyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * A stub class to intercept the clipboard operation for headless testing.
     * Prevents "Toolkit not initialized" exceptions in CI/CD pipelines.
     */
    private static class CopyCommandStub extends CopyCommand {
        private String copiedText = "";

        public CopyCommandStub(Index targetIndex, Name targetName, boolean useUserProfile) {
            super(targetIndex, targetName, useUserProfile);
        }

        @Override
        protected void copyToClipboard(String text) {
            this.copiedText = text; // Intercept and save the generated string!
        }
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Person personToCopy = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        CopyCommandStub copyCommand = new CopyCommandStub(INDEX_FIRST_PERSON, null, false);

        CommandResult result = copyCommand.execute(model);

        String expectedMessage = String.format(CopyCommand.MESSAGE_SUCCESS, personToCopy.getName().fullName);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        // Verify the clipboard string actually starts with the correct command word and prefix
        assertTrue(copyCommand.copiedText.startsWith("contact add n/" + personToCopy.getName().fullName));
    }

    @Test
    public void execute_validNameUnfilteredList_success() throws Exception {
        Person personToCopy = ALICE; // Alice is in the TypicalAddressBook
        CopyCommandStub copyCommand = new CopyCommandStub(null, personToCopy.getName(), false);

        CommandResult result = copyCommand.execute(model);

        String expectedMessage = String.format(CopyCommand.MESSAGE_SUCCESS, personToCopy.getName().fullName);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(copyCommand.copiedText.contains("n/" + personToCopy.getName().fullName));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CopyCommand copyCommand = new CopyCommand(outOfBoundIndex, null, false);

        assertCommandFailure(copyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidNameUnfilteredList_throwsCommandException() {
        Name unknownName = new Name("Unknown Person");
        CopyCommand copyCommand = new CopyCommand(null, unknownName, false);

        assertCommandFailure(copyCommand, model, CopyCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_missingUserProfile_throwsCommandException() {
        // Assuming the default TypicalAddressBook does not have a user profile set
        CopyCommand copyCommand = new CopyCommand(null, null, true);

        assertCommandFailure(copyCommand, model, CopyCommand.MESSAGE_NO_PROFILE);
    }

    @Test
    public void equals() {
        CopyCommand copyFirstCommand = new CopyCommand(INDEX_FIRST_PERSON, null, false);
        CopyCommand copySecondCommand = new CopyCommand(INDEX_SECOND_PERSON, null, false);
        CopyCommand copyNameCommand = new CopyCommand(null, ALICE.getName(), false);
        CopyCommand copyProfileCommand = new CopyCommand(null, null, true);

        // same object -> returns true
        assertTrue(copyFirstCommand.equals(copyFirstCommand));

        // same values -> returns true
        CopyCommand copyFirstCommandCopy = new CopyCommand(INDEX_FIRST_PERSON, null, false);
        assertTrue(copyFirstCommand.equals(copyFirstCommandCopy));

        // different types -> returns false
        assertFalse(copyFirstCommand.equals(1));

        // null -> returns false
        assertFalse(copyFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(copyFirstCommand.equals(copySecondCommand));

        // different target types (index vs name) -> returns false
        assertFalse(copyFirstCommand.equals(copyNameCommand));

        // different target types (index vs profile) -> returns false
        assertFalse(copyFirstCommand.equals(copyProfileCommand));
    }
}
