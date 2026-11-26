package cookieclicker.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Collections;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setup() {
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
                .build();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--user-data-dir=C:\\chrome-cookie-profile");

        driver = new ChromeDriver(service, options);
        driver.manage().window().setSize(new Dimension(1280, 720));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
             driver.quit();
        }
    }
}