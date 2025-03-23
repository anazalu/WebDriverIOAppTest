package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.LocatorGroupStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverMethods;

import java.time.Duration;

public class DragScreen {
    AndroidDriver driver;
    WebDriverWait wait;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Drag and Drop\")")
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Drag and Drop\"]", priority = 1)
    private WebElement titleTextView;

    public DragScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(3));
    }

    public boolean isDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(titleTextView)).isDisplayed();
    }

    public void dragAndDropAllPieces() {
        int screenWidth = driver.manage().window().getSize().getWidth();
        int screenHeight = driver.manage().window().getSize().getHeight();
        int widthL = screenWidth / 2 - screenWidth / 5;
        int widthC = screenWidth / 2;
        int widthR = screenWidth / 2 + screenWidth / 5;
        int height1 = screenHeight / 4;
        int height2 = screenHeight / 4 + screenHeight / 10;
        int height3 = screenHeight / 4 + screenHeight / 5;
        int[] widths = {
                widthL, widthL, widthL,
                widthC, widthC, widthC,
                widthR, widthR, widthR
        };
        int[] heights = {
                height1, height2, height3,
                height1, height2, height3,
                height1, height2, height3
        };
        String[] elemIds = {
                "drag-l1", "drag-l2", "drag-l3",
                "drag-c1", "drag-c2", "drag-c3",
                "drag-r1", "drag-r2", "drag-r3"
        };

        for (int i = 0; i < widths.length; i++) {
            String ID = elemIds[i];
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId(ID)));
            DriverMethods.dragElemToCoord(element, widths[i], heights[i], 2000);
        }
    }
}
