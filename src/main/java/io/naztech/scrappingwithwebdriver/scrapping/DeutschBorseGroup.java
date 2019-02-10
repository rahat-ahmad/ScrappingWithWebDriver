package io.naztech.scrappingwithwebdriver.scrapping;

import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.naztech.scrappingwithwebdriver.WebDriverInitializer;

public class DeutschBorseGroup {
	
	WebDriver driver = new WebDriverInitializer().initialize();
	String url= "https://career.deutsche-boerse.com/search/?q=&sortColumn=referencedate&sortDirection=desc";
	private static List<WebElement> pageNumber = new ArrayList<WebElement>();
	
	
	
	public DeutschBorseGroup() {
		pageNumber = driver.findElements(By.xpath("//ul[@class=\"pagination\"]"));
	}

	
	
	
	
	
	
}
