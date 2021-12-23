package pages;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.io.IOException;

public class LoginPage extends AbstractPage {

    // Кнопка для перехода на страницу лоигна
    @FindBy(css="li[class='nav-item dropdown no-arrow']")
    private WebElement btnLogIn;

    // Определяем расположение элементов на странице логина
    // Имя пользователя
    @FindBy(id="username")
    private WebElement user;

    // Пароль
    @FindBy(id="password")
    private WebElement password;

    // Кнопка подтверждения входа
    @FindBy(css="input[class='btn btn-lg btn-primary btn-block']")
    private WebElement btnLogInConfirm;


    @Step("Логинимся")
    @Description("Вход на сайт под своим профилем")
    public void login(String user, String password) throws IOException {

        // Инициализируем элементы на странице
        PageFactory.initElements(driver, this);

        // Переходим на страницу логина
        this.btnLogIn.click();

        // Вводим имя пользвателя
        this.user.sendKeys(user);

        // Вводим пароль
        this.password.sendKeys(password);

        // Скриним окно и отправляем в отчет
        createAttachment();

        // Подтверждаем вход
        this.btnLogInConfirm.click();

    }

}
