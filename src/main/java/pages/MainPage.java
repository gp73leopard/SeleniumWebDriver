package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPage extends AbstractPage {

    private Pattern pattern;

    @FindAll(@FindBy(css="div[class='tickettitle']"))
    List<WebElement> webElementList;

    public void itter(Pattern pattern) throws UnhandledAlertException {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
        int i = 0;

        try {
        // Число 25 взято из количества записей на одной странице в таблице
        while (i < 25) {
            PageFactory.initElements(driver, this);
            Matcher matcher = pattern.matcher(webElementList.get(i).getText());
            // Поиск проводится по названию тикета
            boolean found = matcher.matches();
            if (found == true) {
                System.out.println(webElementList.get(i).getText());
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
            WebDriverWait wait = new WebDriverWait(driver, 20);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        }
    }

    public void searhTicket(String title, String mail) throws Throwable{

        // Добавляем в фильтр поиск по ключевым словам
        Select selectFilter = new Select(driver.findElement(By.id("filterBuilderSelect")));
        selectFilter.selectByVisibleText("Keywords");

        // Задаем ключевое слово, либо их перечисление через OR
        WebElement keyWords = driver.findElement(By.id("id_query"));
        keyWords.sendKeys(title + " OR " + mail); // Поиск будет проведен по
        // почте и названию тикета. Также можно закомментировать строку, тогда будет произведен поиск по всей БД

        // Нажимаем на кнопку применить фильтр
        driver.findElement(By.xpath("//input[@class='btn btn-primary btn-sm']")).click();

        // Задаем паттерн поиска для названия тикета. Название тикета взяли из TicketPage
        // и скомбинировали с регулярным выражением
        pattern = Pattern.compile(".+"+title);

        try {
            // Проверка идет по активной кнопке переключения страниц на пагинаторе
            // Это необходимо для проверки всех строк таблицы. Ведь может быть ситуация,
            // что нужный нам тикет может быть на других страницах. Поиск будет идти до
            // тех пор пока не откроется нужный тикет или пока не будут просмотрены все строки
            while (driver.findElement(By.id("ticketTable_next")).isEnabled()) {
                try {
                    Thread.sleep(500); // Добавим небольшую задержку из-за быстрой прогрузки страниц
                    // Сначала проходит проверка
                    itter(pattern);
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

}
