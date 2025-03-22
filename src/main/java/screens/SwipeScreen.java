package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverMethods;

import java.time.Duration;

public class SwipeScreen {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(accessibility = "Carousel")
    private WebElement carouselView;

    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@content-desc=\"slideTextContainer\"])[1]//android.widget.TextView[1]")
    private WebElement cardTitle;

//    new UiSelector().text("Swipe horizontal")

    public SwipeScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public boolean isDisplayed() {
       return wait.until(ExpectedConditions.visibilityOf(carouselView)).isDisplayed();
    }

    public String getFirstCardTitle() {
        return wait.until(ExpectedConditions.visibilityOf(cardTitle)).getText();
    }

    public String swipeToNextAndGetText() {
        int screenWidth = driver.manage().window().getSize().getWidth();
        int screenHeight = driver.manage().window().getSize().getHeight();
        DriverMethods.swipeByCoord(screenWidth / 8,
                screenHeight / 2 + screenHeight / 4,
                screenWidth / 2 + screenWidth / 4,
                screenHeight / 10,
                "left", 0.75, 500);
        return wait.until(ExpectedConditions.visibilityOf(cardTitle)).getText();
    }

}
