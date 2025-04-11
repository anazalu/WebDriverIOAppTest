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
import screens.DragScreen;

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
    public DragScreen dragScreen;

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
        dragScreen = new DragScreen(driver);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethodSetup() {
        DriverMethods.activateApp();
    }

    @Test(testName = "Login with valid credentials",
        groups = {"smoke", "login", "TC1"},
        description = "Login test",
        dataProvider = "valid-login-signup",
        dataProviderClass = Data.class)
    public void testLoginValidCredentials(Credentials credentials) {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed());
        getTest().info("Login: " + credentials.getEmail() + ", password: " + credentials.getPassword());
        loginAndSignUpScreen.loginUser(credentials.getEmail(), credentials.getPassword());
        Assert.assertEquals(loginAndSignUpScreen.getSuccessMessage(), "Success", "Login failed.");
    }

    @Test(testName = "Login with invalid credentials",
        groups = {"login", "TC1"},
        dataProvider = "invalid-login",
        dataProviderClass = Data.class)
    public void testLoginInvalidCredentials(Credentials credentials) {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed());
        StringBuilder infoText = new StringBuilder();
        infoText.append("Login: ").append(credentials.getEmail());
        infoText.append(", password: ").append(credentials.getPassword());
        infoText.append(", error message: ").append(credentials.getMessage());
        getTest().info(infoText.toString());
        loginAndSignUpScreen.loginUser(credentials.getEmail(), credentials.getPassword());
        Assert.assertTrue(loginAndSignUpScreen.getFailureMessages().contains(credentials.getMessage()), "Error message not displayed.");
    }

    @Test(testName = "Sign up with valid credentials",
            groups = {"smoke", "signup", "TC1"},
            dataProvider = "valid-login-signup",
            dataProviderClass = Data.class)
    public void testSignUpValidCredentials(Credentials credentials) {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed(), "Failed to open Login and Signup screen.");
        loginAndSignUpScreen.switchToSignUp();
        Assert.assertTrue(loginAndSignUpScreen.singUpViewIsDisplayed(), "Failed to switch to Sign Up view.");
        getTest().info("Login: " + credentials.getEmail() + ", password: " + credentials.getPassword());
        loginAndSignUpScreen.signUpUser(credentials.getEmail(), credentials.getPassword(), credentials.getPassword());
        Assert.assertEquals(loginAndSignUpScreen.getSuccessMessage(), "Signed Up!", "Sign up failed.");
    }

    @Test(testName = "Sign up with invalid credentials",
            groups = {"signup", "TC1"},
            dataProvider = "invalid-signup",
            dataProviderClass = Data.class)
    public void testSignUpInvalidCredentials(Credentials credentials) {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapLoginIcon();
        Assert.assertTrue(loginAndSignUpScreen.isDisplayed(), "Failed to open Login and Signup screen.");
        loginAndSignUpScreen.switchToSignUp();
        Assert.assertTrue(loginAndSignUpScreen.singUpViewIsDisplayed(), "Failed to switch to Sign Up view.");
        StringBuilder infoText = new StringBuilder();
        infoText.append("Login: ").append(credentials.getEmail());
        infoText.append(", password: ").append(credentials.getPassword());
        infoText.append(", repeat password: ").append(credentials.getRepeatPassword());
        infoText.append(", error message: ").append(credentials.getMessage());
        getTest().info(infoText.toString());
        loginAndSignUpScreen.signUpUser(credentials.getEmail(), credentials.getPassword(), credentials.getRepeatPassword());
        Assert.assertTrue(loginAndSignUpScreen.getFailureMessages().contains(credentials.getMessage()), "Error message not displayed.");
    }

    @Test(testName = "Form screen, valid text input", groups = {"TC2", "form", "smoke"})
    public void testFormValidInput() {
        getTest().info(DriverMethods.getDeviceInfo());
        String inputText = "Sample input content";
        getTest().info("Input text: " + inputText);
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        formScreen.insertText(inputText);
        String outputText = formScreen.retrieveText();
        Assert.assertEquals(outputText, inputText, "Text mismatch.");
    }

    @Test(testName = "Form screen, oversize text input", groups = {"TC2", "form"})
    @Parameters("inputText")
    public void testFormInputExceedsAllowedSize(String inputText) {
        getTest().info(DriverMethods.getDeviceInfo());
        getTest().info("Input text: " + inputText);
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        formScreen.insertText(inputText);
        String outputText = formScreen.retrieveText();
        getTest().info("Output text: " + outputText);
        Assert.assertNotEquals(outputText, inputText, "Input failed to exceed the allowed size.");
    }

    @Test(testName = "Form screen, dropdown", groups = {"form", "dropdown", "smoke", "TC2"},
            dataProvider = "dropdown-option", dataProviderClass = Data.class)
    public void testFormDropdown(String defaultOption, String selectedOption) {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapFormsIcon();
        Assert.assertTrue(formScreen.isDisplayed());
        String retrievedOption = formScreen.getSelectedOption();
        Assert.assertEquals(retrievedOption, defaultOption, "Default dropdown item is not displayed.");
        formScreen.tapOnDropdown();
        Assert.assertTrue(formScreen.dropDownOptionsDisplayed(selectedOption));
        formScreen.selectDropDownOption(selectedOption);

        Assert.assertTrue(formScreen.validateItemAttributeIsChecked(selectedOption), "Error. For a selected dropdown item, attribute \"checked\" is not set to true.");
        getTest().info(formScreen.selectedIsChecked);
        getTest().info(formScreen.notSelectedIsNotChecked);

        retrievedOption = formScreen.getSelectedOption();
        getTest().info("Selected dropdown option: " + selectedOption);
        Assert.assertEquals(retrievedOption, selectedOption, "Selected dropdown item is not displayed.");
    }

    @Test(testName = "Swipe", groups = {"swipe", "smoke", "TC3"})
    public void testSwipe() {
        getTest().info(DriverMethods.getDeviceInfo());
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
        getTest().addScreenCaptureFromBase64String(DriverMethods.getScreenshot()).getModel().getMedia().get(0);
    }

    @Test(testName = "Drag and drop", groups = {"drag", "smoke", "TC4"})
    public void testDragAndDrop() {
        getTest().info(DriverMethods.getDeviceInfo());
        Assert.assertTrue(bottomNavigation.isDisplayed());
        bottomNavigation.tapDragIcon();
        Assert.assertTrue(dragScreen.isDisplayed());
        dragScreen.dragAndDropAllPieces();
        Assert.assertTrue(dragScreen.congratulationsTextIsDisplayed());
        Assert.assertTrue(dragScreen.retryButtonIsDisplayed());
        getTest().info("Puzzle solved");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodCleanup() {
        driver.executeScript("mobile: terminateApp", Map.ofEntries(
                Map.entry("appId", "com.wdiodemoapp"),
                Map.entry("timeout", 1000)
        ));
    }

    @AfterClass(alwaysRun = true)
    public void afterClassCleanup() {
        driver.quit();
        server.stop();
    }
}
