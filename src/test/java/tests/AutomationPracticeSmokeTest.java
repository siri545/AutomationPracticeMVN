package tests;

import libraries.Base;
import org.testng.annotations.Test;
import pages.HomePage;

public class AutomationPracticeSmokeTest extends Base {

    HomePage myHomePage = new HomePage();

    @Test
    public void smokeTest() {
        try {
            myHomePage.navigateToAutomationPracticeWebSite();
            myHomePage.verifyHomePageDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
