package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class ArticleTests extends CoreTestCase {

    @Test
    public void testCheckArticleTitleImmediately()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String search_word = "Star Wars";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);

        SearchPageObject.clickByArticleWithSubstring(search_word);

        ArticlePageObject.getArticleTitleImmediately();
    }
}
