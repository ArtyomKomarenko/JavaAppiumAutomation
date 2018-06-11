import lib.CoreTestCase;
import lib.ui.MainPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

    private MainPageObject MainPageObject;

    protected void setUp() throws Exception
    {
        super.setUp();

        MainPageObject = new MainPageObject(driver);
    }

    @Test
    public void testCompareSearchFieldPlaceholder()
    {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement search_element = MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search input",
                15
        );

        String search_text = search_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected text!",
                "Search…",
                search_text
        );
    }

    @Test
    public void testCancelSearch()
    {
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Star Wars",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Articles not found!",
                15
        );

        List<WebElement> articles = driver.findElementsById("org.wikipedia:id/page_list_item_container");

        Assert.assertTrue(
                "There is only one article!",
                articles.size() > 1
        );

        MainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        MainPageObject.waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );
    }

    @Test
    public void testCheckTextInArticles()
    {
        String searchWord = "Star Wars";

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchWord,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Articles not found!",
                15
        );

        List<WebElement> titles = driver.findElementsById("org.wikipedia:id/page_list_item_title");
        if (titles.size() == 0) {
            Assert.fail("Articles don't have title!");
        }

        for (WebElement title : titles) {
            String actualTitle = title.getAttribute("text");
            Assert.assertTrue(
                    "Title \"" + actualTitle +"\" does not contain search word \"" + searchWord + "\"!",
                    actualTitle.contains(searchWord)
            );
        }
    }

    @Test
    public void testSaveTwoArticlesAndDelete()
    {
        String search_word = "Star Wars";
        String folder_name = "My test list";
        int amount_of_search_results;

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        // Ищем первую статью
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        MainPageObject.waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by request " + search_word,
                15
        );

        amount_of_search_results = MainPageObject.getAmountOfElements(By.xpath(search_result_locator));
        Assert.assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        MainPageObject.waitForElementAndClick(
                By.xpath(search_result_locator + "[@index='0']"),
                "Cannot find first article in search result",
                5
        );

        String first_article_title = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Got it' tip overlay",
                5
        );

        MainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to set name of articles folder",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folder_name,
                "Cannot put text into articles folder input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.Button[@text='OK']"),
                "Cannot press OK button",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X",
                5
        );

        // Ищем вторую статью
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by request " + search_word,
                15
        );

        amount_of_search_results = MainPageObject.getAmountOfElements(By.xpath(search_result_locator));
        Assert.assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        MainPageObject.waitForElementAndClick(
                By.xpath(search_result_locator + "[@index='1']"),
                "Cannot find second article in search result",
                5
        );

        String second_article_title = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );

        String created_folder_listitem = "//*[@resource-id='org.wikipedia:id/item_container']//*[@text='" + folder_name + "']";
        MainPageObject.waitForElementAndClick(
                By.xpath(created_folder_listitem),
                "Cannot find created folder",
                5
        );

        MainPageObject.waitForElementNotPresent(
                By.xpath(created_folder_listitem),
                "Cannot add article to existing list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X",
                5
        );

        // Переходим в сохраненный список
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find  navigation button to My lists",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath(created_folder_listitem),
                "Cannot find created folder on My lists screen",
                5
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/item_title"),
                "Cannot find list title",
                15
        );

        MainPageObject.swipeElementToLeft(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + first_article_title + "']"),
                "Cannot find saved article " + first_article_title
        );

        MainPageObject.assertElementNotPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + first_article_title + "']"),
                "Cannot delete saved article " + first_article_title
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + second_article_title + "']"),
                "Cannot find saved article " + second_article_title,
                5
        );

        String last_article_title = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15
        );

        Assert.assertEquals(
                "Last article title '" + last_article_title + "' is different from expected title '" + second_article_title + "'",
                second_article_title,
                last_article_title
        );

    }

    @Test
    public void testCheckArticleTitleImmediately()
    {
        String search_word = "Star Wars";

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']"),
                "Cannot find anything by request " + search_word,
                15
        );

        MainPageObject.assertElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title"
        );
    }

    @Test
    public void testRestoreOrientationAfterFail()
    {
        if(driver.getOrientation() != ScreenOrientation.PORTRAIT) {
            Assert.fail("Device orientation is wrong: " + ScreenOrientation.PORTRAIT.value());
        }

        driver.rotate(ScreenOrientation.LANDSCAPE);

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        Assert.fail("Force fail");
    }


}
