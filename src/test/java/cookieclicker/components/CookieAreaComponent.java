package cookieclicker.components;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieAreaComponent extends BaseComponent {

    private static final By BIG_COOKIE = By.id("bigCookie");
    private WebElement bigCookieBtn;

    private static final  Pattern cookiesPattern = Pattern.compile("([\\d.,]+(?:\\s+[a-zA-Z]+)?)\\s+ciastk");
    private static final  Pattern perSecondPattern = Pattern.compile("na sekundę:\\s*([\\d.,]+(?:\\s+[a-zA-Z]+)?)");


    public CookieAreaComponent(WebDriver driver) {
        super(driver);
    }

    public void waitUntilReady() {
        bigCookieBtn = wait.until(ExpectedConditions.elementToBeClickable(BIG_COOKIE));
    }

    public void cookieData() {
        WebElement cookieNumber = driver.findElement(By.id("cookies"));

        String cookies = extract(cookiesPattern, cookieNumber.getText());
        String perSecond = extract(perSecondPattern, cookieNumber.getText());

        System.out.println("Ciastka: " + cookies);
        System.out.println("Na sekundę: " + perSecond);
    }

    private static String extract(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", " ").trim();
        }
        return null;
    }

    public void clickBigCookie() {
        try {
            if (bigCookieBtn == null) {
                waitUntilReady();
            }
            bigCookieBtn.click();
            cookieData();

        } catch (StaleElementReferenceException e) {
            waitUntilReady();
            bigCookieBtn.click();
        }
    }
}