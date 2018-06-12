package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    public static final String
            TITLE = "org.wikipedia:id/view_page_title_text",
            FOOTER_ELEMENT = "//*[@text='View page in browser']",
            OPTIONS_BUTTON = "//android.widget.ImageView[@content-desc='More options']",
            OPTIONS_ADD_TO_MY_LIST_BUTTON = "//*[@text='Add to reading list']",
            ADD_TO_MY_LIST_OVERLAY = "org.wikipedia:id/onboarding_button",
            MY_LIST_NAME_INPUT = "org.wikipedia:id/text_input",
            MY_LIST_OK_BUTTON = "//*[@text='OK']",
            CLOSE_ARTICLE_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']",
            FOLDER_BY_NAME_TPL = "//*[@resource-id='org.wikipedia:id/item_container']//*[@text='{FOLDER_NAME}']";

    public static boolean first_launch;

    public ArticlePageObject(AppiumDriver driver)
    {
        super(driver);

        first_launch = true;
    }

    /* TEMPLATE METHODS */
    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }
    /* TEMPLATE METHODS */

    public WebElement waitForTitleElement()
    {
        return this.waitForElementPresent(
                By.id(TITLE),
                "Cannot find article title on page",
                15
        );
    }

    public String getArticleTitle()
    {
        WebElement title_element = waitForTitleElement();
        return title_element.getAttribute("text");
    }

    public String getArticleTitleImmediately()
    {
        this.assertElementPresent(
                By.id(TITLE),
                "Cannot find article title"
        );
        WebElement title_element = driver.findElement(By.id(TITLE));
        return title_element.getAttribute("text");
    }

    public void swipeToFooter()
    {
        this.swipeUpToFindElement(
                By.xpath(FOOTER_ELEMENT),
                "Cannot find the end of article",
                20
        );
    }

    public void addArticleToMyList(String name_of_folder, boolean create_new_folder)
    {
        this.waitForElementAndClick(
                By.xpath(OPTIONS_BUTTON),
                "Cannot find button to open article options",
                5
        );

        this.waitForElementAndClick(
                By.xpath(OPTIONS_ADD_TO_MY_LIST_BUTTON),
                "Cannot find option to add article to reading list",
                5
        );

        if(first_launch) {
            this.waitForElementAndClick(
                    By.id(ADD_TO_MY_LIST_OVERLAY),
                    "Cannot find 'Got it' tip overlay",
                    5
            );

            first_launch = false;
        }

        if(create_new_folder) {
            this.waitForElementAndClear(
                    By.id(MY_LIST_NAME_INPUT),
                    "Cannot find input to set name of articles folder",
                    5
            );

            this.waitForElementAndSendKeys(
                    By.id(MY_LIST_NAME_INPUT),
                    name_of_folder,
                    "Cannot put text into articles folder input",
                    5
            );

            this.waitForElementAndClick(
                    By.xpath(MY_LIST_OK_BUTTON),
                    "Cannot press OK button",
                    5
            );
        } else {
            String created_folder_listitem = getFolderXpathByName(name_of_folder);
            this.waitForElementAndClick(
                    By.xpath(created_folder_listitem),
                    "Cannot find created folder",
                    5
            );

            this.waitForElementNotPresent(
                    By.xpath(created_folder_listitem),
                    "Cannot add article to existing list",
                    5
            );
        }
    }

    public void addArticleToMyList(String name_of_folder)
    {
        addArticleToMyList(name_of_folder, false);
    }

    public void closeArticle()
    {
        this.waitForElementAndClick(
                By.xpath(CLOSE_ARTICLE_BUTTON),
                "Cannot close article, cannot find X link",
                5
        );
    }
}
