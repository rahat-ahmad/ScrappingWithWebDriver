package io.naztech.scrappingwithwebdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverInitializer {
	
	public WebDriver initialize() {
		System.setProperty("webdriver.chrome.driver","C:\\Users\\rahat.ahmad\\Desktop\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		return driver;
	}
	

}
