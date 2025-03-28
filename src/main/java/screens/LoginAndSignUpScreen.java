package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.LocatorGroupStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LoginAndSignUpScreen {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Login / Sign up Form\")")
    private WebElement loginScreenTextView;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Email\")", priority = 1)
    @AndroidFindBy(accessibility = "input-email")
    private WebElement emailField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc=\"input-password\"]")
    private WebElement passwordField;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"LOGIN\")")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[preceding-sibling::android.view.ViewGroup[android.widget.EditText[@content-desc=\"input-email\"]]]")
    private List<WebElement> messages;

    @AndroidFindBy(id = "android:id/alertTitle")
    private WebElement successMessage;

    @AndroidFindBy(accessibility = "button-sign-up-container")
    private WebElement signUpToggle;

    @AndroidFindBy(accessibility = "input-repeat-password")
    private WebElement repeatPasswordField;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"button-SIGN UP\")")
    private WebElement signUpButton;


    public LoginAndSignUpScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(5));
    }

    public boolean isDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(loginScreenTextView)).isDisplayed();
    }

    public void switchToSignUp() {
        wait.until(ExpectedConditions.visibilityOf(signUpToggle)).click();
    }

    public boolean singUpViewIsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(repeatPasswordField)).isDisplayed();
    }

    public void loginUser(String email, String password) {
        wait.until(ExpectedConditions.visibilityOf(emailField)).clear();
        wait.until(ExpectedConditions.visibilityOf(emailField)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOf(loginButton)).click();
    }

    public void signUpUser(String email, String password, String repeatPassword) {
        wait.until(ExpectedConditions.visibilityOf(emailField)).clear();
        wait.until(ExpectedConditions.visibilityOf(emailField)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOf(repeatPasswordField)).clear();
        wait.until(ExpectedConditions.visibilityOf(repeatPasswordField)).sendKeys(repeatPassword);
        wait.until(ExpectedConditions.visibilityOf(signUpButton)).click();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
    }

    public List<String> getFailureMessages() {
        wait.until(ExpectedConditions.visibilityOf(messages.get(1)));
        List<String> messageTexts = new ArrayList<>();
        for (WebElement message : messages) {
            messageTexts.add(message.getText());
        }
        return messageTexts;
    }

}
