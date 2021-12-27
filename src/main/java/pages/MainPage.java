package pages;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPage extends AbstractPage {

    // Задаем паттерн и матчер для создания регулярного выражения, чтобы найти наш тикет
    private Pattern pattern;
    private Matcher matcher;

    // Определение элементов на главной странице
    // Фильтр для поиска
    @FindBy(id="filterBuilderSelect")
    private WebElement selectFilter;

    // Выбор фильтра поиска по ключевому слова
    @FindBy(id="filterBuilderSelect-Keywords")
    private WebElement selectFilterKeyWords;
    @FindBy(id="id_query")
    private WebElement keyWords;

    // Кнопка задания фильтра
    @FindBy(xpath="//input[@class='btn btn-primary btn-sm']")
    private WebElement btnConfirmFilter;

    // Список элементов, который будет хранить строки (тикеты)
    @FindAll(@FindBy(css="div[class='tickettitle']"))
    List<WebElement> webElementList;

    // Блок описания тикета. Будет найден на странице тикета, если он будет найден
    @FindBy(xpath="//td[@id='ticket-description']/p")
    private WebElement descriptionText;

    private WebDriverWait wait = new WebDriverWait(driver, 20);

    @Step("Пролистываем таблицу тикетов")
    @Description("Поиск осуществляется по ключевым словам")
    private void iterationTicketTable(Pattern pattern) throws UnhandledAlertException, IOException {

        // Зададим ожидание
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);

        // Пытаемся найти интересующий нас тикет
        int i = 0;
        try {
        // Число 25 взято из количества записей на одной странице в таблице
        while (i < 25) {
            // Инициализируем элементы на странице
            PageFactory.initElements(driver, this);
            matcher = pattern.matcher(webElementList.get(i).getText());
            // Поиск проводится по названию тикета
            boolean found = matcher.matches();
            if (found == true) {
                System.out.println(webElementList.get(i).getText());
                createAttachment(); // Скриним окно и отправляем в отчет
                webElementList.get(i).click();
                System.out.println("Тикет совпадает");
            } else {
                System.out.println(webElementList.get(i).getText());
                System.out.println("Нет совпадения");
                i++;
            }
        }
        // Во время поиска может быть вызвано диалоговое окно, этот блок нам поможет
        }catch (UnhandledAlertException ex){
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        }
    }
    @Step("Ищем тикет")
    @Description("Необходимо найти созданный тикет среди всех имеющихся в БД")
    public void searhTicket(String title, String mail) throws Throwable{

        // Инициализируем элементы на странице
        PageFactory.initElements(driver, this);

        // Добавляем в фильтр поиск по ключевым словам
        this.selectFilter.click();
        this.selectFilterKeyWords.click();

        // Задаем ключевое слово, либо их перечисление через OR
        this.keyWords.sendKeys(title + " OR " + mail); // Поиск будет проведен по
        // почте и названию тикета. Также можно закомментировать строку, тогда будет произведен поиск по всей БД

        // Нажимаем на кнопку применить фильтр
        this.btnConfirmFilter.click();

        // Задаем паттерн поиска для названия тикета. Название тикета взяли из TicketPage
        // и скомбинировали с регулярным выражением
        this.pattern = Pattern.compile(".+"+title);

        try {
            // Проверка идет по активной кнопке переключения страниц на пагинаторе
            // Это необходимо для проверки всех строк таблицы. Ведь может быть ситуация,
            // что нужный нам тикет может быть на других страницах. Поиск будет идти до
            // тех пор пока не откроется нужный тикет или пока не будут просмотрены все строки
            while (driver.findElement(By.id("ticketTable_next")).isEnabled()) {
                try {
                    Thread.sleep(500); // Добавим небольшую задержку из-за быстрой прогрузки страниц
                    // Сначала проходит проверка
                    iterationTicketTable(pattern);
                    // после переход на другую страницу
                    driver.findElement(By.id("ticketTable_next")).click();
                } catch (UnhandledAlertException ex) {
                    continue; // Игнорим диалоговое окно и идем искать дальше
                }
            }
        // Обработка ситуцации когда нет строк после фильтрации
        }catch (IndexOutOfBoundsException ex) {
            return; // Исключение сработает после нахождения тикета, потому делаем возврат
        }
        // Обработка ситуации, когда мы выбрали необходимый тикет, а поиск продолжился
        catch (NoSuchElementException ex) {
            return; // И это исключение сработает после нахождения тикета, потому делаем возврат
        }
    }
    @Step("Находим наш тикет")
    @Description("После нахождения созданного тикета обращаемся к полю описания тикета. Это будет финальная проверка на соответствие")
    public void compareTicket(String text, String title) throws IOException {

        // Инициализируем элементы на странице
        PageFactory.initElements(driver, this);

        createAttachment(); // Скриним окно и отправляем в отчет

        // Сравнение по полю Description (описание)
        this.descriptionText.getText().equals(text);
        System.out.println("Тикет [" + title + "] найден. Мы произвели поиск по его названию" +
                ", a также сравнили описание тикетов.");
    }
}
