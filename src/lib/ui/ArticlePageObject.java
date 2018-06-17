package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

abstract public class ArticlePageObject extends MainPageObject {


    protected static String
            TITLE,
            FOOTER_ELEMENT,
            OPTIONS_BUTTON,
            OPTIONS_ADD_TO_MY_LIST_BUTTON,
            ADD_TO_MY_LIST_OVERLAY,
            MY_LIST_NAME_INPUT,
            MY_LIST_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON,
            FOLDER_BY_NAME_TPL;

    public static boolean first_launch;

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);

        first_launch = true;
    }

    /* TEMPLATE METHODS */
    private static String getTitleXpathByName(String title) {
        return TITLE.replace("{TITLE}", title);
    }

    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }
    /* TEMPLATE METHODS */

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(
                TITLE,
                "Cannot find article title on page",
                15
        );
    }

    public WebElement waitForTitleElement(String title) {
        String title_xpath = getTitleXpathByName(title);
        System.out.println(title_xpath);
        return this.waitForElementPresent(
                title_xpath,
                "Cannot find article title on page",
                15
        );
    }

    public String getArticleTitle() {
        WebElement title_element = waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            return title_element.getAttribute("text");
        } else {
            return title_element.getAttribute("name");
        }
    }

    public String getArticleTitle(String title) {
        WebElement title_element = waitForTitleElement(title);
        if (Platform.getInstance().isAndroid()) {
            return title_element.getAttribute("text");
        } else {
            return title_element.getAttribute("name");
        }
    }

    public String getArticleTitleImmediately() {
        this.assertElementPresent(
                TITLE,
                "Cannot find article title"
        );
        WebElement title_element = getElement(TITLE);
        if (Platform.getInstance().isAndroid()) {
            return title_element.getAttribute("text");
        } else {
            return title_element.getAttribute("name");
        }
    }

    public String getArticleTitleImmediately(String title) {
        String title_id = getTitleXpathByName(title);
        this.assertElementPresent(
                title_id,
                "Cannot find article title"
        );
        WebElement title_element = getElement(title_id);
        if (Platform.getInstance().isAndroid()) {
            return title_element.getAttribute("text");
        } else {
            return title_element.getAttribute("name");
        }
    }

    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid()) {
            this.swipeUpToFindElement(
                    FOOTER_ELEMENT,
                    "Cannot find the end of article",
                    40
            );
        } else {
            this.swipeUpTillElementAppear(
                    FOOTER_ELEMENT,
                    "Cannot find the end of article",
                    40
            );
        }
    }

    public void addArticleToMyList(String name_of_folder, boolean create_new_folder) {
        this.waitForTitleElement();

        this.waitForElementAndClick(
                OPTIONS_BUTTON,
                "Cannot find button to open article options",
                5
        );

        this.waitForElementAndClick(
                OPTIONS_ADD_TO_MY_LIST_BUTTON,
                "Cannot find option to add article to reading list",
                5
        );

        if (first_launch) {
            this.waitForElementAndClick(
                    ADD_TO_MY_LIST_OVERLAY,
                    "Cannot find 'Got it' tip overlay",
                    5
            );

            first_launch = false;
        }

        if (create_new_folder) {
            this.waitForElementAndClear(
                    MY_LIST_NAME_INPUT,
                    "Cannot find input to set name of articles folder",
                    5
            );

            this.waitForElementAndSendKeys(
                    MY_LIST_NAME_INPUT,
                    name_of_folder,
                    "Cannot put text into articles folder input",
                    5
            );

            this.waitForElementAndClick(
                    MY_LIST_OK_BUTTON,
                    "Cannot press OK button",
                    5
            );
        } else {
            String created_folder_listitem = getFolderXpathByName(name_of_folder);
            this.waitForElementAndClick(
                    created_folder_listitem,
                    "Cannot find created folder",
                    5
            );

            this.waitForElementNotPresent(
                    created_folder_listitem,
                    "Cannot add article to existing list",
                    5
            );
        }
    }

    public void addArticleToMyList(String name_of_folder) {
        addArticleToMyList(name_of_folder, false);
    }

    public void closeArticle() {
        this.waitForElementAndClick(
                CLOSE_ARTICLE_BUTTON,
                "Cannot close article, cannot find X link",
                5
        );
    }

    public void addArticleToMySaved() {
        this.waitForElementAndClick(
                OPTIONS_ADD_TO_MY_LIST_BUTTON,
                "Cannot find find option to add article to reading list",
                5
        );
    }
}
