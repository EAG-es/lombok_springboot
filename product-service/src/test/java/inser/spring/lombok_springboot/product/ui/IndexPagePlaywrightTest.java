package inser.spring.lombok_springboot.product.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/test-data.sql", executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class IndexPagePlaywrightTest {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500)); // Visible for the user, with slight delay
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        // Create context with explicit English locale to ensure consistency
        com.microsoft.playwright.BrowserContext context = browser.newContext(
                new com.microsoft.playwright.Browser.NewContextOptions().setLocale("en-US"));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        page.context().close();
    }

    @Test
    @DisplayName("Check if index page loads correctly in English")
    void testIndexPageLoads() {
        page.navigate("http://localhost:" + port + "/", new Page.NavigateOptions().setTimeout(600000));
        page.waitForLoadState();

        // Assert English content
        assertTrue(page.textContent("h1").contains("Product Service"), "Header should be in English");
        assertTrue(page.isVisible("text=Welcome to the Product Catalog"), "Welcome message should be in English");
    }

    @Test
    @DisplayName("Test language switching functionality")
    void testLanguageSwitching() {
        page.navigate("http://localhost:" + port + "/", new Page.NavigateOptions().setTimeout(600000));
        page.waitForLoadState();

        // Select Spanish
        page.selectOption("select[name='lang']", "es");

        // Wait for the text to change to Spanish
        page.waitForSelector("h1:has-text('Servicio de Productos')",
                new Page.WaitForSelectorOptions().setTimeout(10000));

        // Assert Spanish content
        assertTrue(page.textContent("h1").contains("Servicio de Productos"), "Header should change to Spanish");

        // Switch back to English
        page.selectOption("select[name='lang']", "en");

        // Wait for English text
        page.waitForSelector("h1:has-text('Product Service')", new Page.WaitForSelectorOptions().setTimeout(10000));

        // Assert back to English
        assertTrue(page.textContent("h1").contains("Product Service"), "Header should change back to English");
    }
}
