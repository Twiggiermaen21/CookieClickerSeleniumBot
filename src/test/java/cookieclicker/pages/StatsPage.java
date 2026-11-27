package cookieclicker.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class StatsPage {
    private static final By STATS_BTN = By.id("statsButton");
    private static final By STATS_GENERAL = By.id("statsGeneral");
    private static final By STATS_ROWS = By.cssSelector("#statsGeneral div.listing");
    private static final By CLOSE_BTN = By.cssSelector(".close.menuClose");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public StatsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openStatsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(STATS_BTN)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(STATS_GENERAL));
    }

    public void closeStatsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(CLOSE_BTN)).click();
    }

    public void attachStatsToAllure(String stage) {
        List<WebElement> rows = driver.findElements(STATS_ROWS);

        StringBuilder html = new StringBuilder();
        html.append("<meta charset='UTF-8'>")
                .append("<style>")
                .append("table { border-collapse: collapse; width: 100%; font-family: sans-serif; font-size: 14px; }")
                .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
                .append("th { background-color: #f4f4f4; }")
                .append("</style>")
                .append("<table><tr><th>Statystyka</th><th>Wartość</th></tr>");

        for (WebElement row : rows) {
            String line = row.getText().replaceAll("\\s+", " ").trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(":", 2);
            String key = parts[0].trim();
            String value = parts.length > 1 ? parts[1].trim() : "";

            html.append("<tr><td><b>").append(key).append("</b></td><td>").append(value).append("</td></tr>");
        }
        html.append("</table>");

        Allure.addAttachment(
                "Statystyki [" + stage + "]",
                "text/html",
                new ByteArrayInputStream(html.toString().getBytes(StandardCharsets.UTF_8)),
                ".html"
        );
    }

    // Wygodna metoda 3-w-1 do wywołania w teście
    public void reportStats(String stage) {
        openStatsPage();
        attachStatsToAllure(stage);
        closeStatsPage();
    }
}