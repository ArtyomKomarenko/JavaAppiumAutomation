package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

public class SearchPageObject extends MainPageObject {

    private static final String
            SEARCH_INIT_ELEMENT = "xpath://*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT = "xpath://*[contains(@text, 'Search…')]",
            SEARCH_CANCEL_BUTTON = "id:org.wikipedia:id/search_close_btn",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
            SEARCH_RESULT_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']",
            SEARCH_EMPTY_RESULT_ELEMENT = "xpath://*[@text='No results found']",
            SEARCH_RESULT_TITLE = "id:org.wikipedia:id/page_list_item_title",
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']/following-sibling::*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='{DESCRIPTION}']";

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    /* TEMPLATE METHODS */
    private static String getResultSearchElement(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultSearchElementByTitleAndDescription(String title, String description) {
        return SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL
                .replace("{TITLE}", title)
                .replace("{DESCRIPTION}", description);
    }
    /* TEMPLATE METHODS */

    public void initSearchInput()
    {
        this.waitForElementAndClick(
                SEARCH_INIT_ELEMENT,
                "Cannot find and click init element",
                5
        );

        this.waitForElementPresent(
                SEARCH_INIT_ELEMENT,
                "Cannot find search input after clicking search init element"
        );
    }

    public void waitForCancelButtonToAppear()
    {
        this.waitForElementPresent(
                SEARCH_CANCEL_BUTTON,
                "Cannot find search cancel button",
                5
        );
    }

    public void waitForCancelButtonToDisappear()
    {
        this.waitForElementNotPresent(
                SEARCH_CANCEL_BUTTON,
                "Search cancel button still present",
                10
        );
    }

    public void clickCancelButton()
    {
        this.waitForElementAndClick(
                SEARCH_CANCEL_BUTTON,
                "Cannot find and click search cancel button",
                5
        );
    }

    public void typeSearchLine(String search_line)
    {
        this.waitForElementAndSendKeys(
                SEARCH_INPUT,
                search_line,
                "Cannot find and type into search input",
                5
        );
    }

    public String getSearchInputPlaceholder()
    {
        WebElement search_input_element = waitForElementPresent(
                SEARCH_INPUT,
                "Cannot find search input",
                5
        );
        return search_input_element.getAttribute("text");
    }

    public void waitForSearchResult(String substring)
    {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementPresent(
                search_result_xpath,
                "Cannot find search result by substring " + substring
        );
    }

    public void waitForElementByTitleAndDescription(String title, String description)
    {
        String search_result_xpath = getResultSearchElementByTitleAndDescription(title, description);
        this.waitForElementPresent(
                search_result_xpath,
                "Cannot find search result with title '" + title + "' and description '" + description + "'"
        );
    }

    public void clickByArticleWithSubstring(String substring)
    {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(
                search_result_xpath,
                "Cannot find and click search result by substring " + substring,
                10
        );
    }

    public void clickByArticleByPositionInResults(int position)
    {
        this.waitForElementAndClick(
                SEARCH_RESULT_ELEMENT + "[@index='" + (position - 1) + "']",
                "Cannot find article on position " + position,
                5
        );
    }

    public int getAmountOfFoundArticles()
    {
        this.waitForElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Cannot find anything by request",
                15
        );
        return this.getAmountOfElements(SEARCH_RESULT_ELEMENT);
    }

    public List getFoundArticleElements()
    {
        this.waitForElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Cannot find anything by request",
                15
        );
        return getElements(SEARCH_RESULT_ELEMENT);
        //return driver.findElementsByXPath(SEARCH_RESULT_ELEMENT.split(Pattern.quote(":"), 2)[1]);
    }

    public void checkTextInArticles(List<WebElement> articles, String text)
    {
        for (WebElement article : articles) {
            WebElement title_element = article.findElement(By.id(SEARCH_RESULT_TITLE.split(Pattern.quote(":"), 2)[1]));
            String title = title_element.getAttribute("text");
            assertTrue(
                    "Title '" + title +"' does not contain search word '" + text + "'!",
                    title.contains(text)
            );
        }
    }

    public void waitForEmptySearchResultLabel()
    {
        this.waitForElementPresent(
                SEARCH_EMPTY_RESULT_ELEMENT,
                "Cannot find empty result element",
                15
        );
    }

    public void assertThereIsNoResultOfSearch()
    {
        this.assertElementNotPresent(SEARCH_RESULT_ELEMENT, "We supposed not to find any results");
    }
}
