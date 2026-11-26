package cookieclicker.tests;

import cookieclicker.base.BaseTest;
import cookieclicker.pages.GamePage;
import org.junit.jupiter.api.Test;

import java.time.Duration;


class CookieClickerTest extends BaseTest {

    @Test
    public void playCookieClicker() throws InterruptedException {
        GamePage game = new GamePage(driver);

        game.open();

        game.startGameInPolish();

        long lastCheckTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + Duration.ofHours(1).toMillis();
        while (System.currentTimeMillis() < endTime) {
            game.cookieArea().clickBigCookie();

            if (System.currentTimeMillis() - lastCheckTime >= 10_000) {
                game.overlays().closeNotificationsAndPopups();
                game.store().buyAllAvailableUpgrades();
                game.store().buyBestAvailableProducts();

                lastCheckTime = System.currentTimeMillis();
            }
        }
    }
}