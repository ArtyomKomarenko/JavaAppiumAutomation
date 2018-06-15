package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class CoreTestCase extends TestCase {

    private static final String PLATFORM_IOS = "ios";
    private static final String PLATFORM_ANDROID = "android";

    protected AppiumDriver driver;
    private static String AppiumURL = "http://127.0.0.1:4723/wd/hub";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        DesiredCapabilities capabilities = this.getCapabilitiesByPlatformEnv();

        driver = getDriverByPlatform(new URL(AppiumURL), capabilities);
        this.rotateScreenPortrait();
    }

    @Override
    protected void tearDown() throws Exception
    {
        driver.quit();

        super.tearDown();
    }

    protected void rotateScreenPortrait()
    {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    protected void rotateScreenLandscape()
    {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    protected void backgroundApp(int seconds)
    {
        driver.runAppInBackground(seconds);
    }

    private DesiredCapabilities getCapabilitiesByPlatformEnv() throws Exception
    {
        String platform = System.getenv("PLATFORM");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        switch (platform) {
            case PLATFORM_ANDROID:
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("deviceName", "AndroidTestDevice");
                capabilities.setCapability("platformVersion", "6.0");
                capabilities.setCapability("automationName", "Appium");
                capabilities.setCapability("appPackage", "org.wikipedia");
                capabilities.setCapability("appActivity", ".main.MainActivity");
                capabilities.setCapability("app", "/Users/ex/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");
                break;
            case PLATFORM_IOS:
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("deviceName", "iPhone SE");
                capabilities.setCapability("platformVersion", "11.4");
                capabilities.setCapability("app", "/Users/ex/Desktop/JavaAppiumAutomation/apks/wikipedia.app");
                break;
            default:
                throw new Exception("Cannot get run platform from env variable. Platform value " + platform);
        }
        capabilities.setCapability("orientation", "PORTRAIT");

        return capabilities;
    }

    private AppiumDriver getDriverByPlatform(URL url, DesiredCapabilities caps) throws Exception
    {
        String platform = System.getenv("PLATFORM");
        AppiumDriver driver;

        switch (platform) {
            case PLATFORM_ANDROID:
                driver = new AndroidDriver(url, caps);
                break;
            case PLATFORM_IOS:
                driver = new IOSDriver(url, caps);
                break;
            default:
                throw new Exception("Cannot get run platform from env variable. Platform value " + platform);
        }

        return driver;
    }
}
