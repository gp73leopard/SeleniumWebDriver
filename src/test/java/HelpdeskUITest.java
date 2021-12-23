import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.LoginPage;
import pages.MainPage;
import pages.TicketsPage;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HelpdeskUITest{

    // Входные данные
    private WebDriver driver;
    private String title = "Ticket_name_" + UUID.randomUUID().toString(); // UUID гарантирует уникальность строки
    private String text = "Formulation of the problem is the first stage, basis, basis of scientific work. " +
            "Without such a foundation, the rest of the activity turns into a meaningless, haphazard " +
            "set of concepts, calculations and experiments. A systematic approach to analytical work is to " +
            "create continuity, when, on the basis of the accumulated information, a problem is solved and a " +
            "reserve is created for the future to continue research."; // Описание проблемы
    private int date = new Random().nextInt(28); // Число окончания тикета
    private String mail = title+"@gmail.com"; // Почта

    @Before
    public void setup() throws IOException {
        // Читаем конфигурационный файл в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        // Создание экземпляра драйвера
        driver = new ChromeDriver();
        // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().window().maximize();
        // Установим время ожидания для поиска элементов
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        // Установить созданный драйвер для поиска в веб-страницах
        AbstractPage.setDriver(driver);
    }

    @Test
    @Description("Создание тикета и проверка его существование в БД")
    @Owner("Александр")
    public void createTicketTest() throws Throwable{

        // Заходим на сайт
        driver.get(System.getProperty("site.url"));

        // Создаем тикет
        TicketsPage ticket = new TicketsPage();
        ticket.ticketCreate(this.title, this.text, this.date, mail);

        // Логинимся
        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"), System.getProperty("password"));

        // Поиск созданного тикета
        MainPage createdTicket = new MainPage();
        try {
            createdTicket.searhTicket(title, mail);
            createdTicket.compareTicket(text, title);
        }catch (Exception ex){
            throw new Exception("Ошибка поиска. Соответствий не найдено");
        }
        driver.close();
    }


}
