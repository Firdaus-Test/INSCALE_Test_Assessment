import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;  

public class question1 {
	
	private WebDriver driver;
	private String webDriverURL = "D:\\ChromeDriver\\chromedriver.exe";
	private String baseUrl = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";
	
	private String xpathManagerLogin = "//button[normalize-space()='Bank Manager Login']";
	private String xpathCustomerLogin = "//button[normalize-space()='Customer Login']";
	private String xpathHomeBtn = "//button[normalize-space()='Home']";
	private String xpathAddCust = "//button[normalize-space()='Add Customer']";
	private String xpathOpenAcct = "//button[normalize-space()='Open Account']";
	private String xpathCustomer = "//button[normalize-space()='Customers']";
	private String xpathFName = "//input[@placeholder='First Name']";
	private String xpathLName = " //input[@placeholder='Last Name']";
	private String xpathPostCode = "//input[@placeholder='Post Code']";
	private String xpathSubmitBtn = "//button[@type='submit']";
	
	@BeforeMethod
	public void setUp() throws Exception {
		
		System.setProperty("webdriver.chrome.driver", webDriverURL);  
		
		driver=new ChromeDriver();
		driver.manage().window().maximize();
	}
	
	//create class to wait load page
	public void waitToLoad() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}
	
	//create class for find element by xpath
	private WebElement findElementByXPath(String elementName) {
		WebElement el = driver.findElement(By.xpath(elementName));
		
		return el;
	}

	@Test
	public void main() throws Exception{
		
		//define data
		String[][] data = {
				{"Christopher","Connely","L789C349"},
				{"Frank","Christopher","A897N450"},
				{"Christopher","Minka","M098Q585"},
				{"Connely","Jackson","L789C349"},
				{"Jackson","Frank","L789C349"},
				{"Minka","Jackson","A897N450"},
				{"Jackson","Connely","L789C349"}
		};
		
		String[][] remove = {
				{"Jackson","Frank","L789C349"},	
				{"Christopher","Connely","L789C349"},
		};
		
		//open URL
	    driver.get(baseUrl);
	    
	    //Navigate to Bank Manager Login 
	    waitToLoad();
	    findElementByXPath(xpathManagerLogin).click();
	    
	    //Navigate to Add Customer 
	    waitToLoad();
	    findElementByXPath(xpathAddCust).click();
	    
	    //Loop process to add new customers
	    waitToLoad();
	    for(String[] customerInfo : data) {
	    	
	    	String firstname = customerInfo[0];
	    	String lastName = customerInfo[1];
	    	String postCode = customerInfo[2];
	    	
	    	waitToLoad();
	    	findElementByXPath(xpathFName).sendKeys(firstname);
	    	findElementByXPath(xpathLName).sendKeys(lastName);
	    	findElementByXPath(xpathPostCode).sendKeys(postCode);
	    	findElementByXPath(xpathSubmitBtn).click();
	    	
	    	//include the element to accept the alert message
	    	driver.switchTo().alert().accept();
	    }
	  
	    findElementByXPath(xpathCustomer).click();
	    
	    WebElement table = findElementByXPath("//tbody");
    	List<WebElement> rows = table.findElements(By.tagName("tr"));

	    //convert to array to a list for easier manipulation
	    ArrayList<String[]> dataList = new ArrayList<>();
	    for(String[] entry : remove) {
	    	dataList.add(entry);
	    }
	    
	    int i = 0;
	    
	    ArrayList<Integer> rowsToDelete = new ArrayList<>();

	    for (WebElement element : rows) {
	        System.out.println("Searching for " + element.getText());

	        for (String[] entry: dataList) {
	            String concatenatedValues = entry[0] + " " + entry[1] + " " + entry[2];

	            if (element.getText().contains(concatenatedValues)) {
	              System.out.println("Entry found: " + concatenatedValues);
				  
				  rowsToDelete.add(i + 1);
	            }
	          }
	        i++;
	    }
	    
		int count=0;
		// Perform actions on elements based on rowsToDelete
        for (int row : rowsToDelete) {
        	
        	WebElement deleteButton;
			if(count > 0){
				deleteButton = driver.findElement(By.xpath("(//button[@ng-click='deleteCust(cust)'][normalize-space() = 'Delete'])[" + (row - 1) + "]"));
			} else {
				deleteButton = driver.findElement(By.xpath("(//button[@ng-click='deleteCust(cust)'][normalize-space() = 'Delete'])[" + row + "]"));
			}
				count++;
            	deleteButton.click();
        }
	}
}
