package MyTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class tests {
	
	String theWebSite = "https://automationteststore.com/";
	WebDriver driver =new ChromeDriver();
	Random rand = new Random();
	// Establish database connection to fetch required data from the users table for sign-up validation
	//For DataBase Connection 
	Connection con;
	Statement stmt;
	ResultSet rs; //for Read Query
	
	String FirstName, LastName, Email,Address,City,Country,PostlCode;
	String LogInName;
	int RandomNumber= rand.nextInt(112345675);
	String RandomNumberForUserName =Integer.toString(RandomNumber);
	String password ="Test123!@#%%";
	boolean expectedResultForSignUpTest = true;
	boolean expectedResultForLogOutTest = true;
	
	@BeforeTest
	public void Setup() throws SQLException {
		driver.get(theWebSite);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","abc123");
	}
	
	/**
	 * @throws SQLException
	 */
	@Test(priority = 1)
	public void SignUpTest() throws SQLException {
		WebElement logInOrRigesterBtn = driver.findElement(By.linkText("Login or register"));
		logInOrRigesterBtn.click();
		WebElement continueBtnToSignUp = driver.findElement(By.xpath("//button[@title='Continue']"));
		continueBtnToSignUp.click();
		
		//SignUp Form (to create Acc)
		// Fill in the mandatory fields in the sign-up form
		
		WebElement firstNameInput = driver.findElement(By.xpath("//input[@id='AccountFrm_firstname']"));
		WebElement lastNameInput = driver.findElement(By.xpath("//input[@id='AccountFrm_lastname']"));
		WebElement emailInput = driver.findElement(By.xpath("//input[@id='AccountFrm_email']"));
		WebElement addressInput = driver.findElement(By.xpath("//input[@id='AccountFrm_address_1']"));
		WebElement cityInput = driver.findElement(By.xpath("//input[@id='AccountFrm_city']"));
		WebElement country = driver.findElement(By.xpath("//select[@id='AccountFrm_country_id']"));
		WebElement zone = driver.findElement(By.xpath("//select[@id='AccountFrm_zone_id']"));
		WebElement postcodeInput = driver.findElement(By.xpath("//input[@id='AccountFrm_postcode']"));
		WebElement loginNameInput = driver.findElement(By.xpath("//input[@id='AccountFrm_loginname']"));
		WebElement passwordInput = driver.findElement(By.xpath("//input[@id='AccountFrm_password']"));
		WebElement confirmPasswordInput = driver.findElement(By.xpath("//input[@id='AccountFrm_confirm']"));
		WebElement PolicyAgreementBtn = driver.findElement(By.xpath("//input[@id='AccountFrm_agree']"));
		WebElement CountnieBtn = driver.findElement(By.xpath("//button[@title='Continue']"));
		
		String [] ArrayOfUsers = {"103","112","114","119","121","124"};
		int RandomUser = rand.nextInt(ArrayOfUsers.length);
		String SelectedUser =ArrayOfUsers[RandomUser];
		
		String QueryToRead = "select * from customers where customerNumber="+SelectedUser;
		stmt = con.createStatement();
		rs = stmt.executeQuery(QueryToRead);
		
		// Retrieve the data from the database to fill in the mandatory fields in the sign-up form
		while(rs.next()) {
			FirstName = rs.getString("contactFirstName").trim();
			LastName = rs.getString("contactLastName").trim();
			Email = FirstName+LastName+rand.nextInt(2345632)+"@gmail.com";
			Address = rs.getString("addressLine1");
			City = rs.getString("city");
			Country = rs.getString("country");
			PostlCode = rs.getString("postalCode");
			}
		firstNameInput.sendKeys(FirstName);
		lastNameInput.sendKeys(LastName);
		emailInput.sendKeys(Email);
		addressInput.sendKeys(Address);
		cityInput.sendKeys(City);
		
		// UI issue: country must be selected first in order to enable and determine the region
		Select countrySelect = new Select(country);
		countrySelect.getFirstSelectedOption().click();
		
		Select RegionSelect = new Select (zone);
		RegionSelect.selectByIndex(3);
		
		postcodeInput.sendKeys(PostlCode);
		
		LogInName = FirstName+LastName+RandomNumberForUserName;
		loginNameInput.sendKeys(LogInName);
		passwordInput.sendKeys(password);
		confirmPasswordInput.sendKeys(password);
		PolicyAgreementBtn.click();
		CountnieBtn.click();
		
		Assert.assertEquals(driver.getPageSource().contains("Welcome back"), expectedResultForSignUpTest);
		
		WebElement countinueBtnInWelcomePage = driver.findElement(By.cssSelector(".btn.btn-default.mr10"));
		countinueBtnInWelcomePage.click();
		
	}
	@Test(priority = 2)
	public void LogOutTest() {
		driver.navigate().to("https://automationteststore.com/index.php?rt=account/logout");
		Assert.assertEquals(driver.getPageSource().contains(" Account Logout"),expectedResultForLogOutTest);
	}
	
	@Test(priority = 3)
	public void LogInTest() {
		driver.navigate().to("https://automationteststore.com/index.php?rt=account/login");
		WebElement UserNameInput = driver.findElement(By.id("loginFrm_loginname"));
		WebElement passwordInput = driver.findElement(By.id("loginFrm_password"));
		WebElement LoginBtn = driver.findElement(By.xpath("//button[@title='Login']"));
		
		UserNameInput.sendKeys(LogInName);
		passwordInput.sendKeys(password);
		LoginBtn.click();
		

		
	}
	@AfterTest
	public void AfterFinishedTheTest() {}

}
