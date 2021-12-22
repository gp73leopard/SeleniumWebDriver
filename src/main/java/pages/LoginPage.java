package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public void login(String user, String password){

        // Инициализируем элементы на странице
        PageFactory.initElements(driver, this);

        // Переходим на страницу логина
        this.btnLogIn.click();

        // Вводим имя пользвателя
        this.user.sendKeys(user);

        // Вводим пароль
        this.password.sendKeys(password);

        // Подтверждаем вход
        this.btnLogInConfirm.click();
    }

}
