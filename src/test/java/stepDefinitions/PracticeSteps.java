package stepDefinitions;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
	public void user_open_it() {
		driver.get("https://www.google.com");
		int numberOfGoogleImages = practicePage.FindCountOfGoogleImage();
		
		Assert.assertTrue("Login page not populated correctly. ", numberOfGoogleImages > 0);
		System.out.println("User is in User open it. Number of elements with google image = "+ numberOfGoogleImages);
		
		driver.quit();
	}
}
