package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CopyCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Person personToCopy = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        CopyCommandStub copyCommand = new CopyCommandStub(INDEX_FIRST_PERSON, null);

        CommandResult commandResult = copyCommand.execute(model);

        assertEquals(String.format(CopyCommand.MESSAGE_SUCCESS, personToCopy.getName().fullName),
                commandResult.getFeedbackToUser());

        // Assert that the generated string starts correctly
        assertTrue(copyCommand.getCopiedText().startsWith("contact add n/" + personToCopy.getName().fullName));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CopyCommand copyCommand = new CopyCommand(outOfBoundIndex, null);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                copyCommand.execute(model));
    }

    @Test
    public void execute_validName_success() throws Exception {
        CopyCommandStub copyCommand = new CopyCommandStub(null, ALICE.getName());
        CommandResult commandResult = copyCommand.execute(model);

        assertEquals(String.format(CopyCommand.MESSAGE_SUCCESS, ALICE.getName().fullName),
                commandResult.getFeedbackToUser());
        assertTrue(copyCommand.getCopiedText().startsWith("contact add n/Alice Pauline"));
    }

    @Test
    public void execute_invalidName_throwsCommandException() {
        Name notInBookName = new Name("Someone Not In Book");
        CopyCommand copyCommand = new CopyCommand(null, notInBookName);

        assertThrows(CommandException.class, CopyCommand.MESSAGE_PERSON_NOT_FOUND, () -> copyCommand.execute(model));
    }

    @Test
    public void execute_generatesCorrectComplexString() throws Exception {
        // Create a person with tags, games, and aliases to test the string builder
        Person complexPerson = new PersonBuilder().withName("Gamer John")
                .withTags("friend")
                .withGameAndAliases("Valorant", "JDog", "ValoKing")
                .withGameAndAliases("Minecraft", "MinerSteve")
                .build();

        model.addPerson(complexPerson);

        CopyCommandStub copyCommand = new CopyCommandStub(null, complexPerson.getName());
        copyCommand.execute(model);

        String copiedText = copyCommand.getCopiedText();

        // Verify all parts were constructed correctly
        assertTrue(copiedText.contains("contact add n/Gamer John"));
        assertTrue(copiedText.contains("t/friend"));
        assertTrue(copiedText.contains("g/Valorant"));
        assertTrue(copiedText.contains("al/JDog"));
        assertTrue(copiedText.contains("al/ValoKing"));
        assertTrue(copiedText.contains("g/Minecraft"));
        assertTrue(copiedText.contains("al/MinerSteve"));
    }

    @Test
    public void equals() {
        CopyCommand copyFirstCommand = new CopyCommand(INDEX_FIRST_PERSON, null);
        CopyCommand copySecondCommand = new CopyCommand(INDEX_SECOND_PERSON, null);
        CopyCommand copyNameCommand1 = new CopyCommand(null, new Name("Alice"));
        CopyCommand copyNameCommand2 = new CopyCommand(null, new Name("Bob"));

        assertTrue(copyFirstCommand.equals(copyFirstCommand));
        CopyCommand copyFirstCommandCopy = new CopyCommand(INDEX_FIRST_PERSON, null);
        assertTrue(copyFirstCommand.equals(copyFirstCommandCopy));
        assertFalse(copyFirstCommand.equals(1));
        assertFalse(copyFirstCommand.equals(null));
        assertFalse(copyFirstCommand.equals(copySecondCommand));
        assertTrue(copyNameCommand1.equals(new CopyCommand(null, new Name("Alice"))));
        assertFalse(copyNameCommand1.equals(copyNameCommand2));
        assertFalse(copyFirstCommand.equals(copyNameCommand1));
    }

    /**
     * A stub class to bypass the JavaFX Clipboard initialization requirement during headless testing.
     */
    private class CopyCommandStub extends CopyCommand {
        private String copiedText = "";

        public CopyCommandStub(Index targetIndex, Name targetName) {
            super(targetIndex, targetName);
        }

        @Override
        protected void copyToClipboard(String text) {
            this.copiedText = text;
        }

        public String getCopiedText() {
            return copiedText;
        }
    }
}
