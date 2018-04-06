package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.ui.DetailPanel.DEFAULT_PAGE;
import static seedu.address.ui.UiPart.FXML_FILE_FOLDER;
import static seedu.address.ui.testutil.GuiTestAssert.assertPanelDisplaysPerson;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.DetailPanelHandle;
import guitests.guihandles.PersonCardHandle;
import seedu.address.MainApp;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;

public class DetailPanelTest extends GuiUnitTest {
    private PersonCard selectionChangedPersonCardStub;
    private PersonPanelSelectionChangedEvent selectionChangedEventStub;

    private DetailPanel detailPanel;
    private DetailPanelHandle detailPanelHandle;

    @Before
    public void setUp() {
        selectionChangedPersonCardStub = new PersonCard(ALICE, 0);
        selectionChangedEventStub = new PersonPanelSelectionChangedEvent(selectionChangedPersonCardStub);

        guiRobot.interact(() -> detailPanel = new DetailPanel());
        uiPartRule.setUiPart(detailPanel);

        detailPanelHandle = new DetailPanelHandle(detailPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, detailPanelHandle.getLoadedUrl());

        // associated web page of a person & person card detail
        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL("https://calendar.google.com/calendar/embed?src="
                + "testCalendarId&ctz=Asia%2FSingapore");

        waitUntilBrowserLoaded(detailPanelHandle);
        assertEquals(expectedPersonUrl, detailPanelHandle.getLoadedUrl());

        //@@author emer7
        detailPanelHandle = new DetailPanelHandle(detailPanel.getRoot());
        assertPanelDisplay(new PersonCardHandle(selectionChangedPersonCardStub.getRoot()), detailPanelHandle);
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertPanelDisplay(PersonCardHandle expectedPersonCard, DetailPanelHandle detailPanelHandle) {
        guiRobot.pauseForHuman();
        assertPanelDisplaysPerson(expectedPersonCard, detailPanelHandle);
        //@@author
    }
}
