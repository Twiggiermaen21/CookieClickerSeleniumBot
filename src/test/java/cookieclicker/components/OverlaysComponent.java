package cookieclicker.components;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OverlaysComponent extends BaseComponent {

    private static final By CONSENT_BTN = By.cssSelector("button.fc-cta-consent");
    private static final By GOT_IT_BTN = By.cssSelector("a.cc_btn_accept_all");
    private static final By NOTES_ITEMS = By.cssSelector("#notes > div:not(.sidenote)");
    private static final By CLOSE_NOTES_BTN = By.cssSelector(".framed.close.sidenote");
    private static final By GOOGLE_BUBBLE_OPEN = By.id("ft-reg-bubble");
    private static final By GOOGLE_BUBBLE_CLOSE = By.className("button.ft-reg-bubble-close");

    public OverlaysComponent(WebDriver driver) {
        super(driver);
    }

    public void acceptInitialConsentIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(CONSENT_BTN))
                    .click();
        } catch (TimeoutException ignored) {
            // Baner nie pojawił się (zgoda już zapisana w profilu)
        }
    }
    public void closeBottomCookieBanner() {
        clickIfVisible(GOT_IT_BTN);
        if (!driver.findElements(GOOGLE_BUBBLE_OPEN).isEmpty()) {
            clickIfVisible(GOOGLE_BUBBLE_CLOSE);
            System.out.println("yes");
        }
    }

    public void closeNotificationsAndPopups() {
        if (driver.findElements(NOTES_ITEMS).size() >= 2) {
            clickIfVisible(CLOSE_NOTES_BTN);
        }

    }
}