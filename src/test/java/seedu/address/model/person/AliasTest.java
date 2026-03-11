package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AliasTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Alias(null));
    }

    @Test
    public void constructor_invalidAlias_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Alias("   "));
    }

    @Test
    public void isValidAlias_variousInputs_correctValidation() {
        // null alias
        assertThrows(NullPointerException.class, () -> Alias.isValidAlias(null));

        // invalid alias
        assertFalse(Alias.isValidAlias("")); // empty string
        assertFalse(Alias.isValidAlias("   ")); // spaces only
        assertFalse(Alias.isValidAlias("a".repeat(Alias.MAX_LENGTH + 1))); // too long

        // valid alias
        assertTrue(Alias.isValidAlias("fr1edDucky"));
        assertTrue(Alias.isValidAlias("da biggus brain"));
        assertTrue(Alias.isValidAlias("a".repeat(Alias.MAX_LENGTH)));
        assertTrue(Alias.isValidAlias("  trimmed alias  "));
    }

    @Test
    public void constructor_trimsSurroundingWhitespace() {
        Alias alias = new Alias("  supportMain  ");
        assertTrue(alias.equals(new Alias("supportMain")));
        assertTrue(alias.toString().equals("supportMain"));
    }

    @Test
    public void equals_variousInputs_correctEquality() {
        Alias alias = new Alias("Valid Alias");

        // same values -> returns true
        assertTrue(alias.equals(new Alias("Valid Alias")));

        // same object -> returns true
        assertTrue(alias.equals(alias));

        // null -> returns false
        assertFalse(alias.equals(null));

        // different types -> returns false
        assertFalse(alias.equals(5.0f));

        // different values -> returns false
        assertFalse(alias.equals(new Alias("Other Alias")));

        // different case -> returns false (equals is case-sensitive)
        assertFalse(alias.equals(new Alias("valid alias")));
    }
}
