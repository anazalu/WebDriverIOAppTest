package utils;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties {
    private static Properties props = new Properties();
    private static String propsPath = "src/main/resources/config.properties";
    private static DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    public static void loadProperties() {
        try {
            InputStream input = new FileInputStream(propsPath);
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String name) {
        return props.getProperty(name);
    }

    public static DesiredCapabilities getDesiredCapabilities() {
        desiredCapabilities.setCapability("platformName", getProperty("platformName"));
        desiredCapabilities.setCapability("appium:automationName", getProperty("automationName"));
        desiredCapabilities.setCapability("appium:appPackage", getProperty("appPackage"));
        desiredCapabilities.setCapability("appium:appActivity", getProperty("appActivity"));
        desiredCapabilities.setCapability("appium:autoLaunch", getProperty("autoLaunch"));
        desiredCapabilities.setCapability("appium:noReset", getProperty("noReset"));

        return desiredCapabilities;
    }
}
