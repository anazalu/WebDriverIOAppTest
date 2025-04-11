package screens;

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
import java.util.List;
import java.util.Objects;

public class FormScreen {
    private final AndroidDriver driver;
    private final WebDriverWait wait;
    public String selectedIsChecked = "Selected element has attribute \"checked\" set to true";
    public String notSelectedIsNotChecked = "Not selected element has attribute \"checked\" set to false";

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Form components\"]", priority = 1)
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Form components\")")
    private WebElement formTextView;

    @AndroidFindBy(accessibility = "text-input")
    private WebElement textInputField;

    @AndroidFindBy(accessibility = "input-text-result")
    private WebElement textOutputField;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"text_input\")")
    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"text_input\"]", priority = 1)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"icon_container\")", priority = 2)
    private WebElement dropDown;

    @AndroidFindBy(xpath = "//android.widget.CheckedTextView[@resource-id=\"android:id/text1\"]")
    protected List<WebElement> dropDownItems;

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

    public boolean dropDownOptionsDisplayed(String option) {
        WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"" + option + "\"]")));
        return optionElement.isDisplayed();
    }

    public void selectDropDownOption(String option) {
        WebElement optionElem = new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"" + option + "\"]")));
        optionElem.click();
    }

    public boolean validateItemAttributeIsChecked(String selectedOption) {
        boolean checkedAttributeSetToTrue = false;
        wait.until(ExpectedConditions.visibilityOf(dropDown)).click();
        wait.until(ExpectedConditions.visibilityOf(dropDownItems.get(0)));
        for (WebElement dropDownItem : dropDownItems) {
            if (dropDownItem.getText().equalsIgnoreCase(selectedOption)) {
                if (Objects.equals(dropDownItem.getDomAttribute("checked"), "true")) {
                    checkedAttributeSetToTrue = true;
                } else {
                    selectedIsChecked = "Error. Selected element " + dropDownItem.getText() + " has attribute \"checked\" set to false";
                }
            } else {
                if (Objects.equals(dropDownItem.getDomAttribute("checked"), "true")) {
                    notSelectedIsNotChecked = "Error. Not selected element  " + dropDownItem.getText() +" has attribute \"checked\" set to true";
                }
            }
        }
        DriverMethods.tapOnCoordinates(driver.manage().window().getSize().getWidth() / 2, driver.manage().window().getSize().getHeight() / 16);
        return checkedAttributeSetToTrue;
    }

    public String getSelectedOption() {
        return wait.until(ExpectedConditions.visibilityOf(dropDown)).getText();
    }
}
