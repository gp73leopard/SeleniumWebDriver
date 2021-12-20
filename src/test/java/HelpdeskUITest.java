import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.LoginPage;
import pages.MainPage;
import pages.TicketsPage;
import org.testng.Assert;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HelpdeskUITest extends Assert{
    private String text;
    private String mail;
    private String title;
    private WebDriver driver;

    @Before
    public void setup() throws IOException {
        // Читаем конфигурационный файл в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        // Создание экземпляра драйвера
        driver = new ChromeDriver();
        // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().window().maximize();
        // Установим время ожидания для поиска элементов
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Установить созданный драйвер для поиска в веб-страницах
        AbstractPage.setDriver(driver);
    }

    @Test
    public void createTicketTest()  throws Throwable, IOException{

        // Заходим на сайт
        driver.get(System.getProperty("site.url"));
        // Создаем тикет
        TicketsPage a = new TicketsPage();
        a.ticketCreate();
        // Получаем название тикета, почту и описание проблемы
        text = a.getText();
        title = a.getName();
        mail = a.getMail();

        // Логинимся
        driver.findElement(By.xpath("//a[@class='nav-link dropdown-toggle']")).click();
        // todo: чтение данных учетной записи пользователя из user.properties в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"), System.getProperty("password"));
        driver.findElement(By.xpath("//input[@class='btn btn-lg btn-primary btn-block']")).click();

        // Поиск созданного тикета
        MainPage searсh = new MainPage();
        try {
            searсh.searhTicket(title, mail);
            WebElement descript = driver.findElement(By.xpath("//td[@id='ticket-description']/p"));
            assertEquals(descript.getText(), text); // Сравнение по полю Description (описание)
            System.out.println("Тикет [" + title + "] найден. Мы произвели поиск по его названию" +
                    ", a также сравнили описание тикетов.");
        }catch (Exception ex){
            throw new Exception("Ошибка поиска. Соответствий не найдено");
        }
    }


}
