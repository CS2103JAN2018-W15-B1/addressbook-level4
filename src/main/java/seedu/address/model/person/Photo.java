package seedu.address.model.person;

/**
 * Represents a Person's photo.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhotoPath(String)}
 */
public class Photo {

    public static final String DEFAULT_PHOTO_FOLDER = "/images/";

    public final String path;

    /**
     * Constructs a {@code Photo}.
     *
     * @param name A photo name in images folder.
     */
    public Photo(String name) {
        this.path = DEFAULT_PHOTO_FOLDER + name;
    }

    /**
     * Returns true if a given string is a valid photo path.
     */
    public static boolean isValidPhotoPath(String test) {
        return true;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.path.equals(((Photo) other).path)); // state check
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
