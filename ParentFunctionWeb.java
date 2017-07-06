package selenium.framework;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import selenium.helpers.*;
import selenium.interfaces.IFileOperation;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class ParentFunctionWeb {

    public static WebDriver driver;
    protected static ThreadLocal<RemoteWebDriver> threadDriver = null;
    private static IFileOperation fileOp = new FileOperation();
    public static String platform = fileOp.getPlatform();
    public static String env = fileOp.getEnv();
    private static org.apache.log4j.Logger log = Logger.getLogger(ParentFunctionWeb.class);
    private static String SELENIUM_HUB_URL;
    //public String currentPath = this.getClass().getClassLoader().getResource("").getPath();

    public String getTestrailRun() {
        return testrailRun;
    }
    public void setTestrailRun(String testrailRun) {
        this.testrailRun = testrailRun;
    }
    private String testrailRun="";
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        TestRail apiCall = new TestRail();
        @Override
        protected void failed(Throwable e, Description description) {
            Collection<Annotation> list=  description.getAnnotations();
            Iterator<Annotation> itr=list.iterator();
            itr.next();
            if(itr.hasNext()){
                Annotation ant=itr.next();
                String str=ant.toString().split("=")[1];
                str=str.substring(0,str.length()-1);
                String [] caseids=new String[1];
                caseids[0]=str;
                try {
                    apiCall.setCaseResult(caseids,testrailRun,true);
                    apiCall.setJira(new ScreenshotTaker(driver).takeScreenShot(description.getMethodName()),caseids);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        @Override
        protected void succeeded(Description description){
            Collection<Annotation> list=  description.getAnnotations();
            Iterator<Annotation> itr=list.iterator();
            itr.next();
            if(itr.hasNext()){
                Annotation ant=itr.next();
                String str=ant.toString().split("=")[1];
                str=str.substring(0,str.length()-1);
                String [] caseids=new String[1];
                caseids[0]=str;
                try {
                    apiCall.setCaseResult(caseids,testrailRun,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.info("errortype: InterruptedException");
            e.printStackTrace();
        }
    }

    public static void openBrowser(String url) throws MalformedURLException {

        //JENKINS APPIUM STARTING TERMINAL COMMAND FOR IOS
        //appium -p 4723 -bp 5423 --session-override
        //JENKINS APPIUM STARTING TERMINAL COMMAND FOR ANDROID
        //appium -p 4724 -bp 5424 --session-override

        DesiredCapabilities dc = new DesiredCapabilities();
        SELENIUM_HUB_URL = "http://172.17.0.2:4444/wd/hub";

        String workingDir = System.getProperty("user.dir");
        System.out.println("Working path is: " + workingDir);

        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        System.out.println("Working OS is: " + OS);

        if (platform.equals("FIREFOX")) {

            FirefoxDriverManager.getInstance().setup();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            driver = new FirefoxDriver(capabilities);
            driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenResolution = new Dimension((int)
                    toolkit.getScreenSize().getWidth(), (int)
                    toolkit.getScreenSize().getHeight());
            driver.manage().window().setSize(screenResolution);
            driver.get(url);
            driver.manage().window().maximize();

        } else if (platform.equals("GRID")) {

            //System.setProperty("webdriver.gecko.driver", "" + workingDir + "/src/test/java/EFT_SELENIUM/Drivers/geckodriver");
            //System.setProperty("webdriver.chrome.driver", "" + workingDir + "/src/test/java/EFT_SELENIUM/Drivers/chromedriver");
            //TERMINAL COMMANDS MUST BE EXECUTE BEFORE
            //java -jar selenium-server-standalone-3.0.1.jar -role hub -port 4444
            //java -jar selenium-server-standalone-3.0.1.jar -role node  -hub http://localhost:4444/grid/register
            //java -jar selenium-server-standalone-2.53.1.jar -role node -hub http://localhost:4444/grid/register -maxSession 15 -platform browserName="chrome",version=ANY,platform=WINDOWS,maxInstances=15 -Dwebdriver.chrome.driver=/Users/ozgur.kaya/Testing/Selenium/src/test/java/EFT_SELENIUM/Drivers/chromedriver

            ChromeDriverManager.getInstance().setup();
            //DesiredCapabilities capability = DesiredCapabilities.firefox();
            DesiredCapabilities capability = DesiredCapabilities.chrome();
            capability.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), capability);

            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(url);

        } else if (platform.equals("Mobile")) {

            //https://github.com/bonigarcia/webdrivermanager
            ChromeDriverManager.getInstance().setup();

            Map<String, Object> deviceMetrics = new HashMap<String, Object>();
            deviceMetrics.put("width", 360);
            deviceMetrics.put("height", 640);
            deviceMetrics.put("pixelRatio", 4.0);

            Map<String, Object> mobileEmulation = new HashMap<String, Object>();
            mobileEmulation.put("deviceMetrics", deviceMetrics);
            mobileEmulation
                    .put("userAgent",
                            "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");

            Map<String, Object> browserOptions = new HashMap<String, Object>();
            browserOptions.put("mobileEmulation", mobileEmulation);

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(ChromeOptions.CAPABILITY, browserOptions);

            driver = new ChromeDriver(capabilities);

            /*Toolkit toolkit = Toolkit.getDefaultToolkit();
             Dimension screenResolution = new Dimension((int)
                    toolkit.getScreenSize().getWidth(), (int)
                    toolkit.getScreenSize().getHeight());*/
            driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            driver.manage().deleteAllCookies();

            Dimension d =new Dimension(360,640);
            driver.manage().window().setSize(d);
            driver.get(url);


        }
        else if (platform.equals("CHROME")) {

            //https://github.com/bonigarcia/webdrivermanager
            ChromeDriverManager.getInstance().setup();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

            driver = new ChromeDriver(capabilities);
            driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

            /*Toolkit toolkit = Toolkit.getDefaultToolkit();
             Dimension screenResolution = new Dimension((int)
                    toolkit.getScreenSize().getWidth(), (int)
                    toolkit.getScreenSize().getHeight());*/
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(url);


        }else if (platform.equals("IE")) {
            InternetExplorerDriverManager.getInstance().setup();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, true);
            capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);

            driver = new InternetExplorerDriver(capabilities);

            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(url);


        } else if (platform.equals("PHANTOMJS")) {
            //brew install phantomjs
            //brew install homebrew/science/openimageio
            PhantomJsDriverManager.getInstance().setup();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, true);
            capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);

            driver = new PhantomJSDriver(capabilities);

            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(url);


        }
    }

    protected static String returnEnvironment() {
        return env;
    }

    protected static String returnPlatform() {
        return platform;
    }

    protected static void clearCookiesAndGoHomePage(String url) throws Exception {

        driver.manage().deleteAllCookies();
        driver.get(url);

    }

    protected static void quitBrowser() {

        for (String winHandle : driver.getWindowHandles()) {
            try {
                driver.switchTo().window(winHandle);
                driver.close();
                driver.quit();
            } catch (Exception e) {
            }
        }
    }


}

