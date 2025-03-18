import dataObjects.Credentials;
import org.testng.annotations.DataProvider;

public class Data {

    @DataProvider(name = "valid-credentials")
    public static Object[][] getValidCredentials() {
        return new Object[][] {
                {new Credentials("email@email.com", "password")}
        };
    }

}
