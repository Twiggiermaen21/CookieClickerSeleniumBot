package cookieclicker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StatsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public StatsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public void openStatsPage() {
        WebElement statsBtn = driver.findElement(By.id("statsButton"));
        statsBtn.click();
    }

    public void getStatsData() {
        WebElement statsGeneral = driver.findElement(By.id("statsGeneral"));
        System.out.println(statsGeneral.getText());
    }

    public void closeStatsPage(){
        WebElement closeBtn = driver.findElement(By.className("close menuClose"));
        closeBtn.click();
    }
}
