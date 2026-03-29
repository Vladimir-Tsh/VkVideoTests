import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Thread.sleep;

public class VkVideoTests {

    private static final Logger log = LoggerFactory.getLogger(VkVideoTests.class);
    private static final String PROGRESS_LOG = "Прогресс: {} -> {}";

    private static AndroidDriver driver;

    @BeforeAll
    static void setUp() throws URISyntaxException, MalformedURLException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:deviceName", "Pixel4");
        capabilities.setCapability("appium:platformVersion", "11");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        //указать корректный полный путь до VK-Video-v1.136.apk
        capabilities.setCapability("appium:app", "...VK-Video-v1.136.apk");

        driver = new AndroidDriver(new URI("http://127.0.0.1:4723").toURL(), capabilities);

        log.info("Приложение загружается...");
        sleep(10000);
        log.info("Закрываем всплывающее сообщение и предложение залогиниться");
        try {
            WebElement onboarding_view_pager = driver.findElement(By.id("com.vk.vkvideo:id/onboarding_view_pager"));
            if (onboarding_view_pager.isDisplayed()) {
                WebElement close_btn_left = driver.findElement(By.id("com.vk.vkvideo:id/close_btn_left"));
                if (close_btn_left.isEnabled()) {
                    close_btn_left.click();
                }
            }
        } catch (NoSuchElementException _) {}
        sleep(10000);
        try {
            WebElement design_bottom_sheet = driver.findElement(By.id("com.vk.vkvideo:id/design_bottom_sheet"));
            if (design_bottom_sheet.isDisplayed()) {
                WebElement fast_login_tertiary_btn = driver.findElement(By.id("com.vk.vkvideo:id/fast_login_tertiary_btn"));
                if (fast_login_tertiary_btn.isEnabled()) {
                    fast_login_tertiary_btn.click();
                }
            }
        } catch (NoSuchElementException _) {}
        sleep(10000);
        log.info("Ищем видео Место встречи изменить нельзя (1979)");
        WebElement search_button = driver.findElement(By.id("com.vk.vkvideo:id/search_button"));
        search_button.click();
        WebElement query = driver.findElement(By.id("com.vk.vkvideo:id/query"));
        query.sendKeys("место встречи изменить нельзя (1979)");
        log.info("Выбираем Место встречи изменить нельзя (1979)");
        WebElement select = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.vk.vkvideo:id/title\" and @text=\"место встречи изменить нельзя (1979)\"]"));
        select.click();
        sleep(10000);
        log.info("Открываем просмотр видео Место встречи изменить нельзя (1979)");
        WebElement video_display = driver.findElement(By.id("com.vk.vkvideo:id/video_display"));
        video_display.click();
        sleep(10000);
    }

    @AfterAll
    static void quit()
    {
        driver.quit();
    }

    @Test
    @DisplayName("Видео проигрывается")
    void playVideoTest() throws InterruptedException {
        try {
            log.info("Смотрим изменение прогресса, кликая на видео для появления прогрессбара");
            WebElement video_display = driver.findElement(By.id("com.vk.vkvideo:id/video_display"));
            video_display.click();
            sleep(1000);
            WebElement seek_bar = driver.findElement(By.id("com.vk.vkvideo:id/seek_bar"));
            String start = seek_bar.getText();
            sleep(10000);
            video_display.click();
            sleep(1000);
            String stop = seek_bar.getText();
            setProgressLog(start, stop);
            Assertions.assertNotEquals(start, stop);
        } catch (NoSuchElementException exception) {
            log.error(exception.getLocalizedMessage());
            throw new AssertionError(exception);
        }
    }

    @Test
    @DisplayName("Видео не проигрывается")
    void notPlayVideoTest() throws InterruptedException {
        try {
            log.info("Ставим на паузу");
            WebElement video_display = driver.findElement(By.id("com.vk.vkvideo:id/video_display"));
            video_display.click();
            sleep(1000);
            WebElement video_play_button = driver.findElement(AppiumBy.id("com.vk.vkvideo:id/video_play_button"));
            video_play_button.click();
            sleep(1000);
            log.info("Смотрим сохранение прогресса");
            WebElement seek_bar = driver.findElement(By.id("com.vk.vkvideo:id/seek_bar"));
            String start = seek_bar.getText();
            sleep(10000);
            String stop = seek_bar.getText();
            setProgressLog(start, stop);
            log.info("Снимаем с паузы");
            video_play_button.click();
            sleep(10000);
            Assertions.assertEquals(start, stop);
        } catch (NoSuchElementException exception) {
            log.error(exception.getLocalizedMessage());
            throw new AssertionError(exception);
        }
    }

    private void setProgressLog(String start, String stop) {
        log.info(PROGRESS_LOG, start, stop);
    }
}
