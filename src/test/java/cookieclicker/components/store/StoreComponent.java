package cookieclicker.components.store;

import org.openqa.selenium.WebDriver;

public class StoreComponent {

    private final UpgradesComponent upgrades;
    private final ProductsComponent products;

    public StoreComponent(WebDriver driver) {
        GameJsClient gameJs = new GameJsClient(driver);
        this.upgrades = new UpgradesComponent(driver);
        this.products = new ProductsComponent(driver, gameJs);
    }

    public void buyAllAvailableUpgrades() {
        upgrades.buyAllAvailable();
    }

    public void buyBestAvailableProducts() {
        products.buyBestAvailable();
    }
}