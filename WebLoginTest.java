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

    private JetonWalletConfig conf = new JetonWalletConfig(returnEnvironment(), returnPlatform());

    public WebLoginTest(WebDriver webDriver){
        this.driver = webDriver;
    }

    public WebLoginTest(){

    }

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
                doLogin(
                        conf.getJetonWalletlWebUserName(),
                        conf.getJetonWalletWebUserPassword(),
                        conf.getJetonWalletCode()
                );

        Helper.clickObjectById("panel-content",driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));

    }

    @Test()
    public void unSuccessfullyLoginTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        conf.getJetonWalletlWebUserName(),
                        "Asdfg1234!",
                        conf.getJetonWalletCode()
                );

        String str= Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.toLowerCase().contains("could not authenticate"));
    }

    @Test()
    public void invalidForgottenPasswordExistsUserTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);

        loginPage.
                clickLogin().
                doForgotUser(conf.getJetonWalletPhone());

        Helper.setObjectByIdWithTag("passCodeForgotId","190711","input",0,driver);

        Assert.assertTrue(Helper.getTextByIdWithTag("passCodeForgotId","p",0,driver).contains("Enter the code"));

        Helper.setObjectByIdWithTag("passCodeForgotId",conf.getJetonWalletCode(),"input",0,driver);

        Helper.clickObjectByIdWithTag("lightbox-passcode-valid-forgot-id","button",0,driver);

        loginPage.doForgotpasswd(conf.getJetonWalletPhone(),"190711");

        Assert.assertTrue(Helper.getTextByIdWithTag("passCodeForm","p",0,driver).contains("Enter the code"));

    }


    @Test()
    public void invalidForgottenPasswordDoesNotExistsUserTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);

        String user="5029999997";

        loginPage.
                clickLogin().
                doForgotUser(user);

        String str= Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.toLowerCase().contains("could not find"));

        new Actions(driver).sendKeys(Keys.ESCAPE).perform();

        Helper.clickObjectByXpath(".//*[@id='lightbox-forgot-id']/div/p/a",driver);

        loginPage. clickLogin().
                doForgotpasswd(user,conf.getJetonWalletCode());

        str= Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.toLowerCase().contains("could not find"));

    }

    @Test()
    public void validForgottenPasswordTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);

        loginPage.
                clickLogin().
                doForgotUser(conf.getJetonWalletPhone());

        Helper.setObjectByIdWithTag("passCodeForgotId",conf.getJetonWalletCode(),"input",0,driver);

        Assert.assertTrue(Helper.getTextByIdWithTag("lightbox-passcode-valid-forgot-id","p",1,driver).contains(conf.getJetonWalletlWebUserName()));

        Helper.clickObjectByIdWithTag("lightbox-passcode-valid-forgot-id","button",0,driver);

        loginPage.doForgotpasswd(conf.getJetonWalletPhone(),conf.getJetonWalletCode());

        Helper.setObjectById("newPassword",conf.getJetonWalletWebUserPassword(),driver);

        Helper.setObjectById("newPasswordConfirm",conf.getJetonWalletWebUserPassword(),driver);

        Helper.clickObjectByIdWithTag("resetPassword","button",0,driver);

        Assert.assertTrue(Helper.getTextById("lightbox-new-password-success",driver).toLowerCase().contains("password has been changed"));
    }

    @Test()
    public void logoutTest() throws Exception {

        WebLoginPage loginPage = new WebLoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        conf.getJetonWalletlWebUserName(),
                        conf.getJetonWalletWebUserPassword(),
                        conf.getJetonWalletCode()
                );


        Helper.clickObjectByClassdWithTag("logout-area","a",0,driver);

        if(!Helper.IsElementPresentById("animation_container",driver))
            Assert.fail();

        Assert.assertTrue(driver.getCurrentUrl().equals(conf.getJetonWalletUrl()));
    }

}

