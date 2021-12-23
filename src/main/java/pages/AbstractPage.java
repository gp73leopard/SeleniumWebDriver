package pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;


// Элементы общие для всех страниц
public abstract class AbstractPage extends Assert {

    protected static WebDriver driver;

    public static void setDriver(WebDriver webDriver) {


        driver = webDriver;
    }
}
