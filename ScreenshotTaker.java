package selenium.helpers;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import selenium.framework.ParentFunctionWeb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScreenshotTaker extends ParentFunctionWeb {

    public ScreenshotTaker(WebDriver testDriver) {

        this.driver = testDriver;
    }

    public File takeScreenShot(String resultName) throws Exception {

        File source = null;

        try {
            String timeStamp = new SimpleDateFormat("dd.MM.yy-HH.mm.ss").format(Calendar.getInstance().getTime());
            TakesScreenshot ts = (TakesScreenshot) driver;
            source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("./Screenshots/" + resultName + "-" + timeStamp + ".png"));
            System.out.println("SCREENSHOT TAKEN: " + resultName);
        } catch (Exception e) {

            System.out.println("exceptionTypes while taking screenshot " + e.getMessage());
        }

        return source;
    }

}
