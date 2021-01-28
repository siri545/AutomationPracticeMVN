package pages;

import libraries.Base;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;

public class HomePage extends Base {
    final static Logger logger = LogManager.getLogger(HomePage.class);

    public static String AutomationPracticeURL = "http://automationpractice.com/";
    String ExpectedTitle = "My Store";

    public void navigateToAutomationPracticeWebSite() {
        driver.get(AutomationPracticeURL);
        String ActualTitle = driver.getTitle();
        assertThat(ActualTitle, equalToIgnoringWhiteSpace(ExpectedTitle));
        logger.info("\n" + "Actual Title is: " + ActualTitle + "\n" + "Expected Title is: " + ExpectedTitle);
    }

    public void verifyHomePageDisplay() {
        try {
            selLibrary.waitForElementVisibility(objMap.getLocator("HomePage.Logo"));
            selLibrary.waitForElementVisibility(objMap.getLocator("HomePage.Header"));
            selLibrary.waitForElementVisibility(objMap.getLocator("HomePage.SearchBox"));
            selLibrary.waitForElementVisibility(objMap.getLocator("HomePage.CartLink"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
