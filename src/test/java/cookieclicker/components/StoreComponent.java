package cookieclicker.components;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StoreComponent extends BaseComponent {

    private static final By UPGRADES_BOX = By.id("upgrades");
    private static final By ENABLED_UPGRADES = By.cssSelector("#upgrades > div.enabled");
    private static final By ENABLED_PRODUCTS = By.cssSelector("#products > div.product.unlocked.enabled");

    public StoreComponent(WebDriver driver) {
        super(driver);
    }

    @Step("Buy all available upgrades")
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

    @Step("Buy most cost-effective available products")
    public void buyBestAvailableProducts() {
        while (true) {
            List<WebElement> enabledProducts = driver.findElements(ENABLED_PRODUCTS);
            if (enabledProducts.isEmpty()) {
                break;
            }

            // Wybiera budynek o najkrótszym czasie zwrotu (Cena / CpS budynku)
            Optional<WebElement> bestProduct = enabledProducts.stream()
                    .min(Comparator.comparingDouble(this::getPaybackTimeInSeconds));

            if (bestProduct.isEmpty()) {
                break;
            }

            try {
                WebElement product = bestProduct.get();
                actions.moveToElement(product).perform();
                product.click();
            } catch (StaleElementReferenceException ignored) {
                // DOM przebudował się w trakcie kliknięcia — kolejna iteracja przeliczy stan od nowa
            }
        }
    }

    private double getPaybackTimeInSeconds(WebElement product) {
        String id = product.getAttribute("id"); // np. "product0"
        if (id == null || !id.startsWith("product")) {
            return Double.MAX_VALUE;
        }

        int index = Integer.parseInt(id.replace("product", ""));
        Double payback = (Double) ((JavascriptExecutor) driver).executeScript(
                "var obj = Game.ObjectsById[arguments[0]];" +
                        "if (!obj) return Number.MAX_VALUE;" +
                        "var cps = obj.storedCps * Game.globalCpsMult;" +
                        "return cps > 0 ? (obj.price / cps) : obj.price;",
                index
        );

        return payback != null ? payback : Double.MAX_VALUE;
    }
}