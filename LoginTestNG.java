package EFT_SELENIUM.Tests.JetonWallet;

import EFT_SELENIUM.ConfigReaders.JetonWalletConfig;
import EFT_SELENIUM.Tests.JetonWallet.Page.WebLoginPage;
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

        openBrowser(conf.getJetonWalletUrl());

    }

    @BeforeMethod
    public void beforeMethod() throws Exception {

        clearCookiesAndGoHomePage(conf.getJetonWalletUrl());

    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {

        if (ITestResult.FAILURE == result.getStatus()) {
            String resultName = result.getName();
            new ScreenshotTaker(driver).takeScreenShot(resultName);
        }
        try{
            Helper.SearchAndFindElement(By.className("js-error-out"),driver).findElements(By.tagName("Button")).get(0).click();

        }catch(Exception e){

        }

    }

    @Test
    public void successfullyLoginTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);
        loginPage.
                clickLogin().
                doLogin();

        Helper.clickObjectById("panel-content",driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));

    }
}

