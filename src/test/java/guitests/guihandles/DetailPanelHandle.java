package guitests.guihandles;

import java.net.URL;

import guitests.GuiRobot;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * A handler for the {@code DetailPanel} of the UI.
 */
public class DetailPanelHandle extends NodeHandle<Node> {

    public static final String BROWSER_ID = "#browser";

    //@@author emer7
    public static final String BROWSER_PANEL_ID = "#detailPanel";
    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    //@@author

    private boolean isWebViewLoaded = true;
    private URL lastRememberedUrl;

    //@@author emer7
    private final Label nameLabel;
    private final Label addressLabel;

    private Label oldNameLabel;
    private Label oldAddressLabel;
    //@@author

    public DetailPanelHandle(Node detailPanelNode) {
        super(detailPanelNode);

        WebView webView = getChildNode(BROWSER_ID);
        WebEngine engine = webView.getEngine();
        new GuiRobot().interact(() -> engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.RUNNING) {
                isWebViewLoaded = false;
            } else if (newState == Worker.State.SUCCEEDED) {
                isWebViewLoaded = true;
            }
        }));

        //@@author emer7
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        //@@author
    }

    /**
     * Returns the {@code URL} of the currently loaded page.
     */
    public URL getLoadedUrl() {
        return WebViewUtil.getLoadedUrl(getChildNode(BROWSER_ID));
    }

    /**
     * Remembers the {@code URL} of the currently loaded page.
     */
    public void rememberUrl() {
        lastRememberedUrl = getLoadedUrl();
    }

    /**
     * Returns true if the current {@code URL} is different from the value remembered by the most recent
     * {@code rememberUrl()} call.
     */
    public boolean isUrlChanged() {
        return !lastRememberedUrl.equals(getLoadedUrl());
    }

    /**
     * Returns true if the browser is done loading a page, or if this browser has yet to load any page.
     */
    public boolean isLoaded() {
        return isWebViewLoaded;
    }

    //@@author emer7
    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    /**
     * Remember the current Person details
     */
    public void rememberPersonDetail() {
        oldNameLabel = nameLabel;
        oldAddressLabel = addressLabel;
    }

    public boolean isDetailChanged() {
        return !(oldNameLabel.getText().equals(getName())
                && oldAddressLabel.getText().equals(getAddress()));
    }

    public boolean isFieldsEmpty() {
        return nameLabel.getText().equals("")
                && addressLabel.getText().equals("");
    }
    //@@author
}
