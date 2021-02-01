package sk.itsovy.dolinsky;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

/**
 * @author Martin Dolinsky
 */
public class Main {

	private static final String AMOUNT_ERROR_MESSAGE = "Amount must be a number between 0 and 1000000 !";
	private static final String INTEREST_ERROR_MESSAGE = "Interest must be a number between 0 and 100 !";
	private static final String AGREEMENT_ERROR_MESSAGE = "You must agree to the processing !";
	static String periodString;

	static WebElement errorMessage;
	static WebElement amount;
	static WebElement interest;
	static WebElement periodRange;
	static WebElement periodLabel;
	static WebElement taxYes;
	static WebElement taxNo;
	static WebElement agreement;
	static WebElement resetBtn;
	static WebElement calculateBtn;
	static WebElement result;

	/**
	 * Method should test various cases of calculating the income
	 *
	 * @param amountDouble   number of what amount we are going to deposit
	 * @param interestDouble annual interest that bank provides
	 * @param periodInt      number of years from 1 to 5
	 * @param tax            boolean value if we want to include tax
	 * @param agree          boolean value if we agree to processing the data
	 */
	public static void testScenario(double amountDouble, double interestDouble, int periodInt, boolean tax, boolean agree) {
		amount.sendKeys(String.valueOf(amountDouble));
		interest.sendKeys(String.valueOf(interestDouble));
		for (int i = 1; i <= periodInt - 1; i++) {
			periodRange.sendKeys(Keys.RIGHT);
		}
		if (tax) {
			taxYes.click();
		} else {
			taxNo.click();
		}
		if (agree) {
			agreement.click();
		}
		calculateBtn.click();
	}

	public static boolean isResetWorking() {
		return !errorMessage.isDisplayed()
				&& amount.getText().equals("")
				&& interest.getText().equals("")
				&& periodRange.getAttribute("value").equals("1")
				&& (String.valueOf(periodLabel.getText().charAt(periodLabel.getText().length() - 1)).equals("1")
				&& taxYes.isSelected()
				&& !taxNo.isSelected()
				&& !agreement.isSelected()
				&& !result.isDisplayed());
	}

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "/Users/martindolinsky/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();

		driver.get("http://itsovy.sk/testing/");
		driver.manage().window();

		errorMessage = driver.findElement(By.id("error"));
		amount = driver.findElement(By.id("amount"));
		interest = driver.findElement(By.id("interest"));
		periodRange = driver.findElement(By.id("period"));
		periodLabel = driver.findElement(By.id("lblPeriod"));
		taxYes = driver.findElement(By.cssSelector("input[value=\"y\"]"));
		taxNo = driver.findElement(By.cssSelector("input[value=\"n\"]"));
		agreement = driver.findElement(By.id("confirm"));
		resetBtn = driver.findElement(By.id("btnreset"));
		calculateBtn = driver.findElement(By.id("btnsubmit"));
		result = driver.findElement(By.id("result"));

		testScenario(100, 1, 5, true, true);
		Assert.assertEquals(result.getText(), "Total amount : 104.07 , net profit : 4.07");
		Assert.assertEquals(result.getCssValue("color"), "rgba(0, 100, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		Assert.assertTrue(isResetWorking());

		testScenario(100, 1, 5, false, true);
		Assert.assertEquals(result.getText(), "Total amount : 105.10 , net profit : 5.10");
		Assert.assertEquals(result.getCssValue("color"), "rgba(0, 100, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		Assert.assertTrue(isResetWorking());

		testScenario(100000, 98.07, 3, false, true);
		Assert.assertEquals(result.getText(), "Total amount : 777062.78 , net profit : 677062.78");
		Assert.assertEquals(result.getCssValue("color"), "rgba(0, 100, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		Assert.assertTrue(isResetWorking());

		testScenario(100000, 98.07, 3, true, true);
		Assert.assertEquals(result.getText(), "Total amount : 568320.69 , net profit : 468320.69");
		Assert.assertEquals(result.getCssValue("color"), "rgba(0, 100, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		Assert.assertTrue(isResetWorking());

		testScenario(1000, 101, 4, true, true);
		Assert.assertEquals(errorMessage.getText(), INTEREST_ERROR_MESSAGE);
		Assert.assertEquals(errorMessage.getCssValue("color"), "rgba(255, 0, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		//TODO: Test did not pass because error message did not disappear, please fix it ASAP
		Assert.assertTrue(isResetWorking());

		testScenario(1000, -98.07, 4, true, true);
		Assert.assertEquals(errorMessage.getText(), INTEREST_ERROR_MESSAGE);
		Assert.assertEquals(errorMessage.getCssValue("color"), "rgba(255, 0, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		//TODO: Test did not pass because error message did not disappear, please fix it ASAP
		Assert.assertTrue(isResetWorking());

		testScenario(-1000, 98.07, 3, true, true);
		Assert.assertEquals(errorMessage.getText(), AMOUNT_ERROR_MESSAGE);
		Assert.assertEquals(errorMessage.getCssValue("color"), "rgba(255, 0, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		//TODO: Test did not pass because error message did not disappear, please fix it ASAP
		Assert.assertTrue(isResetWorking());

		testScenario(100, 1, 5, false, false);
		Assert.assertEquals(errorMessage.getText(), AGREEMENT_ERROR_MESSAGE);
		Assert.assertEquals(errorMessage.getCssValue("color"), "rgba(255, 0, 0, 1)");
		Thread.sleep(3000);
		resetBtn.click();
		//TODO: Test did not pass because error message did not disappear, please fix it ASAP
		Assert.assertTrue(isResetWorking());

		driver.quit();

	}

}
