package cookieclicker.components;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StoreComponent extends BaseComponent {

    private static final By UPGRADES_BOX = By.id("upgrades");
    private static final By ENABLED_UPGRADES = By.cssSelector("#upgrades > div.enabled");
    private static final By PRODUCTS = By.cssSelector("#products > div.product");

    public StoreComponent(WebDriver driver) {
        super(driver);
    }

    public void buyAllAvailableUpgrades() {
        if (driver.findElements(ENABLED_UPGRADES).isEmpty()) {
            return;
        }

        actions.moveToElement(driver.findElement(UPGRADES_BOX)).perform();

        while (true) {
            List<WebElement> enabled = driver.findElements(ENABLED_UPGRADES);
            if (enabled.isEmpty()) {
                break;
            }
            try {
                enabled.getFirst().click();
            } catch (StaleElementReferenceException ignored) {
                // Lista przebudowała się po zakupie — kolejna iteracja pobierze ją na świeżo
            }
        }
    }

    public void buyBestAvailableProducts() {
        int productsCount = driver.findElements(PRODUCTS).size();

        for (int i = productsCount - 1; i >= 0; i--) {
            WebElement product = driver.findElement(By.id("product" + i));
            String className = product.getAttribute("class");

            if (className != null && className.contains("enabled")) {
                product.click();
            }
        }
    }
}