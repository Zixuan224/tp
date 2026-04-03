package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final List<JsonAdaptedGame> games = new ArrayList<>();
    private final boolean isUserProfile;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     * The {@code tags} parameter is accepted for backward compatibility with existing data files
     * but is intentionally ignored as the tag feature has been removed.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("tags") List<Object> tags,
                             @JsonProperty("games") List<JsonAdaptedGame> games,
                             @JsonProperty("isUserProfile") boolean isUserProfile) {
        this.name = name;
        this.isUserProfile = isUserProfile;
        if (games != null) {
            this.games.addAll(games);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        isUserProfile = source.isUserProfile();
        games.addAll(source.getGames().stream()
                .map(JsonAdaptedGame::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Game> personGames = new ArrayList<>();
        for (JsonAdaptedGame game : games) {
            personGames.add(game.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Set<Game> modelGames = new HashSet<>(personGames);
        return new Person(modelName, modelGames, isUserProfile);
    }

}
