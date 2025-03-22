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

public class BottomNavigation {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Home\")")
    private WebElement homeIcon;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Login\"]")
    private WebElement loginIcon;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(accessibility = "Forms")
    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Forms\")", priority = 1)
    private WebElement formsIcon;

    @AndroidFindBy(accessibility = "Swipe")
    private WebElement swipeIcon;

    public BottomNavigation(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(5));
    }

    public boolean isDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(homeIcon)).isDisplayed();
    }

    public void tapLoginIcon() {
        wait.until(ExpectedConditions.visibilityOf(loginIcon)).click();
    }

    public void tapFormsIcon() {
        wait.until(ExpectedConditions.visibilityOf(formsIcon)).click();
    }

    public void tapSwipeIcon() {
        wait.until(ExpectedConditions.visibilityOf(swipeIcon)).click();
    }

}
