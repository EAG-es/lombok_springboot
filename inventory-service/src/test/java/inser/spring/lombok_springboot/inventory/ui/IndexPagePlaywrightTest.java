package inser.spring.lombok_springboot.inventory.ui;

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
                .setSlowMo(500));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
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
        assertTrue(page.textContent("h1").contains("Inventory Service"), "Header should be in English");
        assertTrue(page.isVisible("text=Welcome to Inventory Management"), "Welcome message should be in English");
    }

    @Test
    @DisplayName("Test language switching functionality")
    void testLanguageSwitching() {
        page.navigate("http://localhost:" + port + "/", new Page.NavigateOptions().setTimeout(600000));
        page.waitForLoadState();

        // Select Spanish
        page.selectOption("select[name='lang']", "es");
        page.waitForSelector("h1:has-text('Servicio de Inventario')");

        // Assert Spanish content
        assertTrue(page.textContent("h1").contains("Servicio de Inventario"), "Header should be in Spanish");
        assertTrue(page.isVisible("text=Bienvenido a la Gestión de Inventario"),
                "Welcome message should be in Spanish");

        // Switch back to English
        page.selectOption("select[name='lang']", "en");
        page.waitForSelector("h1:has-text('Inventory Service')");

        // Assert back to English
        assertTrue(page.textContent("h1").contains("Inventory Service"), "Header should be in English");
        assertTrue(page.isVisible("text=Welcome to Inventory Management"),
                "Welcome message should be in English");
    }

    @Test
    @DisplayName("Full CRUD and Pagination Test")
    void testFullCrudAndPagination() {
        page.navigate("http://localhost:" + port + "/", new Page.NavigateOptions().setTimeout(600000));
        page.waitForLoadState();

        // 1. Create 11 records
        for (int i = 1; i <= 11; i++) {
            String value = String.valueOf(i);
            page.fill("input[name='id']", ""); // Ensure ID is empty for auto-generation
            page.fill("input[name='productId']", value);
            page.fill("input[name='quantity']", value);

            page.click("button:has-text('Create')");

            // Wait for success message
            page.waitForSelector(".alert-success:has-text('Form submitted successfully via CREATE')");
        }

        // 2. Read for: ID: "", productId: "", quantity: ""
        page.fill("input[name='id']", "");
        page.fill("input[name='productId']", "");
        page.fill("input[name='quantity']", "");

        page.click("button:has-text('Read')");

        // Wait for table to load
        page.waitForSelector("table");

        // 3. Page forward
        page.click("button:has-text('Next')");
        // Verify we are on next page (should see 11-12 / 12 or similar)
        page.waitForSelector("text=11 -");

        // 4. Page backward
        page.click("button:has-text('Previous')");
        page.waitForSelector("text=1 - 10");

        // 5. Select ID: "1"
        page.click("tr:has(td:text-is('1')) button[title='Select']");

        // 6. Update record; ID: 1, to new productId: "111", new quantity: "111"
        page.fill("input[name='productId']", "111");
        page.fill("input[name='quantity']", "111");

        page.click("button:has-text('Update')");

        // After success, it goes back to 'read' mode (showReadList was true)
        // So we should wait for the table to appear again
        page.waitForSelector("table", new Page.WaitForSelectorOptions().setTimeout(60000));

        // 7. Use Back button
        page.waitForSelector("button:has-text('Back')");
        page.click("button:has-text('Back')");

        // Final assertion to ensure we are back at the main panel
        assertTrue(page.isVisible("button:has-text('Create')"), "Should be back at the main form");
    }
}
