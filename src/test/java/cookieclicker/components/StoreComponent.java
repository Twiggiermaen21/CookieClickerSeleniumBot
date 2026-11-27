package cookieclicker.components;

import io.qameta.allure.Allure;
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
    private static final By TOOLTIP_UPGRADE_NAME = By.cssSelector("#tooltipCrate .name");

    public StoreComponent(WebDriver driver) {
        super(driver);
    }

    @Step("Buy all available upgrades")
    public void buyAllAvailableUpgrades() {
        if (driver.findElements(ENABLED_UPGRADES).isEmpty()) {
            return;
        }

        while (true) {
            List<WebElement> enabled = driver.findElements(ENABLED_UPGRADES);
            if (enabled.isEmpty()) {
                break;
            }
            try {
                WebElement upgrade = enabled.getFirst();
                actions.moveToElement(upgrade).perform();

                String upgradeName = getUpgradeNameFromTooltip();
                upgrade.click();

                String message = "Kupiono ulepszenie: " + upgradeName;
                System.out.println(message);
                Allure.step(message);
            } catch (StaleElementReferenceException ignored) {
                // Lista przebudowała się po zakupie — kolejna iteracja pobierze ją na świeżo
            }
        }
    }

    private String getUpgradeNameFromTooltip() {
        List<WebElement> nameElements = driver.findElements(TOOLTIP_UPGRADE_NAME);
        if (!nameElements.isEmpty()) {
            String name = nameElements.getFirst().getText().trim();
            if (!name.isBlank()) {
                return name;
            }
        }
        return "Nieznane ulepszenie";
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
                int index = getProductIndex(product);
                String productName = getProductName(index);

                actions.moveToElement(product).perform();
                product.click();

                String owned = getProductOwned(index);
                String message = "Kupiono produkt: " + productName + " (lvl: " + owned + ")";
                System.out.println(message);
                Allure.step(message);
            } catch (StaleElementReferenceException ignored) {
                // DOM przebudował się w trakcie kliknięcia — kolejna iteracja przeliczy stan od nowa
            }
        }
    }

    private int getProductIndex(WebElement product) {
        String id = product.getAttribute("id"); // np. "product4"
        if (id == null || !id.startsWith("product")) {
            return -1;
        }
        return Integer.parseInt(id.replace("product", ""));
    }

    private String getProductName(int index) {
        if (index < 0) {
            return "Nieznany produkt";
        }

        String name = (String) ((JavascriptExecutor) driver).executeScript(
                "var obj = Game.ObjectsById[arguments[0]];" +
                        "return obj ? (obj.dname || obj.name) : null;",
                index
        );
        return name != null ? name : ("product" + index);
    }

    private String getProductOwned(int index) {
        if (index < 0) {
            return "?";
        }
        List<WebElement> ownedEl = driver.findElements(By.id("productOwned" + index));
        if (ownedEl.isEmpty() || ownedEl.getFirst().getText().isBlank()) {
            return "0";
        }
        return ownedEl.getFirst().getText().trim();
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