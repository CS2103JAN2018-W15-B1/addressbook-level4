package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Rating;
import seedu.address.model.review.Review;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_TAGS = "friends";
    public static final String DEFAULT_CALENDARID = "null";
    public static final String DEFAULT_RATING = "-1";
    public static final String DEFAULT_REVIEWS = "-\n-";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Rating rating;
    private Set<Review> reviews;
    private Set<Tag> tags;
    private String calendarId;

    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        rating = new Rating(DEFAULT_RATING);
        //reviews = SampleDataUtil.getReviewSet(DEFAULT_REVIEWS);
        reviews = new HashSet<>();
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        calendarId = this.DEFAULT_CALENDARID;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        rating = personToCopy.getRating();
        reviews = personToCopy.getReviews();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code Person} that we are building.
     */
    public PersonBuilder withRating(String rating) {
        this.rating = new Rating(rating);
        return this;
    }

    //@@author emer7
    /**
     * Sets the {@code Review} of the {@code Person} that we are building.
     */
    public PersonBuilder withReviews(String ... reviews) {
        this.reviews = SampleDataUtil.getReviewSet(reviews);
        return this;
    }
    //@@author

    /**
     * Build a person with the determined details
     * @return person to be built
     */
    public Person build() {
        //@@author emer7
        Person toReturn = new Person(name, phone, email, address, tags, calendarId);
        toReturn.setRating(rating);
        toReturn.setReviews(reviews);
        return toReturn;
        //@@author
    }

}
