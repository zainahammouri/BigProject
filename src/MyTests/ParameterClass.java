package MyTests;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class ParameterClass {
	
	String theWebSite = "https://automationteststore.com/";
	WebDriver driver =new EdgeDriver();
	Random rand = new Random();
	// Establish database connection to fetch required data from the users table for sign-up validation
	//For DataBase Connection 
	Connection con;
	Statement stmt;
	ResultSet rs; //for Read Query
	
	String FirstName, LastName, Email,Address,City,Country,PostlCode;
	String LogInName;
	int RandomNumber= rand.nextInt(112345675);
	String [] ArrayOfUsers = {"103","112","114","119","121","124"};
	int RandomUser = rand.nextInt(ArrayOfUsers.length);
	String SelectedUser =ArrayOfUsers[RandomUser];
	String RandomNumberForUserName =Integer.toString(RandomNumber);
	String password ="Test123!@#%%";
	boolean expectedResultForSignUpTest = true;
	boolean expectedResultForLogOutTest = true;
	String QueryToRead = "select * from customers where customerNumber="+SelectedUser;
	 Date timeStamp = new Date();
	
	public void TheSetUp() throws SQLException {
		driver.get(theWebSite);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","abc123");
	}
	
	public void ScrollAndScreenShot(int NumberToScroll, String screenShotOrder) throws IOException, InterruptedException {
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0,arguments[0])",NumberToScroll);
		
		Thread.sleep(3000);
		 TakesScreenshot ts = (TakesScreenshot)driver;
		 File Myscreenshots = ts.getScreenshotAs(OutputType.FILE);
		 String FileName = timeStamp.toString().replace(":", "-")+screenShotOrder;
		 FileUtils.copyFile(Myscreenshots, new File("src/screenShot/"+FileName+".jpg"));
	}

}
