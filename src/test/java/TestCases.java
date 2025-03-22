import dataObjects.Credentials;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import utils.DriverMethods;
import utils.MyCustomListener;
import utils.TestProperties;

import java.io.File;
import java.time.Duration;
import java.util.Map;

import screens.BottomNavigation;
import screens.LoginAndSignUpScreen;
import screens.FormScreen;
import screens.SwipeScreen;

import static reports.ExtentTestManager.getTest;

@Listeners(MyCustomListener.class)
public class TestCases {

    public AppiumDriverLocalService server;
    public AndroidDriver driver;
    public WebDriverWait wait;

    public BottomNavigation bottomNavigation;
    public LoginAndSignUpScreen loginAndSignUpScreen;
    public FormScreen formScreen;
    public SwipeScreen swipeScreen;

    @BeforeClass(alwaysRun = true)
    public void beforeClassSetup() {
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

        bottomNavigation = new BottomNavigation(driver);
        loginAndSignUpScreen = new LoginAndSignUpScreen(driver);
        formScreen = new FormScreen(driver);
        swipeScreen = new SwipeScreen(driver);

    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethodSetup() {
        DriverMethods.activateApp();
    }

    @Test(testName = "Get device info")
    public void testGetDeviceInfo() {
        getTest().info(DriverMethods.getDeviceInfo());
    }

    @Test(testName = "Login with valid credentials",
        groups = {"smoke", "login"},
        description = "Login test",
        dataProvider = "valid-login-signup",
        dataProviderClass = Data.class)
    public void testLoginValidCredentials(Credentials credentials) {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed());
        getTest().info("Login: " + credentials.getEmail() + " , password: " + credentials.getPassword());
        loginAndSignUpScreen.loginUser(credentials.getEmail(), credentials.getPassword());
        Assert.assertEquals(loginAndSignUpScreen.getSuccessMessage(), "Success", "Login failed.");
    }

    @Test(testName = "Login with invalid credentials",
        groups = {"login"},
        dataProvider = "invalid-login",
        dataProviderClass = Data.class)
    public void testLoginInvalidCredentials(Credentials credentials) {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed());
        StringBuilder infoText = new StringBuilder();
        infoText.append("Login: ").append(credentials.getEmail());
        infoText.append(" , password: ").append(credentials.getPassword());
        infoText.append(" , error message: ").append(credentials.getMessage());
        getTest().info(infoText.toString());
        loginAndSignUpScreen.loginUser(credentials.getEmail(), credentials.getPassword());
        Assert.assertTrue(loginAndSignUpScreen.getFailureMessages().contains(credentials.getMessage()), "Error message not displayed.");
    }

    @Test(testName = "Sign up with valid credentials",
            groups = {"smoke", "signup"},
            dataProvider = "valid-login-signup",
            dataProviderClass = Data.class)
    public void testSignUpValidCredentials(Credentials credentials) {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed(), "Failed to open Login and Signup screen.");
        loginAndSignUpScreen.switchToSignUp();
        Assert.assertTrue(loginAndSignUpScreen.singUpViewIsDisplayed(), "Failed to switch to Sign Up view.");
        getTest().info("Login: " + credentials.getEmail() + " , password: " + credentials.getPassword());
        loginAndSignUpScreen.signUpUser(credentials.getEmail(), credentials.getPassword(), credentials.getPassword());
        Assert.assertEquals(loginAndSignUpScreen.getSuccessMessage(), "Signed Up!", "Sign up failed.");
    }

    @Test(testName = "Sign up with invalid credentials",
            groups = {"signup"},
            dataProvider = "invalid-signup",
            dataProviderClass = Data.class)
    public void testSignUpInvalidCredentials(Credentials credentials) {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed(), "Failed to open Login and Signup screen.");
        loginAndSignUpScreen.switchToSignUp();
        Assert.assertTrue(loginAndSignUpScreen.singUpViewIsDisplayed(), "Failed to switch to Sign Up view.");
        StringBuilder infoText = new StringBuilder();
        infoText.append("Login: ").append(credentials.getEmail());
        infoText.append(" , password: ").append(credentials.getPassword());
        infoText.append(" , repeat password: ").append(credentials.getRepeatPassword());
        infoText.append(" , error message: ").append(credentials.getMessage());
        getTest().info(infoText.toString());
        loginAndSignUpScreen.signUpUser(credentials.getEmail(), credentials.getPassword(), credentials.getRepeatPassword());
        Assert.assertTrue(loginAndSignUpScreen.getFailureMessages().contains(credentials.getMessage()), "Error message not displayed.");
    }

    @Test(testName = "Form screen, valid text input", groups = {"form", "smoke"})
    public void testFormValidInput() {
        String inputText = "Sample input content";
        getTest().info("Input text: " + inputText);
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        formScreen.insertText(inputText);
        String outputText = formScreen.retrieveText();
        Assert.assertEquals(outputText, inputText, "Text mismatch.");
    }

    @Test(testName = "Form screen, oversize text input")
    @Parameters("inputText")
    public void testFormInputExceedsAllowedSize(String inputText) {
        getTest().info("Input text: " + inputText);
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        formScreen.insertText(inputText);
        String outputText = formScreen.retrieveText();
        Assert.assertNotEquals(outputText, inputText, "Input failed to exceed the allowed size.");
    }

    @Test(testName = "Form screen, dropdown", groups = {"form", "dropdown", "smoke"},
            dataProvider = "dropdown-option", dataProviderClass = Data.class)
    public void testFormDropdown(String defaultOption, String option) {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        String retrievedOption = formScreen.getSelectedOption();
        Assert.assertEquals(retrievedOption, defaultOption, "Dropdown options mismatch.");
        formScreen.tapOnDropdown();
        Assert.assertTrue(formScreen.optionsDisplayed(option));
        formScreen.selectOption(option);
        retrievedOption = formScreen.getSelectedOption();
        getTest().info("Selected dropdown option: " + option);
//        TODO: checked = true
        Assert.assertEquals(retrievedOption, option, "Dropdown options mismatch.");
    }

    @Test(testName = "Swipe", groups = {"swipe"})
    public void testSwipe() {
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapSwipeIcon();
        Assert.assertTrue(swipeScreen.isDisplayed(), "Swipe Screen failed to be displayed.");
        int carouselLength = Data.expectedTitles.length;
        String[] actualTitles = new String[carouselLength];
        actualTitles[0] = swipeScreen.getFirstCardTitle();
        getTest().info("First card title: " + actualTitles[0]);
        for (int i = 1; i < carouselLength; i++) {
            actualTitles[i] = swipeScreen.swipeToNextAndGetText();
            getTest().info("Next card title: " + actualTitles[i]);
        }
        Assert.assertEquals(actualTitles, Data.expectedTitles, "Card titles mismatch.");
//        getTest().addScreenCaptureFromBase64String(DriverMethods.getScreenshot()).getModel().getMedia().get(0);

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
