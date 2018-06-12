package tests;

import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import org.junit.Test;
import org.openqa.selenium.ScreenOrientation;

public class ChangeAppConditionTests extends CoreTestCase {
    @Test
    public void testRestoreOrientationAfterFail()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        if(driver.getOrientation() != ScreenOrientation.PORTRAIT) {
            fail("Device orientation is wrong: " + ScreenOrientation.PORTRAIT.value());
        }

        this.rotateScreenLandscape();

        SearchPageObject.initSearchInput();

        fail("Force fail");
    }
}
