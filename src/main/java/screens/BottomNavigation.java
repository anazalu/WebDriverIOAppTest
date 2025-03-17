package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
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

}
