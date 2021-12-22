package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class TicketsPage extends AbstractPage {

    // Кнопка для перехода в окно создания тикета
    private WebElement openTicket = driver.findElement(By.xpath("//a[@href='/tickets/submit/']"));

    // Определяем расположение элементов на странице создания тикета
    // Очередь
    @FindBy(id="id_queue")
    private WebElement queueSelect;
    @FindBy(css="option[value='1']")
    private WebElement queueSelectChoice;

    // Название тикета
    @FindBy(id="id_title")
    private WebElement summaryOfTheProblem;

    // Описание тикета
    @FindBy(css="textarea[class='form-control form-control']")
    private WebElement descriptionOfYourIssue;

    // Приоритет
    @FindBy(id="id_priority")
    private WebElement idPrioritySelect;
    @FindBy(css="option[value='3']")
    private WebElement idPrioritySelectChoice;

    // Календарь
    @FindBy(id="id_due_date")
    private WebElement dueOnSelect;
    @FindBy(css="span[class='ui-icon ui-icon-circle-triangle-e']")
    private WebElement btnNextMonth;
    @FindBy(xpath="//table[@class='ui-datepicker-calendar']//td//a")
    private List<WebElement> selectDate;

    // Поле почты
    @FindBy(css="input[name='submitter_email']")
    private WebElement eMail;

    // Кнопка создания тикета
    @FindBy(css="button[class='btn btn-primary btn-lg btn-block']")
    private WebElement btnCreateTicket;

    public void ticketCreate(String name, String text, int date, String mail){

        // Открываем окно создания тикета
        this.openTicket.click();

        // Инициализируем элементы страницы
        PageFactory.initElements(driver, this);

        // Сначала нажимаем на список, потом выбираем нужный элемент
        this.queueSelect.click();
        this.queueSelectChoice.click();

        // Задаем название тикета
        this.summaryOfTheProblem.sendKeys(name);

        // Задаем описание проблемы
        this.descriptionOfYourIssue.sendKeys(text);

        // Ставим приоритет
        this.idPrioritySelect.click();
        this.idPrioritySelectChoice.click();

        // Выбираем дедлайн для тикета
        this.dueOnSelect.click();
        this.btnNextMonth.click();
        this.selectDate.get(date).click();

        // Задаем почту
        this.eMail.sendKeys(mail);

        // Нажимаем на кнопку создания тикета
        this.btnCreateTicket.click();
    }
}
