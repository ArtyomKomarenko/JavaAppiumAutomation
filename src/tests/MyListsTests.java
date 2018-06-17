package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {

    private static final String FOLDER_NAME = "My test list";
    private static final String SEARCH_WORD = "Star Wars";

    @Test
    public void testSaveTwoArticlesAndDelete() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        MyListsPageObject MyListsPageObject = MyListsPageObjectFactory.get(driver);

        String first_article_title;
        String second_article_title;
        int amount_of_search_results;

        // Ищем первую статью
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(SEARCH_WORD);

        amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        first_article_title = SearchPageObject.getTitleOfSearchResultByPosition(1);

        SearchPageObject.clickByArticleByPositionInResults(1);

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(FOLDER_NAME, true);
        } else {
            ArticlePageObject.addArticleToMySaved();
        }

        ArticlePageObject.closeArticle();

        // Ищем вторую статью
        SearchPageObject.initSearchInput();

        if (Platform.getInstance().isAndroid()) {
            SearchPageObject.typeSearchLine(SEARCH_WORD);

            amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
            assertTrue(
                    "We found only one article, for test there should be two articles",
                    amount_of_search_results > 1
            );
        }

        second_article_title = SearchPageObject.getTitleOfSearchResultByPosition(2);

        SearchPageObject.clickByArticleByPositionInResults(2);

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(FOLDER_NAME);
        } else {
            ArticlePageObject.addArticleToMySaved();
        }

        ArticlePageObject.closeArticle();

        // Переходим в сохраненный список
        NavigationUI.clickMyLists();

        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.openFolderByName(FOLDER_NAME);
        }

        if (Platform.getInstance().isIOS()) {
            MyListsPageObject.closeOverlay();
        }

        MyListsPageObject.waitForArticleToAppearByTitle(first_article_title);
        MyListsPageObject.swipeByArticleToDelete(first_article_title);
        MyListsPageObject.waitForArticleToDisappearByTitle(first_article_title);

        String last_article_title = MyListsPageObject.getTitleOfSearchResultByPosition(1);

        assertEquals(
                "Last article title '" + last_article_title + "' is different from expected title '" + second_article_title + "'",
                second_article_title,
                last_article_title
        );
    }
}
