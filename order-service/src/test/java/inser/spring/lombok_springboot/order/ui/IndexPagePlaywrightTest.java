package inser.spring.lombok_springboot.order.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
                .setHeadless(true));
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
    @DisplayName("Test Order Service UI - Full CRUD and Pagination")
    void testOrderCRUDAndPagination() {
        page.navigate("http://localhost:" + port + "/");

        // 1. READ (Initial check)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Read")).click();
        assertThat(page.locator("table")).containsText("ORD-001");

        // 2. CREATE
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Back")).click();
        page.locator("input[name='orderNumber']").fill("ORD-NEW-TEST");
        page.locator("input[name='productId']").fill("10");
        page.locator("input[name='quantity']").fill("5");
        page.locator("input[name='totalPrice']").fill("500.00");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create")).click();

        // Should show success and remain in panel (since we didn't 'Select' anything
        // before)
        assertThat(page.locator("body")).containsText("Form submitted successfully via CREATE");

        // Now go to Read to verify it's there
        page.locator("input[name='orderNumber']").fill("");
        page.locator("input[name='productId']").fill("");
        page.locator("input[name='quantity']").fill("");
        page.locator("input[name='totalPrice']").fill("");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Read")).click();
        assertThat(page.locator("table")).containsText("ORD-NEW-TEST");

        // 3. UPDATE (Select the new record)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select")).last().click();
        // Since we clicked Select, showReadList is now true.
        // We are back at the panel with fields filled.
        page.locator("input[name='quantity']").fill("10");
        page.locator("input[name='totalPrice']").fill("1200.00");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update")).click();
        // Because showReadList is true, it should switch back to 'read' automatically.
        // The success message in OperationPanel is NOT visible in currentOp='read'.
        // So we verify the table content.
        assertThat(page.locator("table")).containsText("1200.00");

        // 4. DELETE
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select")).last().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
        // Switches back to 'read' automatically.
        // Again, verify it's gone. Since table might not be rendered if empty, check
        // body text.
        assertThat(page.locator("body")).not().containsText("ORD-NEW-TEST");
    }

    @Test
    @DisplayName("Test language switching functionality")
    void testLanguageSwitching() {
        page.navigate("http://localhost:" + port + "/");

        // Select Spanish
        page.selectOption("select[name='lang']", "es");
        assertThat(page.locator("h1")).containsText("Servicio de Pedidos");
        assertThat(page.locator("body")).containsText("Bienvenido al Procesamiento de Pedidos");

        // Switch back to English
        page.selectOption("select[name='lang']", "en");
        assertThat(page.locator("h1")).containsText("Order Service");
        assertThat(page.locator("body")).containsText("Welcome to Order Processing");
    }
}
