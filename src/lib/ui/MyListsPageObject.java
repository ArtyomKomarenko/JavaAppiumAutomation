package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;

abstract public class MyListsPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            ARTICLE_BY_TITLE_TPL,
            SYNC_ARTICLES_OVERLAY,
            OVERLAY_CLOSE_BUTTON,
            SAVED_LIST_ELEMENT,
            SAVED_LIST_TITLE;

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATE METHODS */
    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getSavedArticleXpathByTitle(String article_title) {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", article_title);
    }
    /* TEMPLATE METHODS */

    public void openFolderByName(String name_of_folder) {
        String folder_name_xpath = getFolderXpathByName(name_of_folder);
        this.waitForElementAndClick(
                folder_name_xpath,
                "Cannot find folder by name " + name_of_folder,
                5
        );
    }

    public void openArticleByTitle(String article_title) {
        String article_title_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementAndClick(
                article_title_xpath,
                "Cannot find saved article by title " + article_title,
                5
        );
    }

    public void waitForArticleToAppearByTitle(String article_title) {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementPresent(
                article_xpath,
                "Cannot find saved article by title " + article_title,
                15
        );
    }

    public void waitForArticleToDisappearByTitle(String article_title) {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementNotPresent(
                article_xpath,
                "Saved article still present with title " + article_title,
                15
        );
    }

    public void swipeByArticleToDelete(String article_title) {
        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.swipeElementToLeft(
                article_xpath,
                "Cannot find saved article"
        );

        if (Platform.getInstance().isIOS()) {
            this.clickElementToTheRightUpperCorner(article_xpath, "Cannot find saved article");
        }

        this.waitForArticleToDisappearByTitle(article_title);
    }

    public void closeOverlay()
    {
        this.waitForElementAndClick(
                OVERLAY_CLOSE_BUTTON,
                "Cannot find and click overlay close button",
                10
        );

        this.waitForElementNotPresent(
                SYNC_ARTICLES_OVERLAY,
                "Overlay is still presented",
                5
        );
    }

    public String getTitleOfSearchResultByPosition(int position)
    {
        this.waitForElementPresent(SAVED_LIST_ELEMENT, "Articles not found");

        String locator;
        if(Platform.getInstance().isAndroid()) {
            locator = SAVED_LIST_TITLE;
        } else {
            locator = SAVED_LIST_ELEMENT;
        }

        return getTextFromElementByPosition(locator, position);

    }
}
