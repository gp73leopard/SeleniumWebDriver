package pages;

import io.qameta.allure.Attachment;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Элементы общие для всех страниц
public abstract class AbstractPage {

    // Создаем вложение в виде скриншота
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] createAttachment() throws IOException {
        return captureScreenShot();
    }
    // Получаем наш скрин в виде массива байтов
    private byte[] captureScreenShot() throws IOException {
        BufferedImage image = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver).getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    protected static WebDriver driver;

    public static void setDriver(WebDriver webDriver) {

        driver = webDriver;
    }
}
