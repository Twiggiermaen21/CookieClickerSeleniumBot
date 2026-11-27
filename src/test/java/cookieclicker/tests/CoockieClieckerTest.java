package cookieclicker.tests;

import cookieclicker.base.BaseTest;
import cookieclicker.pages.GamePage;
import cookieclicker.pages.StatsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

@Epic("Cookie Clicker Automation")
@Feature("Gameplay & Store Progression")
class CookieClickerTest extends BaseTest {

    @Test
    @Story("Automated cookie farming and upgrading")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Play Cookie Clicker for 1 hour and purchase upgrades")
    @Description("Opens Cookie Clicker in Polish, checks initial stats, and runs a 1-hour loop clicking the big cookie and buying upgrades/products every 10 seconds.")
    public void playCookieClicker()  {
        GamePage game = new GamePage(driver);
        StatsPage stats = new StatsPage(driver);

        game.open();
        game.startGameInPolish();
        stats.reportStats("START");

        try {
//            runAutomatedGameplayLoop(game, Duration.ofHours(1));
            runAutomatedGameplayLoop(game, Duration.ofMinutes(1));
        } finally {
            stats.reportStats("KONIEC");
        }
    }

    @Step("Run automated gameplay loop for {duration}")
    private void runAutomatedGameplayLoop(GamePage game, Duration duration) {
        long lastCheckTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + duration.toMillis();

        while (System.currentTimeMillis() < endTime) {
            game.cookieArea().clickBigCookie();
            game.cookieArea().clickGoldenCookieIfPresent();
            if (System.currentTimeMillis() - lastCheckTime >= 10_000) {
                performPeriodicStoreAndPopupCheck(game);
                lastCheckTime = System.currentTimeMillis();
            }
        }
    }

    private void performPeriodicStoreAndPopupCheck(GamePage game) {
        game.overlays().closeNotificationsAndPopups();
        game.store().buyAllAvailableUpgrades();
        game.store().buyBestAvailableProducts();
    }

}