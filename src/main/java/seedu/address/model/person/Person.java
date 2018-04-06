
package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.review.Review;
import seedu.address.model.review.UniqueReviewList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final int UNINITIALISED_ID = -1;
    public static final String DEFAULT_PHOTO = "DefaultPerson.png";

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private Rating rating;
    private int id;
    private String photoName;

    private UniqueReviewList reviews;
    private final UniqueTagList tags;
    private final String calendarId;

    /**
     * All fields except Rating and Review is not provided
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, String calendarId) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.reviews = new UniqueReviewList();

        this.calendarId = calendarId;
        this.rating = new Rating();
        this.photoName = DEFAULT_PHOTO;
        this.id = UNINITIALISED_ID;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    //@@author IzHoBX
    public Rating getRating() {
        return rating;
    }

    public String getRatingDisplay() {
        return rating.getRatingDisplay();
    }
    //@@author

    public Address getAddress() {
        return address;
    }

    public Integer getId() {
        return id;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public String getPhotoName() {
        return photoName;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    //@@author emer7
    /**
     * Returns an immutable review set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Review> getReviews() {
        return Collections.unmodifiableSet(reviews.toSet());
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = new UniqueReviewList(reviews);
    }

    public String getPersonUrl() {
        return "https://calendar.google.com/calendar/embed?src="
                + calendarId.replaceAll("@", "%40") + "&ctz=Asia%2FSingapore";
    }

    /**
     * Set the photo field which is the path to the photo.
     */
    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
    //@@author

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Rating: ")
                .append(getRatingDisplay())
                .append(" Reviews: ");
        getReviews().forEach(builder::append);
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    public boolean isInitialized() {
        return id != UNINITIALISED_ID;
    }
}
