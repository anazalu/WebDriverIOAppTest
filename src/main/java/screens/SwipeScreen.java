package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SwipeScreen {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(accessibility = "Carousel")
    private WebElement carouselView;

//    new UiSelector().text("Swipe horizontal")

    public SwipeScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public void isDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(carouselView));
    }

}
