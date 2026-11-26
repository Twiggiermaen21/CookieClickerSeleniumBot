package open;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.StaleElementReferenceException;
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

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

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
        actions = new Actions(driver);

        driver.manage().window().setSize(new Dimension(1280, 720));
        // Brak implicitlyWait - inaczej puste findElements blokują pętlę
        driver.get("https://orteil.dashnet.org/cookieclicker/");
    }

    @Test
    public void openWebsite() throws InterruptedException {
        assertEquals("Cookie Clicker", driver.getTitle());

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.fc-cta-consent"))).click();

        WebElement langSelectButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("langSelect-PL")));
        langSelectButton.click();
        Thread.sleep(30000);
        // Czekamy na przeładowanie po wyborze języka zamiast sztywnego sleep(30000)
        wait.until(ExpectedConditions.stalenessOf(langSelectButton));
        WebElement cookieBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("bigCookie")));

        closeCookieBanner();

        long lastCheckTime = System.currentTimeMillis();

        while (true) {
            cookieBtn.click();
            Thread.sleep(50);

            if (System.currentTimeMillis() - lastCheckTime >= 10_000) {
                closeOverlays();
                buyUpgrades();
                buyProducts();
                lastCheckTime = System.currentTimeMillis();
            }
        }
    }

    private void closeCookieBanner() {
        List<WebElement> gotItBtns = driver.findElements(By.cssSelector("a.cc_btn_accept_all"));
        if (!gotItBtns.isEmpty() && gotItBtns.get(0).isDisplayed()) {
            gotItBtns.get(0).click();
        }
    }

    private void closeOverlays() {
        int notesCount = driver.findElements(By.cssSelector("#notes > div:not(.sidenote)")).size();
        if (notesCount > 2) {
            List<WebElement> closeBtns = driver.findElements(By.cssSelector(".framed.close.sidenote"));
            if (!closeBtns.isEmpty() && closeBtns.get(0).isDisplayed()) {
                closeBtns.get(0).click();
            }
        }

        List<WebElement> googleBubble = driver.findElements(By.cssSelector("#ft-reg-bubble:not(.ft-collapsed)"));
        if (!googleBubble.isEmpty()) {
            List<WebElement> closeBtn = driver.findElements(By.cssSelector("button.ft-reg-bubble-close"));
            if (!closeBtn.isEmpty() && closeBtn.get(0).isDisplayed()) {
                closeBtn.get(0).click();
            }
        }
    }

    private void buyUpgrades() {
        List<WebElement> enabledUpgrades = driver.findElements(By.cssSelector("#upgrades > div.enabled"));
        if (enabledUpgrades.isEmpty()) {
            return;
        }

        // Rozwijamy pasek ulepszeń najechaniem myszką
        WebElement upgradesBox = driver.findElement(By.id("upgrades"));
        actions.moveToElement(upgradesBox).perform();

        // Pobieramy na świeżo po każdym kliknięciu, bo gra przebudowuje listę #upgrades
        while (true) {
            enabledUpgrades = driver.findElements(By.cssSelector("#upgrades > div.enabled"));
            if (enabledUpgrades.isEmpty()) {
                break;
            }
            try {
                enabledUpgrades.get(0).click();
            } catch (StaleElementReferenceException ignored) {
                // Element zniknął w trakcie klikania - kolejna iteracja pobierze aktualną listę
            }
        }
    }

    private void buyProducts() {
        int productsCount = driver.findElements(By.cssSelector("#products > div.product")).size();

        // Od najdroższych do najtańszych; klasa "enabled" sama mówi, czy nas stać
        for (int i = productsCount - 1; i >= 0; i--) {
            WebElement product = driver.findElement(By.id("product" + i));
            String className = product.getAttribute("class");

            if (className != null && className.contains("enabled")) {
                product.click();
            }
        }
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            // driver.quit();
        }
    }
}