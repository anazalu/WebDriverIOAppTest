package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FormScreen {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Form components\")")
    private WebElement formTextView;

    @AndroidFindBy(accessibility = "text-input")
//    @AndroidFindBy(id = "RNE__Input__text-input")
    private WebElement textInputField;

    @AndroidFindBy(accessibility = "input-text-result")
    private WebElement textOutputField;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"text_input\")")
//    @AndroidFindBy(id = "text_input")
    private WebElement dropDown;


    public FormScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(formTextView)).isDisplayed();
    }

    public void insertText(String text) {
        wait.until(ExpectedConditions.visibilityOf(textInputField)).clear();
        wait.until(ExpectedConditions.visibilityOf(textInputField)).sendKeys(text);
    }

    public String retrieveText() {
        return wait.until(ExpectedConditions.visibilityOf(textOutputField)).getText();
    }

    public void tapOnDropdown() {
        wait.until(ExpectedConditions.visibilityOf(dropDown)).click();
    }

    public boolean optionsDisplayed(String option) {
        WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"" + option + "\"]")));
        return optionElement.isDisplayed();
    }

    public void selectOption(String option) {
        WebElement optionElem = new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"" + option + "\"]")));
        optionElem.click();
    }

    public String getSelectedOption() {
        return wait.until(ExpectedConditions.visibilityOf(dropDown)).getText();
    }

}
