package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractPage {

    // Обычный поиск элемента
    private WebElement user = driver.findElement(By.id("username"));

    // Поиск элемента через аннотацию
    @FindBy(id="password")
    private WebElement password;


    // todo: остальные элементы страницы

    public void login(String user, String password)  {

        this.user.sendKeys(user);
        PageFactory.initElements(driver, this);
        this.password.sendKeys(password);

    }

}
