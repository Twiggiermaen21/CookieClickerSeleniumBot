package cookieclicker.components.store;

import cookieclicker.components.BaseComponent;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class UpgradesComponent extends BaseComponent {

    private static final By ENABLED_UPGRADES = By.cssSelector("#upgrades > div.enabled");
    private static final By TOOLTIP_UPGRADE_NAME = By.cssSelector("#tooltipCrate .name");

    public UpgradesComponent(WebDriver driver) {
        super(driver);
    }

    @Step("Buy all available upgrades")
    public void buyAllAvailable() {
        while (true) {
            List<WebElement> enabled = driver.findElements(ENABLED_UPGRADES);
            if (enabled.isEmpty()) break;

            try {
                WebElement upgrade = enabled.getFirst();
                actions.moveToElement(upgrade).perform();

                String upgradeName = getUpgradeNameFromTooltip();
                upgrade.click();

                logPurchase("Kupiono ulepszenie: " + upgradeName);
            } catch (StaleElementReferenceException ignored) {
            }
        }
    }

    private String getUpgradeNameFromTooltip() {
        List<WebElement> nameElements = driver.findElements(TOOLTIP_UPGRADE_NAME);
        if (!nameElements.isEmpty()) {
            String name = nameElements.getFirst().getText().trim();
            if (!name.isBlank()) return name;
        }
        return "Nieznane ulepszenie";
    }

    private void logPurchase(String message) {
        System.out.println(message);
        Allure.step(message);
    }
}