package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TicketsPage extends AbstractPage {

    // Входные данные
    private String name = "Ticket_name_" + UUID.randomUUID().toString(); // UUID гарантирует уникальность строки
    private String text = "Formulation of the problem is the first stage, basis, basis of scientific work. " +
            "Without such a foundation, the rest of the activity turns into a meaningless, haphazard " +
            "set of concepts, calculations and experiments. A systematic approach to analytical work is to " +
            "create continuity, when, on the basis of the accumulated information, a problem is solved and a " +
            "reserve is created for the future to continue research."; // Описание проблемы
    private int date = new Random().nextInt(28); // Число окончания тикета
    private String mail = name+"@gmail.com"; // Почта

    public void ticketCreate(){

        // Открываем окно создания тикета
        driver.findElement(By.xpath("//a[@href='/tickets/submit/']")).click();

        // Выбираем Django Helpdesk для очереди
        Select queueElement = new Select(driver.findElement(By.xpath("//select[@name='queue']")));
        queueElement.selectByVisibleText("Django Helpdesk");

        // Задаем название тикета
        WebElement summaryOfTheProblem = driver.findElement(By.id("id_title"));
        summaryOfTheProblem.sendKeys(name);

        // Задаем описание проблемы
        WebElement descriptionOfYourIssue = driver.findElement(By.xpath("//textarea[@class='form-control form-control']"));
        descriptionOfYourIssue.sendKeys(text);

        // Ставим приоритет
        Select idPriority = new Select(driver.findElement(By.id("id_priority")));
        idPriority.selectByVisibleText("3. Normal");

        // Открываем календарь и тыкаем следующий месяц
        WebElement dueOn = driver.findElement(By.id("id_due_date"));
        dueOn.click();
        driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();

        // Добавляем любое число, к котороу должен быть завершен наш тикет
        List<WebElement> selectDate = driver.findElements(By.xpath("//table[@class='ui-datepicker-calendar']//td//a"));
        selectDate.get(date).click();

        // Задаем почту
        WebElement email = driver.findElement(By.xpath("//input[@name='submitter_email']"));
        email.sendKeys(mail);

        // Нажимаем на кнопку создания тикета
        driver.findElement(By.xpath("//button[@class='btn btn-primary btn-lg btn-block']")).click();
    }

    // Добавляем геттеры по которым будем искать наш тикет
    public String getName(){return name;}
    public String getMail(){return mail;}
    public String getText(){return text;}

}
