package selenium.tests;

import org.junit.*;
import org.openqa.selenium.By;
import selenium.framework.ParentFunctionWeb;
import selenium.helpers.Helper;
import selenium.tests.pages.LoginPage;
/**
 * Created by onur.yenici on 5.06.2017.
 */
public class LoginTest extends ParentFunctionWeb {

    @BeforeClass
    public static void beforeClass() throws Exception {
        openBrowser();
    }

    @AfterClass
    public static void afterClass() throws Exception {

        quitBrowser();
    }

    @Before
    public void beforeMethod() throws Exception {

        clearCookiesAndGoHomePage();

    }

    @After
    public void afterMethod() throws Exception {

        try{
            Helper.SearchAndFindElement(By.className("js-error-out"),driver).findElements(By.tagName("Button")).get(0).click();

        }catch(Exception e){

        }

    }

    @Test()
    @Parameters(name="C3103")
    public void successfullLogin() throws Exception{

        LoginPage loginPage = new LoginPage(driver);
        loginPage.
                clickLogin().
                doLogin();

        Helper.clickObjectById("panel-content",driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"));

    }
}
