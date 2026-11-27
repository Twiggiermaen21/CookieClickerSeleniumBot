package cookieclicker.components.store;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class GameJsClient {

    private final JavascriptExecutor js;

    public GameJsClient(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public String getProductName(int index) {
        if (index < 0) return "Nieznany produkt";
        String name = (String) js.executeScript(
                "var obj = Game.ObjectsById[arguments[0]];" +
                        "return obj ? (obj.dname || obj.name) : null;",
                index
        );
        return name != null ? name : ("product" + index);
    }

    public double getProductPaybackTime(int index) {
        if (index < 0) return Double.MAX_VALUE;
        Double payback = (Double) js.executeScript(
                "var obj = Game.ObjectsById[arguments[0]];" +
                        "if (!obj) return Number.MAX_VALUE;" +
                        "var cps = obj.storedCps * Game.globalCpsMult;" +
                        "return cps > 0 ? (obj.price / cps) : obj.price;",
                index
        );
        return payback != null ? payback : Double.MAX_VALUE;
    }
}