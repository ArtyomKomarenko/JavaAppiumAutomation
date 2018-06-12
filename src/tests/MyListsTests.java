package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {

    @Test
    public void testSaveTwoArticlesAndDelete()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        NavigationUI NavigationUI = new NavigationUI(driver);
        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);

        String search_word = "Star Wars";
        String folder_name = "My test list";
        int amount_of_search_results;

        // Ищем первую статью
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);

        amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );
        SearchPageObject.clickByArticleByPositionInResults(1);

        String first_article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addArticleToMyList(folder_name, true);
        ArticlePageObject.closeArticle();

        // Ищем вторую статью
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);

        amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "We found only one article, for test there should be two articles",
                amount_of_search_results > 1
        );

        SearchPageObject.clickByArticleByPositionInResults(2);


        String second_article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addArticleToMyList(folder_name);
        ArticlePageObject.closeArticle();

        // Переходим в сохраненный список
        NavigationUI.clickMyLists();

        MyListsPageObject.openFolderByName(folder_name);
        MyListsPageObject.waitForArticleToAppearByTitle(first_article_title);
        MyListsPageObject.swipeByArticleToDelete(first_article_title);
        MyListsPageObject.waitForArticleToDisappearByTitle(first_article_title);

        MyListsPageObject.openArticleByTitle(second_article_title);

        String last_article_title = ArticlePageObject.getArticleTitle();

        assertEquals(
                "Last article title '" + last_article_title + "' is different from expected title '" + second_article_title + "'",
                second_article_title,
                last_article_title
        );
    }
}
