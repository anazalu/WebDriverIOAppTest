package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.*;

import utils.DriverMethods;
//import utils.MyCustomListener;
import utils.TestProperties;

import java.io.File;
import java.time.Duration;
import java.util.Map;

//import static reports.ExtentTestManager.getTest;

//@Listeners(MyCustomListener.class)
public class TestCases {

    public AppiumDriverLocalService server;
    public AndroidDriver driver;
    public WebDriverWait wait;

    @BeforeClass(alwaysRun = true)
    public void beforeClassSetup() {
//        server = AppiumDriverLocalService.buildDefaultService();
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.usingPort(4724);
        appiumServiceBuilder.withLogFile(new File("appium-server-logs.log"));
        appiumServiceBuilder.withArgument(GeneralServerFlag.RELAXED_SECURITY);
        server = appiumServiceBuilder.build();
        server.clearOutPutStreams();
        server.start();

        TestProperties.loadProperties();
        DesiredCapabilities desiredCapabilities = TestProperties.getDesiredCapabilities();

        driver = new AndroidDriver(server.getUrl(), desiredCapabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        DriverMethods.setDriver(driver);

//        loginScreen = new LoginScreen(driver);

    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethodSetup() {
        DriverMethods.activateApp();
    }

    @Test(testName = "Get device info")
    public void testGetDeviceInfo() {
        System.out.println(DriverMethods.getDeviceInfo());
        DriverMethods.logScreenShot();
        DriverMethods.getScreenshot();
    }

    @Test(testName = "Open app")
    public void testHomePage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Webview\")")));
        DriverMethods.logScreenShot();
        DriverMethods.getScreenshot();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodCleanup() {
        driver.executeScript("mobile: terminateApp", Map.ofEntries(
                Map.entry("appId", "com.wdiodemoapp"),
//                Map.entry("appId", TestProperties.getProperty("appPackage")),
                Map.entry("timeout", 1000)
        ));
    }

    @AfterClass(alwaysRun = true)
    public void afterClassCleanup() {
        driver.quit();
        server.stop();
    }
}
