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
        // 1. Tryb Docker z podpięciem do istniejącego Chromium (obejście Cloudflare)
        String debuggerAddress = System.getenv("SELENIUM_DEBUGGER_ADDRESS");
        if (debuggerAddress != null && !debuggerAddress.isBlank()) {
            ChromeOptions debugOptions = new ChromeOptions();
            debugOptions.setExperimentalOption("debuggerAddress", debuggerAddress);

            File linuxDriver = new File("/usr/bin/chromedriver");
            if (linuxDriver.exists()) {
                ChromeDriverService service = new ChromeDriverService.Builder()
                        .usingDriverExecutable(linuxDriver)
                        .build();
                return new ChromeDriver(service, debugOptions);
            }
            return new ChromeDriver(debugOptions);
        }

        // Opcje startowe dla nowych instancji przeglądarki
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--lang=pl-PL");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // 2. Tryb Docker z Selenium Grid (RemoteWebDriver)
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        if (remoteUrl != null && !remoteUrl.isBlank()) {
            options.addArguments("--user-data-dir=/home/seluser/chrome-profile");
            return new RemoteWebDriver(URI.create(remoteUrl).toURL(), options);
        }

        // 3. Tryb lokalny (Windows)
        options.addArguments("--user-data-dir=C:\\chrome-cookie-profile");
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("chromedriver.exe"))
                .withLogFile(new File("chromedriver.log"))
                .withVerbose(true)
                .build();

        return new ChromeDriver(service, options);
    }
}