package cookieclicker.pages;

import cookieclicker.components.CookieAreaComponent;
import cookieclicker.components.OverlaysComponent;
import cookieclicker.components.StoreComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GamePage {

    private static final String URL = "https://orteil.dashnet.org/cookieclicker/";
    private static final By LANG_PL_BTN = By.id("langSelect-PL");

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final CookieAreaComponent cookieArea;
    private final StoreComponent store;
    private final OverlaysComponent overlays;

    public GamePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.cookieArea = new CookieAreaComponent(driver);
        this.store = new StoreComponent(driver);
        this.overlays = new OverlaysComponent(driver);
    }

    public void open() {
        driver.get(URL);
    }

    public void startGameInPolish() throws InterruptedException {
        overlays.acceptInitialConsentIfPresent();

        List<WebElement> langBtns = driver.findElements(LANG_PL_BTN);
        if (!langBtns.isEmpty() && langBtns.getFirst().isDisplayed()) {
            WebElement langBtn = langBtns.getFirst();
            langBtn.click();
            wait.until(ExpectedConditions.stalenessOf(langBtn));
        }

        Thread.sleep(30000);

        cookieArea.waitUntilReady();
        overlays.closeBottomCookieBanner();
    }

    public CookieAreaComponent cookieArea() {
        return cookieArea;
    }

    public StoreComponent store() {
        return store;
    }

    public OverlaysComponent overlays() {
        return overlays;
    }
}