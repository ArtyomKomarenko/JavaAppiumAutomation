package tests;

import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchTests extends CoreTestCase {

    @Test
    public void testCompareSearchFieldPlaceholder()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        String search_text = SearchPageObject.getSearchInputPlaceholder();

        assertEquals(
                "We see unexpected text!",
                "Search…",
                search_text
        );
    }

    @Test
    public void testCancelSearch()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Star Wars");

        int amount_of_found_articles = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "There is only one article!",
                amount_of_found_articles > 1
        );

        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelButton();
        SearchPageObject.clickCancelButton();
        SearchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testCheckTextInArticles()
    {
        String searchWord = "Star Wars";
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(searchWord);

        List<WebElement> articles = SearchPageObject.getFoundArticleElements();

        SearchPageObject.checkTextInArticles(articles, searchWord);
    }

    @Test
    public void testCheckFirstThreeSearchResults()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Deadpool");

        int amount_of_found_articles = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "There are too few search results for test, should be at least 3",
                amount_of_found_articles >= 3
        );

        SearchPageObject.waitForElementByTitleAndDescription("Deadpool", "Character appearing in Marvel Comics");
        SearchPageObject.waitForElementByTitleAndDescription("Deadpool 2", "2018 film directed by David Leitch");
        SearchPageObject.waitForElementByTitleAndDescription("Deadpool (film)", "2016 superhero film produced by Marvel Entertainment");
    }
}
