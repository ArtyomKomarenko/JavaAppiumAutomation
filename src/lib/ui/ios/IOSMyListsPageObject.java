package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class IOSMyListsPageObject extends MyListsPageObject {

    static {
        ARTICLE_BY_TITLE_TPL = "xpath://XCUIElementTypeLink[contains(@name,'{TITLE}')]";
        SYNC_ARTICLES_OVERLAY = "id:Sync your saved articles?";
        OVERLAY_CLOSE_BUTTON = "id:places auth close";
        SAVED_LIST_ELEMENT = "xpath://XCUIElementTypeLink";
    }

    public IOSMyListsPageObject(AppiumDriver driver) {
        super(driver);
    }
}
