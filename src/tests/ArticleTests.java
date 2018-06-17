package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ArticleTests extends CoreTestCase {

    @Test
    public void testCheckArticleTitleImmediately() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        String search_word = "Star Wars";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);

        SearchPageObject.clickByArticleWithSubstring(search_word);

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.getArticleTitleImmediately();
        } else {
            ArticlePageObject.getArticleTitleImmediately(search_word);
        }
    }
}
