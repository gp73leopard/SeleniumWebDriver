package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    // Обычный поиск элемента
    private WebElement user = driver.findElement(By.id(""));

    // Поиск элемента через аннотацию
    @FindBy(id = "")
    private WebElement password;

    // todo: остальные элементы страницы

    public void login(String user, String password) {
        // todo
    }
}
