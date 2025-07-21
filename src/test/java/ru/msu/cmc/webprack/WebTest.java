package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WebTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-autofill-keyboard-accessory-view[8]");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");

        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.default_content_setting_values.notifications", 2
        ));

        options.addArguments("--incognito");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        baseUrl = "http://localhost:8080/";

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(baseUrl);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void registrationTest() {
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-outline-primary")));
        registerButton.click();
        WebElement button = driver.findElement(By.cssSelector(".btn.btn-outline-primary"));
        button.click();

        wait.until(ExpectedConditions.titleIs("Регистрация"));

        driver.findElement(By.name("username")).sendKeys("newUser");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("password123");
        driver.findElement(By.name("name")).sendKeys("Test User");
        Select roleSelect = new Select(driver.findElement(By.name("role")));
        roleSelect.selectByValue("ADMIN");

        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getPageSource().contains("Регистрация успешна! Теперь вы можете войти."));

    }

    @Test
    public void loginTest() {
        driver.get(baseUrl + "login");
        wait.until(ExpectedConditions.titleIs("Вход в систему"));
        driver.findElement(By.id("username")).sendKeys("1234");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        wait.until(ExpectedConditions.urlToBe(baseUrl));
        assertEquals(baseUrl, driver.getCurrentUrl());
    }

    @Test
    public void invalidLoginTest() {
        driver.get(baseUrl + "login");

        driver.findElement(By.id("username")).sendKeys("wrongUser");
        driver.findElement(By.id("password")).sendKeys("wrongPassword");

        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));
        assertTrue(driver.getPageSource().contains("Неверный логин или пароль"));
    }
    @Test
    public void AddClientTest() throws InterruptedException {
        driver.get(baseUrl + "login");

        driver.findElement(By.id("username")).sendKeys("1234");
        driver.findElement(By.id("password")).sendKeys("1234");

        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        wait.until(ExpectedConditions.urlToBe(baseUrl));

        /*WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();*/

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement clientsButton = wait2.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-outline-primary")));
        clientsButton.click();

        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button1 = wait1.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-primary")));
        button1.click();

        driver.findElement(By.name("name")).sendKeys("Test Client");
        driver.findElement(By.name("address")).sendKeys("123 Test St, Test City");
        driver.findElement(By.name("phone")).sendKeys("+1234567890");
        driver.findElement(By.name("email")).sendKeys("testclient@example.com");
        Thread.sleep(5000);
        // Отправляем форму
        //driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();


        wait.until(ExpectedConditions.urlContains("/clients"));
    }

    @Test
    public void ViewEmployee() throws InterruptedException {
        driver.get(baseUrl + "login");

        driver.findElement(By.id("username")).sendKeys("1234");
        driver.findElement(By.id("password")).sendKeys("1234");

        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
        wait.until(ExpectedConditions.urlToBe(baseUrl));

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement clientsButton = wait2.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-outline-warning")));
        clientsButton.click();


        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button1 = wait1.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-primary")));
        button1.click();

        driver.findElement(By.name("name")).sendKeys("Test Client");
        driver.findElement(By.name("position")).sendKeys("Jury");
        driver.findElement(By.name("phone")).sendKeys("+1234567890");
        driver.findElement(By.name("email")).sendKeys("testclient@example.com");
        driver.findElement(By.name("address")).sendKeys("123 Test St, Test City");
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();


        wait.until(ExpectedConditions.urlContains("/employees"));
        String employeeId = "41";
        driver.findElement(By.cssSelector("a[href*='/employees/" + employeeId + "'][class*='btn-outline-primary']")).click();
        Thread.sleep(5000);
    
        driver.findElement(By.cssSelector("button.btn.btn-danger")).click();



    }
}