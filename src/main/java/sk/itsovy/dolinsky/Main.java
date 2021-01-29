package sk.itsovy.dolinsky;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @author Martin Dolinsky
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "/Users/martindolinsky/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();

		driver.get("http://itsovy.sk/testing/");
		driver.manage().window();

		WebElement errorMessage = driver.findElement(By.id("error"));
		WebElement amount = driver.findElement(By.id("amount"));
		WebElement interest = driver.findElement(By.id("interest"));
		WebElement periodRange = driver.findElement(By.id("period"));
		WebElement periodLabel = driver.findElement(By.id("lblPeriod"));
		WebElement taxYes = driver.findElement(By.cssSelector("input[value=\"y\"]"));
		WebElement taxNo = driver.findElement(By.cssSelector("input[value=\"n\"]"));
		WebElement agreement = driver.findElement(By.id("confirm"));
		WebElement resetBtn = driver.findElement(By.id("btnreset"));
		WebElement calculateBtn = driver.findElement(By.id("btnsubmit"));
		WebElement result = driver.findElement(By.id("result"));

		Random rnd = new Random();
		String colorRed = "rgba(255, 0, 0, 1)";
		String colorGreen = "rgba(0, 100, 0, 1)";
		String amountErrorMsg = "Amount must be a number between 0 and 1000000 !";
		String interestErrorMsg = "Interest must be a number between 0 and 100 !";
		String agreementErrorMsg = "You must agree to the processing !";
		String periodString;


		//default state
		Assert.assertFalse(errorMessage.isDisplayed());
		Assert.assertEquals(amount.getText(), "");
		Assert.assertEquals(interest.getText(), "");
		Assert.assertEquals(periodRange.getAttribute("value"), "1");
		periodString = periodLabel.getText();
		Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), "1");
		Assert.assertTrue(taxYes.isSelected());
		Assert.assertFalse(taxNo.isSelected());
		Assert.assertFalse(agreement.isSelected());
		Assert.assertFalse(result.isDisplayed());

		//nothing fulfilled
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg + "\n" + agreementErrorMsg);

		//amount and interest not fulfilled
		agreement.click();
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg);
		resetBtn.click();

		//interest not fulfilled and not checked agreement
		amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), interestErrorMsg + "\n" + agreementErrorMsg);
		resetBtn.click();

		//negative amount
		amount.sendKeys("-" + rnd.nextInt(1000000));
		interest.sendKeys(String.valueOf(rnd.nextInt(100)));
		agreement.click();
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
		resetBtn.click();

		//amount more than 1 000 000
		amount.sendKeys(String.valueOf(rnd.nextInt(9000000)+1000000));
		interest.sendKeys(String.valueOf(rnd.nextInt(100)));
		agreement.click();
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
		resetBtn.click();

		//negative interest
		amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
		interest.sendKeys("-" + rnd.nextInt(100));
		agreement.click();
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
		resetBtn.click();

		//interest more than 100
		amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
		interest.sendKeys(String.valueOf(rnd.nextInt(900)+100));
		agreement.click();
		calculateBtn.click();
		Assert.assertTrue(errorMessage.isDisplayed());
		Assert.assertEquals(errorMessage.getCssValue("color"), colorRed);
		Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
		resetBtn.click();

		//tax radio button
		Assert.assertTrue(taxYes.isSelected());
		Assert.assertFalse(taxNo.isSelected());
		taxNo.click();
		Assert.assertFalse(taxYes.isSelected());
		Assert.assertTrue(taxNo.isSelected());
		resetBtn.click();

		driver.quit();

	}

}
