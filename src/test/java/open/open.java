package open;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class open {

    static WebDriver driver;
    static WebDriverWait wait;
    final static boolean work = true;

    @BeforeEach
    public void setup() {
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
                .build();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        driver = new ChromeDriver(service, options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().setSize((new Dimension(1280, 720)));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get("https://orteil.dashnet.org/cookieclicker/");
    }

    @Test
    public void OpenWebsite() throws InterruptedException {
        String title = driver.getTitle();
        assertEquals("Cookie Clicker", title);

        WebElement consentBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.fc-cta-consent"))
        );

        consentBtn.click();
        Thread.sleep(1000);
        WebElement langSelectButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("langSelect-PL"))
        );
        langSelectButton.click();
        Thread.sleep(30000);
        long lastCheckTime = System.currentTimeMillis();
        int iteration = 1;

        while (work) {
            WebElement cookieBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("bigCookie")));
            cookieBtn.click();
            Thread.sleep(1000);

            // Wykonuje się co 30 sekund
            if (System.currentTimeMillis() - lastCheckTime >= 10_000) {
                String cookiesOnly = driver.findElement(By.id("cookies")).getText().split("\n")[0];
                int cookiesNumber = Integer.parseInt(cookiesOnly.replaceAll("\\D+", ""));

                for (int i = 19; i >= 0; i--) {
                    WebElement product = driver.findElement(By.id("product" + i));
                    String className = product.getAttribute("class");

                    if (className != null && className.equals("product unlocked enabled")) {
                        String priceText = driver.findElement(By.id("productPrice" + i)).getText().replaceAll("\\D+", "");
                        int price = Integer.parseInt(priceText);

                        if (price <= cookiesNumber) {
                            iteration -= price;
                            cookiesNumber -= price; // aktualizujemy lokalnie po zakupie
                            wait.until(ExpectedConditions.elementToBeClickable(By.id("product" + i))).click();
                        }
                    }
                }

                lastCheckTime = System.currentTimeMillis(); // reset timera na kolejne 30 s
            }
            List<WebElement> closeBtns = driver.findElements(By.cssSelector(".framed.close.sidenote"));

            if (!closeBtns.isEmpty() && closeBtns.get(0).isDisplayed()) {
                closeBtns.get(0).click();
            }

            if(System.currentTimeMillis() - lastCheckTime >= 10_000) {
                WebElement element = driver.findElement(By.id("upgrade0"));
                Actions actions = new Actions(driver);
                actions.moveToElement(element).perform();

                int upgradesCount = driver.findElements(By.cssSelector("#upgrades > div")).size();

                for (int j = upgradesCount; j > 0; j--) {
                    WebElement upgrade = driver.findElement(By.id("product" + j));
                    String className = upgrade.getAttribute("class");
                    if (className.equals("crate upgrade enabled")) {

                        WebElement shopElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("upgrade" + j)));

                    }
                }
            }

        }
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
//            driver.quit();
        }
    }


}
