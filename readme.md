# 🍪 Cookie Clicker Selenium Bot

Automatyczny bot do gry [Cookie Clicker](https://orteil.dashnet.org/cookieclicker/) napisany w Javie (Selenium WebDriver + JUnit 5) z raportowaniem w **Allure** oraz pełnym wdrożeniem w **Dockerze** z podglądem przeglądarki na żywo (**noVNC**).

## ✨ Funkcjonalności
* **Inteligentne zakupy w sklepie:**
    * Automatyczne wykupywanie dostępnych ulepszeń (`Upgrades`) z odczytem nazw z tooltipów.
    * Wybór najbardziej opłacalnych budynków (`Products`) na podstawie czasu zwrotu (`Cena / CpS`) wyliczanego bezpośrednio z obiektów JS gry (`Game.ObjectsById`).
* **Obsługa interfejsu:** Automatyczne akceptowanie zgód, wybór języka polskiego, czekanie na załadowanie gry (`#offGameMessage`) oraz zamykanie banerów.
* **Raportowanie Allure:**
    * Osobny suite z datą i godziną dla każdej sesji.
    * Szczegółowe kroki zakupów (nazwa ulepszenia / nazwa budynku + aktualny poziom).
    * Załączniki HTML ze statystykami gry na starcie i końcu sesji.
* **Praca ciągła w Dockerze:** Trwały profil Chrome (zapis stanu gry), automatyczny restart po zakończeniu sesji i serwer raportów Allure aktualizowany na bieżąco.

## 🛠 Stack technologiczny
* **Java 25** & **Maven**
* **Selenium 4** & **JUnit 5**
* **Allure Report** + **AspectJ**
* **Docker** & **Docker Compose** (`selenium/standalone-chrome`, `allure-docker-service`)

## 🚀 Uruchomienie w Dockerze (zalecane)

1. Utwórz katalogi na profil przeglądarki i wyniki Allure (na Linuxie nadaj uprawnienia dla kontenera):
   ```bash
   mkdir -p chrome-profile target/allure-results
   chmod -R 777 chrome-profile target/allure-results
   ```

2. Zbuduj i uruchom stos w tle:
   ```bash
   docker compose up --build -d
   ```

3. Otwórz panele w przeglądarce:
    * **Podgląd gry na żywo (noVNC):** `http://localhost:7900`
    * **Raport Allure:** `http://localhost:5050/allure-docker-service/latest-report`
    * **Selenium Grid UI:** `http://localhost:4444/ui`

4. Podgląd logów bota:
   ```bash
   docker logs -f cookie-clicker-bot
   ```

## 💻 Uruchomienie lokalne (Windows / IntelliJ IDEA)

1. Uruchom testy przez Mavena (IntelliJ: **Maven → Lifecycle → `test`**):
   ```bash
   mvn test
   ```
2. Wygeneruj i otwórz raport Allure (IntelliJ: **Maven → Plugins → allure → `allure:serve`**):
   ```bash
   mvn allure:serve
   ```

## 📂 Struktura projektu
```text
src/
├── main/java/org/cookieclicker/config/
│   └── DriverFactory.java          # Konfiguracja lokalnego ChromeDriver i RemoteWebDriver
└── test/java/cookieclicker/
    ├── base/BaseTest.java          # Cykl życia drivera i konfiguracja suite'ów Allure
    ├── components/
    │   ├── CookieAreaComponent.java
    │   ├── OverlaysComponent.java
    │   └── store/
    │       ├── StoreComponent.java
    │       ├── UpgradesComponent.java
    │       ├── ProductsComponent.java
    │       └── GameJsClient.java
    ├── pages/
    │   ├── GamePage.java
    │   └── StatsPage.java
    └── tests/
        └── CookieClickerTest.java
```