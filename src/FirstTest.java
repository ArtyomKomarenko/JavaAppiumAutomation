import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/ex/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");
        capabilities.setCapability("orientation", "PORTRAIT");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testCompareSearchFieldPlaceholder()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement search_element = waitForElementPresent(
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
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Star Wars",
                "Cannot find search input",
                5
        );

         waitForElementPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Articles not found!",
                15
        );

        List<WebElement> articles = driver.findElementsById("org.wikipedia:id/page_list_item_container");

        Assert.assertTrue(
                "There is only one article!",
                articles.size() > 1
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );
    }

    @Test
    public void testCheckTextInArticles()
    {
        String searchWord = "Star Wars";

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchWord,
                "Cannot find search input",
                5
        );

        waitForElementPresent(
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

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        // Ищем первую статью
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by request " + search_word,
                15
        );

        amount_of_search_results = getAmountOfElements(By.xpath(search_result_locator));
        Assert.assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        waitForElementAndClick(
                By.xpath(search_result_locator + "[@index='0']"),
                "Cannot find first article in search result",
                5
        );

        String first_article_title = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Got it' tip overlay",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to set name of articles folder",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folder_name,
                "Cannot put text into articles folder input",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.Button[@text='OK']"),
                "Cannot press OK button",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X",
                5
        );

        // Ищем вторую статью
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by request " + search_word,
                15
        );

        amount_of_search_results = getAmountOfElements(By.xpath(search_result_locator));
        Assert.assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        waitForElementAndClick(
                By.xpath(search_result_locator + "[@index='1']"),
                "Cannot find second article in search result",
                5
        );

        String second_article_title = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                15
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );

        String created_folder_listitem = "//*[@resource-id='org.wikipedia:id/item_container']//*[@text='" + folder_name + "']";
        waitForElementAndClick(
                By.xpath(created_folder_listitem),
                "Cannot find created folder",
                5
        );

        waitForElementNotPresent(
                By.xpath(created_folder_listitem),
                "Cannot add article to existing list",
                5
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X",
                5
        );

        // Переходим в сохраненный список
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find  navigation button to My lists",
                5
        );

        waitForElementAndClick(
                By.xpath(created_folder_listitem),
                "Cannot find created folder on My lists screen",
                5
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/item_title"),
                "Cannot find list title",
                15
        );

        swipeElementToLeft(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + first_article_title + "']"),
                "Cannot find saved article " + first_article_title
        );

        assertElementNotPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + first_article_title + "']"),
                "Cannot delete saved article " + first_article_title
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + second_article_title + "']"),
                "Cannot find saved article " + second_article_title,
                5
        );

        String last_article_title = waitForElementAndGetAttribute(
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

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']"),
                "Cannot find anything by request " + search_word,
                15
        );

        assertElementPresent(
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

        waitForElementPresent(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        Assert.fail("Force fail");
    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_message)
    {
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private void swipeUp(int timeOfSwipe)
    {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        action
                .press(x, startY)
                .waitAction(timeOfSwipe)
                .moveTo(x, endY)
                .release()
                .perform();
    }

    private void swipeUpQuick()
    {
        swipeUp(200);
    }

    private void swipeUpToFindElements(By by, String error_message, int max_swipes)
    {
        int already_swiped = 0;
        while(driver.findElements(by).size() == 0){

            if(already_swiped > max_swipes) {
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
                return;
            }

            swipeUpQuick();
            ++already_swiped;
        }
    }

    private void swipeElementToLeft(By by, String error_message)
    {
        WebElement element = waitForElementPresent(by, error_message, 10);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(300)
                .moveTo(left_x, middle_y)
                .release()
                .perform();

    }

    private int getAmountOfElements(By by)
    {
        List elements = driver.findElements(by);
        return elements.size();
    }

    private void assertElementNotPresent(By by, String error_message)
    {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element '"+ by.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + ' ' + error_message);
        }
    }

    private void assertElementPresent(By by, String error_message)
    {
        int amount_of_elements = getAmountOfElements(by);
        if (0 == amount_of_elements) {
            String default_message = "An element '"+ by.toString() + "' supposed to be present";
            throw new AssertionError(default_message + ' ' + error_message);
        }
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }
}
