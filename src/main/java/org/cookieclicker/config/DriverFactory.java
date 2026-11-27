package org.cookieclicker.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URI;
import java.util.Collections;

public class DriverFactory {

    public static WebDriver createDriver() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");

        if (remoteUrl != null && !remoteUrl.isBlank()) {
            // Tryb Docker (Linux)
            options.addArguments("--user-data-dir=/home/seluser/chrome-profile");
            return new RemoteWebDriver(URI.create(remoteUrl).toURL(), options);
        }

        // Tryb lokalny (Windows)
        options.addArguments("--user-data-dir=C:\\chrome-cookie-profile");
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
                .build();

        return new ChromeDriver(service, options);
    }
}