package cookieclicker.components.store;

import cookieclicker.components.BaseComponent;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductsComponent extends BaseComponent {

    private static final By ENABLED_PRODUCTS = By.cssSelector("#products > div.product.unlocked.enabled");
    private final GameJsClient gameJs;

    public ProductsComponent(WebDriver driver, GameJsClient gameJs) {
        super(driver);
        this.gameJs = gameJs;
    }

    @Step("Buy most cost-effective available products")
    public void buyBestAvailable() {
        while (true) {
            List<WebElement> enabledProducts = driver.findElements(ENABLED_PRODUCTS);
            if (enabledProducts.isEmpty()) break;

            Optional<WebElement> bestProduct = enabledProducts.stream()
                    .min(Comparator.comparingDouble(p -> gameJs.getProductPaybackTime(getProductIndex(p))));

            try {
                WebElement product = bestProduct.get();
                int index = getProductIndex(product);
                String productName = gameJs.getProductName(index);

                actions.moveToElement(product).perform();
                product.click();

                String owned = getProductOwned(index);
                logPurchase("Kupiono produkt: " + productName + " (lvl: " + owned + ")");
            } catch (StaleElementReferenceException ignored) {
            }
        }
    }

    private int getProductIndex(WebElement product) {
        String id = product.getAttribute("id");
        if (id == null || !id.startsWith("product")) return -1;
        return Integer.parseInt(id.replace("product", ""));
    }

    private String getProductOwned(int index) {
        if (index < 0) return "?";
        List<WebElement> ownedEl = driver.findElements(By.id("productOwned" + index));
        if (ownedEl.isEmpty() || ownedEl.getFirst().getText().isBlank()) return "0";
        return ownedEl.getFirst().getText().trim();
    }

    private void logPurchase(String message) {
        System.out.println(message);
        Allure.step(message);
    }
}