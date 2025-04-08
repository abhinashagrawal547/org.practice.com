package stepDefinitions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import io.cucumber.java.en.Given;
import pageObjects.PracticePage;

public class PracticeSteps {
	
	WebDriver driver;
	PracticePage practicePage;
	
	public PracticeSteps()
	{
		driver = new ChromeDriver();
	    driver.manage().window().maximize();
		practicePage = new PracticePage(driver);
	}
	
	@Given("User have the url")
	public void user_have_the_url() {
		System.out.println("User is in User have the url. Number of elements with google image");
	}
	
	@Given("User open it")
	public void user_open_it() throws InterruptedException, AWTException {
		driver.get("https://www.hyrtutorials.com/p/window-handles-practice.html");
		
		String OrigWindow = driver.getWindowHandle();
		
		JavascriptExecutor exec = (JavascriptExecutor)driver;
		exec.executeScript("window.scrollBy(0,600);");
		
		FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
		fluentWait.pollingEvery(Duration.ofSeconds(1));
		fluentWait.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='newWindowBtn']")));
		
		WebElement elem = driver.findElement(By.xpath("//button[@id='newWindowBtn']"));
		elem.click();
		
		Set<String> windowsSet = driver.getWindowHandles();
		for(String win :windowsSet)
		{
			driver.switchTo().window(win);
			System.out.println(driver.getCurrentUrl());
			if(driver.getCurrentUrl().contains("basic-controls.html"))
			{
				System.out.println("found the other window");
				Robot r = new Robot();
				r.keyPress(KeyEvent.VK_ALT);
				r.keyPress(KeyEvent.VK_ENTER);
				r.keyPress(KeyEvent.VK_F4);
				r.keyPress(KeyEvent.VK_ENTER);
				r.keyRelease(KeyEvent.VK_ALT);
				r.keyRelease(KeyEvent.VK_F4);
				
				System.out.println("closed the other window");
				driver.switchTo().window(OrigWindow);
			}
		}
		
		Set<String> countOfOpenWindows = driver.getWindowHandles();
		Assert.assertTrue(countOfOpenWindows.size()==1);
		
		driver.close();
	}
	
	@Given("User read excel")
	public void abcd() throws IOException
	{
		FileInputStream read = new FileInputStream("C:\\Users\\it200\\Downloads\\TestData.xlsx");
		Workbook wb = new XSSFWorkbook(read); 
		Sheet sheet = wb.getSheet("Sheet1");
		
		
//		double a = sheet.getRow(1).getCell(2).getNumericCellValue();
//		System.out.println("value of a = "+a);
		
		for(Row row:sheet)
		{
			for(Cell cell: row)
			{
				switch (cell.getCellType()) {
				case NUMERIC:
					System.out.print(cell.getNumericCellValue() + "\t");
					break;
				case STRING:
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				default:
					break;
				}
			}
			System.out.println(); // New line after each row
		}
		
		driver.quit();
	}
	
	@Given("User connects to db to have data check")
	public void abcd2() throws SQLException
	{
		String connStr = "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
		Connection conn = DriverManager.getConnection(connStr);
		Statement statement = conn.createStatement();
		
		String query = "select * from practice";
		ResultSet resultSet = statement.executeQuery(query);
		
		while(resultSet.next())
		{
			System.out.println("Value getting printed = "+ resultSet.getInt("firstname"));
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
}