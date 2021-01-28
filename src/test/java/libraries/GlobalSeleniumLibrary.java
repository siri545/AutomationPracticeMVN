package libraries;

import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

/***
 * This class have all Selenium/WebDriver related wrapper methods and features.
 * @author Siriporn Singkhamsirikul on 1/24/2021
 */
public class GlobalSeleniumLibrary extends Base {
    final static Logger logger = LogManager.getLogger(GlobalSeleniumLibrary.class);

    private boolean isDemoMode = false;
    private boolean isChromeHeadless = false;

    public boolean getChromeHeadless() {
        return isChromeHeadless;
    }

    public void setChromeHeadless(boolean _isChromeHeadless) {
        this.isChromeHeadless = _isChromeHeadless;
    }

    private boolean isRemote = false;

    public boolean getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(boolean _isRemote) {
        this.isRemote = _isRemote;
    }

    private boolean isBrowserTypeFirefox;
    public List<String> errorScreenshots;

    public void setDemoMode(boolean isDemoMode) {
        this.isDemoMode = isDemoMode;
    }

    enum Browser {
        IE, FIREFOX, CHROME, SAFARI, EDGE
    }

    public GlobalSeleniumLibrary() {

    }

    public GlobalSeleniumLibrary(WebDriver _driver) {
        driver = _driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver _driver) {
        try {
            if (_driver != null) {
                this.driver = _driver;
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    private WebDriver startRemoteIEBrowser(String hubURL) {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            InternetExplorerOptions IEOps = new InternetExplorerOptions();
            IEOps.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), IEOps);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startRemoteChromeBrowser(String hubURL) {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            ChromeOptions chromeOps = new ChromeOptions();
            if (isChromeHeadless) {
                chromeOps.setHeadless(true);
            }
            chromeOps.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), chromeOps);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startRemoteFirefoxBrowser(String hubURL) {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            FirefoxOptions firefoxOps = new FirefoxOptions();
            firefoxOps.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), firefoxOps);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startRemoteSafariBrowser(String hubURL) {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            SafariOptions safariOps = new SafariOptions();
            safariOps.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), safariOps);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startRemoteEdgeBrowser(String hubURL) {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            EdgeOptions edgeOps = new EdgeOptions();
            edgeOps.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), edgeOps);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public WebDriver startRemoteBrowser(String hubURL, Browser browser) {
        try {
            switch (browser) {
                case IE:
                    driver = startRemoteIEBrowser(hubURL);
                    break;

                case CHROME:
                    driver = startRemoteChromeBrowser(hubURL);
                    break;

                case FIREFOX:
                    driver = startRemoteFirefoxBrowser(hubURL);
                    break;

                case SAFARI:
                    driver = startRemoteSafariBrowser(hubURL);
                    break;

                case EDGE:
                    driver = startRemoteEdgeBrowser(hubURL);

                default:
                    logger.error("Currently we are not suporting this browser type!");
                    logger.error("Default browser set to Remote Chrome.");
                    driver = startRemoteChromeBrowser(hubURL);
                    break;
            }
            isRemote = true;
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public WebDriver startLocalBrowser(Browser browser) {
        try {
            switch (browser) {
                case IE:
                    driver = startIEBrowser();
                    break;

                case CHROME:
                    driver = startChromeBrowser();
                    break;

                case FIREFOX:
                    driver = startFirefoxBrowser();
                    break;

                case SAFARI:
                    driver = startSafariBrowser();
                    break;

                case EDGE:
                    driver = startEdgeBrowser();

                default:
                    logger.error("Currently we are not supporting this browser type!");
                    logger.error("Default browser set to Chrome.");
                    driver = startChromeBrowser();
                    break;
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    /***
     * This method starts/launch a Chrome Browser
     *
     * @return WebDriver
     */
    private WebDriver startChromeBrowser() {
        try {
            ChromeOptions chromeOps = new ChromeOptions();
            if (isChromeHeadless) {
                chromeOps.setHeadless(true);
            }
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(chromeOps);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            logger.debug("Maximize the browser");
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    /***
     * This method starts/launch a Firefox Browser
     *
     * @return WebDriver
     */
    private WebDriver startFirefoxBrowser() {
        try {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            logger.debug("maximize the browser");
            driver.manage().window().maximize();
            isBrowserTypeFirefox = true;
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startIEBrowser() {
        try {
            InternetExplorerOptions IEOps = new InternetExplorerOptions();
            IEOps.introduceFlakinessByIgnoringSecurityDomains();
            // IEOps.ignoreZoomSettings();
            driver = new InternetExplorerDriver(IEOps);
            // resetting the IE zoom to 100%
            driver.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.CONTROL, "0"));

            WebDriverManager.iedriver().arch32().setup();
            //driver = new InternetExplorerDriver();
            // driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            // logger.debug("maximize the browser");
            //driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startSafariBrowser() {
        try {
            driver = new SafariDriver();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            logger.debug("maximize the browser");
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    private WebDriver startEdgeBrowser() {
        try {
            EdgeOptions edgeOps = new EdgeOptions();
            driver = new EdgeDriver(edgeOps);

            WebDriverManager.edgedriver().setup();
            //driver = new EdgeDriver();
            //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            //logger.debug("maximize the browser");
            //driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", element);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void scrollUpDown(int pixels) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("scroll(0," + pixels + ")");
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void scrollRightLeft(int pixels) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("scroll(" + pixels + ",0)");
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void highlightElement(WebElement element) {
        if (isDemoMode == true) {
            try {
                for (int i = 0; i < 4; i++) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                            "color: red; border: 2px solid yellow");
                    customWait(0.5);
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
                    customWait(0.5);
                }
            } catch (Exception e) {
                logger.error("Error: ", e);
                assertTrue(false);
            }
        }
    }

    public WebElement waitForElementVisibility(By by) {
        WebElement elem = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return elem;
    }

    public WebElement waitForElementPresence(By by) {
        WebElement elem = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            elem = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return elem;
    }

    /***
     * Explicit wait for an element to be clickable
     *
     * @param by
     * @return WebElement
     */
    public WebElement waitForElementToBeClickable(By by) {
        WebElement elem = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            elem = wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return elem;
    }

    public void enterTxt(By by, String inputTxt) {
        try {
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(inputTxt);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void enterTxt(WebElement element, String inputTxt) {
        try {
            element.clear();
            element.sendKeys(inputTxt);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void clickElement(By by) {
        try {
            WebElement elem = driver.findElement(by);
            elem.click();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void selectDropDown(By by, String optionValue) {
        try {
            WebElement elem = driver.findElement(by);
            Select dropdown = new Select(elem);
            dropdown.selectByVisibleText(optionValue);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void selectDropDown(WebElement element, String optionValue) {
        try {
            Select dropdown = new Select(element);
            dropdown.selectByVisibleText(optionValue);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void customWait(double inSeconds) {
        try {
            Thread.sleep((long) (inSeconds * 1000));
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    /***
     * Method returns current timestamp
     *
     * @return String
     */
    public String getCurrentTime() {
        String finalTime = null;
        try {
            Date date = new Date();
            String tempTime = new Timestamp(date.getTime()).toString();
            logger.debug("Time: " + tempTime);
            finalTime = tempTime.replace("-", "").replace(":", "").replace(" ", "").replace(".", "");
            logger.info("getCurrentTime() ---> " + finalTime);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return finalTime;
    }

    /***
     * This method return total number of iframe if they exist, if not it will
     * return zero
     *
     * @return int
     */
    public int getAlliframes() {
        int totalIframe = 0;
        try {
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            totalIframe = iframes.size();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return totalIframe;
    }

    public WebDriver switchToIframeByIdex(int index) {
        try {
            driver = driver.switchTo().frame(index);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public WebDriver switchToBrowserByIndex(int index) {
        int totalBrowsers = 0;
        try {
            Set<String> setBrowsers = driver.getWindowHandles();
            totalBrowsers = setBrowsers.size();
            if (index < totalBrowsers) {
                List<String> listBrowsers = new ArrayList<String>();
                for (String browser : setBrowsers) {
                    listBrowsers.add(browser);
                }
                String windowName = listBrowsers.get(index);
                driver = driver.switchTo().window(windowName);
            } else {
                int tempBrowsers = index + 1;
                logger.info("There are only [" + totalBrowsers + "] open browser available, "
                        + "can't switch to browser number [" + tempBrowsers + "], that doesn't exit!");
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public WebDriver switchIframe(String htmlAttributeName, String htmlAttributeValue) {
        try {
            String value = "";
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement element : iframes) {
                if (htmlAttributeName.toLowerCase().contains("class")) {
                    value = element.getAttribute("class");
                    if (value.contains(htmlAttributeValue)) {
                        driver = driver.switchTo().frame(element);
                        break;
                    }
                } else if (htmlAttributeName.toLowerCase().contains("for")) {
                    value = element.getAttribute("for");
                    if (value.contains(htmlAttributeValue)) {
                        driver = driver.switchTo().frame(element);
                        break;
                    }
                } else if (htmlAttributeName.toLowerCase().contains("id")) {
                    value = element.getAttribute("id");
                    if (value.contains(htmlAttributeValue)) {
                        driver = driver.switchTo().frame(element);
                        break;
                    }
                } else if (htmlAttributeName.toLowerCase().contains("name")) {
                    value = element.getAttribute("name");
                    if (value.contains(htmlAttributeValue)) {
                        driver = driver.switchTo().frame(element);
                        break;
                    }
                } else if (htmlAttributeName.toLowerCase().contains("src")) {
                    value = element.getAttribute("src");
                    if (value.contains(htmlAttributeValue)) {
                        driver = driver.switchTo().frame(element);
                        break;
                    }
                } else {
                    logger.info("Error ---- ");
                    logger.info("The parameter html attirbute name [" + htmlAttributeName
                            + "], is not yet supported at this time.");
                    logger.info("Please check method [switchIframe()] under 'GlobalSeleniumLibrary' class.");
                }
                logger.info("attribute [" + htmlAttributeName + "], value [" + value + "]");
                logger.info("parameter attribute_value: " + htmlAttributeValue);
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return driver;
    }

    public void handleCheckBox(By by, boolean isCheck) {
        // scenarios
        // 1) user wants to check the check-box,
        // I) check-box is NOT checked by default ==>
        // II) check-box is already checked by default ==>

        // 2)user wants to un-check the check-box,
        // III) check-box is NOT checked by default ==>
        // IV) check-box is already checked by default ==>
        try {
            WebElement checkboxElem = driver.findElement(by);
            handleCheckBox(checkboxElem, isCheck);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public void handleCheckBox(WebElement element, boolean isCheck) {
        try {
            boolean checkboxStatus = element.isSelected();
            if (isCheck == true) {
                if (checkboxStatus == false) {
                    // scenario 1: ---> click the check-box
                    element.click();
                    customWait(0.5);
                } else {
                    // scenario 2: ---> do nothing
                }
            } else {
                if (checkboxStatus == false) {
                    // scenario 3: ---> do nothing
                } else {
                    // scenario 4: ---> click the check-box to un-check
                    element.click();
                    customWait(0.5);
                }
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

//    public String currentTime() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        Date date = new Date(System.currentTimeMillis());
//        //System.out.println(formatter.format(date));
//        //return formatter.format(date);
//        return formatter.format(date);
//    }
//
//    public WebElement waitForElementNotPresence(By by) {
//        WebElement elem = null;
//        WebDriverWait wait = new WebDriverWait(driver, 15);
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
//        return elem;
//    }

    public void captureScreenshot(String screenshotFileName, String filePath) {
        String finalScreenshotPath = null;
        try {
            String timeStamp = getCurrentTime();
            if (filePath.isEmpty()) {
                checkDirectory("target/screenshots/");
                finalScreenshotPath = "target/screenshots/" + screenshotFileName + "_" + timeStamp + ".png";
            } else {
                checkDirectory(filePath);
                finalScreenshotPath = filePath + screenshotFileName + "_" + timeStamp + ".png";
            }
            if (isRemote) {
                driver = new Augmenter().augment(driver);
            }
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(scrFile, new File(finalScreenshotPath));
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        String fullPath = getAbsulutePath(finalScreenshotPath);
        logger.info("Screenshot location: " + fullPath);
    }


    private void checkDirectory(String inputPath) {
        File file = new File(inputPath);
        String abPath = file.getAbsolutePath();
        File file2 = new File(abPath);
        try {
            if (!file2.exists()) {
                if (file2.mkdirs()) {
                    logger.info("Directories are created....");
                } else {
                    logger.info("Directories are NOT created...");
                }
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }


    private String getAbsulutePath(String inputPath) {
        String abPath = null;
        try {
            File file = new File(inputPath);
            abPath = file.getAbsolutePath();
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        return abPath;
    }

    public List<String> autoAttachErrorImgToEmail() {
        List<String> fileNames = new ArrayList<>();
        try {
            JavaPropertiesManager sessionTimeProp = new JavaPropertiesManager("src/test/resources/sessionConfig.properties");
            String startTimeStamp = sessionTimeProp.readProperty("sessionTime");
            //String imgTimeStamp = null;
            //long testStatTime = Long.parseLong(startTimeStamp);
            File file = new File("target/screenshots");
            if (file.isDirectory()) {
                if (file.list().length > 0) {
                    File[] screenShotFiles = file.listFiles();
                    for (int i = 0; i < screenShotFiles.length; i++) {
                        // checking if file is a file, not a folder
                        if (screenShotFiles[i].isFile()) {
                            String eachFileName = screenShotFiles[i].getName();
                            // logger.info("Image file name: " + eachFileName);
                            int indexOfUnderScore = eachFileName.lastIndexOf("_");
                            int indexOfLast = eachFileName.length() - 4;
                            String imgTemTimeStamp = eachFileName.substring(indexOfUnderScore + 1, indexOfLast);
                            //logger.info("data of image time stamps......");
                            //logger.info(imgTemTimeStamp);
                            long actualStartTimeStamp = Long.parseLong(startTimeStamp.substring(0, 14));
                            long actualImgTimeStamp = Long.parseLong(imgTemTimeStamp.substring(0, 14));
                            if (actualImgTimeStamp > actualStartTimeStamp) {
                                fileNames.add("target/screenshots/" + eachFileName);
                                logger.info("Screenshots attaching: " + eachFileName);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        errorScreenshots = fileNames;
        return fileNames;
    }


    public void uploadFile(String filePath, By by) {
        String absulateFilePath = null;
        try {
            File file = new File(filePath);
            absulateFilePath = file.getAbsolutePath();
            WebElement fileUpload = driver.findElement(by);
            if (isRemote) {
                ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            }
            fileUpload.sendKeys(absulateFilePath);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
        logger.info("file uploaded: " + absulateFilePath);
    }

    public void closeBrowsers() {
        if (driver != null) {
            driver.close();
            if (isBrowserTypeFirefox == true) {
                // driver.quit();
            } else {
                //driver.quit();
            }
        }
    }


    public static void main(String[] args) {
        GlobalSeleniumLibrary myObject = new GlobalSeleniumLibrary();
        myObject.autoAttachErrorImgToEmail();

        // myObject.getCurrentTime();
        // difference between mkdir() vs mkdirs()

        // let's assume using mkdir() --> result: only "c:/abc" folder would be created
        // myObject.checkDirectory("c:/abc/aaa/a1/2/1");

        // let's assume using mkdirs() --> result: it creates full folder and
        // sub-folders like 'c:/abc/aaa/a1/2/1'
        // myObject.checkDirectory("c:/abc/aaa/a1/2/1");
    }

}

// check if folder path is exist, not create the path
//
//            String tempInput = screnshotFilePath;
//            String folders = tempInput.substring(0, tempInput.lastIndexOf("/") + 1);
//            String imageName = tempInput.substring(
//                    tempInput.lastIndexOf("/") + 1, (tempInput.length()));
//
//            File filePath = new File(folders);
//            if (!filePath.exists()) {
//                filePath.mkdirs();
//            }
