import dataObjects.Credentials;
import org.testng.annotations.DataProvider;

public class Data {

    @DataProvider(name = "valid-login")
    public static Object[][] getValidCredentials() {
        return new Object[][]{
                {new Credentials("email@email.com", "password")}
        };
    }

    @DataProvider(name = "invalid-login")
    public static Object[][] getInValidCredentials () {
        return new Object[][]{
                {new Credentials("email", "password", "Please enter a valid email address")},
                {new Credentials("email@email.com", "p", "Please enter at least 8 characters")}
        };
    }
}
