package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Photo;
import seedu.address.model.person.Rating;
import seedu.address.model.review.Review;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String calendarId;
    @XmlElement
    private String rating;
    @XmlElement(required = true)
    private String photoName;
    @XmlElement
    private List<XmlAdaptedReview> reviewed = new ArrayList<>();
    @XmlElement
    private int id;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     * To retain until XmlAdaptedPersonTest is updated.
     */
    public XmlAdaptedPerson(String name, String phone, String email, String address, List<XmlAdaptedTag> tagged,
                            String calendarId, String photoName) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.calendarId = calendarId;
        this.id = Person.UNINITIALISED_ID;
        this.photoName = photoName;
    }

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedPerson(String name,
                            String phone,
                            String email,
                            String address,
                            String rating,
                            List<XmlAdaptedReview> reviewed,
                            List<XmlAdaptedTag> tagged,
                            int id,
                            String photoName) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rating = rating;
        if (reviewed != null) {
            this.reviewed = new ArrayList<>(reviewed);
        }
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.id = id;
        this.photoName = photoName;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        rating = source.getRating().value.toString();
        reviewed = new ArrayList<>();
        for (Review review : source.getReviews()) {
            reviewed.add(new XmlAdaptedReview(review));
        }
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        calendarId = source.getCalendarId();
        id = source.getId();
        photoName = source.getPhotoName();
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        final List<Review> personReviews = new ArrayList<>();
        for (XmlAdaptedReview review : reviewed) {
            personReviews.add(review.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        //@@author IzHoBX
        if (this.rating == null) {
            this.rating = (new Rating()).toString();
        }
        if (!Rating.isValidRating(this.rating)) {
            throw new IllegalValueException(Rating.MESSAGE_RATING_CONSTRAINTS);
        }

        final Rating rating = new Rating(this.rating);
        //@@author crizyli

        final Photo photo = new Photo(this.photoName);

        //@@author emer7
        final Set<Review> reviews = new HashSet<>(personReviews);
        //@@author

        final Set<Tag> tags = new HashSet<>(personTags);

        //@@author emer7
        Person toReturn = new Person(name, phone, email, address, tags, calendarId);
        toReturn.setRating(rating);
        toReturn.setReviews(reviews);
        toReturn.setId(id);
        toReturn.setPhotoName(photo.getName());
        //System.out.println(toReturn.getName().fullName + "  " + toReturn.getPhotoName());

        return toReturn;
        //@@author
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedPerson otherPerson = (XmlAdaptedPerson) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}
