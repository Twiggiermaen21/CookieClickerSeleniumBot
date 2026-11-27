package cookieclicker.base;

import io.qameta.allure.Step;
import org.cookieclicker.config.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    @Step("Set up WebDriver and set window size to 1280x720")
    public void setup() throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Allure.getLifecycle().updateTestCase(testResult -> {
            // Usuwa domyślną nazwę klasy jako suite i ustawia nową z datą/godziną
            testResult.getLabels().removeIf(label -> "suite".equals(label.getName()));
            testResult.getLabels().add(new Label().setName("suite").setValue("Session " + timestamp));

            // Wymusza traktowanie każdego uruchomienia jako osobny wynik (a nie Retry)
            testResult.setHistoryId(UUID.randomUUID().toString());
        });

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