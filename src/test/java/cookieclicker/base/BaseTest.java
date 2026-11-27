package cookieclicker.base;

import io.qameta.allure.Step;
import org.cookieclicker.config.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    @Step("Set up WebDriver and set window size to 1280x720")
    public void setup() throws Exception {
        driver = DriverFactory.createDriver();
        driver.manage().window().setSize(new Dimension(1280, 720));
    }

    @AfterEach
    @Step("Tear down WebDriver session")
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}