package com.newtours.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Report extends TestListenerAdapter {

	public ExtentHtmlReporter reporter;
	public ExtentReports extent;
	public ExtentTest logger;

	public void onStart(ITestContext testContext) {

		String timeformat = new SimpleDateFormat("yyyy-MM-dd ' ' HH.mm.ss").format(new Date());
		String reportformat = "test_report" + "   " + timeformat + ".html";
		// reporter.config().setTimeStampFormat("dd/mm/yyyy/HH.mm.ss");
		reporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/" + reportformat);
		reporter.loadXMLConfig("./extent-config.xml");

		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("os", "windows");
		extent.setSystemInfo("browser", "chrome");
		extent.setSystemInfo("browser", "firefox");
		// reporter.config().setTestViewChartLocation(ChartLocation.TOP);
		reporter.config().setDocumentTitle("The maven project report file");
		reporter.config().setReportName("Test Report");
		reporter.config().setTheme(Theme.DARK);

	}

	public void onTestSuccess(ITestResult tr) {
		logger = extent.createTest(tr.getName());
		logger.log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));
	}

	public void onTestSkipped(ITestResult tr) {

		logger = extent.createTest(tr.getName());
		logger.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
	}

	public void onTestFailure(ITestResult tr) {

		logger = extent.createTest(tr.getName());
		logger.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));
		String Screencapturepath = System.getProperty("user.dir") + "/screenshot/" + tr.getName() + ".png";
		File src = new File(Screencapturepath);
		if (src.exists()) {
			try {
				logger.addScreenCaptureFromPath(Screencapturepath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void onFinish(ITestContext testContext) {
		extent.flush();
	}
}
