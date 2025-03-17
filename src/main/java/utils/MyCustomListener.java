package utils;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import reports.ExtentManager;
import reports.ExtentTestManager;

import static reports.ExtentTestManager.getTest;
import static reports.ExtentTestManager.startTest;

public class MyCustomListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().log(Status.FAIL,
                getTest().addScreenCaptureFromBase64String(DriverMethods.getScreenshot()).getModel().getMedia().get(0)
        );
        DriverMethods.logScreenShot();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getName(), result.getTestContext().toString());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.extentReports.flush();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test passed.");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().log(Status.SKIP, "Test skipped.");
    }
}
