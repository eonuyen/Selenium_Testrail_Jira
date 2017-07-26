package Tests;

import Tests.Page.WebLoginPage;
import main.framework.ParentFunctionWeb;
import main.helpers.Helper;
import main.helpers.ScreenshotTaker;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Created by onur.yenici on 9.01.2017.
 */
public class WebLoginTest extends ParentFunctionWeb {

    @AfterClass
    public void afterClass() throws Exception {

        quitBrowser();

    }

    @BeforeClass
    public void BeforeClass() throws Exception

    {

        openBrowser();

    }

    @BeforeMethod
    public void beforeMethod() throws Exception {

        clearCookiesAndGoHomePage();

    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {

        TestRail apiCall=new TestRail();
        String[] caseIds=result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName().split(",");

        if (ITestResult.FAILURE == result.getStatus()) {
            String resultName = result.getName();
            new ScreenshotTaker(driver).takeScreenShot(resultName);
            apiCall.setCaseResult(caseIds,testrailRun,true);
            apiCall.setJira(new ScreenshotTaker(driver).takeScreenShot(resultName),caseIds);

        }
        else
            apiCall.setCaseResult(caseIds,testrailRun,false);
     }

        try{
            Helper.SearchAndFindElement(By.className("js-error-out"),driver).findElements(By.tagName("Button")).get(0).click();

        }catch(Exception e){

        }

    }

    @Test(testName = "C3103")
    public void successfullyLoginTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);
        loginPage.
                clickLogin().
                doLogin();

        Helper.clickObjectById("panel-content",driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));

    }
}

