import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class question2 {

	private WebDriver driver;
	private String webDriverURL = "D:\\ChromeDriver\\chromedriver.exe";
	private String baseUrl = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

	private String xpathManagerLogin = "//button[normalize-space()='Bank Manager Login']";
	private String xpathCustomerLogin = "//button[normalize-space()='Customer Login']";
	private String xpathLoginBtn = "//button[normalize-space()='Login']";
	private String xpathTransactionBtn = "//button[normalize-space()='Transactions']";
	private String xpathDeposit = "//button[normalize-space()='Deposit']";
	private String xpathWithdrawal = "//button[normalize-space()='Withdrawl']";
	private String xpathAmountInput = "//input[@placeholder='amount']";
	private String xpathSubmitBtn = "//button[@type='submit']";
	private String xpathWithdrawBtn = "//button[normalize-space()='Withdraw']";
	private String xpathHomeBtn = "//button[normalize-space()='Home']";
	private String xpathLogOutBtn = "//button[normalize-space()='Logout']";
	
	@BeforeMethod
	public void setUp() throws Exception {

		System.setProperty("webdriver.chrome.driver", webDriverURL);

		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	// create class to wait load page
	public void waitToLoad() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	// create class for find element by xpath
	private WebElement findElementByXPath(String elementName) {
		WebElement el = driver.findElement(By.xpath(elementName));

		return el;
	}

	public void ActionToDo(String tranType, String amount) throws InterruptedException {

		if (tranType.equals("Credit")) {
			findElementByXPath(xpathDeposit).click();

			waitToLoad();
			findElementByXPath(xpathAmountInput).sendKeys(amount);

			waitToLoad();
			findElementByXPath(xpathSubmitBtn).click();

		} else {
			findElementByXPath(xpathWithdrawal).click();

			Thread.sleep(1000);
			findElementByXPath(xpathAmountInput).sendKeys(amount);

			Thread.sleep(1000);
			findElementByXPath(xpathWithdrawBtn).click();
		}
	}

	public int readBalance(String acctInfo) {

		// Find the index of "Balance :"
		int balanceIndex = acctInfo.indexOf("Balance :");

		// Find the index of the next comma after "Balance :"
		int commaAfterBalance = acctInfo.indexOf(",", balanceIndex);

		// Extract the balance value substring
		String balanceSubstring = acctInfo.substring(balanceIndex + "Balance :".length(), commaAfterBalance);

		// Remove leading and trailing spaces, then convert to an integer
		int balanceValue = Integer.parseInt(balanceSubstring.trim());

		return balanceValue;
	}

	@Test
	public void main() throws Exception {

		// Open URL
		driver.get(baseUrl);

		// Navigate to Customer Login
		waitToLoad();
		findElementByXPath(xpathCustomerLogin).click();

		waitToLoad();
		WebElement userList = driver.findElement(By.id("userSelect"));
		Select user = new Select(userList);
		user.selectByVisibleText("Hermoine Granger");

		waitToLoad();
		findElementByXPath(xpathLoginBtn).click();

		waitToLoad();
		WebElement acctNumberList = driver.findElement(By.id("accountSelect"));
		Select acctNumber = new Select(acctNumberList);
		acctNumber.selectByVisibleText("1003");

		// To look account info to read balance value
		WebElement accountInfo = driver.findElement(By.xpath("(//div[@class='center'])[1]"));
		int balance = readBalance(accountInfo.getText());

		String[] amounts = { "50000", "3000", "2000", "5000", "10000", "15000", "1500" };
		String[] transactionTypes = { "Credit", "Debit", "Debit", "Credit", "Debit", "Debit", "Credit" };

		for (int i = 0; i < amounts.length; i++) {
			int amount = Integer.parseInt(amounts[i]);
			String transactionType = transactionTypes[i];

			if (transactionType.equals("Credit")) {
				balance += amount;
				ActionToDo(transactionType, amounts[i]);
			} else if (transactionType.equals("Debit")) {
				balance -= amount;
				ActionToDo(transactionType, amounts[i]);
			}

			WebElement currentAccountInfo = driver.findElement(By.xpath("(//div[@class='center'])[1]"));
			int currentBalance = readBalance(currentAccountInfo.getText());
			System.out.println("Transaction: " + transactionType + "\tAmount: " + amount + "\tExpected Balance: " + balance + "\tActual Balance: " + currentBalance);
		}
		
		waitToLoad();
		findElementByXPath(xpathLogOutBtn).click();

	}
}
