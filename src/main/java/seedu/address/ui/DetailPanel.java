package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.HideDetailPanelEvent;
import seedu.address.commons.events.ui.PersonEditedEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class DetailPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    //default dummy page.
    private static final String SEARCH_PAGE_URL =
            "https://calendar.google.com/calendar/embed?src=ck6s71ditb731dfepeporbnfb0@group.calendar"
                    + ".google.com&ctz=Asia%2FSingapore";
    private static final String FXML = "DetailPanel.fxml";
    private static final String[] TAG_COLOR_STYLES = {"red", "yellow", "blue", "orange", "brown", "green"};

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;
    //@@author emer7
    @FXML
    private GridPane detailPanel;
    @FXML
    private Label name;
    @FXML
    private Label address;
    @FXML
    private FlowPane reviews;
    //@@author

    public DetailPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    private void loadPersonPage(Person person) {
        loadPage(person.getPersonUrl());
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    public static String getSearchPageUrl() {
        return SEARCH_PAGE_URL;
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
        //@@author emer7
        name = null;
        address = null;
        reviews = null;
        //@@author
    }

    @Subscribe
    private void handleHideDetailPanelEvent(HideDetailPanelEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        name.setText("");
        address.setText("");
        reviews.getChildren().clear();
        loadDefaultPage();
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //@@author emer7
        Person person = event.getNewSelection().person;
        name.setText(person.getName().fullName);
        address.setText(person.getAddress().value);
        reviews.getChildren().clear();
        person.getReviews().forEach(review -> reviews.getChildren().add(new Label(review.toString())));
        //@@author
        loadPersonPage(event.getNewSelection().person);
    }

    //@@author emer7
    @Subscribe
    public void handlePersonEditedEvent(PersonEditedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Person newPerson = event.getNewPerson();
        name.setText(newPerson.getName().fullName);
        address.setText(newPerson.getAddress().value);
        reviews.getChildren().clear();
        newPerson.getReviews().forEach(review -> reviews.getChildren().add(new Label(review.toString())));
        loadPersonPage(event.getNewPerson());
    }
    //@@author
}
