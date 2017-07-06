package selenium.tests;

import org.junit.*;
//import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import selenium.configReaders.*;
import selenium.framework.ParentFunctionWeb;
import selenium.helpers.Helper;
import selenium.tests.pages.LoginPage;
/**
 * Created by onur.yenici on 5.06.2017.
 */
public class LoginTest extends ParentFunctionWeb {

    private static FriendsConfig conf = new FriendsConfig();

    @BeforeClass
    public static void beforeClass() throws Exception {
        openBrowser(conf.getGoToWallet());
    }

    @AfterClass
    public static void afterClass() throws Exception {

        quitBrowser();
    }

    @Before
    public void beforeMethod() throws Exception {

        clearCookiesAndGoHomePage(conf.getGoToWallet());

    }

    @After
    public void afterMethod() throws Exception {

        try{
            Helper.SearchAndFindElement(By.className("js-error-out"),driver).findElements(By.tagName("Button")).get(0).click();

        }catch(Exception e){

        }

    }

    @Test()
    //@Parameters(name="C2980")
    public void successfullLogin() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        conf.getOzanWalletWebUserName(),
                        conf.getOzanWalletWebUserPassword(),
                        conf.getOzanWalletCountryCode()
                );

        Helper.clickObjectById("panel-content",driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));

    }

    //@Test
    public void loginConnTimeout()throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        wait(300000);

        String str=Helper.getTextByIdWithTag("lightbox-login","p",1,driver);

        Assert.assertTrue(str.contains("Connection timed out."));
    }

    @Test
    public void checkLoginTexts() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();

        String str=Helper.getTextByIdWithTag("lightbox-login","p",0,driver);

        Assert.assertTrue(str.contains("Good to see you. Come on in!"));
    }

    @Test
    public void unregisteredUserLogin() throws Exception{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        "7700178903",
                        conf.getOzanWalletWebUserPassword(),
                        conf.getOzanWalletCountryCode()
                );
        String str = Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.contains("COULD_NOT_AUTHENTICATE_USER"));

    }

    @Test
    public void wrongPasswordLogin() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        conf.getOzanWalletWebUserName(),
                        "wewe23",
                        conf.getOzanWalletCountryCode()
                );
        String str = Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.contains("COULD_NOT_AUTHENTICATE_USER"));
    }

    @Test
    public void checkForgot() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","abcd1234",driver);

        Helper.setObjectById("newPasswordConfirm","abcd1234",driver);

        Helper.clickObjectById("newPasswordSubmit",driver);

        String str = Helper.getTextByIdWithTag("lightbox-new-password-success","p",1,driver);

        Helper.clickObjectByIdWithTag("lightbox-new-password-success","button",0,driver);

        loginPage.clickForgot();

        Helper.clickObjectById("forgotSubmit",driver);

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword",conf.getOzanWalletWebUserPassword(),driver);

        Helper.setObjectById("newPasswordConfirm",conf.getOzanWalletWebUserPassword(),driver);

        Helper.clickObjectById("newPasswordSubmit",driver);

        Assert.assertTrue(str.contains("Your password has been changed."));
    }

    @Test
    public void checkForgotPasswordNotMatch() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","abcd1234",driver);

        Helper.setObjectById("newPasswordConfirm","abcd1235",driver);

        Helper.clickObjectById("newPasswordSubmit",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).getText();

        Assert.assertTrue(str.contains("Passwords do not match."));
    }

    @Test
    public void checkForgotWithWrongSMS() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode","190701",driver);

        String str = Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.contains("Could not verify SMS Verification Code"));
    }

    @Test
    public void checkForgotWithWrongSMSTimeOut() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        wait(180000);

        String str = Helper.getTextByClassWithTag("js-error-out","li",0,driver);

        Assert.assertTrue(str.contains("Could not find SMS Verification Code for this user"));
    }

    @Test
    public void checkForgotTexts() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        String str=Helper.getTextByIdWithTag("lightbox-forgot","p",0,driver);

        Assert.assertTrue(str.contains("Forgot your password?"));

        str=Helper.getTextByIdWithTag("lightbox-forgot","p",1,driver);

        Assert.assertTrue(str.contains("We'll send a code to reset your password"));

        str=Helper.getTextByIdWithTag("lightbox-forgot","p",3,driver);

        Assert.assertTrue(str.contains("Nevermind? Continue logging in"));
    }

    @Test
    public void checkNumberInput() throws Exception{

        LoginPage loginPage= new LoginPage(driver);

        loginPage.clickLogin();

        Helper.setObjectById("loginUsername","abcde",driver);

        String str=Helper.getTextById("loginUsername",driver);

        Assert.assertTrue(str.isEmpty());

    }

    @Test
    public void checkPasswordInput() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.SearchAndFindElement(By.xpath("//span[contains(text(),'Show')]"),driver).click();

        String str=Helper.SearchAndFindElement(By.id("signupPassword"),driver).getAttribute("type");

        Assert.assertTrue(str.equals("text"));

    }

    @Test
    public void checkPasswordLessThanEight() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","234wesd",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).findElements(By.tagName("span")).get(0).getAttribute("class");

        Assert.assertTrue(str.toLowerCase().contains("danger"));

    }

    @Test
    public void checkPasswordMoreThanFifty() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","234wesd111113wwwww111111111111111111111111111111113131331313131313113131331131313131313313131313",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).findElements(By.tagName("span")).get(0).getAttribute("class");

        Assert.assertTrue(str.toLowerCase().contains("danger"));

    }

    @Test
    public void checkPasswordNoCharacter() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","2344322432",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).findElements(By.tagName("span")).get(1).getAttribute("class");

        Assert.assertTrue(str.toLowerCase().contains("danger"));

    }

    @Test
    public void checkPasswordNoNumeric() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","wessdadadsssd",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).findElements(By.tagName("span")).get(2).getAttribute("class");

        Assert.assertTrue(str.toLowerCase().contains("danger"));

    }

    @Test
    public void checkPasswordNoSpecialCharacter() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin();
        loginPage.clickForgot();

        loginPage.doForgot(conf.getOzanWalletWebUserName(),
                conf.getOzanWalletCountryCode());

        Helper.setObjectById("passcode",conf.getOzanWalletCode(),driver);

        Helper.setObjectById("newPassword","234wesdwewç",driver);

        String str=Helper.SearchAndFindElement(By.xpath("//div[@role]"),driver).findElements(By.tagName("span")).get(3).getAttribute("class");

        Assert.assertTrue(str.toLowerCase().contains("danger"));

    }

    @Test()
    public void logoutTest() throws Exception {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin().
                doLogin(
                        conf.getOzanWalletWebUserName(),
                        conf.getOzanWalletWebUserPassword(),
                        conf.getOzanWalletCountryCode()
                );


        Helper.clickObjectByClassdWithTag("ozan-area","a",1,driver);

        if(!Helper.IsElementPresentByClassName("logout-top-text",driver))
            Assert.fail();

        Assert.assertTrue(driver.getCurrentUrl().contains("logout"));
    }
}
