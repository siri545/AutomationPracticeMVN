package libraries;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class Base {
    final static Logger logger = LogManager.getLogger(Base.class);

    public static ObjectMap objMap = new ObjectMap("objectmap.properties");

    public static WebDriver driver;
    public static GlobalSeleniumLibrary selLibrary;
    private String browserType;
    private String hubURL;

    @BeforeClass // before all tests start, this method runs only 1 time
    public void beforeAllTests() {
        selLibrary = new GlobalSeleniumLibrary();

        // test suite start time
        String tempTestStartTime = selLibrary.getCurrentTime();
        JavaPropertiesManager sessionTimeProp = new JavaPropertiesManager(
                "src/test/resources/sessionConfig.properties");
        sessionTimeProp.setProperty("sessionTime", tempTestStartTime);

        JavaPropertiesManager myProperty = new JavaPropertiesManager("src/test/resources/config.properties");

        String demoModePropValue = myProperty.readProperty("demoMode");
        if (demoModePropValue.contains("On")) {
            selLibrary.setDemoMode(true);
        }

        browserType = myProperty.readProperty("browserType");


        String remoteRun = myProperty.readProperty("isRemote");
        if (remoteRun.toLowerCase().contains("on")) {
            selLibrary.setIsRemote(true);
            hubURL = myProperty.readProperty("hubURL");
            logger.info("hubURL is: [" + hubURL + "]");
        }

        String headless = myProperty.readProperty("isHeadless");
        if (headless.toLowerCase().contains("on")) {
            selLibrary.setChromeHeadless(true);
        }

    }

    @AfterClass // this method runs only 1 time after all tests are completed.
    // after all the tests are completed, then try to close browsers
    // if there are open browsers left from selenium
    public void afterAllTests() {
        // selLibrary.closeBrowsers();
        logger.info("All the tests are completed...");

        JavaPropertiesManager configProp = new JavaPropertiesManager("src/test/resources/config.properties");
    }

    @BeforeMethod // this method runs/executes depending on how many tests you
    // have in your test class
    // before each test starts - setting up browser
    public void setup() {
        logger.info("Test started...");

        if (selLibrary.getIsRemote()) {
            if (browserType.toLowerCase().contains("chrome")) {
                driver = selLibrary.startRemoteBrowser(hubURL, GlobalSeleniumLibrary.Browser.CHROME);
            } else if (browserType.toLowerCase().contains("firefox")) {
                driver = selLibrary.startRemoteBrowser(hubURL, GlobalSeleniumLibrary.Browser.FIREFOX);
            } else if (browserType.toLowerCase().contains("ie")) {
                driver = selLibrary.startRemoteBrowser(hubURL, GlobalSeleniumLibrary.Browser.IE);
            } else if (browserType.toLowerCase().contains("edge")) {
                driver = selLibrary.startRemoteBrowser(hubURL, GlobalSeleniumLibrary.Browser.EDGE);
            } else {
                logger.info("Starting default browser as [Chrome].");
                driver = selLibrary.startRemoteBrowser(hubURL, GlobalSeleniumLibrary.Browser.CHROME);
            }
        } else {
            if (browserType.toLowerCase().contains("chrome")) {
                driver = selLibrary.startLocalBrowser(GlobalSeleniumLibrary.Browser.CHROME);
            } else if (browserType.toLowerCase().contains("firefox")) {
                driver = selLibrary.startLocalBrowser(GlobalSeleniumLibrary.Browser.FIREFOX);
            } else if (browserType.toLowerCase().contains("ie")) {
                driver = selLibrary.startLocalBrowser(GlobalSeleniumLibrary.Browser.IE);
            } else if (browserType.toLowerCase().contains("edge")) {
                driver = selLibrary.startLocalBrowser(GlobalSeleniumLibrary.Browser.EDGE);
            } else {
                logger.info("Starting default browser as [Chrome].");
                driver = selLibrary.startLocalBrowser(GlobalSeleniumLibrary.Browser.CHROME);
            }
        }
    }

    @AfterMethod // after each test is completed, cleaning up - close the browser
    // capture screenshots only if there is test failure.
    public void close(ITestResult result) {
        try {
            Thread.sleep(5 * 1000);

            if (ITestResult.FAILURE == result.getStatus()) {
                // test failed, call capture screenshot method
                selLibrary.captureScreenshot(result.getName(), "");
            }
            selLibrary.closeBrowsers();
            logger.info("Test is ended...");
        } catch (Exception e) {
            logger.error("Error: ", e);
            selLibrary.closeBrowsers();
        }
    }

}


